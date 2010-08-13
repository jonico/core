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

package com.collabnet.ccf.pi.qc.v90;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifact.ArtifactModeValue;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.core.utils.GATransformerUtil;
import com.collabnet.ccf.pi.qc.v90.api.DefectAlreadyLockedException;
import com.collabnet.ccf.pi.qc.v90.api.IBug;
import com.collabnet.ccf.pi.qc.v90.api.ICommand;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;
import com.collabnet.ccf.pi.qc.v90.api.IBugFactory;
import com.collabnet.ccf.pi.qc.v90.api.IFactoryList;
import com.collabnet.ccf.pi.qc.v90.api.IFilter;
import com.collabnet.ccf.pi.qc.v90.api.IRecordSet;
import com.collabnet.ccf.pi.qc.v90.api.IRequirement;
import com.collabnet.ccf.pi.qc.v90.api.IRequirementsFactory;
import com.collabnet.ccf.pi.qc.v90.api.IVersionControl;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Bug;
import com.collabnet.ccf.pi.qc.v90.api.dcom.Requirement;
import com.jacob.com.ComFailException;

/**
 * The Defect handler class provides support for listing and/or edit defects.
 * 
 */
public class QCHandler {

	private static final String FIRST_TAGS = "<html><body>";
	private static final String LAST_TAGS = "</body></html>";
	private static final Log log = LogFactory.getLog(QCHandler.class);
	// private QCAttachmentHandler attachmentHandler;
	private QCGAHelper qcGAHelper = new QCGAHelper();
	private final static String QC_BUG_ID = "BG_BUG_ID";
	private final static String QC_REQ_ID = "RQ_REQ_ID";
	private final static String QC_BUG_VER_STAMP = "BG_BUG_VER_STAMP";
	private final static String QC_BG_ATTACHMENT = "BG_ATTACHMENT";
	private final static String QC_RQ_ATTACHMENT = "RQ_ATTACHMENT";
	private final static String QC_BG_VTS = "BG_VTS";
	private final static String QC_RQ_VTS = "RQ_VTS";
	private final static String UNDERSCORE_STRING = "<font color=\"#000080\"><b>________________________________________</b></font>";

	public static IRecordSet executeSQL(IConnection qcc, String sql) {
		ICommand command = null;
		if (log.isDebugEnabled()) {
			log.debug("Going to execute SQL statement " + sql);
		}
		try {
			command = qcc.getCommand();
			command.setCommandText(sql);
			IRecordSet rs = command.execute();
			return rs;
		} finally {
			command = null;
		}
	}

