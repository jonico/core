package com.collabnet.ccf.pi.qc.v90.api.dcom;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;

public class OracRecordSet implements IRecordSet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(OracRecordSet.class);

	private ResultSet rs  = null;
	private ResultSetMetaData meta = null;
	
	
	public OracRecordSet( ResultSet rsinit ) {
		rs = rsinit;
	}
	
	private void lerr( String msg , SQLException e ) {
		log.error(msg);
		log.error("Error Code: " + e.getErrorCode());
		log.error("Message:    " + e.getMessage());
		log.error("SQL State:  " + e.getSQLState());
	}
	

	@Override
	public void safeRelease() {
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void logCaller() {
		int indexer = 3;
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement sElem = stack[stack.length - indexer ];
		log.info(stack[stack.length - indexer + 1].getMethodName() + " called by " + 
				sElem.getClassName() + "." + 
				sElem.getMethodName() + "@" + 
				sElem.getLineNumber() );
	}
	
	@Override
	public int getRecordCount() {
		logCaller();
		int rc = -1;
		try {
			rc = rs.last() ? rs.getRow() : 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			lerr( "Could not get recordcount" , e );
		}
		return rc;
	}
	

	@Override
	public int getColCount() {
		logCaller();
		try {
			return meta.getColumnCount();
		} catch (SQLException e) {
			lerr( "Could not get column count" , e );
		}
		return 0;
	}

	@Override
	public String getFieldValueAsString(String field) {
		logCaller();
		boolean found = false;
		String sret = null;
		int index = -1;
		int rowIndex = 0;
			for ( index = 1 ; index <= this.getColCount() ; index++ ) {
				if ( this.getColNameAsString(index).equalsIgnoreCase(field) ) {
					break;
				}
				try {
					rowIndex = rs.getRow();
				} catch (SQLException e) {
					lerr( "Could not get current row count ", e );
				}
			}

			if ( found ) {
			try {
				sret = rs.getString( index );
			} catch (SQLException e) {
				lerr( "Could not retrieve value for column #" + index + " row #" + rowIndex , e );
			}
		}
		return sret;
	}

	@Override
	public String getColNameAsString(int index) {
		logCaller();
		try {
			return meta.getColumnName(index);
		} catch (SQLException e) {
			lerr( "Could not retrieve column name at index " + index , e );
		}
		return null;
	}

	@Override
	public String getColName(int index) {
		logCaller();
		return getColNameAsString(index);
	}

	@Override
	public void next() {
		logCaller();
		try {
			rs.next();
		} catch (SQLException e) {
			lerr( "Could not move to next recordset item" , e );	
		}
	}

	@Override
	public void first() {
		logCaller();
		try {
			rs.first();
		} catch (SQLException e) {
			lerr( "Could not move to first recordset item" , e );	
		}
	}

	@Override
	public void last() {
		logCaller();
		try {
			rs.last();
		} catch (SQLException e) {
			lerr( "Could not move to last recordset item" , e );	
		}
	}

	@Override
	public int getCacheSize() {
		logCaller();
		// VZK currently not implemented!
		return 0;
	}

}
