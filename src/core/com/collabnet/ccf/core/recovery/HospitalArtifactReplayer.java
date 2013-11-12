/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.core.recovery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.spring.SpringAdaptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.hospital.HospitalEntry;

// FIXME What about successfully replayed artifacts?
// FIXME Use another user for the replay script to force resync?
// FIXME How to replay just a set of artifacts
// FIXME Should we include the entity service in front of the source component
// to avoid duplicates?
public class HospitalArtifactReplayer extends SpringAdaptor {
    public static final String           HOSPITAL                  = "-hospital";
    // FIXME Should this constant be configurable?
    public static final String           REPLAY_WORK_DIR           = "logs/replay/work";
    private DocumentBuilder              builder                   = null;
    HospitalEntry                        entry                     = null;
    private File                         replayWorkDir             = null;
    private static HashMap<String, File> componentConfigFileMap    = new HashMap<String, File>();
    private static final Log             log                       = LogFactory
                                                                           .getLog(HospitalArtifactReplayer.class);
    public static final String           OPENADAPTOR_SPRING_CONFIG = ".openadaptor-spring.xml";

    public HospitalArtifactReplayer(HospitalEntry entry) {
        this.entry = entry;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try {
            builder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        replayWorkDir = new File(REPLAY_WORK_DIR);
        if (!replayWorkDir.exists()) {
            boolean isDirectoryCreated = replayWorkDir.mkdirs();
            if (!isDirectoryCreated) {
                String message = "Could not create working directory for the replay script";
                log.error(message);
                throw new CCFRuntimeException(message);
            }
        }
    }

    protected void loadBeanDefinitions(String url,
            GenericApplicationContext context) {
        if (url.endsWith(OPENADAPTOR_SPRING_CONFIG)) {
            url = "classpath:" + "org/openadaptor/spring/"
                    + OPENADAPTOR_SPRING_CONFIG;
        }
        String protocol = "";
        if (url.indexOf(':') != -1) {
            protocol = url.substring(0, url.indexOf(':'));
        }

        if (protocol.equals("file") || protocol.equals("http")) {
            loadBeanDefinitionsFromUrl(url, context);
        } else if (protocol.equals("classpath")) {
            loadBeanDefinitionsFromClasspath(url, context);
        } else {
            loadBeanDefinitions("file:" + url, context);
        }
    }

    private void addGAImportComponents(Document document, Element routerElement) {
        NodeList propertyNodes = routerElement.getElementsByTagName("map");
        Element mapElement = (Element) propertyNodes.item(0);
        Element fileReaderEntry = document.createElement("entry");
        fileReaderEntry.setAttribute("key-ref", "FileReader");
        fileReaderEntry.setAttribute("value-ref",
                "GenericArtifactMultiLineParser");
        Element gaParserEntry = document.createElement("entry");
        gaParserEntry.setAttribute("key-ref", "GenericArtifactMultiLineParser");
        gaParserEntry.setAttribute("value-ref", entry.getSourceComponent());
        mapElement.appendChild(fileReaderEntry);
        mapElement.appendChild(gaParserEntry);

        Element documentElement = document.getDocumentElement();
        Element fileReaderElement = document.createElement("bean");
        fileReaderElement.setAttribute("id", "FileReader");
        fileReaderElement
                .setAttribute("class",
                        "org.openadaptor.auxil.connector.iostream.reader.FileReadConnector");
        Element fileProperty = document.createElement("property");
        fileProperty.setAttribute("name", "filename");
        fileProperty.setAttribute("value", entry.getDataFile()
                .getAbsolutePath());
        fileReaderElement.appendChild(fileProperty);
        documentElement.appendChild(fileReaderElement);

        Element gaParserElement = document.createElement("bean");
        gaParserElement.setAttribute("id", "GenericArtifactMultiLineParser");
        gaParserElement.setAttribute("class",
                "com.collabnet.ccf.core.utils.GenericArtifactMultiLineParser");

        documentElement.appendChild(gaParserElement);

        NodeList allBeans = document.getElementsByTagName("bean");
        String exceptionProcessorComponentName = null;
        for (int i = 0; i < allBeans.getLength(); i++) {
            Node beanNode = allBeans.item(i);
            NamedNodeMap atts = beanNode.getAttributes();
            Node beanIdNode = atts.getNamedItem("id");
            String beanId = beanIdNode.getNodeValue();
            // FIXME What happens if the router component is not called router
            if (beanId.equals("Router")) {
                NodeList routerPropertyNodes = ((Element) beanNode)
                        .getElementsByTagName("property");
                for (int j = 0; j < routerPropertyNodes.getLength(); j++) {
                    Node propNode = routerPropertyNodes.item(j);
                    NamedNodeMap routerPropAtts = propNode.getAttributes();
                    Node nameNode = routerPropAtts.getNamedItem("name");
                    String routerProperty = nameNode.getNodeValue();
                    if (routerProperty.equals("exceptionProcessor")) {
                        Node expProcessorNode = routerPropAtts
                                .getNamedItem("ref");
                        exceptionProcessorComponentName = expProcessorNode
                                .getNodeValue();
                    }
                }
            } else if (beanId.equals(exceptionProcessorComponentName)) {
                NodeList exceptionPropertyNodes = ((Element) beanNode)
                        .getElementsByTagName("property");
                for (int j = 0; j < exceptionPropertyNodes.getLength(); j++) {
                    Node propertyNode = exceptionPropertyNodes.item(j);
                    NamedNodeMap excepPropAtts = propertyNode.getAttributes();
                    Node nameNode = excepPropAtts.getNamedItem("name");
                    String propertyName = nameNode.getNodeValue();
                    if (propertyName.equals("hospitalFileName")) {
                        Node valueNode = excepPropAtts.getNamedItem("value");
                        valueNode.setNodeValue(replayWorkDir.getAbsolutePath()
                                + File.separator + "hospital.txt");
                    } else if (propertyName.equals("artifactsDirectory")) {
                        Node valueNode = excepPropAtts.getNamedItem("value");
                        valueNode.setNodeValue(replayWorkDir.getAbsolutePath()
                                + File.separator + "artifacts");
                    }
                }
            }
        }
    }

    private Element getRouterElement(Document document) {
        NodeList beanNodesList = document.getElementsByTagName("bean");
        Node routerNode = null;
        for (int i = 0; i < beanNodesList.getLength(); i++) {
            Node node = beanNodesList.item(i);
            NamedNodeMap nodeMap = node.getAttributes();
            Node idNode = nodeMap.getNamedItem("id");
            String id = idNode.getNodeValue();
            // FIXME What happens if the router is not called router?
            if (id.equals("Router")) {
                routerNode = node;
            }
        }
        return (Element) routerNode;
    }

    private void loadBeanDefinitionsFromClasspath(String url,
            GenericApplicationContext context) {
        String resourceName = url.substring(url.indexOf(':') + 1);
        BeanDefinitionReader reader = null;
        if (url.endsWith(".xml")) {
            reader = new XmlBeanDefinitionReader(context);
        } else if (url.endsWith(".properties")) {
            reader = new PropertiesBeanDefinitionReader(context);
        }

        if (reader != null) {
            reader.loadBeanDefinitions(new ClassPathResource(resourceName));
        } else {
            throw new RuntimeException(
                    "No BeanDefinitionReader associated with " + url);
        }
    }

    @SuppressWarnings("deprecation")
    private void loadBeanDefinitionsFromUrl(String url,
            GenericApplicationContext context) {
        BeanDefinitionReader reader = null;
        if (url.endsWith(".xml")) {
            reader = new XmlBeanDefinitionReader(context);
        } else if (url.endsWith(".properties")) {
            reader = new PropertiesBeanDefinitionReader(context);
        }

        if (reader != null) {
            try {
                UrlResource urlResource = new UrlResource(url);
                InputStream is = urlResource.getInputStream();
                Document document = builder.parse(is);
                Element routerElement = this.getRouterElement(document);
                this.stripOffProcessors(routerElement);
                this.addGAImportComponents(document, routerElement);
                DOMImplementationRegistry registry = null;
                try {
                    registry = DOMImplementationRegistry.newInstance();
                } catch (ClassCastException e) {
                    log.error("error", e);
                    throw new CCFRuntimeException("error", e);
                } catch (ClassNotFoundException e) {
                    log.error("error", e);
                    throw new CCFRuntimeException("error", e);
                } catch (InstantiationException e) {
                    log.error("error", e);
                    throw new CCFRuntimeException("error", e);
                } catch (IllegalAccessException e) {
                    log.error("error", e);
                    throw new CCFRuntimeException("error", e);
                }
                String originalConfigFileAbsolutePath = urlResource.getFile()
                        .getAbsolutePath();
                String componentName = entry.getSourceComponent();
                String configComponentIdentifier = "{"
                        + originalConfigFileAbsolutePath + "}" + componentName;

                File outputFile = null;
                if (componentConfigFileMap
                        .containsKey(configComponentIdentifier)) {
                    outputFile = componentConfigFileMap
                            .get(configComponentIdentifier);
                } else {
                    outputFile = File.createTempFile(componentName, ".xml",
                            replayWorkDir);
                    DOMImplementationLS impl = (DOMImplementationLS) registry
                            .getDOMImplementation("LS");
                    LSSerializer writer = impl.createLSSerializer();
                    LSOutput output = impl.createLSOutput();
                    FileOutputStream bas = new FileOutputStream(
                            outputFile.getAbsolutePath());
                    output.setByteStream(bas);
                    writer.write(document, output);
                    bas.flush();
                    bas.close();
                    componentConfigFileMap.put(configComponentIdentifier,
                            outputFile);
                }

                // FIXME Use of deprecated method
                UrlResource newUrlResource = new UrlResource(outputFile.toURL()
                        .toString());
                ((XmlBeanDefinitionReader) reader).registerBeanDefinitions(
                        document, newUrlResource);
            } catch (BeansException e) {
                log.error("error", e);
                throw new RuntimeException(
                        "BeansException : " + e.getMessage(), e);
            } catch (MalformedURLException e) {
                log.error("error", e);
                throw new RuntimeException("MalformedUrlException : "
                        + e.getMessage(), e);
            } catch (IOException e) {
                log.error("error", e);
                throw new RuntimeException("IOExceptionException : "
                        + e.getMessage(), e);
            } catch (SAXException e) {
                log.error("error", e);
                throw new RuntimeException("SAXException : " + e.getMessage(),
                        e);
            }
        } else {
            throw new RuntimeException(
                    "No BeanDefinitionReader associated with " + url);
        }
    }

    // FIXME Will only work if all router entries are in a certain order
    // FIXME What happens with components that have mutiple outputs? 
    private void removeProcessors(Node mapNode) {
        NodeList entryNodes = ((Element) mapNode).getElementsByTagName("entry");
        String sourceComponent = entry.getSourceComponent();
        int nodeLength = entryNodes.getLength();
        for (int i = 0; i < nodeLength; i++) {
            Node entryNode = entryNodes.item(i);
            NamedNodeMap attributes = entryNode.getAttributes();
            Node keyRefNode = attributes.getNamedItem("key-ref");
            String keyRef = keyRefNode.getNodeValue();
            if (keyRef.equals(sourceComponent)) {
                Node valueRefNode = attributes.getNamedItem("value-ref");
                String valueRef = valueRefNode.getNodeValue();
                sourceComponent = valueRef;
            } else {
                nodeLength--;
                i--;
                mapNode.removeChild(entryNode);
            }
        }
    }

    private void stripOffProcessors(Element routerElement) {
        NodeList propertyNodes = routerElement.getElementsByTagName("property");
        for (int i = 0; i < propertyNodes.getLength(); i++) {
            Node node = propertyNodes.item(i);
            NamedNodeMap nodeMap = node.getAttributes();
            Node nameNode = nodeMap.getNamedItem("name");
            String value = nameNode.getNodeValue();
            // FIXME What happens if no map is used within the router?
            if (value.equals("processMap")) {
                NodeList propertyChilds = ((Element) node)
                        .getElementsByTagName("map");
                Node mapNode = propertyChilds.item(0);
                removeProcessors(mapNode);
            }
        }
    }

    public static void main(String[] args) {
        String hospitalFileName = null;
        String[] springAppArgs = new String[args.length - 2];
        // FIXME What happens if -hospital is the last parameter?
        for (int i = 0, j = 0; i < args.length; i++, j++) {
            if (args[i].equals(HOSPITAL)) {
                hospitalFileName = args[++i];
            } else {
                springAppArgs[j] = args[i];
            }
        }
        HospitalArtifactsReader reader = null;
        try {
            reader = new HospitalArtifactsReader(hospitalFileName);
        } catch (FileNotFoundException e) {
            String message = "Hospital file not found";
            log.error(message, e);
            throw new CCFRuntimeException(message, e);
        }
        HospitalEntry entry = null;
        try {
            while ((entry = reader.getNextEntry()) != null) {
                HospitalArtifactReplayer springAdaptor = new HospitalArtifactReplayer(
                        entry);
                springAdaptor.execute(springAppArgs);
            }
            reader.close();
        } catch (IOException e) {
            String message = "Hospital file reader IO failure";
            log.error(message, e);
            throw new CCFRuntimeException(message, e);
        }
        System.exit(0);
    }
}