	public static String getRequirementTypeTechnicalId(IConnection qcc,
			String requirementTypeName) {
		IRecordSet rs = null;
		try {
			rs = executeSQL(qcc,
					"SELECT TPR_TYPE_ID FROM REQ_TYPE WHERE TPR_NAME = '"
							+ requirementTypeName + "'");
			if (rs.getRecordCount() != 1) {
				throw new CCFRuntimeException(
						"Could not retrieve technical id for requirements type "
								+ requirementTypeName);
			} else {
				return rs.getFieldValueAsString("TPR_TYPE_ID");
			}
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
	}

	// TODO review
	public List<IQCDefect> getDefectsWithIds(IConnection qcc, List<Integer> ids) {
		IBugFactory bf = qcc.getBugFactory();
		IFilter filter = bf.getFilter();

		List<IQCDefect> tasks = new ArrayList<IQCDefect>();
		for (int i = 0; i < ids.size(); ++i) {
			filter.setFilter(QC_BUG_ID, ids.get(i).toString());
			IFactoryList fl = filter.getNewList();

			IBug bug = fl.getBug(1);
			QCDefect defect = new QCDefect((Bug) bug);

			tasks.add(defect);

			fl.safeRelease();
		}

		filter.safeRelease();
		bf = null;

		return tasks;
	}

	/**
	 * Updates the requirement identified by the incoming requirement in QC
	 * 
	 * @param qcc
	 *            The Connection object
	 * @param requirementId
	 *            The ID of the requirement to be updated
	 * @param List
	 *            <GenericArtifactField> The values of each fields of the
	 *            requirement that need to be updated on the old values.
	 * @param connectorUser
	 *            The connectorUser name used while updating the comments
	 * @param targetParentArtifactId
	 * @param preserveSemanticallyUnchangedHTMLFieldValues 
	 * @param ignoreLocks 
	 * @param genericArtifact 
	 * 
	 */
	public void updateRequirement(IConnection qcc,
			String requirementId, List<GenericArtifactField> allFields,
			String connectorUser, String targetSystemTimezone,
			String targetParentArtifactId, boolean preserveSemanticallyUnchangedHTMLFieldValues,
			boolean ignoreLocks, GenericArtifact genericArtifact) {

		IRequirementsFactory reqFactory = null;
		IRequirement req = null;
		boolean lockedReq = false;
		boolean movedLock = false;
		boolean wasPartialUpdate = false;
		boolean shouldPerformCompleteUpdate = genericArtifact.getArtifactMode() != GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY;

		IVersionControl versionControl = null;
		boolean versionControlSupported = false;
		try {
			reqFactory = qcc.getRequirementsFactory();
			req = reqFactory.getItem(requirementId);
			versionControl = req.getVersionControlObject();

			if (versionControl != null) {
				versionControlSupported = ccfCheckoutReq(qcc, req, versionControl);
			}
			if (ignoreLocks) {
				String lockOwner = getLockOwner(qcc, requirementId, false);
				if (!StringUtils.isEmpty(lockOwner) && !qcc.getUsername().equalsIgnoreCase(lockOwner)) {
					if (!shouldPerformCompleteUpdate) {
						// comments that have previously been quarantined for later processing.
						throw new DefectAlreadyLockedException(String.format(
								"Could not perform partial update, because requirement '%s' is still locked / locked again by user '%s'.",
								requirementId,
								lockOwner));
					} else {
						moveLock(qcc, requirementId, false);
						movedLock = true;
					}
				}
			}
			req.lockObject();
			lockedReq = true;

			Set<String> allFieldNames = new HashSet<String>();
			String fieldValue = null;
			for (GenericArtifactField thisField : allFields) {

				String fieldName = thisField.getFieldName();
				if (!shouldPerformCompleteUpdate
						&& !QCConfigHelper.QC_RQ_DEV_COMMENTS.equals(fieldName)) {
						// we already updated the other fields before and are only
						// interested in the comments this time.
						continue;
					}
				if (thisField.getFieldValueType().equals(
						GenericArtifactField.FieldValueTypeValue.DATE)
						|| thisField.getFieldValueType().equals(GenericArtifactField.FieldValueTypeValue.DATETIME)) {
					fieldValue = getProperFieldValue(thisField,
							targetSystemTimezone);
				} else {
					fieldValue = (String) thisField.getFieldValue();
				}

				try {	
					if (fieldName.equals(QCConfigHelper.QC_RQ_DEV_COMMENTS)) {
						String oldFieldValue = req.getFieldAsString(fieldName);
						if ((!StringUtils.isEmpty(oldFieldValue) &&
							 !StringUtils.isEmpty(fieldValue) &&
							 !oldFieldValue.equals(fieldValue))
						    ||
						    (StringUtils.isEmpty(oldFieldValue) &&
				    		 !StringUtils.isEmpty(fieldValue))) {
							fieldValue = getConcatinatedCommentValue(oldFieldValue,
									fieldValue, connectorUser);
						}
						if (!movedLock) {
							req.setField(fieldName, fieldValue);
						} else {
							wasPartialUpdate = true;
						}
					} else if (!(allFieldNames.contains(thisField.getFieldName()))
							&& !(fieldName.equals(QC_REQ_ID)
									|| fieldName.equals(QC_RQ_ATTACHMENT) 
									|| fieldName.equals(QC_RQ_VTS)
									|| fieldName.endsWith(QCConfigHelper.HUMAN_READABLE_SUFFIX))) {
						 
						// only handle every field once
						// if there was a multi select field, we already concatenated all its values in the field value of its first occurrence
							if (preserveSemanticallyUnchangedHTMLFieldValues && 
								!StringUtils.isEmpty(fieldValue) && 
								fieldValue.startsWith(QCConfigHelper.HTMLSTRING_PREFIX)) {
	
								String oldFieldValue = req.getFieldAsString(fieldName);
								String strippedOldValue = GATransformerUtil.stripHTML(oldFieldValue).replaceAll("\\s", "");
								String strippedNewValue = GATransformerUtil.stripHTML(fieldValue).replaceAll("\\s", "");
								if (StringUtils.isEmpty(oldFieldValue) ||
									! strippedNewValue.equals(strippedOldValue)) {
									req.setField(fieldName, fieldValue);
								} else {
									log.info("skipping update of field '" + fieldName + "', because only fomatting has changed.");
								}
							} else if("RQ_TARGET_REL".equals(fieldName) || "RQ_TARGET_RCYC".equals(fieldName)) {
								// hard-code the linked fields here
								if(fieldValue == null || fieldValue.trim().length() == 0) {
									req.clearListValuedField(fieldName);
								} else {
									req.setListValuedField(fieldName, fieldValue);
								}
							} else {
								req.setField(fieldName, fieldValue);
							}
							allFieldNames.add(fieldName);
					}
				} catch (ComFailException e) {
					String message = "Exception while setting the value of field "
							+ fieldName
							+ " to "
							+ fieldValue
							+ ": "
							+ e.getMessage();
					log.error(message, e);
					throw new CCFRuntimeException(message, e);
				}
			}

			req.post();

			String parentId = req.getParentId();
			// move to other parent if necessary
			if (targetParentArtifactId != null
					&& !targetParentArtifactId
							.equals(GenericArtifact.VALUE_UNKNOWN)
					&& !targetParentArtifactId.equals(parentId)) {
				if (targetParentArtifactId.equals(GenericArtifact.VALUE_NONE)) {
					if (!"-1".equals(parentId) && !"0".equals(parentId)) {
						// we assume that requirement 0 is the top level
						// requirement
						req.move(0, 1);
					}
				} else {
					req.move(Integer.parseInt(targetParentArtifactId), 1);
				}
			}

		} catch (DefectAlreadyLockedException e) {
			/*
			 * do not try to restore a moved lock here, because this 
			 * condition would only occur if another user re-locked the 
			 * artifact before we could acquire it after stealing it.
			 */
			
			if (!shouldPerformCompleteUpdate && ignoreLocks) {
				throw new CCFRuntimeException(e.getMessage(), e);
			} else {
		
				String message = "Attempt to lock the requirement with id "
						+ requirementId + " failed.";
				String lockOwner = getLockOwner(qcc, requirementId, false);
				
				if (StringUtils.isEmpty(lockOwner)) {
						message += " Could not find out the user who locked it.";
					} else {
						message += " Requirement has been locked by user " + lockOwner;
					}
		
				throw new CCFRuntimeException(message, e);
			}
		} catch (ComFailException e) {
			req.undo();

			String message = "ComFailException while updating the requirement with id "
					+ requirementId;
			log.error(message, e);
			throw new CCFRuntimeException(message + ": " + e.getMessage(), e);
		} catch (Exception e) {
			req.undo();
			String message = "Exception while updating the requirement with id "
					+ requirementId;
			log.error(message, e);
			throw new CCFRuntimeException(message + ": " + e.getMessage(), e);
		} finally {
			if (versionControlSupported) {
				try {
					versionControl.checkIn("CCF CheckIn");
					versionControl.safeRelease();
				} catch (Exception e) {
					String message = "Failed to checkin requirement "
							+ req.getId() + " again";
					log.error(message, e);
					if (lockedReq) {
						req.unlockObject();
					}
					if (movedLock) {
						restoreLock(qcc, requirementId, false);
						if (wasPartialUpdate) {
							// mark requirement as only partially updated, so later components can
							// react appropriately.
							genericArtifact.setErrorCode(GenericArtifact.ERROR_OBJECT_LOCKED);
							genericArtifact.setArtifactMode(ArtifactModeValue.CHANGEDFIELDSONLY);
						}
					}
					throw new CCFRuntimeException(message, e);
				}
			}
			if (req != null) {
				if (lockedReq) {
					req.unlockObject();
				}
				req.safeRelease();
			}
			if (movedLock) {
				restoreLock(qcc, requirementId, false);
				if (wasPartialUpdate) {
					// mark requirement as only partially updated, so later components can
					// react appropriately.
					genericArtifact.setErrorCode(GenericArtifact.ERROR_OBJECT_LOCKED);
					genericArtifact.setArtifactMode(ArtifactModeValue.CHANGEDFIELDSONLY);
				}
			}

		}
	}

	private boolean ccfCheckoutReq(IConnection qcc, IRequirement req,
			IVersionControl versionControl) {
		boolean versionControlSupported = false;
		try {
			versionControlSupported = versionControl.checkOut("CCF Checkout");
		} catch (ComFailException e) {
			// check whether we have already checked out this
			// requirement
			if (qcc.getUsername().equalsIgnoreCase(
					req.getFieldAsString("RQ_VC_CHECKOUT_USER_NAME"))) {
				log
						.warn("Requirement "
								+ req.getId()
								+ " has been already checked out by connector user "
								+ qcc.getUsername()
								+ " so we still proceed ...");
			} else {
				String message = "Requirement "
						+ req.getId()
						+ " has been checked out by "
						+ req
								.getFieldAsString("RQ_VC_CHECKOUT_USER_NAME")
						+ " on "
						+ req.getFieldAsDate("RQ_VC_CHECKOUT_DATE")
						+ " at "
						+ req.getFieldAsString("RQ_VC_CHECKOUT_TIME")
						+ " with version number "
						+ req.getFieldAsInt("RQ_VC_VERSION_NUMBER");
				log.error(message, e);
				throw new CCFRuntimeException(message, e);
			}
		}
		return versionControlSupported;
	}

	/**
	 * Updates the defect identified by the incoming bugId in QC
	 * 
	 * @param qcc
	 *            The Connection object
	 * @param bugId
	 *            The ID of the defect to be updated
	 * @param List
	 *            <GenericArtifactField> The values of each fields of the defect
	 *            that need to be updated on the old values.
	 * @param connectorUser
	 *            The connectorUser name used while updating the comments
	 * @param ignoreLocks 
	 * @param genericArtifact 
	 */
	public void updateDefect(IConnection qcc, String bugId,
			List<GenericArtifactField> allFields, String connectorUser,
			String targetSystemTimezone,
			boolean preserveSemanticallyUnchangedHTMLFieldValues,
			boolean ignoreLocks, GenericArtifact genericArtifact) {

		IBugFactory bugFactory = null;
		IBug bug = null;
		boolean lockedBug = false;
		boolean movedLock = false;
		boolean wasPartialUpdate = false;
		boolean shouldPerformCompleteUpdate = genericArtifact.getArtifactMode() != GenericArtifact.ArtifactModeValue.CHANGEDFIELDSONLY;
		try {
			bugFactory = qcc.getBugFactory();
			bug = bugFactory.getItem(bugId);
			if (ignoreLocks) {
				String lockOwner = getLockOwner(qcc, bugId, true);
				if (!StringUtils.isEmpty(lockOwner) && !qcc.getUsername().equalsIgnoreCase(lockOwner)) {
					if (!shouldPerformCompleteUpdate) {
						// comments that have previously been quarantined for later processing.
						throw new DefectAlreadyLockedException(String.format(
								"Could not perform partial update, because defect '%s' is still locked / locked again by user '%s'.",
								bugId,
								lockOwner));
					} else {
						moveLock(qcc, bugId, true);
						movedLock = true;
					}
				}
			}
			bug.lockObject();
			lockedBug = true;
			Set<String> allFieldNames = new HashSet<String>();
			String fieldValue = null;
			for (GenericArtifactField thisField : allFields) {

				String fieldName = thisField.getFieldName();
				if (!shouldPerformCompleteUpdate
					&& !QCConfigHelper.QC_BG_DEV_COMMENTS.equals(fieldName)) {
					// we already updated the other fields before and are only
					// interested in the comments this time.
					continue;
				}
				if (thisField.getFieldValueType().equals(
						GenericArtifactField.FieldValueTypeValue.DATE)
						|| thisField
								.getFieldValueType()
								.equals(
										GenericArtifactField.FieldValueTypeValue.DATETIME)) {
					fieldValue = getProperFieldValue(thisField, targetSystemTimezone);
				} else {
					fieldValue = (String) thisField.getFieldValue();
				}

				try {
					if (fieldName.equals(QCConfigHelper.QC_BG_DEV_COMMENTS)) {
						String oldFieldValue = bug.getFieldAsString(fieldName);
						if ((!StringUtils.isEmpty(oldFieldValue) && 
							 !StringUtils.isEmpty(fieldValue) &&
							 !oldFieldValue.equals(fieldValue))
							|| 
							(StringUtils.isEmpty(oldFieldValue) &&
							 !StringUtils.isEmpty(fieldValue))) {
							fieldValue = getConcatinatedCommentValue(oldFieldValue,
									fieldValue, connectorUser);
						}
						if (!movedLock) {
							bug.setField(fieldName, fieldValue);
						} else {
							wasPartialUpdate = true;
						}
					} else if (!(allFieldNames.contains(thisField.getFieldName()))
							&& !(fieldName.equals(QC_BUG_ID)
									|| fieldName.equals(QC_BUG_VER_STAMP)
									|| fieldName.equals(QC_BG_ATTACHMENT) 
									|| fieldName.equals(QC_BG_VTS)
									|| fieldName.endsWith(QCConfigHelper.HUMAN_READABLE_SUFFIX))) {
						// only handle every field once
						// if there was a multi select field, we already concatenated all its values in the field value of its first occurrence
						if (preserveSemanticallyUnchangedHTMLFieldValues && 
							!StringUtils.isEmpty(fieldValue) && 
							fieldValue.startsWith(QCConfigHelper.HTMLSTRING_PREFIX)) {
							String oldFieldValue = bug.getFieldAsString(fieldName);
							String strippedOldValue = GATransformerUtil.stripHTML(oldFieldValue).replaceAll("\\s", "");
							String strippedNewValue = GATransformerUtil.stripHTML(fieldValue).replaceAll("\\s", "");
							if (StringUtils.isEmpty(oldFieldValue) ||
								! strippedNewValue.equals(strippedOldValue)) {
								bug.setField(fieldName, fieldValue);
							} else {
								log.info("skipping update of field '" + fieldName + "', because only fomatting has changed.");
							}
						} else {
							bug.setField(fieldName, fieldValue);
						}
						allFieldNames.add(fieldName);
					}
				} catch (ComFailException e) {
					String message = "Exception while setting the value of field "
							+ fieldName
							+ " to "
							+ fieldValue
							+ ": "
							+ e.getMessage();
					log.error(message, e);
					throw new CCFRuntimeException(message, e);
				}
			}
			bug.post();
			
		} catch (DefectAlreadyLockedException e) {
			/*
			 * do not try to restore a moved lock here, because this 
			 * condition would only occur if another user re-locked the 
			 * artifact before we could acquire it after stealing it.
			 */
			
			if (!shouldPerformCompleteUpdate && ignoreLocks) {
				throw new CCFRuntimeException(e.getMessage(), e);
			} else {
				String message = "Attempt to lock the defect with id " + bugId
						+ " failed.";
				String lockOwner = getLockOwner(qcc, bugId, true);
				
				if (StringUtils.isEmpty(lockOwner)) {
					message += " Could not find out the user who locked it.";
				} else {
					message += " Defect has been locked by user " + lockOwner;
				}

				throw new CCFRuntimeException(message, e);
			}
		} catch (ComFailException e) {
			bug.undo();
			String message = "ComFailException while updating the defect with id "
					+ bugId;
			log.error(message, e);
			throw new CCFRuntimeException(message + ": " + e.getMessage(), e);
		} catch (Exception e) {
			bug.undo();
			String message = "Exception while updating the defect with id "
					+ bugId;
			log.error(message, e);
			throw new CCFRuntimeException(message + ": " + e.getMessage(), e);
		} finally {
			if(bug != null) {
				if (lockedBug) {
					bug.unlockObject();
				}
				bug.safeRelease();
			}
			if (movedLock) {
				restoreLock(qcc, bugId, true);
				if (wasPartialUpdate) {
					// mark defect as only partially updated, so later components can
					// react appropriately.
					genericArtifact.setErrorCode(GenericArtifact.ERROR_OBJECT_LOCKED);
					genericArtifact.setArtifactMode(ArtifactModeValue.CHANGEDFIELDSONLY);
				}
			}
		}
	}

	private void moveLock(IConnection qcc, String artifactId, boolean isDefect) {
		deleteStaleMovedLock(qcc, artifactId, isDefect);
		int newLockId = -(Integer.parseInt(artifactId) + 1);
		String sql = String.format(
				"UPDATE LOCKS SET LK_OBJECT_KEY='%s' WHERE LK_OBJECT_KEY='%s' AND LK_OBJECT_TYPE='%s'",
				newLockId,
				artifactId,
				artifactTypeString(isDefect));
		IRecordSet rs = null;
		try {
			rs = executeSQL(qcc, sql);
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
	}
	
	private void restoreLock(IConnection qcc, String artifactId, boolean isDefect) {
		int currentLockId = -(Integer.parseInt(artifactId) + 1);
		String sql = String.format(
				"UPDATE LOCKS SET LK_OBJECT_KEY='%s' WHERE LK_OBJECT_KEY='%s' AND LK_OBJECT_TYPE='%s'",
				artifactId,
				currentLockId,
				artifactTypeString(isDefect));
		IRecordSet rs = null;
		try {
			rs = executeSQL(qcc, sql);
		} catch (Exception e) {
			deleteStaleMovedLock(qcc, artifactId, isDefect);
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
	}

	private String artifactTypeString(boolean isDefect) {
		return isDefect ? "BUG" : "REQ";
	}
	
	private void deleteStaleMovedLock(IConnection qcc, String artifactId,
			boolean isDefect) {
		int newArtifactId = -(Integer.parseInt(artifactId) + 1);
		String sql = String.format("DELETE FROM LOCKS WHERE LK_OBJECT_KEY = '%s' AND LK_OBJECT_TYPE = '%s'",
				newArtifactId,
				artifactTypeString(isDefect));
		IRecordSet rs = null;
		try {
			rs = executeSQL(qcc, sql);
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
	}

	private String getLockOwner(IConnection qcc, String artifactId,
			boolean isDefect) {
		// retrieving user who locked the bug
		IRecordSet rs = null;
		try {
			rs = executeSQL(qcc, String.format(
					"SELECT LK_USER FROM LOCKS WHERE LK_OBJECT_KEY = '%s' AND LK_OBJECT_TYPE = '%s'",
					artifactId,
					artifactTypeString(isDefect)));
			if (rs.getRecordCount() != 1) {
				return null;
			} else {
				String userName = rs.getFieldValueAsString("LK_USER");
				return userName;
			}
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
	}

	/**
	 * Create the defect based on the incoming field values
	 * 
	 * @param qcc
	 *            The Connection object
	 * @param List
	 *            <GenericArtifactField> The values of each fields of the defect
	 *            that need to be used while creation.
	 * 
	 * @return IQCDefect Created defect object
	 * 
	 */
	public IQCDefect createDefect(IConnection qcc,
			List<GenericArtifactField> allFields, String connectorUser,
			String targetSystemTimezone) throws RemoteException {

		IBugFactory bugFactory = null;
		IBug bug = null;
		try {
			bugFactory = qcc.getBugFactory();
			bug = bugFactory.addItem("Defect created by Connector");
			bug.lockObject();
			List<String> allFieldNames = new ArrayList<String>();
			String fieldValue = null;
			for (int cnt = 0; cnt < allFields.size(); cnt++) {

				GenericArtifactField thisField = allFields.get(cnt);
				String fieldName = thisField.getFieldName();
				if (thisField.getFieldValueType().equals(
						GenericArtifactField.FieldValueTypeValue.DATE)
						|| thisField
								.getFieldValueType()
								.equals(
										GenericArtifactField.FieldValueTypeValue.DATETIME))
					fieldValue = getProperFieldValue(thisField,
							targetSystemTimezone);
				else
					fieldValue = (String) thisField.getFieldValue();

				if (fieldName.equals(QCConfigHelper.QC_BG_DEV_COMMENTS)) {
					String oldFieldValue = bug.getFieldAsString(fieldName);
					if ((!StringUtils.isEmpty(oldFieldValue)
							&& !StringUtils.isEmpty(fieldValue) && !oldFieldValue
							.equals(fieldValue))
							|| (StringUtils.isEmpty(oldFieldValue) && !StringUtils
									.isEmpty(fieldValue))) {
						fieldValue = getConcatinatedCommentValue(oldFieldValue,
								fieldValue, connectorUser);
					}
				}
				/*
				 * The following fields cannot be set or have some conditions
				 * Cannot be set from here: 1. BG_BUG_ID 2. BG_BUG_VER_STAMP 3.
				 * BG_VTS Has some conditions: 1. BG_SUBJECT -> Can be set to a
				 * Valid value that is present in the list.
				 */
				if (!(allFieldNames.contains(allFields.get(cnt).getFieldName()))
						&& !(fieldName.equals(QC_BUG_ID)
								|| fieldName.equals(QC_BUG_VER_STAMP)
								|| fieldName.equals(QC_BG_ATTACHMENT) 
								|| fieldName.equals(QC_BG_VTS)
								|| fieldName.endsWith(QCConfigHelper.HUMAN_READABLE_SUFFIX))) {
					try {
						bug.setField(fieldName, fieldValue);
					} catch (Exception e) {
						String message = "Exception while setting the value of field "
								+ fieldName
								+ " to "
								+ fieldValue
								+ ": "
								+ e.getMessage();
						log.error(message, e);
						throw new CCFRuntimeException(message, e);
					}
				} else {
					log.debug(fieldName);
				}
				if (!fieldName.equals(QCConfigHelper.QC_BG_DEV_COMMENTS))
					allFieldNames.add(fieldName);
			}
			bug.post();
		} catch (Exception e) {
			String bugId = null;
			if (bug != null) {
				bugId = bug.getId();
				bugFactory.removeItem(bugId);
				bug = null;
			}
			String message = "Exception while creating Bug " + bugId;
			log.error(message, e);
			throw new CCFRuntimeException(message + ": " + e.getMessage(), e);
		} finally {
			if (bug != null) {
				bug.unlockObject();
			}
			bugFactory = null;
		}

		return new QCDefect((Bug) bug);
	}

	public String getConcatinatedCommentValue(String oldFieldValue,
			String fieldValue, String connectorUser) {

		String concatinatedFieldValue = null;
		java.util.Date date = new java.util.Date();
		String currentDateString = DateUtil.formatQCDate(date);
		fieldValue = fieldValue.replaceAll("\t", "        ");
		if (!StringUtils.isEmpty(oldFieldValue)) {
			oldFieldValue = this.stripStartAndEndTags(oldFieldValue);
			concatinatedFieldValue = FIRST_TAGS + oldFieldValue + "<br>"
					+ UNDERSCORE_STRING + "<br>"
					+ "<font color=\"#000080\"><b>" + connectorUser + ", "
					+ currentDateString + ": " + "</b></font>" + fieldValue
					+ LAST_TAGS;
		} else {
			concatinatedFieldValue = FIRST_TAGS + "<font color=\"#000080\"><b>"
					+ connectorUser + ", " + currentDateString + ": "
					+ "</b></font>" + fieldValue + LAST_TAGS;
		}

		return concatinatedFieldValue;
	}

	// public String stripLastTags(String oldFieldValue) {
	// if (oldFieldValue.endsWith(LAST_TAGS)) {
	// return oldFieldValue.substring(0, oldFieldValue.length() -
	// LAST_TAGS.length());
	// }
	// else {
	// return oldFieldValue;
	// }
	// }

	/**
	 * Gets the value of the field in a suitable data type.
	 * 
	 */
	public String getProperFieldValue(GenericArtifactField thisField,
			String targetSystemTimezone) {

		String fieldValue = null;
		GenericArtifactField.FieldValueTypeValue fieldValueTypeValue = thisField
				.getFieldValueType();
		switch (fieldValueTypeValue) {
		case DATE: {
			GregorianCalendar gcal = (GregorianCalendar) thisField
					.getFieldValue();
			if (gcal != null) {
				Date targetTimezoneDate = gcal.getTime();
				if (DateUtil.isAbsoluteDateInTimezone(targetTimezoneDate,
						DateUtil.GMT_TIME_ZONE_STRING)) {
					targetTimezoneDate = DateUtil
							.convertGMTToTimezoneAbsoluteDate(
									targetTimezoneDate, TimeZone.getDefault()
											.getDisplayName(false,
													TimeZone.SHORT));
				}
				fieldValue = DateUtil.formatQCDate(targetTimezoneDate);
			}
			break;
		}
		case DATETIME: {
			Date targetTimezoneDate = (Date) thisField.getFieldValue();
			if (targetTimezoneDate != null) {
				fieldValue = DateUtil.formatQCDate(targetTimezoneDate);
			}
			break;
		}

		}
		return fieldValue;

	}

	/**
	 * Return all defects modified between the given time range, in a map
	 * 
	 */
	public List<ArtifactState> getLatestChangedDefects(IConnection qcc,
			String transactionId) {

		int rc = 0;
		String sql = "SELECT AU_ENTITY_ID, AU_ACTION_ID, AU_TIME FROM AUDIT_LOG WHERE AU_ENTITY_TYPE = 'BUG' AND AU_ACTION_ID > '"
				+ transactionId
				+ "' AND AU_ACTION!='DELETE' AND AU_FATHER_ID = '-1' ORDER BY AU_ACTION_ID";
		log.debug(sql);
		ArrayList<ArtifactState> changedDefects = new ArrayList<ArtifactState>();
		HashMap<String, ArtifactState> artifactIdStateMap = new HashMap<String, ArtifactState>();
		IRecordSet rs = null;
		try {
			rs = executeSQL(qcc, sql);
			if (rs != null)
				rc = rs.getRecordCount();

			for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
				String bugId = rs.getFieldValueAsString("AU_ENTITY_ID");
				String actionIdStr = rs.getFieldValueAsString("AU_ACTION_ID");
				int actionId = Integer.parseInt(actionIdStr);
				String actionDateStr = rs.getFieldValueAsString("AU_TIME");
				Date actionDate = DateUtil.parseQCDate(actionDateStr);
				if (artifactIdStateMap.containsKey(bugId)) {
					ArtifactState state = artifactIdStateMap.get(bugId);
					changedDefects.remove(state);
					state.setArtifactLastModifiedDate(actionDate);
					state.setArtifactVersion(actionId);
					changedDefects.add(state);
				} else {
					ArtifactState state = new ArtifactState();
					state.setArtifactId(bugId);
					state.setArtifactLastModifiedDate(actionDate);
					state.setArtifactVersion(actionId);
					changedDefects.add(state);
					artifactIdStateMap.put(bugId, state);
				}
			}
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
		return changedDefects;
	}

	/**
	 * Orders the values of the incoming HashMap according to its keys.
	 * 
	 * @param HashMap
	 *            This hashMap contains the transactionIds as values indexed by
	 *            their defectIds.
	 * @return List<String> This list of strings is the ordering of the keys of
	 *         the incoming HashMaps, which are the defectIds, according to the
	 *         order of their transactionIds
	 * 
	 */
	public List<String> orderByLatestTransactionIds(
			Map<String, String> defectIdTransactionIdMap) {

		List<String> mapKeys = new ArrayList<String>(defectIdTransactionIdMap
				.keySet());
		List<String> mapValues = new ArrayList<String>(defectIdTransactionIdMap
				.values());

		defectIdTransactionIdMap.clear();
		TreeSet<String> sortedSet = new TreeSet<String>(mapValues);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;
		for (int i = 0; i < size; i++) {
			defectIdTransactionIdMap.put(mapKeys.get(mapValues
					.indexOf(sortedArray[i])), (String) sortedArray[i]);
		}

		List<String> orderedDefectList = new ArrayList<String>(
				defectIdTransactionIdMap.keySet());
		return orderedDefectList;
	}

	// /**
	// * Finds the state of the defect at a particular defectId and
	// transactionId.
	// * This is used while finding a bunch of history artifacts for a defectId
	// * after a particular state represented by the transactionId.
	// *
	// * @param entityId
	// * The defectId for which the search has to be made in QC
	// * @param actionId
	// * The transactionId starting from which the search has to be
	// * made for a particular defectId in QC
	// * @param transactionId
	// * The transactionId starting from which the search has to be
	// * made for a particular defectId in QC
	// * @param qcc
	// * The Connection object
	// * @param latestDefectArtifact
	// * The GenericArtifact into which the latest state information of
	// * the defect identified by entityId is captured.
	// * @return GenericArtifact The resultant GenericArtifact which has the
	// * latest state of the defect information in it.
	// */
	// public GenericArtifact getStateOfDefectAtActionID(IConnection qcc,
	// int entityId, int actionId, String transactionId,
	// GenericArtifact latestDefectArtifact) {
	//
	// String sql = "SELECT AU_ACTION_ID FROM AUDIT_LOG WHERE AU_ENTITY_TYPE =
	// 'BUG' AND AU_ACTION_ID > '"
	// + actionId
	// + "' AND AU_ACTION!='DELETE' AND AU_ENTITY_TYPE= 'BUG' AND AU_ENTITY_ID =
	// '"
	// + entityId + "'";
	// if (transactionId != null && !transactionId.equals(""))
	// sql += " AND AU_ACTION_ID >= '" + transactionId + "'";
	// sql += " ORDER BY AU_ACTION_ID DESC";
	// log.debug(sql);
	//
	// IRecordSet rs = executeSQL(qcc, sql);
	// int rc = rs.getRecordCount();
	// int txnId = 0;
	// for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
	//
	// txnId = Integer.parseInt(rs.getFieldValue("AU_ACTION_ID"));
	// sql = "SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID= '"
	// + txnId + "'";
	// IRecordSet newRs = executeSQL(qcc, sql);
	// int newRc = newRs.getRecordCount();
	//
	// for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
	// String fieldName = newRs.getFieldValue("AP_FIELD_NAME");
	// String oldFieldValue = null;
	// if (!(fieldName.equals("BG_DESCRIPTION")))
	// oldFieldValue = newRs.getFieldValue("AP_OLD_VALUE");
	// else
	// oldFieldValue = newRs.getFieldValue("AP_OLD_LONG_VALUE");
	//
	// List<GenericArtifactField> genArtifactFields = latestDefectArtifact
	// .getAllGenericArtifactFieldsWithSameFieldName(fieldName);
	// if (genArtifactFields != null
	// && genArtifactFields.get(0) != null)
	// genArtifactFields.get(0).setFieldValue(oldFieldValue);
	// // genArtifactFields.get(0).setFieldValueHasChanged(true);
	// }
	//
	// }
	// List<String> txnIds = getTransactionIdsInRange(qcc, entityId, actionId,
	// actionId, null);
	// IRecordSet newRs = getAuditPropertiesRecordSet(qcc, txnIds);
	// String deltaComment = getDeltaOfComment(newRs);
	// if (deltaComment != null) {
	// List<GenericArtifactField> genArtifactFieldsForComments =
	// latestDefectArtifact
	// .getAllGenericArtifactFieldsWithSameFieldName("BG_DEV_COMMENTS");
	// genArtifactFieldsForComments.get(0).setFieldValue(deltaComment);
	// }
	//
	// List<GenericArtifactField> genArtifactFields = latestDefectArtifact
	// .getAllGenericArtifactFieldsWithSameFieldName("BG_VTS");
	//
	// if (genArtifactFields != null && genArtifactFields.get(0) != null
	// && genArtifactFields.get(0).getFieldValue() != null
	// && !(genArtifactFields.get(0).getFieldValue().equals(""))) {
	// Date newBgVts = DateUtil.parseQCDate((String) genArtifactFields
	// .get(0).getFieldValue());
	// latestDefectArtifact.setSourceArtifactLastModifiedDate(DateUtil
	// .format(newBgVts));
	// return latestDefectArtifact;
	// } else {
	// // This means the BG_VTS field is null. So, find it, populate it &
	// // ArtifactLastModifiedDate
	// String bgVts = qcGAHelper.findBgVtsFromQC(qcc, actionId, entityId);
	// genArtifactFields.get(0).setFieldValue((String) bgVts);
	// Date newBgVts = DateUtil.parseQCDate(bgVts);
	// String lastModifiedDate = DateUtil.format(newBgVts);
	// latestDefectArtifact
	// .setSourceArtifactLastModifiedDate(lastModifiedDate);
	// }
	//
	// // The ArtifactActionValue IGNORE and DELETE needs to be done.
	// //
	// latestDefectArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UNKNOWN);
	// return latestDefectArtifact;
	//
	// }
	//
	// /**
	// * Given an action id (id for the AUDIT_LOG table
	// *
	// * @param actionId
	// * @return the defect at the time of the actionId
	// */
	// public QCDefect getArtifactStateFromActionId(int actionId) {
	// // return the state of the given defect at transaction actionId
	//
	// QCDefect defect = null;
	// return defect;
	// }

	/**
	 * Assigns values of the incoming parameters to the incoming genericArtifact
	 * 
	 * @param latestDefectArtifact
	 *            The GenericArtifact to which the following values need to be
	 *            assigned.
	 * @param sourceArtifactId
	 * @param sourceRepositoryId
	 * @param sourceRepositoryKind
	 * @param sourceSystemId
	 * @param sourceSystemKind
	 * @param targetRepositoryId
	 * @param targetRepositoryKind
	 * @param targetSystemId
	 * @param targetSystemKind
	 * @param thisTransactionId
	 */
	public void assignValues(GenericArtifact latestDefectArtifact,
			String sourceArtifactId, String sourceRepositoryId,
			String sourceRepositoryKind, String sourceSystemId,
			String sourceSystemKind, String targetRepositoryId,
			String targetRepositoryKind, String targetSystemId,
			String targetSystemKind, String thisTransactionId, 
			String sourceSystemTimezone, String targetSystemTimezone) {

		latestDefectArtifact.setSourceArtifactId(sourceArtifactId);
		latestDefectArtifact.setSourceRepositoryId(sourceRepositoryId);
		latestDefectArtifact.setSourceRepositoryKind(sourceRepositoryKind);
		latestDefectArtifact.setSourceSystemId(sourceSystemId);
		latestDefectArtifact.setSourceSystemKind(sourceSystemKind);
		latestDefectArtifact.setSourceSystemTimezone(sourceSystemTimezone);

		latestDefectArtifact.setTargetRepositoryId(targetRepositoryId);
		latestDefectArtifact.setTargetRepositoryKind(targetRepositoryKind);
		latestDefectArtifact.setTargetSystemId(targetSystemId);
		latestDefectArtifact.setTargetSystemKind(targetSystemKind);
		latestDefectArtifact.setSourceArtifactVersion(thisTransactionId);
		latestDefectArtifact.setTargetSystemTimezone(targetSystemTimezone);
	}

	/**
	 * Obtains the artifactAction based on the date at which that defect was
	 * created and the lastReadTime synchronization parameter.
	 * 
	 * @param entityId
	 *            The defectId for which the search has to be made in QC
	 * @param actionId
	 *            The transactionId at which it needs to be determined if the
	 *            defect is a create or update.
	 * @param qcc
	 *            The Connection object
	 * @param latestDefectArtifact
	 *            The GenericArtifact into which the artifactAction is populated
	 *            after it is determined.
	 * @param lastReadTime
	 *            This is synchronization parameter used to compare with the
	 *            defect created date and find out the artifactAction.
	 * @return GenericArtifact Updated artifact
	 */
	/*
	public GenericArtifact getArtifactActionForDefects(
			GenericArtifact latestDefectArtifact, IConnection qcc,
			String syncInfoTransactionId, String actionId, int entityId,
			String lastReadTime) {

		List<GenericArtifactField> genArtifactFields = latestDefectArtifact
				.getAllGenericArtifactFieldsWithSameFieldName("BG_VTS");
		// Date lastReadDate = DateUtil.parse(lastReadTime);
		// Date createdOn = qcGAHelper.getDefectCreatedDate(qcc, entityId);
		if (genArtifactFields != null && genArtifactFields.get(0) != null) {
			String bgVts = qcGAHelper.findVtsFromQC(qcc, Integer
					.parseInt(actionId), entityId);
			Date newBgVts = DateUtil.parseQCDate(bgVts);
			genArtifactFields.get(0).setFieldValue(newBgVts);
			String lastModifiedDate = DateUtil.format(newBgVts);
			latestDefectArtifact.setSourceArtifactLastModifiedDate(lastModifiedDate);
		}
		return latestDefectArtifact;
	}
	 */
	
	
	/**
	 * updates the artifact's last modification date to the one passed into the method.
	 * 
	 * This is necessary, because the modification date may have changed between when
	 * the last transaction to be processed was determined and the point in time when
	 * the artifact is processed.
	 * 
	 * @param artifact the artifact to adjust.
	 * @param lastModifiedDate the date to set for the artifact.
	 */
	public void adjustLastModificationDate(
			GenericArtifact artifact, Date lastModifiedDate, boolean isDefect) {
		String fieldName = isDefect ? "BG_VTS" : "RQ_VTS";
		List<GenericArtifactField> genArtifactFields = artifact
				.getAllGenericArtifactFieldsWithSameFieldName(fieldName);
		if (genArtifactFields != null && genArtifactFields.get(0) != null) {
			genArtifactFields.get(0).setFieldValue(lastModifiedDate);
		}
		String lastModifiedDateStr = DateUtil.format(lastModifiedDate);
		artifact.setSourceArtifactLastModifiedDate(lastModifiedDateStr);
	}

	public List<String> getTransactionIdsInRangeForDefects(IConnection qcc,
			int entityId, int syncInfoTxnId, int actionId, String connectorUser) {

		List<String> listOfTxnIds = new ArrayList<String>();

		String sql = "SELECT AU_ACTION_ID FROM AUDIT_LOG WHERE AU_ACTION_ID > '"
				+ syncInfoTxnId
				+ "' AND AU_ACTION_ID <= '"
				+ actionId
				+ "' AND AU_ACTION!='DELETE' AND AU_ENTITY_TYPE='BUG' AND AU_USER!='"
				+ connectorUser
				+ "' AND AU_FATHER_ID='-1' AND AU_ENTITY_ID='"
				+ entityId + "'";
		IRecordSet newRs = null;
		try {
			newRs = executeSQL(qcc, sql);
			log.debug("The SQL query in getTransactionIdsInRangeForDefects::"
					+ sql);
			int newRc = newRs.getRecordCount();

			for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
				String fieldValue = newRs.getFieldValueAsString("AU_ACTION_ID");
				listOfTxnIds.add(fieldValue);
			}
		} finally {
			if (newRs != null) {
				newRs.safeRelease();
				newRs = null;
			}
		}
		return listOfTxnIds;
	}

	public List<String> getTransactionIdsInRangeForRequirements(
			IConnection qcc, int entityId, int syncInfoTxnId, int actionId,
			String connectorUser) {

		List<String> listOfTxnIds = new ArrayList<String>();

		String sql = "SELECT AU_ACTION_ID FROM AUDIT_LOG WHERE AU_ACTION_ID > '"
				+ syncInfoTxnId
				+ "' AND AU_ACTION_ID <= '"
				+ actionId
				+ "' AND AU_ACTION!='DELETE' AND AU_ENTITY_TYPE='REQ' AND AU_USER!='"
				+ connectorUser
				+ "' AND AU_FATHER_ID='-1' AND AU_ENTITY_ID='"
				+ entityId + "'";
		IRecordSet newRs = null;
		try {
			newRs = executeSQL(qcc, sql);
			log
					.debug("The SQL query in getTransactionIdsInRangeForRequirements::"
							+ sql);
			int newRc = newRs.getRecordCount();

			for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
				String fieldValue = newRs.getFieldValueAsString("AU_ACTION_ID");
				listOfTxnIds.add(fieldValue);
			}
		} finally {
			if (newRs != null) {
				newRs.safeRelease();
				newRs = null;
			}
		}
		return listOfTxnIds;
	}

	public IRecordSet getAuditPropertiesRecordSet(IConnection qcc,
			List<String> txnIds) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID in (");
		int len = txnIds.size();
		for (int cnt = 0; cnt < len; cnt++) {
			if (cnt != (len - 1))
				sql.append("'" + txnIds.get(cnt) + "',");
			else
				sql.append("'" + txnIds.get(cnt) + "'");
		}
		sql.append(")");
		log.debug("New SQL in getDeltaOfComment is:" + sql);
		IRecordSet newRs = executeSQL(qcc, sql.toString());
		return newRs;
	}

	/**
	 * Gets the difference between the comment values of the previous and
	 * current transaction pointed by actionId
	 * 
	 * @param qcc
	 * @param actionId
	 * @return
	 */
	public String getDeltaOfCommentForDefects(IRecordSet newRs) {

		String deltaComment = "";
		String newFieldValue = null;
		String emptyString = "";

		int newRc = newRs.getRecordCount();

		for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
			String fieldName = newRs.getFieldValueAsString("AP_FIELD_NAME");
			if (fieldName.equals(QCConfigHelper.QC_BG_DEV_COMMENTS)) {
				String oldFieldValue = newRs
						.getFieldValueAsString("AP_OLD_LONG_VALUE");
				newFieldValue = newRs
						.getFieldValueAsString("AP_NEW_LONG_VALUE");
				if (!StringUtils.isEmpty(newFieldValue)
						&& !StringUtils.isEmpty(oldFieldValue)) {
					if (newFieldValue.length() > oldFieldValue.length()) {
						String strippedOldValue = this
								.stripStartAndEndTags(oldFieldValue);
						String strippedNewValue = this
								.stripStartAndEndTags(newFieldValue);
						deltaComment += (strippedNewValue
								.substring(strippedOldValue.length()));
					} else if (newFieldValue.length() == oldFieldValue.length()) {
						log.warn("QC comments not changed");
					} else {
						log.warn("New comment is smaller than old comment");
					}
				} else {
					if (!StringUtils.isEmpty(newFieldValue)) {
						deltaComment = newFieldValue;
					}
				}
			}
		}
		if (StringUtils.isEmpty(newFieldValue))
			return emptyString;
		else {
			deltaComment = deltaComment
					.replaceAll(
							"<[fF][oO][Nn][Tt]\\s*[cC][oO][lL][oO][rR]=[\"']#[0-9]{6,6}[\"']><b>_+</b></[fF][oO][Nn][Tt]>",
							emptyString);
			deltaComment = FIRST_TAGS + deltaComment + LAST_TAGS;
			return deltaComment;
		}
	}

	/**
	 * Gets the difference between the comment values of the previous and
	 * current transaction pointed by actionId
	 * 
	 * @param qcc
	 * @param actionId
	 * @return
	 */
	public String getDeltaOfCommentForRequirements(IRecordSet newRs) {

		String deltaComment = "";
		String newFieldValue = null;
		String emptyString = "";

		int newRc = newRs.getRecordCount();

		for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
			String fieldName = newRs.getFieldValueAsString("AP_FIELD_NAME");
			if (fieldName.equals(QCConfigHelper.QC_RQ_DEV_COMMENTS)) {
				String oldFieldValue = newRs
						.getFieldValueAsString("AP_OLD_LONG_VALUE");
				newFieldValue = newRs
						.getFieldValueAsString("AP_NEW_LONG_VALUE");
				if (!StringUtils.isEmpty(newFieldValue)
						&& !StringUtils.isEmpty(oldFieldValue)) {
					if (newFieldValue.length() > oldFieldValue.length()) {
						String strippedOldValue = this
								.stripStartAndEndTags(oldFieldValue);
						String strippedNewValue = this
								.stripStartAndEndTags(newFieldValue);
						deltaComment += (strippedNewValue
								.substring(strippedOldValue.length()));
					} else if (newFieldValue.length() == oldFieldValue.length()) {
						log.warn("QC comments not changed");
					} else {
						log.warn("New comment is smaller than old comment");
					}
				} else {
					if (!StringUtils.isEmpty(newFieldValue)) {
						deltaComment = newFieldValue;
					}
				}
			}
		}
		if (StringUtils.isEmpty(newFieldValue))
			return emptyString;
		else {
			deltaComment = deltaComment
					.replaceAll(
							"<[fF][oO][Nn][Tt]\\s*[cC][oO][lL][oO][rR]=[\"']#[0-9]{6,6}[\"']><b>_+</b></[fF][oO][Nn][Tt]>",
							emptyString);
			deltaComment = FIRST_TAGS + deltaComment + LAST_TAGS;
			return deltaComment;
		}
	}

	private String stripStartAndEndTags(String fieldValue) {
		if (StringUtils.isEmpty(fieldValue)) {
			return "";
		}
		int startTagsIndex = FIRST_TAGS.length();
		int endTagsLength = LAST_TAGS.length();
		String strippedOldValue = fieldValue.substring(startTagsIndex);
		strippedOldValue = strippedOldValue.substring(0, strippedOldValue
				.length()
				- endTagsLength);
		return strippedOldValue;
	}

	public String getOldFieldValue(IRecordSet newRs, String fieldName) {

		int newRc = newRs.getRecordCount();
		String oldFieldValue = null;
		for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) {
			String fieldNameRs = newRs.getFieldValueAsString("AP_FIELD_NAME");
			if (fieldNameRs.equals(fieldName)) {
				oldFieldValue = newRs.getFieldValueAsString("AP_OLD_VALUE");
			}
		}
		return oldFieldValue;
	}

