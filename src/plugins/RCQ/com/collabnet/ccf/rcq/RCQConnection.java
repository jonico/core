package com.collabnet.ccf.rcq;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.eis.connection.ConnectionManager;
import com.rational.clearquest.cqjni.CQException;
import com.rational.clearquest.cqjni.CQSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RCQConnection {
	
	private String username;
	private String password;
	private String schema;
	private String database;
	private String recType;
	private String historyName;
	
	private CQSession cqs;

	private static final Log log = LogFactory.getLog(RCQConnection.class);

	/**
	 * 
	 * @param repositoryId			schema-database, e.g. samplerepo-DEFSM
	 * @param repositoryKind		currently CQ?
	 * @param connectionInfo		unused
	 * @param credentialInfo		username:password
	 * @param connectionManager
	 */
	
	
	public RCQConnection(String repositoryId, String repositoryKind,
			String connectionInfo, String credentialInfo,
			ConnectionManager<RCQConnection> connectionManager) {
		if (credentialInfo != null) {
			String[] splitCredentials = credentialInfo
					.split(RCQConnectionFactory.PARAM_DELIMITER);
			if (splitCredentials != null) {
				if (splitCredentials.length == 1) {
					username = splitCredentials[0];
					password = "";
				} else if (splitCredentials.length == 2) {
					username = splitCredentials[0];
					password = splitCredentials[1];
				} else {
					throw new IllegalArgumentException(
							"Credentials info is not valid.");
				}
			}
		}

		
		// repository is managed in the gui, returning domain-project-reqtype == schema-database-RecordType, e.g. samplerepo-DEFSM-Defect;
		String[] repoParts = parseRepositoryId( repositoryId );
		
		schema = repoParts[0];
		database = repoParts[1];
		recType = repoParts[2];
		
		setCqs(new CQSession() );
		
		log.debug("logging in with username = " + username + " and password ='" + password + "'");
		log.debug("connection: database '" + database + "' and schema '" + schema + "'");
		
		try {
			cqs.UserLogon( username , password , database , schema );
			log.debug("succesfully logged in!, FeatureLevel: " + cqs.GetSessionFeatureLevel());
			log.info("Succesfully connected to ClearQuest.");
		} catch (CQException e) {
			log.debug("problem logging in...", e);		
		}
	}

	private String[] parseRepositoryId( String repositoryId ) {
		
		String[] ret = repositoryId.split("-");
		if ( ret.length != 3 ) {
			log.error("not enough or too many repository info!");
			for ( int i = 0 ; i < ret.length - 1 ; i++) {
				log.error(i + ": " + ret[i]);
			}
			throw new CCFRuntimeException("wrong repository information");
		} else {
			return ret;
		}
	}
	
	public void shutdown() {
		try {
			if ( cqs.CheckHeartbeat() ) {
				cqs.detach();
			}
		} catch (CQException e) {
			log.error("cannot detach clearquest" , e);
		}
	}
	
	public CQSession getCqs() {
		// FIXME: after some time, the session gets killed by CQ. Need top check here and re-connect if necessary 
		return cqs;
	}

	public void setCqs( CQSession cqs ) {
		this.cqs = cqs;
	}
	
	public String getHistoryFieldName() {
		// FIXME revert after we got the param from the gui
		// return this.historyName;
		return "history";
	}
	
	public String getRecType() {
		return recType;
	}
}
