package com.collabnet.ccf.rqp;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.AbstractWriter;
import com.collabnet.ccf.core.ArtifactState;
import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldActionValue;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.rqp.enums.RQPRequirementField;
import com.collabnet.ccf.rqp.enums.RQPType;
import com.rational.reqpro.rpx._Attr;
import com.rational.reqpro.rpx._AttrValue;
import com.rational.reqpro.rpx._AttrValues;
import com.rational.reqpro.rpx._Attrs;
import com.rational.reqpro.rpx._ListItemValue;
import com.rational.reqpro.rpx._Package;
import com.rational.reqpro.rpx._Relationship;
import com.rational.reqpro.rpx._ReqType;
import com.rational.reqpro.rpx._ReqTypes;
import com.rational.reqpro.rpx._Requirement;
import com.rational.reqpro.rpx._Requirements;
import com.rational.reqpro.rpx._Revision;
import com.rational.reqpro.rpx._Revisions;
import com.rational.reqpro.rpx._RootPackage;
import com.rational.reqpro.rpx.enumAttrDataTypes;
import com.rational.reqpro.rpx.enumAttrLookups;
import com.rational.reqpro.rpx.enumAttrValueLookups;
import com.rational.reqpro.rpx.enumElementTypes;
import com.rational.reqpro.rpx.enumListItemValueLookups;
import com.rational.reqpro.rpx.enumPackageLookups;
import com.rational.reqpro.rpx.enumReqTypesLookups;
import com.rational.reqpro.rpx.enumRequirementFlags;
import com.rational.reqpro.rpx.enumRequirementLookups;
import com.rational.reqpro.rpx.enumRequirementsLookups;
import com.rational.reqpro.rpx.enumRequirementsWeights;
import com.rational.reqpro.rpx.enumRevisionLookups;
import com.rational.rjcb.ComException;

public class RQPHandler {

