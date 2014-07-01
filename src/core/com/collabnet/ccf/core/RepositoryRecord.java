/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.core;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;

import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.utils.DummyArtifactSoapDO;

public class RepositoryRecord {
    private String                     repositoryId;
    private Document                   newSyncInfo                      = null;
    private Document                   syncInfo                         = null;
    private ArrayList<GenericArtifact> artifactsToBeShippedList         = null;
    private ArrayList<ArtifactState>   artifactsToBeReadList            = null;
    private boolean                    newSyncInfoReceived              = true;

    private boolean                    readyForNewSynchInfo             = true;
    private boolean                    staleRecordUpdateAlreadyReceived = true;

    private List<DummyArtifactSoapDO>  dummyArtifactSoapToBeShippedList = null;

    public RepositoryRecord(String repositoryId, Document syncInfo) {
        this.repositoryId = repositoryId;
        this.syncInfo = syncInfo;
        this.newSyncInfo = syncInfo;
        artifactsToBeShippedList = new ArrayList<GenericArtifact>();
        artifactsToBeReadList = new ArrayList<ArtifactState>();
        this.dummyArtifactSoapToBeShippedList = new ArrayList<DummyArtifactSoapDO>();
    }

    public ArrayList<ArtifactState> getArtifactsToBeReadList() {
        return artifactsToBeReadList;
    }

    public ArrayList<GenericArtifact> getArtifactsToBeShippedList() {
        return artifactsToBeShippedList;
    }

    public List<DummyArtifactSoapDO> getDummyArtifactSoapToBeShippedList() {
        return dummyArtifactSoapToBeShippedList;
    }

    /**
     * @return the newSyncInfo
     */
    public Document getNewSyncInfo() {
        return newSyncInfo;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public Document getSyncInfo() {
        return syncInfo;
    }

    public boolean isNewSyncInfoReceived() {
        return newSyncInfoReceived;
    }

    /**
     * Called by streaming algorithm to tell that it is not ready for new synch
     * info
     */
    public void notReadyForNewSynchInfo() {
        readyForNewSynchInfo = false;
        staleRecordUpdateAlreadyReceived = false;
    }

    /**
     * Called by streaming algorithm to tell that it is ready for new synch info
     */
    public void readyForNewSynchInfo() {
        readyForNewSynchInfo = true;
    }

    public void setArtifactsToBeReadList(
            ArrayList<ArtifactState> artifactsToBeReadList) {
        this.artifactsToBeReadList = artifactsToBeReadList;
    }

    public void setArtifactsToBeShippedList(
            ArrayList<GenericArtifact> artifactsToBeShippedList) {
        this.artifactsToBeShippedList = artifactsToBeShippedList;
    }

    public void setDummyArtifactSoapToBeShippedList(
            List<DummyArtifactSoapDO> dummyArtifactSoapToBeShippedList) {
        this.dummyArtifactSoapToBeShippedList = dummyArtifactSoapToBeShippedList;
    }

    /**
     * This method is called every time, a new synchronization status record for
     * this repository arrives. As long as the buffers are not flushed, it will
     * not be used by the streaming algorithm
     * 
     * @param newSyncInfo
     *            the newSyncInfo to set
     */
    public void setNewSyncInfo(Document newSyncInfo) {
        if (readyForNewSynchInfo) {
            if (staleRecordUpdateAlreadyReceived) {
                this.newSyncInfo = newSyncInfo;
                newSyncInfoReceived = true;
            } else {
                staleRecordUpdateAlreadyReceived = true;
            }
        }
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public void setSyncInfo(Document syncInfo) {
        this.syncInfo = syncInfo;
    }

    /**
     * This method should be called whenever all buffers of the repository are
     * flushed to take the new sync info document
     */
    public void switchToNewSyncInfo() {
        this.syncInfo = this.newSyncInfo;
        newSyncInfoReceived = false;
    }
}
