package com.collabnet.ccf.pi.qc.v90.api.dcom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import oracle.net.ano.AnoServices;
import oracle.net.ano.Service;
import oracle.net.ns.NetException;

import org.apache.log4j.Logger;

import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;

public class OraConnection {

	private static final Logger log = Logger.getLogger(OraConnection.class);
	private static String nl = System.getProperty("line.separator");

	private static final String DRIVERKEY     = "ora.jdbc.driver";
	private static final String CONNECTIONKEY = "ora.jdbc.connection";
	private static final String USERNAMEKEY   = "ora.jdbc.user.name";
	private static final String PASSWORDKEY   = "ora.jdbc.user.password";
	private static final String LOGGEDRSKEY   = "ora.jdbc.OraRecordSet.calllog.included";
	private static final String ENCRYPTKEY    = "ora.jdb.encrypted";
	private static final String SCHEMAEXTKEY  = "ora.jdbc.schema.extension";

	
	private String driverClassName = null;
	private String connectionString = null;
	private String username = null;
	private String password = null;
	private String schema = null;
	private String loggedOraRecordMethods = null;
	private boolean useEncryption = false;
	private String schemaAppendix = null;
	
	private String myVersion = "0.9.2";
	
	private Connection oraConnection = null;
	private Statement oraEngine = null;
	private Properties oraProps = new Properties();
	private Properties cProps = new Properties();
	
	private void lErr( String msg , SQLException e ) {
		log.error( "[" + this.hashCode() + "] " + msg + nl +
		"Error Code: " + e.getErrorCode() + nl +
		"Message:    " + e.getMessage() + nl +
		"SQL State:  " + e.getSQLState() + nl , e );
	}
	
	public OraConnection( String domain , String project ) {

		// currently not using spring to get property file location
		URL url = ClassLoader.getSystemResource("oracle.jdbc.properties");
		if ( url != null ) {
			try {
				oraProps.load( new FileInputStream( new File(url.getFile())));
			} catch ( IOException e) {
					log.error("Could not open oracle jdbc config file: " + url.getFile() , e);
			}
		} else {
			log.error("Could not find oracle jdbc config file: is oracle.jdbc.properties present and in classpath?");
		}
		
		driverClassName = oraProps.getProperty(DRIVERKEY);
		connectionString = oraProps.getProperty(CONNECTIONKEY);
		username = oraProps.getProperty(USERNAMEKEY);
		password = oraProps.getProperty(PASSWORDKEY);
		schemaAppendix = oraProps.getProperty(SCHEMAEXTKEY);
		schema = domain.toUpperCase() + "_" + project.toUpperCase() + schemaAppendix ;
		loggedOraRecordMethods = oraProps.getProperty(LOGGEDRSKEY);
		useEncryption = !oraProps.getProperty(ENCRYPTKEY).equals("0");
		
		String passwordInfo = ( !password.equals("") ) ? "(given)" : "(not given)" ;
		
		log.info( "Oracle JDBC Patch version " + myVersion + nl +
				"succesfully loaded JDBC properties from " + url.getFile()  + nl + 
				"Driver Class Name: " + driverClassName + nl + 
				"Connection String: " + connectionString + nl + 
				"User Name:         " + username + nl + 
				"Password:          " + passwordInfo + nl +
				"Use Encryption:    " + useEncryption + nl + 
				"Schema:            " + schema );
		
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			log.error("Could not instantiate oracle driver " + driverClassName + ", is the file ojdbc6.jar present and in classpath?" , e);
		}
	
		this.connect();
	}
	
	private void connect() {
		cProps.put("user", username );
		cProps.put("password", password);
		
		if ( useEncryption ) {
			int level = AnoServices.REQUIRED;
			try {
				cProps.put("oracle.net.encryption_client", Service.getLevelString(level));
				// Set the Client encryption selected list
				cProps.put("oracle.net.encryption_types_client", "(RC4_40)");
				// Set the Client integrity level
				cProps.put("oracle.net.crypto_checksum_client", Service.getLevelString(level));
				// Set the client integrity selected list
				cProps.put("oracle.net.crypto_checksum_types_client", "( MD5 )");
			} catch (NetException e) {
				log.error("could not enable encryption!" , e );
			}
		}
		
		String current = "create connection";
		
		try {
			oraConnection = DriverManager.getConnection(connectionString , cProps );
			current = "create Engine (Statement)";
			oraEngine = oraConnection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			current = "switch schema";
			this.oraEngine.execute("ALTER SESSION SET CURRENT_SCHEMA = " + schema );
			log.debug("[" + this.hashCode() + "] switched schema to " + schema );
		} catch (SQLException e) {
			lErr( "Could not " + current , e );
		}
		
	}

	private void reconnect() {
		
		String current = "clear all SQL statements";
		
		try {
			oraEngine.clearBatch();
			current = "close engine";
			oraEngine.close();
			current = "close JDBC connection";
			oraConnection.close();
			this.connect();
			log.debug("[" + this.hashCode() + "] successfully reconnected to Oracle DB" );
		} catch (SQLException e) {
			lErr( "Could not "+ current , e );
		}
		
	}
	
	private boolean needsReconnect() {

		String current = "evaluate engine status";
		try {
			if ( oraEngine == null &&  oraEngine.isClosed() )
				return true;
			current = "evaulate JDBC connection status";
			if ( !oraConnection.isValid(0) )
				return true;
		} catch (SQLException e) {
			lErr("Could not " + current , e );
		}

		return false;
	}
	
	public IRecordSet executeSql ( String sql ) {
		
		ResultSet rs = null;
		OraRecordSet orc = null;
		
		if ( this.needsReconnect() ) 
			this.reconnect();
		
		try {
			logCaller("" , nl + "SQL: " + sql );
			rs = this.oraEngine.executeQuery( sql );
			log.debug("[" + this.hashCode() + "] successfully executed: " + sql);
			orc = new OraRecordSet(rs , loggedOraRecordMethods , this.hashCode() );
		} catch (SQLException e) {
			lErr("Could not execute SQL " + sql , e );
		} 
		return orc;
		
	}
	
	public void safeRelease() {
		String current = "close engine/statement";
		try {
			this.oraEngine.close();
			current = "close JDBC connection";
			this.oraConnection.close();
		} catch (SQLException e) {
			lErr( "Could not " + current , e );
		}
	}
	
	private void logCaller(String args , String appendix ) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement sElem = stack[ 4 ];
		log.debug( "[" + this.hashCode() + "] executeSql(" + args + ") called by " + 
			sElem.getClassName() + "." + 
			sElem.getMethodName() + " @" + 
			sElem.getLineNumber() + appendix );
	}

}
