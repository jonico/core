package com.collabnet.ccf.ist;

import java.util.ArrayList;
import java.util.List;

import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;

/**
 * This maps the types of all known fixed incidents fields (a.k.a. mandatory in
 * CCF lingo) ISTCustomFieldType is defining the possible field types for
 * SpiraTest while FieldValueTypeValue describes the Generic Artifact field
 * types
 *
 * @author volker
 *
 */
public enum ISTMandatoryFieldType {
    ArtifactTypeId(false, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    ClosedDate(true, ISTCustomFieldType.Date, false, FieldValueTypeValue.DATETIME, true),
    CompletionPercent(false, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    ConcurrencyDate(false, ISTCustomFieldType.Date, false, FieldValueTypeValue.DATETIME, true),
    CreationDate(true, ISTCustomFieldType.Date, false, FieldValueTypeValue.DATETIME, false),
    Description(true, ISTCustomFieldType.Text, false, FieldValueTypeValue.HTMLSTRING, true),
    DetectedReleaseId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    DetectedReleaseVersionNumber(true, ISTCustomFieldType.Text, false, FieldValueTypeValue.STRING, true),
    EstimatedEffort(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    FixedBuildId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    FixedBuildName(true, ISTCustomFieldType.Text, false, FieldValueTypeValue.STRING, true),
    IncidentId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    IncidentStatusId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    IncidentStatusName(true, ISTCustomFieldType.List, false, FieldValueTypeValue.STRING, true),
    IncidentStatusOpenStatus(true, ISTCustomFieldType.Boolean, false, FieldValueTypeValue.STRING, true),
    IncidentTypeId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    IncidentTypeName(true, ISTCustomFieldType.List, false, FieldValueTypeValue.STRING, true),
    LastUpdateDate(false, ISTCustomFieldType.Date, true, FieldValueTypeValue.DATETIME, false), // not used to identify the content
    Name(true, ISTCustomFieldType.Text, false, FieldValueTypeValue.STRING, true),
    OpenerId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    OpenerName(true, ISTCustomFieldType.User, false, FieldValueTypeValue.STRING, true),
    OwnerId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    OwnerName(true, ISTCustomFieldType.User, false, FieldValueTypeValue.STRING, true),
    PriorityId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    PriorityName(true, ISTCustomFieldType.List, false, FieldValueTypeValue.STRING, true),
    ProjectedEffort(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    ProjectId(true, ISTCustomFieldType.Integer, true, FieldValueTypeValue.STRING, true),
    ProjectName(true, ISTCustomFieldType.Text, true, FieldValueTypeValue.STRING, true),
    RemainingEffort(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    ResolvedReleaseId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    ResolvedReleaseVersionNumber(true, ISTCustomFieldType.List, false, FieldValueTypeValue.STRING, true),
    SeverityId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    SeverityName(true, ISTCustomFieldType.List, false, FieldValueTypeValue.STRING, true),
    StartDate(true, ISTCustomFieldType.Date, false, FieldValueTypeValue.DATETIME, true),
    TestRunStepId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    VerifiedReleaseId(true, ISTCustomFieldType.Integer, false, FieldValueTypeValue.STRING, true),
    VerifiedReleaseVersionNumber(true, ISTCustomFieldType.List, false, FieldValueTypeValue.STRING, true);

    static List<ISTMandatoryFieldType> getIdentifyingMandatoryFields() {
        List<ISTMandatoryFieldType> ret = new ArrayList<ISTMandatoryFieldType>();

        for (ISTMandatoryFieldType ft : values()) {
            if (ft.isIdentifying())
                ret.add(ft);
        }
        return ret;
    }

    static List<ISTMandatoryFieldType> getReadOnlyMandatoryFields() {
        List<ISTMandatoryFieldType> ret = new ArrayList<ISTMandatoryFieldType>();

        for (ISTMandatoryFieldType ft : values()) {
            if (ft.isReadOnlyField)
                ret.add(ft);
        }
        return ret;
    }

    private final boolean             usesJAXWrapper;
    private final ISTCustomFieldType  fieldValueType;
    private final boolean             isReadOnlyField;

    private final FieldValueTypeValue genericFieldType;

    private final boolean             identifying;

    /**
     *
     * @param jax
     *            Uses a JAX wrapper
     * @param fvt
     *            SpiraTest field type
     * @param ro
     *            Read Only field
     * @param gat
     *            GenericArtifact field type
     * @param isIdentifying
     *            used to hash calculation?
     */
    private ISTMandatoryFieldType(boolean jax, ISTCustomFieldType fvt,
            boolean ro, FieldValueTypeValue gat, boolean isIdentifying) {
        this.usesJAXWrapper = jax;
        this.fieldValueType = fvt;
        this.isReadOnlyField = ro;
        this.genericFieldType = gat;
        this.identifying = isIdentifying;
    }

    FieldValueTypeValue genericFielValueType() {
        return genericFieldType;
    }

    boolean isIdentifying() {
        return identifying;
    }

    boolean isReadOnly() {
        return isReadOnlyField;
    }

    ISTCustomFieldType istFieldValueType() {
        return fieldValueType;
    }

    boolean usesJAX() {
        return usesJAXWrapper;
    }

}
