package com.collabnet.ccf.pi.qc;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.openadaptor.auxil.connector.iostream.EncodingAwareObject;
import org.openadaptor.core.IDataProcessor;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;

public class QCReader extends QCConnectHelper implements
		IDataProcessor {
    
	private static final Log log = LogFactory.getLog(QCReader.class);
	
	private boolean isDry=true;

	private Object readerContext;

	private QCDefectHandler defectHandler;

    public QCReader(String id) {
	    super(id);
	}

	public Object[] process(Object data) {
		// TODO evaluate data to decide which items to fetch again
		if (!(data instanceof Document)) {
			log.error("Supplied data not in the expected dom4j format: "+data);
			return null;
		}
			
		Document document=(Document) data;
		if(document!=null) log.info(document.getText());
		try {
			connect();
			qcc.connectProjectEx(getDomain(), getProjectName(), getUserName(), getPassword());
		} catch (Exception e) {
			// TODO Declare exception so that it can be processed by OA exception handler
			log.error("Could not log into QC", e);
			return null;
		}

		// Fix these time operations
		String fromTimestamp = getFromTime(document);
		String fromTime = convertIntoString(fromTimestamp);
		String transactionId = getTransactionId(document);
		String sourceArtifactId = getSourceArtifactId(document); 
		String sourceRepositoryId = getSourceRepositoryId(document);
		String sourceRepositoryKind = getSourceRepositoryKind(document);
		String sourceSystemId = getSourceSystemId(document);
		String sourceSystemKind = getSourceSystemKind(document);
		
		String targetRepositoryId = getTargetRepositoryId(document);
		String targetRepositoryKind = getTargetRepositoryKind(document);
		String targetSystemId = getTargetSystemId(document);
		String targetSystemKind = getTargetSystemKind(document);
		
		log.error(fromTime);
		Object[] result=readModifiedDefects(transactionId, sourceArtifactId, sourceRepositoryId, sourceRepositoryKind, sourceSystemId, sourceSystemKind, targetRepositoryId, targetRepositoryKind, targetSystemId, targetSystemKind);
		disconnect();
		return result;
	}

	public Object getReaderContext() {
		return readerContext;
	}
	
	public boolean isDry() {
		return isDry;
	}

	@Override
	public void connect() throws IOException {
		log.info("Before calling the parent connect()");
		super.connect();
		isDry=false;
	}
	
	
	
	public Object[] readModifiedDefects(String transactionId, String sourceArtifactId, String sourceRepositoryId, String sourceRepositoryKind, String sourceSystemId, String sourceSystemKind, String targetRepositoryId, String targetRepositoryKind, String targetSystemId, String targetSystemKind) {
		// TODO Use the information of the firstTimeImport flag
		
		//Object[] retObj = new Object[100];
		List<List<Document>> retObj=new ArrayList<List<Document>>();
		List<Document> dataRows=new ArrayList<Document>();
		List<GenericArtifact> defectRows;
		
		try {
			log.error("The transactionId coming from HQSL DB is:" + transactionId);
			defectRows = defectHandler.getChangedDefects(this.getQcc(), transactionId, sourceArtifactId, sourceRepositoryId, sourceRepositoryKind, sourceSystemId, sourceSystemKind, targetRepositoryId, targetRepositoryKind, targetSystemId, targetSystemKind);
		} catch (Exception e) {
			// TODO Throw an exception?
			log.error("During the artifact retrieval process from QC, an error occured",e);
			return null;
		}
		
		if (defectRows==null || defectRows.size() == 0) {

			// Return an empty generic artifact
			// TODO: Should we fill in the latest from and to time?
			Document document = null;
			GenericArtifact emptyGenericArtifact = new GenericArtifact();
			emptyGenericArtifact = populateRequiredFields(emptyGenericArtifact);
			
			try {
				document = GenericArtifactHelper.createGenericArtifactXMLDocument(emptyGenericArtifact);
			}
			catch (GenericArtifactParsingException gape) {
				// TODO: Handle this appropriately
				log.error("GenericArtifactParsingException while creating a GenericArtifact XML from an empty GenericArtifact object");
			}
			
			return new Object[]{document};
		}
		 
		//String newFromTime = getLastModifiedDateFromLastArtifact(defectRows);
		//Boolean updateStatus = updateFromTimeInHsqlTable(newFromTime, sourceRepositoryId, sourceRepositoryKind, sourceSystemId, sourceSystemKind, targetRepositoryId, targetRepositoryKind, targetSystemId, targetSystemKind);		
		
		String newTransactionId = getLastTransactionIdFromLastArtifact(defectRows);
		Boolean updateStatus = updateTransactionIdInHsqlTable(newTransactionId, sourceRepositoryId, sourceRepositoryKind, sourceSystemId, sourceSystemKind, targetRepositoryId, targetRepositoryKind, targetSystemId, targetSystemKind);		
		
		for (GenericArtifact defectRow: defectRows) {
			Document document = null;
			try {
				document = GenericArtifactHelper.createGenericArtifactXMLDocument(defectRow);
			}
			catch (GenericArtifactParsingException gape) {
				// TODO: Handle this appropriately
				log.error("GenericArtifactParsingException while creating a GenericArtifact XML from the GenericArtifact object");
			}

			if (document != null) {
				log.error(document.asXML());
			}
			else {
				log.error("DOCUMENT IS NULL");
			}
			
			dataRows.add(document);
			
		}		
		retObj.add(dataRows);
		//s.writeObject(retObj);		
		/*}
		catch (IOException e) {
		log.error("IOException in QCReader caught " + e);	
		}		
		*/
		return dataRows.toArray();
		//return retObj.toArray();
	}
	
	@Override
	public void validate(List exceptions) {
		super.validate(exceptions);
		// Capture the return exception list and validate the exceptions
		
		defectHandler = new QCDefectHandler();
	}
	public GenericArtifact populateRequiredFields(GenericArtifact emptyGenericArtifact) {
		
		
		emptyGenericArtifact = QCConfigHelper.getSchemaFields(qcc);
		emptyGenericArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.UNKNOWN);
		emptyGenericArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.UNKNOWN);
		emptyGenericArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UNKNOWN);
		
		/*List<GenericArtifactField> allFields = emptyGenericArtifact.getAllGenericArtifactFields();
		for (GenericArtifactField thisField : allFields) {
			thisField.setFieldAction(GenericArtifactField.FieldActionValue.UNKNOWN);
		}
		*/
		return emptyGenericArtifact;
	}
	
	public String getLastTransactionIdFromLastArtifact(List<GenericArtifact> defectRows) {
		
		String newTransactionId = new String();
		int size = defectRows.size();
		GenericArtifact thisArtifact = defectRows.get(size-1);
		newTransactionId = thisArtifact.getLastReadTransactionId();
		return newTransactionId;
	}
	
	public Timestamp convertIntoTimestamp(String newFromTime) {
		Timestamp fromDateInTimestamp=null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date fromDate = sdf.parse(newFromTime); //like 2008-01-11 22:09:54
			fromDateInTimestamp = new Timestamp(fromDate.getTime());
			log.info("After parsing, the timestamp is:"+fromDateInTimestamp);
			}
		catch(Exception e) {
			log.error("Exception while parsing the string into Date"+e);
		}
		return fromDateInTimestamp;
	}
	
	public String convertIntoString(String fromTimeStamp) {
		String finalString=null;
		try {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date fromDate = sdf.parse(fromTimeStamp);
		finalString=ft.format(fromDate);
		System.out.println(finalString);
		}
		catch(Exception e) {
			log.error("Exception while parsing the string into Date"+e);
		}
		return finalString;		
	}
	
	public boolean updateTransactionIdInHsqlTable(String newTransactionId, String sourceRepositoryId, String sourceRepositoryKind, String sourceSystemId, String sourceSystemKind, String targetRepositoryId, String targetRepositoryKind, String targetSystemId, String targetSystemKind) {
		
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
		log.info(sql);	
		ResultSet rs = null;
		try {
			rs = executeSql(sql);
		}
		catch(Exception e) {
			log.error("Exception while executing the UPDATE Query in QCReader:"+e);
		}
		if(rs==null) return false;	
		return status;
	}
	
	
	
	
	private String getVersion(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//VERSION");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getTransactionId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TRANSACTION_ID");
		if (node==null)
			return null;
		return node.getText();
	}	
	private String getFromTime(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//FROM_TIME");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getSourceArtifactId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_ARTIFACT_ID");
		if (node==null)
			return null;
		return node.getText();
	}

	private String getSourceRepositoryId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getSourceRepositoryKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	private String getSourceSystemId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getSourceSystemKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//SOURCE_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
	}
		
	private String getTargetRepositoryId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_REPOSITORY_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getTargetRepositoryKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_REPOSITORY_KIND");
		if (node==null)
			return null;
		return node.getText();
	}

	private String getTargetSystemId(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_SYSTEM_ID");
		if (node==null)
			return null;
		return node.getText();
	}
	private String getTargetSystemKind(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TARGET_SYSTEM_KIND");
		if (node==null)
			return null;
		return node.getText();
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

	public QCReader() {
		super();
	}
}
