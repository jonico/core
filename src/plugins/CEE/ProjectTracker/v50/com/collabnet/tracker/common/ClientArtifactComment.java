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

package com.collabnet.tracker.common;

/**
 * This represents an artifact in the artifact xml document Used by the
 * ClientArtifactListXMLHelper
 * 
 * @author Shawn Minto
 * 
 */
public class ClientArtifactComment {

    private String commentId;
    private String commentDate;
    private String commentText;
    private String commenter;

    public ClientArtifactComment(String commentId, String commentDate,
            String commentText, String commenter) {
        this.commentId = commentId;
        this.commentDate = commentDate;
        this.commentText = commentText;
        this.commenter = commenter;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public String getCommenter() {
        return commenter;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getCommentText() {
        return commentText;
    }

}
