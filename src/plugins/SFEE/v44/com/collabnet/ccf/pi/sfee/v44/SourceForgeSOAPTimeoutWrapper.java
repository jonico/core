package com.collabnet.ccf.pi.sfee.v44;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

public class SourceForgeSOAPTimeoutWrapper implements ISourceForgeSoap {
	
	private static final Log log = LogFactory.getLog(SourceForgeSOAPTimeoutWrapper.class);

	private ISourceForgeSoap sfSoap;

	public SourceForgeSOAPTimeoutWrapper(String connectionInfo) {
		sfSoap = (ISourceForgeSoap) ClientSoapStubFactory.getSoapStub(ISourceForgeSoap.class, connectionInfo);
	}
	
	
	public void addGroupMember(String arg0, String arg1, String arg2)
			throws RemoteException {
		sfSoap.addGroupMember(arg0, arg1, arg2);

	}

	public void addProjectMember(String arg0, String arg1, String arg2)
			throws RemoteException {
		sfSoap.addProjectMember(arg0, arg1, arg2);

	}

	public void createAssociation(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		sfSoap.createAssociation(arg0, arg1, arg2, arg3);

	}

	public GroupSoapDO createGroup(String arg0, String arg1, String arg2)
			throws RemoteException {
		return sfSoap.createGroup(arg0, arg1, arg2);
	}

	public ProjectSoapDO createProject(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		return sfSoap.createProject(arg0, arg1, arg2, arg3);
	}

	public ProjectSoapDO createProjectFromTemplate(String arg0, String arg1,
			String arg2, String arg3, String arg4) throws RemoteException {
		return sfSoap.createProjectFromTemplate(arg0, arg1, arg2, arg3, arg4);
	}

