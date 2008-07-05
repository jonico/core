package com.collabnet.ccf.core.ga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * This is the root element for every entity that is transported through the CCF
 * pipeline. If the artifact's mode is not "attachment", the element will
 * typically only contain field-sub-elements. If it is an attachment, it may
 * also contain a BASE64 encoded text-string, describing the content of the
 * attachment. The content of an attachment does not have to be shipped if the
 * artifactMode is set to "changedFieldsOnly".
 * 
 * @author jnicolai
 */
public class GenericArtifact {

	/**
	 * This is a hash map that indexes all contained field elements according to
	 * their name
	 */
	private HashMap<String, List<GenericArtifactField>> fieldNameHashMap = new HashMap<String, List<GenericArtifactField>>();
	
	/**
	 * 
	 * Possible values for the artifact mode, "changedFieldsOnly" and "complete"
	 * 
	 * @author jnicolai
	 */
	public enum ArtifactModeValue {
		CHANGEDFIELDSONLY, COMPLETE, UNKNOWN
	};

	/**
	 * 
	 * Possible values for the artifact action, "create", "delete", "update",
	 * "ignore"
	 * 
	 * @author jnicolai
	 */
	public enum ArtifactActionValue {
		CREATE, DELETE, UPDATE, IGNORE, UNKNOWN
	};

	/**
	 * 
	 * Possible values for the artifact mode, "changedFieldsOnly" and "complete"
	 * 
	 * @author jnicolai
	 */
	public enum ArtifactTypeValue {
		PLAINARTIFACT, DEPENDENCY, ATTACHMENT, UNKNOWN
	};

	/**
	 * 
	 * Possible values for the includesFieldMetaData, "true" and "false"
	 * 
	 * @author jnicolai
	 */
	public enum IncludesFieldMetaDataValue {
		TRUE, FALSE
	};

	/**
	 * This is a hash map that indexes all contained field elements according to
	 * their field type
	 */
	private HashMap<String, List<GenericArtifactField>> fieldTypeHashMap = new HashMap<String, List<GenericArtifactField>>();

	/**
	 * This is a hash map that indexes all contained field elements according to
	 * their field type and field name
	 */
	private HashMap<String, HashMap<String, List<GenericArtifactField>>> fieldTypeFieldNameHashMap = new HashMap<String, HashMap<String, List<GenericArtifactField>>>();

	/**
	 * This is a list that contains all field element
	 */
	private List<GenericArtifactField> allFieldList;
	
	/**
	 * The value (content) of the artifact, encoded as BASE64 string. The
	 * content is typically null unless it is an attachment.
	 */
	private String artifactValue = null;

	/**
	 * This is the constant attribute values should set to if the value is not
	 * (yet) known or the whole functionality is not supported
	 */
	public static final String VALUE_UNKNOWN = "unknown";

	/**
	 * Constant value for conflict resolution policy "always ignore"
	 */
	public static final String VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_IGNORE = "alwaysIgnore";

	/**
	 * Constant value for conflict resolution policy "always override"
	 */
	public static final String VALUE_CONFLICT_RESOLUTION_PRIORITY_ALWAYS_OVERRIDE = "alwaysOverride";

	/**
	 * The value of this attribute could be either “complete” or
	 * “changedFieldsOnly” – which denote how the source system has formed this
	 * artifact. Typically, source systems are expected to provide a complete
	 * artifact, but in some cases, this may not be possible or might be very
	 * imperformant (e. g. updated meta-data for attachments), and when that
	 * happens, the source system should set this mode value to
	 * “changedFieldsOnly".
	 */
	private ArtifactModeValue artifactMode = ArtifactModeValue.UNKNOWN;

	/**
	 * This attribute defines if this artifact should be "create"d, "update"d,
	 * "delete"d or "ignore"d by the target system. As long the artifact has not
	 * yet passed the entity service (that may change the value), the value will
	 * typically be "create" or "delete", since the source system does not know
	 * whether the artifact has been already created in the target system. The
	 * four possible values of action are "create", "update", "delete", and
	 * "ignore".
	 */
	private ArtifactActionValue artifactAction = ArtifactActionValue.UNKNOWN;

