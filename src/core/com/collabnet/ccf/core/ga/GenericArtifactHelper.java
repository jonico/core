package com.collabnet.ccf.core.ga;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.xml.datatype.XMLGregorianCalendar;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.openadaptor.core.exception.RecordFormatException;

import com.collabnet.ccf.pi.qc.AttachmentData;
import com.collabnet.ccf.pi.qc.NamesTypesAndValues;


/*
 * The following is the current tree sturcture of the Generic Artifact.
 * For more details, refer to the Generic Artifact Template document at:
 * http://ccf.open.collab.net/servlets/ProjectProcess?pageID=3788&subpageID=3786
 * 
 * Artifact
 * 	+-----Fields
 * 	|      +-----Field
 *  |
 * 	+-----Attachments
 * 	       +-----Attachment
 */

public class GenericArtifactHelper {
	
	// Tag and attribute names as specified in the Generic Artifact Template
	static final String artifactTag = "artifact";
	static final String typeAttribute = "type";
	static final String modeAttribute = "mode";
	static final String actionAttribute = "action";
	static final String idFieldAttribute = "id-field";
	static final String fieldsTag = "fields";
	static final String fieldTag = "field";
	static final String changedAttribute = "changed";
	static final String trueStringValue = "true";
	static final String falseStringValue = "false";
	static final String nameTag = "name";
	static final String datatypeTag = "datatype";
	static final String handleMethodTag = "handle-method";
	static final String valueTag = "value";
	static final String deletedAttribute = "deleted";
	static final String attachmentsTag = "attachments";
	static final String attachmentTag = "attachment";
	static final String idTag = "id";
	static final String displayNameTag = "display-name";
	static final String srcUrlTag = "src-url";
	static final String mimeTypeTag = "mime-type";
	static final String sizeTag = "size";
	static final String dataTag = "data";

