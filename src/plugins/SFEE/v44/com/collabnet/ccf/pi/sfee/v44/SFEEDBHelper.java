package com.collabnet.ccf.pi.sfee.v44;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dom4j.Document;
import org.dom4j.Node;

public class SFEEDBHelper {
//	String targetArtifactIDupdateQuery = "UPDATE ARTIFACT_MAPPING SET TARGET_ARTIFACT_ID=? WHERE ID=(SELECT ARTIFACT_MAPPING.ID" 
//				+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B, ARTIFACT_MAPPING WHERE"
//				+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
//				+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
//				+" A.REPOSITORY_ID=? AND"
//				+" B.REPOSITORY_ID=? AND"
//				+" REPOSITORY_MAPPING.ID=ARTIFACT_MAPPING.MAPPING_ID AND"
////				+" ARTIFACT_MAPPING.MAPPING_ID = ? AND"
//				+" ARTIFACT_MAPPING.SOURCE_ARTIFACT_ID=?)";
	
//	String targetArtifactIDQuery = "SELECT ARTIFACT_MAPPING.TARGET_ARTIFACT_ID" 
//		+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B, ARTIFACT_MAPPING WHERE"
//		+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
//		+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
//		+" A.REPOSITORY_ID=? AND"
//		+" B.REPOSITORY_ID=? AND"
//		+" REPOSITORY_MAPPING.ID=ARTIFACT_MAPPING.MAPPING_ID AND"
////		+" ARTIFACT_MAPPING.MAPPING_ID = ? AND"
//		+" ARTIFACT_MAPPING.SOURCE_ARTIFACT_ID=?";
//	String sourceArtifactIDInsertQuery = "INSERT INTO ARTIFACT_MAPPING (SOURCE_ARTIFACT_ID,MAPPING_ID)"
//		+" VALUES (?, (SELECT REPOSITORY_MAPPING.ID" 
//		+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B WHERE"
//		+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
//		+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
//		+" A.REPOSITORY_ID=? AND"
//		+" B.REPOSITORY_ID=?"
//		+" ))";
//	String sourceArtifactCheckQuery = "SELECT REPOSITORY_MAPPING.ID"
//			+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B,"
//			+" ARTIFACT_MAPPING WHERE"
//			+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
//			+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
//			+" A.REPOSITORY_ID=? AND"
//			+" B.REPOSITORY_ID=? AND"
//			+" ARTIFACT_MAPPING.MAPPING_ID=REPOSITORY_MAPPING.ID AND"
//			+" ARTIFACT_MAPPING.SOURCE_ARTIFACT_ID=?";