	/**
	 * This attribute contains the version of the artifact in the target system.
	 * There is one reserved value "unknown" that is used if the source system
	 * does not support version control.
	 */
	private String sourceArtifactVersion = VALUE_UNKNOWN;
	/**
	 * This attribute contains the version of the artifact in the target system.
	 * There is one reserved value "unknown" that is used if the source system
	 * does not support version control.
	 */
	private String targetArtifactVersion = VALUE_UNKNOWN;
	/**
	 * This attribute contains the last transaction that was read. This is
	 * updated by the reader and functions similar to lastModifiedDate.
	 */
	private String transactionId = VALUE_UNKNOWN;

	/**
	 * This attribute contains the date when this artifact was lastly updated.
	 * The more specific this date is, the better the polling components can do
	 * its job.
	 */
	private String sourceArtifactLastModifiedDate = VALUE_UNKNOWN;
	/**
	 * This attribute contains the date when this artifact was lastly updated.
	 * The more specific this date is, the better the polling components can do
	 * its job.
	 */
	private String targetArtifactLastModifiedDate = VALUE_UNKNOWN;

	/**
	 * If a conflict is detected in the target system, the value of this
	 * attribute will be used to determine, whether the target artifact should
	 * be overriden or not. Reserved values are "alwaysIgnore" and
	 * "alwaysOverride".
	 */
	private String conflictResolutionPriority = VALUE_UNKNOWN;

	/**
	 * This attribute determines, whether this is a plain artifact that contains
	 * actual data, whether it is a dependency or association between two
	 * artifacts or whether it is an attachment that belongs to a parent
	 * artifact. Supported values: "plainArtifact", "dependency" (dependency or
	 * association) and "attachment".
	 */
	private ArtifactTypeValue artifactType = ArtifactTypeValue.UNKNOWN;

	/**
	 * This attribute indicates whether field specific meta-data like
	 * cardinality, nillability and alternative names will be shipped with this
	 * artifact. In most cases, this data will be ommitted due to performance
	 * reasons, however this data might be necessary for purposes like
	 * schema-generation and graphical mapping facilities. The only allowed
	 * values are "true" and "false".
	 */
	private IncludesFieldMetaDataValue includesFieldMetaData = IncludesFieldMetaDataValue.FALSE;

	/**
	 * This attribute indicates whether an error occured during processing. The
	 * default value for this field is "ok". If this field is set to another
	 * value but "ok", components not specialized in handling errors should just
	 * ignore the artifact, so the artifactAction attribute should be set to
	 * "ignore".
	 */
	private String errorCode = VALUE_UNKNOWN;

	/**
	 * This attribute contains the nature of the source system of the artifact,
	 * e. g. SourceForge EnterpriseEdition 4.4, CollabNet Enterprise Edition
	 * 5.1, HP Quality Center 9.1, Conigma CCM.
	 */
	private String sourceSystemKind = VALUE_UNKNOWN;

	/**
	 * This attribute contains the id of the source system. This id, together
	 * with the source system kind, should be unique in the whole system
	 * landscape.
	 */
	private String sourceSystemId = VALUE_UNKNOWN;

	/**
	 * This attribute contains the nature of the source repository of the
	 * artifact, e. g. SourceForge EnterpriseEdition 4.4 Tracker, SourceForge
	 * EnterpriseEdition 4.4 Tracker Dependency, HP Quality Center 9.1 Defect,
	 * Conigma CCM SWFM_CR.
	 */
	private String sourceRepositoryKind = VALUE_UNKNOWN;

	/**
	 * This attribute contains the id of the source repository where this
	 * artifact is coming from. This id, together with the source system id,
	 * should be sufficient to determine the source repository. There is one
	 * reserved value "unknown" that is used if the artifact cannot be
	 * directlyassociated to a source repository (this might be the case for
	 * dependencies, associations and attachments).
	 */
	private String sourceRepositoryId = VALUE_UNKNOWN;

	/**
	 * This attribute contains the id of the artifact in the source system.
	 * There is one reserved value named "unknown" that should be used, if the
	 * artifact has no direct id in the source system (this might be the case
	 * for dependencies, associations and attachments).
	 */
	private String sourceArtifactId = VALUE_UNKNOWN;

