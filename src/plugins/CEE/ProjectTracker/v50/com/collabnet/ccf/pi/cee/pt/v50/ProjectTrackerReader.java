package com.collabnet.ccf.pi.cee.pt.v50;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.collabnet.ccf.core.eis.connection.MaxConnectionsReachedException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.core.ws.exception.WSException;
import com.collabnet.tracker.common.ClientArtifact;
import com.collabnet.tracker.common.ClientArtifactListXMLHelper;
import com.collabnet.tracker.core.TrackerWebServicesClient;
import com.collabnet.tracker.core.model.TrackerArtifactType;

public class ProjectTrackerReader extends AbstractReader {
	
	private String serverUrl = null;

	private String password = null;

	private String username = null;
	
	private ConnectionManager<TrackerWebServicesClient> connectionManager = null;

	@Override
	public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
			String artifactId) {
		// TODO Auto-generated method stub
		return new ArrayList<GenericArtifact>();
	}

	@Override
	public List<GenericArtifact> getArtifactData(Document syncInfo,
			String artifactId) {
		TrackerWebServicesClient twsclient = this.getConnection(syncInfo);
		ClientArtifact artifact = null;
		try {
			ClientArtifactListXMLHelper listHelper = twsclient.getArtifactById(artifactId);
			List<ClientArtifact> artifacts = listHelper.getAllArtifacts();
			if(artifacts == null || artifacts.size() == 0){
				throw new CCFRuntimeException("There is no artifact with id "+artifactId);
			}
			else if(artifacts.size() == 1){
				artifact = artifacts.get(0);
			}
			else{
				throw new CCFRuntimeException("More than one artifact were returned for id "+artifactId);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<GenericArtifact> gaList = new ArrayList<GenericArtifact>();
		GenericArtifact ga = new GenericArtifact();
		ga.setSourceArtifactId(artifactId);
		Map<String, List<String>> attributes = artifact.getAttributes();
		for(String attributeName:attributes.keySet()){
			List<String> attValues = attributes.get(attributeName);
			for(String attValue:attValues){
				GenericArtifactField field = ga.addNewField(attributeName,
						GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
				field.setFieldAction(GenericArtifactField.FieldActionValue.REPLACE);
				field.setFieldValueType(GenericArtifactField.FieldValueTypeValue.STRING);
				field.setFieldValue(attValue);
			}
		}
		this.populateSrcAndDest(syncInfo, ga);
		gaList.add(ga);
		return gaList;
	}

	@Override
	public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
			String artifactId) {
		// TODO Auto-generated method stub
		return new ArrayList<GenericArtifact>();
	}
	
	private TrackerWebServicesClient getConnection(Document syncInfo){
		String systemId = this.getSourceSystemId(syncInfo);
		String systemKind = this.getSourceSystemKind(syncInfo);
		String repositoryId = this.getSourceRepositoryId(syncInfo);
		String repositoryKind = this.getSourceRepositoryKind(syncInfo);
		String connectionInfo = this.getServerUrl();
		String credentialInfo = this.getUsername()+
							CollabNetConnectionFactory.PARAM_DELIMITER+this.getPassword();
		TrackerWebServicesClient twsclient = null;
		try {
			twsclient = this.connect(systemId, systemKind, repositoryId,
					repositoryKind, connectionInfo, credentialInfo);
		} catch (MaxConnectionsReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CCFRuntimeException("Could not get connection for PT",e);
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CCFRuntimeException("Could not get connection for PT",e);
		}
		return twsclient;
	}

	@Override
	public List<String> getChangedArtifacts(Document syncInfo) {
		String repositoryId = this.getSourceRepositoryId(syncInfo);
		String lastSynchronizedVersion = this.getLastSourceVersion(syncInfo);
		Date lastModifiedDate = this.getLastModifiedDate(syncInfo);
		String fromTime = Long.toString(lastModifiedDate.getTime());
		
		String[] splitRepositoryId = repositoryId.split(CollabNetConnectionFactory.PARAM_DELIMITER);
		String[] trackerArtifactTypes = null;
		if(splitRepositoryId.length > 1){
			trackerArtifactTypes = new String[splitRepositoryId.length-1];
			System.arraycopy(splitRepositoryId, 1, trackerArtifactTypes, 0, splitRepositoryId.length-1);
		}
		Collection<TrackerArtifactType> tAT = null;
		TrackerWebServicesClient twsclient = this.getConnection(syncInfo);
		// To get the artifact types
		try {
			tAT = twsclient.getArtifactTypes();
		} catch (WSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//As of now retrieving artifacts of all the types 
		Set<String> artifactTypes = new HashSet<String>();
		for (TrackerArtifactType type : tAT )
		{
			System.out.println(type.getDisplayName());
			System.out.println(type.getNamespace());
			System.out.println(type.getTagName());
			artifactTypes.add("{"+type.getNamespace()+"}"+type.getTagName());
		}

		String changedArtifacts[] = null;
		try {
			changedArtifacts = twsclient.getChangedArtifacts(artifactTypes, fromTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> artifacts = new ArrayList<String>();
		for (String artifact: changedArtifacts)
		{
			artifacts.add(artifact);
		}
		return artifacts;
	}
	
	/**
	 * Connects to the source SFEE system using the connectionInfo and credentialInfo
	 * details.
	 * 
	 * This method uses the ConnectionManager configured in the wiring file
	 * for the SFEEReader
	 *  
	 * @param systemId - The system id of the source SFEE system
	 * @param systemKind - The system kind of the source SFEE system
	 * @param repositoryId - The tracker id in the source SFEE system
	 * @param repositoryKind - The repository kind for the tracker
	 * @param connectionInfo - The SFEE server URL
	 * @param credentialInfo - User name and password concatenated with a delimiter.
	 * @return - The connection object obtained from the ConnectionManager
	 * @throws MaxConnectionsReachedException 
	 * @throws ConnectionException 
	 */
	public TrackerWebServicesClient connect(String systemId, String systemKind, String repositoryId,
			String repositoryKind, String connectionInfo, String credentialInfo) throws MaxConnectionsReachedException, ConnectionException {
		//log.info("Before calling the parent connect()");
		TrackerWebServicesClient connection = null;
		connection = connectionManager.getConnection(systemId, systemKind, repositoryId,
			repositoryKind, connectionInfo, credentialInfo);
		return connection;
	}
	
	/**
	 * Populates the source and destination attributes for this GenericArtifact
	 * object from the Sync Info database document.
	 * 
	 * @param syncInfo
	 * @param ga
	 */
	private void populateSrcAndDest(Document syncInfo, GenericArtifact ga){
		String sourceRepositoryId = this.getSourceRepositoryId(syncInfo);
		String sourceRepositoryKind = this.getSourceRepositoryKind(syncInfo);
		String sourceSystemId = this.getSourceSystemId(syncInfo);
		String sourceSystemKind = this.getSourceSystemKind(syncInfo);
		String conflictResolutionPriority = this.getConflictResolutionPriority(syncInfo);
		
		String targetRepositoryId = this.getTargetRepositoryId(syncInfo);
		String targetRepositoryKind = this.getTargetRepositoryKind(syncInfo);
		String targetSystemId = this.getTargetSystemId(syncInfo);
		String targetSystemKind = this.getTargetSystemKind(syncInfo);
		
		ga.setSourceRepositoryId(sourceRepositoryId);
		ga.setSourceRepositoryKind(sourceRepositoryKind);
		ga.setSourceSystemId(sourceSystemId);
		ga.setSourceSystemKind(sourceSystemKind);
		ga.setConflictResolutionPriority(conflictResolutionPriority);
		
		ga.setTargetRepositoryId(targetRepositoryId);
		ga.setTargetRepositoryKind(targetRepositoryKind);
		ga.setTargetSystemId(targetSystemId);
		ga.setTargetSystemKind(targetSystemKind);
	}
	
	/**
	 * Releases the connection to the ConnectionManager.
	 * 
	 * @param connection - The connection to be released to the ConnectionManager
	 */
	public void disconnect(TrackerWebServicesClient connection) {
		connectionManager.releaseConnection(connection);
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ConnectionManager<TrackerWebServicesClient> getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(
			ConnectionManager<TrackerWebServicesClient> connectionManager) {
		this.connectionManager = connectionManager;
	}

}
