package com.collabnet.ccf.pi.sfee;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;

import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;

import com.collabnet.ccf.pi.qc.NamesTypesAndValues;

public class SFEEWriter extends SFEEConnectHelper implements
		IDataProcessor {

	private static final Log log = LogFactory.getLog(SFEEWriter.class);
	private String CreateToken;
	private SFEETrackerHandler trackerHandler;	
	private String updateComment;
	private String otherSystemVersionInSFEETargetFieldname;
	private String lastSynchronizedWithOtherSystemSFEETargetFieldname;
	
	private Object[] processXMLDocument(Document data) {
		Boolean duplicateArtifact=(Boolean)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data, "isDuplicate", false), "Boolean");
		if (duplicateArtifact)
			// do not suppress it anymore, but pass it unchanged
			// return new Object[0];
			return new Object [] {data};
		
		
		// this field is probably added from a read processor or read connector component
		Boolean deleteArtifact=(Boolean)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data, "deleteFlag", false), "Boolean");
		// this field is probably added by a dedicated conflict resolution policy processor or a generic processor
		// TODO Let user specify this field?
		Boolean forceOverride=(Boolean)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data, "forceOverride", false), "Boolean");
		// fill flexFieldNames and flexFieldValue arrays
		List<String> flexFieldNames=new ArrayList<String>();
		List<Object> flexFieldValues=new ArrayList<Object>();
		List<String> flexFieldTypes=new ArrayList<String>();
		for (NamesTypesAndValues key : SFEEXMLHelper.getAllFlexFields(data)) {
			flexFieldNames.add(key.name);
			flexFieldValues.add(SFEEXMLHelper.asTypedValue(key.value, key.type));
			flexFieldTypes.add(key.type);
		}

		// check whether we should create or update the artifact
		if (SFEEXMLHelper.getSingleValue(data,"Id",false).equals(getCreateToken())) {
			// find out whether we should delete something, that is not even present here
			if (deleteArtifact.booleanValue()) {
				log.warn("Cannot delete an artifact that is not even mirrored (yet): "+data.asXML());
				return null;
			}
			connect();
			// TODO apply a better type conversion concept here
			try {
				//trackerHandler.createArtifact(getSessionId(), (String)data.get("folderId"), (String)data.get("description"), (String) data.get("category"), (String) data.get("group"), (String) data.get("status"), (String) data.get("statusClass"), (String) data.get("customer"), Integer.parseInt((String)data.get("priority")), Integer.parseInt((String)data.get("estimatedHours")), Integer.parseInt((String)data.get("actualHours")), (Date) data.get("closeDate"), (String) data.get("assignedTo"), (String) data.get("reportedReleaseId"), (String) data.get("resolvedReleaseId"), flexFields, (String) data.get("title"));
				
				// update Id field after creating the artifact
				ArtifactSoapDO result=trackerHandler.createArtifact(getSessionId(), 
						SFEEXMLHelper.getSingleValue(data,"folderId",false), 
						SFEEXMLHelper.getSingleValue(data,"description",false), 
						SFEEXMLHelper.getSingleValue(data,"category",false), 
						SFEEXMLHelper.getSingleValue(data,"group",false), 
						SFEEXMLHelper.getSingleValue(data,"status",false), 
						SFEEXMLHelper.getSingleValue(data,"statusClass",false), 
						SFEEXMLHelper.getSingleValue(data,"customer",false),
						(Integer)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data,"priority",false),"Integer"),
						(Integer)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data,"estimatedHours",false),"Integer"),
						(Integer)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data,"actualHours",false),"Integer"),
						(Date)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data,"closeDate",false),"DateTime"), 
						SFEEXMLHelper.getSingleValue(data,"assignedTo",false), 
						SFEEXMLHelper.getSingleValue(data,"reportedReleaseId",false),
						SFEEXMLHelper.getSingleValue(data,"resolvedReleaseId",false), 
						flexFieldNames,
						flexFieldValues,
						flexFieldTypes,
						SFEEXMLHelper.getSingleValue(data,"title",false),
						getUpdateComment(),
						getLastSynchronizedWithOtherSystemSFEETargetFieldname());
				
				SFEEXMLHelper.updateSingleField(data.getRootElement(),"Id", result.getId(), false);
						
			} catch (NumberFormatException e) {
				log.error("Wrong data format of attribute for artifact "+data.asXML(),e);
				disconnect();
				return null;
			} catch (RemoteException e) {
				log.error("Remote exception while trying to create new artifact: "+data.asXML(),e);
				disconnect();
				return null;
			}
		}
		else {
			connect();
			try {
				if (deleteArtifact.booleanValue()) {
					trackerHandler.removeArtifact(getSessionId(), SFEEXMLHelper.getSingleValue(data,"Id",false));
				}
				else {
					// update token or do conflict resolution
					// TODO apply a better type conversion concept here
					// update Id-field in data object
					//trackerHandler.updateArtifact(getSessionId(),"tracker1006", SFEEXMLHelper.getSingleValue(data,"description"), SFEEXMLHelper.getSingleValue(data,"category"), SFEEXMLHelper.getSingleValue(data,"group"), SFEEXMLHelper.getSingleValue(data,"status"), SFEEXMLHelper.getSingleValue(data,"statusClass"), SFEEXMLHelper.getSingleValue(data,"customer"), ((Integer)data.get("priority")).intValue(), ((Integer)data.get("estimatedHours")).intValue(), ((Integer)data.get("actualHours")).intValue(), (Date) data.get("closeDate"), SFEEXMLHelper.getSingleValue(data,"assignedTo"), SFEEXMLHelper.getSingleValue(data,"reportedReleaseId"), SFEEXMLHelper.getSingleValue(data,"resolvedReleaseId"), flexFields, SFEEXMLHelper.getSingleValue(data,"title"),SFEEXMLHelper.getSingleValue(data,"Id"),getUpdateComment());
					ArtifactSoapDO result=trackerHandler.updateArtifact(getSessionId(), 
							SFEEXMLHelper.getSingleValue(data,"folderId",false), 
							SFEEXMLHelper.getSingleValue(data,"description",false), 
							SFEEXMLHelper.getSingleValue(data,"category",false), 
							SFEEXMLHelper.getSingleValue(data,"group",false), 
							SFEEXMLHelper.getSingleValue(data,"status",false), 
							SFEEXMLHelper.getSingleValue(data,"statusClass",false), 
							SFEEXMLHelper.getSingleValue(data,"customer",false),
							(Integer)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data,"priority",false),"Integer"),
							(Integer)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data,"estimatedHours",false),"Integer"),
							(Integer)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data,"actualHours",false),"Integer"),
							(Date)SFEEXMLHelper.asTypedValue(SFEEXMLHelper.getSingleValue(data,"closeDate",false),"DateTime"),
							SFEEXMLHelper.getSingleValue(data,"assignedTo",false), 
							SFEEXMLHelper.getSingleValue(data,"reportedReleaseId",false),
							SFEEXMLHelper.getSingleValue(data,"resolvedReleaseId",false), 
							flexFieldNames,
							flexFieldValues,
							flexFieldTypes,
							SFEEXMLHelper.getSingleValue(data,"title",false),
							SFEEXMLHelper.getSingleValue(data,"Id",false),
							getUpdateComment(),
							getLastSynchronizedWithOtherSystemSFEETargetFieldname(),
							SFEEXMLHelper.getSingleValue(data,getOtherSystemVersionInSFEETargetFieldname(), true),
							getOtherSystemVersionInSFEETargetFieldname(),
							SFEEXMLHelper.getSingleValue(data,"version",false),
							forceOverride);
					
					if (result==null) {
						// conflict resolution has decided in favor of the target copy
						disconnect();
						return new Object[0];
					}
				}
			} catch (NumberFormatException e) {
				log.error("Wrong data format of attribute for artifact "+data.asXML(),e);
				disconnect();
				return null;
			} catch (RemoteException e) {
				// TODO Find a better way how to deal with stale updates (calling conflict resolution and trying it again?)
				log.error("Remote exception while trying to update or delete artifact: "+data.asXML(),e);
				disconnect();
				return null;
			}
		}
		Object[] result={data};
		disconnect();
		return result;
	}

	@Override
	public void connect()  {
		try {
			super.connect();
		} catch (IOException e) {
			// TODO Throw an exception?
			log.error("Could not login into SFEE: ",e);
		}
	}
	
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		
		if (getCreateToken()==null) {
			log.error("createToken-property no set");
			exceptions.add(new ValidationException("createToken-property not set",this));
		}
		
		if (getUpdateComment()==null) {
			log.error("updateComment-property no set");
			exceptions.add(new ValidationException("updateComment-property not set",this));
		}
		
		if (getOtherSystemVersionInSFEETargetFieldname()==null) {
			log.error("otherSystemVersionInSFEETargetFieldname-property no set");
			exceptions.add(new ValidationException("otherSystemVersionInSFEETargetFieldname-property no set",this));
		}
		
		trackerHandler=new SFEETrackerHandler(getServerUrl());
	}

	public void setCreateToken(String createToken) {
		CreateToken = createToken;
	}

	public String getCreateToken() {
		return CreateToken;
	}