	/**
	 * This attribute contains the nature of the target system of the artifact,
	 * e. g. SourceForge EnterpriseEdition 4.4, CollabNet Enterprise Edition
	 * 5.1, HP Quality Center 9.1, Conigma CCM.
	 */
	private String targetSystemKind = VALUE_UNKNOWN;

	/**
	 * This attribute contains the id of the target system. This id, together
	 * with the target system kind, should be unique in the whole system
	 * landscape.
	 */
	private String targetSystemId = VALUE_UNKNOWN;

	/**
	 * This attribute contains the nature of the target repository of the
	 * artifact, e. g. SourceForge EnterpriseEdition 4.4 Tracker, SourceForge
	 * EnterpriseEdition 4.4 Task Dependency, HP Quality Center 9.1 Defect
	 * Attachment, Conigma CCM SWFM_CR.
	 */
	private String targetRepositoryKind = VALUE_UNKNOWN;

	/**
	 * This attribute contains the nature of the target repository of the
	 * artifact, e. g. SourceForge EnterpriseEdition 4.4 Tracker, SourceForge
	 * EnterpriseEdition 4.4 Task Dependency, HP Quality Center 9.1 Defect
	 * Attachment, Conigma CCM SWFM_CR.
	 */
	private String targetRepositoryId = VALUE_UNKNOWN;

	/**
	 * This attribute contains the id of the artifact in the target system.
	 * There is one reserved value "unknown" that should be used if the artifact
	 * cannot be associated to a target id (this might be the case for
	 * dependencies, associations and attachments), the target artifact id is
	 * unknown at this stage and/or it has not been created yet in the target
	 * repository. Typically, the value will be "unknown" until this artifact
	 * passed the entity service.
	 */
	private String targetArtifactId = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency" or
	 * "attachment". In this case, it contains the nature of the source
	 * repository of the parent artifact that takes part in this
	 * association/dependency respectively the nature of the source repository
	 * of the parent of this attachment.
	 */
	private String depParentSourceRepositoryKind = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency" or
	 * "attachment". In this case, it contains the id of the source repository
	 * where the parent artifact that takes part in this association/dependency
	 * respectively the parent artifact of this attachment comes from. This id,
	 * together with the source system id, should be sufficient to determine the
	 * source repository.
	 */
	private String depParentSourceRepositoryId = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency" or
	 * "attachment". In this case, this attribute contains the id of the parent
	 * artifact that takes part in the defined association/dependency ,
	 * respectively the id of the parent of this attachment in the source
	 * system.
	 */
	private String depParentSourceArtifactId = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency",
	 * therefore defines a dependency or association. In this case, it contains
	 * the nature of the source repository of the child artifact that takes part
	 * in this association or dependency, e. g. SourceForge EnterpriseEdition
	 * 4.4 Tracker, SourceForge EnterpriseEdition 4.4 Task, HP Quality Center
	 * 9.1 Defect, Conigma CCM SWFM_CR.
	 */
	private String depChildSourceRepositoryKind = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency",
	 * therefore defines a dependency or association. In this case, it contains
	 * the id of the source repository where the child artifact that takes part
	 * in this association or dependency is coming from. This id, together with
	 * the source system id, should be sufficient to determine the source
	 * repository.
	 */
	private String depChildSourceRepositoryId = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency",
	 * therefore defines a dependency or association. In this case, this
	 * attribute contains the id of the child artifact that takes part in the
	 * defined association or dependency in the source system.
	 */
	private String depChildSourceArtifactId = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency" or
	 * "attachment". In this case, it contains the nature of the target
	 * repository of the parent artifact that takes part in this
	 * association/dependency respectively the nature of the target repository
	 * of the parent artifact of this attachment. Typically, the value will be
	 * "unknown" until this artifact passes the entity service.
	 */
	private String depParentTargetRepositoryKind = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency" or
	 * "attachment". In this case, it contains the id of the target repository
	 * where the parent artifact that takes part in this association/dependency
	 * respectively the parent artifact of this attachment should go. This id,
	 * together with the target system id, should be sufficient to determine the
	 * target repository. Typically, the value will be "unknown" until this
	 * artifact passes the entity service.
	 */
	private String depParentTargetRepositoryId = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency" or
	 * "attachment". In this case, this attribute contains the id of the parent
	 * artifact that takes part in the defined association/dependency
	 * respectively the id of the parent artifact of this attachment in the
	 * target system. Typically, the value will be "unknown" until this artifact
	 * passes the entity service.
	 */
	private String depParentTargetArtifactId = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency",
	 * therefore defines a dependency or association. In this case, it contains
	 * the nature of the target repository of the child artifact that takes part
	 * in this association or dependency, e. g. SourceForge EnterpriseEdition
	 * 4.4 Tracker, SourceForge EnterpriseEdition 4.4 Task, HP Quality Center
	 * 9.1 Defect, Conigma CCM SWFM_CR. Typically, the value will be "unknown"
	 * until this artifact passes the entity service.
	 */
	private String depChildTargetRepositoryKind = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency",
	 * therefore defines a dependency or association. In this case, it contains
	 * the id of the target repository where the child artifact that takes part
	 * in this association or dependency should go. This id, together with the
	 * target system id, should be sufficient to determine the target
	 * repository. Typically, the value will be "unknown" until this artifact
	 * passes the entity service.
	 */
	private String depChildTargetRepositoryId = VALUE_UNKNOWN;

