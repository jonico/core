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

package com.collabnet.ccf.pi.qc.v90.api.dcom;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.AttachmentUploadStillInProgressException;
import com.collabnet.ccf.pi.qc.v90.api.DefectAlreadyLockedException;
import com.collabnet.ccf.pi.qc.v90.api.IAttachment;
import com.collabnet.ccf.pi.qc.v90.api.IAttachmentFactory;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.IFactoryList;
import com.collabnet.ccf.pi.qc.v90.api.IFilter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.DateUtilities;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Bug extends ActiveXComponent implements IBug {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger(Bug.class);
	public static ConcurrentHashMap<String, Integer> attachmentRetryCount = new ConcurrentHashMap<String, Integer>();
	
	private boolean alwaysReloadAttachmentSize = false;

	public Bug(Dispatch arg0) {
		super(arg0);
	}

	public String getStatus() {
		return getPropertyAsString("Status");
	}

	public void setStatus(String status) {
		setProperty("Status", status);
	}

	public String getId() {
		return Integer.toString(getPropertyAsInt("ID"));
	}

	public String getSummary() {
		return getPropertyAsString("Summary");
	}

	public String getAssignedTo() {
		return getPropertyAsString("AssignedTo");
	}

	public void setAssignedTo(String assignedTo) {
		setProperty("AssignedTo", assignedTo);
	}

	public String getDetectedBy() {
		// getFieldAsString("BG_DETECTED_BY")
		return getPropertyAsString("DetectedBy");
	}

	public void setDetectedBy(String detectedBy) {
		setProperty("DetectedBy", detectedBy);
	}

	public String getFieldAsString(String field) {
		Variant res = Dispatch.call(this, "Field", field);
		if (res.isNull()) {
			return null;
		} else if (res.getvt() == Variant.VariantInt) {
			int val = res.getInt();
			return Integer.toString(val);
		} else {
			return res.getString();
		}
	}

	public Date getFieldAsDate(String field) {
		Variant res = Dispatch.call(this, "Field", field);
		double ddate = 0.0;
		if (res.isNull()) {
			return null;
		} else {
			ddate = res.getDate();
		}
		Date d = DateUtilities.convertWindowsTimeToDate(ddate);
		return d;
	}

	public Date getFieldAsDate(String field, String timeZone) {
		Variant res = Dispatch.call(this, "Field", field);
		double ddate = 0.0;
		if (res.isNull()) {
			return null;
		} else {
			ddate = res.getDate();
		}
		Date d = DateUtilities.convertWindowsTimeToDate(ddate);
		d = DateUtil.convertDateToTimeZone(d, timeZone);
		return d;
	}

	public Integer getFieldAsInt(String field) {
		Variant res = Dispatch.call(this, "Field", field);
		if (res.isNull() || res.getvt() == Variant.VariantDispatch) {
			return null;
		} else {
			return res.getInt();
		}
	}

	public void setField(String field, String value) {
		Dispatch.invoke(this, "Field", Dispatch.Put, new Object[] { field, value },
				new int[2]);
	}

	public boolean hasChange() {
		return getPropertyAsBoolean("HasChange");
	}

	public boolean isLocked() {
		return getPropertyAsBoolean("IsLocked");
	}

	public boolean isModified() {
		return getPropertyAsBoolean("Modified");
	}

	public String getPriority() {
		return getPropertyAsString("Priority");
	}

	public String getSeverity() {
		return getFieldAsString("BG_SEVERITY");
	}

	public void setSeverity(String severity) {
		setProperty("BG_SEVERITY", severity);
	}

	public void setPriority(String priority) {
		setProperty("Priority", priority);
	}

	public String getProject() {
		return getPropertyAsString("Project");
	}

	public void setSummary(String summary) {
		setProperty("Summary", summary);
	}

	public boolean isAutoPost() {
		return getPropertyAsBoolean("AutoPost");
	}

	public void setAutoPost(boolean autoPost) {
		setProperty("Autopost", autoPost);
	}

	public void post() {
		Dispatch.call(this, "Post");
	}

	public void undo() {
		Dispatch.call(this, "Undo");
	}

	public void lockObject() throws DefectAlreadyLockedException {
		if (isLocked()) {
			throw new DefectAlreadyLockedException("Bug locked by another user");
		} else {
			Dispatch.call(this, "LockObject");
			return;
		}
	}

	public void unlockObject() {
		Dispatch.call(this, "UnlockObject");
	}

	public void refresh() {
		Dispatch.call(this, "Refresh");
	}

	public String getDescription() {
		return getFieldAsString("BG_DESCRIPTION");
	}

	public void setDescription(String desc) {
		setField("BG_DESCRIPTION", desc);
	}

	public List<String> getAttachmentsNames() {
		IFactoryList attachments = new BugFactory(
				getPropertyAsComponent("Attachments")).getFilter().getNewList();
		List<String> att = new ArrayList<String>();
		for (int n = 1; n <= attachments.getCount(); ++n) {
			Dispatch item = attachments.getItem(n);
			String fileName = Dispatch.get(item, "FileName").toString();

			String shortName = fileName;
			int slash = shortName.lastIndexOf(File.separatorChar);
			if (slash >= 0) {
				shortName = shortName.substring(slash + 1);
			}

			att.add(shortName);
		}

		return att;
	}

	public File retrieveAttachmentData(String attachmentName, long delayBeforeDownloadingAttachment) {
		// int maxAttachmentUploadWaitCount = 10;
		// int waitCount = 0;
		IFilter filter = new AttachmentFactory(getPropertyAsComponent("Attachments")).getFilter();
		IFactoryList attachments = filter.getNewList();
		for (int n = 1; n <= attachments.getCount(); ++n) {
			Dispatch item = attachments.getItem(n);
			String fileName = Dispatch.get(item, "FileName").toString();
			if (!fileName.endsWith(attachmentName))
				continue;
			String attachmentKey = attachmentName + getId();
			Integer retryCount = attachmentRetryCount.get(attachmentKey);
			retryCount = retryCount == null ? 1 : retryCount + 1;
			attachmentRetryCount.put(attachmentKey, retryCount);
			
			
			int size = Dispatch.get(item, "FileSize").getInt(); // FIXME: should this be getLong?
			// Dispatch.get(item, "Data");
			try {
				logger.info("waiting for "+delayBeforeDownloadingAttachment+" ms before downloading "+attachmentName+".");
				Thread.sleep(delayBeforeDownloadingAttachment);
			} catch (InterruptedException e) {
			}
			logger.info("Going to load attachment " + attachmentName + ", expected file size: " + size);
			Dispatch.call(item, "Load", true, "");
			logger.debug("Attachment " + attachmentName + " has been read.");
			File attachmentFile = new File(fileName);

			// treat zero sized files like file not found but only do 7 retries at most in order to avoid
			// issues with "real" zero sized attachments
			boolean maxRetryCountReached = retryCount >= (size == 0 ? 7 : 10);
			if (!attachmentFile.exists() || (attachmentFile.length() == 0 && !maxRetryCountReached)) {
				/*
				 * If an attachment is still being uploaded when CCF tries to retrieve it,
				 * the QC 9.2 COM-API seems to succeed, but the file doesn't exist after the
				 * Load call.
				 * 
				 * QCReader.handleException() unwraps the AttachmentUploadStillInProgressException and
				 * causes the artifact to be retried.
				 */
				String message = String.format("The attachment file %s does not exist yet or is zero bytes long, ",
						fileName);
				if (!maxRetryCountReached) {
					throw new AttachmentUploadStillInProgressException(message + "retrying ...");
				} else {
					// give up on this attachment but don't stop other attachments
					// from being added with the same name later.
					attachmentRetryCount.remove(attachmentKey);
					throw new CCFRuntimeException(message + "giving up.");
				}
			}
			logger.info("actual file size downloaded: " + attachmentFile.length());
			if (attachmentFile.length() != reloadAttachmentSize(filter, attachmentName, delayBeforeDownloadingAttachment)) {
				String message = "Downloaded file size ("
						+ attachmentFile.length()
						+ ") and expected file size (" + size
						+ ") do not match for attachment "
						+ attachmentFile.getAbsolutePath();
				if (!maxRetryCountReached) {
					throw new AttachmentUploadStillInProgressException(message);
				} else {
					logger.warn(message + ". Shipping what we've got so far.");
				}
			}
			attachmentRetryCount.remove(attachmentKey);
			return attachmentFile;
		}

		throw new IllegalArgumentException(
				"No attachment with matching file name found: "
						+ attachmentName);
	}

	private long reloadAttachmentSize(IFilter filter, String attachmentName, long delay) {
		try {
			logger.info("waiting for "+delay+" ms before reloading meta data for "+attachmentName+".");
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
		IFactoryList attachments = filter.getNewList();
		String fileName = null;
		long fileSize = -1L;
		for (int n = 1; n <= attachments.getCount(); ++n) {
			Dispatch item = attachments.getItem(n);
			fileName = Dispatch.get(item, "FileName").toString();
			if (!fileName.endsWith(attachmentName))
				continue;
			fileSize = Dispatch.get(item, "FileSize").getInt();
		}
		if (fileName != null) {
			long currentDownloadedFileLenght = new File(fileName).length(); 
			return (currentDownloadedFileLenght > fileSize) ? currentDownloadedFileLenght:fileSize;
		} else {
			return -1L;
		}
	}

	public void createNewAttachment(String fileName, String description,
			int type) {
		IAttachmentFactory attachmentFactory = null;
		IAttachment attachment = null;
		try {
			attachmentFactory = new AttachmentFactory(
					getPropertyAsComponent("Attachments"));
			attachment = attachmentFactory.addItem();

			attachment.putFileName(fileName);
			attachment.putType(type);
			if (description != null) {
				attachment.putDescription(description);
			}
			attachment.post();
		} finally {
			if (attachment != null) {
				attachment.safeRelease();
				attachment = null;
			}
			if (attachmentFactory != null) {
				attachmentFactory.safeRelease();
				attachmentFactory = null;
			}
		}
		return;
	}

	public void createNewAttachment(String fileName, int type) {
		this.createNewAttachment(fileName, null, type);
		return;
	}

	public IAttachmentFactory getAttachmentFactory() {
		IAttachmentFactory attachmentFactory = new AttachmentFactory(
				getPropertyAsComponent("Attachments"));
		return attachmentFactory;
	}

	public boolean hasAttachments() {
		return getPropertyAsBoolean("HasAttachment");
	}

	public String getCommentsAsHTML() {
		return getFieldAsString("BG_DEV_COMMENTS");
	}

	public void setCommentsAsHTML(String s) {
		setField("BG_DEV_COMMENTS", s);
	}
	
    /**
     * Support for values that are not directly contained in this Bug, but
     * are instead determined via a separate, referenced object (i.e.
     * a joined table at the DB level). Values for these fields must be
     * indirectly obtained by first obtaining a separate COM object for the
     * referenced field, and then calling a direct property-get on that object.
     * Concrete example: Bug release and cycle info
     * @param fieldName name of the referenced field (e.g. BG_DETECTED_IN_REL)
     * @param subFieldName (sub) field in the referenced field whose value 
     *          you want
     * @return value from the referenced object as a String
     */
    public String getReferencedFieldAsString(String fieldName,
                                             String subFieldName) {
        String val = null;
		Variant res = Dispatch.call(this, "Field", fieldName);
		if (!res.isNull()) {
            Dispatch subfield = res.getDispatch();
            Variant subfieldVal = Dispatch.call(subfield, subFieldName);
            if (!subfieldVal.isNull()) {
                // NOTE: assuming that getString works for datetimes/numbers too
                val = subfieldVal.getString();
            }
		}
        return val;
    }

    /**
     * Support for values that are not directly contained in this Bug, but
     * are instead determined via a separate, referenced object (i.e.
     * a joined table at the DB level). Values for these fields must be
     * indirectly obtained by first obtaining a separate COM object for the
     * referenced field, and then calling a direct property-get on that object.
     * Concrete example: Bug release and cycle info
     * @param fieldName name of the referenced field (e.g. BG_DETECTED_IN_REL)
     * @param subFieldName (sub) field in the referenced field whose value 
     *          you want
     * @return value from the referenced object as an Integer
     */
    public Integer getReferencedFieldAsInt(String fieldName,
                                             String subFieldName) {
        Integer val = null;
		Variant res = Dispatch.call(this, "Field", fieldName);
		if (!res.isNull()) {
            Dispatch subfield = res.getDispatch();
            Variant subfieldVal = Dispatch.call(subfield, subFieldName);
            if (!subfieldVal.isNull()) {
                // NOTE: assuming that getString works for datetimes/numbers too
                val = subfieldVal.getInt();
            }
		}
        return val;
    }

	public void setAlwaysReloadAttachmentSize(boolean alwaysReloadAttachmentSize) {
		this.alwaysReloadAttachmentSize = alwaysReloadAttachmentSize;
	}

	public boolean isAlwaysReloadAttachmentSize() {
		return alwaysReloadAttachmentSize;
	}

    
	// public List<AttachmentData> getAttachmentData() {
	//
	// List<AttachmentData> attachmentDataList = new
	// ArrayList<AttachmentData>();
	//
	// List<String> attachmentNames = getAttachmentsNames();
	// for (String attachmentName:attachmentNames) {
	// byte [] contents = retrieveAttachmentData(attachmentName);
	// AttachmentData attachmentData = new AttachmentData();
	// attachmentData.setName(attachmentName);
	// attachmentData.setContents(contents);
	// attachmentDataList.add(attachmentData);
	// }
	//
	// return attachmentDataList;
	// }
}
