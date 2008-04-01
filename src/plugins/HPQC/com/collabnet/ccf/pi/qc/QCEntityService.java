package com.collabnet.ccf.pi.qc;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;




public class QCEntityService extends QCConnectHelper implements
		IDataProcessor {
	
	public QCEntityService() {
		super();
	}

	private static final Log log = LogFactory.getLog(QCEntityService.class);
	/*private String CreateToken;
	private String synchronizationUser;
	private String otherSystemInQCTargetFieldname;
	*/
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
		
		System.out.println("Inside QCEntityService, the incoming document is ::" + data.asXML());
		System.out.println("***************************************************");
		Document filledArtifactDocument = null;
		GenericArtifact genericArtifact = new GenericArtifact();
		try {
			genericArtifact = GenericArtifactHelper.createGenericArtifactJavaObject(data);
			//genericArtifact = fillInRequiredFields(genericArtifact);
		}
		catch(Exception e) {
			System.out.println("GenericArtifact Parsing exception" + e);
		}
		String sourceArtifactId = genericArtifact.getSourceArtifactId();
		String sourceSystemId  = genericArtifact.getSourceSystemId();
		String sourceRepositoryId = genericArtifact.getSourceRepositoryId();
		String targetArtifactId = genericArtifact.getTargetArtifactId();
		String targetSystemId = genericArtifact.getTargetSystemId();
		String targetRepositoryId = genericArtifact.getTargetRepositoryId();
		
		String targetArtifactIdFromTable = getTargetArtifactIdFromTable(sourceArtifactId, sourceSystemId, sourceRepositoryId, targetSystemId, targetRepositoryId);
		
		if(targetArtifactIdFromTable!=null && !(targetArtifactIdFromTable.equals("NEW"))) {
	    	genericArtifact.setTargetArtifactId(targetArtifactIdFromTable);
	    }
		if(targetArtifactIdFromTable==null) {
	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.UPDATE)) {
	    		//Send this artifact to HOSPITAL
	    	}
	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.CREATE)) {
	    		//Insert a new record in the QC_ENTITY_CHECK Hsql table with the targetArtifactId value as "NEW". 
	    		//This should be updated by the QCWriter after creating a defect.
	    		Boolean insertStatus = insertRecordInTable(sourceArtifactId, sourceSystemId, sourceRepositoryId, targetSystemId, targetRepositoryId);
	    		
	    	}
	    }
		
	    try {
	    	filledArtifactDocument = GenericArtifactHelper.createGenericArtifactXMLDocument(genericArtifact);
	    }
	    catch(Exception e) {
	    	log.error("Exception while converting the resultantGenericArtifact into the resultDocument in QCEntityService:"+e);
	    }
		
	    System.out.println("Inside QCEntityService, after filing in the targetArtifactId::" + filledArtifactDocument.asXML());
	    
	    Object[] result = {filledArtifactDocument};
		return result;
	}
	
	
	public static String getTargetArtifactIdFromTable(String sourceArtifactId, String sourceSystemId, String sourceRepositoryId, String targetSystemId, String targetRepositoryId) {
		
	    /* 1. Get the values of sourceArtifactId, sourceSystemId, sourceRepositoryId, targetArtifactId, targetSystemId, targetRepositoryId
	      from the incoming GenericArtifact.
	    2. Query the HSQL Table for the presence of a combination of sourceSystemId, sourceRepositoryId,targetSystemId, targetRepositoryId.
	    3. If such a record exists, get the value of targetArtifactId for the given sourceArtifactId and populate the GenericArtifact and send it to QCWriter.
	    4. If such a record doesn't exists, that Artifact is for creation (check is the artifactAction is CREATE or not, 
	   	  handle that properly. The QCWriter should insert a new record in that table for this targetArtifactId.
	    5.  
	    */
		
		String targetArtifactIdFromTable = null;
		String sql = "SELECT TARGET_ARTIFACT_ID FROM QC_ENTITY_CHECK WHERE SOURCE_SYSTEM_ID= '"+sourceSystemId+"' AND ";
		       sql+= "SOURCE_REPOSITORY_ID= '"+sourceRepositoryId+"' AND TARGET_SYSTEM_ID='"+targetSystemId+"' AND ";
		       sql+= "TARGET_REPOSITORY_ID='"+targetRepositoryId+"' AND SOURCE_ARTIFACT_ID='"+sourceArtifactId+"'";
		
		log.info("QCEntityService SQL Query:"+ sql);       
		try {
			ResultSet rs = executeSql(sql);
			//while(!(rs.isLast()) ) {
			if(rs!=null) {
			for (; rs.next(); ) {
				if(rs.getObject(1)!=null)
					targetArtifactIdFromTable= rs.getObject(1).toString();
		      	System.out.println(targetArtifactIdFromTable);
		      	rs.next();
		      }
			}
		}
		catch(Exception e) {
			log.error("Exception while executing the Query in QCEntityService:"+e);
		}
	    
		return targetArtifactIdFromTable;
	}
	
	public boolean insertRecordInTable(String sourceArtifactId, String sourceSystemId, String sourceRepositoryId, String targetSystemId, String targetRepositoryId) {
		
		Boolean status = false;
		
		String sql = "INSERT INTO QC_ENTITY_CHECK VALUES('"+sourceSystemId+"', '"+sourceRepositoryId+"', '"+sourceArtifactId+"', '"+targetSystemId+"', '"+targetRepositoryId+"', 'NEW')";
		log.info("QCEntityService INSERT SQL Query:"+ sql); 
		ResultSet rs = null;
		try {
			rs = executeSql(sql);
		}
		catch(Exception e) {
			log.error("Exception while executing the INSERT Query in QCEntityService:"+e);
		}
		if(rs==null) return false;
		else return true;
	}
	
	public static ResultSet executeSql(String sql) throws ClassNotFoundException, SQLException {
	  	Connection conn;                                                
        Class.forName("org.hsqldb.jdbcDriver");
        conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
        Statement st = null;
        ResultSet rs = null;
        st = conn.createStatement();         // statement objects can be reused with
        rs = st.executeQuery(sql);    // run the query
        return rs;
  }
 
	
	
	public void reset(Object context) {
	}

	/*public void setSynchronizationUser(String synchronizationUser) {
		this.synchronizationUser = synchronizationUser;
	}

	public String getSynchronizationUser() {
		return synchronizationUser;
	}
	*/
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		/*
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
		*/
		// Create tracker handler
		defectHandler = new QCDefectHandler();
	}

	/*public void setOtherSystemInQCTargetFieldname(String sFEEmappingID) {
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
	*/

}