	/**
	 * This attribute is only used if the artifact type is "dependency",
	 * therefore defines a dependency or association. In this case, this
	 * attribute contains the id of the child artifact that takes part in the
	 * defined association or dependency in the target system. Typically, the
	 * value will be "unknown" until this artifact passes the entity service.
	 */
	private String depChildTargetArtifactId = VALUE_UNKNOWN;

	public final static String TRANSFORMER_TRANSFORMATION_ERROR = "transformerTransformationError";

	public final static String TRANSFORMER_FILE_ERROR = "transformerFileError";

	public final static String EXTERNAL_SYSTEM_WRITE_ERROR = "externalSystemWriteError";

	public final static String MAX_CONNECTIONS_REACHED_FOR_POOL = "maxConnectionReachedForPool";

	public final static String EXTERNAL_SYSTEM_CONNECTION_ERROR = "connectionError";

	public final static String GENERIC_ARTIFACT_PARSING_ERROR = "parsingError";

	public final static String GENERIC_ARTIFACT_NOT_SCHEMA_COMPLIANT_ERROR = "notSchemaCompliant";

	public final static String NO_ERROR = "ok";

	/**
	 * Create a new empty generic artifact All attributes will be set to
	 * "unknown"
	 */
	public GenericArtifact() {
	}

	/**
	 * @param artifactMode
	 *            the artifactMode to set
	 */
	public void setArtifactMode(ArtifactModeValue artifactMode) {
		this.artifactMode = artifactMode;
	}

	/**
	 * @return the artifactMode
	 */
	public ArtifactModeValue getArtifactMode() {
		return artifactMode;
	}

	/**
	 * @param artifactAction
	 *            the artifactAction to set
	 */
	public void setArtifactAction(ArtifactActionValue artifactAction) {
		this.artifactAction = artifactAction;
	}

	/**
	 * @return the artifactAction
	 */
	public ArtifactActionValue getArtifactAction() {
		return artifactAction;
	}

	/**
	 * @param conflictResolutionPolicy
	 *            the conflictResolutionPolicy to set
	 */
	public void setConflictResolutionPriority(String conflictResolutionPolicy) {
		this.conflictResolutionPriority = conflictResolutionPolicy;
	}

	/**
	 * @return the conflictResolutionPolicy
	 */
	public String getConflictResolutionPriority() {
		return conflictResolutionPriority;
	}

	/**
	 * @param artifactType
	 *            the artifactType to set
	 */
	public void setArtifactType(ArtifactTypeValue artifactType) {
		this.artifactType = artifactType;
	}

	/**
	 * @return the artifactType
	 */
	public ArtifactTypeValue getArtifactType() {
		return artifactType;
	}

	/**
	 * @param sourceSystemKind
	 *            the sourceSystemKind to set
	 */
	public void setSourceSystemKind(String sourceSystemKind) {
		this.sourceSystemKind = sourceSystemKind;
	}

