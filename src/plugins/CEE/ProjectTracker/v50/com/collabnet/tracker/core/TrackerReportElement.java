/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

/**
 * 
 */
package com.collabnet.tracker.core;

import com.collabnet.tracker.core.util.TrackerUtil;

/**
 * Some known and required elements of a PT artifact.
 * 
 * @author Shawn Minto
 * 
 */
public enum TrackerReportElement {

	// GLOBAL NAMESPACE
	CREATEDON("Created On:", "createdOn", true, true, true), CREATEDBY("Created By:", "createdBy", false, true, true), MODIFIEDON(
			"Modified On:", "modifiedOn", true, true, true), MODIFIEDBY("Modified By:", "modifiedBy", false, true, true), ID(
			"ID:", "id", true, true, true), PROJECT("Project:", "project",true,true,true),

	// comments
	COMMENT_TEXT("Comment Text", "comment_text", true, true, true), COMMENT_DATE("Comment Date", "comment_date", true,
			true, true), COMMENT_AUTHOR("Comment Author", "comment_author", true, true, true),

	// attachments
	ATTACHMENT_ID("Attachment Id", "attachment_id", true, true, true), ATTACHMENT_LOCATION("Attachment Location",
			"attachment_location", true, true, true), ATTACHMENT_NAME("Attachment Name", "attachment_name", true, true,
			true), MIME_TYPE("Mime Type", "mime_type", true, true, true), IS_FILE("Is File", "is_file", true, true,
			true),

	// MYLYN SPECIFIC
	NEW_COMMENT("New Comment", "new_comment", true, true, true), REASON_FOR_CHANGE("Reason For Change",
			"reason_for_change", true, true, true),

	// TODO we could add more permutations of what these could be here as well
	// and map them in the TrackerAttributeFactory to give users more
	// flexibility in the support
	// REQUIRED BUT NOT STANDARD
	SUMMARY("Summary:", "summary", true, false, false), DESCRIPTION("Description:", "description", true, false, false), PRIORITY(
			"Priority:", "priority", false, true, false), STATUS("Status:", "status", true, true, false), ASSIGNED_TO(
			"Assigned To:", "assigned_to", false, false, false), CARBON_COPY("CC:", "carbon_copy", true, false, false);

	private final boolean hidden;

	private final boolean isReadOnly;

	private final boolean isGlobal;

	private final String tagName;

	private final String prettyName;

	TrackerReportElement(String prettyName, String fieldName, boolean hidden, boolean readonly, boolean isGeneric) {
		this.prettyName = prettyName;
		this.tagName = fieldName;
		this.hidden = hidden;
		this.isReadOnly = readonly;
		this.isGlobal = isGeneric;
	}

	public String getTagName() {
		return tagName;
	}

	public String getKeyString() {
		return TrackerUtil.getKey(getNamespace(), getTagName());
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public boolean isGlobal() {
		return isGlobal;
	}

	public String toString() {
		return prettyName;
	}

	public String getNamespace() {
		return TrackerWebServicesClient.DEFAULT_NAMESPACE;
	}

}
