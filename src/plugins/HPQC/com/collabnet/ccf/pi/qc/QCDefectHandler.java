package com.collabnet.ccf.pi.qc;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.config.Field;
import com.collabnet.ccf.pi.qc.api.IBug;
import com.collabnet.ccf.pi.qc.api.ICommand;
import com.collabnet.ccf.pi.qc.api.IConnection;
import com.collabnet.ccf.pi.qc.api.IFactory;
import com.collabnet.ccf.pi.qc.api.IFactoryList;
import com.collabnet.ccf.pi.qc.api.IFilter;
import com.collabnet.ccf.pi.qc.api.IRecordSet;
import com.collabnet.ccf.pi.qc.api.dcom.Bug;


/**
 * The tracker handler class provides support for listing and/or edit trackers
 * and artifacts.
 */
public class QCDefectHandler {

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

	public IQCDefect getDefectWithId(IConnection qcc, int id) {
		IFactory bf = qcc.getBugFactory();
		IFilter filter = bf.getFilter();
		
		filter.setFilter(QCConfigHelper.bgBugIdFieldName, Integer.toString(id));
		IFactoryList fl = filter.getNewList();

		IBug bug = fl.getBug(1);
		QCDefect defect = new QCDefect((Bug)bug);

		fl.safeRelease();

		filter.safeRelease();
		bf.safeRelease();
		
		return defect;
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
		String sql = "SELECT UNIQUE(AU_ENTITY_ID) FROM AUDIT_LOG WHERE AU_ENTITY_TYPE = 'BUG'";
		if (from != null && !from.equals(""))
			sql += " AND AU_TIME >= '" + from + "'";
		if (to != null && !to.equals(""))
			sql += " AND AU_TIME < '" + to + "'";
		// sql += " AND AU_ACTION_ID = AP_ACTION_ID ";
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

		List<IQCDefect> modifiedDefects = getDefectsWithIds(qcc, ids);
		for (IQCDefect modifiedDefect:modifiedDefects) {
			modifiedDefect.fillFieldsFromBug(qcc);
		}

		// remove the deltas successively and obtain the defect
		// as it was before each delta
		for ( int ctr = 0 ; ctr < ids.size() ; ctr++) {
			int id = ids.get(ctr);
			List<IQCDefect> deltas = getDeltasBetween(qcc, id, from, to);
		}
		
		return modifiedDefects;
	}

	List<IQCDefect> getDeltasBetween(IConnection qcc, int id, String from, String to) {
		
		List<IQCDefect> defects = new ArrayList<IQCDefect>();
		// obtain the transaction ids on this defect between from and to time
		String sql = "SELECT AU_ACTION_ID FROM AUDIT_LOG WHERE AU_ENTITY_TYPE = 'BUG'";
		sql       += "AND AU_ENTITY_ID = " + Integer.toString(id); 
		if (from != null && !from.equals(""))
			sql += " AND AU_TIME >= '" + from + "'";
		if (to != null && !to.equals(""))
			sql += " AND AU_TIME < '" + to + "'";
		sql += " order by AU_TIME DESC, AU_ACTION_ID DESC";
		// sql += " AND AU_ACTION_ID = AP_ACTION_ID ";
		// sql += "AND AU_USER NOT LIKE '" + username + "'";
		log.info(sql);
		
		IRecordSet rs = executeSQL(qcc, sql);

		int rc = rs.getRecordCount();

		IQCDefect defect = getDefectWithId(qcc, id);
		defect.fillFieldsFromBug(qcc);
		// The last IQCDefect contains the defect fields as is
		// NOTE: We are adding to list from the end
		defects.add(rc-1, defect);

		// For every transaction (ACTION_ID)
		for(int cnt = 0 ; cnt < rc-1 ; cnt++, rs.next())
		{
			// Fill the defect with the post-change value
			// Should implement a deep copy later
			defect = getDefectWithId(qcc, id);
			defect.fillFieldsFromBug(qcc);

			int actionId = Integer.parseInt(rs.getFieldValue(QCConfigHelper.auActionIdFieldName));
			// obtain the corresponding changes
			// TODO: take care of attachments too later. These reside in the
			// AUDIT_LOG table itself instead of AUDIT_PROPERTIES
			// Obtain the list values
			String subSql = "SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID = " + actionId;
			IRecordSet subRs = executeSQL(qcc, subSql);
			int rsRc = subRs.getRecordCount();
			
			// For every changed field
			for (int rsCnt = 0 ; rsCnt < rsRc ; subRs.next()) {
				String changedFieldName = subRs.getFieldValue(QCConfigHelper.apFieldNameFieldName);
			
				// replace the newer value with the older value - if any
				for (int i = 0 ; i < defect.getFields().size() ; i++) {
					Field currentField = defect.getFields().get(i);

					// If the field name matches
					if (currentField.getName().equals(changedFieldName)) {
						
						// decide which column to consider based on the datatype
						String oldValueColumnName = QCConfigHelper.apOldValueFieldName;
						if (currentField.getDatatype().equals(QCConfigHelper.memoDataType)) {
							oldValueColumnName = QCConfigHelper.apOldLongValueFieldName;
						}
						
						String oldFieldValue = subRs.getFieldValue(oldValueColumnName);
						// TODO: Handle multiple list values based on the editStyle
						List<String> valuesList = new ArrayList<String>();
						valuesList.add(oldFieldValue);
						
						// Replace the value with the old value
						currentField.setValues(valuesList);
					}
				} // End of - replace the newer value with the older value - if any
			} // End of - For every changed field
			
			defects.add(rc-2-cnt, defect);
		} // END of - For every transaction (ACTION_ID)
		
		return defects;
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
