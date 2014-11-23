package com.collabnet.ccf.ist;

import java.util.List;
import java.util.Set;

import org.dom4j.Document;

import com.collabnet.ccf.core.AbstractReader;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.ga.GenericArtifact;

public class ISTReader extends AbstractReader<ISTReader> {

    @Override
    public List<GenericArtifact> getArtifactAttachments(Document syncInfo,
            GenericArtifact artifactData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GenericArtifact getArtifactData(Document syncInfo, String artifactId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<GenericArtifact> getArtifactDependencies(Document syncInfo,
            String artifactId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ArtifactState> getChangedArtifacts(Document syncInfo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ArtifactState> getChangedArtifactsToForceSync(
            Set<String> artifactsToForce, Document SyncInfo) {
        // TODO Auto-generated method stub
        return null;
    }

}
