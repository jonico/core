package com.collabnet.tracker.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.message.MessageElement;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.collabnet.core.ws.exception.WSException;
import com.collabnet.core.ws.services.Dispatcher;
import com.collabnet.core.ws.services.DispatcherService;
import com.collabnet.core.ws.services.DispatcherServiceLocator;
import com.collabnet.core.ws.services.ProjectInfo;
import com.collabnet.core.ws.services.Request;
import com.collabnet.core.ws.services.Response;
import com.collabnet.core.ws.services.SystemStatus;
import com.collabnet.core.ws.services.SystemStatusService;
import com.collabnet.core.ws.services.SystemStatusServiceLocator;
import com.collabnet.core.ws.services.Version;
import com.collabnet.tracker.common.ClientArtifact;
import com.collabnet.tracker.common.ClientArtifactComment;
import com.collabnet.tracker.common.ClientArtifactListXMLHelper;
import com.collabnet.tracker.common.ClientXMLOperationError;
import com.collabnet.tracker.common.WebServiceClient;
import com.collabnet.tracker.core.model.TrackerArtifactType;
import com.collabnet.tracker.core.model.TrackerClientData;
import com.collabnet.tracker.core.util.TrackerUtil;
import com.collabnet.tracker.ws.ArtifactHistoryList;
import com.collabnet.tracker.ws.ArtifactType;
import com.collabnet.tracker.ws.ArtifactTypeMetadata;
import com.collabnet.tracker.ws.HistoryTransactionList;
import com.collabnet.tracker.ws.Metadata;
import com.collabnet.tracker.ws.MetadataService;
import com.collabnet.tracker.ws.MetadataServiceLocator;
import com.collabnet.tracker.ws.Query;
import com.collabnet.tracker.ws.attachment.AttachmentManager;
import com.collabnet.tracker.ws.attachment.AttachmentService;
import com.collabnet.tracker.ws.attachment.AttachmentServiceLocator;
import com.collabnet.tracker.ws.history.ArtifactHistoryManager;
import com.collabnet.tracker.ws.history.ArtifactHistoryService;
import com.collabnet.tracker.ws.history.ArtifactHistoryServiceLocator;
import com.collabnet.tracker.ws.query.QueryManager;
import com.collabnet.tracker.ws.query.QueryManagerService;
import com.collabnet.tracker.ws.query.QueryManagerServiceLocator;

/**
 * This is the interface to tracker through Axis.  This class handles all xml requests and
 * results through axis.
 * 
 * @author Shawn Minto
 * 
 */
public class TrackerWebServicesClient {

	public static final String ISSUE_URL = "/servlets/Scarab?id=";

	public static final String NEW_ISSUE_URL = "/servlets/Scarab/action/CreateArtifact";

	public static final String DEFINED_QUERY_URL = "/servlets/Scarab/action/ExecuteQuery?query=";

	public static final String EDIT_QUERY_URL = "/servlets/Scarab/remcurreport/true/template/EditQuery";

	public static final String ATTACHMENT_URL = "/servlets/ScarabDownload/remcurreport/true/template/ViewAttachment.vm/attachid/**ID**/filename/**FILENAME**";

	public static final String DEFAULT_NAMESPACE = "urn:ws.tracker.collabnet.com";
	
	public static final String HISTORY_URL_1 = "/servlets/Scarab/template/ViewIssue.vm/id/";
	
	public static final String HISTORY_URL_2 = "/eventsubmit_dosetissueview/foo/action/ViewIssue/tab/5/";

	public static final String API_VERSION = "1.2.0";
	
	public static final String TEXT_TAG = "tag.type.text";
	public static final String VALUE_TAG = "tag.type.value";

	public static final String CHANGE_OPERATION = "change";
	public static final String LEAVE_OPERATION = "leave";

	public static final String REASON_FOR_CHANGE_TYPE = "_REASON_FOR_CHANGE";

	private WebServiceClient mClient;
	private TrackerClientData repositoryData;
	private Proxy proxy;
	private String httpUser;
	private String httpPassword;

	public TrackerWebServicesClient(String url, String username, String password, Proxy proxy, String httpUser,
			String httpPassword) throws MalformedURLException {
		mClient = new WebServiceClient();
		mClient.init(username, password, url);

		this.proxy = proxy;
		this.httpUser = httpUser;
		this.httpPassword = httpPassword;
	}
	
