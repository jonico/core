package com.collabnet.ccf.rcq;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.rational.clearquest.cqjni.CQEntity;
import com.rational.clearquest.cqjni.CQException;
import com.rational.clearquest.cqjni.CQHistories;
import com.rational.clearquest.cqjni.CQHistory;
import com.rational.clearquest.cqjni.CQHistoryField;
import com.rational.clearquest.cqjni.CQHistoryFields;

public class RCQHistoryHelper {

	private static final Log log = LogFactory.getLog(RCQHistoryHelper.class);
	private static final String nl = System.getProperty("line.separator");
	private static String DELIMIT = "\t";
	private Date lastDate = null;
	private Date firstDate = null;
	private long version = -1;
	private String lastUser = null;
	private String firstUser = null;
	private String historyFieldName = null;
	private CQHistories chapter = null;
	
	
	public RCQHistoryHelper( CQEntity record , RCQConnection connection ) throws CQException, ParseException {

		historyFieldName = connection.getHistoryFieldName();
		
		CQHistoryFields book = record.GetHistoryFields();
		CQHistoryField story = book.ItemByName( historyFieldName );
		chapter = story.GetHistories();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");

		this.version = chapter.Count();
		CQHistory lastPara = chapter.Item(this.version - 1);
		CQHistory firstPara = chapter.Item(0);

		String[] lastParts = lastPara.GetValue().split(DELIMIT);
		String[] firstParts = firstPara.GetValue().split(DELIMIT);
		
		this.lastDate = (Date)formatter.parse( lastParts[1] );
		this.firstDate = (Date)formatter.parse(firstParts[1]);
		
		this.firstUser = firstParts[2];
		this.lastUser = lastParts[2];
		
	}
	
	public void addHistoryEntries( GenericArtifact ga ) {
		try {
			// travel from youngest to oldest
			for ( long h = chapter.Count() - 1 ; h >= 0 ; h-- ) {
				CQHistory hEntry = chapter.Item(h);
				// timestamp, dateFormtted, user name, action name, old state, new state
				String[] hParts = hEntry.GetValue().split(DELIMIT);
				String history = String.format(
					"History log: on %1$s, %2$s performed the action: %3$s" + nl + "The state changed from %4$s to %5$s" ,
					hParts[1], hParts[2], hParts[3] , hParts[4] , hParts[5]);
				// note: the mapping to TF comment field has to happen in the XSL
				GenericArtifactField gaField = ga.addNewField(historyFieldName, "mandatoryField" );
				gaField.setFieldValueType(FieldValueTypeValue.HTMLSTRING);
				gaField.setFieldAction(FieldActionValue.APPEND);
				gaField.setFieldValue(history);
			}
		} catch (CQException e) {
			log.error("Issue retrieving history entries", e);
		}
	}
	
	public Date GetCreationDate() {
		return this.firstDate;
	}
	public String GetCreateUser() {
		return this.firstUser;
	}
	
	public Date GetLastModfiedDate() {
		return this.lastDate;
	}
	
	public String GetLastModifiedUser() {
		return this.lastUser;
	}
	
	public long GetLastVersion() {
		return version;
	}
	
}
