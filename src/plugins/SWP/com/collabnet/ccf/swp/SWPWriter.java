package com.collabnet.ccf.swp;

import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;

import com.collabnet.ccf.core.AbstractWriter;
/**
 * SWP Writer component
 * @author jnicolai
 *
 */
public class SWPWriter extends AbstractWriter<Connection> implements
		IDataProcessor {

	@Override
	public Document createArtifact(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document[] createAttachment(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document createDependency(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document deleteArtifact(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document[] deleteAttachment(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document deleteDependency(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document updateArtifact(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document updateAttachment(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document updateDependency(Document gaDocument) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setUserName(String userName) {
		// TODO implement me
	}
	
	public void setPassword(String password) {
		// TODO implement me
	}

	public void setServerUrl(String serverUrl) {
		// TODO implement me
	}
	
	public void setResyncUserName(String resyncUserName) {
		// TODO implement me
	}
	
	public void setResyncPassword(String resyncPassword) {
		// TODO implement me
	}

}