	/**
	 * @return the sourceSystemKind
	 */
	public String getSourceSystemKind() {
		return sourceSystemKind;
	}

	/**
	 * @param sourceSystemId
	 *            the sourceSystemId to set
	 */
	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	/**
	 * @return the sourceSystemId
	 */
	public String getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * @param sourceRepositoryKind
	 *            the sourceRepositoryKind to set
	 */
	public void setSourceRepositoryKind(String sourceRepositoryKind) {
		this.sourceRepositoryKind = sourceRepositoryKind;
	}

	/**
	 * @return the sourceRepositoryKind
	 */
	public String getSourceRepositoryKind() {
		return sourceRepositoryKind;
	}

	/**
	 * @param sourceRepositoryId
	 *            the sourceRepositoryId to set
	 */
	public void setSourceRepositoryId(String sourceRepositoryId) {
		this.sourceRepositoryId = sourceRepositoryId;
	}

	/**
	 * @return the sourceRepositoryId
	 */
	public String getSourceRepositoryId() {
		return sourceRepositoryId;
	}

	/**
	 * @param sourceArtifactId
	 *            the sourceArtifactId to set
	 */
	public void setSourceArtifactId(String sourceArtifactId) {
		this.sourceArtifactId = sourceArtifactId;
	}

	/**
	 * @return the sourceArtifactId
	 */
	public String getSourceArtifactId() {
		return sourceArtifactId;
	}

	/**
	 * @param targetSystemKind
	 *            the targetSystemKind to set
	 */
	public void setTargetSystemKind(String targetSystemKind) {
		this.targetSystemKind = targetSystemKind;
	}

	/**
	 * @return the targetSystemKind
	 */
	public String getTargetSystemKind() {
		return targetSystemKind;
	}

	/**
	 * @param targetSystemId
	 *            the targetSystemId to set
	 */
	public void setTargetSystemId(String targetSystemId) {
		this.targetSystemId = targetSystemId;
	}

	/**
	 * @return the targetSystemId
	 */
	public String getTargetSystemId() {
		return targetSystemId;
	}

	/**
	 * @param targetRepositoryKind
	 *            the targetRepositoryKind to set
	 */
	public void setTargetRepositoryKind(String targetRepositoryKind) {
		this.targetRepositoryKind = targetRepositoryKind;
	}

	/**
	 * @return the targetRepositoryKind
	 */
	public String getTargetRepositoryKind() {
		return targetRepositoryKind;
	}

	/**
	 * @param targetRepositoryId
	 *            the targetRepositoryId to set
	 */
	public void setTargetRepositoryId(String targetRepositoryId) {
		this.targetRepositoryId = targetRepositoryId;
	}

	/**
	 * @return the targetRepositoryId
	 */
	public String getTargetRepositoryId() {
		return targetRepositoryId;
	}

	/**
	 * @param targetArtifactId
	 *            the targetArtifactId to set
	 */
	public void setTargetArtifactId(String targetArtifactId) {
		this.targetArtifactId = targetArtifactId;
	}

	/**
	 * @return the targetArtifactId
	 */
	public String getTargetArtifactId() {
		return targetArtifactId;
	}

	/**
	 * @param depParentSourceRepositoryKind
	 *            the depParentSourceRepositoryKind to set
	 */
	public void setDepParentSourceRepositoryKind(
			String depParentSourceRepositoryKind) {
		this.depParentSourceRepositoryKind = depParentSourceRepositoryKind;
	}

	/**
	 * @return the depParentSourceRepositoryKind
	 */
	public String getDepParentSourceRepositoryKind() {
		return depParentSourceRepositoryKind;
	}

	/**
	 * @param depParentSourceRepositoryId
	 *            the depParentSourceRepositoryId to set
	 */
	public void setDepParentSourceRepositoryId(
			String depParentSourceRepositoryId) {
		this.depParentSourceRepositoryId = depParentSourceRepositoryId;
	}

	/**
	 * @return the depParentSourceRepositoryId
	 */
	public String getDepParentSourceRepositoryId() {
		return depParentSourceRepositoryId;
	}

