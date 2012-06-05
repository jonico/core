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
	private static final String SCHEMAKEY     = "ora.jdbc.schema";
	
	private String driverClassName = null;
	private String connectionString = null;
	private String username = null;
	private String password = null;
	private String schema = null;
	
	private Connection oraConnection = null;
	private Statement oraEngine = null;
	private Properties oraProps = new Properties();
	
	private void lErr( String msg , SQLException e ) {
		log.error( msg + ":" + nl +
		"Error Code: " + e.getErrorCode() + nl +
		"Message:    " + e.getMessage() + nl +
		"SQL State:  " + e.getSQLState() + nl , e );
	}
	
	public OraConnection( String domain , String project ) {

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
		schema = domain.toUpperCase() + "_" + project.toUpperCase() + "_DB";
		
		log.info( "succesfully loaded JDBC properties from " + url.getFile()  + nl + 
				"Driver Class Name: " + driverClassName + nl + 
				"Connection String: " + connectionString + nl + 
				"User Name:         " + username + nl + 
				"Password:          " + password + nl +
				"Schema:            " + schema );
		
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			log.error("Could not instantiate oracle driver " + driverClassName + ", is the file ojdbc6.jar present and in classpath?" , e);
		}
		
		String current = "create connection";
		try {
			oraConnection = DriverManager.getConnection(connectionString , username , password );
			current = "create Engine (Statement)";
			oraEngine = oraConnection.createStatement();
			current = "switch schema";
			this.executeSql("ALTER SESSION SET CURRENT_SCHEMA = " + schema );
		} catch (SQLException e) {
			lErr( "Could not " + current , e );
		}
	}
	public IRecordSet executeSql ( String sql ) {
		
		ResultSet rs = null;
		try {
			rs = this.oraEngine.executeQuery( sql );
		} catch (SQLException e) {
			lErr("Could not execute SQL " + sql , e );
		} 
		
		return new OracRecordSet( rs );
		
	}
}
