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
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.hospital.CCFExceptionToOrderedMapConvertor;

import com.rational.clearquest.cqjni.CQEntity;
import com.rational.clearquest.cqjni.CQException;
import com.rational.clearquest.cqjni.CQFieldInfos;
import com.rational.clearquest.cqjni.CQHistories;
import com.rational.clearquest.cqjni.CQHistory;
import com.rational.clearquest.cqjni.CQHistoryField;
import com.rational.clearquest.cqjni.CQHistoryFields;
import com.rational.clearquest.cqjni.CQQueryDef;
import com.rational.clearquest.cqjni.CQResultSet;

public class RCQHandler {

	public String WI_QUERY = "Select [Id] From WorkItems Where [Work Item Type] = '?' Order By [Changed Date] Desc";

	private static final Log log = LogFactory.getLog(RCQHandler.class);
	
	private enum HistoryType {
		ID , DATE , USER , ACTION , OLDSTATE , NEWSTATE
	}
	
	private List<String> atrList;
	
	public void getChangedRecords(RCQConnection connection,
			Date lastModifiedDate, 
			String lastSynchronizedVersion,
			String lastSynchedArtifactId,
			ArrayList<ArtifactState> artifactStates, 
			boolean ignoreConnectorUserUpdates) throws ParseException {

		CQQueryDef myQD = null;
		
		//build not exec query.
		log.debug("building query for record type '" + connection.getRecType() + "'");
		try {
			myQD = connection.getCqs().BuildQuery(connection.getRecType());
		} catch (CQException e) {
			log.error( "Problem building the query" , e);
			throw new CCFRuntimeException("problem creating a clearquest query" , e);
		}
		
		// add some standard built-in fields
		try {
			// here we only need the ids, all other necessary info will be pulled via getEntity()
			log.debug("Adding result fields to query");
			myQD.BuildField("id");

		} catch (CQException e) {
			log.error("problem adding fields to result set." , e);
			throw new CCFRuntimeException("problem building a query for clearquest" , e);
		}
		
		// are we in the first run?
		if ( lastSynchedArtifactId.equalsIgnoreCase("0") && lastSynchronizedVersion.equalsIgnoreCase("0") ) {
			// no special query to define
			
		} else {
			// use the last modified date - 10 seconds 
			// TODO create filter for date last modified
		}
		
		
		// finally, execute the query.
		log.debug("executing query....");
		CQResultSet results = null;
		long numResults = 0;
		try {
			results = connection.getCqs().BuildResultSet(myQD);
			numResults =  results.ExecuteAndCountRecords();
			log.info("query returned " + numResults + " records");
			
		} catch (CQException e) {
			log.error("problem running the query" , e);
			throw new CCFRuntimeException("problem querying clearquest" , e);
		}
		
		// build the result set as array of Artifact State items
		
		log.debug("parsing results into artifactStates");
		int count = 1;
		try {
			long status = results.MoveNext();
			while ( status == 1 ) {
				ArtifactState artState = new ArtifactState();
				artState.setArtifactId(getValueFromResultSet( results , "id"));
				
				CQEntity record = connection.getCqs().GetEntity( connection.getRecType() ,  artState.getArtifactId() );
				Date lastMod = (Date)getLastHistoryInformation(HistoryType.DATE , record, connection);
				artState.setArtifactLastModifiedDate(lastMod);
				artState.setArtifactVersion(lastMod.getTime());
				
				artifactStates.add(artState);
				
				// next round
				count++;
				status = results.MoveNext();
			}
		} catch (CQException e) {
			log.error("Problem getting data from resultset");
			throw new CCFRuntimeException("issue getting data", e);
		}
		log.info("query fetched " + count + " records from clearquest");
		log.debug("-----> Fetch Results:");
		
		for ( ArtifactState aS : artifactStates ) {
			log.debug(aS.getArtifactId() + ": last modified on " + aS.getArtifactLastModifiedDate().toLocaleString() + "; Verion: " + aS.getArtifactVersion() );
		}
		
		
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
	
	private Object getFirstHistoryInformation( HistoryType type, CQEntity record , RCQConnection connection ) throws CQException, ParseException {
		
		CQHistoryFields book = record.GetHistoryFields();
		CQHistoryField story = book.ItemByName( connection.getHistoryFieldName() );
		CQHistories chapter = story.GetHistories();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		//FIXME ... in 89 years, this routine will break
		Date firstModDate = (Date)formatter.parse("2100-01-01 23:59:59");
		Boolean foundIt = false;
		String firstUser = null;

		// store info from youngest record
		for ( int h = 1 ; h < chapter.Count() ; h++ ) {
			CQHistory paragraph = chapter.Item(h);
//			log.debug("paragraph: " + paragraph.GetValue() );
			String[] parts = paragraph.GetValue().split("\t");
			String sDate = parts[1];
			Date date = (Date)formatter.parse(sDate);
			if ( firstModDate.compareTo(date) > 0 ) {
				firstModDate = date;
				firstUser = parts[2]; 
				foundIt = true;
			}
		}
		
		switch (type) {
		case DATE :
			return foundIt ? firstModDate : null;
		case USER :
			return foundIt ? firstUser : null;
		}
		
		// supposedly never executed:-)
		return null;
		

	}
	
	private Object getLastHistoryInformation( HistoryType type, CQEntity record , RCQConnection connection ) throws CQException, ParseException {
	
		CQHistoryFields book = record.GetHistoryFields();
		CQHistoryField story = book.ItemByName( connection.getHistoryFieldName() );
		CQHistories chapter = story.GetHistories();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		Date lastModDate = (Date)formatter.parse("1960-01-01 23:59:59");
		Boolean foundIt = false;
		String lastUser = null;

		// store info from youngest record
		for ( int h = 1 ; h < chapter.Count() ; h++ ) {
			CQHistory paragraph = chapter.Item(h);
//			log.debug("paragraph: " + paragraph.GetValue() );
			String[] parts = paragraph.GetValue().split("\t");
			String sDate = parts[1];
			Date date = (Date)formatter.parse(sDate);
			if ( lastModDate.compareTo(date) < 0 ) {
				lastModDate = date;
				lastUser = parts[2]; 
				foundIt = true;
			}
		}
		
		switch (type) {
		case DATE :
			return foundIt ? lastModDate : null;
		case USER :
			return foundIt ? lastUser : null;
		}
		
		// supposedly never executed:-)
		return null;
		

	}
	
	
	public void getRecordData( RCQConnection connection , String recID , boolean ignoreConnectoreUserUpdates , String ccfUserName , GenericArtifact ga) {
		try {
			CQEntity myRec = connection.getCqs().GetEntity(connection.getRecType(), recID);
			String lastModifedBy = (String)getLastHistoryInformation(HistoryType.USER, myRec, connection);
			Date creationDate = (Date)getFirstHistoryInformation(HistoryType.DATE, myRec, connection);
			Date lastModifedDate = (Date)getLastHistoryInformation(HistoryType.DATE, myRec, connection);
			String revNumber = Long.toString( lastModifedDate.getTime() ); 
			
			boolean isResync = false;
			boolean isIgnore =false;
			
			// identify if we|re getting a resync done by our ccf user
			if ( lastModifedBy.equalsIgnoreCase(ccfUserName) && ignoreConnectoreUserUpdates ) {
				if (creationDate.after(lastModifedDate)) {
						// as we fetch BOTH dates from the same pool of histories, 
						// creationDate will be always BEFORE or EQUAL lastModfiedDate
						log.debug("I SHOULD NOT BE HERE");
	//					log.info(String
	//						.format("resync is necessary, despite the artifact %s last being updated by the connector user",
	//								artifactId));				
						isResync = true;
					} else {
						log.info(String
								.format("artifact %s is a connector update, ignore it.",
										recID));
						isIgnore = true;
					}
			}
			
			// TODO iterate over all field values
			// for now, only use title, description (defined in getAtrList() )
			for ( String f : getAtrList()) {
				String value =  myRec.GetFieldValue(f).GetValue();
				GenericArtifactField gaField = ga.addNewField(f , 
						"mandatoryField" );
				gaField.setFieldValueType( RCQMetaData.getFieldType( myRec.GetFieldType(f) ) );
				gaField.setFieldAction( FieldActionValue.REPLACE );
				gaField.setFieldValue(value);
			}

			if ( isIgnore ) {
				ga.setArtifactAction( ArtifactActionValue.IGNORE );
			} else {
				if ( isResync ) {
					// we're not getting here...
					ga.setArtifactAction(ArtifactActionValue.RESYNC);
				}
				
				// TODO: fetch the comments 
			}
			
			ga.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df.format(lastModifedDate));
			ga.setSourceArtifactVersion(revNumber);

			
		} catch (CQException e) {
			String cause = "Could not retrieve record with ID = " + recID + " of type " +  connection.getRecType();
			log.error(cause, e);
			throw new CCFRuntimeException(cause);
		} catch (ParseException e) {
			String cause = "Could not get last modifying user for ID " + recID + " of type " +  connection.getRecType();
			log.error(cause, e);
			throw new CCFRuntimeException(cause);
		}
		
	}
	
	// workitem is a tfs type, here we need the generic CQ object type
	public CQEntity createRecord(GenericArtifact ga, RCQConnection connection) {
		// TODO Auto-generated method stub
		return null;
	}

	public CQEntity updateRecord(GenericArtifact ga, RCQConnection connection) {
		// TODO Auto-generated method stub
		return null;
	}


	private List<String> getAtrList() {
		List<String> l = new ArrayList<String>();
		l.add("id");
		l.add("Description");
		l.add("Headline");
		return l;
	}
	
}