	/**
	 * @param depParentSourceArtifactId
	 *            the depParentSourceArtifactId to set
	 */
	public void setDepParentSourceArtifactId(String depParentSourceArtifactId) {
		this.depParentSourceArtifactId = depParentSourceArtifactId;
	}

	/**
	 * @return the depParentSourceArtifactId
	 */
	public String getDepParentSourceArtifactId() {
		return depParentSourceArtifactId;
	}

	/**
	 * @param depChildSourceRepositoryKind
	 *            the depChildSourceRepositoryKind to set
	 */
	public void setDepChildSourceRepositoryKind(
			String depChildSourceRepositoryKind) {
		this.depChildSourceRepositoryKind = depChildSourceRepositoryKind;
	}

	/**
	 * @return the depChildSourceRepositoryKind
	 */
	public String getDepChildSourceRepositoryKind() {
		return depChildSourceRepositoryKind;
	}

	/**
	 * @param depChildSourceRepositoryId
	 *            the depChildSourceRepositoryId to set
	 */
	public void setDepChildSourceRepositoryId(String depChildSourceRepositoryId) {
		this.depChildSourceRepositoryId = depChildSourceRepositoryId;
	}

	/**
	 * @return the depChildSourceRepositoryId
	 */
	public String getDepChildSourceRepositoryId() {
		return depChildSourceRepositoryId;
	}

	/**
	 * @param depChildSourceArtifactId
	 *            the depChildSourceArtifactId to set
	 */
	public void setDepChildSourceArtifactId(String depChildSourceArtifactId) {
		this.depChildSourceArtifactId = depChildSourceArtifactId;
	}

	/**
	 * @return the depChildSourceArtifactId
	 */
	public String getDepChildSourceArtifactId() {
		return depChildSourceArtifactId;
	}

	/**
	 * @param depParentTargetRepositoryKind
	 *            the depParentTargetRepositoryKind to set
	 */
	public void setDepParentTargetRepositoryKind(
			String depParentTargetRepositoryKind) {
		this.depParentTargetRepositoryKind = depParentTargetRepositoryKind;
	}

	/**
	 * @return the depParentTargetRepositoryKind
	 */
	public String getDepParentTargetRepositoryKind() {
		return depParentTargetRepositoryKind;
	}

	/**
	 * @param depParentTargetRepositoryId
	 *            the depParentTargetRepositoryId to set
	 */
	public void setDepParentTargetRepositoryId(
			String depParentTargetRepositoryId) {
		this.depParentTargetRepositoryId = depParentTargetRepositoryId;
	}

	/**
	 * @return the depParentTargetRepositoryId
	 */
	public String getDepParentTargetRepositoryId() {
		return depParentTargetRepositoryId;
	}

	/**
	 * @param depParentTargetArtifactId
	 *            the depParentTargetArtifactId to set
	 */
	public void setDepParentTargetArtifactId(String depParentTargetArtifactId) {
		this.depParentTargetArtifactId = depParentTargetArtifactId;
	}

	/**
	 * @return the depParentTargetArtifactId
	 */
	public String getDepParentTargetArtifactId() {
		return depParentTargetArtifactId;
	}

	/**
	 * @param depChildTargetRepositoryKind
	 *            the depChildTargetRepositoryKind to set
	 */
	public void setDepChildTargetRepositoryKind(
			String depChildTargetRepositoryKind) {
		this.depChildTargetRepositoryKind = depChildTargetRepositoryKind;
	}

	/**
	 * @return the depChildTargetRepositoryKind
	 */
	public String getDepChildTargetRepositoryKind() {
		return depChildTargetRepositoryKind;
	}

	/**
	 * @param depChildTargetRepositoryId
	 *            the depChildTargetRepositoryId to set
	 */
	public void setDepChildTargetRepositoryId(String depChildTargetRepositoryId) {
		this.depChildTargetRepositoryId = depChildTargetRepositoryId;
	}

	/**
	 * @return the depChildTargetRepositoryId
	 */
	public String getDepChildTargetRepositoryId() {
		return depChildTargetRepositoryId;
	}

	/**
	 * @param depChildTargetArtifactId
	 *            the depChildTargetArtifactId to set
	 */
	public void setDepChildTargetArtifactId(String depChildTargetArtifactId) {
		this.depChildTargetArtifactId = depChildTargetArtifactId;
	}