//	public void setNoFlexFields(HashSet<String> noFlexFields) {
//		this.noFlexFields = noFlexFields;
//	}
//
//	public HashSet<String> getNoFlexFields() {
//		return noFlexFields;
//	}

	public Object[] process(Object data) {
		if (data == null) {
		      throw new NullRecordException("Expected Document. Null record not permitted.");
		    }

		    if (!(data instanceof Document)) {
		      throw new RecordFormatException("Expected Document. Got [" + data.getClass().getName() + "]");
		    }
		    
		    return processXMLDocument((Document) data);
	}

	public void reset(Object context) {
	}

	public void setUpdateComment(String updateComment) {
		this.updateComment = updateComment;
	}

	public String getUpdateComment() {
		return updateComment;
	}

	public void setOtherSystemVersionInSFEETargetFieldname(
			String otherSystemVersionInSFEETargetFieldname) {
		this.otherSystemVersionInSFEETargetFieldname = otherSystemVersionInSFEETargetFieldname;
	}

	public String getOtherSystemVersionInSFEETargetFieldname() {
		return otherSystemVersionInSFEETargetFieldname;
	}

	public void setLastSynchronizedWithOtherSystemSFEETargetFieldname(
			String lastSynchronizedWithOtherSystemSFEETargetFieldname) {
		this.lastSynchronizedWithOtherSystemSFEETargetFieldname = lastSynchronizedWithOtherSystemSFEETargetFieldname;
	}

	public String getLastSynchronizedWithOtherSystemSFEETargetFieldname() {
		return lastSynchronizedWithOtherSystemSFEETargetFieldname;
	}
}
