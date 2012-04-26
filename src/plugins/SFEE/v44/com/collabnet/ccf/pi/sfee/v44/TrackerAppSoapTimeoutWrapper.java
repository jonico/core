/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import org.apache.axis.AxisFault;

import com.collabnet.ccf.core.eis.connection.ConnectionManager;
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

public class TrackerAppSoapTimeoutWrapper extends TimeoutWrapper implements
		ITrackerAppSoap {
	/** Tracker Soap API handle */
	private ITrackerAppSoap mTrackerApp;
	private ConnectionManager<Connection> connectionManager;

	/**
	 * Class constructor.
	 * 
	 * @param serverUrl
	 *            - The source TF SOAP server URL
	 * @param connectionManager
	 */
	public TrackerAppSoapTimeoutWrapper(String serverUrl,
			ConnectionManager<Connection> connectionManager) {
		mTrackerApp = (ITrackerAppSoap) ClientSoapStubFactory.getSoapStub(
				ITrackerAppSoap.class, serverUrl);
		this.connectionManager = connectionManager;
	}

	public void addDateField(String arg0, String arg1, String arg2,
			boolean arg3, boolean arg4, boolean arg5) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.addDateField(arg0, arg1, arg2, arg3, arg4, arg5);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void addMultiSelectField(String arg0, String arg1, String arg2,
			int arg3, boolean arg4, boolean arg5, boolean arg6, String[] arg7,
			String[] arg8) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.addMultiSelectField(arg0, arg1, arg2, arg3, arg4,
						arg5, arg6, arg7, arg8);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void addSingleSelectField(String arg0, String arg1, String arg2,
			boolean arg3, boolean arg4, boolean arg5, String[] arg6, String arg7)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.addSingleSelectField(arg0, arg1, arg2, arg3, arg4,
						arg5, arg6, arg7);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void addTextField(String arg0, String arg1, String arg2, int arg3,
			int arg4, boolean arg5, boolean arg6, boolean arg7, String arg8)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.addTextField(arg0, arg1, arg2, arg3, arg4, arg5,
						arg6, arg7, arg8);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void addUserSelectField(String arg0, String arg1, String arg2,
			int arg3, boolean arg4, boolean arg5, boolean arg6, String[] arg7,
			String arg8) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.addUserSelectField(arg0, arg1, arg2, arg3, arg4,
						arg5, arg6, arg7, arg8);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void copyWorkflowTransitions(String arg0, String arg1, String arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.copyWorkflowTransitions(arg0, arg1, arg2);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactSoapDO createArtifact(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, String arg6, String arg7,
			int arg8, int arg9, String arg10, String arg11,
			SoapFieldValues arg12, String arg13, String arg14, String arg15)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.createArtifact(arg0, arg1, arg2, arg3, arg4,
						arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12,
						arg13, arg14, arg15);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void createArtifactDependency(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.createArtifactDependency(arg0, arg1, arg2, arg3);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public TrackerSoapDO createTracker(String arg0, String arg1, String arg2,
			String arg3, String arg4) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.createTracker(arg0, arg1, arg2, arg3, arg4);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void deleteArtifact(String arg0, String arg1) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.deleteArtifact(arg0, arg1);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void deleteField(String arg0, String arg1, String arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.deleteField(arg0, arg1, arg2);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactSoapList findArtifacts(String arg0, String arg1,
			String arg2, boolean arg3) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.findArtifacts(arg0, arg1, arg2, arg3);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public WorkflowTransitionSoapList getAllowedWorkflowTransitionList(
			String arg0, String arg1) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getAllowedWorkflowTransitionList(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactSoapDO getArtifactData(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getArtifactData(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactDetailSoapList getArtifactDetailList(String arg0,
			String arg1, String[] arg2, SoapFilter[] arg3, SoapSortKey[] arg4,
			int arg5, int arg6, boolean arg7, boolean arg8)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getArtifactDetailList(arg0, arg1, arg2,
						arg3, arg4, arg5, arg6, arg7, arg8);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactSoapList getArtifactList(String arg0, String arg1,
			SoapFilter[] arg2) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getArtifactList(arg0, arg1, arg2);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactDependencySoapList getChildDependencyList(String arg0,
			String arg1) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getChildDependencyList(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public TrackerFieldSoapDO[] getFields(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getFields(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactDependencySoapList getParentDependencyList(String arg0,
			String arg1) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getParentDependencyList(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public TrackerSoapDO getTrackerData(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getTrackerData(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public TrackerSoapList getTrackerList(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getTrackerList(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactSoapDO moveArtifact(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.moveArtifact(arg0, arg1, arg2, arg3);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void removeArtifactDependency(String arg0, String arg1, String arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.removeArtifactDependency(arg0, arg1, arg2);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void setArtifactData(String arg0, ArtifactSoapDO arg1, String arg2,
			String arg3, String arg4, String arg5) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.setArtifactData(arg0, arg1, arg2, arg3, arg4, arg5);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void setField(String arg0, String arg1, TrackerFieldSoapDO arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.setField(arg0, arg1, arg2);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void setTrackerData(String arg0, TrackerSoapDO arg1)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				mTrackerApp.setTrackerData(arg0, arg1);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactSoapDO getArtifactDataFull(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return mTrackerApp.getArtifactDataFull(arg0, arg1);
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// arg0 is the invalid session id
				arg0 = retryLogin(arg0, e, numberOfTries, connectionManager);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public ArtifactDetailSoapList getArtifactDetailListFull(String arg0,
			String arg1, String[] arg2, SoapFilter[] arg3, SoapSortKey[] arg4,
			int arg5, int arg6, boolean arg7, boolean arg8)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
