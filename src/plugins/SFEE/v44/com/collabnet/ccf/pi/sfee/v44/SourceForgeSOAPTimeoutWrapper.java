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
import com.vasoftware.sf.soap44.types.SoapFilter;
import com.vasoftware.sf.soap44.webservices.ClientSoapStubFactory;
import com.vasoftware.sf.soap44.webservices.sfmain.AssociationSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.AttachmentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.AuditHistorySoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.CommentSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.GroupSoapDO;
import com.vasoftware.sf.soap44.webservices.sfmain.GroupSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.ISourceForgeSoap;
import com.vasoftware.sf.soap44.webservices.sfmain.ProjectMemberSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.ProjectSoapDO;
import com.vasoftware.sf.soap44.webservices.sfmain.ProjectSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.UserSoapDO;
import com.vasoftware.sf.soap44.webservices.sfmain.UserSoapList;
import com.vasoftware.sf.soap44.webservices.sfmain.VersionInformationSoapList;

public class SourceForgeSOAPTimeoutWrapper extends TimeoutWrapper implements
		ISourceForgeSoap {

	private ISourceForgeSoap sfSoap;
	private ConnectionManager<Connection> connectionManager;

	public SourceForgeSOAPTimeoutWrapper(String connectionInfo,
			ConnectionManager<Connection> connectionManager) {
		sfSoap = (ISourceForgeSoap) ClientSoapStubFactory.getSoapStub(
				ISourceForgeSoap.class, connectionInfo);
		this.connectionManager = connectionManager;
	}

	public void addGroupMember(String arg0, String arg1, String arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.addGroupMember(arg0, arg1, arg2);
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

	public void addProjectMember(String arg0, String arg1, String arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.addProjectMember(arg0, arg1, arg2);
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

	public void createAssociation(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.createAssociation(arg0, arg1, arg2, arg3);
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

	public GroupSoapDO createGroup(String arg0, String arg1, String arg2)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.createGroup(arg0, arg1, arg2);
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

	public ProjectSoapDO createProject(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.createProject(arg0, arg1, arg2, arg3);
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

	public ProjectSoapDO createProjectFromTemplate(String arg0, String arg1,
			String arg2, String arg3, String arg4) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.createProjectFromTemplate(arg0, arg1, arg2, arg3,
						arg4);
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

	public UserSoapDO createUser(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, boolean arg6, boolean arg7,
			String arg8) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.createUser(arg0, arg1, arg2, arg3, arg4, arg5,
						arg6, arg7, arg8);
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

	public void deleteAssociation(String arg0, String arg1, String arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.deleteAssociation(arg0, arg1, arg2);
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

	public void deleteAttachment(String arg0, String arg1, String arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.deleteAttachment(arg0, arg1, arg2);
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

	public void deleteGroup(String arg0, String arg1) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.deleteGroup(arg0, arg1);
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

	public void deleteProject(String arg0, String arg1) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.deleteProject(arg0, arg1);
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

	public ProjectSoapList findProjects(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.findProjects(arg0, arg1);
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

	public UserSoapList findUsers(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.findUsers(arg0, arg1);
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

	public UserSoapList getActiveGroupMembers(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getActiveGroupMembers(arg0, arg1);
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

	public String getApiVersion() throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getApiVersion();
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public AssociationSoapList getAssociationList(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getAssociationList(arg0, arg1);
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

	public AuditHistorySoapList getAuditHistoryList(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getAuditHistoryList(arg0, arg1);
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

	public CommentSoapList getCommentList(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getCommentList(arg0, arg1);
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

	public String getConfigurationValue(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getConfigurationValue(arg0, arg1);
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

	public GroupSoapDO getGroupData(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getGroupData(arg0, arg1);
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

	public GroupSoapList getGroupList(String arg0, SoapFilter arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getGroupList(arg0, arg1);
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

	public int getProjectAccessLevel(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getProjectAccessLevel(arg0, arg1);
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

	public ProjectSoapDO getProjectData(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getProjectData(arg0, arg1);
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

	public long getProjectDiskUsage(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getProjectDiskUsage(arg0, arg1);
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

	public GroupSoapList getProjectGroupList(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getProjectGroupList(arg0, arg1);
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

	public ProjectSoapList getProjectList(String arg0) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getProjectList(arg0);
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

	public ProjectSoapList getProjectListForUser(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getProjectListForUser(arg0, arg1);
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

	public ProjectMemberSoapList getProjectMemberList(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getProjectMemberList(arg0, arg1);
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

	public long getProjectQuota(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getProjectQuota(arg0, arg1);
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

	public ProjectSoapList getProjectsForUser(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getProjectListForUser(arg0, arg1);
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

	public UserSoapDO getUserData(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getUserData(arg0, arg1);
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

	public GroupSoapList getUserGroupList(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getUserGroupList(arg0, arg1);
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

	public UserSoapList getUserList(String arg0, SoapFilter arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getUserList(arg0, arg1);
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

	public ProjectSoapList getUserProjectList(String arg0)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getUserProjectList(arg0);
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

	public String getVersion(String arg0) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getVersion(arg0);
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

	public VersionInformationSoapList getVersionInformationList(String arg0,
			String arg1) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.getVersionInformationList(arg0, arg1);
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

	public boolean hasPermission(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.hasPermission(arg0, arg1, arg2, arg3);
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

	public boolean isHostedMode(String arg0) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.isHostedMode(arg0);
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

	public void keepAlive(String arg0) throws RemoteException {
		// This method is not wrapped since it is used to check whether a
		// network problem appeared
		sfSoap.keepAlive(arg0);
	}

	public AttachmentSoapList listAttachments(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.listAttachments(arg0, arg1);
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

	public ProjectSoapList listTemplates(String arg0) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.listTemplates(arg0);
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

	public String login(String arg0, String arg1) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.login(arg0, arg1);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public String loginAnonymous(String arg0) throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.loginAnonymous(arg0);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public String loginWithToken(String arg0, String arg1)
			throws RemoteException {
		int numberOfTries = 1;
		while (true) {
			try {
				return sfSoap.loginWithToken(arg0, arg1);
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void logoff(String arg0, String arg1) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.logoff(arg0, arg1);
				retryCall = false;
			} catch (AxisFault e) {
				javax.xml.namespace.QName faultCode = e.getFaultCode();
				if (!faultCode.getLocalPart().equals("InvalidSessionFault")) {
					throw e;
				}
				// ignore this one since we are already logged out
				retryCall = false;
			} catch (RemoteException e) {
				if (!handleException(e, numberOfTries, connectionManager))
					throw e;
			}
			++numberOfTries;
		}
	}

	public void reindexObject(String arg0, String arg1) throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.reindexObject(arg0, arg1);
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

	public void removeGroupMember(String arg0, String arg1, String arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.removeGroupMember(arg0, arg1, arg2);
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

	public void removeProjectMember(String arg0, String arg1, String arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.removeProjectMember(arg0, arg1, arg2);
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

	public void setGroupData(String arg0, GroupSoapDO arg1)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.setGroupData(arg0, arg1);
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

	public void setProjectAccessLevel(String arg0, String arg1, int arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.setProjectAccessLevel(arg0, arg1, arg2);
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

	public void setProjectQuota(String arg0, String arg1, long arg2)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.setProjectQuota(arg0, arg1, arg2);
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

	public void setUserData(String arg0, UserSoapDO arg1)
			throws RemoteException {
		boolean retryCall = true;
		int numberOfTries = 1;
		while (retryCall) {
			try {
				sfSoap.setUserData(arg0, arg1);
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
}
