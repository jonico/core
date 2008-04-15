package com.collabnet.ccf.core.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.openadaptor.auxil.connector.jdbc.JDBCConnection;
import org.openadaptor.core.IDataProcessor;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;

public class MappingDBUpdater implements IDataProcessor{
	JDBCConnection jdbcConnection = null;
	String sql = "UPDATE REPOSITORY_MAPPING SET LAST_READ_TIME=? WHERE ID=(SELECT REPOSITORY_MAPPING.ID" 
					+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B, ARTIFACT_MAPPING WHERE"
					+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
					+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
					+" A.REPOSITORY_ID=? AND"
					+" B.REPOSITORY_ID=? AND"
					+" REPOSITORY_MAPPING.ID=ARTIFACT_MAPPING.MAPPING_ID AND"
					+" ARTIFACT_MAPPING.SOURCE_ARTIFACT_ID=? AND"
					+" ARTIFACT_MAPPING.TARGET_ARTIFACT_ID=?)";
	
	String qcSql = "UPDATE REPOSITORY_MAPPING SET TRANSACTION_ID=? WHERE ID=(SELECT REPOSITORY_MAPPING.ID" 
		+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B, ARTIFACT_MAPPING WHERE"
		+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
		+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
		+" A.REPOSITORY_ID=? AND"
		+" B.REPOSITORY_ID=? AND"
		+" REPOSITORY_MAPPING.ID=ARTIFACT_MAPPING.MAPPING_ID AND"
		+" ARTIFACT_MAPPING.SOURCE_ARTIFACT_ID=? AND"
		+" ARTIFACT_MAPPING.TARGET_ARTIFACT_ID=?)";
	
	public Object[] process(Object data) {
		// I will expect a Generic Artifact object
		if(data instanceof Document){
			System.out.println(((Document)data).asXML());
			GenericArtifact ga = null;
			try {
				ga = GenericArtifactHelper.createGenericArtifactJavaObject((Document)data);
			} catch (GenericArtifactParsingException e1) {
				throw new RuntimeException(e1);
			}
			
			if(ga.getArtifactAction().equals(GenericArtifact.ArtifactActionValue.UNKNOWN)) {
				Object [] result = {data};
				return result;
			}
			
			String lastModifiedDateString = ga.getArtifactLastModifiedDate();
			String lastReadTransactionId = ga.getLastReadTransactionId();
			String sourceRepositoryID = ga.getSourceRepositoryId();
			String targetRepositoryID = ga.getTargetRepositoryId();
			String sourceArtifactID = ga.getSourceArtifactId();
			String targetArtifactID = ga.getTargetArtifactId();
			java.util.Date lastModifiedDate = null;
			if(lastModifiedDateString.equalsIgnoreCase("Unknown")){
				lastModifiedDate = new Date();
				return new Object[]{};
			}
			else {
				lastModifiedDate = DateUtil.parse(lastModifiedDateString);
			}
			java.sql.Timestamp time = new java.sql.Timestamp(lastModifiedDate.getTime());
			java.sql.Date sqlDate = new java.sql.Date(lastModifiedDate.getTime());
			PreparedStatement pstmt = null;;
			try {
				if (!jdbcConnection.isConnected()) {
					jdbcConnection.connect();
				}
				Connection dbConnection = jdbcConnection.getConnection();
				dbConnection.setAutoCommit(false);
				pstmt = dbConnection.prepareStatement(sql);
				pstmt.setTimestamp(1, time);
				pstmt.setString(2, sourceRepositoryID);
				pstmt.setString(3, targetRepositoryID);
				pstmt.setString(4, sourceArtifactID);
				pstmt.setString(5, targetArtifactID);
				int numRecordsAffected = pstmt.executeUpdate();
				if(numRecordsAffected != 1){
					dbConnection.rollback();
					throw new RuntimeException("How come I am updating two repository mapping....?");
				}
				else{
					dbConnection.commit();
					dbConnection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			finally{
				if(pstmt != null){
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(!StringUtils.isEmpty(lastReadTransactionId)&& (lastReadTransactionId!=null && !lastReadTransactionId.equals("unknown")) ) {
			//updating the transaction_id for QC
			System.out.println(qcSql);	
			try {
				if (!jdbcConnection.isConnected()) {
					jdbcConnection.connect();
				}
				Connection dbConnection = jdbcConnection.getConnection();
				dbConnection.setAutoCommit(false);
				pstmt = dbConnection.prepareStatement(qcSql);
				pstmt.setString(1, lastReadTransactionId);
				pstmt.setString(2, sourceRepositoryID);
				pstmt.setString(3, targetRepositoryID);
				pstmt.setString(4, sourceArtifactID);
				pstmt.setString(5, targetArtifactID);
				int numRecordsAffected = pstmt.executeUpdate();
				if(numRecordsAffected > 1){
					dbConnection.rollback();
					throw new RuntimeException("How come I am updating two repository mapping....?"+numRecordsAffected);
				}
				else{
					dbConnection.commit();
					dbConnection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			finally{
				if(pstmt != null){
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			}
			
			
			
		}
		else {
			String message = "The Mapping updater needs a GenericArtifact object";
			message += " But it got something else.";
			throw new RuntimeException(message);
		}
		// TODO Auto-generated method stub
		return null;
	}

	public void reset(Object context) {
		// TODO Auto-generated method stub
		
	}

	public void validate(List exceptions) {
		// TODO Auto-generated method stub
		
	}

	public JDBCConnection getJdbcConnection() {
		return jdbcConnection;
	}

	public void setJdbcConnection(JDBCConnection dbConnection) {
		this.jdbcConnection = dbConnection;
	}

}
