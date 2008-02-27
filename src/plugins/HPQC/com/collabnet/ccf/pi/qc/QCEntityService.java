package com.collabnet.connector.qc;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;



public class QCEntityService extends QCConnectHelper implements
		IDataProcessor {
	
	public QCEntityService() {
		super();
	}

	private static final Log log = LogFactory.getLog(QCEntityService.class);
	private String CreateToken;
	private String synchronizationUser;
	private String otherSystemInQCTargetFieldname;
	private QCDefectHandler defectHandler;

	public Object[] process(Object data) {
		if (data == null) {
		      throw new NullRecordException("Expected Document. Null record not permitted.");
		    }

		    if (!(data instanceof Document)) {
		      throw new RecordFormatException("Expected Document. Got [" + data.getClass().getName() + "]");
		    }

		    return processXMLDocument((Document) data);
	}

	private Object[] processXMLDocument(Document data) {
		Boolean duplicateArtifact=(Boolean)QCXMLHelper.asTypedValue(QCXMLHelper.getSingleValue(data, "isDuplicate", false), "Boolean");
		if (duplicateArtifact)
			return new Object[0];
		
		boolean defectAlreadyCreated=false;
		
		// check whether there is already an entity in the target system
		String id = QCXMLHelper.getSingleValue(data, "BG_BUG_ID", false);
		log.info("Found bug id: "+id);
		if (id!=null && !id.isEmpty())
				defectAlreadyCreated=true;
		else {
			
			if (!QCXMLHelper.containsSingleField(data,getOtherSystemInQCTargetFieldname(),true)) {
				// TODO Throw an exception?
				log.error("required "+getOtherSystemInQCTargetFieldname()+"-element not present in artifact: "+data.asXML());
				return null;
			}
			
			// now we can query the target system for the existence of the artifact
			try {
				connect();
				IQCDefect[] defects = defectHandler.getDefectsWithOtherSystemId(qcc, getOtherSystemInQCTargetFieldname(), QCXMLHelper.getSingleValue(data,getOtherSystemInQCTargetFieldname(),true));
				//IQCDefect[] defects = defectHandler.getDefectsWithOtherSystemId(qcc, "BG_BUG_ID", QCXMLHelper.getSingleValue(data,"BG_BUG_ID",true));
				//ArtifactDetailSoapRow [] artifacts=trackerHandler.getFilteredTrackerItems(getSessionId(), "tracker1006", getSFEEmappingID(), (String)data.get(getSFEEmappingID()));
				log.error(defects.length);
				if (defects==null || defects.length==0)
					defectAlreadyCreated=false;
				else if (defects.length>1) {
					// TODO Throw an exception?
					log.error("Defect is mapped more than once in the target system: "+data.asXML());
					disconnect();
					return null;
				}
				// defect already created, insert or update Id element
				else {
					defectAlreadyCreated=true;
					if (QCXMLHelper.containsSingleField(data,"BG_BUG_ID",false))
						QCXMLHelper.updateSingleField(data.getRootElement(), "BG_BUG_ID", defects[0].getId(), false);
					else
						QCXMLHelper.addField(data.getRootElement(), "BG_BUG_ID", defects[0].getId(),"String", false);
				}	
				disconnect();
			} catch (Exception e) {
				// TODO Declare exception so that it can be processed by OA exception handler
				log.error("An error occured while connecting to QC", e);
				disconnect();
				return null;
			}	
		}		
		if (defectAlreadyCreated) {	
			// this object is already created, now find out, whether we have to do an update
			// check whether last update was done by synchronization user
			if (!QCXMLHelper.containsSingleField(data,"lastModifiedBy",false)) {
				// TODO Throw an exception?
				log.error("required lastModifiedBy-element not present in artifact: "+data.asXML());
				return null;
			}
			// Only accept the change if the artifact was created or the change did not come from the synchronization middleware itself
			// TODO Due to the update after a delete, this may miss some artifacts that were just created
			if (QCXMLHelper.getSingleValue(data,"lastModifiedBy",false).equals(getSynchronizationUser()) && (!QCXMLHelper.getSingleValue(data, "lastModifiedDate", false).equals(QCXMLHelper.getSingleValue(data, "createdDate", false)))) {
				log.info("lastModifiedBy Value :" + QCXMLHelper.getSingleValue(data,"lastModifiedBy",false));
				log.info("getSynchronizationUser" + getSynchronizationUser());
				log.info("lastModfiedDate from the xml file :" + QCXMLHelper.getSingleValue(data, "lastModifiedDate", false));
				log.info("createdDate from the xml file :" + QCXMLHelper.getSingleValue(data, "createdDate", false));
				// we do not have to pass this artifact because all changes are already presented in target system
				log.info("Do not change object, since the change came from this system: "+QCXMLHelper.getSingleValue(data,"BG_BUG_ID",false));
				return new Object[0];
			}
			// else object should be updated
			else {
				log.info("Updating or deleting object with "+getOtherSystemInQCTargetFieldname()+" "+QCXMLHelper.getSingleValue(data,"BG_BUG_ID",false)+" last modified by "+QCXMLHelper.getSingleValue(data,"lastModifiedBy",false));
				return new Object [] {data};
			}
		}
		
		// artifact has to be created
		if (QCXMLHelper.containsSingleField(data,"BG_BUG_ID",false))
			QCXMLHelper.updateSingleField(data.getRootElement(), "BG_BUG_ID", getCreateToken(), false);
		else
			QCXMLHelper.addField(data.getRootElement(),"BG_BUG_ID", getCreateToken(),"String",false);
		log.info("Trying to create new object or trying to delete object that is not (yet) mirrored");
		return new Object [] {data};
	}

	public void reset(Object context) {
	}

	public void setSynchronizationUser(String synchronizationUser) {
		this.synchronizationUser = synchronizationUser;
	}

	public String getSynchronizationUser() {
		return synchronizationUser;
	}
	
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		if (getSynchronizationUser()==null) {
			log.error("synchronizationUser-property no set");
			exceptions.add(new ValidationException("synchronizationUser-property not set",this));
		}
		if (getOtherSystemInQCTargetFieldname()==null) {
			log.error("otherSystemInQCTargetFieldname-property not set");
			exceptions.add(new ValidationException("otherSystemInQCTargetFieldname not set",this));
		}
		
		if (getCreateToken()==null) {
			log.error("createToken-property no set");
			exceptions.add(new ValidationException("createToken-property not set",this));
		}
		// Create tracker handler
		defectHandler = new QCDefectHandler();
	}

	public void setOtherSystemInQCTargetFieldname(String sFEEmappingID) {
		otherSystemInQCTargetFieldname = sFEEmappingID;
	}

	public String getOtherSystemInQCTargetFieldname() {
		return otherSystemInQCTargetFieldname;
	}

	public void setCreateToken(String createToken) {
		CreateToken = createToken;
	}

	public String getCreateToken() {
		return CreateToken;
	}

}
