package com.collabnet.ccf.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openadaptor.auxil.connector.jdbc.writer.map.MapTableWriter;

public class MapTableUpdater extends MapTableWriter {
	private String sql = null;
	
	/**
	 * log4j logger instance
	 */
	private static final Log log = LogFactory.getLog(MapTableUpdater.class);
	
	protected PreparedStatement generatePreparedStatement(Connection connection,String tableName,String[] columnNames) throws SQLException {
	    return connection.prepareStatement(sql);
	  }
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	protected void initialiseReusablePreparedStatement() {
		try {
			reusablePreparedStatement=connection.prepareStatement(sql);
		} catch (SQLException e) {
			log.error("While creating an prepared statement, an error occured: "+e.getMessage());
		}
		return;
	}
}
