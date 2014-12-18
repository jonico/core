package com.collabnet.ccf.ist;

import org.dom4j.Document;

import com.collabnet.ccf.core.AbstractWriter;

public class ISTWriter extends AbstractWriter<ISTWriter> {

    private String serverUrl = null;
    private String userName  = null;
    private String password  = null;

    @Override
    public Document createArtifact(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document[] createAttachment(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document createDependency(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document deleteArtifact(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document[] deleteAttachment(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document deleteDependency(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPassword() {
        return password;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    @Override
    public Document updateArtifact(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document updateAttachment(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Document updateDependency(Document gaDocument) {
        // TODO Auto-generated method stub
        return null;
    }

}