	public UserSoapDO createUser(String arg0, String arg1, String arg2,
			String arg3, String arg4, String arg5, boolean arg6, boolean arg7,
			String arg8) throws RemoteException {
		return sfSoap.createUser(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	public void deleteAssociation(String arg0, String arg1, String arg2)
			throws RemoteException {
		sfSoap.deleteAssociation(arg0, arg1, arg2);
	}

	public void deleteAttachment(String arg0, String arg1, String arg2)
			throws RemoteException {
		sfSoap.deleteAttachment(arg0, arg1, arg2);
	}

	public void deleteGroup(String arg0, String arg1) throws RemoteException {
		sfSoap.deleteGroup(arg0, arg1);
	}

	public void deleteProject(String arg0, String arg1) throws RemoteException {
		sfSoap.deleteProject(arg0, arg1);
	}

	public ProjectSoapList findProjects(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.findProjects(arg0, arg1);
	}

	public UserSoapList findUsers(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.findUsers(arg0, arg1);
	}

	public UserSoapList getActiveGroupMembers(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getActiveGroupMembers(arg0, arg1);
	}

	public String getApiVersion() throws RemoteException {
		return sfSoap.getApiVersion();
	}

	public AssociationSoapList getAssociationList(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getAssociationList(arg0, arg1);
	}

	public AuditHistorySoapList getAuditHistoryList(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getAuditHistoryList(arg0, arg1);
	}

	public CommentSoapList getCommentList(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getCommentList(arg0, arg1);
	}

	public String getConfigurationValue(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getConfigurationValue(arg0, arg1);
	}

	public GroupSoapDO getGroupData(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getGroupData(arg0, arg1);
	}

	public GroupSoapList getGroupList(String arg0, SoapFilter arg1)
			throws RemoteException {
		return sfSoap.getGroupList(arg0, arg1);
	}

	public int getProjectAccessLevel(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getProjectAccessLevel(arg0, arg1);
	}

	public ProjectSoapDO getProjectData(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getProjectData(arg0, arg1);
	}

	public long getProjectDiskUsage(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getProjectDiskUsage(arg0, arg1);
	}

	public GroupSoapList getProjectGroupList(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getProjectGroupList(arg0, arg1);
	}

	public ProjectSoapList getProjectList(String arg0) throws RemoteException {
		return sfSoap.getProjectList(arg0);
	}

	public ProjectSoapList getProjectListForUser(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getProjectListForUser(arg0, arg1);
	}

	public ProjectMemberSoapList getProjectMemberList(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getProjectMemberList(arg0, arg1);
	}

	public long getProjectQuota(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getProjectQuota(arg0, arg1);
	}

	public ProjectSoapList getProjectsForUser(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getProjectListForUser(arg0, arg1);
	}

	public UserSoapDO getUserData(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getUserData(arg0, arg1);
	}

	public GroupSoapList getUserGroupList(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.getUserGroupList(arg0, arg1);
	}

	public UserSoapList getUserList(String arg0, SoapFilter arg1)
			throws RemoteException {
		return sfSoap.getUserList(arg0, arg1);
	}

	public ProjectSoapList getUserProjectList(String arg0)
			throws RemoteException {
		return sfSoap.getUserProjectList(arg0);
	}

	public String getVersion(String arg0) throws RemoteException {
		return sfSoap.getVersion(arg0);
	}

	public VersionInformationSoapList getVersionInformationList(String arg0,
			String arg1) throws RemoteException {
		return sfSoap.getVersionInformationList(arg0, arg1);
	}

	public boolean hasPermission(String arg0, String arg1, String arg2,
			String arg3) throws RemoteException {
		return sfSoap.hasPermission(arg0, arg1, arg2, arg3);
	}

	public boolean isHostedMode(String arg0) throws RemoteException {
		return sfSoap.isHostedMode(arg0);
	}

	public void keepAlive(String arg0) throws RemoteException {
		sfSoap.keepAlive(arg0);
	}

	public AttachmentSoapList listAttachments(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.listAttachments(arg0, arg1);
	}

	public ProjectSoapList listTemplates(String arg0) throws RemoteException {
		return sfSoap.listTemplates(arg0);
	}

	public String login(String arg0, String arg1) throws RemoteException {
		int numberOfTries=1;
		while (true) {
			try {
				return sfSoap.login(arg0, arg1);
			}
			catch (RemoteException e) {
					if (!handleException(e,numberOfTries))
						throw e;
			}
			++numberOfTries;
		}
	}

	public String loginAnonymous(String arg0) throws RemoteException {
		return sfSoap.loginAnonymous(arg0);
	}

	public String loginWithToken(String arg0, String arg1)
			throws RemoteException {
		return sfSoap.loginWithToken(arg0, arg1);
	}

	public void logoff(String arg0, String arg1) throws RemoteException {
		sfSoap.logoff(arg0, arg1);
	}

	public void reindexObject(String arg0, String arg1) throws RemoteException {
		sfSoap.reindexObject(arg0, arg1);
	}

	public void removeGroupMember(String arg0, String arg1, String arg2)
			throws RemoteException {
		sfSoap.removeGroupMember(arg0, arg1, arg2);

	}

	public void removeProjectMember(String arg0, String arg1, String arg2)
			throws RemoteException {
		sfSoap.removeProjectMember(arg0, arg1, arg2);
	}

	public void setGroupData(String arg0, GroupSoapDO arg1)
			throws RemoteException {
		sfSoap.setGroupData(arg0, arg1);
	}

	public void setProjectAccessLevel(String arg0, String arg1, int arg2)
			throws RemoteException {
		sfSoap.setProjectAccessLevel(arg0, arg1, arg2);
	}

	public void setProjectQuota(String arg0, String arg1, long arg2)
			throws RemoteException {
		sfSoap.setProjectQuota(arg0, arg1, arg2);
	}

	public void setUserData(String arg0, UserSoapDO arg1)
			throws RemoteException {
		sfSoap.setUserData(arg0, arg1);
	}
	
	/**
	 * This method determines whether the exception is timeout related and will be handled in this method or whether it should be handled by another layer
	 * @param e exception that was intercepted
	 * @param numberOfTries number this method was already called for the same method call, this will help to determine the timeout
	 * @return true if exception was handled here and method call should be retried, false if exception should be passed to next layer
	 */
	
	private boolean handleException(RemoteException e, int numberOfTries) {
		Throwable cause=e.getCause();
		if (cause instanceof java.net.SocketException || cause instanceof java.net.UnknownHostException ) {
			if (numberOfTries == 1) {
				// first try, long error message
				log.error("Network related problem occurred while calling SFEE/CSFE webservice. Try operation again",e);
			}
			else if (numberOfTries < 7) {
				// error occurred again, short error message, go to sleep
				int timeOut=(int)Math.pow(2, numberOfTries);
				log.error("Network related error occurred again ("+e.getMessage()+"), sleeping for "+timeOut+" seconds.");
				try {
					Thread.sleep(timeOut*1000);
				} catch (InterruptedException e1) {
					log.error("Interrupted sleep in timeout method: ",e1);
				}
			}
			else {
				// error occurred more than 6 times, short error message, go to sleep for two minutes
				log.error("Network related error occurred again ("+e.getMessage()+") , sleeping for two minutes.");
				try {
					Thread.sleep(120000);
				} catch (InterruptedException e1) {
					log.error("Interrupted sleep in timeout method: ",e1);
				}
			}
			return true;
		}
		else
			return false;
	}
}
