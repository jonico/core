package com.collabnet.ccf.core.utils;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.collabnet.ccf.core.eis.connection.ConnectionException;
import com.collabnet.ccf.core.eis.connection.ConnectionFactory;


import com.collabnet.teamforge.api.Connection;
import com.collabnet.teamforge.api.main.UserDO;


/**
 * This class verify the existence of TF username in the TF system(TF user lookup).This class holds a static method which 
 * will be called from custom XSLT snippet.
 * Sample snippet
 * <assignedTo>
 *  <xsl:variable name="user" select="BG_RESPONSIBLE"/>
 *  <xsl:variable name="dummyuser" select="'normal'"/>
 *  <xsl:value-of select='user:searchUser($user,$dummyuser)' xmlns:user="com.collabnet.ccf.core.utils.TFUserHandler"/>
 * </assignedTo>
 * @author selvakumar
 *
 */
public  class TFUserHandler {

	private static final Log log = LogFactory.getLog(TFUserHandler.class);
	
	private static String serverUrl;

	private static  String password;

	private static String username;
	
	private static ConnectionFactory<Connection> connectionFactory;
	
	public  final static String PARAM_DELIMITER = ":";

	
	/**
	 * Static method that take original username and defaultusername. Verifies the existence of username
	 * in TF system.If exist returns the username, else returns the defaultusername.This static method will be
	 * called from custom xslt snippet from QC2TF and the same is whitelisted in config2.xml.If the QC assignedto
	 * user exist in TF system, assignedTo field value will have the username, else a defaultusername will be 
	 * assigned to assignedTo field value.
	 * @param userName
	 * @param defaultUserName
	 * @return verifiedUserName
	 * @throws ConnectionException 
	 */
	public static String searchUser(String userName ,String defaultUserName){
		//If the QC assignedTo doesn't have any value,verifiedUserName will be null and this will be mapped to 'None' of the TF assignedTo
		String verifiedUserName=null;
		Connection connection = null;
		try {
			 connection = getConnection();
			if(!userName.isEmpty()) {
				UserDO userDO=connection.getTeamForgeClient().getUserData(userName);
				verifiedUserName= userDO.getUsername();
			}
		} catch (RemoteException e) {
			verifiedUserName=defaultUserName;
			log.debug("Username doesnot exist "
					+ userName + ": " + e.getMessage());
		}
		catch (ConnectionException e) {
			String cause = "Could not create connection to the TF system "
					+ getServerUrl();
			log.debug(cause, e);
		}
		finally {
			try {
				getConnectionFactory().closeConnection(connection);
			} catch (ConnectionException e) {
				String cause = "Could not create connection to the TF system "
						+ getServerUrl();
				log.debug(cause, e);
			}	
		}
		return verifiedUserName;
		
	}
	
	public static  Connection getConnection() throws ConnectionException{
			 return getConnectionFactory().createConnection("TF", "TF", "tracker", "tracker", getServerUrl(), getUsername() + PARAM_DELIMITER
						+ getPassword(), null);
	}

	public static String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		TFUserHandler.serverUrl = serverUrl;
	}

	private static  String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		TFUserHandler.password = Obfuscator.deObfuscatePassword(password);
	}

	public static   String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		TFUserHandler.username = username;
	}
	

	public static ConnectionFactory<Connection> getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory<Connection> connectionFactory) {
		TFUserHandler.connectionFactory = connectionFactory;
	}
}
