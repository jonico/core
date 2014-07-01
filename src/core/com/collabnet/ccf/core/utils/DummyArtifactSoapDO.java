package com.collabnet.ccf.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.dom4j.Document;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.teamforge.api.tracker.ArtifactDO;

public class DummyArtifactSoapDO {

    private String     type             = "Artifact";

    private String     operation        = null;

    private String     projectIdString  = null;

    private Date       lastModifiedDate = null;

    private String     lastVersion      = null;

    private Document   genericArtifact  = null;

    private ArtifactDO originalData     = null;

    private ArtifactDO updatedData      = null;

    public DummyArtifactSoapDO() {

    }

    public DummyArtifactSoapDO(ArtifactDO orignalData, ArtifactDO updatedData) {
        this.originalData = orignalData;
        this.updatedData = updatedData;
    }

    public Document getGenericArtifact() {
        return genericArtifact;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public String getOperation() {
        return operation;
    }

    public ArtifactDO getOriginalData() {
        return originalData;
    }

    public String getProjectIdString() {
        return projectIdString;
    }

    public String getType() {
        return type;
    }

    public ArtifactDO getUpdatedData() {
        return updatedData;
    }

    public void setGenericArtifact(GenericArtifact genericArtifact) {
        try {
            this.genericArtifact = GenericArtifactHelper
                    .createGenericArtifactXMLDocument(genericArtifact);
        } catch (GenericArtifactParsingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setOriginalData(ArtifactDO originalData) {
        this.originalData = originalData;
    }

    public void setProjectIdString(String projectIdString) {
        this.projectIdString = projectIdString;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUpdatedData(ArtifactDO updatedData) {
        addVal(updatedData);
        this.updatedData = updatedData;
    }

    @Override
    public String toString() {
        return "DummyArtifactSoapDO [type=" + type + ", operation=" + operation
                + ", projectIdString=" + projectIdString + ", originalData="
                + originalData + ", updatedData=" + updatedData + "]";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private void addVal(ArtifactDO updatedData2) {
        setLastModifiedDate(updatedData2.getLastModifiedDate());
        setLastVersion(String.valueOf(updatedData2.getVersion())); //Hope this comes with incremental value
        //        setProjectIdString(updatedData2.);

    }

    private String iso8601Date(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        //        DateFormat df = isElasticSearch ? new SimpleDateFormat(
        //                "yyyy-MM-dd'T'HH:mm:ss") : new SimpleDateFormat(
        //                "yyyy-MM-dd'T'HH:mm:ss:SSSZ");
        String osName = System.getProperty("os.name");
        SimpleDateFormat format1, format2;
        if (osName.startsWith("Win")) {
            format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH.mm.ss");
            format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");
        } else {
            format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ");

        }
        DateFormat df = true ? format1 : format2;
        df.setTimeZone(tz);
        return df.format(date);
    }

}
