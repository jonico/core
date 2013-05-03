package com.collabnet.ccf.rcq;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.hospital.CCFExceptionToOrderedMapConvertor;
import com.collabnet.ccf.core.utils.DateUtil;

import com.rational.clearquest.cqjni.CQEntity;
import com.rational.clearquest.cqjni.CQEntityDef;
import com.rational.clearquest.cqjni.CQException;
import com.rational.clearquest.cqjni.CQFieldInfo;
import com.rational.clearquest.cqjni.CQFieldInfos;
import com.rational.clearquest.cqjni.CQFilterNode;
import com.rational.clearquest.cqjni.CQHistories;
import com.rational.clearquest.cqjni.CQHistory;
import com.rational.clearquest.cqjni.CQHistoryField;
import com.rational.clearquest.cqjni.CQHistoryFields;
import com.rational.clearquest.cqjni.CQQueryDef;
import com.rational.clearquest.cqjni.CQQueryFilterNode;
import com.rational.clearquest.cqjni.CQResultSet;
import com.rational.clearquest.cqjni.CQSession;

public class RCQHandler {

	public String WI_QUERY = "Select [Id] From WorkItems Where [Work Item Type] = '?' Order By [Changed Date] Desc";

	private static final Log log = LogFactory.getLog(RCQHandler.class);
	
	private enum HistoryType {
		ID , DATE , USER , ACTION , OLDSTATE , NEWSTATE , INDEX
	}
	
	private enum HistoryEnding {
		FIRST, LAST
	}
	
	private List<String> atrList;
	
	public void getChangedRecords(RCQConnection connection,
			Date lastModifiedDate, 
			String lastSynchronizedVersion,
			String lastSynchedArtifactId,
			ArrayList<ArtifactState> artifactStates, 
			boolean ignoreConnectorUserUpdates) throws ParseException {

		CQQueryDef myQD = null;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		
		//build not exec query.
//		log.debug("building query for record type '" + connection.getRecType() + "'");
		try {
			myQD = connection.getCqSession().BuildQuery(connection.getRecType());
		} catch (CQException e) {
			log.error( "Problem building the query" , e);
			throw new CCFRuntimeException("problem creating a clearquest query" , e);
		}
		
		try {
			// here we only need the ids, all other necessary info will be pulled via getEntity()
			myQD.BuildField("id");

		} catch (CQException e) {
			log.error("problem adding id field to result set." , e);
			throw new CCFRuntimeException("problem building getChangedRecords query for clearquest" , e);
		}
		
		try {
			// TODO add ascending sort on id field.
			// BOOL_OP_AND = 1
			CQQueryFilterNode operator = myQD.BuildFilterOperator(1);
			// COMP_OP_GT = 5, GTE = 6
			String[] lastDate = new String[1];
			lastDate[0] =  formatter.format(lastModifiedDate);
					
			operator.BuildFilter("history.action_timestamp", 5 , lastDate );
			
		} catch (CQException e1) {
			log.error("Could not create filter for last modified");
		}
		
		// finally, execute the query.
		CQResultSet results = null;
		long numResults = 0;
		try {
			results = connection.getCqSession().BuildResultSet(myQD);
			results.EnableRecordCount();
			numResults =  results.ExecuteAndCountRecords();
			
		} catch (CQException e) {
			if( e.getMessage().contains("\\bugbert.src\\cqobjectmodel\\cqresultset.cpp: line 505-SQLExecDirect: RETCODE=-1, State=S1001, Native Error=-1311") ) {
				// MS Access too many open tables
				connection.reconnect();
				try {
					results = connection.getCqSession().BuildResultSet(myQD);
					results.EnableRecordCount();
					numResults =  results.ExecuteAndCountRecords();
					log.warn("had to reconnect to ms access!");
				} catch (CQException e1) {
					log.error("could not run getChangedRecords query after reconnecting ms access",e);
					throw new CCFRuntimeException("problem querying clearquest" , e);
				}
				
			}
			log.error("could not run getChangedRecords query",e);
			throw new CCFRuntimeException("problem querying clearquest" , e);
		}
		
		// build the result set as array of ArtifactState items
		long count = 1;
		long updates = 0;
		try {
			long status = results.MoveNext();
			while ( status == 1 ) {
				
				String curCQId = getValueFromResultSet( results , "id" );
				CQEntity record = connection.getCqSession().GetEntity( connection.getRecType() ,  curCQId );
				// updates won't be caught if we do not reload.
				record.Reload();
				RCQHistoryHelper myStory = new RCQHistoryHelper(record, connection);

				Date artLastModified = myStory.GetLastModfiedDate();
				
				ArtifactState artState = new ArtifactState();

				artState.setArtifactId( curCQId );
				artState.setArtifactLastModifiedDate(artLastModified);
				artState.setArtifactVersion(myStory.GetLastVersion());
				artifactStates.add(artState);
				updates++;
				count++;
				status = results.MoveNext();
			}
		} catch (CQException e) {
			log.error("Problem getting data from resultset");
			throw new CCFRuntimeException("issue getting data", e);
		}
		//log.debug("fetched " + (count - 1) + " records from clearquest, " + updates + " of them are updated after " + lastModifiedDate.toString());
//		log.debug("-----> Fetch Results:");
		
//		for ( ArtifactState aS : artifactStates ) {
//			log.debug("      " + aS.getArtifactId() + ": last modified on " + aS.getArtifactLastModifiedDate().toLocaleString() + "; Version: " + aS.getArtifactVersion() );
//		}
		
		
	}

