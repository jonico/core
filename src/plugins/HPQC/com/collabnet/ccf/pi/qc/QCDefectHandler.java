package com.collabnet.ccf.pi.qc;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import td2jira.td.TDConnector;
import td2jira.td.api.IBug;
import td2jira.td.api.ICommand;
import td2jira.td.api.IConnection;
import td2jira.td.api.IFactory;
import td2jira.td.api.IFactoryList;
import td2jira.td.api.IFilter;
import td2jira.td.api.IRecordSet;
import td2jira.td.api.dcom.Bug;

/**
 * The tracker handler class provides support for listing and/or edit trackers
 * and artifacts.
 */
public class QCDefectHandler {

	private TDConnector td = null;
	private String username;
	
	private static final Log log = LogFactory.getLog(QCDefectHandler.class);

	public IRecordSet executeSQL(IConnection qcc, String sql){
		ICommand command = qcc.getCommand();
		command.setCommandText(sql);
		return command.execute();
	}

	public List<IQCDefect> getDefectsWithIds(IConnection qcc, List<Integer> ids) {
		IFactory bf = qcc.getBugFactory();
		IFilter filter = bf.getFilter();
		
		List<IQCDefect> tasks = new ArrayList<IQCDefect>();
		for( int i = 0 ; i < ids.size() ; ++i ) {
			filter.setFilter("BG_BUG_ID", ids.get(i).toString());
			IFactoryList fl = filter.getNewList();

			IBug bug = fl.getBug(1);
			QCDefect defect = new QCDefect((Bug)bug);

			tasks.add(defect);
			
			fl.safeRelease();
		}

		filter.safeRelease();
		bf.safeRelease();
		
		return tasks;
	}

	public IQCDefect updateDefect(IConnection qcc, String bugId, List<String> fieldNames, List<String> fieldValues) throws Exception {

		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.getItem(bugId);
		
		for (int cnt = 0 ; cnt < fieldNames.size() ; cnt++) {
			bug.setField(fieldNames.get(cnt), fieldValues.get(cnt));
		}
		bug.post();
		
		return new QCDefect((Bug)bug);
	}

	public IQCDefect createDefect(IConnection qcc, List<String> fieldNames, List<String> fieldValues) throws Exception {

		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.addItem("Created by the connector");
		
		for (int cnt = 0 ; cnt < fieldNames.size() ; cnt++) {
			bug.setField(fieldNames.get(cnt), fieldValues.get(cnt));
		}
		bug.post();
		
		return new QCDefect((Bug)bug);
	}

	/**
	 * Return all defects modified between the given time range, in a map
	 * 
	 * @param from
	 * 		Start time from which modifications have to be queried.
	 * @param to
	 * 		End time upto which modifications have to be queried.
	 * @return null if no modifications were found, else a list of modified
	 *         defects
	 * @throws RemoteException (, COMException?)
	 */
	public List<IQCDefect> getChangedDefects(IConnection qcc, String from, String to)
	 throws Exception {
		String sql = "SELECT DISTINCT(AU_ENTITY_ID) FROM AUDIT_LOG, AUDIT_PROPERTIES WHERE AU_ENTITY_TYPE = 'BUG'";
		if (from != null && !from.equals(""))
			sql += " AND AU_TIME >= '" + from + "'";
		if (to != null && !to.equals(""))
			sql += " AND AU_TIME < '" + to + "'";
		sql += " AND AU_ACTION_ID = AP_ACTION_ID ";
		// sql += "AND AU_USER NOT LIKE '" + username + "'";
		log.info(sql);
		
		IRecordSet rs = executeSQL(qcc, sql);

		int rc = rs.getRecordCount();
		List<Integer> ids = new ArrayList<Integer>();
		for(int cnt = 0 ; cnt < rc ; cnt++, rs.next())
		{
			int defectId = Integer.parseInt(rs.getFieldValue("AU_ENTITY_ID"));
			ids.add(defectId);
		}

		return getDefectsWithIds(qcc, ids);
	}

	public void deleteDefect(String id) {
		// Yet to implement
	}
	
	public IQCDefect[] getDefectsWithOtherSystemId(IConnection qcc, String otherSystemIdField, String otherSystemIdValue) {
		IFactory bugFactory = qcc.getBugFactory();
		IFilter filter = bugFactory.getFilter();
		IFactoryList factoryList;

		log.error("--------------");
		log.error(otherSystemIdField);
		log.error(otherSystemIdValue);
		log.error("--------------");
		filter.setFilter(otherSystemIdField, otherSystemIdValue);
		factoryList = filter.getNewList();
		
		int factoryListCount = factoryList.getCount();
		IQCDefect[] qcDefectArray = new IQCDefect[factoryListCount];
		for( int i = 1 ; i <= factoryListCount ; ++i ) {
			IBug bug = factoryList.getBug(i);
			qcDefectArray[i-1] = new QCDefect((Bug)bug);
		}

		return qcDefectArray;
	}
}
