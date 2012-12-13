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

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.hospital.CCFExceptionToOrderedMapConvertor;

import com.rational.clearquest.cqjni.CQEntity;
import com.rational.clearquest.cqjni.CQException;
import com.rational.clearquest.cqjni.CQFieldInfos;
import com.rational.clearquest.cqjni.CQFilterNode;
import com.rational.clearquest.cqjni.CQHistories;
import com.rational.clearquest.cqjni.CQHistory;
import com.rational.clearquest.cqjni.CQHistoryField;
import com.rational.clearquest.cqjni.CQHistoryFields;
import com.rational.clearquest.cqjni.CQQueryDef;
import com.rational.clearquest.cqjni.CQQueryFilterNode;
import com.rational.clearquest.cqjni.CQResultSet;

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
			log.error("problem adding fields to result set." , e);
			throw new CCFRuntimeException("problem building a query for clearquest" , e);
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
			log.error("problem running the query" , e);
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
		log.info("fetched " + (count - 1) + " records from clearquest, " + updates + " of them are updated after " + lastModifiedDate.toString());
//		log.debug("-----> Fetch Results:");
		
//		for ( ArtifactState aS : artifactStates ) {
//			log.debug(aS.getArtifactId() + ": last modified on " + aS.getArtifactLastModifiedDate().toLocaleString() + "; Verion: " + aS.getArtifactVersion() );
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
	
	
	
	public void getRecordData( RCQConnection connection , String recID , 
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
			boolean isIgnore =false;
			
			// identify if we|re getting a resync done by our ccf user
			if ( lastModifedBy.equalsIgnoreCase(ccfUserName) && ignoreConnectoreUserUpdates ) {
				if (creationDate.after(lastModifedDate)) {
					log.info(String
							.format("resync is necessary, despite the artifact %s last being updated by the connector user",
									recID));				
					isResync = true;
				} else {
//					log.info(String
//							.format("artifact %s is a connector update, ignoring it.",
//									recID));
					isIgnore = true;
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
			
			if ( isIgnore ) {
				genericArtifact.setArtifactAction( ArtifactActionValue.IGNORE );
				log.debug("encountered IGNORE for " + recID );
			} else {
				if ( isResync ) {
					genericArtifact.setArtifactAction(ArtifactActionValue.RESYNC);
					log.debug("encountered RESYNC for " + recID );
				}
				
				// TODO: fetching history
				myStory.addHistoryEntries(genericArtifact);
			}
			
			genericArtifact.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df.format(lastRecordModifedDate));
			genericArtifact.setSourceArtifactVersion(revNumber);
			log.debug("Artifact requested: " + recID);
			
			
		} catch (CQException e) {
			String cause = "Could not retrieve record with ID = " + recID + " of type " +  connection.getRecType();
			log.error(cause, e);
			throw new CCFRuntimeException(cause);
		}
		
	}
	// workitem is a tfs type, here we need the generic CQ object type
	public CQEntity createRecord(GenericArtifact ga, RCQConnection connection) {
		// TODO Reader: create Record in cq
		return null;
	}

	public CQEntity updateRecord(GenericArtifact ga, RCQConnection connection) {
		// TODO Reader: update Record in cq
		return null;
	}

}