	/**
	 * Returns the value of a particular field in the given GenericArtifact.
	 * 
	 * @param individualGenericArtifact
	 * @param fieldName
	 * @return
	 */
	public String getIntegerValueFromGenericArtifactAsString(
			GenericArtifact individualGenericArtifact, String fieldName) {

		Integer intFieldValue = (Integer) individualGenericArtifact
				.getAllGenericArtifactFieldsWithSameFieldName(fieldName).get(0)
				.getFieldValue();
		String fieldValue = Integer.toString(intFieldValue.intValue());
		return fieldValue;
	}

	/**
	 * For a given transactionId, this returns whether it is a create or update
	 * operation.
	 * 
	 * @param qcc
	 * @param txnId
	 * @return
	 */
	/*
	 * public boolean checkForCreate(IConnection qcc, int txnId) {
	 * 
	 * Boolean check = false; int newRc = 0; String sql =
	 * "SELECT * FROM AUDIT_PROPERTIES WHERE AP_ACTION_ID= '" + txnId + "'";
	 * IRecordSet newRs = null; try { newRs = executeSQL(qcc, sql); if (newRs !=
	 * null) newRc = newRs.getRecordCount();
	 * 
	 * for (int newCnt = 0; newCnt < newRc; newCnt++, newRs.next()) { String
	 * fieldName = newRs.getFieldValueAsString("AP_FIELD_NAME"); String
	 * oldFieldValue = null; if (!(fieldName.equals("BG_DESCRIPTION")))
	 * oldFieldValue = newRs.getFieldValueAsString("AP_OLD_VALUE"); else
	 * oldFieldValue = newRs.getFieldValueAsString("AP_OLD_LONG_VALUE");
	 * 
	 * if (fieldName.equals("BG_VTS") && (oldFieldValue == null ||
	 * (oldFieldValue != null && oldFieldValue .equals("")))) return true; } }
	 * finally { if (newRs != null) { newRs.safeRelease(); newRs = null; } }
	 * return check; }
	 */

