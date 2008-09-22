package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import com.vasoftware.sf.soap44.types.SoapFieldValues;
import com.vasoftware.sf.soap44.types.SoapFilter;
import com.vasoftware.sf.soap44.types.SoapSortKey;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.sfmain.TrackerFieldSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactDependencySoapList;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactDetailSoapList;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.ArtifactSoapList;
import com.vasoftware.sf.soap44.webservices.tracker.ITrackerAppSoap;
import com.vasoftware.sf.soap44.webservices.tracker.TrackerSoapDO;
import com.vasoftware.sf.soap44.webservices.tracker.TrackerSoapList;
import com.vasoftware.sf.soap44.webservices.tracker.WorkflowTransitionSoapList;

public class TrackerAppSoapTimeoutWrapper implements ITrackerAppSoap {
	/** Tracker Soap API handle */
	private ITrackerAppSoap mTrackerApp;
	
	/**
	 * Class constructor.
	 * 
	 * @param serverUrl - The source SFEE SOAP server URL
	 */
	public TrackerAppSoapTimeoutWrapper(String serverUrl) {
		mTrackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
				ITrackerAppSoap.class, serverUrl);
	}

	public void addDateField(String arg0, String arg1, String arg2,
			boolean arg3, boolean arg4, boolean arg5) throws RemoteException {
		mTrackerApp.addDateField(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public void addMultiSelectField(String arg0, String arg1, String arg2,
			int arg3, boolean arg4, boolean arg5, boolean arg6, String[] arg7,
			String[] arg8) throws RemoteException {
		mTrackerApp.addMultiSelectField(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		
	}

	public void addSingleSelectField(String arg0, String arg1, String arg2,
			boolean arg3, boolean arg4, boolean arg5, String[] arg6, String arg7)
			throws RemoteException {
		mTrackerApp.addSingleSelectField(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}

	public void addTextField(String arg0, String arg1, String arg2, int arg3,
			int arg4, boolean arg5, boolean arg6, boolean arg7, String arg8)
			throws RemoteException {
		mTrackerApp.addTextField(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	public void addUserSelectField(String arg0, String arg1, String arg2,
			int arg3, boolean arg4, boolean arg5, boolean arg6, String[] arg7,
			String arg8) throws RemoteException {
		mTrackerApp.addUserSelectField(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	public void copyWorkflowTransitions(String arg0, String arg1, String arg2)
			throws RemoteException {
		mTrackerApp.copyWorkflowTransitions(arg0, arg1, arg2);
	}

	public ArtifactSoapDO createArtifact(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, String arg6, String arg7,
			int arg8, int arg9, String arg10, String arg11,
			SoapFieldValues arg12, String arg13, String arg14, String arg15)
			throws RemoteException {
		return mTrackerApp.createArtifact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	}

	public void createArtifactDependency(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		mTrackerApp.createArtifactDependency(arg0, arg1, arg2, arg3);		
	}

	public TrackerSoapDO createTracker(String arg0, String arg1, String arg2,
			String arg3, String arg4) throws RemoteException {
		return mTrackerApp.createTracker(arg0, arg1, arg2, arg3, arg4);
	}

	public void deleteArtifact(String arg0, String arg1) throws RemoteException {
		mTrackerApp.deleteArtifact(arg0, arg1);
	}

	public void deleteField(String arg0, String arg1, String arg2)
			throws RemoteException {
		mTrackerApp.deleteField(arg0, arg1, arg2);
		
	}

	public ArtifactSoapList findArtifacts(String arg0, String arg1,
			String arg2, boolean arg3) throws RemoteException {
		return mTrackerApp.findArtifacts(arg0, arg1, arg2, arg3);
	}

	public WorkflowTransitionSoapList getAllowedWorkflowTransitionList(
			String arg0, String arg1) throws RemoteException {
		return mTrackerApp.getAllowedWorkflowTransitionList(arg0, arg1);
	}

	public ArtifactSoapDO getArtifactData(String arg0, String arg1)
			throws RemoteException {
		return mTrackerApp.getArtifactData(arg0, arg1);
	}

	public ArtifactDetailSoapList getArtifactDetailList(String arg0,
			String arg1, String[] arg2, SoapFilter[] arg3, SoapSortKey[] arg4,
			int arg5, int arg6, boolean arg7, boolean arg8)
			throws RemoteException {
		return mTrackerApp.getArtifactDetailList(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	public ArtifactSoapList getArtifactList(String arg0, String arg1,
			SoapFilter[] arg2) throws RemoteException {
		return mTrackerApp.getArtifactList(arg0, arg1, arg2);
	}

	public ArtifactDependencySoapList getChildDependencyList(String arg0,
			String arg1) throws RemoteException {
		return mTrackerApp.getChildDependencyList(arg0, arg1);
	}

	public TrackerFieldSoapDO[] getFields(String arg0, String arg1)
			throws RemoteException {
		return mTrackerApp.getFields(arg0, arg1);
	}

	public ArtifactDependencySoapList getParentDependencyList(String arg0,
			String arg1) throws RemoteException {
		return mTrackerApp.getParentDependencyList(arg0, arg1);
	}

	public TrackerSoapDO getTrackerData(String arg0, String arg1)
			throws RemoteException {
		return mTrackerApp.getTrackerData(arg0, arg1);
	}

	public TrackerSoapList getTrackerList(String arg0, String arg1)
			throws RemoteException {
		return mTrackerApp.getTrackerList(arg0, arg1);
	}

	public ArtifactSoapDO moveArtifact(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		return mTrackerApp.moveArtifact(arg0, arg1, arg2, arg3);
	}

	public void removeArtifactDependency(String arg0, String arg1, String arg2)
			throws RemoteException {
		mTrackerApp.removeArtifactDependency(arg0, arg1, arg2);
		
	}

	public void setArtifactData(String arg0, ArtifactSoapDO arg1, String arg2,
			String arg3, String arg4, String arg5) throws RemoteException {
		mTrackerApp.setArtifactData(arg0, arg1, arg2, arg3, arg4, arg5);
		
	}

	public void setField(String arg0, String arg1, TrackerFieldSoapDO arg2)
			throws RemoteException {
		mTrackerApp.setField(arg0, arg1, arg2);
		
	}

	public void setTrackerData(String arg0, TrackerSoapDO arg1)
			throws RemoteException {
		mTrackerApp.setTrackerData(arg0, arg1);
		
	}
	
}