	public static boolean updateTransactionIdInHsqlTable(String newTransactionId, String sourceRepositoryId, String sourceRepositoryKind, String sourceSystemId, String sourceSystemKind, String targetRepositoryId, String targetRepositoryKind, String targetSystemId, String targetSystemKind) {
		
		Boolean status=true;
		//Timestamp fromDateInTimestamp=convertIntoTimestamp(newFromTime);
		
		String sql = "UPDATE REPOSITORY_MAPPING SET TRANSACTION_ID="+newTransactionId; 
			sql+= " WHERE ";
			
			sql+= "REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID ="; 
			sql+= "(SELECT ID FROM REPOSITORY_INFO WHERE ";
			sql+= "REPOSITORY_INFO.REPOSITORY_ID='"+sourceRepositoryId+"' AND ";
			sql+= "REPOSITORY_INFO.REPOSITORY_KIND='"+sourceRepositoryKind+"' AND ";			
			sql+= "REPOSITORY_INFO.SYSTEM_INFO_ID= ";
			sql+= "(SELECT ID FROM SYSTEM_INFO WHERE ";
			sql+= "SYSTEM_INFO.SYSTEM_ID='"+sourceSystemId+"' AND ";
			sql+= "SYSTEM_INFO.SYSTEM_KIND='"+sourceSystemKind+"' )) AND ";
			
			sql+= "REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID = "; 
			sql+= "(SELECT ID FROM REPOSITORY_INFO WHERE ";
			sql+= "REPOSITORY_INFO.REPOSITORY_ID='"+targetRepositoryId+"' AND ";
			sql+= "REPOSITORY_INFO.REPOSITORY_KIND='"+targetRepositoryKind+"' AND ";
			sql+= "REPOSITORY_INFO.SYSTEM_INFO_ID= ";
			sql+= "(SELECT ID FROM SYSTEM_INFO WHERE ";
			sql+= "SYSTEM_INFO.SYSTEM_ID='"+targetSystemId+"' AND ";
			sql+= "SYSTEM_INFO.SYSTEM_KIND='"+targetSystemKind+"' ))";
		ResultSet rs = null;
		try {
			rs = executeSql(sql);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		if(rs==null) return false;	
		return status;
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
	   ResultSet rs = null;
	   String mappingId=null;
	   int rsCount=0;
	   try {
		    rs = executeSql(sql1);
			if(rs.next())
				mappingId = rs.getString("ID");
		   }
	   catch(Exception e) {
		   	throw new RuntimeException(e);
		   }       
		
	   return mappingId;
	
	}
	
	public static synchronized boolean insertRecordInTable(String sourceArtifactId, String sourceSystemId, String sourceSystemKind, String sourceRepositoryId, String sourceRepositoryKind, String targetSystemId, String targetSystemKind, String targetRepositoryId, String targetRepositoryKind) {
		Boolean status = false;
		
		String mappingId = SFEEDBHelper.getMappingIdFromTable(
				sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind,
				targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind);
		String sql = "INSERT INTO ARTIFACT_MAPPING(MAPPING_ID, SOURCE_ARTIFACT_ID, TARGET_ARTIFACT_ID) VALUES("+mappingId+",'"+sourceArtifactId+"', 'NEW')";
		ResultSet rs = null;
		try {
			rs = executeSql(sql);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		if(rs==null) return false;
		else return true;
	}
	
	public static String getTargetArtifactIdFromTable(String sourceArtifactId, String sourceSystemId,
			String sourceSystemKind, String sourceRepositoryId, String sourceRepositoryKind,
			String targetSystemId, String targetSystemKind, String targetRepositoryId,
			String targetRepositoryKind) {
		
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
		       
  
		try {
			ResultSet rs = executeSql(sql);
			if(rs!=null && rs.next())
				targetArtifactIdFromTable = rs.getString("TARGET_ARTIFACT_ID");
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	    
		return targetArtifactIdFromTable;
	}
	
	public static String getSourceArtifactIdFromTable(String targetArtifactId, String sourceSystemId,
			String sourceSystemKind, String sourceRepositoryId, String sourceRepositoryKind,
			String targetSystemId, String targetSystemKind, String targetRepositoryId,
			String targetRepositoryKind) {
		
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
		String sql= "SELECT SOURCE_ARTIFACT_ID FROM ARTIFACT_MAPPING WHERE ";
		       sql+= "TARGET_ARTIFACT_ID='"+targetArtifactId+"' AND ";
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
		       
  
		try {
			ResultSet rs = executeSql(sql);
			if(rs!=null && rs.next())
				targetArtifactIdFromTable = rs.getString("TARGET_ARTIFACT_ID");
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	    
		return targetArtifactIdFromTable;
	}
	
	public static synchronized boolean updateTable(String sourceArtifactId, String sourceSystemId, String sourceSystemKind, String sourceRepositoryId, String sourceRepositoryKind, String targetArtifactIdAfterCreation, String targetSystemId, String targetSystemKind, String targetRepositoryId, String targetRepositoryKind) {
		
		Boolean status = false;
		System.out.println();
		String mappingId = SFEEDBHelper.getMappingIdFromTable(sourceSystemId, sourceSystemKind,
				sourceRepositoryId, sourceRepositoryKind, targetSystemId, targetSystemKind,
				targetRepositoryId, targetRepositoryKind);
		
		String reverseMappingId = SFEEDBHelper.getMappingIdFromTable(
				targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind,
				sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind);
		
		String sql = "UPDATE ARTIFACT_MAPPING SET TARGET_ARTIFACT_ID='"+targetArtifactIdAfterCreation+"' WHERE ";
			   sql+= "MAPPING_ID='"+mappingId+"' AND SOURCE_ARTIFACT_ID='"+sourceArtifactId+"'";
		ResultSet rs = null;
		try {
			rs = executeSql(sql);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		if(reverseMappingId != null){
			String reverseMappingTargetArtifactId = SFEEDBHelper.getTargetArtifactIdFromTable(
					targetArtifactIdAfterCreation,
					targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind,
					sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind);
			if(reverseMappingTargetArtifactId == null){
				SFEEDBHelper.insertRecordInTable(targetArtifactIdAfterCreation,
						targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind,
						sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind);
				SFEEDBHelper.updateTable(targetArtifactIdAfterCreation, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind,
						sourceArtifactId, sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind);
			}
			else if(reverseMappingTargetArtifactId.equals("NEW")){
				SFEEDBHelper.updateTable(targetArtifactIdAfterCreation, targetSystemId, targetSystemKind, targetRepositoryId, targetRepositoryKind,
						sourceArtifactId, sourceSystemId, sourceSystemKind, sourceRepositoryId, sourceRepositoryKind);
			}
			else if(reverseMappingTargetArtifactId.equals(sourceArtifactId)){
				System.out.println("Artifact mapping is already done");
			}
			else {
				throw new RuntimeException("Artifact is already mapped to some other artifact");
			}
		}
		
		if(rs==null) return false;
		else return true;
	}
	
	public static ResultSet executeSql(String sql) throws ClassNotFoundException, SQLException {
	  	
	    Statement st = null;
	    ResultSet rs = null;
	    Connection con = getConnection();
	    st = con.createStatement();         // statement objects can be reused with
	    rs = st.executeQuery(sql);    // run the query
	    con.close();
	    return rs;
	}
	public static Connection getConnection() throws SQLException{
		Connection conn;                                                
	    try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not load the driver class ",e);
		}
	    conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
	    return conn;
	}
	public String getToTime(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TO_TIME");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getFromTime(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//FROM_TIME");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getSourceArtifactId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_ARTIFACT_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getSourceRepositoryId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getSourceRepositoryKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getSourceSystemId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getSourceSystemKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getTargetArtifactId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_ARTIFACT_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getTargetRepositoryId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getTargetRepositoryKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	public String getTargetSystemId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	public String getTargetSystemKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}
}
