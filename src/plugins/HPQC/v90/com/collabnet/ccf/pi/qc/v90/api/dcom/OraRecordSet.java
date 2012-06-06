package com.collabnet.ccf.pi.qc.v90.api.dcom;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;

public class OraRecordSet implements IRecordSet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(OraRecordSet.class);
	private static String nl = System.getProperty("line.separator");

	private ResultSet rs  = null;
	private ResultSetMetaData meta = null;
	
	
	public OraRecordSet( ResultSet rsinit ) {
		rs = rsinit;
		log.debug("instantiaed OraRecordSet");
	}
	
	private void logCaller() {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
//		String sStack = "Call Stack:" + nl;
//		for ( int i = 0 ; i < stack.length ; i++ ) {
//			StackTraceElement el = stack[i];
//			sStack += i + ": " + el.getClassName() + "." + el.getMethodName() + "@" + el.getLineNumber() + nl;
//		}
//		log.debug(sStack);
		StackTraceElement sElem = stack[ 3 ];
		log.debug(stack[ 2 ].getMethodName() + " called by " + 
				sElem.getClassName() + "." + 
				sElem.getMethodName() + " @" + 
				sElem.getLineNumber() );
	}
	
	private void lErr( String msg , SQLException e ) {
		log.error(msg + nl +
			"Error Code: " + e.getErrorCode() + nl +
			"Message:    " + e.getMessage() + nl +
			"SQL State:  " + e.getSQLState());
	}
	

	@Override
	public void safeRelease() {
		try {
			rs.close();
		} catch (SQLException e) {
			lErr("could not release/close recordset" , e);
		}
	}

	
	@Override
	public int getRecordCount() {
		logCaller();
		int rc = -1;
		try {
			rc = rs.last() ? rs.getRow() : 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			lErr( "Could not get recordcount" , e );
		}
		return rc;
	}
	

	@Override
	public int getColCount() {
		logCaller();
		try {
			return meta.getColumnCount();
		} catch (SQLException e) {
			lErr( "Could not get column count" , e );
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
					lErr( "Could not get current row count ", e );
				}
			}

			if ( found ) {
			try {
				sret = rs.getString( index );
			} catch (SQLException e) {
				lErr( "Could not retrieve value for column #" + index + " row #" + rowIndex , e );
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
			lErr( "Could not retrieve column name at index " + index , e );
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
			lErr( "Could not move to next recordset item" , e );	
		}
	}

	@Override
	public void first() {
		logCaller();
		try {
			rs.first();
		} catch (SQLException e) {
			lErr( "Could not move to first recordset item" , e );	
		}
	}

	@Override
	public void last() {
		logCaller();
		try {
			rs.last();
		} catch (SQLException e) {
			lErr( "Could not move to last recordset item" , e );	
		}
	}

	@Override
	public int getCacheSize() {
		logCaller();
		// VZK currently not implemented!
		return 0;
	}

}
