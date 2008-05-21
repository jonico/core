package com.collabnet.ccf.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.openadaptor.auxil.connector.jdbc.writer.MapTableWriter;

public class MapTableUpdater extends MapTableWriter {
	private String sql = null;
	protected PreparedStatement generatePreparedStatement(Connection connection,String tableName,String[] columnNames) throws SQLException {
	    return connection.prepareStatement(sql);
	  }
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	protected PreparedStatement initialiseReusablePreparedStatement() {
		try {
			return connection.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw e;
		}
		return null;
	}
}