	public String getValueFromResultSet( CQResultSet cursor , String fName ) {
		
		try {
			for (int i = 1 ; i <= cursor.GetNumberOfColumns() ; i++) {
				if ( cursor.GetColumnLabel(i).equalsIgnoreCase(fName) ) {
					return cursor.GetColumnValue(i);
				}
			}
			// landing here means the name wasn't found.
			log.error("unknown field Name : " + fName);
			return null;
		} catch (CQException e) {
			log.error("cannot get field value for field '" + fName + "'", e);
			throw new CCFRuntimeException("failed value extraction", e);
		}
	}
	
	
	
	public void updateGAWithRecordData( RCQConnection connection , String recID , 
			boolean ignoreConnectoreUserUpdates , Date lastModifedDate , 
			String ccfUserName , GenericArtifact genericArtifact) throws ParseException {
		try {
			CQEntity myRec = connection.getCqSession().GetEntity(connection.getRecType(), recID);
			RCQHistoryHelper myStory = new RCQHistoryHelper(myRec, connection);
			
			String lastModifedBy = myStory.GetLastModifiedUser();
			Date creationDate = myStory.GetCreationDate();
			Date lastRecordModifedDate = myStory.GetLastModfiedDate();
			// we're using the index as version
			String revNumber = Long.toString( myStory.GetLastVersion() ); 
			
			boolean isResync = false;
			boolean ignoreCCFUserUpdate =false;
			
			// identify if we're getting a resync done by our ccf user
			if ( lastModifedBy.equalsIgnoreCase(ccfUserName) && ignoreConnectoreUserUpdates ) {
				if (creationDate.after(lastModifedDate)) {
					log.info(String
							.format("resync is necessary, despite the artifact %s last being updated by the connector user",
									recID));
					log.debug("		artifact created on " + creationDate.toString());
					log.debug("		artfiact lasty modified on " + lastModifedDate.toString());
					isResync = true;
				} else {
					ignoreCCFUserUpdate = true;
				}
			}
			
			GenericArtifactField gaField = null;
			String[] allFields = myRec.GetFieldNames();
			for ( String f : allFields ) {
				// exclude history field
				if ( f.equals(connection.getHistoryFieldName()) ) {
					continue;
				}
					
				// notes fields should be included already.
				String value =  myRec.GetFieldValue(f).GetValue();
				boolean isEmpty = value.isEmpty();
				gaField = genericArtifact.addNewField(f , 
						"mandatoryField" );
				gaField.setFieldValueType( RCQMetaData.getFieldType( myRec.GetFieldType(f) ) );
				gaField.setFieldAction( FieldActionValue.REPLACE );
				gaField.setFieldValue(value);
			}
			
			if ( ignoreCCFUserUpdate ) {
				genericArtifact.setArtifactAction( ArtifactActionValue.IGNORE );
				log.debug("ignoring ccf user updates on record " + recID );
			} else {
				if ( isResync ) {
					genericArtifact.setArtifactAction(ArtifactActionValue.RESYNC);
					log.debug("encountered RESYNC for " + recID );
				}
				
				myStory.addHistoryEntries(genericArtifact, ccfUserName);
			}
			
			genericArtifact.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df.format(lastRecordModifedDate));
			genericArtifact.setSourceArtifactVersion(revNumber);
//			log.debug("Artifact requested: " + recID);
			
			
		} catch (CQException e) {
			String cause = "Could not retrieve record with ID = " + recID + " of type " +  connection.getRecType();
			log.error(cause, e);
			throw new CCFRuntimeException(cause);
		}
		
	}

	private String getActionName (CQEntity record , GenericArtifact ga) {

		String[] recFieldNames;
		String currentAction = "getting cq field names (getActionName)";
		String actionName = null;
		try {
			recFieldNames = record.GetFieldNames();
			for ( String recFieldName : recFieldNames ) { 
				List<GenericArtifactField> gaFields = ga
						.getAllGenericArtifactFieldsWithSameFieldName(recFieldName);
				
				if (gaFields != null
						&& gaFields.get(0).getFieldValueHasChanged()) {
					GenericArtifactField currentField = gaFields.get(0);
					
					
					if ( currentField.getFieldType().equalsIgnoreCase("transitionField") ) {
						String gaFieldValue = (String) currentField.getFieldValue();
						
						CQSession cqs = record.GetSession();
						CQEntityDef currentEntityDef = cqs.GetEntityDef(record.GetEntityDefName());
						
						currentAction = "getting default action info";
						String defaultAction = record.GetDefaultActionName();
						if ( defaultAction != null && !defaultAction.equals("") ) {
							log.debug("Default Action: " + defaultAction);
							log.debug("Default Target State: " + currentEntityDef.GetActionDestStateName(record.GetDefaultActionName()));
						} else {
							log.debug("no default action found for record " + record.GetDisplayName());
						}
						
						String newState = (String) gaFieldValue;
						currentAction = "getting fieldInfo for transition field " + recFieldName;
						CQFieldInfo currentStateInfo = record.GetFieldValue(recFieldName);
						currentAction = "getting current value for transition field " + recFieldName;
						String oldState = currentStateInfo.GetValue();
						log.debug("got newState: " + newState);
						if ( oldState.equals("")) {
							log.debug("clearquest State is empty");
						} else {
							log.debug("clearquest State: " + oldState);
						}

						currentAction = "getting state/transition names";
						String[] legalActions = record.GetLegalActionDefNames();
						log.debug("legal actions/transitions:");
						List<String> actionCandidates = new ArrayList<String>();
						for ( String action : legalActions ) {
							String targetState = currentEntityDef.GetActionDestStateName(action);
							log.debug("		" + action + " --> " + targetState);
							if ( newState.equals(targetState)) {
								actionCandidates.add(action);
							}
						}

						
						if ( ! newState.equals(oldState) ) {
							if ( actionCandidates.size() == 1 ) {
								// nice!
								log.debug("single action '" + actionCandidates.get(0) + "' transitions from state '" + oldState + "' to new state '" + newState + "'");
								actionName = actionCandidates.get(0);
							} else {
								log.warn("found multiple actions from state '" + oldState +
										"' to state '" + newState + "'");
								for ( String act : actionCandidates ) {
									log.warn("	- " + act);
								}
							}
						} else {
							// updates without state changes
							actionName = "modify";
						}
						break;
					} 
				}
			}
		} catch (CQException e) {
			log.error(currentAction);
			throw new CCFRuntimeException(currentAction, e);
		}
		
		if ( actionName != null && !actionName.equals("") ) {
			return actionName;
		} else {
			// rather hospitalize?
			log.warn("unable to determine unique action name, using 'modify' action");
			return "modify";
		}
		
	}
	
	public CQEntity createRecord(GenericArtifact ga, RCQConnection connection) {
		String currentAction = null;
		CQEntity record = null;
		
		try {
			currentAction = "creating new cq entity of type" + connection.getRecType();
			record = connection.getCqSession().BuildEntity(connection.getRecType());
			
			
			updateRecordFields(ga, connection, record);
			
			// validate new values
			validateAndSaveRecord(record);
			
			// update comments

			updateComments(ga, connection, record);
			currentAction = "retrieving history information for new record " + record.GetDisplayName();
			RCQHistoryHelper versionInfo = new RCQHistoryHelper(record, connection);
			
			
			// update new artifact information
			ga.setTargetArtifactVersion(Long.toString(versionInfo.GetLastVersion()));
			ga.setTargetArtifactId(record.GetDisplayName());
			ga.setTargetArtifactLastModifiedDate(GenericArtifactHelper.df.format(versionInfo.GetLastModfiedDate()));
	
		} catch (CQException e) {
			log.error("failed at " + currentAction, e);
			throw new CCFRuntimeException(currentAction);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return record;
	}

	public CQEntity updateRecord(GenericArtifact ga, RCQConnection connection) {

		String currentAction = null;
		String entityDisplayName = null;
		CQEntity record = null;
		try {
			currentAction = "retrieving cq entity " + connection.getRecType() + "::" + ga.getTargetArtifactId();
			record = connection.getCqSession().GetEntity(connection.getRecType(), ga.getTargetArtifactId() );
			
			entityDisplayName = record.GetDisplayName();
			
			RCQHistoryHelper myStory = new RCQHistoryHelper(record, connection);

			if (!AbstractWriter.handleConflicts(myStory.GetLastVersion() , ga) ) {
				return null;
			}
			
			
			// starting edit session
			currentAction = "Starting Edit Session for entity " + entityDisplayName;
			connection.getCqSession().EditEntity(record , getActionName(record, ga) );
			
			updateRecordFields(ga, connection, record);
			
			// validate new values
			validateAndSaveRecord(record);
			
			// update comments
			updateComments(ga, connection, record);
			
			
			// get latest data from database
			record.Reload();
			
			// update last modified and version 
			RCQHistoryHelper versionInfo = new RCQHistoryHelper(record, connection);
			ga.setTargetArtifactLastModifiedDate(GenericArtifactHelper.df.format(versionInfo.GetLastModfiedDate()));
			ga.setTargetArtifactVersion(Long.toString(versionInfo.GetLastVersion()));
	
		} catch (CQException e) {
			log.error("failed at " + currentAction, e);
			throw new CCFRuntimeException(currentAction);
		} catch (ParseException e) {
			String cause = "Could not retrieve history information for ClearQuest ID " 
					+ ga.getTargetArtifactId();
			log.error(cause, e);
			throw new CCFRuntimeException(cause);
		}
		
		return record;
	}

	
	private void updateComments(GenericArtifact ga,
			RCQConnection connection, CQEntity record) {
	
		String currentAction = null;
		currentAction = "getting fieldnames for entity "
				+ ga.getTargetArtifactId();
		String[] recFields;

		try {
			recFields = record.GetFieldNames();
			for (String cqFieldName : recFields) {
				
				// do not write to the history field
				if (cqFieldName.equals(connection.getHistoryFieldName())) {
					continue;
				}
				
				List<GenericArtifactField> gaFields = ga
						.getAllGenericArtifactFieldsWithSameFieldName(cqFieldName);
				
				
				if ( gaFields != null 
						&& gaFields.get(0).getFieldType().equalsIgnoreCase("notesField")
						&& gaFields.get(0).getFieldValueHasChanged() ) {
					
					log.debug("adding " + gaFields.size() + " notes using field '" + cqFieldName + "'");
					
					for ( GenericArtifactField currentField : gaFields ) {
						
						String notesEntryText = (String) currentField.getFieldValue();
						
						// start edit
						connection.getCqSession().EditEntity(record , "modify" );
						
						// set comment text value
						currentAction = "adding notes entry to field " + cqFieldName;
						String setValueResult = record.SetFieldValue(cqFieldName,
								notesEntryText);

						reportActionResult(
								setValueResult,
								"could not add notes entry for record "
										+ record.GetDisplayName());
						
						// validate and save
						validateAndSaveRecord(record);
					}
					
				}
			}
		} catch (CQException e) {
			log.error("failed at " + currentAction, e);
			throw new CCFRuntimeException(currentAction, e);
		}
		
	}
	
	
	
	private void updateRecordFields(GenericArtifact ga,
			RCQConnection connection, CQEntity record) {
		String currentAction = null;
		currentAction = "getting fieldnames for entity "
				+ ga.getTargetArtifactId();
		String[] recFields;
		try {
			recFields = record.GetFieldNames();
			for (String cqFieldName : recFields) {
				
				// do not write to the history field
				if (cqFieldName.equals(connection.getHistoryFieldName())) {
					continue;
				}

				List<GenericArtifactField> gaFields = ga
						.getAllGenericArtifactFieldsWithSameFieldName(cqFieldName);

//				log.debug(">> clearquest field " + cqFieldName);

				if (gaFields != null
						&& gaFields.get(0).getFieldValueHasChanged()) {

					GenericArtifactField currentField = gaFields.get(0);
					Object gaFieldValue = currentField.getFieldValue();


					// skip status and notes fields
					if ( currentField.getFieldType().equalsIgnoreCase("transitionField") || currentField.getFieldType().equalsIgnoreCase("notesField") ) {
						continue;
					}

					log.debug(">>>>>>>> shipping field " + currentField.getFieldName() + " of type " + currentField.getFieldType());
										
					// if it's a date field, set the timezone
					if (RCQMetaData
							.isDateType(record.GetFieldType(cqFieldName))) {
						if (DateUtil.isAbsoluteDateInTimezone(
								(Date) gaFieldValue,
								ga.getSourceSystemTimezone())) {
							gaFieldValue = DateUtil.convertToGMTAbsoluteDate(
									(Date) gaFieldValue,
									ga.getSourceSystemTimezone());
						}
					}
					
					currentAction = "setting " + cqFieldName + " to value '" + (String) gaFieldValue + "'";
					String setValueResult = record.SetFieldValue(cqFieldName,
							(String) gaFieldValue);

					reportActionResult(
							setValueResult,
							"could not set field value for entity "
									+ record.GetDisplayName());

					// multi-select field - the previous SetFieldValue
					// de-selected all values and set the first one already
					if (gaFields.size() > 1) {
						for (int i = 1; i < gaFields.size(); i++) {
							String newValue = (String) gaFields.get(i)
									.getFieldValue();
							String addValueResult = record.AddFieldValue(
									cqFieldName, newValue);
							reportActionResult(addValueResult,
									"could not add multi-select FieldValue '"
											+ newValue + "' to entity "
											+ record.GetDisplayName());
						}
					}
					
					// update comments is done one level up as it needs separate edit sessions per comment.
					
					
					// TODO relations&dependencies
					
				}
			} // end for
		} catch (CQException e) {
			log.error("failed at " + currentAction, e);
			throw new CCFRuntimeException(currentAction, e);
		}

		// TODO update dependencies in CQ
	}

	
	private void logStrings(String title, String[] list) {
		log.debug("--- " + title + " ---");
		int count = 1;
		for ( String s : list ) { 
			log.debug(count++ + " " + s );
		}
		log.debug("	total " + list.length);
	}
	
	public void reportActionResult(String actionResult , String causePrefix) {
		if ( ! actionResult.equals("") ) {
			log.error(causePrefix + ": " + actionResult);
			throw new CCFRuntimeException(causePrefix + ": " + actionResult);
		}
	}

	
	
	private void validateAndSaveRecord(CQEntity record) {	
		
		String entityDisplayName = null;
		String currentAction = null;
		try {
			entityDisplayName = record.GetDisplayName();
			currentAction = "validating entity " + entityDisplayName;
			String validationResult = record.Validate();
			
			if ( validationResult.equals("") ) {
				currentAction = "committing changes to entity " + entityDisplayName;
				String commitResult = record.Commit();
				if ( ! commitResult.equals("") ) {
					// failed commit, rolling back
					String cause = "commit of changed " + entityDisplayName + " failed: " + commitResult ;
					log.error(cause);
					// cancel changes
					currentAction = "reverting changes to entity " + entityDisplayName + " (failed commit)";
					record.Revert();
					throw new CCFRuntimeException(cause + " [changes not saved]");
				}
			} else {
				String cause = "validation of " + entityDisplayName + " failed: " + validationResult ;
				log.error(cause);
				// cancel changes
				currentAction = "reverting changes to entity " + entityDisplayName;
				record.Revert();
				throw new CCFRuntimeException(cause + " [changes not saved]");
			}
		} catch (CQException e) {
			log.error("failed at " + currentAction, e);
			throw new CCFRuntimeException(currentAction);
		}

	}
	
}
