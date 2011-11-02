package com.collabnet.ccf.rcq;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rational.clearquest.cqjni.CQEntity;
import com.rational.clearquest.cqjni.CQException;
import com.rational.clearquest.cqjni.CQHistories;
import com.rational.clearquest.cqjni.CQHistory;
import com.rational.clearquest.cqjni.CQHistoryField;
import com.rational.clearquest.cqjni.CQHistoryFields;

public class RCQHistoryHelper {

	private static String DELIMIT = "\t";
	private Date lastDate = null;
	private Date firstDate = null;
	private long version = -1;
	private String lastUser = null;
	private String firstUser = null;
	
	public RCQHistoryHelper( CQEntity record , RCQConnection connection ) throws CQException, ParseException {

		CQHistoryFields book = record.GetHistoryFields();
		CQHistoryField story = book.ItemByName( connection.getHistoryFieldName() );
		CQHistories chapter = story.GetHistories();
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
