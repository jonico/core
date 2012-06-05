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


import org.apache.log4j.Logger;

import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;

public class OraConnection {

	private static final Logger log = Logger.getLogger(OraConnection.class);
	private static String nl = System.getProperty("line.separator");

	private static final String DRIVERKEY     = "ora.jdbc.driver";
	private static final String CONNECTIONKEY = "ora.jdbc.connection";
	private static final String USERNAMEKEY   = "ora.jdbc.user.name";
	private static final String PASSWORDKEY   = "ora.jdbc.user.password";
	
	private String driverClassName = null;
	private String connectionString = null;
	private String username = null;
	private String password = null;
	
	private Connection oraConnection = null;
	private Statement oraEngine = null;
	private Properties oraProps = new Properties();
	
	public OraConnection() {

		// currently not using spring
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
		
		log.info( "succesfully loaded JDBC properties from " + url.getFile()  + nl + 
				"Driver Class Name: " + driverClassName + nl + 
				"Connection String: " + connectionString + nl + 
				"User Name:         " + username + nl + 
				"Password:          " + password );
		
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			log.error("Could not instantiate oracle driver " + driverClassName + ", is the file ojdbc6.jar present and in classpath?" , e);
		}
		
		String current = "connection";
		try {
			oraConnection = DriverManager.getConnection(connectionString , username , password );
			current = "Engine (Statement)";
			oraEngine = oraConnection.createStatement();
		} catch (SQLException e) {
			log.error("Could not create " + current + " to Oracle DB:");
			log.error("Error Code: " + e.getErrorCode());
			log.error("Message:    " + e.getMessage());
			log.error("SQL State:  " + e.getSQLState());
			log.error("No " + current , e);
		}
	}
	public IRecordSet executeSql ( String sql ) {
		
		ResultSet rs = null;
		try {
			rs = this.oraEngine.executeQuery( sql );
		} catch (SQLException e) {
			log.error("Could not execute SQL " + sql );
			log.error("Error Code: " + e.getErrorCode());
			log.error("Message:    " + e.getMessage());
			log.error("SQL State:  " + e.getSQLState());
			log.error("Failed SQL execution", e);
		} 
		
		return new OracRecordSet( rs );
		
	}
}