	public List<ClientArtifact> getAllArtifacts(String artifactType, String nameSpace) throws Exception {
		if (nameSpace == null) nameSpace = mClient.getDefaultNamespace();
        EngineConfiguration config = mClient.getEngineConfiguration();
        DispatcherService service = new DispatcherServiceLocator(config);
        URL portAddress = mClient.constructServiceURL("/ws/Dispatcher");
        Dispatcher theService = service.getDispatcher(portAddress);
        String runQuery = "<getArtifactList xmlns='urn:ws.tracker.collabnet.com'><adhocQuery><artifactTypes><artifactType><tagName>" +
          artifactType + "</tagName><namespace>" + nameSpace + "</namespace></artifactType></artifactTypes></adhocQuery></getArtifactList>";
        Request queryRequest = toRequest(createDocument(runQuery));
        Response queryResponse = theService.execute(queryRequest);
        Document queryResponseDocument = toDocument(queryResponse);
        ClientArtifactListXMLHelper result = new ClientArtifactListXMLHelper(queryResponseDocument);
        return result.getAllArtifacts();
	}

	/**
	 * Get all of the server defined queries for the project
	 * @return
	 * @param artifact
	 * @throws Exception
	 * 
	 * 
	 * NOTE: All artifact in the List must belong to the same namespace and artifactType
	 * 
	 */
	public ClientArtifactListXMLHelper createArtifactList(List<ClientArtifact> artifacts) throws Exception {
		// TODO: implement
		System.out.println();
		try {
			Document doc = null;
			doc = createNewXMLDocument(DEFAULT_NAMESPACE, "ns1:"+"createArtifactList");

			ClientArtifactListXMLHelper helper = this.createOrUpdateArtifactList(doc, artifacts);
			return helper;
		}
		catch (Exception e) {
			throw e;
		}		
	}

	/**
	 * Get all of the server defined queries for the project
	 * @return
	 * @param artifact
	 * @throws Exception
	 * 
	 * 
	 * NOTE: All artifact in the List must belong to the same namespace and artifactType
	 * 
	 */
	public ClientArtifactListXMLHelper updateArtifactList(List<ClientArtifact> artifacts) throws Exception {
		// TODO: implement
		System.out.println();
		try {
			
			Document doc = null;
			doc = createNewXMLDocument(DEFAULT_NAMESPACE, "ns1:"+"updateArtifactList");
			
			ClientArtifactListXMLHelper helper  = this.createOrUpdateArtifactList(doc, artifacts);

			return helper;
		}
		catch (Exception e) {
			throw e;
		}		
	}
	