	/**
	 * @return the depChildTargetArtifactId
	 */
	public String getDepChildTargetArtifactId() {
		return depChildTargetArtifactId;
	}

	/**
	 * @param artifactValue
	 *            the artifact value to set (attachments only)
	 */
	public void setArtifactValue(String artifactValue) {
		this.artifactValue = artifactValue;
	}

	/**
	 * @return the artifact value (only set within attachments)
	 */
	public String getArtifactValue() {
		return artifactValue;
	}

	/**
	 * @param attachmentValue
	 *            the attachmentValue to set
	 */
	public void setRawAttachmentData(byte[] attachmentData) {
		this.artifactValue = new String(Base64.encodeBase64(attachmentData));
	}

	/**
	 * @return the fieldValue
	 */
	public String getRawAttachmentData() {
		return new String(Base64.decodeBase64(artifactValue.getBytes()));
	}

	/**
	 * This operation creates a new field with all attributes but fieldName and
	 * fieldType set to "unknown" and the value set to null
	 * 
	 * @param fieldName
	 *            field name, this value cannot be changed afterwards
	 * @param fieldType
	 *            field type, this value cannot be changed afterwards
	 * @return the newly added generic artifact field object
	 */
	public GenericArtifactField addNewField(String fieldName, String fieldType) {
		GenericArtifactField genericArtifactField = new GenericArtifactField(
				fieldName, fieldType);
		if (allFieldList == null) {
			allFieldList = new ArrayList<GenericArtifactField>();
		}
		allFieldList.add(genericArtifactField);
		indexNewFieldElement(fieldName, fieldType, genericArtifactField);
		return genericArtifactField;
	}

	/**
	 * Add new field to a number of hash maps to speed up indexing
	 * 
	 * @param fieldName
	 *            name of the new field
	 * @param fieldType
	 *            type of the new field
	 * @param genericArtifactField
	 *            new field
	 */
	private void indexNewFieldElement(String fieldName, String fieldType,
			GenericArtifactField genericArtifactField) {
		if (fieldTypeHashMap.containsKey(fieldType)) {
			fieldTypeHashMap.get(fieldType).add(genericArtifactField);
			HashMap<String, List<GenericArtifactField>> allGenericArtifactFieldsWithSameFieldTypeMap = fieldTypeFieldNameHashMap
					.get(fieldType);
			if (allGenericArtifactFieldsWithSameFieldTypeMap
					.containsKey(fieldName)) {
				allGenericArtifactFieldsWithSameFieldTypeMap.get(fieldName)
						.add(genericArtifactField);
			} else {
				List<GenericArtifactField> allArtifactFieldsWithSameFieldTypeAndNameList = new ArrayList<GenericArtifactField>();
				allArtifactFieldsWithSameFieldTypeAndNameList
						.add(genericArtifactField);
				allGenericArtifactFieldsWithSameFieldTypeMap.put(fieldName,
						allArtifactFieldsWithSameFieldTypeAndNameList);
			}
		} else {
			List<GenericArtifactField> allGenericArtifactFieldsWithSameFieldTypeList = new ArrayList<GenericArtifactField>();
			allGenericArtifactFieldsWithSameFieldTypeList
					.add(genericArtifactField);
			fieldTypeHashMap.put(fieldType,
					allGenericArtifactFieldsWithSameFieldTypeList);
			HashMap<String, List<GenericArtifactField>> allGenericArtifactFieldsWithSameFieldTypeMap = new HashMap<String, List<GenericArtifactField>>();
			List<GenericArtifactField> allArtifactFieldsWithSameFieldTypeAndNameList = new ArrayList<GenericArtifactField>();
			allArtifactFieldsWithSameFieldTypeAndNameList
					.add(genericArtifactField);
			allGenericArtifactFieldsWithSameFieldTypeMap.put(fieldName,
					allArtifactFieldsWithSameFieldTypeAndNameList);
			fieldTypeFieldNameHashMap.put(fieldType,
					allGenericArtifactFieldsWithSameFieldTypeMap);
		}

		if (fieldNameHashMap.containsKey(fieldName)) {
			fieldNameHashMap.get(fieldName).add(genericArtifactField);
		} else {
			List<GenericArtifactField> allGenericArtifactFieldsWithSameFieldNameList = new ArrayList<GenericArtifactField>();
			allGenericArtifactFieldsWithSameFieldNameList
					.add(genericArtifactField);
			fieldNameHashMap.put(fieldName,
					allGenericArtifactFieldsWithSameFieldNameList);
		}
	}

