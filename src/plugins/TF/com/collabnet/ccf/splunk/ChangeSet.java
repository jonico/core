package com.collabnet.ccf.splunk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.collabnet.teamforge.api.main.AuditHistoryRow;

public class ChangeSet {

    private Date                        lastModifieDate = null;

    private String                      comment         = null;

    private List<AuditHistoryRow> changeList      = new ArrayList<AuditHistoryRow>();

    public List<AuditHistoryRow> getChangeList() {
        return changeList;
    }

    public String getComment() {
        return comment;
    }

    public Date getLastModifieDate() {
        return lastModifieDate;
    }

    public void setChangeList(List<AuditHistoryRow> changeList) {
        this.changeList = changeList;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLastModifieDate(Date lastModifieDate) {
        this.lastModifieDate = lastModifieDate;
    }

}
