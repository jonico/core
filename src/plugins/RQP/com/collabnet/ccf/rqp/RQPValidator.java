package com.collabnet.ccf.rqp;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.core.Component;
import org.openadaptor.core.exception.ValidationException;

/**
 * Validates RQP reader and writer.
 * 
 * @author Sergio Piano.
 */
public class RQPValidator {

	private static final Log log = LogFactory.getLog(RQPValidator.class);
	
	/**
	 * Default constructor.
	 */
	public RQPValidator() {
	}
	
	private void validate(Object toValidate, String cause, Component component, List<ValidationException> exceptions) {
		if (toValidate == null) {
			log.error(cause);
			exceptions.add(new ValidationException(cause, component));
		}
	}

	/**
	 * validate RQP Reader.
	 * @param reader {@link RQPReader}
	 * @param exceptions list of exceptions.
	 */
	public void validate(RQPReader reader, List<ValidationException> exceptions) {
		validate(reader.getPassword(), "password-property not set", reader, exceptions);
		validate(reader.getUserName(), "userName-property not set", reader, exceptions);
		String serverUrl = reader.getServerUrl();
		validate(serverUrl, "serverUrl-property not set", reader, exceptions);
		try {
			reader.setRQPHandler(new RQPHandler());
		}  catch (Exception e){
			validate(null, "Could not initialize RQPHandler", reader, exceptions);
		}
		try {
			reader.setAttachmentHandler(new RQPAttachmentHandler(serverUrl, reader.getConnectionManager()));
		} catch (Exception e) {
			validate(null, "Could not initialize RQPAttachmentHandler", reader, exceptions);
		}
	}

	/**
	 * validate RQP Writer.
	 * @param writer {@link RQPWriter}
	 * @param exceptions list of exceptions.
	 */
	public void validate(RQPWriter writer, List<ValidationException> exceptions) {
		validate(writer.getPassword(), "password-property not set", writer, exceptions);
		validate(writer.getUserName(), "userName-property not set", writer, exceptions);
		String serverUrl = writer.getServerUrl();
		validate(serverUrl, "serverUrl-property not set", writer, exceptions);
		try {
			writer.setRQPHandler(new RQPHandler());
		}  catch (Exception e){
			validate(null, "Could not initialize RQPHandler", writer, exceptions);
		}
		try {
			writer.setAttachmentHandler(new RQPAttachmentHandler(serverUrl, writer.getConnectionManager()));
		} catch (Exception e) {
			validate(null, "Could not initialize RQPAttachmentHandler", writer, exceptions);
		}
	}
}