	public ClientArtifactListXMLHelper createOrUpdateArtifactList(Document doc,
			List<ClientArtifact> artifacts) throws Exception{
		EngineConfiguration config = mClient.getEngineConfiguration();
		DispatcherService service = new DispatcherServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/ws/Dispatcher");
		Dispatcher theService = service.getDispatcher(portAddress);
		Element root = doc.getDocumentElement();

		Element artifactListNode = doc.createElementNS(DEFAULT_NAMESPACE, "ns1:"+"artifactList");
		root.appendChild(artifactListNode);

		// TODO: Move all the below code to clientArtifact.java?
		
		HashMap<String, Integer> nameSpaces = new HashMap<String, Integer>();
		//List<String> nameSpaces = new ArrayList<String>();
		int nameSpaceCount = 1;
		nameSpaces.put(DEFAULT_NAMESPACE,nameSpaceCount);
		
		for (int i = 0 ; i < artifacts.size() ; i++) {
			ClientArtifact ca = artifacts.get(i);
			String nsXNameSpace = ca.getNamespace();
			String artifactType = ca.getTagName();
			//int nsCtr;
			// check if the namespace alrady exists in the xml so far
			if (nameSpaces.get(nsXNameSpace) == null) {
				nameSpaces.put(nsXNameSpace, ++nameSpaceCount);
			}
			
			String nsNumberString = "ns" + nameSpaces.get(nsXNameSpace) + ":";
			
			Element artifactNode = doc.createElementNS(nsXNameSpace, nsNumberString + artifactType);
			artifactListNode.appendChild(artifactNode);

			Element modByNode = doc.createElementNS(DEFAULT_NAMESPACE, "ns1:"+"modifiedBy");
			modByNode.appendChild(doc.createTextNode(mClient.getUserName()));
			artifactNode.appendChild(modByNode);

			Element lastReadNode = doc.createElementNS(DEFAULT_NAMESPACE, "ns1:"+"lastReadOn");
			lastReadNode.appendChild(doc.createTextNode(Long.toString(new Date().getTime())));
			artifactNode.appendChild(lastReadNode);

			// Add each attribute
			Map<String, List<String>> textAttributes = ca.getAttributes();
			
				for (String attribute: textAttributes.keySet()) {
					List<String> values = textAttributes.get(attribute);

					// strip the namespace from the attribute key
		    		String[] parts = attribute.substring(1).split("\\}");
		    		String attributeNamespace = parts[0];
		    		attribute = parts[1];
		    		if (nameSpaces.get(attributeNamespace) == null) {
						nameSpaces.put(attributeNamespace, ++nameSpaceCount);
					}
		    		nsNumberString = "ns" + nameSpaces.get(attributeNamespace) + ":";
					
					if (values.size() > 1 ||(attributeNamespace.equals(DEFAULT_NAMESPACE) &&
									attribute.equals("id"))) {
						for (String value: values) {
							if(value != null){
								Element attributeNode = doc.createElementNS(attributeNamespace, nsNumberString + attribute);
								Element valueNode = doc.createElementNS(DEFAULT_NAMESPACE, "ns1:value");
								//valueNode.setNodeValue(value);
								valueNode.appendChild(doc.createTextNode(value));
								attributeNode.appendChild(valueNode);
								
								artifactNode.appendChild(attributeNode);
							}
						}
					}
					else {
						// TODO: consider the namespace of the attributes?
						Element attributeNode = doc.createElementNS(nsXNameSpace, nsNumberString + attribute);
						//attributeNode.setNodeValue(values.get(0));
						attributeNode.appendChild(doc.createTextNode(values.get(0)));
						
						artifactNode.appendChild(attributeNode);
					}
				}
				List<ClientArtifactComment> comments = ca.getComments();
				for(ClientArtifactComment comment:comments){
					String commentText = comment.getCommentText();
					Element commentNode = doc.createElementNS("urn:ws.tracker.collabnet.com", "ns1:"+"comment");
					Element textNode = doc.createElementNS(DEFAULT_NAMESPACE, "ns1:"+"text");
					textNode.appendChild(doc.createTextNode(commentText));
					commentNode.appendChild(textNode);
					artifactNode.appendChild(commentNode);
				}
				
				Element reasonNode = doc.createElementNS("urn:ws.tracker.collabnet.com", "ns1:"+"reason");
				reasonNode.appendChild(doc.createTextNode("Synchronized by Connector"));
				artifactNode.appendChild(reasonNode);
		} // for every artifact
		
		Element sendMail = doc.createElementNS(DEFAULT_NAMESPACE, "ns1:"+"sendEmail");
		sendMail.appendChild(doc.createTextNode("true"));
		root.appendChild(sendMail);

//		printResult(doc);
		String docString = doc.getTextContent();
		System.out.println(docString); 
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		StringWriter sw = new StringWriter();
		t.transform( new DOMSource(doc), new StreamResult(sw));
		sw.toString();

		Request req = toRequest(doc);
		String reqString = req.toString();
		System.out.println(reqString); 
		Response r = theService.execute(toRequest(doc));
		Document result = toDocument(r);
		StringWriter sw1 = new StringWriter();
		t.transform( new DOMSource(result), new StreamResult(sw1));
		sw1.toString();
		ClientArtifactListXMLHelper helper = new ClientArtifactListXMLHelper(result);
		return helper;
	}


	/**
	 * Get all of the server defined queries for the project
	 * @return
	 * @throws ServiceException
	 * @throws WSException
	 * @throws RemoteException
	 */
	public Query[] getAllProjectDefinedQueries() throws ServiceException, WSException, RemoteException {
		EngineConfiguration config = mClient.getEngineConfiguration();
		QueryManagerService queryService = new QueryManagerServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/tracker/Query");
		QueryManager queryManager = queryService.getQueryManagerService(portAddress);
		Query[] queries = queryManager.getProjectQueries();
		return queries;
	}

