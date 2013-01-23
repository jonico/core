package com.collabnet.ccf.pi.qc.v90.api.dcom;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

import org.apache.log4j.Logger;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;

public class OraRecordSet implements IRecordSet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(OraRecordSet.class);
	private static final String nl = System.getProperty("line.separator");

	private ResultSet rs  = null;
	private ResultSetMetaData meta = null;
	private int recCount = -1;
	private int colCount = -1;
	private String loggedMethods = "";
	private Statement myEngine;
	
	private int uid = 0;
	private int rsid = 0;
	
	public OraRecordSet(Connection jdbcConnection , String schema, String loggedmethods, int callerHash , String sql ) {
		// create Engine
		// switch schema

		loggedMethods = loggedmethods;
		
		String what = "create Engine / Statement";
		try {
			myEngine = jdbcConnection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			what = "switch to schema " + schema;
			myEngine.execute( "ALTER SESSION SET CURRENT_SCHEMA = " + schema );
			what = "execute query " + sql;
			rs = myEngine.executeQuery(sql);
			what = "get meta data for recordset";
			meta = rs.getMetaData();
			what = "get record count";
			recCount = rs.last() ? rs.getRow() : 0;
			what = "move to first record";
			// mimics COM RecordSet behavior (these don't have a cursor beforeFirst, I assume)
			rs.first();

			uid = this.hashCode();
			rsid = ( rs == null ) ? 0 : rs.hashCode();
		} catch ( SQLException e ) {
			lErr("Could not " + what , e );
		}
		
		
		log.debug( "instantiated OraRecordSet " + uid + " with " + recCount + " records; connectionHash: " + callerHash +"; result Set Hash: " + rsid + nl + "\t[" + uid + "] " + sql );
	}
	
	private void logCaller(String args , String appendix ) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement sElem = stack[ 3 ];

		if ( loggedMethods.contains(stack[ 2 ].getMethodName())) 
			log.debug("[" + uid + "] " + stack[ 2 ].getMethodName() + "(" + args + ") called by " + 
				sElem.getClassName() + "." + 
				sElem.getMethodName() + " @" + 
				sElem.getLineNumber() + appendix );
	}
	
	private void lErr( String msg , SQLException e ) {
		String appendix = "";
		if ( rs != null ) {
			try {
				appendix = "Cursor @ " + rs.getRow();
			} catch (SQLException e1) {
				appendix = "Cursor @ (undefined)" ;
			}
		} else {
			appendix = "Cursor @ (no recordset)" ;
		}
		
		log.error("[" + uid + "] " + msg + nl +
			"[" + uid + "] Error #: " + e.getErrorCode() + "; " +
			"SQLState: " + e.getSQLState() + "; " + 
			"Message: " + e.getMessage() + "; " + appendix);
	}
	

	@Override
	public void safeRelease() {
		logCaller("", nl);
		String doing = "close recordset";
		try {
			
			rs.close();
			doing = "close Statement";
			myEngine.close();
		} catch (SQLException e) {
			lErr("could not " + doing , e);
		}
	}

	
	@Override
	public int getRecordCount() {
		logCaller("" , " ==> " + recCount);
		return recCount;
	}	
	
	@Override
	public int getColCount() {
		if ( colCount == -1 ) {
			logCaller("", " --- initial call");
			try {
				colCount = meta.getColumnCount();
			} catch (SQLException e) {
				lErr( "Could not get column count" , e );
			}
		} else {
			logCaller("", " ==> " + colCount);
		}
		return colCount;
	}

	@Override
	public String getFieldValueAsString(String field) {
		boolean found = false;
		String sret = null;
		int index = -1;
		int cc = this.getColCount();
		for ( index = 1 ; index <=  cc ; index++ ) {
			if ( this.getColNameAsString(index).equalsIgnoreCase(field) ) {
				try {
					sret = rs.getString(index);
				} catch (SQLException e) {
					lErr("Could not retrieve value for column #" + index , e);
				}
				found = true;
				break;
			}
		}

		if (!found) {
			log.error( "getFieldValueAsString did not find a field named '" + field + "'!" );
		}
		
		logCaller("String field = '" + field + "'" , " --> " + sret);
		return sret;
	}

	@Override
	public String getColNameAsString(int index) {
		String sret = ""; 
		try {
			sret = meta.getColumnName(index);
		} catch (SQLException e) {
			lErr( "Could not retrieve column name at index " + index , e );
		}
		logCaller("int index = " + index , " --> " + sret );
		return sret;
	}

	@Override
	public String getColName(int index) {
		return getColNameAsString(index);
	}

	@Override
	public void next() {
		String action = "get new row number";
		int oldRow = -1;
		String rowInfo = "";
		try {
			oldRow = rs.getRow();
			action = "move to next recordset item";
			rs.next();
			if ( !rs.isAfterLast() ) {
				rowInfo = " moved: " + oldRow + " -> " + rs.getRow();
			} else {
				rowInfo = " reached last row at " + oldRow + " ";
			}
		} catch (SQLException e) {
			lErr( "Could not " + action , e );	
		}
		logCaller("" , rowInfo);
	}

	@Override
	public void first() {
		logCaller("" , "");
		try {
			rs.first();
		} catch (SQLException e) {
			lErr( "Could not move to first recordset item" , e );	
		}
	}

	@Override
	public void last() {
		logCaller("" , "");
		try {
			rs.last();
		} catch (SQLException e) {
			lErr( "Could not move to last recordset item" , e );	
		}
	}

	@Override
	public int getCacheSize() {
		logCaller("" , "");
		// VZK currently not implemented!
		return 0;
	}

}
