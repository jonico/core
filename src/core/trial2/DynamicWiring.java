package trial2;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.hsqldb.lib.HashMap;
import org.openadaptor.auxil.connector.iostream.EncodingAwareObject;
import org.openadaptor.auxil.connector.iostream.reader.FileReadConnector;
import org.openadaptor.auxil.connector.iostream.reader.string.LineReader;
import org.openadaptor.auxil.connector.iostream.writer.FileWriteConnector;
import org.openadaptor.auxil.connector.jdbc.JDBCConnection;
import org.openadaptor.auxil.connector.jdbc.reader.JDBCReadConnector;
import org.openadaptor.auxil.connector.jdbc.reader.xml.ResultSetToXMLConverter;
import org.openadaptor.auxil.convertor.delimited.DelimitedStringToOrderedMapConvertor;
import org.openadaptor.auxil.convertor.xml.OrderedMapToXmlConvertor;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.adaptor.Adaptor;
import org.openadaptor.core.router.Router;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;

public class DynamicWiring implements
		IDataProcessor {
    
	private static final Log log = LogFactory.getLog(DynamicWiring.class);
	
	private boolean isDry=true;

	private Object readerContext;

	public Object[] process(Object data) {
		
		log.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		// TODO evaluate data to decide which items to fetch again
		if (!(data instanceof Document)) {
			log.error("Supplied data not in the expected dom4j format: "+data);
			return null;
		}
			
		Document document=(Document) data;
		log.error(document.asXML());
		log.error("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		try {
			Thread.sleep(3000);
		}
		catch (Exception e) {
			System.out.println("Exception while sleeping");
		}
		Object objectArray[] = new  Object[1];
		objectArray[0] = data;
		
		
		/* main wiring here */
		/*
	    // stream read connectors delegate the stream reading to an IStreamReader
	    // and the record reading to an IRecordReader. This example uses a
	    // FileReader and because this doesn't specify a file to read from it
	    // defaults to stdin.
	    
	    FileReadConnector reader = new FileReadConnector("Reader");
	    reader.setDataReader(new LineReader());

		JDBCConnection JDBCConnectionRead = new JDBCConnection();
		JDBCConnectionRead.setDriver("org.hsqldb.jdbcDriver");
		JDBCConnectionRead.setUrl("jdbc:hsqldb:hsql://localhost/xdb");
		JDBCConnectionRead.setUsername("sa");
		JDBCConnectionRead.setPassword("");
		
	    ResultSetToXMLConverter CCFResultSetConvertor = new ResultSetToXMLConverter();
	    CCFResultSetConvertor.setConvertToString(false);
	    CCFResultSetConvertor.setRootElement("CCFMappings");
		
	    JDBCReadConnector CCFDatabaseReader = new JDBCReadConnector();
	    CCFDatabaseReader.setJdbcConnection(JDBCConnectionRead);
	    CCFDatabaseReader.setBatchSize(0);
	    CCFDatabaseReader.setResultSetConverter(CCFResultSetConvertor);
	    CCFDatabaseReader.setSql("SELECT * FROM REPOSITORIES");

	    // this convertor converts a delimited string to a map
	    // the configuration specifies a single key
	    
	    DelimitedStringToOrderedMapConvertor mapConverter;
	    mapConverter = new DelimitedStringToOrderedMapConvertor("MapConverter");
	    mapConverter.setFieldNames(new String[] {"field"});
	    
	    // this converter converts a map into an xml string
	    
	    OrderedMapToXmlConvertor xmlConverter;
	    xmlConverter = new OrderedMapToXmlConvertor("XmlConverter");
	    xmlConverter.setRootElementTag("data");
	    
	    // stream writer connectors delegate the stream writing to an IStreamWriter
	    // and the record writing to an IRecordWriter. This example uses a
	    // FileWriter and because it doesn't specify a file to write to it
	    // defaults to stdin. Because no record writer is configured it defaults
	    // to a StringRecordWriter (which writes a string followed by a newline)
	    
	    FileWriteConnector writer = new FileWriteConnector("Writer");
	    
	    // we then instantiate an Adaptor and set it's pipeline to be the
	    // array of ordered components
	    
	    Adaptor adaptor = new Adaptor();
	    Router router=new Router();
	    //router.setProcessors(Arrays.asList(new Object[] {reader, mapConverter, xmlConverter, writer}));
	    router.setProcessors(Arrays.asList(new Object[] {CCFDatabaseReader, xmlConverter, writer}));	    adaptor.setMessageProcessor(router);
	    
	    // this starts the adaptor
	    adaptor.run();
*/
		return objectArray;

	
	}

	public Object getReaderContext() {
		return readerContext;
	}
	
	public boolean isDry() {
		return isDry;
	}


	public void validate(List exceptions) {
	}

	private String getToTime(Document document) {
		// TODO Let the user specify this value?
		Node node= document.selectSingleNode("//TO_TIME");
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
	
	
	
	public void reset(Object context) {
	}

	public DynamicWiring() {
		super();
	}
}