	private static final int MAXIMUM_LOOKUP_ATTEMPTS = 1000;
	private static final String NONE = "None";
	private static final String EMPTY_VALUE = "";
	private static final String REQUISITE_PRO_COMMENT_KEY = "COMMENTS: ";
	private static final String PLANNING_FOLDER_PREFIX = "plan";
	private static final String BUSINESS_VALUE_DEFAULT = "3- Medium";
	private static final Log log = LogFactory.getLog(RQPHandler.class);
	private static final Log logConflictResolutor = LogFactory.getLog("com.collabnet.ccf.core.conflict.resolution");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public void getRequirement(RQPConnection connection, String packageName, String projectName, String rqpItemType,
			Date lastModifiedDate, String lastSynchronizedVersion, String lastSynchedArtifactId, String artifactId,
			String userName, boolean ignoreConnectorUserUpdates, GenericArtifact genericArtifact,
			String sourceRepositoryId) throws IOException {

		_Requirement req = connection.getProjectConnection().GetRequirement(artifactId,
				enumRequirementLookups.eReqLookup_Key, enumRequirementsWeights.eReqWeight_Heavy,
				enumRequirementFlags.eReqFlag_Refresh);

		_Revisions revs = req.getRevisions();
		_Revision firstRevision = revs.getItem(new Integer(revs.getCount()), enumRevisionLookups.eRevLookup_Index);
		_Revision lastRevision = revs.getItem(new Integer(1), enumRevisionLookups.eRevLookup_Index);

		Date creationDate = null;
		Date modifiedDate = null;

		try {
			creationDate = this.dateFormat.parse(firstRevision.getVersionDateTime());
			modifiedDate = this.dateFormat.parse(lastRevision.getVersionDateTime());
		} catch (ParseException e) {
			log.fatal("Unable to parse ReqPro dates");
		}
		String lastModifiedBy = lastRevision.getVersionUser().getName();
		String revisionNumber = String.valueOf(RQPMetaData.processRQPVersionNumber(req.getVersionNumber()));

		boolean isResync = false;
		boolean isIgnore = false;

		if (lastModifiedBy.equalsIgnoreCase(userName) && ignoreConnectorUserUpdates) {
			if (creationDate.after(lastModifiedDate)) {
				log.info(String.format(
						"resync is necessary, despite the artifact %s last being updated by the connector user",
						artifactId));
				isResync = true;
			} else {
				log.info(String.format("artifact %s is an ordinary connector update, ignore it.", artifactId));
				isIgnore = true;
			}
		}

		try {

			_AttrValues attrs = req.getAttrValues();

			for (int i = 0; i < attrs.getCount(); i++) {
				_AttrValue attr = attrs.getItem(new Integer(i), enumAttrLookups.eAttrLookups_Index);
				if (RQPRequirementField.COMMENTS.getName().equals(attr.getLabel())) {
					String commentDiff = getCommentDiff(revs, lastRevision, req);
					if (commentDiff != null) {

						String label = attr.getLabel();
						GenericArtifactField gaField = genericArtifact.addNewField(label,
								GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
						gaField.setFieldValueType(RQPMetaData.translateRQPFieldValueTypeToCCFFieldValueType(attr
								.getDataType()));
						gaField.setFieldAction(FieldActionValue.REPLACE);
						gaField.setFieldValue(commentDiff);

					}
				} else {
					this.addRequirementAttributes(genericArtifact, attr, userName);
				}
			}
			this.addRequirementField(genericArtifact, RQPRequirementField.NAME.getName(), req.getName(),
					enumAttrDataTypes.eAttrDataTypes_Text, GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
			this.addRequirementField(genericArtifact, RQPRequirementField.TEXT.getName(), req.getText(),
					enumAttrDataTypes.eAttrDataTypes_Text, GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
			this.addRequirementField(genericArtifact, RQPRequirementField.REQPRO_ID.getName(),
					String.valueOf(req.getKey()), enumAttrDataTypes.eAttrDataTypes_Text,
					GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (isIgnore) {
			genericArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.IGNORE);
		} else {
			if (isResync) {
				genericArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.RESYNC);
			}
		}

		genericArtifact.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df.format(modifiedDate));
		genericArtifact.setSourceArtifactVersion(revisionNumber);

		_Relationship relationship = req.getParent(enumRequirementsWeights.eReqWeight_Heavy);

		if (relationship != null) {
			_Requirement parentReq = relationship.getSourceRequirement(enumRequirementsWeights.eReqWeight_Heavy);
			if (parentReq != null) {

				String parentSourceRepositoryId = sourceRepositoryId.substring(0, sourceRepositoryId.lastIndexOf("-"))
						+ "-" + parentReq.getReqType().getReqPrefix();

				genericArtifact.setDepParentSourceRepositoryId(parentSourceRepositoryId);
				genericArtifact.setDepParentSourceArtifactId(String.valueOf(parentReq.getKey()));
			}
		} else {
			// In this case, when there is no parent associated, the package which is above in hierarchy will
			// be taken as parent
			_Package parentPackage = getPackage(req.getPackage().getName(), connection, req.getPackage().GetHierarchyPathName(),false);
			String parentSourceRepositoryId = sourceRepositoryId.substring(0, sourceRepositoryId.lastIndexOf("-")) + "-" + RQPType.PACKAGE;
			
			String parentKey;
			if(parentPackage != null){
				parentKey = String.valueOf(req.getPackageKey());
			}else{
				parentKey = String.valueOf(connection.getProjectConnection().GetRootPackage(false).getKey());
			}
			genericArtifact.setDepParentSourceRepositoryId(parentSourceRepositoryId);			
			genericArtifact.setDepParentSourceArtifactId(parentKey);
			
		}

	}

	private String getCommentDiff(_Revisions revs, _Revision lastRevision, _Requirement req) throws IOException {
		String lastVersionReason = lastRevision.getVersionReason();
		String currentComment = "";
		_AttrValues attrs = req.getAttrValues();

		for (int i = 0; i < attrs.getCount(); i++) {
			_AttrValue attr = attrs.getItem(new Integer(i), enumAttrLookups.eAttrLookups_Index);
			if (RQPRequirementField.COMMENTS.getName().equals(attr.getLabel())) {
				currentComment = attr.getText();
				break;
			}
		}
		if (currentComment != null && currentComment.isEmpty()) {
			return null;
		}
		if (!lastVersionReason.contains(REQUISITE_PRO_COMMENT_KEY)) {
			return null;
		}
		String lastComment = null;
		String revisionReason = null;
		try {
			// Reading from newer revision by older revision up to found the
			// last comment made
			Integer limit = revs.getCount();
			for (int index = 2; index <= limit; index++) {
				_Revision revision = revs.getItem(new Integer(index), enumRevisionLookups.eRevLookup_Index);
				revisionReason = revision.getVersionReason();
				if (revisionReason.contains(REQUISITE_PRO_COMMENT_KEY)) {
					break;
				} else {
					revisionReason = null;
				}
			}
			// if there are no comments on revisions
			if (revisionReason == null) {
				return currentComment;
			}
			String previousComment = revisionReason.substring(revisionReason.indexOf(REQUISITE_PRO_COMMENT_KEY)
					+ REQUISITE_PRO_COMMENT_KEY.length());
			lastComment = currentComment.replaceFirst(previousComment, EMPTY_VALUE);
		} catch (IOException e) {
			return currentComment;
		}
		return lastComment;
	}

	private void addRequirementAttributes(GenericArtifact genericArtifact, _AttrValue attr, String userName)
			throws IOException {
		String label = attr.getLabel();
		GenericArtifactField gaField = genericArtifact.addNewField(label,
				GenericArtifactField.VALUE_FIELD_TYPE_FLEX_FIELD);
		gaField.setFieldValueType(RQPMetaData.translateRQPFieldValueTypeToCCFFieldValueType(attr.getDataType()));
		gaField.setFieldAction(FieldActionValue.REPLACE);
		String value = (attr.getText() != null) ? attr.getText() : NONE;
		gaField.setFieldValue(value);
	}

	private void addRequirementField(GenericArtifact genericArtifact, String label, String value, int dataType,
			String valueFieldType) throws IOException {

		GenericArtifactField gaField = genericArtifact.addNewField(label, valueFieldType);
		gaField.setFieldValueType(RQPMetaData.translateRQPFieldValueTypeToCCFFieldValueType(dataType));
		gaField.setFieldAction(FieldActionValue.REPLACE);
		gaField.setFieldValue(value);
	}

	public _Requirement createRequirement(GenericArtifact ga, String packageName, String projectName,
			String rqpItemType, RQPConnection connection, String userName, RQPType rqpType) {

		if (RQPType.contains(rqpType)) {
			boolean workItemWasCreated = true;

			_Requirement newReq = null;

			String reqName = ga.getAllGenericArtifactFieldsWithSameFieldName(RQPRequirementField.NAME.getName()).get(0)
					.getFieldValue().toString();

			String reqText = EMPTY_VALUE;
			if (ga.getAllGenericArtifactFieldsWithSameFieldName(RQPRequirementField.TEXT.getName()) != null) {
				reqText = ga.getAllGenericArtifactFieldsWithSameFieldName(RQPRequirementField.TEXT.getName()).get(0)
						.getFieldValue().toString();
			}

			String attrName = EMPTY_VALUE;

			try {
				newReq = connection.getProjectConnection().CreateRequirement(reqName, reqText, rqpItemType,
						enumReqTypesLookups.eReqTypesLookups_Prefix, EMPTY_VALUE, EMPTY_VALUE, new Integer(0), // vParentReqLookupValue
						enumRequirementLookups.eReqLookup_Empty);

				_ReqTypes reqTypes = connection.getProjectConnection().getReqTypes();
				_ReqType reqType = reqTypes.getItem(rqpItemType, enumReqTypesLookups.eReqTypesLookups_Prefix);

				_Attrs attrs = reqType.getAttrs();
				int attrsLength = attrs.getCount();

				for (int i = 1; i <= attrsLength; i++) {

					_Attr attr = attrs.getItem(i, enumReqTypesLookups.eReqTypesLookups_Index);
					attrName = attr.getLabel();

					int dataTypeEnum = attr.getDataType();
					List<GenericArtifactField> gaFields = ga.getAllGenericArtifactFieldsWithSameFieldName(attrName);

					if (gaFields != null) {

						Object fieldValue = gaFields.get(0).getFieldValue();

						_AttrValue attrVal = newReq.getAttrValue(attrName, enumAttrValueLookups.eAttrValueLookup_Label);

						if (dataTypeEnum == enumAttrDataTypes.eAttrDataTypes_Text) {

							attrVal.setText(String.valueOf(fieldValue));

						} else if (dataTypeEnum == enumAttrDataTypes.eAttrDataTypes_List) {

							if (RQPRequirementField.BUSINESS_VALUE.getName().equals(attrName)
									&& NONE.equals(fieldValue)) {
								fieldValue = BUSINESS_VALUE_DEFAULT;
							}

							_ListItemValue listItemVal = attrVal.getListItemValue(String.valueOf(fieldValue),
									enumListItemValueLookups.eListItemValueLookup_Text);
							listItemVal.setSelected(true);

						} else if (dataTypeEnum == enumAttrDataTypes.eAttrDataTypes_Integer) {
							attrVal.setText(String.valueOf(fieldValue));
						}
					}
				}

				_Package pack = this.getPackage(packageName, connection);
				pack.AddElement(newReq, enumPackageLookups.ePackageLookup_Object,
						enumElementTypes.eElemType_Requirement);

				newReq.Save();

				if (ga.getDepParentSourceArtifactId() != GenericArtifact.VALUE_UNKNOWN
						&& ga.getDepParentSourceArtifactId() != GenericArtifact.VALUE_NONE) {

					connection.getProjectConnection().setLogRelationshipRevisions(true);
					_Requirement parentReq = connection.getProjectConnection().GetRequirement(
							ga.getDepParentTargetArtifactId(), enumRequirementLookups.eReqLookup_Key,
							enumRequirementsWeights.eReqWeight_Heavy, enumRequirementFlags.eReqFlag_Refresh);

					newReq.AssignParent(parentReq.getKey(), enumReqTypesLookups.eReqTypesLookups_Key);
					parentReq.Save();
					newReq.Save();
				}

			} catch (IOException e) {
				logConflictResolutor.warn("Error Creating new requirement (" + attrName + "): ", e);
				workItemWasCreated = false;
			}

			if (workItemWasCreated) {
				try {

					ga.setTargetArtifactVersion(String.valueOf(RQPMetaData.processRQPVersionNumber(newReq.getVersionNumber())));
					ga.setTargetArtifactLastModifiedDate(GenericArtifactHelper.df.format(this.dateFormat.parse(newReq
							.getVersionDateTime())));
					ga.setTargetArtifactId(String.valueOf(newReq.getKey()));

				} catch (ParseException e) {
					throw new CCFRuntimeException("Error updating generic artifact date version");
				} catch (IOException e) {
					throw new CCFRuntimeException("Error updating generic artifact version");
				}
			}
			return newReq;
		} else {
			String cause = "Unsupported repository format";
			log.error(cause);
			throw new CCFRuntimeException(cause);
		}

	}

	private _Package getPackage(String packageName, RQPConnection connection) throws IOException {
		boolean notFound = true;
		int packageId = 2;
		_Package pack = null;
		
		while (notFound && packageId < MAXIMUM_LOOKUP_ATTEMPTS) {
			
			pack = connection.getProjectConnection().GetPackage(packageId, enumPackageLookups.ePackageLookup_Key);
			if (pack != null && packageName.equals(pack.getName())) {
				notFound = false;
			}
			packageId++;
		}
		return pack;
	}
	
	private _Package getPackage(String packageName, RQPConnection connection, String packagePath, boolean modifyPath) throws IOException {
		boolean notFound = true;
		int packageId = 2;
		_Package pack = null;
		String cleanPath = packagePath;
		
		if(modifyPath && packagePath.lastIndexOf('\\') != -1){
			cleanPath = packagePath.substring(0, packagePath.lastIndexOf('\\'));
		}
		
		while (notFound && packageId < MAXIMUM_LOOKUP_ATTEMPTS) {
			
			pack = connection.getProjectConnection().GetPackage(packageId, enumPackageLookups.ePackageLookup_Key);
			if (pack != null && packageName.equals(pack.getName()) && pack.GetHierarchyPathName().equalsIgnoreCase(cleanPath)) {
				notFound = false;
			}
			packageId++;
		}
		return pack;
	}	

	public _Requirement updateRequirement(GenericArtifact ga, String packageName, String projectName,
			String rqpItemType, RQPConnection connection, String userName) {

		String artifactId = ga.getTargetArtifactId();

		_Requirement req;

		try {
			req = connection.getProjectConnection().GetRequirement(artifactId, enumRequirementLookups.eReqLookup_Key,
					enumRequirementsWeights.eReqWeight_Heavy, enumRequirementFlags.eReqFlag_Refresh);

			Long requirementRevision;

			try {
				requirementRevision = RQPMetaData.processRQPVersionNumber(req.getVersionNumber());
			} catch (Exception e) {
				throw new CCFRuntimeException("Error retrieving the requirement version number");
			}

			if (!AbstractWriter.handleConflicts(requirementRevision, ga)) {
				return null;
			}

			if (ga.getAllGenericArtifactFieldsWithSameFieldName(RQPRequirementField.NAME.getName()) != null) {
				String reqName = ga.getAllGenericArtifactFieldsWithSameFieldName(RQPRequirementField.NAME.getName())
						.get(0).getFieldValue().toString();
				req.setName(reqName);
			}

			if (ga.getAllGenericArtifactFieldsWithSameFieldName(RQPRequirementField.TEXT.getName()) != null) {
				req.setText(ga.getAllGenericArtifactFieldsWithSameFieldName(RQPRequirementField.TEXT.getName()).get(0)
						.getFieldValue().toString());
			}

			if (!AbstractWriter.handleConflicts(RQPMetaData.processRQPVersionNumber(req.getVersionNumber()), ga)) {
				return null;
			}

		} catch (IOException e) {
			throw new CCFRuntimeException("Error retrieving the requirement");
		}

		boolean requirementWasUpdated = false;
		_ReqTypes reqTypes;

		try {

			reqTypes = connection.getProjectConnection().getReqTypes();
			_ReqType reqType = reqTypes.getItem(rqpItemType, enumReqTypesLookups.eReqTypesLookups_Prefix);
			_Attrs attrs = reqType.getAttrs();

			while (!requirementWasUpdated) {

				requirementWasUpdated = true;

				int attrsLength = attrs.getCount();
				for (int i = 1; i <= attrsLength; i++) {

					_Attr attr = attrs.getItem(i, enumReqTypesLookups.eReqTypesLookups_Index);
					String attrName = attr.getLabel();

					List<GenericArtifactField> gaFields = ga.getAllGenericArtifactFieldsWithSameFieldName(attrName);

					if (gaFields != null && gaFields.get(0).getFieldValueHasChanged()) {

						boolean shouldBeOverwritten = true;
						Object fieldValue = gaFields.get(0).getFieldValue();
						int dataTypeEnum = attr.getDataType();
						_AttrValue attrVal = req.getAttrValue(attrName, enumAttrValueLookups.eAttrValueLookup_Label);

						if (shouldBeOverwritten) {

							if (dataTypeEnum == enumAttrDataTypes.eAttrDataTypes_Text) {

								String oldComments = attrVal.getText();
								attrVal.setText(String.valueOf(fieldValue));

								if (attrName.equalsIgnoreCase("Implemented in Release") && (fieldValue == null)) {
									attrVal.setText(EMPTY_VALUE);
								}

								if (attrName.equalsIgnoreCase(RQPRequirementField.COMMENTS.getName())) {
									String newComment = ga
											.getAllGenericArtifactFieldsWithSameFieldName(
													RQPRequirementField.COMMENTS.getName()).get(0).getFieldValue()
											.toString();
									String comments = (newComment + "; " + oldComments).replace("\n", " ");
									attrVal.setText(comments);
								}

							} else if (dataTypeEnum == enumAttrDataTypes.eAttrDataTypes_List) {
								_ListItemValue listItemVal = attrVal.getListItemValue(String.valueOf(fieldValue),
										enumListItemValueLookups.eListItemValueLookup_Text);
								listItemVal.setSelected(true);
							} else if (dataTypeEnum == enumAttrDataTypes.eAttrDataTypes_Integer) {
								attrVal.setText(String.valueOf(fieldValue));
							}
						}
					}

				}

				if (isAValidParent(ga.getDepParentSourceArtifactId())) {

					connection.getProjectConnection().setLogRelationshipRevisions(true);
					_Requirement parentReq = connection.getProjectConnection().GetRequirement(
							ga.getDepParentTargetArtifactId(), enumRequirementLookups.eReqLookup_Key,
							enumRequirementsWeights.eReqWeight_Heavy, enumRequirementFlags.eReqFlag_Refresh);

					int[] parents = new int[1];
					parents[0] = parentReq.getKey();
					if (!req.isHasParent(parents)) {
						req.AssignParent(parentReq.getKey(), enumReqTypesLookups.eReqTypesLookups_Key);
						parentReq.Save();
					}
				} else {
					// In case the requirement used to be a parent but It
					// doesn't have It anymore, the dependency must be deleted
					if (req.getParent(new Integer(0)) != null) {
						// Assigning 0 as parent requirement means that there is
						// no parent
						req.AssignParent(new Integer(0), enumReqTypesLookups.eReqTypesLookups_Empty);
					}
				}

				req.Save();
			}

		} catch (IOException e) {
			logConflictResolutor.warn("Stale requirement update, trying again ...:", e);
			requirementWasUpdated = false;
		}

		try {

			ga.setTargetArtifactVersion(String.valueOf(RQPMetaData.processRQPVersionNumber(req.getVersionNumber())));
			ga.setTargetArtifactLastModifiedDate(GenericArtifactHelper.df.format(this.dateFormat.parse(req
					.getVersionDateTime())));

		} catch (Exception e) {

			throw new CCFRuntimeException("Error updating artifact version", e);
		}
		return req;
	}

	/**
	 * Checks whether the parent id of and object is a valid artifact Cases:
	 * unknown & none: meaning that the object doesn't have a parent parent
	 * starting with 'plan': meaning that the object is inside a TF planning
	 * folder
	 * 
	 * @param depParentSourceArtifactId
	 * @return
	 */
	private boolean isAValidParent(String depParentSourceArtifactId) {
		return !GenericArtifact.VALUE_UNKNOWN.equalsIgnoreCase(depParentSourceArtifactId)
				&& !GenericArtifact.VALUE_NONE.equalsIgnoreCase(depParentSourceArtifactId)
				&& !depParentSourceArtifactId.startsWith(PLANNING_FOLDER_PREFIX);
	}

	/**
	 * Obtains a list of state changes in requirements. It takes into account
	 * the hole group of objects, It doesn't matter the folder they belong to
	 * 
	 * @param connection
	 * @param rqpItemType
	 * @param lastModifiedDate
	 * @param lastSynchronizedVersion
	 * @param lastSynchedArtifactId
	 * @param artifactStates
	 * @param userName
	 * @param ignoreConnectorUserUpdates
	 * @param packageName
	 */
	public void getChangedRequirements(RQPConnection connection, String rqpItemType, Date lastModifiedDate,
			String lastSynchronizedVersion, String lastSynchedArtifactId, ArrayList<ArtifactState> artifactStates,
			String userName, boolean ignoreConnectorUserUpdates, String packageName) {

		_Requirements reqs;

		boolean duplicateFound = false;

		ArrayList<_Requirement> detailRowsFull = new ArrayList<_Requirement>();
		ArrayList<_Requirement> detailRowsNew = new ArrayList<_Requirement>();

		try {

			reqs = connection.getProjectConnection().GetRequirements(rqpItemType,
					enumRequirementsLookups.eReqsLookup_TagPrefix, enumRequirementsWeights.eReqWeight_Heavy,
					enumRequirementFlags.eReqFlag_Empty, 100, 100);
			
			_Requirement req;

			for (int i = 1; i <= reqs.getCount(); i++) {

				 req = reqs.getItem(i, enumReqTypesLookups.eReqTypesLookups_Index);

				Date lastModifiedDateToSync = this.dateFormat.parse(req.getVersionDateTime());
				boolean requirementHasChanged = lastModifiedDate.before(lastModifiedDateToSync);

				if (requirementHasChanged) {
					detailRowsFull.add(req);
				}
			}

			req = null;
			
			List<_Requirement> reqList = duplicateFound ? detailRowsNew : detailRowsFull;
			for (_Requirement listReq : reqList) {
				ArtifactState artifactState = new ArtifactState();
				artifactState.setArtifactId(String.valueOf(listReq.getKey()));

				artifactState.setArtifactLastModifiedDate(this.dateFormat.parse(listReq.getVersionDateTime()));

				artifactState.setArtifactVersion(RQPMetaData.processRQPVersionNumber(listReq.getVersionNumber()));
				artifactStates.add(artifactState);
			}

		} catch (ParseException e) {
			logConflictResolutor.warn("Error parsing requirement last modification date, trying again ...:", e);
			e.printStackTrace();
		} catch (IOException e) {
			logConflictResolutor.warn("Error retrieving requirements, trying again ...:", e);
		} finally {
			reqs = null;
		}
		

	}

	/**
	 * Retrieves a list of packages that will be synchronized to artifacts tracker. They will be stored in artifactStates parameter
	 * 
	 * @param connection
	 * @param lastModifiedDate
	 * @param lastSynchronizedVersion
	 * @param lastSynchedArtifactId
	 * @param artifactStates
	 * @param userName
	 * @param ignoreConnectorUserUpdates
	 */
	public void getChangedPackages(RQPConnection connection, Date lastModifiedDate, String lastSynchronizedVersion,
			String lastSynchedArtifactId, ArrayList<ArtifactState> artifactStates, String userName,
			boolean ignoreConnectorUserUpdates) {
		ArrayList<_Package> detailRowsFull = new ArrayList<_Package>();
		ArrayList<ArtifactState> orderedArtifactStates = new ArrayList<ArtifactState>();
		ArrayList<ArtifactState> orphanArtifactStates = new ArrayList<ArtifactState>();
		try {

			_RootPackage rootPackage = connection.getProjectConnection().GetRootPackage(true);
			int count = rootPackage.getCountWithChildren(enumPackageLookups.ePackageLookup_Key, true) + 1;
			int notFoundPackages = 0;
			boolean isRootPackage = true;
			//TODO Look for a better way of counting the amount of packages
			for(int j=1;j <= count ;j++){
				ArtifactState artifactState = new ArtifactState();
				_Package rqpPackage = null;
				_RootPackage firstPackage;
				_Revisions revisions;
				
				if(isRootPackage){
					firstPackage = connection.getProjectConnection().GetRootPackage(false);
					artifactState.setArtifactId(String.valueOf(firstPackage.getKey()));
					artifactState.setArtifactVersion(RQPMetaData.processRQPVersionNumber(connection.getProjectConnection().getVersionNumber()));
					revisions = firstPackage.getProject().getRevisions();
					_Revision item;
					item = revisions.getItem(revisions.getCount(), enumRevisionLookups.eRevLookup_Index);
					artifactState.setArtifactLastModifiedDate(this.dateFormat.parse(item.getVersionDateTime()));
					orderedArtifactStates.add(artifactState);
					isRootPackage = false;
				}else{
					rqpPackage = connection.getProjectConnection().GetPackage(j, enumPackageLookups.ePackageLookup_Index);
					if(rqpPackage != null){
						String parentDependency = getParentPackage(rootPackage.getName(), rqpPackage);
						_Package parentPackage = getPackage(parentDependency, connection,rqpPackage.GetHierarchyPathName(),true);
						//Check if the parent package has already been created
						int parentKey;
						if(parentPackage == null){
							parentKey = connection.getProjectConnection().GetRootPackage(false).getKey();
						}else{
							parentKey = parentPackage.getKey();
						}
						detailRowsFull.add(rqpPackage);
						if(rqpPackage.isHasChildren()){
							count+= rqpPackage.getCountWithChildren(enumPackageLookups.ePackageLookup_Key, true);
						}
						_Revision item;
						revisions = rqpPackage.getProject().getRevisions();
						try {
							item = revisions.getItem(revisions.getCount(), enumRevisionLookups.eRevLookup_Index);
						} catch (ComException e) {
							j++;
							logConflictResolutor.warn("Error retrieving requirements, trying again ...:", e);
							continue;
						}
						
						artifactState.setArtifactId(String.valueOf(rqpPackage.getKey()));
						artifactState.setArtifactVersion(RQPMetaData.processRQPVersionNumber(connection.getProjectConnection().getVersionNumber()));
						artifactState.setArtifactLastModifiedDate(this.dateFormat.parse(item.getVersionDateTime()));
							
						if(parentKey < rqpPackage.getKey()){
							orderedArtifactStates.add(artifactState);
						}else{
							//In this case, parent package has not been created yet, so the artifact is stored in another group, in order to be
							//synchronized later
							orphanArtifactStates.add(artifactState);
						}
					}else{
						notFoundPackages ++;
						if(notFoundPackages < 100){
							count+=20;
						}
					}
					
				}
			}
			
			if(!orphanArtifactStates.isEmpty()){
				orderedArtifactStates.addAll(orderedArtifactStates.size(),orphanArtifactStates);
			}
			
			artifactStates.addAll(orderedArtifactStates);
			
		} catch (IOException e) {
			logConflictResolutor.warn("Error retrieving requirements, trying again ...:", e);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		
	}

	/**
	 * Retrieves all information about a given package and stores the output in the genericArtifact taken as parameter
	 * 
	 * @param connection
	 * @param artifactId
	 * @param genericArtifact
	 * @param sourceRepositoryId
	 */
	public  void getPackage(RQPConnection connection, String artifactId, GenericArtifact genericArtifact, String sourceRepositoryId) {
		_Package rqpPackage = null;
		_Revisions revs;
		_Revision lastRevision;
		Date modifiedDate = null;
		String parentDependency, name, description,revisionNumber;
		boolean isRootPackage = Integer.valueOf(artifactId) == 1;
		
		try {
			_RootPackage rootPackage = connection.getProjectConnection().GetRootPackage(false);
			if(isRootPackage){
				//Root package
				name = rootPackage.getName();
				description = rootPackage.getDescription();
				revs = rootPackage.getProject().getRevisions();
			}else{
				//Common packages
				rqpPackage = connection.getProjectConnection().GetPackage(Integer.parseInt(artifactId), enumPackageLookups.ePackageLookup_Key);
				name = rqpPackage.getName();
				description = rqpPackage.getDescription();
				revs = rqpPackage.getProject().getRevisions();
				
				parentDependency = getParentPackage(rootPackage.getName(), rqpPackage);
				_Package parentPackage = getPackage(parentDependency, connection,rqpPackage.GetHierarchyPathName(),true);
				String parentSourceRepositoryId = sourceRepositoryId.substring(0, sourceRepositoryId.lastIndexOf("-")) + "-" + RQPType.PACKAGE;
				genericArtifact.setDepParentSourceRepositoryId(parentSourceRepositoryId);
				if(parentPackage != null){
					genericArtifact.setDepParentSourceArtifactId(String.valueOf(parentPackage.getKey()));
				}else{
					//In this case the parent of the artifact is root package
					genericArtifact.setDepParentSourceArtifactId(String.valueOf(rootPackage.getKey()));
				}
			}
				
			revisionNumber = String.valueOf(new Integer(-1));
			lastRevision = revs.getItem(new Integer(1), enumRevisionLookups.eRevLookup_Index);
				
			try {
				modifiedDate = this.dateFormat.parse(lastRevision.getVersionDateTime());
			} catch (ParseException e) {
				log.fatal("Unable to parse ReqPro dates");
			}
			
			genericArtifact.setSourceArtifactLastModifiedDate(GenericArtifactHelper.df.format(modifiedDate));
			genericArtifact.setSourceArtifactVersion(revisionNumber);
			this.addRequirementField(genericArtifact, RQPRequirementField.NAME.getName(), name,
					enumAttrDataTypes.eAttrDataTypes_Text, GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);
			this.addRequirementField(genericArtifact, RQPRequirementField.DESCRIPTION.getName(), description,
					enumAttrDataTypes.eAttrDataTypes_Text, GenericArtifactField.VALUE_FIELD_TYPE_MANDATORY_FIELD);			
			
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * Obtains the name of the parent of a given package, taking into account It is next to package name in path
	 * Example:
	 * input: Parent Root Package/Mock Package/Package
	 * output: Mock Package
	 * 
	 * @param rootPackageName
	 * @param rqpPackage
	 * @return
	 * @throws IOException
	 */
	private String getParentPackage(String rootPackageName, _Package rqpPackage) throws IOException {
		String parentPackageName;
		String[] packagePath = rqpPackage.GetHierarchyPathName().split("\\\\");
		boolean rootPackageIsParent = packagePath.length == 2;
		
		if(rootPackageIsParent){
			parentPackageName = rootPackageName;
		}else{
			parentPackageName = packagePath[packagePath.length - 2];
		}
		return parentPackageName;
	}

}
