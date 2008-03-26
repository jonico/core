package trial2;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.openadaptor.core.Component;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.MessageException;

import com.collabnet.ccf.core.ga.GenericArtifactXMLHelper;


public class Ambulance extends Component implements
		IDataProcessor {
    
	private static final Log log = LogFactory.getLog(Ambulance.class);
	private String hospitalFileName = null;
	private FileOutputStream fos = null;
	
	public Ambulance(String id) {
	    super(id);
	}
	
	public Ambulance() {
		super();
	}
	
	public Object[] process(Object data) {
		if(data instanceof MessageException){
					MessageException exception = (MessageException) data;
					Object dataObj = exception.getData();
					String source = exception.getOriginatingModule();
					Exception rootCause = exception.getException();
					Document doc = GenericArtifactXMLHelper.createXMLDocument("UTF-8");
					Element failure = doc.addElement("Failure");
					Element failureSource = failure.addElement("Source");
					failureSource.setText(source);
					Element exceptionDetail = failure.addElement("Exception");
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					PrintStream st = new PrintStream(bos);
					rootCause.printStackTrace(st);
					exceptionDetail.setText(new String(bos.toByteArray()));
					Element dataElement = failure.addElement("Data");
					if(dataObj instanceof Document){
						Document dataDoc = (Document) dataObj;
						dataElement.setText(dataDoc.asXML());
					}
					String writeData = failure.asXML();
					try {
						fos.write(writeData.getBytes());
						fos.write("\n".getBytes());
						fos.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		return new Object[0];
	}

	public void reset(Object context) {
		// TODO Auto-generated method stub
		
	}

	public void validate(List exceptions) {
		// TODO Auto-generated method stub
		
	}

	public String getHospitalFileName() {
		return hospitalFileName;
	}

	public void setHospitalFileName(String hospitalFileName) {
		this.hospitalFileName = hospitalFileName;
		try {
			fos = new FileOutputStream(hospitalFileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
