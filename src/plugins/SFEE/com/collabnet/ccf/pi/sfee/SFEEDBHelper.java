package com.collabnet.ccf.pi.sfee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.Node;
import org.openadaptor.auxil.connector.jdbc.JDBCConnection;

public class SFEEDBHelper {
	private JDBCConnection jdbcConnection;
	String targetArtifactIDupdateQuery = "UPDATE ARTIFACT_MAPPING SET TARGET_ARTIFACT_ID=? WHERE ID=(SELECT ARTIFACT_MAPPING.ID" 
				+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B, ARTIFACT_MAPPING WHERE"
				+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
				+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
				+" A.REPOSITORY_ID=? AND"
				+" B.REPOSITORY_ID=? AND"
				+" REPOSITORY_MAPPING.ID=ARTIFACT_MAPPING.MAPPING_ID AND"
//				+" ARTIFACT_MAPPING.MAPPING_ID = ? AND"
				+" ARTIFACT_MAPPING.SOURCE_ARTIFACT_ID=?)";
	
	String targetArtifactIDQuery = "SELECT ARTIFACT_MAPPING.TARGET_ARTIFACT_ID" 
		+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B, ARTIFACT_MAPPING WHERE"
		+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
		+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
		+" A.REPOSITORY_ID=? AND"
		+" B.REPOSITORY_ID=? AND"
		+" REPOSITORY_MAPPING.ID=ARTIFACT_MAPPING.MAPPING_ID AND"
//		+" ARTIFACT_MAPPING.MAPPING_ID = ? AND"
		+" ARTIFACT_MAPPING.SOURCE_ARTIFACT_ID=?";
	String sourceArtifactIDInsertQuery = "INSERT INTO ARTIFACT_MAPPING (SOURCE_ARTIFACT_ID,MAPPING_ID)"
		+" VALUES (?, (SELECT REPOSITORY_MAPPING.ID" 
		+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B WHERE"
		+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
		+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
		+" A.REPOSITORY_ID=? AND"
		+" B.REPOSITORY_ID=?"
		+" ))";
	String sourceArtifactCheckQuery = "SELECT REPOSITORY_MAPPING.ID"
			+" FROM REPOSITORY_MAPPING, REPOSITORY_INFO A, REPOSITORY_INFO B,"
			+" ARTIFACT_MAPPING WHERE"
			+" A.ID=REPOSITORY_MAPPING.SOURCE_REPOSITORY_INFO_ID AND"
			+" B.ID=REPOSITORY_MAPPING.TARGET_REPOSITORY_INFO_ID AND"
			+" A.REPOSITORY_ID=? AND"
			+" B.REPOSITORY_ID=? AND"
			+" ARTIFACT_MAPPING.MAPPING_ID=REPOSITORY_MAPPING.ID AND"
			+" ARTIFACT_MAPPING.SOURCE_ARTIFACT_ID=?";
	public void updateTargetArtifactID(String targetArtifactId, String sourceArtifactId,
			String sourceRepositoryId, String targetRepositoryId){
		try {
			jdbcConnection.connect();
			Connection con = jdbcConnection.getConnection();
			
			PreparedStatement pstmt = con.prepareStatement(targetArtifactIDupdateQuery);
			pstmt.setString(1, targetArtifactId);
			pstmt.setString(2, sourceRepositoryId);
			pstmt.setString(3, targetRepositoryId);
			pstmt.setString(4, sourceArtifactId);
			int rowsAffected = pstmt.executeUpdate();
			pstmt.close();
			
			
			jdbcConnection.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public String getTargetArtifactID(String sourceArtifactId,
			String sourceRepositoryId, String targetRepositoryId){
		String targetArtifactId = null;
		try {
			jdbcConnection.connect();
			Connection con = jdbcConnection.getConnection();
			
			PreparedStatement pstmt = con.prepareStatement(targetArtifactIDQuery);
			pstmt.setString(1, sourceRepositoryId);
			pstmt.setString(2, targetRepositoryId);
			pstmt.setString(3, sourceArtifactId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				targetArtifactId = rs.getString(1);
			}
			pstmt.close();
			jdbcConnection.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return targetArtifactId;
	}
	public void insertSourceArtifactID(String sourceArtifactId, String sourceRepositoryId,
							String targetRepositoryId){
		try {
			jdbcConnection.connect();
			Connection con = jdbcConnection.getConnection();
			PreparedStatement pstmtSrcArtfCheckSQL = con.prepareStatement(sourceArtifactCheckQuery);
			pstmtSrcArtfCheckSQL.setString(1, sourceRepositoryId);
			pstmtSrcArtfCheckSQL.setString(2, targetRepositoryId);
			pstmtSrcArtfCheckSQL.setString(3, sourceArtifactId);
			ResultSet rs = pstmtSrcArtfCheckSQL.executeQuery();
			if(!rs.next()){
				PreparedStatement pstmt = con.prepareStatement(sourceArtifactIDInsertQuery);
				pstmt.setString(1, sourceArtifactId);
				pstmt.setString(2, sourceRepositoryId);
				pstmt.setString(3, targetRepositoryId);
				int rowsAffected = pstmt.executeUpdate();
				pstmt.close();
			}
			rs.close();
			pstmtSrcArtfCheckSQL.close();
			jdbcConnection.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
			//throw new RuntimeException(e);
		}
		
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

	public JDBCConnection getJdbcConnection() {
		return jdbcConnection;
	}

	public void setJdbcConnection(JDBCConnection jdbcConnection) {
		this.jdbcConnection = jdbcConnection;
	}
}