	private static DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,new Locale("en"));

	public static boolean addArtifact(Element root, String type, String mode, String action, String idField) {
		
		if (type == null || action == null ||
			mode == null || idField == null)
			return false;

		Element artifactElement = root.addElement(artifactTag);
		artifactElement.addAttribute(typeAttribute, type);
		artifactElement.addAttribute(modeAttribute, mode);
		artifactElement.addAttribute(actionAttribute, action);
		artifactElement.addAttribute(idFieldAttribute, idField);		

		return true;
	}
	
	public static boolean addField(Element fieldsElement, boolean changed, String name, String datatype, String handleMethod, String value) {
		
		// value can be null, in which case, the value is supposed to have been removed
		if (fieldsElement == null || name == null         ||
			datatype == null      || handleMethod == null)
			return false;
			
		Element fieldElement = fieldsElement.addElement("fieldTag");
		if (changed)
			fieldElement.addAttribute(changedAttribute, trueStringValue);
		else
			fieldElement.addAttribute(changedAttribute, falseStringValue);

		Element child = fieldElement.addElement(generateElementName(nameTag));
		child.setText(name);
		
		child = fieldElement.addElement(generateElementName(datatypeTag));
		child.setText(datatype);

		child = fieldElement.addElement(generateElementName(handleMethodTag));
		child.setText(handleMethod);

		// Check if the value is deleted
		if (value != null) {
			child = fieldElement.addElement(generateElementName(valueTag));
			child.addAttribute(deletedAttribute, falseStringValue);
			child.setText(value);
		}
		else {
			child = fieldElement.addElement(generateElementName(valueTag));
			child.addAttribute(deletedAttribute, trueStringValue);
		}
		
		return true;
	}

	static boolean addFieldsTag(Element root) {
		if (root == null) {
			return false;
		}
		
		root.addElement(fieldsTag);
		return true;
	}
	
	static boolean addAttachmentsTag(Element root) {
		if (root == null) {
			return false;
		}
		
		root.addElement(attachmentsTag);
		return true;
	}
	
	static boolean addAttachment(Element attachmentsTag, String action, String id, String displayName, String srcUrl, String mimeType, String data) {
		
		if(attachmentsTag == null ||
		   action == null         ||
		   id == null             ||
		   displayName == null    ||
		   srcUrl == null         ||
		   mimeType == null       ||
		   data == null) {
			return false;
		}
		
		Element attachmentElement = attachmentsTag.addElement(generateElementName(attachmentTag));
		attachmentElement.addAttribute(actionAttribute, action);
		
		Element child = attachmentElement.addElement(generateElementName(idTag));
		child.setText(id);
		
		child = attachmentElement.addElement(generateElementName(displayNameTag));
		child.setText(displayName);
		
		child = attachmentElement.addElement(generateElementName(srcUrlTag));
		child.setText(srcUrl);
		
		child = attachmentElement.addElement(generateElementName(mimeTypeTag));
		child.setText(mimeType);
		
		child = attachmentElement.addElement(generateElementName(dataTag));
		child.addCDATA(data);

		return true;
	}
	
	/*
	 * Functions carried over from the orginal XXXXMLHelper class by
	 * Johannes Nicolai
	 */
	public static void addField(Element root,
			String fieldName, Object fieldValue, String fieldType, boolean isFlexField) {
		// TODO Let user specify value of item
		// it is no problem if elements with the same attribute values are inserted more than once
		//Element child=root.addElement(generateElementName("field"),"http://sfee-conigma.open.collab.net");
		Element child=root.addElement(generateElementName("field"));
		child.addAttribute("name", fieldName);
		child.addAttribute("isFlexField", isFlexField?"true":"false");
		Element valueElement=child.addElement(generateElementName("value"));
		if (fieldValue==null) {
			valueElement.setText("");
			valueElement.addAttribute("isNull", "true");
		}
		else {
			valueElement.addAttribute("isNull","false");
			if (fieldValue instanceof Date)
				valueElement.setText(df.format((Date)fieldValue));
			else if (fieldValue instanceof Calendar)
				valueElement.setText(df.format(((Calendar)fieldValue).getTime()));
			else if (fieldValue instanceof XMLGregorianCalendar)
				valueElement.setText(df.format(((XMLGregorianCalendar)fieldValue).toGregorianCalendar().getTime()));
			else
				valueElement.setText(fieldValue.toString());
		}
		Element typeElement=child.addElement(generateElementName("type"));
		typeElement.setText(fieldType);
	}

	public static void addAttachments(Element root,
			List<AttachmentData> attachmentDataList) {

		Element attachmentsElement = root.addElement("attachments");
		for(AttachmentData attachmentData:attachmentDataList) {
			Element attachmentElement = attachmentsElement.addElement("attachment");
			attachmentElement.addAttribute("name", attachmentData.getName());
			attachmentElement.addCDATA(new String(attachmentData.getContents()));
		}
	
	}

	
	public static Document createXMLDocument(String encoding) {
		Document document=DocumentHelper.createDocument();
		document.setXMLEncoding(encoding);
		return document;
	}

	// TODO check whether elementName is valid for XML
	public static String generateElementName(String elementName) {
		return elementName;
	}

	public static boolean containsSingleField(Document data, String fieldName, boolean isFlexField) {
		Node result=data.selectSingleNode("//field[@name='"+fieldName+"' and @isFlexField='"+(isFlexField?"true":"false")+"']");
		return result != null;
	}
	
	public static boolean containsMultipleField(Document data, String fieldName, boolean isFlexField) {
		List result=data.selectNodes("//field[@name='"+fieldName+"' and @isFlexField='"+(isFlexField?"true":"false")+"']");
		return result.size()>1;
	}
	
	public static String getSingleValue(Document data, String fieldName, boolean isFlexField) {
		Node result=data.selectSingleNode("//field[@name='"+fieldName+"' and @isFlexField='"+(isFlexField?"true":"false")+"']/value[@isNull='false']");
		return result==null?null:result.getText();
	}
	
	public static String getSingleType(Document data, String fieldName, boolean isFlexField) {
		Node result=data.selectSingleNode("//field[@name='"+fieldName+"' and @isFlexField='"+(isFlexField?"true":"false")+"']/type");
		return result==null?null:result.getText();
	}
	
	public static List<NamesTypesAndValues> getMultipleTypesAndValues(Document data, String fieldName, boolean isFlexField) {
		List<Node> result=data.selectNodes("//field[@name='"+fieldName+"' and @isFlexField='"+(isFlexField?"true":"false")+"']");
		if (result==null)
			return new ArrayList<NamesTypesAndValues>();
		else {
			List<NamesTypesAndValues> aggregatedResult=new ArrayList<NamesTypesAndValues>();
			for (Node node : result) {
				if (node.selectSingleNode("value/@isNull").getText().equals("true"))
					aggregatedResult.add(new NamesTypesAndValues(fieldName,node.selectSingleNode("type").getText(), null));
				else
					aggregatedResult.add(new NamesTypesAndValues(fieldName,node.selectSingleNode("type").getText(), node.selectSingleNode("value").getText()));
			}
			return aggregatedResult;
		}
	}
	
	public static List<NamesTypesAndValues> getAllDefectFields(Document data) {
		List<Node> result=data.selectNodes("//field[@isFlexField='true']");
		if (result==null)
			return new ArrayList<NamesTypesAndValues>();
		else {
			List<NamesTypesAndValues> aggregatedResult=new ArrayList<NamesTypesAndValues>();
			for (Node node : result) {
				if (node.selectSingleNode("value/@isNull").getText().equals("true"))
					aggregatedResult.add(new NamesTypesAndValues(node.selectSingleNode("@name").getText(),node.selectSingleNode("type").getText(), null));
				else
					aggregatedResult.add(new NamesTypesAndValues(node.selectSingleNode("@name").getText(),node.selectSingleNode("type").getText(), node.selectSingleNode("value").getText()));
			}
			return aggregatedResult;
		}
	}
	
	public static List<NamesTypesAndValues> getAllMandatoryFields(Document data) {
		List<Node> result=data.selectNodes("//field[@isFlexField='false']");
		if (result==null)
			return new ArrayList<NamesTypesAndValues>();
		else {
			List<NamesTypesAndValues> aggregatedResult=new ArrayList<NamesTypesAndValues>();
			for (Node node : result) {
				if (node.selectSingleNode("value/@isNull").getText().equals("true"))
					aggregatedResult.add(new NamesTypesAndValues(node.selectSingleNode("@name").getText(),node.selectSingleNode("type").getText(), null));
				else
					aggregatedResult.add(new NamesTypesAndValues(node.selectSingleNode("@name").getText(),node.selectSingleNode("type").getText(), node.selectSingleNode("value").getText()));
			}
			return aggregatedResult;
		}
	}
	
	public static List<NamesTypesAndValues> getAllFields(Document data) {
		List<Node> result=data.selectNodes("//field");
		if (result==null)
			return new ArrayList<NamesTypesAndValues>();
		else {
			List<NamesTypesAndValues> aggregatedResult=new ArrayList<NamesTypesAndValues>();
			for (Node node : result) {
				if (node.selectSingleNode("value/@isNull").getText().equals("true"))
					aggregatedResult.add(new NamesTypesAndValues(node.selectSingleNode("@name").getText(),node.selectSingleNode("type").getText(), null));
				else
					aggregatedResult.add(new NamesTypesAndValues(node.selectSingleNode("@name").getText(),node.selectSingleNode("type").getText(), node.selectSingleNode("value").getText()));
			}
			return aggregatedResult;
		}
	}
	
	public static final Object asTypedValue(String value, String type) throws RecordFormatException {
		if (value==null)
			return null;
		// TODO Find out what types are possible as well
	    Object result = value;
	    if (type != null) {// Need to apply it.
	      String exceptionMessage = null;
	      Throwable t = null;
	      try {
	        if ("Double".equalsIgnoreCase(type))
	          result = new Double(value);
	        else if ("Integer".equalsIgnoreCase(type))
		          result = new Integer(value);
	        else if ("Long".equalsIgnoreCase(type))
	          result = new Long(value);
	        else if ("DateTime".equalsIgnoreCase(type))
	          result = df.parse(value);
	        else if ("Date".equalsIgnoreCase(type)) {
	        	GregorianCalendar cal=new GregorianCalendar();
	        	cal.setTime(df.parse(value));
	        	result=cal;
	        }
	        else if ("Boolean".equalsIgnoreCase(type))
		          result = new Boolean(value);
	        else if ("User".equalsIgnoreCase(type))
		          result = new String(value);
	        else if (!"String".equalsIgnoreCase(type))
	          throw new RecordFormatException("Type "+type+" unknown to XMLTypeConverter");
	      } catch (ParseException pe) {
	        exceptionMessage = "Failed to get typed value for " + value + ". Exception: " + pe.getMessage();
	        t = pe;
	      } catch (NumberFormatException nfe) {
	        exceptionMessage = "Failed to get typed value for " + value + ". Exception: " + nfe.getMessage();
	        t = nfe;
	      }
	      if (exceptionMessage != null) {
	        throw new RecordFormatException(exceptionMessage, t);
	      }
	    }
	    return result;
	  }

	public static void updateSingleField(Element rootElement,
			String fieldName, Object fieldValue, boolean isFlexField) {
		Node result=rootElement.selectSingleNode("//field[@name='"+fieldName+"' and @isFlexField='"+(isFlexField?"true":"false")+"']/value");
		if (result!=null) {
			if (fieldValue==null) {
				result.setText("");
				((Element)result).addAttribute("isNull", "true");
			}
			else {
				((Element)result).addAttribute("isNull","false");
				if (fieldValue instanceof Date)
					result.setText(df.format((Date)fieldValue));
				else if (fieldValue instanceof Calendar)
					result.setText(df.format(((Calendar)fieldValue).getTime()));
				else
					result.setText(fieldValue.toString());
			}
		}
			
		// TODO Throw an exception if field is not available
	}
}
