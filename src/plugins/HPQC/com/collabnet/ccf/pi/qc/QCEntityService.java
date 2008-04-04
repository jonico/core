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
		String sourceSystemKind = genericArtifact.getSourceSystemKind();
		String sourceRepositoryId = genericArtifact.getSourceRepositoryId();
		String sourceRepositoryKind = genericArtifact.getSourceRepositoryKind();
		
		String targetArtifactId = genericArtifact.getTargetArtifactId();
		String targetSystemId = genericArtifact.getTargetSystemId();
		String targetSystemKind = genericArtifact.getTargetSystemKind();
		String targetRepositoryId = genericArtifact.getTargetRepositoryId();
		String targetRepositoryKind = genericArtifact.getTargetRepositoryKind();
		
		String targetArtifactIdFromTable = getTargetArtifactIdFromTable(sourceArtifactId, sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
		
		if(targetArtifactIdFromTable!=null && !(targetArtifactIdFromTable.equals("NEW")) && !(targetArtifactIdFromTable.equals("NULL"))) {
	    	genericArtifact.setTargetArtifactId(targetArtifactIdFromTable);
	    }
		if(targetArtifactIdFromTable==null) {
	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.UPDATE)) {
	    		//Send this artifact to HOSPITAL
	    	}
	    	if(genericArtifact.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.CREATE)) {
	    		//Insert a new record in the QC_ENTITY_CHECK Hsql table with the targetArtifactId value as "NEW". 
	    		//This should be updated by the QCWriter after creating a defect.
	    		Boolean insertStatus = insertRecordInTable(sourceArtifactId, sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
	    		
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
	
	
	public static String getTargetArtifactIdFromTable(String sourceArtifactId, String sourceSystemId, String sourceSystemKind, String sourceRepositoryId, String sourceRepositoryKind, String targetSystemId, String targetSystemKind, String targetRepositoryId, String targetRepositoryKind) {
		
	    /* 1. Get the values of sourceArtifactId, sourceSystemId, sourceRepositoryId, targetArtifactId, targetSystemId, targetRepositoryId
	      from the incoming GenericArtifact.
	    2. Query the HSQL Table for the presence of a combination of sourceSystemId, sourceRepositoryId,targetSystemId, targetRepositoryId.
	    3. If such a record exists, get the value of targetArtifactId for the given sourceArtifactId and populate the GenericArtifact and send it to QCWriter.
	    4. If such a record doesn't exists, that Artifact is for creation (check is the artifactAction is CREATE or not, 
	   	  handle that properly. The QCWriter should insert a new record in that table for this targetArtifactId.
	    5.  
	    */
		int rsCount=0;
		String targetArtifactIdFromTable = null;
		String sql= "SELECT TARGET_ARTIFACT_ID FROM ARTIFACT_MAPPING WHERE ";
		       sql+= "SOURCE_ARTIFACT_ID='"+sourceArtifactId+"' AND ";
		       sql+= "MAPPING_ID = ";
		       sql+= "(select id FROM repository_mapping WHERE ";
		       sql+= "SOURCE_REPOSITORY_INFO_ID="; 
		       sql+= "(SELECT ID FROM REPOSITORY_INFO WHERE "; 
		       sql+= "REPOSITORY_INFO.REPOSITORY_ID='"+sourceRepositoryId+"' AND ";
		       sql+= "REPOSITORY_INFO.REPOSITORY_KIND='"+sourceRepositoryKind+"' AND ";
		       sql+= "REPOSITORY_INFO.SYSTEM_INFO_ID= "; 
		       sql+= "(SELECT ID FROM SYSTEM_INFO WHERE "; 
		       sql+= "SYSTEM_INFO.SYSTEM_ID='"+sourceSystemId+"' AND ";
		       sql+= "SYSTEM_INFO.SYSTEM_KIND='"+sourceSystemKind+"')) ";
		       sql+= "AND ";
		       sql+= "TARGET_REPOSITORY_INFO_ID= ";
		       sql+= "(SELECT ID FROM REPOSITORY_INFO WHERE "; 
		       sql+= "REPOSITORY_INFO.REPOSITORY_ID='"+targetRepositoryId+"' AND ";
		       sql+= "REPOSITORY_INFO.REPOSITORY_KIND='"+targetRepositoryKind+"' AND ";
		       sql+= "REPOSITORY_INFO.SYSTEM_INFO_ID= "; 
		       sql+= "(SELECT ID FROM SYSTEM_INFO WHERE "; 
		       sql+= "SYSTEM_INFO.SYSTEM_ID='"+targetSystemId+"' AND ";
		       sql+= "SYSTEM_INFO.SYSTEM_KIND='"+targetSystemKind+"')) ) ";
		       
		log.info("QCEntityService SQL Query:"+ sql);       
		try {
			ResultSet rs = executeSql(sql);
			if(rs!=null && rs.next())
				targetArtifactIdFromTable = rs.getString("TARGET_ARTIFACT_ID");
		}
		catch(Exception e) {
			log.error("Exception while executing the Query in QCEntityService:"+e);
		}
	    
		return targetArtifactIdFromTable;
	}
	
	public boolean insertRecordInTable(String sourceArtifactId, String sourceSystemId, String sourceSystemKind, String sourceRepositoryId, String sourceRepositoryKind, String targetSystemId, String targetSystemKind, String targetRepositoryId, String targetRepositoryKind) {
		
		Boolean status = false;
		
		String mappingId = getMappingIdFromTable(sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
		String sql = "INSERT INTO ARTIFACT_MAPPING(MAPPING_ID, SOURCE_ARTIFACT_ID, TARGET_ARTIFACT_ID) VALUES('"+mappingId+"','"+sourceArtifactId+"', 'NEW')";
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
	
	
	public static String getMappingIdFromTable(String sourceSystemId, String sourceSystemKind, String sourceRepositoryId, String sourceRepositoryKind, String targetSystemId, String targetSystemKind, String targetRepositoryId, String targetRepositoryKind) {
		
		String sql1= "select ID FROM repository_mapping WHERE ";
	       sql1+= "SOURCE_REPOSITORY_INFO_ID="; 
	       sql1+= "(SELECT ID FROM REPOSITORY_INFO WHERE "; 
	       sql1+= "REPOSITORY_INFO.REPOSITORY_ID='"+sourceRepositoryId+"' AND ";
	       sql1+= "REPOSITORY_INFO.REPOSITORY_KIND='"+sourceRepositoryKind+"' AND ";
	       sql1+= "REPOSITORY_INFO.SYSTEM_INFO_ID= "; 
	       sql1+= "(SELECT ID FROM SYSTEM_INFO WHERE "; 
	       sql1+= "SYSTEM_INFO.SYSTEM_ID='"+sourceSystemId+"' AND ";
	       sql1+= "SYSTEM_INFO.SYSTEM_KIND='"+sourceSystemKind+"')) ";
	       sql1+= "AND ";
	       sql1+= "TARGET_REPOSITORY_INFO_ID= ";
	       sql1+= "(SELECT ID FROM REPOSITORY_INFO WHERE "; 
	       sql1+= "REPOSITORY_INFO.REPOSITORY_ID='"+targetRepositoryId+"' AND ";
	       sql1+= "REPOSITORY_INFO.REPOSITORY_KIND='"+targetRepositoryKind+"' AND ";
	       sql1+= "REPOSITORY_INFO.SYSTEM_INFO_ID= "; 
	       sql1+= "(SELECT ID FROM SYSTEM_INFO WHERE "; 
	       sql1+= "SYSTEM_INFO.SYSTEM_ID='"+targetSystemId+"' AND ";
	       sql1+= "SYSTEM_INFO.SYSTEM_KIND='"+targetSystemKind+"'))";
	   log.info(sql1);    
	   ResultSet rs = null;
	   String mappingId=null;
	   int rsCount=0;
	   try {
		    rs = executeSql(sql1);
			if(rs.next())
				mappingId = rs.getString("ID");
		   }
	   catch(Exception e) {
		   log.error("Exception while executing the getMappingIdFromTable Query in QCEntityService:"+e);
		   }       
		
	   return mappingId;
	
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