	public void deleteDefect(String id) {
		// Yet to implement
	}

	public IQCDefect[] getDefectsWithOtherSystemId(IConnection qcc,
			String otherSystemIdField, String otherSystemIdValue) {
		IBugFactory bugFactory = qcc.getBugFactory();
		IFilter filter = bugFactory.getFilter();
		IFactoryList factoryList;

		log.error("--------------");
		log.error(otherSystemIdField);
		log.error(otherSystemIdValue);
		log.error("--------------");
		filter.setFilter(otherSystemIdField, otherSystemIdValue);
		factoryList = filter.getNewList();

		int factoryListCount = factoryList.getCount();
		IQCDefect[] qcDefectArray = new IQCDefect[factoryListCount];
		for (int i = 1; i <= factoryListCount; ++i) {
			IBug bug = factoryList.getBug(i);
			qcDefectArray[i - 1] = new QCDefect((Bug) bug);
		}
		filter.safeRelease();
		return qcDefectArray;
	}

	public List<ArtifactState> getLatestChangedRequirements(
			IConnection qcc,
			String transactionId,
			String technicalRequirementsId) {
		int rc = 0;
		String sql = "SELECT AL.AU_ENTITY_ID AS AU_ENTITY_ID, AL.AU_ACTION_ID AS AU_ACTION_ID, AL.AU_TIME AS AU_TIME FROM AUDIT_LOG AL, REQ WHERE AL.AU_ENTITY_TYPE = 'REQ' AND AU_ACTION_ID > '"
				+ transactionId
				+ "' AND AL.AU_ACTION!='DELETE' AND AL.AU_FATHER_ID = '-1'"
				+ " AND REQ.RQ_TYPE_ID = '"
				+ technicalRequirementsId
				+ "'"
				+ " AND AL.AU_ENTITY_ID = REQ.RQ_REQ_ID"
				+ " ORDER BY AL.AU_ACTION_ID";

		log.debug(sql);
		ArrayList<ArtifactState> changedRequirements = new ArrayList<ArtifactState>();
		HashMap<String, ArtifactState> artifactIdStateMap = new HashMap<String, ArtifactState>();
		IRecordSet rs = null;
		try {
			rs = executeSQL(qcc, sql);
			if (rs != null)
				rc = rs.getRecordCount();

			for (int cnt = 0; cnt < rc; cnt++, rs.next()) {
				String reqId = rs.getFieldValueAsString("AU_ENTITY_ID");
				String actionIdStr = rs.getFieldValueAsString("AU_ACTION_ID");
				int actionId = Integer.parseInt(actionIdStr);
				String actionDateStr = rs.getFieldValueAsString("AU_TIME");
				Date actionDate = DateUtil.parseQCDate(actionDateStr);
				if (artifactIdStateMap.containsKey(reqId)) {
					ArtifactState state = artifactIdStateMap.get(reqId);
					changedRequirements.remove(state);
					state.setArtifactLastModifiedDate(actionDate);
					state.setArtifactVersion(actionId);
					changedRequirements.add(state);
				} else {
					ArtifactState state = new ArtifactState();
					state.setArtifactId(reqId);
					state.setArtifactLastModifiedDate(actionDate);
					state.setArtifactVersion(actionId);
					changedRequirements.add(state);
					artifactIdStateMap.put(reqId, state);
				}
			}
		} finally {
			if (rs != null) {
				rs.safeRelease();
				rs = null;
			}
		}
		return changedRequirements;
	}

