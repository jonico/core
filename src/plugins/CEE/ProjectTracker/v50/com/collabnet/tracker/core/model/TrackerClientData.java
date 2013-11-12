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

package com.collabnet.tracker.core.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The data for each artifact type in the repository. This information is used
 * to determine the valid attribute values.
 * 
 * This data is serialized by the client manager
 * 
 * @author Shawn Minto
 * 
 */
public class TrackerClientData implements Serializable {

    /**
	 * 
	 */
    private static final long                serialVersionUID = 1L;
    private Map<String, TrackerArtifactType> trackerArtifactTypes;

    public TrackerClientData() {
        trackerArtifactTypes = new HashMap<String, TrackerArtifactType>();
    }

    public void addArtifactType(TrackerArtifactType trackerArtifactType) {
        trackerArtifactTypes.put(trackerArtifactType.getKey(),
                trackerArtifactType);
    }

    public void clear() {
        trackerArtifactTypes.clear();
    }

    public TrackerArtifactType getArtifactTypeFromKey(String key) {
        return trackerArtifactTypes.get(key);
    }

    /**
     * key is better, but if all you have is the name...
     * 
     * @param name
     * @return
     */
    public TrackerArtifactType getArtifactTypeFromName(String name) {
        TrackerArtifactType outType = null;
        for (TrackerArtifactType aType : trackerArtifactTypes.values()) {
            if (aType.getDisplayName().equals(name)) {
                outType = aType;
                break;
            }
        }
        return outType;
    }

    public Collection<TrackerArtifactType> getArtifactTypes() {
        return trackerArtifactTypes.values();
    }

}