	/**
	 * Returns all fields within the generic artifact You may change the
	 * attributes of the fields but you may not directly add new fields to this
	 * list
	 * 
	 * @return all generic artifact fields of this generic artifact
	 */
	public List<GenericArtifactField> getAllGenericArtifactFields() {
		return allFieldList;
	}

	/**
	 * Returns all fields within the generic artifact that have the specified
	 * field type You may change the attributes of the fields but you may not
	 * directly add new fields to this list
	 * 
	 * @param fieldType
	 *            field type, all returned fields have in common
	 * @return all generic artifact fields of this generic artifact with the
	 *         same field type or null if there is no single field with the
	 *         specified field type
	 */
	public List<GenericArtifactField> getAllGenericArtifactFieldsWithSameFieldType(
			String fieldType) {
		return fieldTypeHashMap.get(fieldType);
	}

	/**
	 * Returns all fields within the generic artifact that have the specified
	 * field name You may change the attributes of the fields but you may not
	 * directly add new fields to this list
	 * 
	 * @param fieldName
	 *            field name, all returned fields have in common
	 * @return all generic artifact fields of this generic artifact with the
	 *         same field name or null if there is no single field with the
	 *         specified field name
	 */
	public List<GenericArtifactField> getAllGenericArtifactFieldsWithSameFieldName(
			String fieldName) {
		return fieldNameHashMap.get(fieldName);
	}

	/**
	 * Returns all fields within the generic artifact that have the specified
	 * field type and field name You may change the attributes of the fields but
	 * you may not directly add new fields to this list
	 * 
	 * @param fieldType
	 *            field type, all returned fields have in common
	 * @param fieldName
	 *            field name, all returned fields have in common
	 * @return all generic artifact fields of this generic artifact with the
	 *         specified field type and field name or null if there is no single
	 *         field with the specified field type and field name
	 */
	public List<GenericArtifactField> getAllGenericArtifactFieldsWithSameFieldTypeAndFieldName(
			String fieldType, String fieldName) {
		HashMap<String, List<GenericArtifactField>> allGenericArtifactFieldsWithSameFieldTypeMap = fieldTypeFieldNameHashMap
				.get(fieldType);
		if (allGenericArtifactFieldsWithSameFieldTypeMap == null) {
			return null;
		} else {
			return allGenericArtifactFieldsWithSameFieldTypeMap.get(fieldName);
		}
	}

	/*public String getLastReadTransactionId() {
		return transactionId;
	}

	public void setLastReadTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}*/

	public IncludesFieldMetaDataValue getIncludesFieldMetaData() {
		return includesFieldMetaData;
	}

	public void setIncludesFieldMetaData(
			IncludesFieldMetaDataValue includesFieldMetaData) {
		this.includesFieldMetaData = includesFieldMetaData;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getSourceArtifactVersion() {
		return sourceArtifactVersion;
	}

	public void setSourceArtifactVersion(String sourceArtifactVersion) {
		this.sourceArtifactVersion = sourceArtifactVersion;
	}

	public String getTargetArtifactVersion() {
		return targetArtifactVersion;
	}

	public void setTargetArtifactVersion(String targetArtifactVersion) {
		this.targetArtifactVersion = targetArtifactVersion;
	}

	public String getSourceArtifactLastModifiedDate() {
		return sourceArtifactLastModifiedDate;
	}

	public void setSourceArtifactLastModifiedDate(
			String sourceArtifactLastModifiedDate) {
		this.sourceArtifactLastModifiedDate = sourceArtifactLastModifiedDate;
	}

	public String getTargetArtifactLastModifiedDate() {
		return targetArtifactLastModifiedDate;
	}

	public void setTargetArtifactLastModifiedDate(
			String targetArtifactLastModifiedDate) {
		this.targetArtifactLastModifiedDate = targetArtifactLastModifiedDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
