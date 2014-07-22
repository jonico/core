package com.collabnet.ccf.splunk;

import org.dom4j.Document;
import org.openadaptor.auxil.orderedmap.IOrderedMap;
import org.openadaptor.auxil.orderedmap.OrderedHashMap;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.EntityService;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DummyArtifactSoapDO;
import com.collabnet.ccf.core.utils.XPathUtils;

public class SplunkEntityService extends EntityService {

    @Override
    public Object[] process(Object data) {

        if (data instanceof DummyArtifactSoapDO) {
            try {
                DummyArtifactSoapDO artifactSoapDO = (DummyArtifactSoapDO) data;
                Document gADocument = artifactSoapDO.getGenericArtifact();
                org.dom4j.Element element = XPathUtils
                        .getRootElement(gADocument);
                String artifactType = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.ARTIFACT_TYPE);
                String sourceArtifactId = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.SOURCE_ARTIFACT_ID);
                String sourceSystemId = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.SOURCE_SYSTEM_ID);
                String sourceRepositoryId = XPathUtils.getAttributeValue(
                        element, GenericArtifactHelper.SOURCE_REPOSITORY_ID);
                String targetSystemId = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.TARGET_SYSTEM_ID);
                String targetRepositoryId = XPathUtils.getAttributeValue(
                        element, GenericArtifactHelper.TARGET_REPOSITORY_ID);
                String targetArtifactId = XPathUtils.getAttributeValue(element,
                        GenericArtifactHelper.TARGET_ARTIFACT_ID);
                IOrderedMap inputParameters = new OrderedHashMap();
                inputParameters.add(sourceSystemId);
                inputParameters.add(sourceRepositoryId);
                inputParameters.add(targetSystemId);
                inputParameters.add(targetRepositoryId);
                inputParameters.add(sourceArtifactId);
                inputParameters.add(artifactType);
                inputParameters.add(targetArtifactId);
                identityMappingDatabaseReader.connect();
                Object[] resultSet = identityMappingDatabaseReader.next(
                        inputParameters, 1000);
                if (resultSet.length == 0) {
                    return new Object[] { data };
                }
                OrderedHashMap result = (OrderedHashMap) resultSet[0];
                int versionFromTable = Integer.valueOf((String) result.get(2));
                int currentVersion = Integer.valueOf(artifactSoapDO
                        .getLastVersion());
                if (artifactType.equalsIgnoreCase("plainArtifact")) {
                    if (currentVersion > versionFromTable) {
                        return new Object[] { data };
                    }
                } else {
                    return null;
                }

                /*
                 * if (artifactSoapDO.getUpdatedData() instanceof
                 * AssociationRow) { if (currentVersion == versionFromTable) {
                 * return new Object[] { data }; } }
                 */

            } catch (GenericArtifactParsingException e) {
                throw new CCFRuntimeException(e.getMessage());
            }
        }
        return null; // discarding old artifactData that have already been moved to tf dump files  
    }
}