	/**
	 * Get a single artifact given its id
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public ClientArtifactListXMLHelper getArtifactById(String taskId) throws Exception {
		Set<String> idList = new HashSet<String>(1);
		idList.add(taskId);
		return getArtifactsById(idList);
	}

	/**
	 * Get all of the artifacts given a list of ids
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	public ClientArtifactListXMLHelper getArtifactsById(Set<String> idList) throws Exception {

		EngineConfiguration config = mClient.getEngineConfiguration();
		DispatcherService service = new DispatcherServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/ws/Dispatcher");
		Dispatcher theService = service.getDispatcher(portAddress);

		String runQuery = "<getArtifactById xmlns='urn:ws.tracker.collabnet.com'> ";
		for (String id : idList) {
			runQuery += "<id>" + id + "</id>";
		}
		runQuery += "</getArtifactById>";

		TrackerUtil.debug("getArtifactsById(): ");
		Response r = theService.execute(toRequest(createDocument(runQuery)));
		Document result = toDocument(r);
		ClientArtifactListXMLHelper helper = new ClientArtifactListXMLHelper(result);
		return helper;
	}

	/**
	 * Execute the predefined server query and return the results
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public ClientArtifactListXMLHelper executePredefinedQuery(Query query) throws Exception {
		EngineConfiguration config = mClient.getEngineConfiguration();
		DispatcherService service = new DispatcherServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/ws/Dispatcher");
		Dispatcher theService = service.getDispatcher(portAddress);

		String xmlName = query.getTagName();
		String namespace = query.getNamespace();
		String runQuery = "<getArtifactList xmlns='urn:ws.tracker.collabnet.com'> " + "  <namedQuery>"
				+ "    <tagName>" + xmlName + "</tagName>" + "    <namespace>" + namespace + "</namespace>"
				+ "</namedQuery></getArtifactList>";
		
		TrackerUtil.debug("executePredefinedQuery(): " + xmlName);
		Response r = theService.execute(toRequest(createDocument(runQuery)));
		Document result = toDocument(r);
		ClientArtifactListXMLHelper helper = new ClientArtifactListXMLHelper(result);
		return helper;
	}

	/**
	 * Get the next page of results.  This is used by the ClientArtifactListXMLHelper
	 * to ensure that all results are returned
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public Document getNextPage(Node pageInfo, String altQueryRef) throws Exception {
		validateNextPage(pageInfo, altQueryRef);
		EngineConfiguration config = mClient.getEngineConfiguration();
		DispatcherService service = new DispatcherServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/ws/Dispatcher");
		Dispatcher theService = service.getDispatcher(portAddress);
		
		Document doc = createNewXMLDocument(DEFAULT_NAMESPACE, "ns1:"+"getNextPage");
		Element root = doc.getDocumentElement();
		Node newPageInfo = doc.importNode(pageInfo, true);
		root.appendChild(newPageInfo);
		
		TrackerUtil.debug("getNextPage()");
		Response r = theService.execute(toRequest(doc));
		Document result = toDocument(r);
	
		return result;
	}
	
	/**
	 * This is a check to ensure that invalid next page calls are not sent to the server.
	 * @param pageInfo
	 * @throws Exception
	 */
	public void validateNextPage(Node pageInfo, String altQueryRef) throws Exception {
		Node child = pageInfo.getFirstChild();
		String msg = "Next page does not have a valid query reference";
		String name, value;
		while(child != null) {
			name = child.getNodeName();
			value = child.getTextContent();
			if(name.contains("queryReference")) {
				if(value == null || value.length() < 1) {
					if(altQueryRef == null)
						throw new Exception(msg);
					child.setTextContent(altQueryRef);
				}
				return; 
			}
			child = child.getNextSibling();
		}
		throw new Exception(msg);
	}
	
//	/*
//	 * helper method that can be used to print an xml document to the command
//	 * line for debugging
//	 */
//	private void printResult(Document result) {
//		try{
//			System.out.println("\n\n\n");
//		DOMSource domSource = new DOMSource(result);
//		StreamResult streamResult = new StreamResult(System.out);
//		TransformerFactory tf = TransformerFactory.newInstance();
//		Transformer serializer = tf.newTransformer();
//		
//		serializer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
//		serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"users.dtd");
//		
//		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
//		serializer.setOutputProperty(OutputKeys.STANDALONE,"yes");
//		serializer.setOutputProperty(OutputKeys.METHOD,"xml");
//		serializer.setOutputProperty(OutputKeys.INDENT,"yes");
//		serializer.transform(domSource, streamResult); 
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}


	private Document createNewXMLDocument(String namespace, String qualifiedTagName) {
		DocumentBuilderFactory dbf;// = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc = null;

		try {
			dbf = DocumentBuilderFactory.newInstance();
	        db = dbf.newDocumentBuilder();
	        DOMImplementation di = db.getDOMImplementation();

	        doc = di.createDocument(namespace, qualifiedTagName, null);
		} catch (ParserConfigurationException e) {
			log(e, "could not create document");
		}
		return doc;
	}

	private void log(Exception e, String string) {
		System.out.println(string + ": " + e.getMessage());
	}


	/**
	 * This queries PT to see if the given task has changed since the given date
	 * @param artitfactNamespaceAndType
	 * @param taskId
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public boolean isChanged(String artitfactNamespaceAndType, String taskId, String dateString) throws Exception {
		List<String> ids = this.getChangedIds(artitfactNamespaceAndType, taskId, taskId, dateString);
		if (ids == null)
			return true;
		return ids.contains(taskId);
	}
	/**
	 * This queries PT to get the list of changed artifacts in range
	 * @param artitfactNamespaceAndType
	 * @param minTaskId
	 * @param maxTaskId
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public List<String> getChangedIds(String artitfactNamespaceAndType, String minTaskId, String maxTaskId, String dateString) throws Exception {
		EngineConfiguration config = mClient.getEngineConfiguration();
		DispatcherService service = new DispatcherServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/ws/Dispatcher");
		Dispatcher theService = service.getDispatcher(portAddress);

		String[] parts = artitfactNamespaceAndType.substring(1).split("\\}");
		String artifactType = parts[1];
		String namespace = parts[0];

		minTaskId = minTaskId.toLowerCase();
		maxTaskId = maxTaskId.toLowerCase();

		String lastSyncDate = dateString;
		String now = new Date().getTime() + "";

		String runQuery = "<getArtifactList xmlns='urn:ws.tracker.collabnet.com'> " + "<adhocQuery>"

		+ "<artifactTypes>" + "<artifactType>" + "<tagName>" + artifactType + "</tagName>" + "<namespace>" + namespace
				+ "</namespace>" + "</artifactType>" + "</artifactTypes>" + "<idRange>" + "<min>" + minTaskId
				+ "</min>" + "<max>" + maxTaskId + "</max>" + "</idRange>" + "<modifiedOn>" + "<rangeCondition>"
				+ "<min>" + lastSyncDate + "</min>" + "<max>" + now + "</max>" + "</rangeCondition>" + "</modifiedOn>"
				+ "</adhocQuery>" + "</getArtifactList>";

		TrackerUtil.debug("request isChanged() for min: " + minTaskId + " max: " + maxTaskId); 
		Response r = theService.execute(toRequest(createDocument(runQuery)));
		Document result = toDocument(r);
		ClientArtifactListXMLHelper helper = new ClientArtifactListXMLHelper(result);

		if (helper.getErrorSize() > 0) {
			// if we have an error, we will make it update so that we can try to
			// sync
			return new ArrayList<String>(0);
		}

		
		return helper.getAllArtifactIds();
	}
	
	/**
	 * Get changed artifacts since last synch time
	 * 
	 * @param kinds
	 * @param lastSynchDateTime
	 * @return
	 * @throws Exception
	 */
	public String[] getChangedArtifacts(Set<String> kinds, String lastSynchDateTime) throws Exception {
        EngineConfiguration config = mClient.getEngineConfiguration();
        ArtifactHistoryService service = new ArtifactHistoryServiceLocator(config);
        URL portAddress = mClient.constructServiceURL("/tracker/ArtifactHistory");
        ArtifactHistoryManager theService = service.getArtifactHistoryManager(portAddress);
        
        ArtifactType[] items = new ArtifactType[kinds.size()];
        int i= 0;
        for(String artitfactNamespaceAndType: kinds) {
    		String[] parts = artitfactNamespaceAndType.substring(1).split("\\}");
    		String artifactType = parts[1];
    		String namespace = parts[0];
            items[i] = new ArtifactType();
            items[i].setNamespace(namespace);
            items[i].setTagName(artifactType);
            i++;
        }
        
          Long to = new Long(System.currentTimeMillis());
         long from = to.longValue();
		try {
 			from = Long.parseLong(lastSynchDateTime);
		} catch(NumberFormatException nfe) {
			
		}

 		TrackerUtil.debug("request changedArtifacts() between: " + new Date(from) + " and " + new Date(to)); 
		System.out.println("request changedArtifacts() between: " + new Date(from) + " and " + new Date(to));
  		return theService.getChangedArtifactIDs(items, from, to);
	}

	/**
	 * Get all artifact changes between from and to times
	 * 
	 * @param ata
	 * @param from
	 * @param to
	 * @return
	 * @throws Exception
	 */
	public HistoryTransactionList getArtifactChanges(ArtifactType[] ata, long from, Long to) throws Exception {
        EngineConfiguration config = mClient.getEngineConfiguration();
        ArtifactHistoryService service = new ArtifactHistoryServiceLocator(config);
        URL portAddress = mClient.constructServiceURL("/tracker/ArtifactHistory");
        ArtifactHistoryManager theService = service.getArtifactHistoryManager(portAddress);

        /*
        Long to = new Long(System.currentTimeMillis());
        long from = to.longValue();
		try {
 			from = Long.parseLong(lastSynchDateTime);
		} catch(NumberFormatException nfe) {
			
		}
		*/

 		TrackerUtil.debug("request artifactChanges() between: " + new Date(from) + " and " + new Date(to)); 
		System.out.println("request artifactChanges() between: " + new Date(from) + " and " + new Date(to));
  		return theService.getArtifactChanges(ata, from, to);
	}
	
	public ArtifactHistoryList getChangeHistoryForArtifact(String artifactId, long from, Long to) throws Exception {
        String[] artifactList = new String[1];
        artifactList[0] = artifactId;
        return this.getChangeHistoryForArtifact(artifactList, from, to);
	}
	
	public ArtifactHistoryList getChangeHistoryForArtifact(String[] artifactList, long from, Long to) throws ServiceException, WSException, RemoteException{
		EngineConfiguration config = mClient.getEngineConfiguration();
        ArtifactHistoryService service = new ArtifactHistoryServiceLocator(config);
        URL portAddress = mClient.constructServiceURL("/tracker/ArtifactHistory");
        ArtifactHistoryManager theService = service.getArtifactHistoryManager(portAddress);
 		TrackerUtil.debug("request artifactChanges() between: " + new Date(from) + " and " + new Date(to)); 
		System.out.println("request artifactChanges() between: " + new Date(from) + " and " + new Date(to));
  		return theService.getArtifactHistory(artifactList, from, to);
	}

	/**
	 * Converts a DOM Document to a Request object expected by the
	 * Dispatcher.execute operation.
	 * 
	 * @param document
	 *            the Document to convert to a Request object
	 * @return the Request object
	 */
	private Request toRequest(Document document) {
		MessageElement element = new MessageElement(document.getDocumentElement());
		return new Request(new MessageElement[] { element });
	}

	/**
	 * Converts a Response returned by the Dispatcher.execute operation to a
	 * Document object.
	 * 
	 * @param response
	 *            the Response returned by the Dispatcher.execute operation
	 * @return the Document contained in the Response.
	 * @throws Exception
	 */
	private Document toDocument(Response response) throws Exception {	
		TrackerUtil.debug("response from server");
		return response.get_any()[0].getAsDocument();
	}

	/**
	 * Constructs a DOM Document from String
	 * 
	 * @param contents
	 *            the XML document
	 * @return the DOM representation of the document
	 * @throws ParserConfigurationException
	 *             if the XML parser cannot be constructed
	 * @throws SAXException
	 *             if the XML is not well formed
	 * @throws IOException
	 *             if the read operation fails
	 */
	protected Document createDocument(String contents) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		contents = contents.replaceAll("__DEFAULT_NAMESPACE__", mClient.getDefaultNamespace());
		InputSource inputSource = new InputSource(new StringReader(contents));

		Document document = builder.parse(inputSource);
		return document;
	}

	/**
	 * This method is used by the TrackerRepositoryConfigurationPage to validate the settings
	 * @throws Exception
	 */
	public void checkConnection() throws Exception {
		try {
			EngineConfiguration config = mClient.getEngineConfiguration();
			SystemStatusService service = new SystemStatusServiceLocator(config);
			URL portAddress = mClient.constructServiceURL("/ws/SystemStatus");
			SystemStatus theService = service.getSystemStatusService(portAddress);
			
			Version serverVer = theService.getVersion();
			if(isVersionGreaterOrEqual(serverVer, "1.2.0")) {
				ProjectInfo projInfo = theService.getProjectInfo(getProjectNameFromUrl());
				if(!projInfo.isPtEnabled()) {
					throw new TrackerException("This project does not support project tracker.");
				}
			}
			
			// see if user has access to pt
			Collection<TrackerArtifactType> artifactTypes = getArtifactTypes();
			if(artifactTypes.isEmpty()) {
				throw new TrackerException("You do not have access to any artifact types in this project");
			}
			
			validateApiVersion(theService.getVersion());
			
		} catch (AxisFault af) {
			if (af.getMessage().toLowerCase().contains("no permission for web services - execute")) {
				throw new TrackerException(
						"Could not connect to server, ensure WS Execute permissions are set (contact site administrator).");
			} else if (af.getMessage().toLowerCase().contains("no password")) {
				throw new TrackerException("Could not connect to server, ensure username and password are correct.");
			} else if (af.getCause() instanceof UnknownHostException) {
				throw new TrackerException("Could not connect to server, ensure server url is correct.");
			} else if (af.getMessage().contains("CoreWSException")) {
				throw new TrackerException("Could not connect to server, ensure server url is correct.");
			} else if (af.getMessage().contains("Error in getProjectInfo")) {
				throw new TrackerException("This is not a valid project.");
			} else {
				throw new Exception(af.getMessage());
			}
		}
	}

	/**
	 * This checks to make sure the server version of the api is <= the client
	 * version. If the server is downlevel, a tracker exception will be thrown
	 * 
	 * @param ver
	 * @throws TrackerException
	 */
	private void validateApiVersion(Version ver) throws TrackerException {
		if(isVersionGreaterOrEqual(ver, API_VERSION))
			return;
		
		String errorMsg = "Your CollabNet server is running API version " + ver.getApiVersion() + 
		".  This version is supported but will not allow required artifact attributes to be validated on the client.";
		throw new TrackerException(errorMsg);
	}
	
	private boolean isVersionGreaterOrEqual(Version ver, String baselineVersion) {
		StringTokenizer serverTokens = new StringTokenizer(ver.getApiVersion(), ".");
		StringTokenizer clientTokens = new StringTokenizer(API_VERSION, ".");
		String serverVer = "";
		String clientVer = "";
		int sVersion, cVersion;

		while (serverTokens.hasMoreTokens()) {
			serverVer = serverTokens.nextToken();
			clientVer = "0";
			if (clientTokens.hasMoreTokens()) {
				clientVer = clientTokens.nextToken();
			}
			sVersion = Integer.parseInt(serverVer);
			cVersion = Integer.parseInt(clientVer);
			if(cVersion > sVersion)
				return false; // test version is greater
			if(cVersion < sVersion)
				return true; // server ver is greater
		}
		return true;
	}

	/**
	 * Queries the server and returns a list of artifact types.
	 * @return
	 * @throws ServiceException
	 * @throws WSException
	 * @throws RemoteException
	 */
	public Collection<TrackerArtifactType> getArtifactTypes() 
			throws ServiceException, WSException, RemoteException {
		EngineConfiguration config = mClient.getEngineConfiguration();
		MetadataService service = new MetadataServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/tracker/Metadata");
		Metadata theService = service.getMetadataService(portAddress);
		ArtifactType[] artifactTypes = theService.getArtifactTypes();
		String key;
		for (ArtifactType type : artifactTypes){
			key = TrackerUtil.getKey(type.getNamespace(), type.getTagName());
			if (repositoryData == null) repositoryData = new TrackerClientData();
			if(repositoryData.getArtifactTypeFromKey(key) == null)
				repositoryData.addArtifactType(new TrackerArtifactType(type));
		}
		
		return repositoryData.getArtifactTypes();
	}
	
	/**
	 * Get the metadata for the given artifact.  The metadata contains the valid values
	 * for attributes and the valid operations that can be performed on the attribute
	 * 
	 * @param namespace
	 * @param artifactType
	 * @param artifactId
	 * @return
	 * @throws ServiceException
	 * @throws WSException
	 * @throws RemoteException
	 */
	public ArtifactTypeMetadata getMetaDataForArtifact(String namespace, String artifactType, String artifactId)
			throws ServiceException, WSException, RemoteException {
		EngineConfiguration config = mClient.getEngineConfiguration();
		MetadataService service = new MetadataServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/tracker/Metadata");
		Metadata theService = service.getMetadataService(portAddress);
		TrackerUtil.debug("getMetaDataForArtifact():" + artifactId);
		ArtifactTypeMetadata metaData = theService.getMetadataForArtifact(
				new ArtifactType(artifactType, namespace, ""), artifactId);
		TrackerUtil.debug("getMetaDataForArtifact():done ");
		TrackerArtifactType type = repositoryData.getArtifactTypeFromKey(TrackerUtil.getKey(namespace, artifactType));
		if (type == null) {
			type = new TrackerArtifactType(metaData.getArtifactType().getDisplayName(), metaData.getArtifactType()
					.getTagName(), metaData.getArtifactType().getNamespace());
		}
		type.populateAttributes(metaData);
		repositoryData.addArtifactType(type);

		return metaData;
	}

	
	/**
	 * Get the metadata that can be used when creating a new PT artifact.  This should be used
	 * when artifact creation is supported
	 * 
	 * @param namespace
	 * @param artifactType
	 * @return
	 * @throws ServiceException
	 * @throws WSException
	 * @throws RemoteException
	 */
	public ArtifactTypeMetadata getMetaDataForNewArtifact(String namespace, String artifactType)
			throws ServiceException, WSException, RemoteException {
		EngineConfiguration config = mClient.getEngineConfiguration();
		MetadataService service = new MetadataServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/tracker/Metadata");
		Metadata theService = service.getMetadataService(portAddress);
		return theService.getMetadataForNewArtifact(new ArtifactType(artifactType, namespace, ""));
	}

	
	/**
	 * Attach a file to a PT artifact
	 * 
	 * @param taskId
	 * @param comment
	 * @param attachment
	 * @throws ServiceException
	 * @throws WSException
	 * @throws RemoteException
	 */
	public void postAttachment(String taskId, String comment, DataSource attachment) throws ServiceException, WSException, RemoteException {
		EngineConfiguration config = mClient.getEngineConfiguration();
		AttachmentService service = new AttachmentServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/tracker/Attachment");
		AttachmentManager theService = service.getAttachmentService(portAddress);
		DataHandler attachmentHandler = new DataHandler(attachment);

		theService.addAttachment(taskId, attachment.getName(), comment, attachment.getContentType(), attachmentHandler);

	}
	
	public DataHandler getDataHandlerForAttachment(String taskId, String attachmentId) throws
			ServiceException, WSException, NumberFormatException, RemoteException{
		EngineConfiguration config = mClient.getEngineConfiguration();
		AttachmentService service = new AttachmentServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/tracker/Attachment");
		AttachmentManager theService = service.getAttachmentService(portAddress);

		DataHandler handler = theService.getAttachment(taskId, Long.parseLong(attachmentId));
		return handler;
	}

	/**
	 * Download an attachment from PT
	 * 
	 * @param taskId
	 * @param attachmentId
	 * @return
	 * @throws ServiceException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public InputStream downloadAttachmentAsStream(String taskId, String attachmentId) throws ServiceException,
			NumberFormatException, IOException {

		EngineConfiguration config = mClient.getEngineConfiguration();
		AttachmentService service = new AttachmentServiceLocator(config);
		URL portAddress = mClient.constructServiceURL("/tracker/Attachment");
		AttachmentManager theService = service.getAttachmentService(portAddress);

		DataHandler handler = theService.getAttachment(taskId, Long.parseLong(attachmentId));

		return handler.getInputStream();
//		
//		byte[] attachment = null;
//		InputStream inputStream = null;
//		ByteArrayOutputStream baos = null;
//		try {
//			inputStream = handler.getInputStream();
//			baos = new ByteArrayOutputStream(2048);
//			byte[] buffer = new byte[2048];
//			int n = 0;
//			while (-1 != (n = inputStream.read(buffer))) {
//				baos.write(buffer, 0, n);
//			}
//
//			attachment = baos.toByteArray();
//			baos.close();
//			inputStream.close();
//			return attachment;
//		} finally {
//			if (baos != null)
//				baos.close();
//			if (inputStream != null)
//				inputStream.close();
//		}
	}

	
	public void updateAttributes() {
		// TODO This should update the attribute information
		// This is not implemented since the attribute information is retrieved
		// per artifact right now, but would preferably be per repository. This
		// will also be needed for new artifact creation

		repositoryData.clear();
	}

	public TrackerClientData getRepositoryData() {
		return repositoryData;
	}

	public void setRepositoryData(TrackerClientData data) {
		this.repositoryData = data;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public String getHttpUser() {
		return httpUser;
	}

	public String getHttpPassword() {
		return httpPassword;
	}

	public String getRepositoryUrl() {
		return mClient.getURL();
	}
	
	/**
	 * Parses the project name from the repository url.
	 * @return
	 */
	public String getProjectNameFromUrl() {
		String aUrl = mClient.getURL();
		if(aUrl == null)
			return "";
		
		int prefixIndex = aUrl.indexOf("//");
		int postfixIndex = aUrl.indexOf(".");
		
		prefixIndex = prefixIndex == -1 ? 0 : prefixIndex + 2;
		postfixIndex = postfixIndex == -1 ? aUrl.length() : postfixIndex;
		
		return aUrl.substring(prefixIndex, postfixIndex);
	}
}