	public IQCRequirement createRequirement(IConnection qcc,
			List<GenericArtifactField> allFields, String connectorUser,
			String targetSystemTimezone, String informalRequirementsType,
			String parentArtifactId) {
		IRequirementsFactory reqFactory = null;
		IRequirement req = null;
		IVersionControl versionControl = null;
		boolean versionControlSupported = false;
		try {
			reqFactory = qcc.getRequirementsFactory();
			if (parentArtifactId != null
					&& !parentArtifactId.equals(GenericArtifact.VALUE_UNKNOWN)
					&& !parentArtifactId.equals(GenericArtifact.VALUE_NONE)) {
				req = reqFactory.addItem(parentArtifactId);
			} else {
				// we will create every requirement under the requirement root
				req = reqFactory.addItem("0");
			}
			req.lockObject();
			versionControl = req.getVersionControlObject();
			if (versionControl != null) {
				versionControlSupported = ccfCheckoutReq(qcc, req, versionControl);
			}

			List<String> allFieldNames = new ArrayList<String>();
			String fieldValue = null;
			
			// make sure the requirement type is known - ccf395
			req.setTypeId(informalRequirementsType);

			for (int cnt = 0; cnt < allFields.size(); cnt++) {

				GenericArtifactField thisField = allFields.get(cnt);
				String fieldName = thisField.getFieldName();
				if (thisField.getFieldValueType().equals(
						GenericArtifactField.FieldValueTypeValue.DATE)
						|| thisField
								.getFieldValueType()
								.equals(
										GenericArtifactField.FieldValueTypeValue.DATETIME))
					fieldValue = getProperFieldValue(thisField,
							targetSystemTimezone);
				else
					fieldValue = (String) thisField.getFieldValue();

				if (fieldName.equals(QCConfigHelper.QC_RQ_DEV_COMMENTS)) {
					String oldFieldValue = req.getFieldAsString(fieldName);
					if ((!StringUtils.isEmpty(oldFieldValue)
							&& !StringUtils.isEmpty(fieldValue) && !oldFieldValue
							.equals(fieldValue))
							|| (StringUtils.isEmpty(oldFieldValue) && !StringUtils
									.isEmpty(fieldValue))) {
						fieldValue = getConcatinatedCommentValue(oldFieldValue,
								fieldValue, connectorUser);
					}
				}

				if (!(allFieldNames.contains(allFields.get(cnt).getFieldName()))
						&& !(fieldName.equals(QC_REQ_ID)
								|| fieldName.equals(QC_RQ_ATTACHMENT)
								|| fieldName.equals(QC_RQ_VTS)
								|| fieldName.endsWith(QCConfigHelper.HUMAN_READABLE_SUFFIX))) {
					try {
						if("RQ_TARGET_REL".equals(fieldName) || "RQ_TARGET_RCYC".equals(fieldName)) {
							// hard-code the linked fields here
							if(fieldValue == null || fieldValue.trim().length() == 0) {
								req.clearListValuedField(fieldName);
							} else {
								req.setListValuedField(fieldName, fieldValue);
							}
						} else { 
							req.setField(fieldName, fieldValue);
						}
					} catch (Exception e) {
						String message = "Exception while setting the value of field "
								+ fieldName
								+ " to "
								+ fieldValue
								+ ": "
								+ e.getMessage();
						log.error(message, e);
						throw new CCFRuntimeException(message, e);
					}
				} else {
					log.debug(fieldName);
				}
				if (!fieldName.equals(QCConfigHelper.QC_RQ_DEV_COMMENTS))
					allFieldNames.add(fieldName);
			}
			// set the requirement type again here (why?)
			req.setTypeId(informalRequirementsType);
			req.post();
		} catch (Exception e) {
			String reqId = null;
			if (req != null) {
				reqId = req.getId();
				reqFactory.removeItem(reqId);
				req = null;
			}
			String message = "Exception while creating requirement " + reqId;
			log.error(message, e);
			throw new CCFRuntimeException(message + ": " + e.getMessage(), e);
		} finally {
			if (versionControlSupported) {
				try {
					versionControl.checkIn("CCF CheckIn");
					versionControl.safeRelease();
				} catch (Exception e) {
					String message = "Failed to checkin requirement "
							+ req.getId() + " again";
					log.error(message, e);
					req.unlockObject();
					throw new CCFRuntimeException(message, e);
				}
			}
			if (req != null) {
				req.unlockObject();
			}
			reqFactory = null;
		}

		return new QCRequirement((Requirement) req);
	}
}
