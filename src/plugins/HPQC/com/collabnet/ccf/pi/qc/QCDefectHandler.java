package com.collabnet.ccf.pi.qc;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifact;
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

	public QCDefect getDefectWithId(IConnection qcc, int id) {
		IFactory bf = qcc.getBugFactory();
		IFilter filter = bf.getFilter();
		
		filter.setFilter(QCConfigHelper.bgBugIdFieldName, Integer.toString(id));		
		IFactoryList fl = filter.getNewList();
		
		IBug bug = null; 
		try {
		bug = fl.getBug(1);
		}
		catch(Exception e) {
			log.error("Exception caught in getDefectWithId of DefectHandler");
			return null;
		}
		QCDefect defect = new QCDefect((Bug)bug);

		fl.safeRelease();

		filter.safeRelease();
		bf.safeRelease();
		
		return defect;
	}

	public IQCDefect updateDefect(IConnection qcc, String bugId, List<GenericArtifactField> allFields) throws Exception {

		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.getItem(bugId);
		
		for (int cnt=0; cnt < allFields.size(); cnt++) {
			
			GenericArtifactField thisField = allFields.get(cnt);
			String fieldName = thisField.getFieldName();
			String fieldValue = (String) thisField.getFieldValue();
			
			if(fieldName.equals("BG_DEV_COMMENTS")) {
				String oldFieldValue = bug.getFieldAsString(fieldName);
				fieldValue = oldFieldValue+" "+fieldValue;				
			}	
			if(!( fieldName.equals("BG_BUG_ID") || fieldName.equals("BG_BUG_VER_STAMP") || fieldName.equals("BG_VTS") 
					|| fieldName.equals("BG_SUBJECT")) )
			bug.setField(fieldName, fieldValue);
		}
		bug.post();
		
		return new QCDefect((Bug)bug);
	}

	public IQCDefect createDefect(IConnection qcc, List<GenericArtifactField> allFields) throws Exception {

		IFactory bugFactory = qcc.getBugFactory();
		IBug bug = bugFactory.addItem("Created by the connector");
		
		/*for (int cnt = 0 ; cnt < fieldNames.size() ; cnt++) {
			bug.setField(fieldNames.get(cnt), fieldValues.get(cnt));
		}
		*/
		for (int cnt=0; cnt < allFields.size(); cnt++) {
			
			GenericArtifactField thisField = allFields.get(cnt);
			String fieldName = thisField.getFieldName();
			String fieldValue = (String) thisField.getFieldValue();
			/* The following fields cannot be set or have some conditions
			Cannot be set from here:
			1. BG_BUG_ID
			2. BG_BUG_VER_STAMP
			3. BG_VTS
			Has some conditions:
			1. BG_SUBJECT -> Can be set to a Valid value that is present in the list.
			
			*/
			if(!( fieldName.equals("BG_BUG_ID") || fieldName.equals("BG_BUG_VER_STAMP") || fieldName.equals("BG_VTS") 
					|| fieldName.equals("BG_SUBJECT")) )
			bug.setField(fieldName, fieldValue);
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
	public List<GenericArtifact> getChangedDefects(IConnection qcc, String from, String to, String sourceArtifactId, String sourceRepositoryId, String sourceRepositoryKind, String sourceSystemId, String sourceSystemKind, String targetRepositoryId, String targetRepositoryKind, String targetSystemId, String targetSystemKind)
	 throws Exception {
		
		// Obtain the transactions that happened within the from and to time
		//Test Values: 
		//1. from="2007-11-05 00:00:00"; to="2007-11-06 00:00:00";
		//2. from="2007-09-15 00:00:00"; to="2007-10-02 00:00:00";
		
		String sql = "SELECT AU_ACTION_ID, AU_ENTITY_ID FROM AUDIT_LOG WHERE AU_ENTITY_TYPE = 'BUG'";
		if (from != null && !from.equals(""))
			sql += " AND AU_TIME >= '" + from + "'";
		if (to != null && !to.equals(""))
			sql += " AND AU_TIME <= '" + to + "'";
		sql += " ORDER BY AU_TIME ASC";
		log.info(sql);
		
		IRecordSet rs = executeSQL(qcc, sql);

		int rc = rs.getRecordCount();
		List<GenericArtifact> modifiedDefectArtifacts = new ArrayList<GenericArtifact>();
		
		for(int cnt = 0 ; cnt < rc ; cnt++, rs.next())
		{
			int actionId = Integer.parseInt(rs.getFieldValue("AU_ACTION_ID"));
			int entityId = Integer.parseInt(rs.getFieldValue("AU_ENTITY_ID"));
			
			QCDefect latestDefect = getDefectWithId(qcc, entityId);
			GenericArtifact latestDefectArtifact = latestDefect.getGenericArtifactObject(qcc);
			
			latestDefectArtifact.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
			latestDefectArtifact.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);
			
			//String lastModifiedDate = latestDefectArtifact.getAllGenericArtifactFieldsWithSameFieldName("BG_VTS").get(0).toString();
			//latestDefectArtifact.setArtifactLastModifiedDate(lastModifiedDate);
			
			Boolean isNewDefect = checkForCreate(qcc, actionId);
			if(isNewDefect==true)
				latestDefectArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
			else
				latestDefectArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
			
			latestDefectArtifact.setSourceArtifactId(sourceArtifactId);
			latestDefectArtifact.setSourceRepositoryId(sourceRepositoryId);
			latestDefectArtifact.setSourceRepositoryKind(sourceRepositoryKind);
			latestDefectArtifact.setSourceSystemId(sourceSystemId);
			latestDefectArtifact.setSourceSystemKind(sourceSystemKind);
			
			latestDefectArtifact.setTargetRepositoryId(targetRepositoryId);
			latestDefectArtifact.setTargetRepositoryKind(targetRepositoryKind);
			latestDefectArtifact.setTargetSystemId(targetSystemId);
			latestDefectArtifact.setTargetSystemKind(targetSystemKind);
			
			latestDefectArtifact=getStateOfDefectAtActionID(qcc, entityId, actionId, from, to, latestDefectArtifact);
			
			modifiedDefectArtifacts.add(latestDefectArtifact);
		}

		return modifiedDefectArtifacts;
	}
	
	public GenericArtifact getStateOfDefectAtActionID(IConnection qcc, int entityId, int actionId, String from, String to, GenericArtifact latestDefectArtifact){
		
		String sql = "SELECT AU_ACTION_ID FROM AUDIT_LOG WHERE AU_ENTITY_TYPE = 'BUG' AND AU_ACTION_ID > '" + actionId + 
		"' AND AU_ENTITY_TYPE= 'BUG' AND AU_ENTITY_ID = '" + entityId + "'";
		if (from != null && !from.equals(""))
			sql += " AND AU_TIME >= '" + from + "'";
		if (to != null && !to.equals(""))
			sql += " AND AU_TIME <= '" + to + "'";
		sql += " ORDER BY AU_ACTION_ID DESC";
		log.info(sql);
		
		IRecordSet rs = executeSQL(qcc, sql);
		int rc = rs.getRecordCount();
		int txnId =0, reqId=0;
		for(int cnt=0; cnt < rc; cnt++, rs.next()){
			
			txnId = Integer.parseInt(rs.getFieldValue("AU_ACTION_ID"));
			sql = "SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID= '"+ txnId + "'";
			IRecordSet newRs = executeSQL(qcc, sql);
			int newRc = newRs.getRecordCount();
			
			for (int newCnt=0; newCnt < newRc; newCnt++, newRs.next()){
				String fieldName = newRs.getFieldValue("AP_FIELD_NAME");
				String oldFieldValue = new String();
				if(!(fieldName.equals("BG_DESCRIPTION")))
					oldFieldValue = newRs.getFieldValue("AP_OLD_VALUE");
				else
					oldFieldValue = newRs.getFieldValue("AP_OLD_LONG_VALUE");
				
				List<GenericArtifactField> genArtifactFields = latestDefectArtifact.getAllGenericArtifactFieldsWithSameFieldName(fieldName);
				if(genArtifactFields!=null & genArtifactFields.get(0)!=null)genArtifactFields.get(0).setFieldValue(oldFieldValue);
			}
			//if(cnt==rc-1) reqId=txnId-1;
		}
		
		
		List<GenericArtifactField> genArtifactFields = latestDefectArtifact.getAllGenericArtifactFieldsWithSameFieldName("BG_VTS");
		/*if(genArtifactFields!=null && genArtifactFields.get(0)!=null && 
				(genArtifactFields.get(0).getFieldValue()==null || genArtifactFields.get(0).getFieldValue().equals(""))) {
			latestDefectArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.CREATE);
			latestDefectArtifact.setArtifactLastModifiedDate("0000-00-00 00:00:00");
			return latestDefectArtifact;
		}
			
		*/
		if(genArtifactFields!=null && genArtifactFields.get(0)!=null && genArtifactFields.get(0).getFieldValue()!=null &&
				!(genArtifactFields.get(0).getFieldValue().equals(""))) {
			//latestDefectArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
			latestDefectArtifact.setArtifactLastModifiedDate((String)genArtifactFields.get(0).getFieldValue());
			return latestDefectArtifact;
		}
			
		
		// The ArtifactActionValue IGNORE and DELETE needs to be done.
		//latestDefectArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UNKNOWN);
		return latestDefectArtifact;
		
	}
	
	/** 
	 * Given an action id (id for the AUDIT_LOG table 
	 * 
	 * @param actionId
	 * @return the defect at the time of the actionId
	 */
	public QCDefect getArtifactStateFromActionId(int actionId) {
		// return the state of the given defect at transaction actionId
		
		QCDefect defect = null;
		return defect;
	}
	
	public boolean checkForCreate(IConnection qcc, int txnId) {
		
		Boolean check = false;
		int newRc =0;
		String sql = "SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID= '"+ txnId + "'";
		IRecordSet newRs = executeSQL(qcc, sql);
		if(newRs!=null)
			newRc = newRs.getRecordCount();
		
		for (int newCnt=0; newCnt < newRc; newCnt++, newRs.next()) {
			String fieldName = newRs.getFieldValue("AP_FIELD_NAME");
			String oldFieldValue = new String();
			if(!(fieldName.equals("BG_DESCRIPTION")))
				oldFieldValue = newRs.getFieldValue("AP_OLD_VALUE");
			else
				oldFieldValue = newRs.getFieldValue("AP_OLD_LONG_VALUE");
			
			if(fieldName.equals("BG_VTS") && (oldFieldValue==null || (oldFieldValue!=null && oldFieldValue.equals(""))) )
				return true;
		}
		return check;
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
