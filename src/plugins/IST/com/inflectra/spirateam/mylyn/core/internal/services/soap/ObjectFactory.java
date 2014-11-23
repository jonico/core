
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.inflectra.spirateam.mylyn.core.internal.services.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _ArrayOfRemoteIncidentSeverity_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteIncidentSeverity");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _ArrayOfRemoteWorkflowIncidentFields_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteWorkflowIncidentFields");
    private final static QName _RemoteDataMapping_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteDataMapping");
    private final static QName _RemoteRelease_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteRelease");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _ArrayOfRemoteIncident_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteIncident");
    private final static QName _RemoteFilter_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteFilter");
    private final static QName _ArrayOfRemoteDocumentFolder_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteDocumentFolder");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _RemoteTestSetTestCaseMapping_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTestSetTestCaseMapping");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _ArrayOfValidationFaultMessageItem_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "ArrayOfValidationFaultMessageItem");
    private final static QName _RemoteArtifact_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteArtifact");
    private final static QName _ArrayOfRemoteIncidentStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteIncidentStatus");
    private final static QName _ServiceFaultMessage_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "ServiceFaultMessage");
    private final static QName _ValidationFaultMessageItem_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "ValidationFaultMessageItem");
    private final static QName _RemoteProject_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteProject");
    private final static QName _ArrayOfRemoteDocumentVersion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteDocumentVersion");
    private final static QName _ArrayOfint_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfint");
    private final static QName _RemoteTestRunStep_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTestRunStep");
    private final static QName _RemoteComment_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteComment");
    private final static QName _ArrayOfRemoteTask_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTask");
    private final static QName _RemoteRequirementTestCaseMapping_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteRequirementTestCaseMapping");
    private final static QName _RemoteDocumentFolder_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteDocumentFolder");
    private final static QName _RemoteBuildSourceCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteBuildSourceCode");
    private final static QName _MultiValueFilter_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "MultiValueFilter");
    private final static QName _RemoteWorkflowIncidentFields_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteWorkflowIncidentFields");
    private final static QName _ArrayOfRemoteManualTestRun_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteManualTestRun");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _ArrayOfRemoteDocument_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteDocument");
    private final static QName _RemoteTestCaseParameter_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTestCaseParameter");
    private final static QName _ArrayOfRemoteProjectUser_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteProjectUser");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _ArrayOfRemoteWorkflowIncidentTransition_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteWorkflowIncidentTransition");
    private final static QName _RemoteSort_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteSort");
    private final static QName _ArrayOfRemoteRequirementTestCaseMapping_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteRequirementTestCaseMapping");
    private final static QName _RemoteAutomationHost_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteAutomationHost");
    private final static QName _ArrayOfRemoteAssociation_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteAssociation");
    private final static QName _RemoteWorkflowIncidentCustomProperties_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteWorkflowIncidentCustomProperties");
    private final static QName _ArrayOfRemoteReleaseTestCaseMapping_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteReleaseTestCaseMapping");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _ArrayOfRemoteAutomatedTestRun_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteAutomatedTestRun");
    private final static QName _ArrayOfRemoteTestSetTestCaseMapping_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTestSetTestCaseMapping");
    private final static QName _RemoteVersion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteVersion");
    private final static QName _ArrayOfRemoteProject_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteProject");
    private final static QName _RemoteIncidentPriority_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteIncidentPriority");
    private final static QName _ArrayOfRemoteBuild_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteBuild");
    private final static QName _ArrayOfRemoteComment_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteComment");
    private final static QName _ArrayOfRemoteTestStep_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTestStep");
    private final static QName _RemoteRequirement_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteRequirement");
    private final static QName _RemoteIncidentSeverity_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteIncidentSeverity");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _ArrayOfRemoteArtifactCustomProperty_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteArtifactCustomProperty");
    private final static QName _ArrayOfRemoteRelease_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteRelease");
    private final static QName _RemoteIncident_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteIncident");
    private final static QName _RemoteUser_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteUser");
    private final static QName _ArrayOfRemoteWorkflowIncidentCustomProperties_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteWorkflowIncidentCustomProperties");
    private final static QName _ArrayOfRemoteCustomPropertyOption_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteCustomPropertyOption");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _RemoteBuild_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteBuild");
    private final static QName _ArrayOfRemoteTestRun_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTestRun");
    private final static QName _ArrayOfRemoteTestSetTestCaseParameter_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTestSetTestCaseParameter");
    private final static QName _RemoteTestStepParameter_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTestStepParameter");
    private final static QName _ArrayOfRemoteDocumentType_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteDocumentType");
    private final static QName _DateRange_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "DateRange");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _RemoteWorkflowIncidentTransition_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteWorkflowIncidentTransition");
    private final static QName _RemoteAssociation_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteAssociation");
    private final static QName _ArrayOfRemoteCustomListValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteCustomListValue");
    private final static QName _RemoteSetting_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteSetting");
    private final static QName _RemoteDocumentVersion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteDocumentVersion");
    private final static QName _RemoteIncidentStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteIncidentStatus");
    private final static QName _RemoteReleaseTestCaseMapping_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteReleaseTestCaseMapping");
    private final static QName _ArrayOfRemoteDataMapping_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteDataMapping");
    private final static QName _ArrayOfRemoteTestCaseParameter_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTestCaseParameter");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _RemoteCustomProperty_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteCustomProperty");
    private final static QName _RemoteTestRun_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTestRun");
    private final static QName _RemoteManualTestRun_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteManualTestRun");
    private final static QName _ArrayOfRemoteCustomList_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteCustomList");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _ArrayOfRemoteTestStepParameter_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTestStepParameter");
    private final static QName _RemoteTestSetTestCaseParameter_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTestSetTestCaseParameter");
    private final static QName _ArrayOfRemoteSetting_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteSetting");
    private final static QName _ArrayOfRemoteAutomationHost_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteAutomationHost");
    private final static QName _ArrayOfRemoteIncidentPriority_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteIncidentPriority");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _RemoteProjectUser_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteProjectUser");
    private final static QName _ArrayOfRemoteBuildSourceCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteBuildSourceCode");
    private final static QName _RemoteCustomListValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteCustomListValue");
    private final static QName _RemoteCustomList_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteCustomList");
    private final static QName _RemoteAutomationEngine_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteAutomationEngine");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _ArrayOfRemoteFilter_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteFilter");
    private final static QName _RemoteDocument_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteDocument");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _ArrayOfRemoteAutomationEngine_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteAutomationEngine");
    private final static QName _RemoteTestCase_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTestCase");
    private final static QName _ArrayOfRemoteCustomProperty_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteCustomProperty");
    private final static QName _RemoteCustomPropertyOption_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteCustomPropertyOption");
    private final static QName _RemoteProjectRole_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteProjectRole");
    private final static QName _ArrayOfRemoteRequirement_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteRequirement");
    private final static QName _ArrayOfRemoteProjectRole_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteProjectRole");
    private final static QName _RemoteDocumentType_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteDocumentType");
    private final static QName _ArrayOfRemoteIncidentType_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteIncidentType");
    private final static QName _RemoteAutomatedTestRun_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteAutomatedTestRun");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _ArrayOfRemoteTestRunStep_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTestRunStep");
    private final static QName _ArrayOfRemoteTestCase_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTestCase");
    private final static QName _RemoteTestStep_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTestStep");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _ValidationFaultMessage_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "ValidationFaultMessage");
    private final static QName _RemoteIncidentType_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteIncidentType");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _RemoteArtifactCustomProperty_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteArtifactCustomProperty");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _ArrayOfRemoteTestSet_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArrayOfRemoteTestSet");
    private final static QName _RemoteTestSet_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTestSet");
    private final static QName _RemoteTask_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemoteTask");
    private final static QName _TestRunCreateForAutomatedTestSetAutomationHostToken_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "automationHostToken");
    private final static QName _AutomationHostRetrieveByTokenResponseAutomationHostRetrieveByTokenResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "AutomationHost_RetrieveByTokenResult");
    private final static QName _RequirementAddTestCoverageRemoteReqTestCaseMapping_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteReqTestCaseMapping");
    private final static QName _TestRunSaveEndDate_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "endDate");
    private final static QName _TestRunSaveRemoteTestRuns_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteTestRuns");
    private final static QName _RemoteVersionVersion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Version");
    private final static QName _RemoteVersionPatch_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Patch");
    private final static QName _RemoteUserLastName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "LastName");
    private final static QName _RemoteUserDepartment_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Department");
    private final static QName _RemoteUserLdapDn_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "LdapDn");
    private final static QName _RemoteUserUserName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "UserName");
    private final static QName _RemoteUserUserId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "UserId");
    private final static QName _RemoteUserEmailAddress_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "EmailAddress");
    private final static QName _RemoteUserRssToken_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RssToken");
    private final static QName _RemoteUserFirstName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "FirstName");
    private final static QName _RemoteUserFullName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "FullName");
    private final static QName _RemoteUserMiddleInitial_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "MiddleInitial");
    private final static QName _RemoteReleasePlannedEffort_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "PlannedEffort");
    private final static QName _RemoteReleaseTaskActualEffort_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TaskActualEffort");
    private final static QName _RemoteReleaseAvailableEffort_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AvailableEffort");
    private final static QName _RemoteReleaseDescription_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Description");
    private final static QName _RemoteReleaseTaskCount_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TaskCount");
    private final static QName _RemoteReleaseReleaseId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ReleaseId");
    private final static QName _RemoteReleaseVersionNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "VersionNumber");
    private final static QName _RemoteReleaseIndentLevel_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IndentLevel");
    private final static QName _RemoteReleaseCreatorId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CreatorId");
    private final static QName _RemoteReleaseCreatorName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CreatorName");
    private final static QName _RemoteReleaseTaskEstimatedEffort_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TaskEstimatedEffort");
    private final static QName _RemoteReleaseName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Name");
    private final static QName _TestCaseCountRemoteFilters_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteFilters");
    private final static QName _DataMappingRetrieveProjectMappingsResponseDataMappingRetrieveProjectMappingsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "DataMapping_RetrieveProjectMappingsResult");
    private final static QName _DocumentRetrieveForFolderRemoteSort_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteSort");
    private final static QName _RemoteDocumentVersionAttachmentVersionId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AttachmentVersionId");
    private final static QName _RemoteDocumentVersionAuthorId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AuthorId");
    private final static QName _RemoteDocumentVersionAuthorName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AuthorName");
    private final static QName _RemoteDocumentVersionFilenameOrUrl_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "FilenameOrUrl");
    private final static QName _RemoteIncidentStatusIncidentStatusId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IncidentStatusId");
    private final static QName _TestRunCreateFromTestCasesReleaseId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "releaseId");
    private final static QName _TestRunCreateFromTestCasesTestCaseIds_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "testCaseIds");
    private final static QName _DataMappingRetrieveCustomPropertyMappingResponseDataMappingRetrieveCustomPropertyMappingResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "DataMapping_RetrieveCustomPropertyMappingResult");
    private final static QName _CustomPropertyAddCustomListValueResponseCustomPropertyAddCustomListValueResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "CustomProperty_AddCustomListValueResult");
    private final static QName _TestCaseCreateFolderParentTestFolderId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "parentTestFolderId");
    private final static QName _TestCaseCreateFolderRemoteTestCase_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteTestCase");
    private final static QName _IncidentRetrieveSeveritiesResponseIncidentRetrieveSeveritiesResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveSeveritiesResult");
    private final static QName _TaskRetrieveResponseTaskRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Task_RetrieveResult");
    private final static QName _TestSetUpdateRemoteTestSet_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteTestSet");
    private final static QName _IncidentAddTypeResponseIncidentAddTypeResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_AddTypeResult");
    private final static QName _TestSetCreateCommentResponseTestSetCreateCommentResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestSet_CreateCommentResult");
    private final static QName _DocumentAddUrlResponseDocumentAddUrlResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_AddUrlResult");
    private final static QName _CustomPropertyAddCustomListResponseCustomPropertyAddCustomListResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "CustomProperty_AddCustomListResult");
    private final static QName _DocumentAddUrlVersionResponseDocumentAddUrlVersionResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_AddUrlVersionResult");
    private final static QName _TaskRetrieveNewResponseTaskRetrieveNewResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Task_RetrieveNewResult");
    private final static QName _AutomationHostUpdateRemoteAutomationHost_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteAutomationHost");
    private final static QName _TestRunRecordAutomated2RunnerMessage_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "runnerMessage");
    private final static QName _TestRunRecordAutomated2BuildId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "buildId");
    private final static QName _TestRunRecordAutomated2TestSetId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "testSetId");
    private final static QName _TestRunRecordAutomated2Password_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "password");
    private final static QName _TestRunRecordAutomated2RunnerName_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "runnerName");
    private final static QName _TestRunRecordAutomated2RunnerStackTrace_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "runnerStackTrace");
    private final static QName _TestRunRecordAutomated2RunnerTestName_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "runnerTestName");
    private final static QName _TestRunRecordAutomated2TestSetTestCaseId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "testSetTestCaseId");
    private final static QName _TestRunRecordAutomated2TesterUserId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "testerUserId");
    private final static QName _TestRunRecordAutomated2UserName_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "userName");
    private final static QName _DocumentAddUrlRemoteDocument_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteDocument");
    private final static QName _AutomationEngineCreateRemoteEngine_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteEngine");
    private final static QName _ReleaseRetrieve2ResponseReleaseRetrieve2Result_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Release_Retrieve2Result");
    private final static QName _RequirementCreate2ParentRequirementId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "parentRequirementId");
    private final static QName _RequirementCreate2RemoteRequirement_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteRequirement");
    private final static QName _RequirementCreateCommentRemoteComment_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteComment");
    private final static QName _AssociationRetrieveForArtifactResponseAssociationRetrieveForArtifactResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Association_RetrieveForArtifactResult");
    private final static QName _RemoteCustomListValueCustomPropertyValueId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CustomPropertyValueId");
    private final static QName _TaskCreateResponseTaskCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Task_CreateResult");
    private final static QName _DataMappingAddUserMappingsRemoteDataMappings_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteDataMappings");
    private final static QName _TestRunSaveResponseTestRunSaveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_SaveResult");
    private final static QName _CustomPropertyAddCustomListValueRemoteCustomListValue_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteCustomListValue");
    private final static QName _AutomationHostRetrieveByTokenToken_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "token");
    private final static QName _RequirementCreate2ResponseRequirementCreate2Result_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Requirement_Create2Result");
    private final static QName _RemoteTestSetTestCaseMappingOwnerId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "OwnerId");
    private final static QName _DocumentUpdateFolderRemoteDocumentFolder_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteDocumentFolder");
    private final static QName _ReleaseCreateResponseReleaseCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Release_CreateResult");
    private final static QName _BuildRetrieveByIdResponseBuildRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Build_RetrieveByIdResult");
    private final static QName _DataMappingRetrieveUserMappingsResponseDataMappingRetrieveUserMappingsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "DataMapping_RetrieveUserMappingsResult");
    private final static QName _RemoteProjectUserProjectRoleName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ProjectRoleName");
    private final static QName _TaskRetrieveByIdResponseTaskRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Task_RetrieveByIdResult");
    private final static QName _AutomationEngineCreateResponseAutomationEngineCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "AutomationEngine_CreateResult");
    private final static QName _RequirementRetrieveByIdResponseRequirementRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Requirement_RetrieveByIdResult");
    private final static QName _CustomPropertyAddCustomListRemoteCustomList_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteCustomList");
    private final static QName _TestCaseAddStepRemoteTestStep_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteTestStep");
    private final static QName _IncidentRetrieveCommentsResponseIncidentRetrieveCommentsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveCommentsResult");
    private final static QName _RemoteIncidentPriorityName2_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Name");
    private final static QName _RemoteIncidentPriorityColor_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Color");
    private final static QName _RemoteIncidentPriorityPriorityId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "PriorityId");
    private final static QName _AutomationEngineRetrieveResponseAutomationEngineRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "AutomationEngine_RetrieveResult");
    private final static QName _ReleaseUpdateRemoteRelease_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteRelease");
    private final static QName _IncidentRetrieveWorkflowFieldsResponseIncidentRetrieveWorkflowFieldsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveWorkflowFieldsResult");
    private final static QName _RemoteTestRunStepActualResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ActualResult");
    private final static QName _RemoteTestRunStepSampleData_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "SampleData");
    private final static QName _RemoteTestRunStepTestCaseId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestCaseId");
    private final static QName _RemoteTestRunStepExpectedResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ExpectedResult");
    private final static QName _RemoteTestRunStepTestStepId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestStepId");
    private final static QName _RemoteTestRunStepTestRunStepId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestRunStepId");
    private final static QName _RemoteTestStepLinkedTestCaseId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "LinkedTestCaseId");
    private final static QName _RemoteTestStepExecutionStatusId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ExecutionStatusId");
    private final static QName _ProjectRetrieveResponseProjectRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Project_RetrieveResult");
    private final static QName _RemoteWorkflowIncidentTransitionIncidentStatusNameOutput_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IncidentStatusName_Output");
    private final static QName _RemoteWorkflowIncidentTransitionIncidentStatusNameInput_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IncidentStatusName_Input");
    private final static QName _AutomationHostRetrieveByIdResponseAutomationHostRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "AutomationHost_RetrieveByIdResult");
    private final static QName _IncidentUpdateRemoteIncident_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteIncident");
    private final static QName _SystemGetSettingsResponseSystemGetSettingsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "System_GetSettingsResult");
    private final static QName _DataMappingRetrieveFieldValueMappingsResponseDataMappingRetrieveFieldValueMappingsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "DataMapping_RetrieveFieldValueMappingsResult");
    private final static QName _DocumentAddFileVersionBinaryData_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "binaryData");
    private final static QName _DocumentAddFileVersionRemoteDocumentVersion_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteDocumentVersion");
    private final static QName _CustomPropertyRetrieveForArtifactTypeResponseCustomPropertyRetrieveForArtifactTypeResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "CustomProperty_RetrieveForArtifactTypeResult");
    private final static QName _TestCaseAddUpdateAutomationScriptDescription_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "description");
    private final static QName _TestCaseAddUpdateAutomationScriptUrlOrFilename_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "urlOrFilename");
    private final static QName _TestCaseAddUpdateAutomationScriptProjectAttachmentFolderId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "projectAttachmentFolderId");
    private final static QName _TestCaseAddUpdateAutomationScriptAutomationEngineId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "automationEngineId");
    private final static QName _TestCaseAddUpdateAutomationScriptVersion_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "version");
    private final static QName _TestCaseAddUpdateAutomationScriptProjectAttachmentTypeId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "projectAttachmentTypeId");
    private final static QName _RemoteAutomationHostToken_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Token");
    private final static QName _RemoteAutomationHostAutomationHostId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AutomationHostId");
    private final static QName _ReleaseRetrieveCommentsResponseReleaseRetrieveCommentsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Release_RetrieveCommentsResult");
    private final static QName _TestCaseRetrieveParametersResponseTestCaseRetrieveParametersResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_RetrieveParametersResult");
    private final static QName _RemoteAssociationArtifactLinkId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArtifactLinkId");
    private final static QName _RemoteAssociationDestArtifactTypeName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "DestArtifactTypeName");
    private final static QName _RemoteAssociationCreationDate_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CreationDate");
    private final static QName _RemoteAssociationComment_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Comment");
    private final static QName _RemoteAssociationDestArtifactName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "DestArtifactName");
    private final static QName _RemoteDataMappingProjectId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ProjectId");
    private final static QName _RemoteDataMappingExternalKey_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ExternalKey");
    private final static QName _TaskCreateRemoteTask_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteTask");
    private final static QName _CustomPropertyAddDefinitionResponseCustomPropertyAddDefinitionResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "CustomProperty_AddDefinitionResult");
    private final static QName _TestCaseMoveStepDestinationTestStepId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "destinationTestStepId");
    private final static QName _RemoteArtifactCustomProperties_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CustomProperties");
    private final static QName _BuildCreateResponseBuildCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Build_CreateResult");
    private final static QName _RemoteTestStepParameterValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Value");
    private final static QName _RemoteIncidentProjectedEffort_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ProjectedEffort");
    private final static QName _RemoteIncidentPriorityName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "PriorityName");
    private final static QName _RemoteIncidentClosedDate_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ClosedDate");
    private final static QName _RemoteIncidentRemainingEffort_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RemainingEffort");
    private final static QName _RemoteIncidentProjectName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ProjectName");
    private final static QName _RemoteIncidentDetectedReleaseVersionNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "DetectedReleaseVersionNumber");
    private final static QName _RemoteIncidentActualEffort_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ActualEffort");
    private final static QName _RemoteIncidentSeverityId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "SeverityId");
    private final static QName _RemoteIncidentVerifiedReleaseVersionNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "VerifiedReleaseVersionNumber");
    private final static QName _RemoteIncidentOpenerId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "OpenerId");
    private final static QName _RemoteIncidentIncidentId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IncidentId");
    private final static QName _RemoteIncidentIncidentTypeId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IncidentTypeId");
    private final static QName _RemoteIncidentOpenerName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "OpenerName");
    private final static QName _RemoteIncidentResolvedReleaseVersionNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ResolvedReleaseVersionNumber");
    private final static QName _RemoteIncidentIncidentStatusOpenStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IncidentStatusOpenStatus");
    private final static QName _RemoteIncidentResolvedReleaseId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ResolvedReleaseId");
    private final static QName _RemoteIncidentEstimatedEffort_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "EstimatedEffort");
    private final static QName _RemoteIncidentOwnerName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "OwnerName");
    private final static QName _RemoteIncidentFixedBuildName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "FixedBuildName");
    private final static QName _RemoteIncidentFixedBuildId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "FixedBuildId");
    private final static QName _RemoteIncidentSeverityName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "SeverityName");
    private final static QName _RemoteIncidentDetectedReleaseId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "DetectedReleaseId");
    private final static QName _RemoteIncidentVerifiedReleaseId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "VerifiedReleaseId");
    private final static QName _RemoteIncidentStartDate_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "StartDate");
    private final static QName _RemoteIncidentIncidentTypeName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IncidentTypeName");
    private final static QName _RemoteIncidentIncidentStatusName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IncidentStatusName");
    private final static QName _CustomPropertyUpdateDefinitionRemoteCustomProperty_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteCustomProperty");
    private final static QName _IncidentRetrieveWorkflowTransitionsResponseIncidentRetrieveWorkflowTransitionsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveWorkflowTransitionsResult");
    private final static QName _IncidentAddSeverityResponseIncidentAddSeverityResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_AddSeverityResult");
    private final static QName _TestRunCreateForAutomationHostDateRange_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "dateRange");
    private final static QName _TestSetCreateParentTestSetFolderId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "parentTestSetFolderId");
    private final static QName _IncidentAddCommentsRemoteComments_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteComments");
    private final static QName _UserCreateProjectRoleId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "projectRoleId");
    private final static QName _UserCreatePasswordQuestion_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "passwordQuestion");
    private final static QName _UserCreateRemoteUser_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteUser");
    private final static QName _UserCreatePasswordAnswer_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "passwordAnswer");
    private final static QName _RemoteProjectRoleProjectRoleId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ProjectRoleId");
    private final static QName _TestSetMoveDestinationTestSetId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "destinationTestSetId");
    private final static QName _TestRunCreateFromTestCasesResponseTestRunCreateFromTestCasesResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_CreateFromTestCasesResult");
    private final static QName _TestRunRecordAutomated1ResponseTestRunRecordAutomated1Result_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_RecordAutomated1Result");
    private final static QName _RequirementRetrieveTestCoverageResponseRequirementRetrieveTestCoverageResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Requirement_RetrieveTestCoverageResult");
    private final static QName _TestCaseAddParameterResponseTestCaseAddParameterResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_AddParameterResult");
    private final static QName _ProjectRetrieveUserMembershipResponseProjectRetrieveUserMembershipResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Project_RetrieveUserMembershipResult");
    private final static QName _IncidentAddStatusRemoteIncidentStatus_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteIncidentStatus");
    private final static QName _IncidentRetrieveByTestStepResponseIncidentRetrieveByTestStepResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveByTestStepResult");
    private final static QName _RemoteProjectWebsite_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Website");
    private final static QName _DateRangeEndDate_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "EndDate");
    private final static QName _TestRunRecordAutomated3ResponseTestRunRecordAutomated3Result_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_RecordAutomated3Result");
    private final static QName _RemoteFilterStringValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "StringValue");
    private final static QName _RemoteFilterPropertyName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "PropertyName");
    private final static QName _RemoteFilterMultiValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "MultiValue");
    private final static QName _RemoteFilterIntValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IntValue");
    private final static QName _RemoteFilterDateRangeValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "DateRangeValue");
    private final static QName _RequirementRetrieveCommentsResponseRequirementRetrieveCommentsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Requirement_RetrieveCommentsResult");
    private final static QName _RemoteManualTestRunTestRunSteps_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestRunSteps");
    private final static QName _TestRunRetrieveManualByIdResponseTestRunRetrieveManualByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_RetrieveManualByIdResult");
    private final static QName _SystemGetArtifactUrlTabName_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "tabName");
    private final static QName _ReleaseRetrieveTestMappingResponseReleaseRetrieveTestMappingResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Release_RetrieveTestMappingResult");
    private final static QName _TestCaseCreateParameterTokenParameterName_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "parameterName");
    private final static QName _TestSetAddTestMappingExistingTestSetTestCaseId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "existingTestSetTestCaseId");
    private final static QName _TestSetAddTestMappingRemoteTestSetTestCaseMapping_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteTestSetTestCaseMapping");
    private final static QName _TestSetAddTestMappingParameters_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "parameters");
    private final static QName _DocumentAddFolderResponseDocumentAddFolderResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_AddFolderResult");
    private final static QName _TestSetRetrieveCommentsResponseTestSetRetrieveCommentsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestSet_RetrieveCommentsResult");
    private final static QName _DocumentRetrieveFolderByIdResponseDocumentRetrieveFolderByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_RetrieveFolderByIdResult");
    private final static QName _ProjectCreateExistingProjectId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "existingProjectId");
    private final static QName _ProjectCreateRemoteProject_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteProject");
    private final static QName _CustomPropertyRetrieveCustomListsResponseCustomPropertyRetrieveCustomListsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "CustomProperty_RetrieveCustomListsResult");
    private final static QName _TestRunRetrieveAutomatedByIdResponseTestRunRetrieveAutomatedByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_RetrieveAutomatedByIdResult");
    private final static QName _MultiValueFilterValues_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Values");
    private final static QName _TestSetCreateFolderResponseTestSetCreateFolderResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestSet_CreateFolderResult");
    private final static QName _DocumentRetrieveFoldersResponseDocumentRetrieveFoldersResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_RetrieveFoldersResult");
    private final static QName _RemoteTestCaseAutomationAttachmentId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AutomationAttachmentId");
    private final static QName _RemoteTestCaseEstimatedDuration_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "EstimatedDuration");
    private final static QName _RemoteTestCaseTestSteps_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestSteps");
    private final static QName _RemoteTestCaseTestCasePriorityName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestCasePriorityName");
    private final static QName _RemoteTestCaseExecutionDate_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ExecutionDate");
    private final static QName _RemoteTestCaseTestCasePriorityId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestCasePriorityId");
    private final static QName _RemoteTestCaseAutomationEngineId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AutomationEngineId");
    private final static QName _AssociationCreateResponseAssociationCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Association_CreateResult");
    private final static QName _RemoteTaskTaskPriorityId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TaskPriorityId");
    private final static QName _RemoteTaskTaskId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TaskId");
    private final static QName _RemoteTaskRequirementId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RequirementId");
    private final static QName _RemoteTaskRequirementName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RequirementName");
    private final static QName _RemoteTaskReleaseVersionNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ReleaseVersionNumber");
    private final static QName _RemoteTaskTaskStatusName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TaskStatusName");
    private final static QName _RemoteTaskTaskPriorityName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TaskPriorityName");
    private final static QName _IncidentAddSeverityRemoteIncidentSeverity_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteIncidentSeverity");
    private final static QName _ReleaseAddTestMappingRemoteReleaseTestCaseMapping_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteReleaseTestCaseMapping");
    private final static QName _BuildRetrieveByReleaseIdResponseBuildRetrieveByReleaseIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Build_RetrieveByReleaseIdResult");
    private final static QName _RemoteTestCaseParameterDefaultValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "DefaultValue");
    private final static QName _RemoteTestCaseParameterTestCaseParameterId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestCaseParameterId");
    private final static QName _TestCaseAddParameterRemoteTestCaseParameter_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteTestCaseParameter");
    private final static QName _DocumentRetrieveForFolderResponseDocumentRetrieveForFolderResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_RetrieveForFolderResult");
    private final static QName _IncidentRetrieveByIdResponseIncidentRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveByIdResult");
    private final static QName _RemoteWorkflowIncidentFieldsFieldCaption_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "FieldCaption");
    private final static QName _RemoteWorkflowIncidentFieldsFieldName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "FieldName");
    private final static QName _RemoteDocumentFolderParentProjectAttachmentFolderId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ParentProjectAttachmentFolderId");
    private final static QName _RemoteDocumentFolderProjectAttachmentFolderId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ProjectAttachmentFolderId");
    private final static QName _DocumentAddFileResponseDocumentAddFileResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_AddFileResult");
    private final static QName _RemoteRequirementCoverageCountBlocked_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CoverageCountBlocked");
    private final static QName _RemoteRequirementCoverageCountCaution_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CoverageCountCaution");
    private final static QName _RemoteRequirementCoverageCountFailed_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CoverageCountFailed");
    private final static QName _RemoteRequirementCoverageCountPassed_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CoverageCountPassed");
    private final static QName _RemoteRequirementCoverageCountTotal_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CoverageCountTotal");
    private final static QName _RemoteRequirementStatusName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "StatusName");
    private final static QName _RemoteRequirementImportanceId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ImportanceId");
    private final static QName _RemoteRequirementStatusId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "StatusId");
    private final static QName _RemoteRequirementImportanceName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ImportanceName");
    private final static QName _SystemGetProductVersionResponseSystemGetProductVersionResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "System_GetProductVersionResult");
    private final static QName _TestRunCreateFromTestSetResponseTestRunCreateFromTestSetResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_CreateFromTestSetResult");
    private final static QName _TestSetRetrieveForOwnerResponseTestSetRetrieveForOwnerResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestSet_RetrieveForOwnerResult");
    private final static QName _AssociationUpdateRemoteAssociation_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteAssociation");
    private final static QName _TestCaseRetrieveByReleaseIdResponseTestCaseRetrieveByReleaseIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_RetrieveByReleaseIdResult");
    private final static QName _RemoteDocumentTypeProjectAttachmentTypeId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ProjectAttachmentTypeId");
    private final static QName _TestCaseRetrieveForOwnerResponseTestCaseRetrieveForOwnerResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_RetrieveForOwnerResult");
    private final static QName _RemoteCommentCommentId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CommentId");
    private final static QName _RemoteCommentText_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Text");
    private final static QName _ReleaseMoveDestinationReleaseId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "destinationReleaseId");
    private final static QName _TestSetRetrieveTestCaseMappingResponseTestSetRetrieveTestCaseMappingResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestSet_RetrieveTestCaseMappingResult");
    private final static QName _ServiceFaultMessageStackTrace_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "StackTrace");
    private final static QName _ServiceFaultMessageType_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "Type");
    private final static QName _ServiceFaultMessageMessage_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "Message");
    private final static QName _IncidentRetrieveByTestCaseResponseIncidentRetrieveByTestCaseResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveByTestCaseResult");
    private final static QName _UserRetrieveByIdResponseUserRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "User_RetrieveByIdResult");
    private final static QName _UserCreateResponseUserCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "User_CreateResult");
    private final static QName _TestCaseCreateResponseTestCaseCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_CreateResult");
    private final static QName _RequirementCreate1ResponseRequirementCreate1Result_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Requirement_Create1Result");
    private final static QName _IncidentAddPriorityRemoteIncidentPriority_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteIncidentPriority");
    private final static QName _RemoteBuildSourceCodeRevisionKey_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RevisionKey");
    private final static QName _ProjectCreateResponseProjectCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Project_CreateResult");
    private final static QName _RequirementMoveDestinationRequirementId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "destinationRequirementId");
    private final static QName _ReleaseAddTestMapping2RemoteReleaseTestCaseMappings_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteReleaseTestCaseMappings");
    private final static QName _DataMappingRetrieveArtifactMappingsResponseDataMappingRetrieveArtifactMappingsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "DataMapping_RetrieveArtifactMappingsResult");
    private final static QName _TestSetRetrieveByIdResponseTestSetRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestSet_RetrieveByIdResult");
    private final static QName _RemoteArtifactCustomPropertyDefinition_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Definition");
    private final static QName _RemoteArtifactCustomPropertyDateTimeValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "DateTimeValue");
    private final static QName _RemoteArtifactCustomPropertyDecimalValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "DecimalValue");
    private final static QName _RemoteArtifactCustomPropertyBooleanValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "BooleanValue");
    private final static QName _RemoteArtifactCustomPropertyIntegerListValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IntegerListValue");
    private final static QName _RemoteArtifactCustomPropertyIntegerValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "IntegerValue");
    private final static QName _DataMappingRetrieveCustomPropertyValueMappingsResponseDataMappingRetrieveCustomPropertyValueMappingsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "DataMapping_RetrieveCustomPropertyValueMappingsResult");
    private final static QName _TestCaseCreateCommentResponseTestCaseCreateCommentResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_CreateCommentResult");
    private final static QName _TestRunCreateForAutomatedTestSetResponseTestRunCreateForAutomatedTestSetResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_CreateForAutomatedTestSetResult");
    private final static QName _DocumentRetrieveTypesResponseDocumentRetrieveTypesResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_RetrieveTypesResult");
    private final static QName _TestSetCreateResponseTestSetCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestSet_CreateResult");
    private final static QName _IncidentRetrieveForOwnerResponseIncidentRetrieveForOwnerResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveForOwnerResult");
    private final static QName _RequirementRetrieveResponseRequirementRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Requirement_RetrieveResult");
    private final static QName _TestCaseCreateFolderResponseTestCaseCreateFolderResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_CreateFolderResult");
    private final static QName _DocumentAddFileVersionResponseDocumentAddFileVersionResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_AddFileVersionResult");
    private final static QName _ReleaseCreateParentReleaseId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "parentReleaseId");
    private final static QName _TestSetAddTestMappingResponseTestSetAddTestMappingResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestSet_AddTestMappingResult");
    private final static QName _IncidentAddTypeRemoteIncidentType_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteIncidentType");
    private final static QName _RemoteBuildBuildStatusName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "BuildStatusName");
    private final static QName _RemoteBuildRevisions_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Revisions");
    private final static QName _RemoteBuildBuildId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "BuildId");
    private final static QName _TestRunRetrieveByIdResponseTestRunRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_RetrieveByIdResult");
    private final static QName _IncidentRetrieveWorkflowCustomPropertiesResponseIncidentRetrieveWorkflowCustomPropertiesResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveWorkflowCustomPropertiesResult");
    private final static QName _TestRunRetrieveAutomatedResponseTestRunRetrieveAutomatedResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_RetrieveAutomatedResult");
    private final static QName _ReleaseRetrieveResponseReleaseRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Release_RetrieveResult");
    private final static QName _RemoteCustomPropertyOptions_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Options");
    private final static QName _RemoteCustomPropertyCustomPropertyTypeName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CustomPropertyTypeName");
    private final static QName _RemoteCustomPropertyCustomList_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CustomList");
    private final static QName _RemoteCustomPropertySystemDataType_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "SystemDataType");
    private final static QName _RemoteCustomPropertyCustomPropertyId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CustomPropertyId");
    private final static QName _RemoteCustomPropertyCustomPropertyFieldName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CustomPropertyFieldName");
    private final static QName _TaskRetrieveCommentsResponseTaskRetrieveCommentsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Task_RetrieveCommentsResult");
    private final static QName _SystemGetProductNameResponseSystemGetProductNameResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "System_GetProductNameResult");
    private final static QName _IncidentRetrieveTypesResponseIncidentRetrieveTypesResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveTypesResult");
    private final static QName _IncidentRetrieveStatusesResponseIncidentRetrieveStatusesResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveStatusesResult");
    private final static QName _ValidationFaultMessageSummary_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "Summary");
    private final static QName _ValidationFaultMessageMessages_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "Messages");
    private final static QName _ProjectRetrieveByIdResponseProjectRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Project_RetrieveByIdResult");
    private final static QName _ConnectionAuthenticate2PlugInName_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "plugInName");
    private final static QName _TestCaseRetrieveByIdResponseTestCaseRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_RetrieveByIdResult");
    private final static QName _TestCaseMoveDestinationTestCaseId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "destinationTestCaseId");
    private final static QName _SystemGetWebServerUrlResponseSystemGetWebServerUrlResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "System_GetWebServerUrlResult");
    private final static QName _TestCaseRetrieveCommentsResponseTestCaseRetrieveCommentsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_RetrieveCommentsResult");
    private final static QName _AutomationEngineRetrieveByTokenResponseAutomationEngineRetrieveByTokenResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "AutomationEngine_RetrieveByTokenResult");
    private final static QName _IncidentAddCommentsResponseIncidentAddCommentsResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_AddCommentsResult");
    private final static QName _TestCaseCreateParameterTokenResponseTestCaseCreateParameterTokenResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_CreateParameterTokenResult");
    private final static QName _DocumentRetrieveForArtifactResponseDocumentRetrieveForArtifactResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_RetrieveForArtifactResult");
    private final static QName _RemoteDocumentProjectAttachmentTypeName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ProjectAttachmentTypeName");
    private final static QName _RemoteDocumentEditorName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "EditorName");
    private final static QName _RemoteDocumentArtifactId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArtifactId");
    private final static QName _RemoteDocumentArtifactTypeId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ArtifactTypeId");
    private final static QName _RemoteDocumentVersions_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Versions");
    private final static QName _RemoteDocumentAttachmentId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AttachmentId");
    private final static QName _RemoteDocumentAttachmentTypeName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AttachmentTypeName");
    private final static QName _RemoteDocumentCurrentVersion_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CurrentVersion");
    private final static QName _RemoteDocumentEditorId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "EditorId");
    private final static QName _RemoteDocumentTags_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Tags");
    private final static QName _TestRunRetrieveResponseTestRunRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_RetrieveResult");
    private final static QName _CustomPropertyAddDefinitionCustomListId_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "customListId");
    private final static QName _RequirementRetrieveForOwnerResponseRequirementRetrieveForOwnerResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Requirement_RetrieveForOwnerResult");
    private final static QName _IncidentAddPriorityResponseIncidentAddPriorityResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_AddPriorityResult");
    private final static QName _IncidentRetrieveByTestRunStepResponseIncidentRetrieveByTestRunStepResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveByTestRunStepResult");
    private final static QName _IncidentAddStatusResponseIncidentAddStatusResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_AddStatusResult");
    private final static QName _TestCaseRetrieveResponseTestCaseRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_RetrieveResult");
    private final static QName _TestRunRecordAutomated1RemoteTestRun_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteTestRun");
    private final static QName _TestCaseAddStepResponseTestCaseAddStepResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_AddStepResult");
    private final static QName _RemoteAutomatedTestRunRunnerTestName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RunnerTestName");
    private final static QName _RemoteAutomatedTestRunRunnerMessage_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RunnerMessage");
    private final static QName _RemoteAutomatedTestRunRunnerAssertCount_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RunnerAssertCount");
    private final static QName _RemoteAutomatedTestRunScheduledDate_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ScheduledDate");
    private final static QName _RemoteAutomatedTestRunRunnerStackTrace_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RunnerStackTrace");
    private final static QName _RemoteAutomatedTestRunAutomationEngineToken_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "AutomationEngineToken");
    private final static QName _RemoteAutomatedTestRunRunnerName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RunnerName");
    private final static QName _RemoteAutomatedTestRunParameters_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "Parameters");
    private final static QName _AutomationHostRetrieveResponseAutomationHostRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "AutomationHost_RetrieveResult");
    private final static QName _DocumentRetrieveByIdResponseDocumentRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_RetrieveByIdResult");
    private final static QName _IncidentCreateResponseIncidentCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_CreateResult");
    private final static QName _DocumentOpenFileResponseDocumentOpenFileResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Document_OpenFileResult");
    private final static QName _TestCaseRetrieveStepParametersResponseTestCaseRetrieveStepParametersResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_RetrieveStepParametersResult");
    private final static QName _TestRunRetrieveManualResponseTestRunRetrieveManualResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_RetrieveManualResult");
    private final static QName _ProjectRoleRetrieveResponseProjectRoleRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "ProjectRole_RetrieveResult");
    private final static QName _IncidentRetrieveResponseIncidentRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveResult");
    private final static QName _AutomationEngineRetrieveByIdResponseAutomationEngineRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "AutomationEngine_RetrieveByIdResult");
    private final static QName _SystemGetArtifactUrlResponseSystemGetArtifactUrlResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "System_GetArtifactUrlResult");
    private final static QName _ReleaseRetrieveByIdResponseReleaseRetrieveByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Release_RetrieveByIdResult");
    private final static QName _TestRunCreateForAutomationHostResponseTestRunCreateForAutomationHostResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestRun_CreateForAutomationHostResult");
    private final static QName _TaskRetrieveForOwnerResponseTaskRetrieveForOwnerResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Task_RetrieveForOwnerResult");
    private final static QName _TaskCreateCommentResponseTaskCreateCommentResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Task_CreateCommentResult");
    private final static QName _TestCaseRetrieveByTestSetIdResponseTestCaseRetrieveByTestSetIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_RetrieveByTestSetIdResult");
    private final static QName _RemoteTestRunTesterId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TesterId");
    private final static QName _RemoteTestRunActualDuration_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "ActualDuration");
    private final static QName _RemoteTestRunTestRunId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestRunId");
    private final static QName _RemoteTestRunTestSetTestCaseId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestSetTestCaseId");
    private final static QName _RemoteTestRunTestSetId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestSetId");
    private final static QName _UserRetrieveByUserNameResponseUserRetrieveByUserNameResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "User_RetrieveByUserNameResult");
    private final static QName _BuildCreateRemoteBuild_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "remoteBuild");
    private final static QName _ValidationFaultMessageItemFieldName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", "FieldName");
    private final static QName _CustomPropertyRetrieveCustomListByIdResponseCustomPropertyRetrieveCustomListByIdResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "CustomProperty_RetrieveCustomListByIdResult");
    private final static QName _RequirementCreateCommentResponseRequirementCreateCommentResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Requirement_CreateCommentResult");
    private final static QName _IncidentRetrievePrioritiesResponseIncidentRetrievePrioritiesResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrievePrioritiesResult");
    private final static QName _AutomationHostCreateResponseAutomationHostCreateResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "AutomationHost_CreateResult");
    private final static QName _TestSetRetrieveResponseTestSetRetrieveResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestSet_RetrieveResult");
    private final static QName _RemoteTestSetRecurrenceName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RecurrenceName");
    private final static QName _RemoteTestSetCountCaution_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CountCaution");
    private final static QName _RemoteTestSetCountFailed_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CountFailed");
    private final static QName _RemoteTestSetCountNotRun_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CountNotRun");
    private final static QName _RemoteTestSetPlannedDate_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "PlannedDate");
    private final static QName _RemoteTestSetCountBlocked_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CountBlocked");
    private final static QName _RemoteTestSetTestRunTypeId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestRunTypeId");
    private final static QName _RemoteTestSetTestSetStatusName_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "TestSetStatusName");
    private final static QName _RemoteTestSetRecurrenceId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "RecurrenceId");
    private final static QName _RemoteTestSetCountNotApplicable_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CountNotApplicable");
    private final static QName _RemoteTestSetCountPassed_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CountPassed");
    private final static QName _TestCaseRetrieveByFolderResponseTestCaseRetrieveByFolderResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "TestCase_RetrieveByFolderResult");
    private final static QName _RemoteCustomListCustomPropertyListId_QNAME = new QName("http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", "CustomPropertyListId");
    private final static QName _IncidentRetrieveNewResponseIncidentRetrieveNewResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Incident_RetrieveNewResult");
    private final static QName _ReleaseCreateCommentResponseReleaseCreateCommentResult_QNAME = new QName("http://www.inflectra.com/SpiraTest/Services/v4.0/", "Release_CreateCommentResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.inflectra.spirateam.mylyn.core.internal.services.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTestStep }
     * 
     */
    public ArrayOfRemoteTestStep createArrayOfRemoteTestStep() {
        return new ArrayOfRemoteTestStep();
    }

    /**
     * Create an instance of {@link TestRunCreateForAutomatedTestSet }
     * 
     */
    public TestRunCreateForAutomatedTestSet createTestRunCreateForAutomatedTestSet() {
        return new TestRunCreateForAutomatedTestSet();
    }

    /**
     * Create an instance of {@link TestCaseDeleteResponse }
     * 
     */
    public TestCaseDeleteResponse createTestCaseDeleteResponse() {
        return new TestCaseDeleteResponse();
    }

    /**
     * Create an instance of {@link AutomationHostRetrieveByTokenResponse }
     * 
     */
    public AutomationHostRetrieveByTokenResponse createAutomationHostRetrieveByTokenResponse() {
        return new AutomationHostRetrieveByTokenResponse();
    }

    /**
     * Create an instance of {@link TestRunSave }
     * 
     */
    public TestRunSave createTestRunSave() {
        return new TestRunSave();
    }

    /**
     * Create an instance of {@link RemoteUser }
     * 
     */
    public RemoteUser createRemoteUser() {
        return new RemoteUser();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveByTestSetId }
     * 
     */
    public TestCaseRetrieveByTestSetId createTestCaseRetrieveByTestSetId() {
        return new TestCaseRetrieveByTestSetId();
    }

    /**
     * Create an instance of {@link RequirementRemoveTestCoverageResponse }
     * 
     */
    public RequirementRemoveTestCoverageResponse createRequirementRemoveTestCoverageResponse() {
        return new RequirementRemoveTestCoverageResponse();
    }

    /**
     * Create an instance of {@link TestSetRetrieve }
     * 
     */
    public TestSetRetrieve createTestSetRetrieve() {
        return new TestSetRetrieve();
    }

    /**
     * Create an instance of {@link TestRunCreateFromTestCases }
     * 
     */
    public TestRunCreateFromTestCases createTestRunCreateFromTestCases() {
        return new TestRunCreateFromTestCases();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveComments }
     * 
     */
    public TestCaseRetrieveComments createTestCaseRetrieveComments() {
        return new TestCaseRetrieveComments();
    }

    /**
     * Create an instance of {@link DocumentDeleteFromArtifactResponse }
     * 
     */
    public DocumentDeleteFromArtifactResponse createDocumentDeleteFromArtifactResponse() {
        return new DocumentDeleteFromArtifactResponse();
    }

    /**
     * Create an instance of {@link DocumentDeleteFolderResponse }
     * 
     */
    public DocumentDeleteFolderResponse createDocumentDeleteFolderResponse() {
        return new DocumentDeleteFolderResponse();
    }

    /**
     * Create an instance of {@link CustomPropertyAddCustomListValueResponse }
     * 
     */
    public CustomPropertyAddCustomListValueResponse createCustomPropertyAddCustomListValueResponse() {
        return new CustomPropertyAddCustomListValueResponse();
    }

    /**
     * Create an instance of {@link IncidentAddTypeResponse }
     * 
     */
    public IncidentAddTypeResponse createIncidentAddTypeResponse() {
        return new IncidentAddTypeResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteFilter }
     * 
     */
    public ArrayOfRemoteFilter createArrayOfRemoteFilter() {
        return new ArrayOfRemoteFilter();
    }

    /**
     * Create an instance of {@link CustomPropertyAddCustomListResponse }
     * 
     */
    public CustomPropertyAddCustomListResponse createCustomPropertyAddCustomListResponse() {
        return new CustomPropertyAddCustomListResponse();
    }

    /**
     * Create an instance of {@link TestRunRecordAutomated2 }
     * 
     */
    public TestRunRecordAutomated2 createTestRunRecordAutomated2() {
        return new TestRunRecordAutomated2();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteCustomList }
     * 
     */
    public ArrayOfRemoteCustomList createArrayOfRemoteCustomList() {
        return new ArrayOfRemoteCustomList();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteIncidentPriority }
     * 
     */
    public ArrayOfRemoteIncidentPriority createArrayOfRemoteIncidentPriority() {
        return new ArrayOfRemoteIncidentPriority();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteDocumentVersion }
     * 
     */
    public ArrayOfRemoteDocumentVersion createArrayOfRemoteDocumentVersion() {
        return new ArrayOfRemoteDocumentVersion();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteRequirement }
     * 
     */
    public ArrayOfRemoteRequirement createArrayOfRemoteRequirement() {
        return new ArrayOfRemoteRequirement();
    }

    /**
     * Create an instance of {@link RequirementCreate2 }
     * 
     */
    public RequirementCreate2 createRequirementCreate2() {
        return new RequirementCreate2();
    }

    /**
     * Create an instance of {@link RequirementCreateComment }
     * 
     */
    public RequirementCreateComment createRequirementCreateComment() {
        return new RequirementCreateComment();
    }

    /**
     * Create an instance of {@link TestSetRetrieveComments }
     * 
     */
    public TestSetRetrieveComments createTestSetRetrieveComments() {
        return new TestSetRetrieveComments();
    }

    /**
     * Create an instance of {@link RemoteCustomListValue }
     * 
     */
    public RemoteCustomListValue createRemoteCustomListValue() {
        return new RemoteCustomListValue();
    }

    /**
     * Create an instance of {@link TaskCreateResponse }
     * 
     */
    public TaskCreateResponse createTaskCreateResponse() {
        return new TaskCreateResponse();
    }

    /**
     * Create an instance of {@link DataMappingAddUserMappings }
     * 
     */
    public DataMappingAddUserMappings createDataMappingAddUserMappings() {
        return new DataMappingAddUserMappings();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveProjectMappings }
     * 
     */
    public DataMappingRetrieveProjectMappings createDataMappingRetrieveProjectMappings() {
        return new DataMappingRetrieveProjectMappings();
    }

    /**
     * Create an instance of {@link TestRunSaveResponse }
     * 
     */
    public TestRunSaveResponse createTestRunSaveResponse() {
        return new TestRunSaveResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteDocumentType }
     * 
     */
    public ArrayOfRemoteDocumentType createArrayOfRemoteDocumentType() {
        return new ArrayOfRemoteDocumentType();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTestCaseParameter }
     * 
     */
    public ArrayOfRemoteTestCaseParameter createArrayOfRemoteTestCaseParameter() {
        return new ArrayOfRemoteTestCaseParameter();
    }

    /**
     * Create an instance of {@link AutomationHostRetrieveByToken }
     * 
     */
    public AutomationHostRetrieveByToken createAutomationHostRetrieveByToken() {
        return new AutomationHostRetrieveByToken();
    }

    /**
     * Create an instance of {@link ConnectionConnectToProjectResponse }
     * 
     */
    public ConnectionConnectToProjectResponse createConnectionConnectToProjectResponse() {
        return new ConnectionConnectToProjectResponse();
    }

    /**
     * Create an instance of {@link RequirementCreate2Response }
     * 
     */
    public RequirementCreate2Response createRequirementCreate2Response() {
        return new RequirementCreate2Response();
    }

    /**
     * Create an instance of {@link ArrayOfValidationFaultMessageItem }
     * 
     */
    public ArrayOfValidationFaultMessageItem createArrayOfValidationFaultMessageItem() {
        return new ArrayOfValidationFaultMessageItem();
    }

    /**
     * Create an instance of {@link DocumentUpdateFolder }
     * 
     */
    public DocumentUpdateFolder createDocumentUpdateFolder() {
        return new DocumentUpdateFolder();
    }

    /**
     * Create an instance of {@link ReleaseCreateResponse }
     * 
     */
    public ReleaseCreateResponse createReleaseCreateResponse() {
        return new ReleaseCreateResponse();
    }

    /**
     * Create an instance of {@link BuildRetrieveByIdResponse }
     * 
     */
    public BuildRetrieveByIdResponse createBuildRetrieveByIdResponse() {
        return new BuildRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link RemoteProjectUser }
     * 
     */
    public RemoteProjectUser createRemoteProjectUser() {
        return new RemoteProjectUser();
    }

    /**
     * Create an instance of {@link AutomationEngineCreateResponse }
     * 
     */
    public AutomationEngineCreateResponse createAutomationEngineCreateResponse() {
        return new AutomationEngineCreateResponse();
    }

    /**
     * Create an instance of {@link DocumentDeleteFolder }
     * 
     */
    public DocumentDeleteFolder createDocumentDeleteFolder() {
        return new DocumentDeleteFolder();
    }

    /**
     * Create an instance of {@link CustomPropertyAddCustomList }
     * 
     */
    public CustomPropertyAddCustomList createCustomPropertyAddCustomList() {
        return new CustomPropertyAddCustomList();
    }

    /**
     * Create an instance of {@link CustomPropertyDeleteDefinitionResponse }
     * 
     */
    public CustomPropertyDeleteDefinitionResponse createCustomPropertyDeleteDefinitionResponse() {
        return new CustomPropertyDeleteDefinitionResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTestSet }
     * 
     */
    public ArrayOfRemoteTestSet createArrayOfRemoteTestSet() {
        return new ArrayOfRemoteTestSet();
    }

    /**
     * Create an instance of {@link TestRunRetrieve }
     * 
     */
    public TestRunRetrieve createTestRunRetrieve() {
        return new TestRunRetrieve();
    }

    /**
     * Create an instance of {@link TaskUpdateResponse }
     * 
     */
    public TaskUpdateResponse createTaskUpdateResponse() {
        return new TaskUpdateResponse();
    }

    /**
     * Create an instance of {@link RequirementMoveResponse }
     * 
     */
    public RequirementMoveResponse createRequirementMoveResponse() {
        return new RequirementMoveResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieveCommentsResponse }
     * 
     */
    public IncidentRetrieveCommentsResponse createIncidentRetrieveCommentsResponse() {
        return new IncidentRetrieveCommentsResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteIncidentStatus }
     * 
     */
    public ArrayOfRemoteIncidentStatus createArrayOfRemoteIncidentStatus() {
        return new ArrayOfRemoteIncidentStatus();
    }

    /**
     * Create an instance of {@link ReleaseUpdate }
     * 
     */
    public ReleaseUpdate createReleaseUpdate() {
        return new ReleaseUpdate();
    }

    /**
     * Create an instance of {@link RemoteTestRunStep }
     * 
     */
    public RemoteTestRunStep createRemoteTestRunStep() {
        return new RemoteTestRunStep();
    }

    /**
     * Create an instance of {@link RemoteTestStep }
     * 
     */
    public RemoteTestStep createRemoteTestStep() {
        return new RemoteTestStep();
    }

    /**
     * Create an instance of {@link DocumentAddFolder }
     * 
     */
    public DocumentAddFolder createDocumentAddFolder() {
        return new DocumentAddFolder();
    }

    /**
     * Create an instance of {@link TaskRetrieveComments }
     * 
     */
    public TaskRetrieveComments createTaskRetrieveComments() {
        return new TaskRetrieveComments();
    }

    /**
     * Create an instance of {@link ProjectRoleRetrieve }
     * 
     */
    public ProjectRoleRetrieve createProjectRoleRetrieve() {
        return new ProjectRoleRetrieve();
    }

    /**
     * Create an instance of {@link TaskDeleteResponse }
     * 
     */
    public TaskDeleteResponse createTaskDeleteResponse() {
        return new TaskDeleteResponse();
    }

    /**
     * Create an instance of {@link RemoteWorkflowIncidentTransition }
     * 
     */
    public RemoteWorkflowIncidentTransition createRemoteWorkflowIncidentTransition() {
        return new RemoteWorkflowIncidentTransition();
    }

    /**
     * Create an instance of {@link AutomationHostRetrieveByIdResponse }
     * 
     */
    public AutomationHostRetrieveByIdResponse createAutomationHostRetrieveByIdResponse() {
        return new AutomationHostRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link IncidentUpdate }
     * 
     */
    public IncidentUpdate createIncidentUpdate() {
        return new IncidentUpdate();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteDocumentFolder }
     * 
     */
    public ArrayOfRemoteDocumentFolder createArrayOfRemoteDocumentFolder() {
        return new ArrayOfRemoteDocumentFolder();
    }

    /**
     * Create an instance of {@link SystemGetProductVersion }
     * 
     */
    public SystemGetProductVersion createSystemGetProductVersion() {
        return new SystemGetProductVersion();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveFieldValueMappingsResponse }
     * 
     */
    public DataMappingRetrieveFieldValueMappingsResponse createDataMappingRetrieveFieldValueMappingsResponse() {
        return new DataMappingRetrieveFieldValueMappingsResponse();
    }

    /**
     * Create an instance of {@link DocumentAddFileVersion }
     * 
     */
    public DocumentAddFileVersion createDocumentAddFileVersion() {
        return new DocumentAddFileVersion();
    }

    /**
     * Create an instance of {@link TaskDelete }
     * 
     */
    public TaskDelete createTaskDelete() {
        return new TaskDelete();
    }

    /**
     * Create an instance of {@link TestCaseMoveResponse }
     * 
     */
    public TestCaseMoveResponse createTestCaseMoveResponse() {
        return new TestCaseMoveResponse();
    }

    /**
     * Create an instance of {@link RemoteAutomationHost }
     * 
     */
    public RemoteAutomationHost createRemoteAutomationHost() {
        return new RemoteAutomationHost();
    }

    /**
     * Create an instance of {@link ReleaseAddTestMappingResponse }
     * 
     */
    public ReleaseAddTestMappingResponse createReleaseAddTestMappingResponse() {
        return new ReleaseAddTestMappingResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteRequirementTestCaseMapping }
     * 
     */
    public ArrayOfRemoteRequirementTestCaseMapping createArrayOfRemoteRequirementTestCaseMapping() {
        return new ArrayOfRemoteRequirementTestCaseMapping();
    }

    /**
     * Create an instance of {@link TaskCreate }
     * 
     */
    public TaskCreate createTaskCreate() {
        return new TaskCreate();
    }

    /**
     * Create an instance of {@link RemoteDataMapping }
     * 
     */
    public RemoteDataMapping createRemoteDataMapping() {
        return new RemoteDataMapping();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteProjectRole }
     * 
     */
    public ArrayOfRemoteProjectRole createArrayOfRemoteProjectRole() {
        return new ArrayOfRemoteProjectRole();
    }

    /**
     * Create an instance of {@link ReleaseRetrieveById }
     * 
     */
    public ReleaseRetrieveById createReleaseRetrieveById() {
        return new ReleaseRetrieveById();
    }

    /**
     * Create an instance of {@link CustomPropertyAddDefinitionResponse }
     * 
     */
    public CustomPropertyAddDefinitionResponse createCustomPropertyAddDefinitionResponse() {
        return new CustomPropertyAddDefinitionResponse();
    }

    /**
     * Create an instance of {@link RequirementDeleteResponse }
     * 
     */
    public RequirementDeleteResponse createRequirementDeleteResponse() {
        return new RequirementDeleteResponse();
    }

    /**
     * Create an instance of {@link ConnectionAuthenticate2Response }
     * 
     */
    public ConnectionAuthenticate2Response createConnectionAuthenticate2Response() {
        return new ConnectionAuthenticate2Response();
    }

    /**
     * Create an instance of {@link BuildCreateResponse }
     * 
     */
    public BuildCreateResponse createBuildCreateResponse() {
        return new BuildCreateResponse();
    }

    /**
     * Create an instance of {@link TestSetRetrieveForOwner }
     * 
     */
    public TestSetRetrieveForOwner createTestSetRetrieveForOwner() {
        return new TestSetRetrieveForOwner();
    }

    /**
     * Create an instance of {@link RemoteTestStepParameter }
     * 
     */
    public RemoteTestStepParameter createRemoteTestStepParameter() {
        return new RemoteTestStepParameter();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTestSetTestCaseParameter }
     * 
     */
    public ArrayOfRemoteTestSetTestCaseParameter createArrayOfRemoteTestSetTestCaseParameter() {
        return new ArrayOfRemoteTestSetTestCaseParameter();
    }

    /**
     * Create an instance of {@link RemoteIncident }
     * 
     */
    public RemoteIncident createRemoteIncident() {
        return new RemoteIncident();
    }

    /**
     * Create an instance of {@link IncidentRetrieveWorkflowTransitionsResponse }
     * 
     */
    public IncidentRetrieveWorkflowTransitionsResponse createIncidentRetrieveWorkflowTransitionsResponse() {
        return new IncidentRetrieveWorkflowTransitionsResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieveStatuses }
     * 
     */
    public IncidentRetrieveStatuses createIncidentRetrieveStatuses() {
        return new IncidentRetrieveStatuses();
    }

    /**
     * Create an instance of {@link SystemGetServerDateTime }
     * 
     */
    public SystemGetServerDateTime createSystemGetServerDateTime() {
        return new SystemGetServerDateTime();
    }

    /**
     * Create an instance of {@link CustomPropertyUpdateCustomListResponse }
     * 
     */
    public CustomPropertyUpdateCustomListResponse createCustomPropertyUpdateCustomListResponse() {
        return new CustomPropertyUpdateCustomListResponse();
    }

    /**
     * Create an instance of {@link ProjectDeleteResponse }
     * 
     */
    public ProjectDeleteResponse createProjectDeleteResponse() {
        return new ProjectDeleteResponse();
    }

    /**
     * Create an instance of {@link ConnectionAuthenticate }
     * 
     */
    public ConnectionAuthenticate createConnectionAuthenticate() {
        return new ConnectionAuthenticate();
    }

    /**
     * Create an instance of {@link IncidentAddComments }
     * 
     */
    public IncidentAddComments createIncidentAddComments() {
        return new IncidentAddComments();
    }

    /**
     * Create an instance of {@link TestRunRetrieveAutomatedById }
     * 
     */
    public TestRunRetrieveAutomatedById createTestRunRetrieveAutomatedById() {
        return new TestRunRetrieveAutomatedById();
    }

    /**
     * Create an instance of {@link RequirementRetrieveTestCoverageResponse }
     * 
     */
    public RequirementRetrieveTestCoverageResponse createRequirementRetrieveTestCoverageResponse() {
        return new RequirementRetrieveTestCoverageResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieveByTestStep }
     * 
     */
    public IncidentRetrieveByTestStep createIncidentRetrieveByTestStep() {
        return new IncidentRetrieveByTestStep();
    }

    /**
     * Create an instance of {@link IncidentAddStatus }
     * 
     */
    public IncidentAddStatus createIncidentAddStatus() {
        return new IncidentAddStatus();
    }

    /**
     * Create an instance of {@link IncidentRetrieveByTestStepResponse }
     * 
     */
    public IncidentRetrieveByTestStepResponse createIncidentRetrieveByTestStepResponse() {
        return new IncidentRetrieveByTestStepResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteAutomationEngine }
     * 
     */
    public ArrayOfRemoteAutomationEngine createArrayOfRemoteAutomationEngine() {
        return new ArrayOfRemoteAutomationEngine();
    }

    /**
     * Create an instance of {@link RemoteProject }
     * 
     */
    public RemoteProject createRemoteProject() {
        return new RemoteProject();
    }

    /**
     * Create an instance of {@link DocumentRetrieveForArtifact }
     * 
     */
    public DocumentRetrieveForArtifact createDocumentRetrieveForArtifact() {
        return new DocumentRetrieveForArtifact();
    }

    /**
     * Create an instance of {@link TestRunRecordAutomated3Response }
     * 
     */
    public TestRunRecordAutomated3Response createTestRunRecordAutomated3Response() {
        return new TestRunRecordAutomated3Response();
    }

    /**
     * Create an instance of {@link RemoteFilter }
     * 
     */
    public RemoteFilter createRemoteFilter() {
        return new RemoteFilter();
    }

    /**
     * Create an instance of {@link RequirementRetrieveCommentsResponse }
     * 
     */
    public RequirementRetrieveCommentsResponse createRequirementRetrieveCommentsResponse() {
        return new RequirementRetrieveCommentsResponse();
    }

    /**
     * Create an instance of {@link TestRunRetrieveManualByIdResponse }
     * 
     */
    public TestRunRetrieveManualByIdResponse createTestRunRetrieveManualByIdResponse() {
        return new TestRunRetrieveManualByIdResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTestCase }
     * 
     */
    public ArrayOfRemoteTestCase createArrayOfRemoteTestCase() {
        return new ArrayOfRemoteTestCase();
    }

    /**
     * Create an instance of {@link RequirementDelete }
     * 
     */
    public RequirementDelete createRequirementDelete() {
        return new RequirementDelete();
    }

    /**
     * Create an instance of {@link AssociationRetrieveForArtifact }
     * 
     */
    public AssociationRetrieveForArtifact createAssociationRetrieveForArtifact() {
        return new AssociationRetrieveForArtifact();
    }

    /**
     * Create an instance of {@link ReleaseRetrieveTestMappingResponse }
     * 
     */
    public ReleaseRetrieveTestMappingResponse createReleaseRetrieveTestMappingResponse() {
        return new ReleaseRetrieveTestMappingResponse();
    }

    /**
     * Create an instance of {@link TestCaseCreateParameterToken }
     * 
     */
    public TestCaseCreateParameterToken createTestCaseCreateParameterToken() {
        return new TestCaseCreateParameterToken();
    }

    /**
     * Create an instance of {@link TestSetAddTestMapping }
     * 
     */
    public TestSetAddTestMapping createTestSetAddTestMapping() {
        return new TestSetAddTestMapping();
    }

    /**
     * Create an instance of {@link DocumentAddFolderResponse }
     * 
     */
    public DocumentAddFolderResponse createDocumentAddFolderResponse() {
        return new DocumentAddFolderResponse();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveForOwner }
     * 
     */
    public TestCaseRetrieveForOwner createTestCaseRetrieveForOwner() {
        return new TestCaseRetrieveForOwner();
    }

    /**
     * Create an instance of {@link TestSetRetrieveCommentsResponse }
     * 
     */
    public TestSetRetrieveCommentsResponse createTestSetRetrieveCommentsResponse() {
        return new TestSetRetrieveCommentsResponse();
    }

    /**
     * Create an instance of {@link DocumentRetrieveFolderByIdResponse }
     * 
     */
    public DocumentRetrieveFolderByIdResponse createDocumentRetrieveFolderByIdResponse() {
        return new DocumentRetrieveFolderByIdResponse();
    }

    /**
     * Create an instance of {@link ProjectCreate }
     * 
     */
    public ProjectCreate createProjectCreate() {
        return new ProjectCreate();
    }

    /**
     * Create an instance of {@link CustomPropertyRetrieveCustomListById }
     * 
     */
    public CustomPropertyRetrieveCustomListById createCustomPropertyRetrieveCustomListById() {
        return new CustomPropertyRetrieveCustomListById();
    }

    /**
     * Create an instance of {@link MultiValueFilter }
     * 
     */
    public MultiValueFilter createMultiValueFilter() {
        return new MultiValueFilter();
    }

    /**
     * Create an instance of {@link DocumentRetrieveFoldersResponse }
     * 
     */
    public DocumentRetrieveFoldersResponse createDocumentRetrieveFoldersResponse() {
        return new DocumentRetrieveFoldersResponse();
    }

    /**
     * Create an instance of {@link RemoteCustomPropertyOption }
     * 
     */
    public RemoteCustomPropertyOption createRemoteCustomPropertyOption() {
        return new RemoteCustomPropertyOption();
    }

    /**
     * Create an instance of {@link DocumentRetrieveTypes }
     * 
     */
    public DocumentRetrieveTypes createDocumentRetrieveTypes() {
        return new DocumentRetrieveTypes();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTestRun }
     * 
     */
    public ArrayOfRemoteTestRun createArrayOfRemoteTestRun() {
        return new ArrayOfRemoteTestRun();
    }

    /**
     * Create an instance of {@link RequirementRemoveTestCoverage }
     * 
     */
    public RequirementRemoveTestCoverage createRequirementRemoveTestCoverage() {
        return new RequirementRemoveTestCoverage();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteAssociation }
     * 
     */
    public ArrayOfRemoteAssociation createArrayOfRemoteAssociation() {
        return new ArrayOfRemoteAssociation();
    }

    /**
     * Create an instance of {@link RemoteTask }
     * 
     */
    public RemoteTask createRemoteTask() {
        return new RemoteTask();
    }

    /**
     * Create an instance of {@link TestSetCreateComment }
     * 
     */
    public TestSetCreateComment createTestSetCreateComment() {
        return new TestSetCreateComment();
    }

    /**
     * Create an instance of {@link IncidentAddSeverity }
     * 
     */
    public IncidentAddSeverity createIncidentAddSeverity() {
        return new IncidentAddSeverity();
    }

    /**
     * Create an instance of {@link ReleaseAddTestMapping }
     * 
     */
    public ReleaseAddTestMapping createReleaseAddTestMapping() {
        return new ReleaseAddTestMapping();
    }

    /**
     * Create an instance of {@link RemoteTestCaseParameter }
     * 
     */
    public RemoteTestCaseParameter createRemoteTestCaseParameter() {
        return new RemoteTestCaseParameter();
    }

    /**
     * Create an instance of {@link DocumentRetrieveForFolderResponse }
     * 
     */
    public DocumentRetrieveForFolderResponse createDocumentRetrieveForFolderResponse() {
        return new DocumentRetrieveForFolderResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieveByIdResponse }
     * 
     */
    public IncidentRetrieveByIdResponse createIncidentRetrieveByIdResponse() {
        return new IncidentRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link RemoteDocumentFolder }
     * 
     */
    public RemoteDocumentFolder createRemoteDocumentFolder() {
        return new RemoteDocumentFolder();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteDocument }
     * 
     */
    public ArrayOfRemoteDocument createArrayOfRemoteDocument() {
        return new ArrayOfRemoteDocument();
    }

    /**
     * Create an instance of {@link AssociationUpdateResponse }
     * 
     */
    public AssociationUpdateResponse createAssociationUpdateResponse() {
        return new AssociationUpdateResponse();
    }

    /**
     * Create an instance of {@link DocumentAddFileResponse }
     * 
     */
    public DocumentAddFileResponse createDocumentAddFileResponse() {
        return new DocumentAddFileResponse();
    }

    /**
     * Create an instance of {@link SystemGetProductVersionResponse }
     * 
     */
    public SystemGetProductVersionResponse createSystemGetProductVersionResponse() {
        return new SystemGetProductVersionResponse();
    }

    /**
     * Create an instance of {@link AutomationEngineRetrieveByToken }
     * 
     */
    public AutomationEngineRetrieveByToken createAutomationEngineRetrieveByToken() {
        return new AutomationEngineRetrieveByToken();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteManualTestRun }
     * 
     */
    public ArrayOfRemoteManualTestRun createArrayOfRemoteManualTestRun() {
        return new ArrayOfRemoteManualTestRun();
    }

    /**
     * Create an instance of {@link ReleaseRemoveTestMapping }
     * 
     */
    public ReleaseRemoveTestMapping createReleaseRemoveTestMapping() {
        return new ReleaseRemoveTestMapping();
    }

    /**
     * Create an instance of {@link AssociationUpdate }
     * 
     */
    public AssociationUpdate createAssociationUpdate() {
        return new AssociationUpdate();
    }

    /**
     * Create an instance of {@link IncidentCount }
     * 
     */
    public IncidentCount createIncidentCount() {
        return new IncidentCount();
    }

    /**
     * Create an instance of {@link TestRunRecordAutomated3 }
     * 
     */
    public TestRunRecordAutomated3 createTestRunRecordAutomated3() {
        return new TestRunRecordAutomated3();
    }

    /**
     * Create an instance of {@link TestRunCountResponse }
     * 
     */
    public TestRunCountResponse createTestRunCountResponse() {
        return new TestRunCountResponse();
    }

    /**
     * Create an instance of {@link BuildRetrieveById }
     * 
     */
    public BuildRetrieveById createBuildRetrieveById() {
        return new BuildRetrieveById();
    }

    /**
     * Create an instance of {@link AssociationCreate }
     * 
     */
    public AssociationCreate createAssociationCreate() {
        return new AssociationCreate();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteBuildSourceCode }
     * 
     */
    public ArrayOfRemoteBuildSourceCode createArrayOfRemoteBuildSourceCode() {
        return new ArrayOfRemoteBuildSourceCode();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveForOwnerResponse }
     * 
     */
    public TestCaseRetrieveForOwnerResponse createTestCaseRetrieveForOwnerResponse() {
        return new TestCaseRetrieveForOwnerResponse();
    }

    /**
     * Create an instance of {@link DocumentRetrieveFolders }
     * 
     */
    public DocumentRetrieveFolders createDocumentRetrieveFolders() {
        return new DocumentRetrieveFolders();
    }

    /**
     * Create an instance of {@link AutomationHostRetrieveById }
     * 
     */
    public AutomationHostRetrieveById createAutomationHostRetrieveById() {
        return new AutomationHostRetrieveById();
    }

    /**
     * Create an instance of {@link AutomationEngineRetrieve }
     * 
     */
    public AutomationEngineRetrieve createAutomationEngineRetrieve() {
        return new AutomationEngineRetrieve();
    }

    /**
     * Create an instance of {@link TestCaseUpdateResponse }
     * 
     */
    public TestCaseUpdateResponse createTestCaseUpdateResponse() {
        return new TestCaseUpdateResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieveWorkflowCustomProperties }
     * 
     */
    public IncidentRetrieveWorkflowCustomProperties createIncidentRetrieveWorkflowCustomProperties() {
        return new IncidentRetrieveWorkflowCustomProperties();
    }

    /**
     * Create an instance of {@link TaskUpdate }
     * 
     */
    public TaskUpdate createTaskUpdate() {
        return new TaskUpdate();
    }

    /**
     * Create an instance of {@link ReleaseMove }
     * 
     */
    public ReleaseMove createReleaseMove() {
        return new ReleaseMove();
    }

    /**
     * Create an instance of {@link DocumentAddToArtifactId }
     * 
     */
    public DocumentAddToArtifactId createDocumentAddToArtifactId() {
        return new DocumentAddToArtifactId();
    }

    /**
     * Create an instance of {@link ServiceFaultMessage }
     * 
     */
    public ServiceFaultMessage createServiceFaultMessage() {
        return new ServiceFaultMessage();
    }

    /**
     * Create an instance of {@link UserCreateResponse }
     * 
     */
    public UserCreateResponse createUserCreateResponse() {
        return new UserCreateResponse();
    }

    /**
     * Create an instance of {@link TestSetDelete }
     * 
     */
    public TestSetDelete createTestSetDelete() {
        return new TestSetDelete();
    }

    /**
     * Create an instance of {@link TestCaseCreateResponse }
     * 
     */
    public TestCaseCreateResponse createTestCaseCreateResponse() {
        return new TestCaseCreateResponse();
    }

    /**
     * Create an instance of {@link AutomationHostRetrieve }
     * 
     */
    public AutomationHostRetrieve createAutomationHostRetrieve() {
        return new AutomationHostRetrieve();
    }

    /**
     * Create an instance of {@link RequirementCreate1Response }
     * 
     */
    public RequirementCreate1Response createRequirementCreate1Response() {
        return new RequirementCreate1Response();
    }

    /**
     * Create an instance of {@link IncidentAddPriority }
     * 
     */
    public IncidentAddPriority createIncidentAddPriority() {
        return new IncidentAddPriority();
    }

    /**
     * Create an instance of {@link ProjectCreateResponse }
     * 
     */
    public ProjectCreateResponse createProjectCreateResponse() {
        return new ProjectCreateResponse();
    }

    /**
     * Create an instance of {@link RemoteBuildSourceCode }
     * 
     */
    public RemoteBuildSourceCode createRemoteBuildSourceCode() {
        return new RemoteBuildSourceCode();
    }

    /**
     * Create an instance of {@link TestSetCount }
     * 
     */
    public TestSetCount createTestSetCount() {
        return new TestSetCount();
    }

    /**
     * Create an instance of {@link RequirementMove }
     * 
     */
    public RequirementMove createRequirementMove() {
        return new RequirementMove();
    }

    /**
     * Create an instance of {@link ReleaseAddTestMapping2 }
     * 
     */
    public ReleaseAddTestMapping2 createReleaseAddTestMapping2() {
        return new ReleaseAddTestMapping2();
    }

    /**
     * Create an instance of {@link IncidentRetrievePriorities }
     * 
     */
    public IncidentRetrievePriorities createIncidentRetrievePriorities() {
        return new IncidentRetrievePriorities();
    }

    /**
     * Create an instance of {@link RemoteSort }
     * 
     */
    public RemoteSort createRemoteSort() {
        return new RemoteSort();
    }

    /**
     * Create an instance of {@link RemoteArtifactCustomProperty }
     * 
     */
    public RemoteArtifactCustomProperty createRemoteArtifactCustomProperty() {
        return new RemoteArtifactCustomProperty();
    }

    /**
     * Create an instance of {@link IncidentRetrieveWorkflowTransitions }
     * 
     */
    public IncidentRetrieveWorkflowTransitions createIncidentRetrieveWorkflowTransitions() {
        return new IncidentRetrieveWorkflowTransitions();
    }

    /**
     * Create an instance of {@link TaskRetrieveForOwner }
     * 
     */
    public TaskRetrieveForOwner createTaskRetrieveForOwner() {
        return new TaskRetrieveForOwner();
    }

    /**
     * Create an instance of {@link TestRunCreateFromTestSet }
     * 
     */
    public TestRunCreateFromTestSet createTestRunCreateFromTestSet() {
        return new TestRunCreateFromTestSet();
    }

    /**
     * Create an instance of {@link TestRunCreateForAutomatedTestSetResponse }
     * 
     */
    public TestRunCreateForAutomatedTestSetResponse createTestRunCreateForAutomatedTestSetResponse() {
        return new TestRunCreateForAutomatedTestSetResponse();
    }

    /**
     * Create an instance of {@link DocumentRetrieveTypesResponse }
     * 
     */
    public DocumentRetrieveTypesResponse createDocumentRetrieveTypesResponse() {
        return new DocumentRetrieveTypesResponse();
    }

    /**
     * Create an instance of {@link RequirementRetrieveResponse }
     * 
     */
    public RequirementRetrieveResponse createRequirementRetrieveResponse() {
        return new RequirementRetrieveResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieveForOwner }
     * 
     */
    public IncidentRetrieveForOwner createIncidentRetrieveForOwner() {
        return new IncidentRetrieveForOwner();
    }

    /**
     * Create an instance of {@link TestCaseCreateFolderResponse }
     * 
     */
    public TestCaseCreateFolderResponse createTestCaseCreateFolderResponse() {
        return new TestCaseCreateFolderResponse();
    }

    /**
     * Create an instance of {@link DocumentAddToArtifactIdResponse }
     * 
     */
    public DocumentAddToArtifactIdResponse createDocumentAddToArtifactIdResponse() {
        return new DocumentAddToArtifactIdResponse();
    }

    /**
     * Create an instance of {@link IncidentCreate }
     * 
     */
    public IncidentCreate createIncidentCreate() {
        return new IncidentCreate();
    }

    /**
     * Create an instance of {@link IncidentRetrieveByTestCase }
     * 
     */
    public IncidentRetrieveByTestCase createIncidentRetrieveByTestCase() {
        return new IncidentRetrieveByTestCase();
    }

    /**
     * Create an instance of {@link ReleaseCreate }
     * 
     */
    public ReleaseCreate createReleaseCreate() {
        return new ReleaseCreate();
    }

    /**
     * Create an instance of {@link TestSetAddTestMappingResponse }
     * 
     */
    public TestSetAddTestMappingResponse createTestSetAddTestMappingResponse() {
        return new TestSetAddTestMappingResponse();
    }

    /**
     * Create an instance of {@link ReleaseRetrieve }
     * 
     */
    public ReleaseRetrieve createReleaseRetrieve() {
        return new ReleaseRetrieve();
    }

    /**
     * Create an instance of {@link UserDeleteResponse }
     * 
     */
    public UserDeleteResponse createUserDeleteResponse() {
        return new UserDeleteResponse();
    }

    /**
     * Create an instance of {@link IncidentAddType }
     * 
     */
    public IncidentAddType createIncidentAddType() {
        return new IncidentAddType();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteAutomatedTestRun }
     * 
     */
    public ArrayOfRemoteAutomatedTestRun createArrayOfRemoteAutomatedTestRun() {
        return new ArrayOfRemoteAutomatedTestRun();
    }

    /**
     * Create an instance of {@link SystemGetProjectIdForArtifact }
     * 
     */
    public SystemGetProjectIdForArtifact createSystemGetProjectIdForArtifact() {
        return new SystemGetProjectIdForArtifact();
    }

    /**
     * Create an instance of {@link CustomPropertyRetrieveForArtifactType }
     * 
     */
    public CustomPropertyRetrieveForArtifactType createCustomPropertyRetrieveForArtifactType() {
        return new CustomPropertyRetrieveForArtifactType();
    }

    /**
     * Create an instance of {@link RequirementRetrieve }
     * 
     */
    public RequirementRetrieve createRequirementRetrieve() {
        return new RequirementRetrieve();
    }

    /**
     * Create an instance of {@link RequirementRetrieveComments }
     * 
     */
    public RequirementRetrieveComments createRequirementRetrieveComments() {
        return new RequirementRetrieveComments();
    }

    /**
     * Create an instance of {@link RemoteBuild }
     * 
     */
    public RemoteBuild createRemoteBuild() {
        return new RemoteBuild();
    }

    /**
     * Create an instance of {@link TestRunRetrieveByIdResponse }
     * 
     */
    public TestRunRetrieveByIdResponse createTestRunRetrieveByIdResponse() {
        return new TestRunRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link TestCaseCreateComment }
     * 
     */
    public TestCaseCreateComment createTestCaseCreateComment() {
        return new TestCaseCreateComment();
    }

    /**
     * Create an instance of {@link ArrayOfint }
     * 
     */
    public ArrayOfint createArrayOfint() {
        return new ArrayOfint();
    }

    /**
     * Create an instance of {@link TestSetCreateFolder }
     * 
     */
    public TestSetCreateFolder createTestSetCreateFolder() {
        return new TestSetCreateFolder();
    }

    /**
     * Create an instance of {@link TestRunRetrieveAutomatedResponse }
     * 
     */
    public TestRunRetrieveAutomatedResponse createTestRunRetrieveAutomatedResponse() {
        return new TestRunRetrieveAutomatedResponse();
    }

    /**
     * Create an instance of {@link ReleaseRetrieveResponse }
     * 
     */
    public ReleaseRetrieveResponse createReleaseRetrieveResponse() {
        return new ReleaseRetrieveResponse();
    }

    /**
     * Create an instance of {@link RemoteWorkflowIncidentCustomProperties }
     * 
     */
    public RemoteWorkflowIncidentCustomProperties createRemoteWorkflowIncidentCustomProperties() {
        return new RemoteWorkflowIncidentCustomProperties();
    }

    /**
     * Create an instance of {@link ConnectionDisconnect }
     * 
     */
    public ConnectionDisconnect createConnectionDisconnect() {
        return new ConnectionDisconnect();
    }

    /**
     * Create an instance of {@link RemoteCustomProperty }
     * 
     */
    public RemoteCustomProperty createRemoteCustomProperty() {
        return new RemoteCustomProperty();
    }

    /**
     * Create an instance of {@link TaskRetrieveCommentsResponse }
     * 
     */
    public TaskRetrieveCommentsResponse createTaskRetrieveCommentsResponse() {
        return new TaskRetrieveCommentsResponse();
    }

    /**
     * Create an instance of {@link DocumentDeleteFromArtifact }
     * 
     */
    public DocumentDeleteFromArtifact createDocumentDeleteFromArtifact() {
        return new DocumentDeleteFromArtifact();
    }

    /**
     * Create an instance of {@link ProjectRetrieveById }
     * 
     */
    public ProjectRetrieveById createProjectRetrieveById() {
        return new ProjectRetrieveById();
    }

    /**
     * Create an instance of {@link IncidentRetrieveTypesResponse }
     * 
     */
    public IncidentRetrieveTypesResponse createIncidentRetrieveTypesResponse() {
        return new IncidentRetrieveTypesResponse();
    }

    /**
     * Create an instance of {@link CustomPropertyUpdateCustomList }
     * 
     */
    public CustomPropertyUpdateCustomList createCustomPropertyUpdateCustomList() {
        return new CustomPropertyUpdateCustomList();
    }

    /**
     * Create an instance of {@link ConnectionAuthenticate2 }
     * 
     */
    public ConnectionAuthenticate2 createConnectionAuthenticate2() {
        return new ConnectionAuthenticate2();
    }

    /**
     * Create an instance of {@link ValidationFaultMessage }
     * 
     */
    public ValidationFaultMessage createValidationFaultMessage() {
        return new ValidationFaultMessage();
    }

    /**
     * Create an instance of {@link ConnectionConnectToProject }
     * 
     */
    public ConnectionConnectToProject createConnectionConnectToProject() {
        return new ConnectionConnectToProject();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveByIdResponse }
     * 
     */
    public TestCaseRetrieveByIdResponse createTestCaseRetrieveByIdResponse() {
        return new TestCaseRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link SystemGetProjectIdForArtifactResponse }
     * 
     */
    public SystemGetProjectIdForArtifactResponse createSystemGetProjectIdForArtifactResponse() {
        return new SystemGetProjectIdForArtifactResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteComment }
     * 
     */
    public ArrayOfRemoteComment createArrayOfRemoteComment() {
        return new ArrayOfRemoteComment();
    }

    /**
     * Create an instance of {@link TestCaseMove }
     * 
     */
    public TestCaseMove createTestCaseMove() {
        return new TestCaseMove();
    }

    /**
     * Create an instance of {@link IncidentRetrieveTypes }
     * 
     */
    public IncidentRetrieveTypes createIncidentRetrieveTypes() {
        return new IncidentRetrieveTypes();
    }

    /**
     * Create an instance of {@link SystemGetWebServerUrlResponse }
     * 
     */
    public SystemGetWebServerUrlResponse createSystemGetWebServerUrlResponse() {
        return new SystemGetWebServerUrlResponse();
    }

    /**
     * Create an instance of {@link IncidentDelete }
     * 
     */
    public IncidentDelete createIncidentDelete() {
        return new IncidentDelete();
    }

    /**
     * Create an instance of {@link DocumentDeleteResponse }
     * 
     */
    public DocumentDeleteResponse createDocumentDeleteResponse() {
        return new DocumentDeleteResponse();
    }

    /**
     * Create an instance of {@link IncidentAddCommentsResponse }
     * 
     */
    public IncidentAddCommentsResponse createIncidentAddCommentsResponse() {
        return new IncidentAddCommentsResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTestSetTestCaseMapping }
     * 
     */
    public ArrayOfRemoteTestSetTestCaseMapping createArrayOfRemoteTestSetTestCaseMapping() {
        return new ArrayOfRemoteTestSetTestCaseMapping();
    }

    /**
     * Create an instance of {@link TestCaseCreateParameterTokenResponse }
     * 
     */
    public TestCaseCreateParameterTokenResponse createTestCaseCreateParameterTokenResponse() {
        return new TestCaseCreateParameterTokenResponse();
    }

    /**
     * Create an instance of {@link DocumentRetrieveForArtifactResponse }
     * 
     */
    public DocumentRetrieveForArtifactResponse createDocumentRetrieveForArtifactResponse() {
        return new DocumentRetrieveForArtifactResponse();
    }

    /**
     * Create an instance of {@link RequirementRetrieveForOwnerResponse }
     * 
     */
    public RequirementRetrieveForOwnerResponse createRequirementRetrieveForOwnerResponse() {
        return new RequirementRetrieveForOwnerResponse();
    }

    /**
     * Create an instance of {@link RequirementCreate1 }
     * 
     */
    public RequirementCreate1 createRequirementCreate1() {
        return new RequirementCreate1();
    }

    /**
     * Create an instance of {@link IncidentUpdateResponse }
     * 
     */
    public IncidentUpdateResponse createIncidentUpdateResponse() {
        return new IncidentUpdateResponse();
    }

    /**
     * Create an instance of {@link TestRunRetrieveAutomated }
     * 
     */
    public TestRunRetrieveAutomated createTestRunRetrieveAutomated() {
        return new TestRunRetrieveAutomated();
    }

    /**
     * Create an instance of {@link ReleaseCreateComment }
     * 
     */
    public ReleaseCreateComment createReleaseCreateComment() {
        return new ReleaseCreateComment();
    }

    /**
     * Create an instance of {@link TaskCount }
     * 
     */
    public TaskCount createTaskCount() {
        return new TaskCount();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteSetting }
     * 
     */
    public ArrayOfRemoteSetting createArrayOfRemoteSetting() {
        return new ArrayOfRemoteSetting();
    }

    /**
     * Create an instance of {@link ReleaseAddTestMapping2Response }
     * 
     */
    public ReleaseAddTestMapping2Response createReleaseAddTestMapping2Response() {
        return new ReleaseAddTestMapping2Response();
    }

    /**
     * Create an instance of {@link TestCaseAddStepResponse }
     * 
     */
    public TestCaseAddStepResponse createTestCaseAddStepResponse() {
        return new TestCaseAddStepResponse();
    }

    /**
     * Create an instance of {@link RemoteAutomatedTestRun }
     * 
     */
    public RemoteAutomatedTestRun createRemoteAutomatedTestRun() {
        return new RemoteAutomatedTestRun();
    }

    /**
     * Create an instance of {@link CustomPropertyUpdateDefinitionResponse }
     * 
     */
    public CustomPropertyUpdateDefinitionResponse createCustomPropertyUpdateDefinitionResponse() {
        return new CustomPropertyUpdateDefinitionResponse();
    }

    /**
     * Create an instance of {@link TestRunCount }
     * 
     */
    public TestRunCount createTestRunCount() {
        return new TestRunCount();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteCustomPropertyOption }
     * 
     */
    public ArrayOfRemoteCustomPropertyOption createArrayOfRemoteCustomPropertyOption() {
        return new ArrayOfRemoteCustomPropertyOption();
    }

    /**
     * Create an instance of {@link AutomationHostRetrieveResponse }
     * 
     */
    public AutomationHostRetrieveResponse createAutomationHostRetrieveResponse() {
        return new AutomationHostRetrieveResponse();
    }

    /**
     * Create an instance of {@link IncidentCreateResponse }
     * 
     */
    public IncidentCreateResponse createIncidentCreateResponse() {
        return new IncidentCreateResponse();
    }

    /**
     * Create an instance of {@link UserRetrieveById }
     * 
     */
    public UserRetrieveById createUserRetrieveById() {
        return new UserRetrieveById();
    }

    /**
     * Create an instance of {@link RemoteTestSetTestCaseParameter }
     * 
     */
    public RemoteTestSetTestCaseParameter createRemoteTestSetTestCaseParameter() {
        return new RemoteTestSetTestCaseParameter();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveStepParametersResponse }
     * 
     */
    public TestCaseRetrieveStepParametersResponse createTestCaseRetrieveStepParametersResponse() {
        return new TestCaseRetrieveStepParametersResponse();
    }

    /**
     * Create an instance of {@link TaskRetrieveById }
     * 
     */
    public TaskRetrieveById createTaskRetrieveById() {
        return new TaskRetrieveById();
    }

    /**
     * Create an instance of {@link SystemGetArtifactUrlResponse }
     * 
     */
    public SystemGetArtifactUrlResponse createSystemGetArtifactUrlResponse() {
        return new SystemGetArtifactUrlResponse();
    }

    /**
     * Create an instance of {@link TestRunCreateForAutomationHostResponse }
     * 
     */
    public TestRunCreateForAutomationHostResponse createTestRunCreateForAutomationHostResponse() {
        return new TestRunCreateForAutomationHostResponse();
    }

    /**
     * Create an instance of {@link TaskCreateCommentResponse }
     * 
     */
    public TaskCreateCommentResponse createTaskCreateCommentResponse() {
        return new TaskCreateCommentResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTestStepParameter }
     * 
     */
    public ArrayOfRemoteTestStepParameter createArrayOfRemoteTestStepParameter() {
        return new ArrayOfRemoteTestStepParameter();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveByTestSetIdResponse }
     * 
     */
    public TestCaseRetrieveByTestSetIdResponse createTestCaseRetrieveByTestSetIdResponse() {
        return new TestCaseRetrieveByTestSetIdResponse();
    }

    /**
     * Create an instance of {@link ReleaseRetrieveTestMapping }
     * 
     */
    public ReleaseRetrieveTestMapping createReleaseRetrieveTestMapping() {
        return new ReleaseRetrieveTestMapping();
    }

    /**
     * Create an instance of {@link RequirementAddTestCoverageResponse }
     * 
     */
    public RequirementAddTestCoverageResponse createRequirementAddTestCoverageResponse() {
        return new RequirementAddTestCoverageResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteIncidentType }
     * 
     */
    public ArrayOfRemoteIncidentType createArrayOfRemoteIncidentType() {
        return new ArrayOfRemoteIncidentType();
    }

    /**
     * Create an instance of {@link TestCaseAddLink }
     * 
     */
    public TestCaseAddLink createTestCaseAddLink() {
        return new TestCaseAddLink();
    }

    /**
     * Create an instance of {@link RequirementCreateCommentResponse }
     * 
     */
    public RequirementCreateCommentResponse createRequirementCreateCommentResponse() {
        return new RequirementCreateCommentResponse();
    }

    /**
     * Create an instance of {@link AutomationHostCreateResponse }
     * 
     */
    public AutomationHostCreateResponse createAutomationHostCreateResponse() {
        return new AutomationHostCreateResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrievePrioritiesResponse }
     * 
     */
    public IncidentRetrievePrioritiesResponse createIncidentRetrievePrioritiesResponse() {
        return new IncidentRetrievePrioritiesResponse();
    }

    /**
     * Create an instance of {@link TestSetRetrieveResponse }
     * 
     */
    public TestSetRetrieveResponse createTestSetRetrieveResponse() {
        return new TestSetRetrieveResponse();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveByFolderResponse }
     * 
     */
    public TestCaseRetrieveByFolderResponse createTestCaseRetrieveByFolderResponse() {
        return new TestCaseRetrieveByFolderResponse();
    }

    /**
     * Create an instance of {@link RemoteCustomList }
     * 
     */
    public RemoteCustomList createRemoteCustomList() {
        return new RemoteCustomList();
    }

    /**
     * Create an instance of {@link ReleaseCreateCommentResponse }
     * 
     */
    public ReleaseCreateCommentResponse createReleaseCreateCommentResponse() {
        return new ReleaseCreateCommentResponse();
    }

    /**
     * Create an instance of {@link RequirementAddTestCoverage }
     * 
     */
    public RequirementAddTestCoverage createRequirementAddTestCoverage() {
        return new RequirementAddTestCoverage();
    }

    /**
     * Create an instance of {@link RemoteVersion }
     * 
     */
    public RemoteVersion createRemoteVersion() {
        return new RemoteVersion();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveStepParameters }
     * 
     */
    public TestCaseRetrieveStepParameters createTestCaseRetrieveStepParameters() {
        return new TestCaseRetrieveStepParameters();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveParameters }
     * 
     */
    public TestCaseRetrieveParameters createTestCaseRetrieveParameters() {
        return new TestCaseRetrieveParameters();
    }

    /**
     * Create an instance of {@link RemoteRelease }
     * 
     */
    public RemoteRelease createRemoteRelease() {
        return new RemoteRelease();
    }

    /**
     * Create an instance of {@link TestCaseCount }
     * 
     */
    public TestCaseCount createTestCaseCount() {
        return new TestCaseCount();
    }

    /**
     * Create an instance of {@link IncidentRetrieveSeverities }
     * 
     */
    public IncidentRetrieveSeverities createIncidentRetrieveSeverities() {
        return new IncidentRetrieveSeverities();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveProjectMappingsResponse }
     * 
     */
    public DataMappingRetrieveProjectMappingsResponse createDataMappingRetrieveProjectMappingsResponse() {
        return new DataMappingRetrieveProjectMappingsResponse();
    }

    /**
     * Create an instance of {@link DocumentRetrieveForFolder }
     * 
     */
    public DocumentRetrieveForFolder createDocumentRetrieveForFolder() {
        return new DocumentRetrieveForFolder();
    }

    /**
     * Create an instance of {@link RemoteDocumentVersion }
     * 
     */
    public RemoteDocumentVersion createRemoteDocumentVersion() {
        return new RemoteDocumentVersion();
    }

    /**
     * Create an instance of {@link TestRunRecordAutomated2Response }
     * 
     */
    public TestRunRecordAutomated2Response createTestRunRecordAutomated2Response() {
        return new TestRunRecordAutomated2Response();
    }

    /**
     * Create an instance of {@link RemoteIncidentStatus }
     * 
     */
    public RemoteIncidentStatus createRemoteIncidentStatus() {
        return new RemoteIncidentStatus();
    }

    /**
     * Create an instance of {@link DocumentDelete }
     * 
     */
    public DocumentDelete createDocumentDelete() {
        return new DocumentDelete();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteReleaseTestCaseMapping }
     * 
     */
    public ArrayOfRemoteReleaseTestCaseMapping createArrayOfRemoteReleaseTestCaseMapping() {
        return new ArrayOfRemoteReleaseTestCaseMapping();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveCustomPropertyMappingResponse }
     * 
     */
    public DataMappingRetrieveCustomPropertyMappingResponse createDataMappingRetrieveCustomPropertyMappingResponse() {
        return new DataMappingRetrieveCustomPropertyMappingResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTask }
     * 
     */
    public ArrayOfRemoteTask createArrayOfRemoteTask() {
        return new ArrayOfRemoteTask();
    }

    /**
     * Create an instance of {@link TestCaseCreateFolder }
     * 
     */
    public TestCaseCreateFolder createTestCaseCreateFolder() {
        return new TestCaseCreateFolder();
    }

    /**
     * Create an instance of {@link IncidentRetrieveByTestRunStep }
     * 
     */
    public IncidentRetrieveByTestRunStep createIncidentRetrieveByTestRunStep() {
        return new IncidentRetrieveByTestRunStep();
    }

    /**
     * Create an instance of {@link IncidentRetrieveSeveritiesResponse }
     * 
     */
    public IncidentRetrieveSeveritiesResponse createIncidentRetrieveSeveritiesResponse() {
        return new IncidentRetrieveSeveritiesResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteBuild }
     * 
     */
    public ArrayOfRemoteBuild createArrayOfRemoteBuild() {
        return new ArrayOfRemoteBuild();
    }

    /**
     * Create an instance of {@link TaskRetrieveResponse }
     * 
     */
    public TaskRetrieveResponse createTaskRetrieveResponse() {
        return new TaskRetrieveResponse();
    }

    /**
     * Create an instance of {@link ReleaseMoveResponse }
     * 
     */
    public ReleaseMoveResponse createReleaseMoveResponse() {
        return new ReleaseMoveResponse();
    }

    /**
     * Create an instance of {@link TestSetUpdate }
     * 
     */
    public TestSetUpdate createTestSetUpdate() {
        return new TestSetUpdate();
    }

    /**
     * Create an instance of {@link TestSetCreateCommentResponse }
     * 
     */
    public TestSetCreateCommentResponse createTestSetCreateCommentResponse() {
        return new TestSetCreateCommentResponse();
    }

    /**
     * Create an instance of {@link DocumentAddUrlResponse }
     * 
     */
    public DocumentAddUrlResponse createDocumentAddUrlResponse() {
        return new DocumentAddUrlResponse();
    }

    /**
     * Create an instance of {@link DocumentAddUrlVersionResponse }
     * 
     */
    public DocumentAddUrlVersionResponse createDocumentAddUrlVersionResponse() {
        return new DocumentAddUrlVersionResponse();
    }

    /**
     * Create an instance of {@link TaskRetrieveNewResponse }
     * 
     */
    public TaskRetrieveNewResponse createTaskRetrieveNewResponse() {
        return new TaskRetrieveNewResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteDataMapping }
     * 
     */
    public ArrayOfRemoteDataMapping createArrayOfRemoteDataMapping() {
        return new ArrayOfRemoteDataMapping();
    }

    /**
     * Create an instance of {@link AutomationHostUpdate }
     * 
     */
    public AutomationHostUpdate createAutomationHostUpdate() {
        return new AutomationHostUpdate();
    }

    /**
     * Create an instance of {@link DocumentAddUrl }
     * 
     */
    public DocumentAddUrl createDocumentAddUrl() {
        return new DocumentAddUrl();
    }

    /**
     * Create an instance of {@link TaskRetrieve }
     * 
     */
    public TaskRetrieve createTaskRetrieve() {
        return new TaskRetrieve();
    }

    /**
     * Create an instance of {@link TestCaseMoveStepResponse }
     * 
     */
    public TestCaseMoveStepResponse createTestCaseMoveStepResponse() {
        return new TestCaseMoveStepResponse();
    }

    /**
     * Create an instance of {@link AutomationEngineCreate }
     * 
     */
    public AutomationEngineCreate createAutomationEngineCreate() {
        return new AutomationEngineCreate();
    }

    /**
     * Create an instance of {@link ReleaseRetrieve2Response }
     * 
     */
    public ReleaseRetrieve2Response createReleaseRetrieve2Response() {
        return new ReleaseRetrieve2Response();
    }

    /**
     * Create an instance of {@link RequirementUpdateResponse }
     * 
     */
    public RequirementUpdateResponse createRequirementUpdateResponse() {
        return new RequirementUpdateResponse();
    }

    /**
     * Create an instance of {@link ReleaseRetrieve2 }
     * 
     */
    public ReleaseRetrieve2 createReleaseRetrieve2() {
        return new ReleaseRetrieve2();
    }

    /**
     * Create an instance of {@link AssociationRetrieveForArtifactResponse }
     * 
     */
    public AssociationRetrieveForArtifactResponse createAssociationRetrieveForArtifactResponse() {
        return new AssociationRetrieveForArtifactResponse();
    }

    /**
     * Create an instance of {@link ProjectDelete }
     * 
     */
    public ProjectDelete createProjectDelete() {
        return new ProjectDelete();
    }

    /**
     * Create an instance of {@link CustomPropertyAddCustomListValue }
     * 
     */
    public CustomPropertyAddCustomListValue createCustomPropertyAddCustomListValue() {
        return new CustomPropertyAddCustomListValue();
    }

    /**
     * Create an instance of {@link RemoteTestSetTestCaseMapping }
     * 
     */
    public RemoteTestSetTestCaseMapping createRemoteTestSetTestCaseMapping() {
        return new RemoteTestSetTestCaseMapping();
    }

    /**
     * Create an instance of {@link TestSetCountResponse }
     * 
     */
    public TestSetCountResponse createTestSetCountResponse() {
        return new TestSetCountResponse();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveUserMappingsResponse }
     * 
     */
    public DataMappingRetrieveUserMappingsResponse createDataMappingRetrieveUserMappingsResponse() {
        return new DataMappingRetrieveUserMappingsResponse();
    }

    /**
     * Create an instance of {@link DocumentRetrieveById }
     * 
     */
    public DocumentRetrieveById createDocumentRetrieveById() {
        return new DocumentRetrieveById();
    }

    /**
     * Create an instance of {@link TaskRetrieveByIdResponse }
     * 
     */
    public TaskRetrieveByIdResponse createTaskRetrieveByIdResponse() {
        return new TaskRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieveById }
     * 
     */
    public IncidentRetrieveById createIncidentRetrieveById() {
        return new IncidentRetrieveById();
    }

    /**
     * Create an instance of {@link RequirementRetrieveByIdResponse }
     * 
     */
    public RequirementRetrieveByIdResponse createRequirementRetrieveByIdResponse() {
        return new RequirementRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link RequirementRetrieveForOwner }
     * 
     */
    public RequirementRetrieveForOwner createRequirementRetrieveForOwner() {
        return new RequirementRetrieveForOwner();
    }

    /**
     * Create an instance of {@link IncidentCountResponse }
     * 
     */
    public IncidentCountResponse createIncidentCountResponse() {
        return new IncidentCountResponse();
    }

    /**
     * Create an instance of {@link RequirementRetrieveTestCoverage }
     * 
     */
    public RequirementRetrieveTestCoverage createRequirementRetrieveTestCoverage() {
        return new RequirementRetrieveTestCoverage();
    }

    /**
     * Create an instance of {@link UserDelete }
     * 
     */
    public UserDelete createUserDelete() {
        return new UserDelete();
    }

    /**
     * Create an instance of {@link TestRunRetrieveById }
     * 
     */
    public TestRunRetrieveById createTestRunRetrieveById() {
        return new TestRunRetrieveById();
    }

    /**
     * Create an instance of {@link TestCaseAddStep }
     * 
     */
    public TestCaseAddStep createTestCaseAddStep() {
        return new TestCaseAddStep();
    }

    /**
     * Create an instance of {@link AutomationEngineRetrieveResponse }
     * 
     */
    public AutomationEngineRetrieveResponse createAutomationEngineRetrieveResponse() {
        return new AutomationEngineRetrieveResponse();
    }

    /**
     * Create an instance of {@link RemoteIncidentPriority }
     * 
     */
    public RemoteIncidentPriority createRemoteIncidentPriority() {
        return new RemoteIncidentPriority();
    }

    /**
     * Create an instance of {@link IncidentRetrieveWorkflowFieldsResponse }
     * 
     */
    public IncidentRetrieveWorkflowFieldsResponse createIncidentRetrieveWorkflowFieldsResponse() {
        return new IncidentRetrieveWorkflowFieldsResponse();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveFieldValueMappings }
     * 
     */
    public DataMappingRetrieveFieldValueMappings createDataMappingRetrieveFieldValueMappings() {
        return new DataMappingRetrieveFieldValueMappings();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteCustomListValue }
     * 
     */
    public ArrayOfRemoteCustomListValue createArrayOfRemoteCustomListValue() {
        return new ArrayOfRemoteCustomListValue();
    }

    /**
     * Create an instance of {@link ProjectRetrieveResponse }
     * 
     */
    public ProjectRetrieveResponse createProjectRetrieveResponse() {
        return new ProjectRetrieveResponse();
    }

    /**
     * Create an instance of {@link CustomPropertyRetrieveCustomLists }
     * 
     */
    public CustomPropertyRetrieveCustomLists createCustomPropertyRetrieveCustomLists() {
        return new CustomPropertyRetrieveCustomLists();
    }

    /**
     * Create an instance of {@link RemoteRequirementTestCaseMapping }
     * 
     */
    public RemoteRequirementTestCaseMapping createRemoteRequirementTestCaseMapping() {
        return new RemoteRequirementTestCaseMapping();
    }

    /**
     * Create an instance of {@link TestSetUpdateResponse }
     * 
     */
    public TestSetUpdateResponse createTestSetUpdateResponse() {
        return new TestSetUpdateResponse();
    }

    /**
     * Create an instance of {@link SystemGetSettingsResponse }
     * 
     */
    public SystemGetSettingsResponse createSystemGetSettingsResponse() {
        return new SystemGetSettingsResponse();
    }

    /**
     * Create an instance of {@link AutomationHostUpdateResponse }
     * 
     */
    public AutomationHostUpdateResponse createAutomationHostUpdateResponse() {
        return new AutomationHostUpdateResponse();
    }

    /**
     * Create an instance of {@link CustomPropertyRetrieveForArtifactTypeResponse }
     * 
     */
    public CustomPropertyRetrieveForArtifactTypeResponse createCustomPropertyRetrieveForArtifactTypeResponse() {
        return new CustomPropertyRetrieveForArtifactTypeResponse();
    }

    /**
     * Create an instance of {@link TestCaseAddUpdateAutomationScript }
     * 
     */
    public TestCaseAddUpdateAutomationScript createTestCaseAddUpdateAutomationScript() {
        return new TestCaseAddUpdateAutomationScript();
    }

    /**
     * Create an instance of {@link ReleaseRetrieveCommentsResponse }
     * 
     */
    public ReleaseRetrieveCommentsResponse createReleaseRetrieveCommentsResponse() {
        return new ReleaseRetrieveCommentsResponse();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveParametersResponse }
     * 
     */
    public TestCaseRetrieveParametersResponse createTestCaseRetrieveParametersResponse() {
        return new TestCaseRetrieveParametersResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteWorkflowIncidentCustomProperties }
     * 
     */
    public ArrayOfRemoteWorkflowIncidentCustomProperties createArrayOfRemoteWorkflowIncidentCustomProperties() {
        return new ArrayOfRemoteWorkflowIncidentCustomProperties();
    }

    /**
     * Create an instance of {@link RemoteAssociation }
     * 
     */
    public RemoteAssociation createRemoteAssociation() {
        return new RemoteAssociation();
    }

    /**
     * Create an instance of {@link RequirementRetrieveById }
     * 
     */
    public RequirementRetrieveById createRequirementRetrieveById() {
        return new RequirementRetrieveById();
    }

    /**
     * Create an instance of {@link TestCaseMoveStep }
     * 
     */
    public TestCaseMoveStep createTestCaseMoveStep() {
        return new TestCaseMoveStep();
    }

    /**
     * Create an instance of {@link RemoteArtifact }
     * 
     */
    public RemoteArtifact createRemoteArtifact() {
        return new RemoteArtifact();
    }

    /**
     * Create an instance of {@link TestCaseDeleteStep }
     * 
     */
    public TestCaseDeleteStep createTestCaseDeleteStep() {
        return new TestCaseDeleteStep();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveCustomPropertyMapping }
     * 
     */
    public DataMappingRetrieveCustomPropertyMapping createDataMappingRetrieveCustomPropertyMapping() {
        return new DataMappingRetrieveCustomPropertyMapping();
    }

    /**
     * Create an instance of {@link CustomPropertyUpdateDefinition }
     * 
     */
    public CustomPropertyUpdateDefinition createCustomPropertyUpdateDefinition() {
        return new CustomPropertyUpdateDefinition();
    }

    /**
     * Create an instance of {@link IncidentAddSeverityResponse }
     * 
     */
    public IncidentAddSeverityResponse createIncidentAddSeverityResponse() {
        return new IncidentAddSeverityResponse();
    }

    /**
     * Create an instance of {@link RequirementCountResponse }
     * 
     */
    public RequirementCountResponse createRequirementCountResponse() {
        return new RequirementCountResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteWorkflowIncidentTransition }
     * 
     */
    public ArrayOfRemoteWorkflowIncidentTransition createArrayOfRemoteWorkflowIncidentTransition() {
        return new ArrayOfRemoteWorkflowIncidentTransition();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteRelease }
     * 
     */
    public ArrayOfRemoteRelease createArrayOfRemoteRelease() {
        return new ArrayOfRemoteRelease();
    }

    /**
     * Create an instance of {@link ReleaseRemoveTestMappingResponse }
     * 
     */
    public ReleaseRemoveTestMappingResponse createReleaseRemoveTestMappingResponse() {
        return new ReleaseRemoveTestMappingResponse();
    }

    /**
     * Create an instance of {@link TestRunCreateForAutomationHost }
     * 
     */
    public TestRunCreateForAutomationHost createTestRunCreateForAutomationHost() {
        return new TestRunCreateForAutomationHost();
    }

    /**
     * Create an instance of {@link TestSetCreate }
     * 
     */
    public TestSetCreate createTestSetCreate() {
        return new TestSetCreate();
    }

    /**
     * Create an instance of {@link UserCreate }
     * 
     */
    public UserCreate createUserCreate() {
        return new UserCreate();
    }

    /**
     * Create an instance of {@link RemoteProjectRole }
     * 
     */
    public RemoteProjectRole createRemoteProjectRole() {
        return new RemoteProjectRole();
    }

    /**
     * Create an instance of {@link TestSetMove }
     * 
     */
    public TestSetMove createTestSetMove() {
        return new TestSetMove();
    }

    /**
     * Create an instance of {@link TestRunCreateFromTestCasesResponse }
     * 
     */
    public TestRunCreateFromTestCasesResponse createTestRunCreateFromTestCasesResponse() {
        return new TestRunCreateFromTestCasesResponse();
    }

    /**
     * Create an instance of {@link TestRunRecordAutomated1Response }
     * 
     */
    public TestRunRecordAutomated1Response createTestRunRecordAutomated1Response() {
        return new TestRunRecordAutomated1Response();
    }

    /**
     * Create an instance of {@link ReleaseUpdateResponse }
     * 
     */
    public ReleaseUpdateResponse createReleaseUpdateResponse() {
        return new ReleaseUpdateResponse();
    }

    /**
     * Create an instance of {@link TestCaseAddParameterResponse }
     * 
     */
    public TestCaseAddParameterResponse createTestCaseAddParameterResponse() {
        return new TestCaseAddParameterResponse();
    }

    /**
     * Create an instance of {@link ProjectRetrieveUserMembershipResponse }
     * 
     */
    public ProjectRetrieveUserMembershipResponse createProjectRetrieveUserMembershipResponse() {
        return new ProjectRetrieveUserMembershipResponse();
    }

    /**
     * Create an instance of {@link TestCaseCountForFolder }
     * 
     */
    public TestCaseCountForFolder createTestCaseCountForFolder() {
        return new TestCaseCountForFolder();
    }

    /**
     * Create an instance of {@link AutomationHostDeleteResponse }
     * 
     */
    public AutomationHostDeleteResponse createAutomationHostDeleteResponse() {
        return new AutomationHostDeleteResponse();
    }

    /**
     * Create an instance of {@link ConnectionAuthenticateResponse }
     * 
     */
    public ConnectionAuthenticateResponse createConnectionAuthenticateResponse() {
        return new ConnectionAuthenticateResponse();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveByReleaseId }
     * 
     */
    public TestCaseRetrieveByReleaseId createTestCaseRetrieveByReleaseId() {
        return new TestCaseRetrieveByReleaseId();
    }

    /**
     * Create an instance of {@link DataMappingAddArtifactMappingsResponse }
     * 
     */
    public DataMappingAddArtifactMappingsResponse createDataMappingAddArtifactMappingsResponse() {
        return new DataMappingAddArtifactMappingsResponse();
    }

    /**
     * Create an instance of {@link DateRange }
     * 
     */
    public DateRange createDateRange() {
        return new DateRange();
    }

    /**
     * Create an instance of {@link SystemGetSettings }
     * 
     */
    public SystemGetSettings createSystemGetSettings() {
        return new SystemGetSettings();
    }

    /**
     * Create an instance of {@link DocumentRetrieveFolderById }
     * 
     */
    public DocumentRetrieveFolderById createDocumentRetrieveFolderById() {
        return new DocumentRetrieveFolderById();
    }

    /**
     * Create an instance of {@link ReleaseCountResponse }
     * 
     */
    public ReleaseCountResponse createReleaseCountResponse() {
        return new ReleaseCountResponse();
    }

    /**
     * Create an instance of {@link ReleaseCount }
     * 
     */
    public ReleaseCount createReleaseCount() {
        return new ReleaseCount();
    }

    /**
     * Create an instance of {@link DocumentAddUrlVersion }
     * 
     */
    public DocumentAddUrlVersion createDocumentAddUrlVersion() {
        return new DocumentAddUrlVersion();
    }

    /**
     * Create an instance of {@link RemoteManualTestRun }
     * 
     */
    public RemoteManualTestRun createRemoteManualTestRun() {
        return new RemoteManualTestRun();
    }

    /**
     * Create an instance of {@link SystemGetArtifactUrl }
     * 
     */
    public SystemGetArtifactUrl createSystemGetArtifactUrl() {
        return new SystemGetArtifactUrl();
    }

    /**
     * Create an instance of {@link IncidentRetrieveNew }
     * 
     */
    public IncidentRetrieveNew createIncidentRetrieveNew() {
        return new IncidentRetrieveNew();
    }

    /**
     * Create an instance of {@link CustomPropertyRetrieveCustomListsResponse }
     * 
     */
    public CustomPropertyRetrieveCustomListsResponse createCustomPropertyRetrieveCustomListsResponse() {
        return new CustomPropertyRetrieveCustomListsResponse();
    }

    /**
     * Create an instance of {@link TestSetRetrieveById }
     * 
     */
    public TestSetRetrieveById createTestSetRetrieveById() {
        return new TestSetRetrieveById();
    }

    /**
     * Create an instance of {@link TestRunRetrieveManual }
     * 
     */
    public TestRunRetrieveManual createTestRunRetrieveManual() {
        return new TestRunRetrieveManual();
    }

    /**
     * Create an instance of {@link TestRunRetrieveAutomatedByIdResponse }
     * 
     */
    public TestRunRetrieveAutomatedByIdResponse createTestRunRetrieveAutomatedByIdResponse() {
        return new TestRunRetrieveAutomatedByIdResponse();
    }

    /**
     * Create an instance of {@link TestSetCreateFolderResponse }
     * 
     */
    public TestSetCreateFolderResponse createTestSetCreateFolderResponse() {
        return new TestSetCreateFolderResponse();
    }

    /**
     * Create an instance of {@link RemoteSetting }
     * 
     */
    public RemoteSetting createRemoteSetting() {
        return new RemoteSetting();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteArtifactCustomProperty }
     * 
     */
    public ArrayOfRemoteArtifactCustomProperty createArrayOfRemoteArtifactCustomProperty() {
        return new ArrayOfRemoteArtifactCustomProperty();
    }

    /**
     * Create an instance of {@link TestSetDeleteResponse }
     * 
     */
    public TestSetDeleteResponse createTestSetDeleteResponse() {
        return new TestSetDeleteResponse();
    }

    /**
     * Create an instance of {@link RemoteTestCase }
     * 
     */
    public RemoteTestCase createRemoteTestCase() {
        return new RemoteTestCase();
    }

    /**
     * Create an instance of {@link AssociationCreateResponse }
     * 
     */
    public AssociationCreateResponse createAssociationCreateResponse() {
        return new AssociationCreateResponse();
    }

    /**
     * Create an instance of {@link SystemGetServerDateTimeResponse }
     * 
     */
    public SystemGetServerDateTimeResponse createSystemGetServerDateTimeResponse() {
        return new SystemGetServerDateTimeResponse();
    }

    /**
     * Create an instance of {@link SystemGetProductName }
     * 
     */
    public SystemGetProductName createSystemGetProductName() {
        return new SystemGetProductName();
    }

    /**
     * Create an instance of {@link BuildRetrieveByReleaseIdResponse }
     * 
     */
    public BuildRetrieveByReleaseIdResponse createBuildRetrieveByReleaseIdResponse() {
        return new BuildRetrieveByReleaseIdResponse();
    }

    /**
     * Create an instance of {@link TestCaseAddLinkResponse }
     * 
     */
    public TestCaseAddLinkResponse createTestCaseAddLinkResponse() {
        return new TestCaseAddLinkResponse();
    }

    /**
     * Create an instance of {@link TestCaseAddParameter }
     * 
     */
    public TestCaseAddParameter createTestCaseAddParameter() {
        return new TestCaseAddParameter();
    }

    /**
     * Create an instance of {@link TestCaseUpdate }
     * 
     */
    public TestCaseUpdate createTestCaseUpdate() {
        return new TestCaseUpdate();
    }

    /**
     * Create an instance of {@link ReleaseRetrieveComments }
     * 
     */
    public ReleaseRetrieveComments createReleaseRetrieveComments() {
        return new ReleaseRetrieveComments();
    }

    /**
     * Create an instance of {@link RemoteWorkflowIncidentFields }
     * 
     */
    public RemoteWorkflowIncidentFields createRemoteWorkflowIncidentFields() {
        return new RemoteWorkflowIncidentFields();
    }

    /**
     * Create an instance of {@link TestSetMoveResponse }
     * 
     */
    public TestSetMoveResponse createTestSetMoveResponse() {
        return new TestSetMoveResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieve }
     * 
     */
    public IncidentRetrieve createIncidentRetrieve() {
        return new IncidentRetrieve();
    }

    /**
     * Create an instance of {@link RemoteRequirement }
     * 
     */
    public RemoteRequirement createRemoteRequirement() {
        return new RemoteRequirement();
    }

    /**
     * Create an instance of {@link TestRunCreateFromTestSetResponse }
     * 
     */
    public TestRunCreateFromTestSetResponse createTestRunCreateFromTestSetResponse() {
        return new TestRunCreateFromTestSetResponse();
    }

    /**
     * Create an instance of {@link TestSetRetrieveForOwnerResponse }
     * 
     */
    public TestSetRetrieveForOwnerResponse createTestSetRetrieveForOwnerResponse() {
        return new TestSetRetrieveForOwnerResponse();
    }

    /**
     * Create an instance of {@link DocumentUpdateFolderResponse }
     * 
     */
    public DocumentUpdateFolderResponse createDocumentUpdateFolderResponse() {
        return new DocumentUpdateFolderResponse();
    }

    /**
     * Create an instance of {@link TestSetRemoveTestMappingResponse }
     * 
     */
    public TestSetRemoveTestMappingResponse createTestSetRemoveTestMappingResponse() {
        return new TestSetRemoveTestMappingResponse();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveByReleaseIdResponse }
     * 
     */
    public TestCaseRetrieveByReleaseIdResponse createTestCaseRetrieveByReleaseIdResponse() {
        return new TestCaseRetrieveByReleaseIdResponse();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveArtifactMappings }
     * 
     */
    public DataMappingRetrieveArtifactMappings createDataMappingRetrieveArtifactMappings() {
        return new DataMappingRetrieveArtifactMappings();
    }

    /**
     * Create an instance of {@link ReleaseDeleteResponse }
     * 
     */
    public ReleaseDeleteResponse createReleaseDeleteResponse() {
        return new ReleaseDeleteResponse();
    }

    /**
     * Create an instance of {@link RemoteDocumentType }
     * 
     */
    public RemoteDocumentType createRemoteDocumentType() {
        return new RemoteDocumentType();
    }

    /**
     * Create an instance of {@link BuildRetrieveByReleaseId }
     * 
     */
    public BuildRetrieveByReleaseId createBuildRetrieveByReleaseId() {
        return new BuildRetrieveByReleaseId();
    }

    /**
     * Create an instance of {@link RemoteComment }
     * 
     */
    public RemoteComment createRemoteComment() {
        return new RemoteComment();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveCustomPropertyValueMappings }
     * 
     */
    public DataMappingRetrieveCustomPropertyValueMappings createDataMappingRetrieveCustomPropertyValueMappings() {
        return new DataMappingRetrieveCustomPropertyValueMappings();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveById }
     * 
     */
    public TestCaseRetrieveById createTestCaseRetrieveById() {
        return new TestCaseRetrieveById();
    }

    /**
     * Create an instance of {@link DataMappingRemoveArtifactMappingsResponse }
     * 
     */
    public DataMappingRemoveArtifactMappingsResponse createDataMappingRemoveArtifactMappingsResponse() {
        return new DataMappingRemoveArtifactMappingsResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteAutomationHost }
     * 
     */
    public ArrayOfRemoteAutomationHost createArrayOfRemoteAutomationHost() {
        return new ArrayOfRemoteAutomationHost();
    }

    /**
     * Create an instance of {@link TestSetRetrieveTestCaseMappingResponse }
     * 
     */
    public TestSetRetrieveTestCaseMappingResponse createTestSetRetrieveTestCaseMappingResponse() {
        return new TestSetRetrieveTestCaseMappingResponse();
    }

    /**
     * Create an instance of {@link TestSetRetrieveTestCaseMapping }
     * 
     */
    public TestSetRetrieveTestCaseMapping createTestSetRetrieveTestCaseMapping() {
        return new TestSetRetrieveTestCaseMapping();
    }

    /**
     * Create an instance of {@link IncidentRetrieveWorkflowFields }
     * 
     */
    public IncidentRetrieveWorkflowFields createIncidentRetrieveWorkflowFields() {
        return new IncidentRetrieveWorkflowFields();
    }

    /**
     * Create an instance of {@link IncidentRetrieveByTestCaseResponse }
     * 
     */
    public IncidentRetrieveByTestCaseResponse createIncidentRetrieveByTestCaseResponse() {
        return new IncidentRetrieveByTestCaseResponse();
    }

    /**
     * Create an instance of {@link ReleaseDelete }
     * 
     */
    public ReleaseDelete createReleaseDelete() {
        return new ReleaseDelete();
    }

    /**
     * Create an instance of {@link UserRetrieveByIdResponse }
     * 
     */
    public UserRetrieveByIdResponse createUserRetrieveByIdResponse() {
        return new UserRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteIncidentSeverity }
     * 
     */
    public ArrayOfRemoteIncidentSeverity createArrayOfRemoteIncidentSeverity() {
        return new ArrayOfRemoteIncidentSeverity();
    }

    /**
     * Create an instance of {@link DocumentAddFile }
     * 
     */
    public DocumentAddFile createDocumentAddFile() {
        return new DocumentAddFile();
    }

    /**
     * Create an instance of {@link TestSetRemoveTestMapping }
     * 
     */
    public TestSetRemoveTestMapping createTestSetRemoveTestMapping() {
        return new TestSetRemoveTestMapping();
    }

    /**
     * Create an instance of {@link AutomationEngineRetrieveById }
     * 
     */
    public AutomationEngineRetrieveById createAutomationEngineRetrieveById() {
        return new AutomationEngineRetrieveById();
    }

    /**
     * Create an instance of {@link DocumentOpenFile }
     * 
     */
    public DocumentOpenFile createDocumentOpenFile() {
        return new DocumentOpenFile();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveArtifactMappingsResponse }
     * 
     */
    public DataMappingRetrieveArtifactMappingsResponse createDataMappingRetrieveArtifactMappingsResponse() {
        return new DataMappingRetrieveArtifactMappingsResponse();
    }

    /**
     * Create an instance of {@link TestSetRetrieveByIdResponse }
     * 
     */
    public TestSetRetrieveByIdResponse createTestSetRetrieveByIdResponse() {
        return new TestSetRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link RequirementCount }
     * 
     */
    public RequirementCount createRequirementCount() {
        return new RequirementCount();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveUserMappings }
     * 
     */
    public DataMappingRetrieveUserMappings createDataMappingRetrieveUserMappings() {
        return new DataMappingRetrieveUserMappings();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveByFolder }
     * 
     */
    public TestCaseRetrieveByFolder createTestCaseRetrieveByFolder() {
        return new TestCaseRetrieveByFolder();
    }

    /**
     * Create an instance of {@link DataMappingRetrieveCustomPropertyValueMappingsResponse }
     * 
     */
    public DataMappingRetrieveCustomPropertyValueMappingsResponse createDataMappingRetrieveCustomPropertyValueMappingsResponse() {
        return new DataMappingRetrieveCustomPropertyValueMappingsResponse();
    }

    /**
     * Create an instance of {@link TestCaseCreateCommentResponse }
     * 
     */
    public TestCaseCreateCommentResponse createTestCaseCreateCommentResponse() {
        return new TestCaseCreateCommentResponse();
    }

    /**
     * Create an instance of {@link RemoteReleaseTestCaseMapping }
     * 
     */
    public RemoteReleaseTestCaseMapping createRemoteReleaseTestCaseMapping() {
        return new RemoteReleaseTestCaseMapping();
    }

    /**
     * Create an instance of {@link TestSetCreateResponse }
     * 
     */
    public TestSetCreateResponse createTestSetCreateResponse() {
        return new TestSetCreateResponse();
    }

    /**
     * Create an instance of {@link RemoteAutomationEngine }
     * 
     */
    public RemoteAutomationEngine createRemoteAutomationEngine() {
        return new RemoteAutomationEngine();
    }

    /**
     * Create an instance of {@link IncidentRetrieveForOwnerResponse }
     * 
     */
    public IncidentRetrieveForOwnerResponse createIncidentRetrieveForOwnerResponse() {
        return new IncidentRetrieveForOwnerResponse();
    }

    /**
     * Create an instance of {@link DocumentAddFileVersionResponse }
     * 
     */
    public DocumentAddFileVersionResponse createDocumentAddFileVersionResponse() {
        return new DocumentAddFileVersionResponse();
    }

    /**
     * Create an instance of {@link CustomPropertyDeleteDefinition }
     * 
     */
    public CustomPropertyDeleteDefinition createCustomPropertyDeleteDefinition() {
        return new CustomPropertyDeleteDefinition();
    }

    /**
     * Create an instance of {@link IncidentDeleteResponse }
     * 
     */
    public IncidentDeleteResponse createIncidentDeleteResponse() {
        return new IncidentDeleteResponse();
    }

    /**
     * Create an instance of {@link UserRetrieveByUserName }
     * 
     */
    public UserRetrieveByUserName createUserRetrieveByUserName() {
        return new UserRetrieveByUserName();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteProject }
     * 
     */
    public ArrayOfRemoteProject createArrayOfRemoteProject() {
        return new ArrayOfRemoteProject();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteProjectUser }
     * 
     */
    public ArrayOfRemoteProjectUser createArrayOfRemoteProjectUser() {
        return new ArrayOfRemoteProjectUser();
    }

    /**
     * Create an instance of {@link ProjectRetrieve }
     * 
     */
    public ProjectRetrieve createProjectRetrieve() {
        return new ProjectRetrieve();
    }

    /**
     * Create an instance of {@link IncidentRetrieveWorkflowCustomPropertiesResponse }
     * 
     */
    public IncidentRetrieveWorkflowCustomPropertiesResponse createIncidentRetrieveWorkflowCustomPropertiesResponse() {
        return new IncidentRetrieveWorkflowCustomPropertiesResponse();
    }

    /**
     * Create an instance of {@link SystemGetProductNameResponse }
     * 
     */
    public SystemGetProductNameResponse createSystemGetProductNameResponse() {
        return new SystemGetProductNameResponse();
    }

    /**
     * Create an instance of {@link ConnectionDisconnectResponse }
     * 
     */
    public ConnectionDisconnectResponse createConnectionDisconnectResponse() {
        return new ConnectionDisconnectResponse();
    }

    /**
     * Create an instance of {@link AutomationHostDelete }
     * 
     */
    public AutomationHostDelete createAutomationHostDelete() {
        return new AutomationHostDelete();
    }

    /**
     * Create an instance of {@link TaskRetrieveNew }
     * 
     */
    public TaskRetrieveNew createTaskRetrieveNew() {
        return new TaskRetrieveNew();
    }

    /**
     * Create an instance of {@link IncidentRetrieveStatusesResponse }
     * 
     */
    public IncidentRetrieveStatusesResponse createIncidentRetrieveStatusesResponse() {
        return new IncidentRetrieveStatusesResponse();
    }

    /**
     * Create an instance of {@link ProjectRetrieveByIdResponse }
     * 
     */
    public ProjectRetrieveByIdResponse createProjectRetrieveByIdResponse() {
        return new ProjectRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteWorkflowIncidentFields }
     * 
     */
    public ArrayOfRemoteWorkflowIncidentFields createArrayOfRemoteWorkflowIncidentFields() {
        return new ArrayOfRemoteWorkflowIncidentFields();
    }

    /**
     * Create an instance of {@link TestCaseDelete }
     * 
     */
    public TestCaseDelete createTestCaseDelete() {
        return new TestCaseDelete();
    }

    /**
     * Create an instance of {@link SystemGetWebServerUrl }
     * 
     */
    public SystemGetWebServerUrl createSystemGetWebServerUrl() {
        return new SystemGetWebServerUrl();
    }

    /**
     * Create an instance of {@link AutomationEngineRetrieveByTokenResponse }
     * 
     */
    public AutomationEngineRetrieveByTokenResponse createAutomationEngineRetrieveByTokenResponse() {
        return new AutomationEngineRetrieveByTokenResponse();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveCommentsResponse }
     * 
     */
    public TestCaseRetrieveCommentsResponse createTestCaseRetrieveCommentsResponse() {
        return new TestCaseRetrieveCommentsResponse();
    }

    /**
     * Create an instance of {@link TestCaseCountResponse }
     * 
     */
    public TestCaseCountResponse createTestCaseCountResponse() {
        return new TestCaseCountResponse();
    }

    /**
     * Create an instance of {@link TaskCountResponse }
     * 
     */
    public TaskCountResponse createTaskCountResponse() {
        return new TaskCountResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieveComments }
     * 
     */
    public IncidentRetrieveComments createIncidentRetrieveComments() {
        return new IncidentRetrieveComments();
    }

    /**
     * Create an instance of {@link RemoteDocument }
     * 
     */
    public RemoteDocument createRemoteDocument() {
        return new RemoteDocument();
    }

    /**
     * Create an instance of {@link TestRunRetrieveResponse }
     * 
     */
    public TestRunRetrieveResponse createTestRunRetrieveResponse() {
        return new TestRunRetrieveResponse();
    }

    /**
     * Create an instance of {@link CustomPropertyAddDefinition }
     * 
     */
    public CustomPropertyAddDefinition createCustomPropertyAddDefinition() {
        return new CustomPropertyAddDefinition();
    }

    /**
     * Create an instance of {@link DataMappingAddUserMappingsResponse }
     * 
     */
    public DataMappingAddUserMappingsResponse createDataMappingAddUserMappingsResponse() {
        return new DataMappingAddUserMappingsResponse();
    }

    /**
     * Create an instance of {@link RemoteIncidentSeverity }
     * 
     */
    public RemoteIncidentSeverity createRemoteIncidentSeverity() {
        return new RemoteIncidentSeverity();
    }

    /**
     * Create an instance of {@link IncidentAddPriorityResponse }
     * 
     */
    public IncidentAddPriorityResponse createIncidentAddPriorityResponse() {
        return new IncidentAddPriorityResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteIncident }
     * 
     */
    public ArrayOfRemoteIncident createArrayOfRemoteIncident() {
        return new ArrayOfRemoteIncident();
    }

    /**
     * Create an instance of {@link IncidentRetrieveByTestRunStepResponse }
     * 
     */
    public IncidentRetrieveByTestRunStepResponse createIncidentRetrieveByTestRunStepResponse() {
        return new IncidentRetrieveByTestRunStepResponse();
    }

    /**
     * Create an instance of {@link ProjectRetrieveUserMembership }
     * 
     */
    public ProjectRetrieveUserMembership createProjectRetrieveUserMembership() {
        return new ProjectRetrieveUserMembership();
    }

    /**
     * Create an instance of {@link IncidentAddStatusResponse }
     * 
     */
    public IncidentAddStatusResponse createIncidentAddStatusResponse() {
        return new IncidentAddStatusResponse();
    }

    /**
     * Create an instance of {@link TaskCreateComment }
     * 
     */
    public TaskCreateComment createTaskCreateComment() {
        return new TaskCreateComment();
    }

    /**
     * Create an instance of {@link TestCaseRetrieveResponse }
     * 
     */
    public TestCaseRetrieveResponse createTestCaseRetrieveResponse() {
        return new TestCaseRetrieveResponse();
    }

    /**
     * Create an instance of {@link TestRunRecordAutomated1 }
     * 
     */
    public TestRunRecordAutomated1 createTestRunRecordAutomated1() {
        return new TestRunRecordAutomated1();
    }

    /**
     * Create an instance of {@link AutomationHostCreate }
     * 
     */
    public AutomationHostCreate createAutomationHostCreate() {
        return new AutomationHostCreate();
    }

    /**
     * Create an instance of {@link DocumentRetrieveByIdResponse }
     * 
     */
    public DocumentRetrieveByIdResponse createDocumentRetrieveByIdResponse() {
        return new DocumentRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link DataMappingAddArtifactMappings }
     * 
     */
    public DataMappingAddArtifactMappings createDataMappingAddArtifactMappings() {
        return new DataMappingAddArtifactMappings();
    }

    /**
     * Create an instance of {@link DocumentOpenFileResponse }
     * 
     */
    public DocumentOpenFileResponse createDocumentOpenFileResponse() {
        return new DocumentOpenFileResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteCustomProperty }
     * 
     */
    public ArrayOfRemoteCustomProperty createArrayOfRemoteCustomProperty() {
        return new ArrayOfRemoteCustomProperty();
    }

    /**
     * Create an instance of {@link TestRunRetrieveManualResponse }
     * 
     */
    public TestRunRetrieveManualResponse createTestRunRetrieveManualResponse() {
        return new TestRunRetrieveManualResponse();
    }

    /**
     * Create an instance of {@link ProjectRoleRetrieveResponse }
     * 
     */
    public ProjectRoleRetrieveResponse createProjectRoleRetrieveResponse() {
        return new ProjectRoleRetrieveResponse();
    }

    /**
     * Create an instance of {@link IncidentRetrieveResponse }
     * 
     */
    public IncidentRetrieveResponse createIncidentRetrieveResponse() {
        return new IncidentRetrieveResponse();
    }

    /**
     * Create an instance of {@link DataMappingRemoveArtifactMappings }
     * 
     */
    public DataMappingRemoveArtifactMappings createDataMappingRemoveArtifactMappings() {
        return new DataMappingRemoveArtifactMappings();
    }

    /**
     * Create an instance of {@link TestCaseAddUpdateAutomationScriptResponse }
     * 
     */
    public TestCaseAddUpdateAutomationScriptResponse createTestCaseAddUpdateAutomationScriptResponse() {
        return new TestCaseAddUpdateAutomationScriptResponse();
    }

    /**
     * Create an instance of {@link AutomationEngineRetrieveByIdResponse }
     * 
     */
    public AutomationEngineRetrieveByIdResponse createAutomationEngineRetrieveByIdResponse() {
        return new AutomationEngineRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link ReleaseRetrieveByIdResponse }
     * 
     */
    public ReleaseRetrieveByIdResponse createReleaseRetrieveByIdResponse() {
        return new ReleaseRetrieveByIdResponse();
    }

    /**
     * Create an instance of {@link TaskRetrieveForOwnerResponse }
     * 
     */
    public TaskRetrieveForOwnerResponse createTaskRetrieveForOwnerResponse() {
        return new TaskRetrieveForOwnerResponse();
    }

    /**
     * Create an instance of {@link TestCaseCreate }
     * 
     */
    public TestCaseCreate createTestCaseCreate() {
        return new TestCaseCreate();
    }

    /**
     * Create an instance of {@link ArrayOfRemoteTestRunStep }
     * 
     */
    public ArrayOfRemoteTestRunStep createArrayOfRemoteTestRunStep() {
        return new ArrayOfRemoteTestRunStep();
    }

    /**
     * Create an instance of {@link TestRunRetrieveManualById }
     * 
     */
    public TestRunRetrieveManualById createTestRunRetrieveManualById() {
        return new TestRunRetrieveManualById();
    }

    /**
     * Create an instance of {@link RemoteTestRun }
     * 
     */
    public RemoteTestRun createRemoteTestRun() {
        return new RemoteTestRun();
    }

    /**
     * Create an instance of {@link UserRetrieveByUserNameResponse }
     * 
     */
    public UserRetrieveByUserNameResponse createUserRetrieveByUserNameResponse() {
        return new UserRetrieveByUserNameResponse();
    }

    /**
     * Create an instance of {@link BuildCreate }
     * 
     */
    public BuildCreate createBuildCreate() {
        return new BuildCreate();
    }

    /**
     * Create an instance of {@link TestCaseDeleteStepResponse }
     * 
     */
    public TestCaseDeleteStepResponse createTestCaseDeleteStepResponse() {
        return new TestCaseDeleteStepResponse();
    }

    /**
     * Create an instance of {@link TestCaseCountForFolderResponse }
     * 
     */
    public TestCaseCountForFolderResponse createTestCaseCountForFolderResponse() {
        return new TestCaseCountForFolderResponse();
    }

    /**
     * Create an instance of {@link CustomPropertyRetrieveCustomListByIdResponse }
     * 
     */
    public CustomPropertyRetrieveCustomListByIdResponse createCustomPropertyRetrieveCustomListByIdResponse() {
        return new CustomPropertyRetrieveCustomListByIdResponse();
    }

    /**
     * Create an instance of {@link ValidationFaultMessageItem }
     * 
     */
    public ValidationFaultMessageItem createValidationFaultMessageItem() {
        return new ValidationFaultMessageItem();
    }

    /**
     * Create an instance of {@link TestCaseRetrieve }
     * 
     */
    public TestCaseRetrieve createTestCaseRetrieve() {
        return new TestCaseRetrieve();
    }

    /**
     * Create an instance of {@link RequirementUpdate }
     * 
     */
    public RequirementUpdate createRequirementUpdate() {
        return new RequirementUpdate();
    }

    /**
     * Create an instance of {@link RemoteTestSet }
     * 
     */
    public RemoteTestSet createRemoteTestSet() {
        return new RemoteTestSet();
    }

    /**
     * Create an instance of {@link IncidentRetrieveNewResponse }
     * 
     */
    public IncidentRetrieveNewResponse createIncidentRetrieveNewResponse() {
        return new IncidentRetrieveNewResponse();
    }

    /**
     * Create an instance of {@link RemoteIncidentType }
     * 
     */
    public RemoteIncidentType createRemoteIncidentType() {
        return new RemoteIncidentType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentSeverity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteIncidentSeverity")
    public JAXBElement<ArrayOfRemoteIncidentSeverity> createArrayOfRemoteIncidentSeverity(ArrayOfRemoteIncidentSeverity value) {
        return new JAXBElement<ArrayOfRemoteIncidentSeverity>(_ArrayOfRemoteIncidentSeverity_QNAME, ArrayOfRemoteIncidentSeverity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteWorkflowIncidentFields }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteWorkflowIncidentFields")
    public JAXBElement<ArrayOfRemoteWorkflowIncidentFields> createArrayOfRemoteWorkflowIncidentFields(ArrayOfRemoteWorkflowIncidentFields value) {
        return new JAXBElement<ArrayOfRemoteWorkflowIncidentFields>(_ArrayOfRemoteWorkflowIncidentFields_QNAME, ArrayOfRemoteWorkflowIncidentFields.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteDataMapping")
    public JAXBElement<RemoteDataMapping> createRemoteDataMapping(RemoteDataMapping value) {
        return new JAXBElement<RemoteDataMapping>(_RemoteDataMapping_QNAME, RemoteDataMapping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRelease }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteRelease")
    public JAXBElement<RemoteRelease> createRemoteRelease(RemoteRelease value) {
        return new JAXBElement<RemoteRelease>(_RemoteRelease_QNAME, RemoteRelease.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteIncident")
    public JAXBElement<ArrayOfRemoteIncident> createArrayOfRemoteIncident(ArrayOfRemoteIncident value) {
        return new JAXBElement<ArrayOfRemoteIncident>(_ArrayOfRemoteIncident_QNAME, ArrayOfRemoteIncident.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteFilter")
    public JAXBElement<RemoteFilter> createRemoteFilter(RemoteFilter value) {
        return new JAXBElement<RemoteFilter>(_RemoteFilter_QNAME, RemoteFilter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDocumentFolder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteDocumentFolder")
    public JAXBElement<ArrayOfRemoteDocumentFolder> createArrayOfRemoteDocumentFolder(ArrayOfRemoteDocumentFolder value) {
        return new JAXBElement<ArrayOfRemoteDocumentFolder>(_ArrayOfRemoteDocumentFolder_QNAME, ArrayOfRemoteDocumentFolder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSetTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTestSetTestCaseMapping")
    public JAXBElement<RemoteTestSetTestCaseMapping> createRemoteTestSetTestCaseMapping(RemoteTestSetTestCaseMapping value) {
        return new JAXBElement<RemoteTestSetTestCaseMapping>(_RemoteTestSetTestCaseMapping_QNAME, RemoteTestSetTestCaseMapping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfValidationFaultMessageItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "ArrayOfValidationFaultMessageItem")
    public JAXBElement<ArrayOfValidationFaultMessageItem> createArrayOfValidationFaultMessageItem(ArrayOfValidationFaultMessageItem value) {
        return new JAXBElement<ArrayOfValidationFaultMessageItem>(_ArrayOfValidationFaultMessageItem_QNAME, ArrayOfValidationFaultMessageItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteArtifact }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteArtifact")
    public JAXBElement<RemoteArtifact> createRemoteArtifact(RemoteArtifact value) {
        return new JAXBElement<RemoteArtifact>(_RemoteArtifact_QNAME, RemoteArtifact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteIncidentStatus")
    public JAXBElement<ArrayOfRemoteIncidentStatus> createArrayOfRemoteIncidentStatus(ArrayOfRemoteIncidentStatus value) {
        return new JAXBElement<ArrayOfRemoteIncidentStatus>(_ArrayOfRemoteIncidentStatus_QNAME, ArrayOfRemoteIncidentStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFaultMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "ServiceFaultMessage")
    public JAXBElement<ServiceFaultMessage> createServiceFaultMessage(ServiceFaultMessage value) {
        return new JAXBElement<ServiceFaultMessage>(_ServiceFaultMessage_QNAME, ServiceFaultMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidationFaultMessageItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "ValidationFaultMessageItem")
    public JAXBElement<ValidationFaultMessageItem> createValidationFaultMessageItem(ValidationFaultMessageItem value) {
        return new JAXBElement<ValidationFaultMessageItem>(_ValidationFaultMessageItem_QNAME, ValidationFaultMessageItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteProject")
    public JAXBElement<RemoteProject> createRemoteProject(RemoteProject value) {
        return new JAXBElement<RemoteProject>(_RemoteProject_QNAME, RemoteProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDocumentVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteDocumentVersion")
    public JAXBElement<ArrayOfRemoteDocumentVersion> createArrayOfRemoteDocumentVersion(ArrayOfRemoteDocumentVersion value) {
        return new JAXBElement<ArrayOfRemoteDocumentVersion>(_ArrayOfRemoteDocumentVersion_QNAME, ArrayOfRemoteDocumentVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfint")
    public JAXBElement<ArrayOfint> createArrayOfint(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_ArrayOfint_QNAME, ArrayOfint.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestRunStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTestRunStep")
    public JAXBElement<RemoteTestRunStep> createRemoteTestRunStep(RemoteTestRunStep value) {
        return new JAXBElement<RemoteTestRunStep>(_RemoteTestRunStep_QNAME, RemoteTestRunStep.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteComment")
    public JAXBElement<RemoteComment> createRemoteComment(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_RemoteComment_QNAME, RemoteComment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTask")
    public JAXBElement<ArrayOfRemoteTask> createArrayOfRemoteTask(ArrayOfRemoteTask value) {
        return new JAXBElement<ArrayOfRemoteTask>(_ArrayOfRemoteTask_QNAME, ArrayOfRemoteTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirementTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteRequirementTestCaseMapping")
    public JAXBElement<RemoteRequirementTestCaseMapping> createRemoteRequirementTestCaseMapping(RemoteRequirementTestCaseMapping value) {
        return new JAXBElement<RemoteRequirementTestCaseMapping>(_RemoteRequirementTestCaseMapping_QNAME, RemoteRequirementTestCaseMapping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentFolder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteDocumentFolder")
    public JAXBElement<RemoteDocumentFolder> createRemoteDocumentFolder(RemoteDocumentFolder value) {
        return new JAXBElement<RemoteDocumentFolder>(_RemoteDocumentFolder_QNAME, RemoteDocumentFolder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteBuildSourceCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteBuildSourceCode")
    public JAXBElement<RemoteBuildSourceCode> createRemoteBuildSourceCode(RemoteBuildSourceCode value) {
        return new JAXBElement<RemoteBuildSourceCode>(_RemoteBuildSourceCode_QNAME, RemoteBuildSourceCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultiValueFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "MultiValueFilter")
    public JAXBElement<MultiValueFilter> createMultiValueFilter(MultiValueFilter value) {
        return new JAXBElement<MultiValueFilter>(_MultiValueFilter_QNAME, MultiValueFilter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteWorkflowIncidentFields }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteWorkflowIncidentFields")
    public JAXBElement<RemoteWorkflowIncidentFields> createRemoteWorkflowIncidentFields(RemoteWorkflowIncidentFields value) {
        return new JAXBElement<RemoteWorkflowIncidentFields>(_RemoteWorkflowIncidentFields_QNAME, RemoteWorkflowIncidentFields.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteManualTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteManualTestRun")
    public JAXBElement<ArrayOfRemoteManualTestRun> createArrayOfRemoteManualTestRun(ArrayOfRemoteManualTestRun value) {
        return new JAXBElement<ArrayOfRemoteManualTestRun>(_ArrayOfRemoteManualTestRun_QNAME, ArrayOfRemoteManualTestRun.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteDocument")
    public JAXBElement<ArrayOfRemoteDocument> createArrayOfRemoteDocument(ArrayOfRemoteDocument value) {
        return new JAXBElement<ArrayOfRemoteDocument>(_ArrayOfRemoteDocument_QNAME, ArrayOfRemoteDocument.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCaseParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTestCaseParameter")
    public JAXBElement<RemoteTestCaseParameter> createRemoteTestCaseParameter(RemoteTestCaseParameter value) {
        return new JAXBElement<RemoteTestCaseParameter>(_RemoteTestCaseParameter_QNAME, RemoteTestCaseParameter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteProjectUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteProjectUser")
    public JAXBElement<ArrayOfRemoteProjectUser> createArrayOfRemoteProjectUser(ArrayOfRemoteProjectUser value) {
        return new JAXBElement<ArrayOfRemoteProjectUser>(_ArrayOfRemoteProjectUser_QNAME, ArrayOfRemoteProjectUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteWorkflowIncidentTransition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteWorkflowIncidentTransition")
    public JAXBElement<ArrayOfRemoteWorkflowIncidentTransition> createArrayOfRemoteWorkflowIncidentTransition(ArrayOfRemoteWorkflowIncidentTransition value) {
        return new JAXBElement<ArrayOfRemoteWorkflowIncidentTransition>(_ArrayOfRemoteWorkflowIncidentTransition_QNAME, ArrayOfRemoteWorkflowIncidentTransition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteSort")
    public JAXBElement<RemoteSort> createRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_RemoteSort_QNAME, RemoteSort.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteRequirementTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteRequirementTestCaseMapping")
    public JAXBElement<ArrayOfRemoteRequirementTestCaseMapping> createArrayOfRemoteRequirementTestCaseMapping(ArrayOfRemoteRequirementTestCaseMapping value) {
        return new JAXBElement<ArrayOfRemoteRequirementTestCaseMapping>(_ArrayOfRemoteRequirementTestCaseMapping_QNAME, ArrayOfRemoteRequirementTestCaseMapping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteAutomationHost")
    public JAXBElement<RemoteAutomationHost> createRemoteAutomationHost(RemoteAutomationHost value) {
        return new JAXBElement<RemoteAutomationHost>(_RemoteAutomationHost_QNAME, RemoteAutomationHost.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteAssociation")
    public JAXBElement<ArrayOfRemoteAssociation> createArrayOfRemoteAssociation(ArrayOfRemoteAssociation value) {
        return new JAXBElement<ArrayOfRemoteAssociation>(_ArrayOfRemoteAssociation_QNAME, ArrayOfRemoteAssociation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteWorkflowIncidentCustomProperties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteWorkflowIncidentCustomProperties")
    public JAXBElement<RemoteWorkflowIncidentCustomProperties> createRemoteWorkflowIncidentCustomProperties(RemoteWorkflowIncidentCustomProperties value) {
        return new JAXBElement<RemoteWorkflowIncidentCustomProperties>(_RemoteWorkflowIncidentCustomProperties_QNAME, RemoteWorkflowIncidentCustomProperties.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteReleaseTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteReleaseTestCaseMapping")
    public JAXBElement<ArrayOfRemoteReleaseTestCaseMapping> createArrayOfRemoteReleaseTestCaseMapping(ArrayOfRemoteReleaseTestCaseMapping value) {
        return new JAXBElement<ArrayOfRemoteReleaseTestCaseMapping>(_ArrayOfRemoteReleaseTestCaseMapping_QNAME, ArrayOfRemoteReleaseTestCaseMapping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteAutomatedTestRun")
    public JAXBElement<ArrayOfRemoteAutomatedTestRun> createArrayOfRemoteAutomatedTestRun(ArrayOfRemoteAutomatedTestRun value) {
        return new JAXBElement<ArrayOfRemoteAutomatedTestRun>(_ArrayOfRemoteAutomatedTestRun_QNAME, ArrayOfRemoteAutomatedTestRun.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTestSetTestCaseMapping")
    public JAXBElement<ArrayOfRemoteTestSetTestCaseMapping> createArrayOfRemoteTestSetTestCaseMapping(ArrayOfRemoteTestSetTestCaseMapping value) {
        return new JAXBElement<ArrayOfRemoteTestSetTestCaseMapping>(_ArrayOfRemoteTestSetTestCaseMapping_QNAME, ArrayOfRemoteTestSetTestCaseMapping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteVersion")
    public JAXBElement<RemoteVersion> createRemoteVersion(RemoteVersion value) {
        return new JAXBElement<RemoteVersion>(_RemoteVersion_QNAME, RemoteVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteProject")
    public JAXBElement<ArrayOfRemoteProject> createArrayOfRemoteProject(ArrayOfRemoteProject value) {
        return new JAXBElement<ArrayOfRemoteProject>(_ArrayOfRemoteProject_QNAME, ArrayOfRemoteProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentPriority }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteIncidentPriority")
    public JAXBElement<RemoteIncidentPriority> createRemoteIncidentPriority(RemoteIncidentPriority value) {
        return new JAXBElement<RemoteIncidentPriority>(_RemoteIncidentPriority_QNAME, RemoteIncidentPriority.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteBuild }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteBuild")
    public JAXBElement<ArrayOfRemoteBuild> createArrayOfRemoteBuild(ArrayOfRemoteBuild value) {
        return new JAXBElement<ArrayOfRemoteBuild>(_ArrayOfRemoteBuild_QNAME, ArrayOfRemoteBuild.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteComment")
    public JAXBElement<ArrayOfRemoteComment> createArrayOfRemoteComment(ArrayOfRemoteComment value) {
        return new JAXBElement<ArrayOfRemoteComment>(_ArrayOfRemoteComment_QNAME, ArrayOfRemoteComment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTestStep")
    public JAXBElement<ArrayOfRemoteTestStep> createArrayOfRemoteTestStep(ArrayOfRemoteTestStep value) {
        return new JAXBElement<ArrayOfRemoteTestStep>(_ArrayOfRemoteTestStep_QNAME, ArrayOfRemoteTestStep.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteRequirement")
    public JAXBElement<RemoteRequirement> createRemoteRequirement(RemoteRequirement value) {
        return new JAXBElement<RemoteRequirement>(_RemoteRequirement_QNAME, RemoteRequirement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentSeverity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteIncidentSeverity")
    public JAXBElement<RemoteIncidentSeverity> createRemoteIncidentSeverity(RemoteIncidentSeverity value) {
        return new JAXBElement<RemoteIncidentSeverity>(_RemoteIncidentSeverity_QNAME, RemoteIncidentSeverity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteArtifactCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteArtifactCustomProperty")
    public JAXBElement<ArrayOfRemoteArtifactCustomProperty> createArrayOfRemoteArtifactCustomProperty(ArrayOfRemoteArtifactCustomProperty value) {
        return new JAXBElement<ArrayOfRemoteArtifactCustomProperty>(_ArrayOfRemoteArtifactCustomProperty_QNAME, ArrayOfRemoteArtifactCustomProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteRelease }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteRelease")
    public JAXBElement<ArrayOfRemoteRelease> createArrayOfRemoteRelease(ArrayOfRemoteRelease value) {
        return new JAXBElement<ArrayOfRemoteRelease>(_ArrayOfRemoteRelease_QNAME, ArrayOfRemoteRelease.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteIncident")
    public JAXBElement<RemoteIncident> createRemoteIncident(RemoteIncident value) {
        return new JAXBElement<RemoteIncident>(_RemoteIncident_QNAME, RemoteIncident.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteUser")
    public JAXBElement<RemoteUser> createRemoteUser(RemoteUser value) {
        return new JAXBElement<RemoteUser>(_RemoteUser_QNAME, RemoteUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteWorkflowIncidentCustomProperties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteWorkflowIncidentCustomProperties")
    public JAXBElement<ArrayOfRemoteWorkflowIncidentCustomProperties> createArrayOfRemoteWorkflowIncidentCustomProperties(ArrayOfRemoteWorkflowIncidentCustomProperties value) {
        return new JAXBElement<ArrayOfRemoteWorkflowIncidentCustomProperties>(_ArrayOfRemoteWorkflowIncidentCustomProperties_QNAME, ArrayOfRemoteWorkflowIncidentCustomProperties.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomPropertyOption }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteCustomPropertyOption")
    public JAXBElement<ArrayOfRemoteCustomPropertyOption> createArrayOfRemoteCustomPropertyOption(ArrayOfRemoteCustomPropertyOption value) {
        return new JAXBElement<ArrayOfRemoteCustomPropertyOption>(_ArrayOfRemoteCustomPropertyOption_QNAME, ArrayOfRemoteCustomPropertyOption.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteBuild }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteBuild")
    public JAXBElement<RemoteBuild> createRemoteBuild(RemoteBuild value) {
        return new JAXBElement<RemoteBuild>(_RemoteBuild_QNAME, RemoteBuild.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTestRun")
    public JAXBElement<ArrayOfRemoteTestRun> createArrayOfRemoteTestRun(ArrayOfRemoteTestRun value) {
        return new JAXBElement<ArrayOfRemoteTestRun>(_ArrayOfRemoteTestRun_QNAME, ArrayOfRemoteTestRun.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTestSetTestCaseParameter")
    public JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> createArrayOfRemoteTestSetTestCaseParameter(ArrayOfRemoteTestSetTestCaseParameter value) {
        return new JAXBElement<ArrayOfRemoteTestSetTestCaseParameter>(_ArrayOfRemoteTestSetTestCaseParameter_QNAME, ArrayOfRemoteTestSetTestCaseParameter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestStepParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTestStepParameter")
    public JAXBElement<RemoteTestStepParameter> createRemoteTestStepParameter(RemoteTestStepParameter value) {
        return new JAXBElement<RemoteTestStepParameter>(_RemoteTestStepParameter_QNAME, RemoteTestStepParameter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDocumentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteDocumentType")
    public JAXBElement<ArrayOfRemoteDocumentType> createArrayOfRemoteDocumentType(ArrayOfRemoteDocumentType value) {
        return new JAXBElement<ArrayOfRemoteDocumentType>(_ArrayOfRemoteDocumentType_QNAME, ArrayOfRemoteDocumentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateRange }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "DateRange")
    public JAXBElement<DateRange> createDateRange(DateRange value) {
        return new JAXBElement<DateRange>(_DateRange_QNAME, DateRange.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteWorkflowIncidentTransition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteWorkflowIncidentTransition")
    public JAXBElement<RemoteWorkflowIncidentTransition> createRemoteWorkflowIncidentTransition(RemoteWorkflowIncidentTransition value) {
        return new JAXBElement<RemoteWorkflowIncidentTransition>(_RemoteWorkflowIncidentTransition_QNAME, RemoteWorkflowIncidentTransition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteAssociation")
    public JAXBElement<RemoteAssociation> createRemoteAssociation(RemoteAssociation value) {
        return new JAXBElement<RemoteAssociation>(_RemoteAssociation_QNAME, RemoteAssociation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomListValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteCustomListValue")
    public JAXBElement<ArrayOfRemoteCustomListValue> createArrayOfRemoteCustomListValue(ArrayOfRemoteCustomListValue value) {
        return new JAXBElement<ArrayOfRemoteCustomListValue>(_ArrayOfRemoteCustomListValue_QNAME, ArrayOfRemoteCustomListValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSetting }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteSetting")
    public JAXBElement<RemoteSetting> createRemoteSetting(RemoteSetting value) {
        return new JAXBElement<RemoteSetting>(_RemoteSetting_QNAME, RemoteSetting.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteDocumentVersion")
    public JAXBElement<RemoteDocumentVersion> createRemoteDocumentVersion(RemoteDocumentVersion value) {
        return new JAXBElement<RemoteDocumentVersion>(_RemoteDocumentVersion_QNAME, RemoteDocumentVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteIncidentStatus")
    public JAXBElement<RemoteIncidentStatus> createRemoteIncidentStatus(RemoteIncidentStatus value) {
        return new JAXBElement<RemoteIncidentStatus>(_RemoteIncidentStatus_QNAME, RemoteIncidentStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteReleaseTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteReleaseTestCaseMapping")
    public JAXBElement<RemoteReleaseTestCaseMapping> createRemoteReleaseTestCaseMapping(RemoteReleaseTestCaseMapping value) {
        return new JAXBElement<RemoteReleaseTestCaseMapping>(_RemoteReleaseTestCaseMapping_QNAME, RemoteReleaseTestCaseMapping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteDataMapping")
    public JAXBElement<ArrayOfRemoteDataMapping> createArrayOfRemoteDataMapping(ArrayOfRemoteDataMapping value) {
        return new JAXBElement<ArrayOfRemoteDataMapping>(_ArrayOfRemoteDataMapping_QNAME, ArrayOfRemoteDataMapping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCaseParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTestCaseParameter")
    public JAXBElement<ArrayOfRemoteTestCaseParameter> createArrayOfRemoteTestCaseParameter(ArrayOfRemoteTestCaseParameter value) {
        return new JAXBElement<ArrayOfRemoteTestCaseParameter>(_ArrayOfRemoteTestCaseParameter_QNAME, ArrayOfRemoteTestCaseParameter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteCustomProperty")
    public JAXBElement<RemoteCustomProperty> createRemoteCustomProperty(RemoteCustomProperty value) {
        return new JAXBElement<RemoteCustomProperty>(_RemoteCustomProperty_QNAME, RemoteCustomProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTestRun")
    public JAXBElement<RemoteTestRun> createRemoteTestRun(RemoteTestRun value) {
        return new JAXBElement<RemoteTestRun>(_RemoteTestRun_QNAME, RemoteTestRun.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteManualTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteManualTestRun")
    public JAXBElement<RemoteManualTestRun> createRemoteManualTestRun(RemoteManualTestRun value) {
        return new JAXBElement<RemoteManualTestRun>(_RemoteManualTestRun_QNAME, RemoteManualTestRun.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteCustomList")
    public JAXBElement<ArrayOfRemoteCustomList> createArrayOfRemoteCustomList(ArrayOfRemoteCustomList value) {
        return new JAXBElement<ArrayOfRemoteCustomList>(_ArrayOfRemoteCustomList_QNAME, ArrayOfRemoteCustomList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestStepParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTestStepParameter")
    public JAXBElement<ArrayOfRemoteTestStepParameter> createArrayOfRemoteTestStepParameter(ArrayOfRemoteTestStepParameter value) {
        return new JAXBElement<ArrayOfRemoteTestStepParameter>(_ArrayOfRemoteTestStepParameter_QNAME, ArrayOfRemoteTestStepParameter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSetTestCaseParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTestSetTestCaseParameter")
    public JAXBElement<RemoteTestSetTestCaseParameter> createRemoteTestSetTestCaseParameter(RemoteTestSetTestCaseParameter value) {
        return new JAXBElement<RemoteTestSetTestCaseParameter>(_RemoteTestSetTestCaseParameter_QNAME, RemoteTestSetTestCaseParameter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteSetting }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteSetting")
    public JAXBElement<ArrayOfRemoteSetting> createArrayOfRemoteSetting(ArrayOfRemoteSetting value) {
        return new JAXBElement<ArrayOfRemoteSetting>(_ArrayOfRemoteSetting_QNAME, ArrayOfRemoteSetting.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomationHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteAutomationHost")
    public JAXBElement<ArrayOfRemoteAutomationHost> createArrayOfRemoteAutomationHost(ArrayOfRemoteAutomationHost value) {
        return new JAXBElement<ArrayOfRemoteAutomationHost>(_ArrayOfRemoteAutomationHost_QNAME, ArrayOfRemoteAutomationHost.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentPriority }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteIncidentPriority")
    public JAXBElement<ArrayOfRemoteIncidentPriority> createArrayOfRemoteIncidentPriority(ArrayOfRemoteIncidentPriority value) {
        return new JAXBElement<ArrayOfRemoteIncidentPriority>(_ArrayOfRemoteIncidentPriority_QNAME, ArrayOfRemoteIncidentPriority.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteProjectUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteProjectUser")
    public JAXBElement<RemoteProjectUser> createRemoteProjectUser(RemoteProjectUser value) {
        return new JAXBElement<RemoteProjectUser>(_RemoteProjectUser_QNAME, RemoteProjectUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteBuildSourceCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteBuildSourceCode")
    public JAXBElement<ArrayOfRemoteBuildSourceCode> createArrayOfRemoteBuildSourceCode(ArrayOfRemoteBuildSourceCode value) {
        return new JAXBElement<ArrayOfRemoteBuildSourceCode>(_ArrayOfRemoteBuildSourceCode_QNAME, ArrayOfRemoteBuildSourceCode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomListValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteCustomListValue")
    public JAXBElement<RemoteCustomListValue> createRemoteCustomListValue(RemoteCustomListValue value) {
        return new JAXBElement<RemoteCustomListValue>(_RemoteCustomListValue_QNAME, RemoteCustomListValue.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteCustomList")
    public JAXBElement<RemoteCustomList> createRemoteCustomList(RemoteCustomList value) {
        return new JAXBElement<RemoteCustomList>(_RemoteCustomList_QNAME, RemoteCustomList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationEngine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteAutomationEngine")
    public JAXBElement<RemoteAutomationEngine> createRemoteAutomationEngine(RemoteAutomationEngine value) {
        return new JAXBElement<RemoteAutomationEngine>(_RemoteAutomationEngine_QNAME, RemoteAutomationEngine.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteFilter")
    public JAXBElement<ArrayOfRemoteFilter> createArrayOfRemoteFilter(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_ArrayOfRemoteFilter_QNAME, ArrayOfRemoteFilter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteDocument")
    public JAXBElement<RemoteDocument> createRemoteDocument(RemoteDocument value) {
        return new JAXBElement<RemoteDocument>(_RemoteDocument_QNAME, RemoteDocument.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomationEngine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteAutomationEngine")
    public JAXBElement<ArrayOfRemoteAutomationEngine> createArrayOfRemoteAutomationEngine(ArrayOfRemoteAutomationEngine value) {
        return new JAXBElement<ArrayOfRemoteAutomationEngine>(_ArrayOfRemoteAutomationEngine_QNAME, ArrayOfRemoteAutomationEngine.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTestCase")
    public JAXBElement<RemoteTestCase> createRemoteTestCase(RemoteTestCase value) {
        return new JAXBElement<RemoteTestCase>(_RemoteTestCase_QNAME, RemoteTestCase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteCustomProperty")
    public JAXBElement<ArrayOfRemoteCustomProperty> createArrayOfRemoteCustomProperty(ArrayOfRemoteCustomProperty value) {
        return new JAXBElement<ArrayOfRemoteCustomProperty>(_ArrayOfRemoteCustomProperty_QNAME, ArrayOfRemoteCustomProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomPropertyOption }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteCustomPropertyOption")
    public JAXBElement<RemoteCustomPropertyOption> createRemoteCustomPropertyOption(RemoteCustomPropertyOption value) {
        return new JAXBElement<RemoteCustomPropertyOption>(_RemoteCustomPropertyOption_QNAME, RemoteCustomPropertyOption.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteProjectRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteProjectRole")
    public JAXBElement<RemoteProjectRole> createRemoteProjectRole(RemoteProjectRole value) {
        return new JAXBElement<RemoteProjectRole>(_RemoteProjectRole_QNAME, RemoteProjectRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteRequirement")
    public JAXBElement<ArrayOfRemoteRequirement> createArrayOfRemoteRequirement(ArrayOfRemoteRequirement value) {
        return new JAXBElement<ArrayOfRemoteRequirement>(_ArrayOfRemoteRequirement_QNAME, ArrayOfRemoteRequirement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteProjectRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteProjectRole")
    public JAXBElement<ArrayOfRemoteProjectRole> createArrayOfRemoteProjectRole(ArrayOfRemoteProjectRole value) {
        return new JAXBElement<ArrayOfRemoteProjectRole>(_ArrayOfRemoteProjectRole_QNAME, ArrayOfRemoteProjectRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteDocumentType")
    public JAXBElement<RemoteDocumentType> createRemoteDocumentType(RemoteDocumentType value) {
        return new JAXBElement<RemoteDocumentType>(_RemoteDocumentType_QNAME, RemoteDocumentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteIncidentType")
    public JAXBElement<ArrayOfRemoteIncidentType> createArrayOfRemoteIncidentType(ArrayOfRemoteIncidentType value) {
        return new JAXBElement<ArrayOfRemoteIncidentType>(_ArrayOfRemoteIncidentType_QNAME, ArrayOfRemoteIncidentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteAutomatedTestRun")
    public JAXBElement<RemoteAutomatedTestRun> createRemoteAutomatedTestRun(RemoteAutomatedTestRun value) {
        return new JAXBElement<RemoteAutomatedTestRun>(_RemoteAutomatedTestRun_QNAME, RemoteAutomatedTestRun.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestRunStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTestRunStep")
    public JAXBElement<ArrayOfRemoteTestRunStep> createArrayOfRemoteTestRunStep(ArrayOfRemoteTestRunStep value) {
        return new JAXBElement<ArrayOfRemoteTestRunStep>(_ArrayOfRemoteTestRunStep_QNAME, ArrayOfRemoteTestRunStep.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTestCase")
    public JAXBElement<ArrayOfRemoteTestCase> createArrayOfRemoteTestCase(ArrayOfRemoteTestCase value) {
        return new JAXBElement<ArrayOfRemoteTestCase>(_ArrayOfRemoteTestCase_QNAME, ArrayOfRemoteTestCase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTestStep")
    public JAXBElement<RemoteTestStep> createRemoteTestStep(RemoteTestStep value) {
        return new JAXBElement<RemoteTestStep>(_RemoteTestStep_QNAME, RemoteTestStep.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidationFaultMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "ValidationFaultMessage")
    public JAXBElement<ValidationFaultMessage> createValidationFaultMessage(ValidationFaultMessage value) {
        return new JAXBElement<ValidationFaultMessage>(_ValidationFaultMessage_QNAME, ValidationFaultMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteIncidentType")
    public JAXBElement<RemoteIncidentType> createRemoteIncidentType(RemoteIncidentType value) {
        return new JAXBElement<RemoteIncidentType>(_RemoteIncidentType_QNAME, RemoteIncidentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteArtifactCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteArtifactCustomProperty")
    public JAXBElement<RemoteArtifactCustomProperty> createRemoteArtifactCustomProperty(RemoteArtifactCustomProperty value) {
        return new JAXBElement<RemoteArtifactCustomProperty>(_RemoteArtifactCustomProperty_QNAME, RemoteArtifactCustomProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArrayOfRemoteTestSet")
    public JAXBElement<ArrayOfRemoteTestSet> createArrayOfRemoteTestSet(ArrayOfRemoteTestSet value) {
        return new JAXBElement<ArrayOfRemoteTestSet>(_ArrayOfRemoteTestSet_QNAME, ArrayOfRemoteTestSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTestSet")
    public JAXBElement<RemoteTestSet> createRemoteTestSet(RemoteTestSet value) {
        return new JAXBElement<RemoteTestSet>(_RemoteTestSet_QNAME, RemoteTestSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemoteTask")
    public JAXBElement<RemoteTask> createRemoteTask(RemoteTask value) {
        return new JAXBElement<RemoteTask>(_RemoteTask_QNAME, RemoteTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "automationHostToken", scope = TestRunCreateForAutomatedTestSet.class)
    public JAXBElement<String> createTestRunCreateForAutomatedTestSetAutomationHostToken(String value) {
        return new JAXBElement<String>(_TestRunCreateForAutomatedTestSetAutomationHostToken_QNAME, String.class, TestRunCreateForAutomatedTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "AutomationHost_RetrieveByTokenResult", scope = AutomationHostRetrieveByTokenResponse.class)
    public JAXBElement<RemoteAutomationHost> createAutomationHostRetrieveByTokenResponseAutomationHostRetrieveByTokenResult(RemoteAutomationHost value) {
        return new JAXBElement<RemoteAutomationHost>(_AutomationHostRetrieveByTokenResponseAutomationHostRetrieveByTokenResult_QNAME, RemoteAutomationHost.class, AutomationHostRetrieveByTokenResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirementTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteReqTestCaseMapping", scope = RequirementAddTestCoverage.class)
    public JAXBElement<RemoteRequirementTestCaseMapping> createRequirementAddTestCoverageRemoteReqTestCaseMapping(RemoteRequirementTestCaseMapping value) {
        return new JAXBElement<RemoteRequirementTestCaseMapping>(_RequirementAddTestCoverageRemoteReqTestCaseMapping_QNAME, RemoteRequirementTestCaseMapping.class, RequirementAddTestCoverage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "endDate", scope = TestRunSave.class)
    public JAXBElement<XMLGregorianCalendar> createTestRunSaveEndDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_TestRunSaveEndDate_QNAME, XMLGregorianCalendar.class, TestRunSave.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteManualTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestRuns", scope = TestRunSave.class)
    public JAXBElement<ArrayOfRemoteManualTestRun> createTestRunSaveRemoteTestRuns(ArrayOfRemoteManualTestRun value) {
        return new JAXBElement<ArrayOfRemoteManualTestRun>(_TestRunSaveRemoteTestRuns_QNAME, ArrayOfRemoteManualTestRun.class, TestRunSave.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Version", scope = RemoteVersion.class)
    public JAXBElement<String> createRemoteVersionVersion(String value) {
        return new JAXBElement<String>(_RemoteVersionVersion_QNAME, String.class, RemoteVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Patch", scope = RemoteVersion.class)
    public JAXBElement<Integer> createRemoteVersionPatch(Integer value) {
        return new JAXBElement<Integer>(_RemoteVersionPatch_QNAME, Integer.class, RemoteVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "LastName", scope = RemoteUser.class)
    public JAXBElement<String> createRemoteUserLastName(String value) {
        return new JAXBElement<String>(_RemoteUserLastName_QNAME, String.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Department", scope = RemoteUser.class)
    public JAXBElement<String> createRemoteUserDepartment(String value) {
        return new JAXBElement<String>(_RemoteUserDepartment_QNAME, String.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "LdapDn", scope = RemoteUser.class)
    public JAXBElement<String> createRemoteUserLdapDn(String value) {
        return new JAXBElement<String>(_RemoteUserLdapDn_QNAME, String.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "UserName", scope = RemoteUser.class)
    public JAXBElement<String> createRemoteUserUserName(String value) {
        return new JAXBElement<String>(_RemoteUserUserName_QNAME, String.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "UserId", scope = RemoteUser.class)
    public JAXBElement<Integer> createRemoteUserUserId(Integer value) {
        return new JAXBElement<Integer>(_RemoteUserUserId_QNAME, Integer.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EmailAddress", scope = RemoteUser.class)
    public JAXBElement<String> createRemoteUserEmailAddress(String value) {
        return new JAXBElement<String>(_RemoteUserEmailAddress_QNAME, String.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RssToken", scope = RemoteUser.class)
    public JAXBElement<String> createRemoteUserRssToken(String value) {
        return new JAXBElement<String>(_RemoteUserRssToken_QNAME, String.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FirstName", scope = RemoteUser.class)
    public JAXBElement<String> createRemoteUserFirstName(String value) {
        return new JAXBElement<String>(_RemoteUserFirstName_QNAME, String.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FullName", scope = RemoteUser.class)
    public JAXBElement<String> createRemoteUserFullName(String value) {
        return new JAXBElement<String>(_RemoteUserFullName_QNAME, String.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "MiddleInitial", scope = RemoteUser.class)
    public JAXBElement<String> createRemoteUserMiddleInitial(String value) {
        return new JAXBElement<String>(_RemoteUserMiddleInitial_QNAME, String.class, RemoteUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "PlannedEffort", scope = RemoteRelease.class)
    public JAXBElement<Integer> createRemoteReleasePlannedEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleasePlannedEffort_QNAME, Integer.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskActualEffort", scope = RemoteRelease.class)
    public JAXBElement<Integer> createRemoteReleaseTaskActualEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseTaskActualEffort_QNAME, Integer.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AvailableEffort", scope = RemoteRelease.class)
    public JAXBElement<Integer> createRemoteReleaseAvailableEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseAvailableEffort_QNAME, Integer.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteRelease.class)
    public JAXBElement<String> createRemoteReleaseDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskCount", scope = RemoteRelease.class)
    public JAXBElement<Integer> createRemoteReleaseTaskCount(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseTaskCount_QNAME, Integer.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ReleaseId", scope = RemoteRelease.class)
    public JAXBElement<Integer> createRemoteReleaseReleaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseReleaseId_QNAME, Integer.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "VersionNumber", scope = RemoteRelease.class)
    public JAXBElement<String> createRemoteReleaseVersionNumber(String value) {
        return new JAXBElement<String>(_RemoteReleaseVersionNumber_QNAME, String.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FullName", scope = RemoteRelease.class)
    public JAXBElement<String> createRemoteReleaseFullName(String value) {
        return new JAXBElement<String>(_RemoteUserFullName_QNAME, String.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IndentLevel", scope = RemoteRelease.class)
    public JAXBElement<String> createRemoteReleaseIndentLevel(String value) {
        return new JAXBElement<String>(_RemoteReleaseIndentLevel_QNAME, String.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreatorId", scope = RemoteRelease.class)
    public JAXBElement<Integer> createRemoteReleaseCreatorId(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseCreatorId_QNAME, Integer.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreatorName", scope = RemoteRelease.class)
    public JAXBElement<String> createRemoteReleaseCreatorName(String value) {
        return new JAXBElement<String>(_RemoteReleaseCreatorName_QNAME, String.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskEstimatedEffort", scope = RemoteRelease.class)
    public JAXBElement<Integer> createRemoteReleaseTaskEstimatedEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseTaskEstimatedEffort_QNAME, Integer.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteRelease.class)
    public JAXBElement<String> createRemoteReleaseName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteRelease.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestCaseCount.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestCaseCountRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestCaseCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "DataMapping_RetrieveProjectMappingsResult", scope = DataMappingRetrieveProjectMappingsResponse.class)
    public JAXBElement<ArrayOfRemoteDataMapping> createDataMappingRetrieveProjectMappingsResponseDataMappingRetrieveProjectMappingsResult(ArrayOfRemoteDataMapping value) {
        return new JAXBElement<ArrayOfRemoteDataMapping>(_DataMappingRetrieveProjectMappingsResponseDataMappingRetrieveProjectMappingsResult_QNAME, ArrayOfRemoteDataMapping.class, DataMappingRetrieveProjectMappingsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = DocumentRetrieveForFolder.class)
    public JAXBElement<RemoteSort> createDocumentRetrieveForFolderRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, DocumentRetrieveForFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = DocumentRetrieveForFolder.class)
    public JAXBElement<ArrayOfRemoteFilter> createDocumentRetrieveForFolderRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, DocumentRetrieveForFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AttachmentVersionId", scope = RemoteDocumentVersion.class)
    public JAXBElement<Integer> createRemoteDocumentVersionAttachmentVersionId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentVersionAttachmentVersionId_QNAME, Integer.class, RemoteDocumentVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AuthorId", scope = RemoteDocumentVersion.class)
    public JAXBElement<Integer> createRemoteDocumentVersionAuthorId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentVersionAuthorId_QNAME, Integer.class, RemoteDocumentVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "VersionNumber", scope = RemoteDocumentVersion.class)
    public JAXBElement<String> createRemoteDocumentVersionVersionNumber(String value) {
        return new JAXBElement<String>(_RemoteReleaseVersionNumber_QNAME, String.class, RemoteDocumentVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AuthorName", scope = RemoteDocumentVersion.class)
    public JAXBElement<String> createRemoteDocumentVersionAuthorName(String value) {
        return new JAXBElement<String>(_RemoteDocumentVersionAuthorName_QNAME, String.class, RemoteDocumentVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FilenameOrUrl", scope = RemoteDocumentVersion.class)
    public JAXBElement<String> createRemoteDocumentVersionFilenameOrUrl(String value) {
        return new JAXBElement<String>(_RemoteDocumentVersionFilenameOrUrl_QNAME, String.class, RemoteDocumentVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteDocumentVersion.class)
    public JAXBElement<String> createRemoteDocumentVersionDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteDocumentVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentStatusId", scope = RemoteIncidentStatus.class)
    public JAXBElement<Integer> createRemoteIncidentStatusIncidentStatusId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentStatusIncidentStatusId_QNAME, Integer.class, RemoteIncidentStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteIncidentStatus.class)
    public JAXBElement<String> createRemoteIncidentStatusName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteIncidentStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestSetRetrieve.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestSetRetrieveRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestSetRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "releaseId", scope = TestRunCreateFromTestCases.class)
    public JAXBElement<Integer> createTestRunCreateFromTestCasesReleaseId(Integer value) {
        return new JAXBElement<Integer>(_TestRunCreateFromTestCasesReleaseId_QNAME, Integer.class, TestRunCreateFromTestCases.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "testCaseIds", scope = TestRunCreateFromTestCases.class)
    public JAXBElement<ArrayOfint> createTestRunCreateFromTestCasesTestCaseIds(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_TestRunCreateFromTestCasesTestCaseIds_QNAME, ArrayOfint.class, TestRunCreateFromTestCases.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "DataMapping_RetrieveCustomPropertyMappingResult", scope = DataMappingRetrieveCustomPropertyMappingResponse.class)
    public JAXBElement<RemoteDataMapping> createDataMappingRetrieveCustomPropertyMappingResponseDataMappingRetrieveCustomPropertyMappingResult(RemoteDataMapping value) {
        return new JAXBElement<RemoteDataMapping>(_DataMappingRetrieveCustomPropertyMappingResponseDataMappingRetrieveCustomPropertyMappingResult_QNAME, RemoteDataMapping.class, DataMappingRetrieveCustomPropertyMappingResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomListValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "CustomProperty_AddCustomListValueResult", scope = CustomPropertyAddCustomListValueResponse.class)
    public JAXBElement<RemoteCustomListValue> createCustomPropertyAddCustomListValueResponseCustomPropertyAddCustomListValueResult(RemoteCustomListValue value) {
        return new JAXBElement<RemoteCustomListValue>(_CustomPropertyAddCustomListValueResponseCustomPropertyAddCustomListValueResult_QNAME, RemoteCustomListValue.class, CustomPropertyAddCustomListValueResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "parentTestFolderId", scope = TestCaseCreateFolder.class)
    public JAXBElement<Integer> createTestCaseCreateFolderParentTestFolderId(Integer value) {
        return new JAXBElement<Integer>(_TestCaseCreateFolderParentTestFolderId_QNAME, Integer.class, TestCaseCreateFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestCase", scope = TestCaseCreateFolder.class)
    public JAXBElement<RemoteTestCase> createTestCaseCreateFolderRemoteTestCase(RemoteTestCase value) {
        return new JAXBElement<RemoteTestCase>(_TestCaseCreateFolderRemoteTestCase_QNAME, RemoteTestCase.class, TestCaseCreateFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentSeverity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveSeveritiesResult", scope = IncidentRetrieveSeveritiesResponse.class)
    public JAXBElement<ArrayOfRemoteIncidentSeverity> createIncidentRetrieveSeveritiesResponseIncidentRetrieveSeveritiesResult(ArrayOfRemoteIncidentSeverity value) {
        return new JAXBElement<ArrayOfRemoteIncidentSeverity>(_IncidentRetrieveSeveritiesResponseIncidentRetrieveSeveritiesResult_QNAME, ArrayOfRemoteIncidentSeverity.class, IncidentRetrieveSeveritiesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Task_RetrieveResult", scope = TaskRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteTask> createTaskRetrieveResponseTaskRetrieveResult(ArrayOfRemoteTask value) {
        return new JAXBElement<ArrayOfRemoteTask>(_TaskRetrieveResponseTaskRetrieveResult_QNAME, ArrayOfRemoteTask.class, TaskRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestSet", scope = TestSetUpdate.class)
    public JAXBElement<RemoteTestSet> createTestSetUpdateRemoteTestSet(RemoteTestSet value) {
        return new JAXBElement<RemoteTestSet>(_TestSetUpdateRemoteTestSet_QNAME, RemoteTestSet.class, TestSetUpdate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_AddTypeResult", scope = IncidentAddTypeResponse.class)
    public JAXBElement<RemoteIncidentType> createIncidentAddTypeResponseIncidentAddTypeResult(RemoteIncidentType value) {
        return new JAXBElement<RemoteIncidentType>(_IncidentAddTypeResponseIncidentAddTypeResult_QNAME, RemoteIncidentType.class, IncidentAddTypeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestSet_CreateCommentResult", scope = TestSetCreateCommentResponse.class)
    public JAXBElement<RemoteComment> createTestSetCreateCommentResponseTestSetCreateCommentResult(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_TestSetCreateCommentResponseTestSetCreateCommentResult_QNAME, RemoteComment.class, TestSetCreateCommentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_AddUrlResult", scope = DocumentAddUrlResponse.class)
    public JAXBElement<RemoteDocument> createDocumentAddUrlResponseDocumentAddUrlResult(RemoteDocument value) {
        return new JAXBElement<RemoteDocument>(_DocumentAddUrlResponseDocumentAddUrlResult_QNAME, RemoteDocument.class, DocumentAddUrlResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "CustomProperty_AddCustomListResult", scope = CustomPropertyAddCustomListResponse.class)
    public JAXBElement<RemoteCustomList> createCustomPropertyAddCustomListResponseCustomPropertyAddCustomListResult(RemoteCustomList value) {
        return new JAXBElement<RemoteCustomList>(_CustomPropertyAddCustomListResponseCustomPropertyAddCustomListResult_QNAME, RemoteCustomList.class, CustomPropertyAddCustomListResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_AddUrlVersionResult", scope = DocumentAddUrlVersionResponse.class)
    public JAXBElement<RemoteDocumentVersion> createDocumentAddUrlVersionResponseDocumentAddUrlVersionResult(RemoteDocumentVersion value) {
        return new JAXBElement<RemoteDocumentVersion>(_DocumentAddUrlVersionResponseDocumentAddUrlVersionResult_QNAME, RemoteDocumentVersion.class, DocumentAddUrlVersionResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Task_RetrieveNewResult", scope = TaskRetrieveNewResponse.class)
    public JAXBElement<ArrayOfRemoteTask> createTaskRetrieveNewResponseTaskRetrieveNewResult(ArrayOfRemoteTask value) {
        return new JAXBElement<ArrayOfRemoteTask>(_TaskRetrieveNewResponseTaskRetrieveNewResult_QNAME, ArrayOfRemoteTask.class, TaskRetrieveNewResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteAutomationHost", scope = AutomationHostUpdate.class)
    public JAXBElement<RemoteAutomationHost> createAutomationHostUpdateRemoteAutomationHost(RemoteAutomationHost value) {
        return new JAXBElement<RemoteAutomationHost>(_AutomationHostUpdateRemoteAutomationHost_QNAME, RemoteAutomationHost.class, AutomationHostUpdate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "runnerMessage", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<String> createTestRunRecordAutomated2RunnerMessage(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2RunnerMessage_QNAME, String.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "buildId", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<Integer> createTestRunRecordAutomated2BuildId(Integer value) {
        return new JAXBElement<Integer>(_TestRunRecordAutomated2BuildId_QNAME, Integer.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "releaseId", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<Integer> createTestRunRecordAutomated2ReleaseId(Integer value) {
        return new JAXBElement<Integer>(_TestRunCreateFromTestCasesReleaseId_QNAME, Integer.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "testSetId", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<Integer> createTestRunRecordAutomated2TestSetId(Integer value) {
        return new JAXBElement<Integer>(_TestRunRecordAutomated2TestSetId_QNAME, Integer.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "password", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<String> createTestRunRecordAutomated2Password(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2Password_QNAME, String.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "runnerName", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<String> createTestRunRecordAutomated2RunnerName(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2RunnerName_QNAME, String.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "runnerStackTrace", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<String> createTestRunRecordAutomated2RunnerStackTrace(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2RunnerStackTrace_QNAME, String.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "runnerTestName", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<String> createTestRunRecordAutomated2RunnerTestName(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2RunnerTestName_QNAME, String.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "testSetTestCaseId", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<Integer> createTestRunRecordAutomated2TestSetTestCaseId(Integer value) {
        return new JAXBElement<Integer>(_TestRunRecordAutomated2TestSetTestCaseId_QNAME, Integer.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "testerUserId", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<Integer> createTestRunRecordAutomated2TesterUserId(Integer value) {
        return new JAXBElement<Integer>(_TestRunRecordAutomated2TesterUserId_QNAME, Integer.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "userName", scope = TestRunRecordAutomated2 .class)
    public JAXBElement<String> createTestRunRecordAutomated2UserName(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2UserName_QNAME, String.class, TestRunRecordAutomated2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteDocument", scope = DocumentAddUrl.class)
    public JAXBElement<RemoteDocument> createDocumentAddUrlRemoteDocument(RemoteDocument value) {
        return new JAXBElement<RemoteDocument>(_DocumentAddUrlRemoteDocument_QNAME, RemoteDocument.class, DocumentAddUrl.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = TaskRetrieve.class)
    public JAXBElement<RemoteSort> createTaskRetrieveRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, TaskRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TaskRetrieve.class)
    public JAXBElement<ArrayOfRemoteFilter> createTaskRetrieveRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TaskRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationEngine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteEngine", scope = AutomationEngineCreate.class)
    public JAXBElement<RemoteAutomationEngine> createAutomationEngineCreateRemoteEngine(RemoteAutomationEngine value) {
        return new JAXBElement<RemoteAutomationEngine>(_AutomationEngineCreateRemoteEngine_QNAME, RemoteAutomationEngine.class, AutomationEngineCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteRelease }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Release_Retrieve2Result", scope = ReleaseRetrieve2Response.class)
    public JAXBElement<ArrayOfRemoteRelease> createReleaseRetrieve2ResponseReleaseRetrieve2Result(ArrayOfRemoteRelease value) {
        return new JAXBElement<ArrayOfRemoteRelease>(_ReleaseRetrieve2ResponseReleaseRetrieve2Result_QNAME, ArrayOfRemoteRelease.class, ReleaseRetrieve2Response.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "parentRequirementId", scope = RequirementCreate2 .class)
    public JAXBElement<Integer> createRequirementCreate2ParentRequirementId(Integer value) {
        return new JAXBElement<Integer>(_RequirementCreate2ParentRequirementId_QNAME, Integer.class, RequirementCreate2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteRequirement", scope = RequirementCreate2 .class)
    public JAXBElement<RemoteRequirement> createRequirementCreate2RemoteRequirement(RemoteRequirement value) {
        return new JAXBElement<RemoteRequirement>(_RequirementCreate2RemoteRequirement_QNAME, RemoteRequirement.class, RequirementCreate2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteComment", scope = RequirementCreateComment.class)
    public JAXBElement<RemoteComment> createRequirementCreateCommentRemoteComment(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_RequirementCreateCommentRemoteComment_QNAME, RemoteComment.class, RequirementCreateComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = ReleaseRetrieve2 .class)
    public JAXBElement<ArrayOfRemoteFilter> createReleaseRetrieve2RemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, ReleaseRetrieve2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Association_RetrieveForArtifactResult", scope = AssociationRetrieveForArtifactResponse.class)
    public JAXBElement<ArrayOfRemoteAssociation> createAssociationRetrieveForArtifactResponseAssociationRetrieveForArtifactResult(ArrayOfRemoteAssociation value) {
        return new JAXBElement<ArrayOfRemoteAssociation>(_AssociationRetrieveForArtifactResponseAssociationRetrieveForArtifactResult_QNAME, ArrayOfRemoteAssociation.class, AssociationRetrieveForArtifactResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CustomPropertyValueId", scope = RemoteCustomListValue.class)
    public JAXBElement<Integer> createRemoteCustomListValueCustomPropertyValueId(Integer value) {
        return new JAXBElement<Integer>(_RemoteCustomListValueCustomPropertyValueId_QNAME, Integer.class, RemoteCustomListValue.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteCustomListValue.class)
    public JAXBElement<String> createRemoteCustomListValueName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteCustomListValue.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Task_CreateResult", scope = TaskCreateResponse.class)
    public JAXBElement<RemoteTask> createTaskCreateResponseTaskCreateResult(RemoteTask value) {
        return new JAXBElement<RemoteTask>(_TaskCreateResponseTaskCreateResult_QNAME, RemoteTask.class, TaskCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteDataMappings", scope = DataMappingAddUserMappings.class)
    public JAXBElement<ArrayOfRemoteDataMapping> createDataMappingAddUserMappingsRemoteDataMappings(ArrayOfRemoteDataMapping value) {
        return new JAXBElement<ArrayOfRemoteDataMapping>(_DataMappingAddUserMappingsRemoteDataMappings_QNAME, ArrayOfRemoteDataMapping.class, DataMappingAddUserMappings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteManualTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_SaveResult", scope = TestRunSaveResponse.class)
    public JAXBElement<ArrayOfRemoteManualTestRun> createTestRunSaveResponseTestRunSaveResult(ArrayOfRemoteManualTestRun value) {
        return new JAXBElement<ArrayOfRemoteManualTestRun>(_TestRunSaveResponseTestRunSaveResult_QNAME, ArrayOfRemoteManualTestRun.class, TestRunSaveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomListValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteCustomListValue", scope = CustomPropertyAddCustomListValue.class)
    public JAXBElement<RemoteCustomListValue> createCustomPropertyAddCustomListValueRemoteCustomListValue(RemoteCustomListValue value) {
        return new JAXBElement<RemoteCustomListValue>(_CustomPropertyAddCustomListValueRemoteCustomListValue_QNAME, RemoteCustomListValue.class, CustomPropertyAddCustomListValue.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "token", scope = AutomationHostRetrieveByToken.class)
    public JAXBElement<String> createAutomationHostRetrieveByTokenToken(String value) {
        return new JAXBElement<String>(_AutomationHostRetrieveByTokenToken_QNAME, String.class, AutomationHostRetrieveByToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Requirement_Create2Result", scope = RequirementCreate2Response.class)
    public JAXBElement<RemoteRequirement> createRequirementCreate2ResponseRequirementCreate2Result(RemoteRequirement value) {
        return new JAXBElement<RemoteRequirement>(_RequirementCreate2ResponseRequirementCreate2Result_QNAME, RemoteRequirement.class, RequirementCreate2Response.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerId", scope = RemoteTestSetTestCaseMapping.class)
    public JAXBElement<Integer> createRemoteTestSetTestCaseMappingOwnerId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetTestCaseMappingOwnerId_QNAME, Integer.class, RemoteTestSetTestCaseMapping.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentFolder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteDocumentFolder", scope = DocumentUpdateFolder.class)
    public JAXBElement<RemoteDocumentFolder> createDocumentUpdateFolderRemoteDocumentFolder(RemoteDocumentFolder value) {
        return new JAXBElement<RemoteDocumentFolder>(_DocumentUpdateFolderRemoteDocumentFolder_QNAME, RemoteDocumentFolder.class, DocumentUpdateFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRelease }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Release_CreateResult", scope = ReleaseCreateResponse.class)
    public JAXBElement<RemoteRelease> createReleaseCreateResponseReleaseCreateResult(RemoteRelease value) {
        return new JAXBElement<RemoteRelease>(_ReleaseCreateResponseReleaseCreateResult_QNAME, RemoteRelease.class, ReleaseCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteBuild }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Build_RetrieveByIdResult", scope = BuildRetrieveByIdResponse.class)
    public JAXBElement<RemoteBuild> createBuildRetrieveByIdResponseBuildRetrieveByIdResult(RemoteBuild value) {
        return new JAXBElement<RemoteBuild>(_BuildRetrieveByIdResponseBuildRetrieveByIdResult_QNAME, RemoteBuild.class, BuildRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "DataMapping_RetrieveUserMappingsResult", scope = DataMappingRetrieveUserMappingsResponse.class)
    public JAXBElement<ArrayOfRemoteDataMapping> createDataMappingRetrieveUserMappingsResponseDataMappingRetrieveUserMappingsResult(ArrayOfRemoteDataMapping value) {
        return new JAXBElement<ArrayOfRemoteDataMapping>(_DataMappingRetrieveUserMappingsResponseDataMappingRetrieveUserMappingsResult_QNAME, ArrayOfRemoteDataMapping.class, DataMappingRetrieveUserMappingsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectRoleName", scope = RemoteProjectUser.class)
    public JAXBElement<String> createRemoteProjectUserProjectRoleName(String value) {
        return new JAXBElement<String>(_RemoteProjectUserProjectRoleName_QNAME, String.class, RemoteProjectUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Task_RetrieveByIdResult", scope = TaskRetrieveByIdResponse.class)
    public JAXBElement<RemoteTask> createTaskRetrieveByIdResponseTaskRetrieveByIdResult(RemoteTask value) {
        return new JAXBElement<RemoteTask>(_TaskRetrieveByIdResponseTaskRetrieveByIdResult_QNAME, RemoteTask.class, TaskRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationEngine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "AutomationEngine_CreateResult", scope = AutomationEngineCreateResponse.class)
    public JAXBElement<RemoteAutomationEngine> createAutomationEngineCreateResponseAutomationEngineCreateResult(RemoteAutomationEngine value) {
        return new JAXBElement<RemoteAutomationEngine>(_AutomationEngineCreateResponseAutomationEngineCreateResult_QNAME, RemoteAutomationEngine.class, AutomationEngineCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Requirement_RetrieveByIdResult", scope = RequirementRetrieveByIdResponse.class)
    public JAXBElement<RemoteRequirement> createRequirementRetrieveByIdResponseRequirementRetrieveByIdResult(RemoteRequirement value) {
        return new JAXBElement<RemoteRequirement>(_RequirementRetrieveByIdResponseRequirementRetrieveByIdResult_QNAME, RemoteRequirement.class, RequirementRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteCustomList", scope = CustomPropertyAddCustomList.class)
    public JAXBElement<RemoteCustomList> createCustomPropertyAddCustomListRemoteCustomList(RemoteCustomList value) {
        return new JAXBElement<RemoteCustomList>(_CustomPropertyAddCustomListRemoteCustomList_QNAME, RemoteCustomList.class, CustomPropertyAddCustomList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = TestRunRetrieve.class)
    public JAXBElement<RemoteSort> createTestRunRetrieveRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, TestRunRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestRunRetrieve.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestRunRetrieveRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestRunRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestStep", scope = TestCaseAddStep.class)
    public JAXBElement<RemoteTestStep> createTestCaseAddStepRemoteTestStep(RemoteTestStep value) {
        return new JAXBElement<RemoteTestStep>(_TestCaseAddStepRemoteTestStep_QNAME, RemoteTestStep.class, TestCaseAddStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveCommentsResult", scope = IncidentRetrieveCommentsResponse.class)
    public JAXBElement<ArrayOfRemoteComment> createIncidentRetrieveCommentsResponseIncidentRetrieveCommentsResult(ArrayOfRemoteComment value) {
        return new JAXBElement<ArrayOfRemoteComment>(_IncidentRetrieveCommentsResponseIncidentRetrieveCommentsResult_QNAME, ArrayOfRemoteComment.class, IncidentRetrieveCommentsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteIncidentPriority.class)
    public JAXBElement<String> createRemoteIncidentPriorityName2(String value) {
        return new JAXBElement<String>(_RemoteIncidentPriorityName2_QNAME, String.class, RemoteIncidentPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Color", scope = RemoteIncidentPriority.class)
    public JAXBElement<String> createRemoteIncidentPriorityColor(String value) {
        return new JAXBElement<String>(_RemoteIncidentPriorityColor_QNAME, String.class, RemoteIncidentPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "PriorityId", scope = RemoteIncidentPriority.class)
    public JAXBElement<Integer> createRemoteIncidentPriorityPriorityId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentPriorityPriorityId_QNAME, Integer.class, RemoteIncidentPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomationEngine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "AutomationEngine_RetrieveResult", scope = AutomationEngineRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteAutomationEngine> createAutomationEngineRetrieveResponseAutomationEngineRetrieveResult(ArrayOfRemoteAutomationEngine value) {
        return new JAXBElement<ArrayOfRemoteAutomationEngine>(_AutomationEngineRetrieveResponseAutomationEngineRetrieveResult_QNAME, ArrayOfRemoteAutomationEngine.class, AutomationEngineRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRelease }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteRelease", scope = ReleaseUpdate.class)
    public JAXBElement<RemoteRelease> createReleaseUpdateRemoteRelease(RemoteRelease value) {
        return new JAXBElement<RemoteRelease>(_ReleaseUpdateRemoteRelease_QNAME, RemoteRelease.class, ReleaseUpdate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteWorkflowIncidentFields }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveWorkflowFieldsResult", scope = IncidentRetrieveWorkflowFieldsResponse.class)
    public JAXBElement<ArrayOfRemoteWorkflowIncidentFields> createIncidentRetrieveWorkflowFieldsResponseIncidentRetrieveWorkflowFieldsResult(ArrayOfRemoteWorkflowIncidentFields value) {
        return new JAXBElement<ArrayOfRemoteWorkflowIncidentFields>(_IncidentRetrieveWorkflowFieldsResponseIncidentRetrieveWorkflowFieldsResult_QNAME, ArrayOfRemoteWorkflowIncidentFields.class, IncidentRetrieveWorkflowFieldsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ActualResult", scope = RemoteTestRunStep.class)
    public JAXBElement<String> createRemoteTestRunStepActualResult(String value) {
        return new JAXBElement<String>(_RemoteTestRunStepActualResult_QNAME, String.class, RemoteTestRunStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "SampleData", scope = RemoteTestRunStep.class)
    public JAXBElement<String> createRemoteTestRunStepSampleData(String value) {
        return new JAXBElement<String>(_RemoteTestRunStepSampleData_QNAME, String.class, RemoteTestRunStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestCaseId", scope = RemoteTestRunStep.class)
    public JAXBElement<Integer> createRemoteTestRunStepTestCaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunStepTestCaseId_QNAME, Integer.class, RemoteTestRunStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ExpectedResult", scope = RemoteTestRunStep.class)
    public JAXBElement<String> createRemoteTestRunStepExpectedResult(String value) {
        return new JAXBElement<String>(_RemoteTestRunStepExpectedResult_QNAME, String.class, RemoteTestRunStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestStepId", scope = RemoteTestRunStep.class)
    public JAXBElement<Integer> createRemoteTestRunStepTestStepId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunStepTestStepId_QNAME, Integer.class, RemoteTestRunStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteTestRunStep.class)
    public JAXBElement<String> createRemoteTestRunStepDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteTestRunStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestRunStepId", scope = RemoteTestRunStep.class)
    public JAXBElement<Integer> createRemoteTestRunStepTestRunStepId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunStepTestRunStepId_QNAME, Integer.class, RemoteTestRunStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "LinkedTestCaseId", scope = RemoteTestStep.class)
    public JAXBElement<Integer> createRemoteTestStepLinkedTestCaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestStepLinkedTestCaseId_QNAME, Integer.class, RemoteTestStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "SampleData", scope = RemoteTestStep.class)
    public JAXBElement<String> createRemoteTestStepSampleData(String value) {
        return new JAXBElement<String>(_RemoteTestRunStepSampleData_QNAME, String.class, RemoteTestStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ExecutionStatusId", scope = RemoteTestStep.class)
    public JAXBElement<Integer> createRemoteTestStepExecutionStatusId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestStepExecutionStatusId_QNAME, Integer.class, RemoteTestStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ExpectedResult", scope = RemoteTestStep.class)
    public JAXBElement<String> createRemoteTestStepExpectedResult(String value) {
        return new JAXBElement<String>(_RemoteTestRunStepExpectedResult_QNAME, String.class, RemoteTestStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestStepId", scope = RemoteTestStep.class)
    public JAXBElement<Integer> createRemoteTestStepTestStepId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunStepTestStepId_QNAME, Integer.class, RemoteTestStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteTestStep.class)
    public JAXBElement<String> createRemoteTestStepDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteTestStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentFolder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteDocumentFolder", scope = DocumentAddFolder.class)
    public JAXBElement<RemoteDocumentFolder> createDocumentAddFolderRemoteDocumentFolder(RemoteDocumentFolder value) {
        return new JAXBElement<RemoteDocumentFolder>(_DocumentUpdateFolderRemoteDocumentFolder_QNAME, RemoteDocumentFolder.class, DocumentAddFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Project_RetrieveResult", scope = ProjectRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteProject> createProjectRetrieveResponseProjectRetrieveResult(ArrayOfRemoteProject value) {
        return new JAXBElement<ArrayOfRemoteProject>(_ProjectRetrieveResponseProjectRetrieveResult_QNAME, ArrayOfRemoteProject.class, ProjectRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentStatusName_Output", scope = RemoteWorkflowIncidentTransition.class)
    public JAXBElement<String> createRemoteWorkflowIncidentTransitionIncidentStatusNameOutput(String value) {
        return new JAXBElement<String>(_RemoteWorkflowIncidentTransitionIncidentStatusNameOutput_QNAME, String.class, RemoteWorkflowIncidentTransition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentStatusName_Input", scope = RemoteWorkflowIncidentTransition.class)
    public JAXBElement<String> createRemoteWorkflowIncidentTransitionIncidentStatusNameInput(String value) {
        return new JAXBElement<String>(_RemoteWorkflowIncidentTransitionIncidentStatusNameInput_QNAME, String.class, RemoteWorkflowIncidentTransition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteWorkflowIncidentTransition.class)
    public JAXBElement<String> createRemoteWorkflowIncidentTransitionName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteWorkflowIncidentTransition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "AutomationHost_RetrieveByIdResult", scope = AutomationHostRetrieveByIdResponse.class)
    public JAXBElement<RemoteAutomationHost> createAutomationHostRetrieveByIdResponseAutomationHostRetrieveByIdResult(RemoteAutomationHost value) {
        return new JAXBElement<RemoteAutomationHost>(_AutomationHostRetrieveByIdResponseAutomationHostRetrieveByIdResult_QNAME, RemoteAutomationHost.class, AutomationHostRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteIncident", scope = IncidentUpdate.class)
    public JAXBElement<RemoteIncident> createIncidentUpdateRemoteIncident(RemoteIncident value) {
        return new JAXBElement<RemoteIncident>(_IncidentUpdateRemoteIncident_QNAME, RemoteIncident.class, IncidentUpdate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteSetting }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "System_GetSettingsResult", scope = SystemGetSettingsResponse.class)
    public JAXBElement<ArrayOfRemoteSetting> createSystemGetSettingsResponseSystemGetSettingsResult(ArrayOfRemoteSetting value) {
        return new JAXBElement<ArrayOfRemoteSetting>(_SystemGetSettingsResponseSystemGetSettingsResult_QNAME, ArrayOfRemoteSetting.class, SystemGetSettingsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "DataMapping_RetrieveFieldValueMappingsResult", scope = DataMappingRetrieveFieldValueMappingsResponse.class)
    public JAXBElement<ArrayOfRemoteDataMapping> createDataMappingRetrieveFieldValueMappingsResponseDataMappingRetrieveFieldValueMappingsResult(ArrayOfRemoteDataMapping value) {
        return new JAXBElement<ArrayOfRemoteDataMapping>(_DataMappingRetrieveFieldValueMappingsResponseDataMappingRetrieveFieldValueMappingsResult_QNAME, ArrayOfRemoteDataMapping.class, DataMappingRetrieveFieldValueMappingsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "binaryData", scope = DocumentAddFileVersion.class)
    public JAXBElement<byte[]> createDocumentAddFileVersionBinaryData(byte[] value) {
        return new JAXBElement<byte[]>(_DocumentAddFileVersionBinaryData_QNAME, byte[].class, DocumentAddFileVersion.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteDocumentVersion", scope = DocumentAddFileVersion.class)
    public JAXBElement<RemoteDocumentVersion> createDocumentAddFileVersionRemoteDocumentVersion(RemoteDocumentVersion value) {
        return new JAXBElement<RemoteDocumentVersion>(_DocumentAddFileVersionRemoteDocumentVersion_QNAME, RemoteDocumentVersion.class, DocumentAddFileVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "CustomProperty_RetrieveForArtifactTypeResult", scope = CustomPropertyRetrieveForArtifactTypeResponse.class)
    public JAXBElement<ArrayOfRemoteCustomProperty> createCustomPropertyRetrieveForArtifactTypeResponseCustomPropertyRetrieveForArtifactTypeResult(ArrayOfRemoteCustomProperty value) {
        return new JAXBElement<ArrayOfRemoteCustomProperty>(_CustomPropertyRetrieveForArtifactTypeResponseCustomPropertyRetrieveForArtifactTypeResult_QNAME, ArrayOfRemoteCustomProperty.class, CustomPropertyRetrieveForArtifactTypeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "description", scope = TestCaseAddUpdateAutomationScript.class)
    public JAXBElement<String> createTestCaseAddUpdateAutomationScriptDescription(String value) {
        return new JAXBElement<String>(_TestCaseAddUpdateAutomationScriptDescription_QNAME, String.class, TestCaseAddUpdateAutomationScript.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "urlOrFilename", scope = TestCaseAddUpdateAutomationScript.class)
    public JAXBElement<String> createTestCaseAddUpdateAutomationScriptUrlOrFilename(String value) {
        return new JAXBElement<String>(_TestCaseAddUpdateAutomationScriptUrlOrFilename_QNAME, String.class, TestCaseAddUpdateAutomationScript.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "projectAttachmentFolderId", scope = TestCaseAddUpdateAutomationScript.class)
    public JAXBElement<Integer> createTestCaseAddUpdateAutomationScriptProjectAttachmentFolderId(Integer value) {
        return new JAXBElement<Integer>(_TestCaseAddUpdateAutomationScriptProjectAttachmentFolderId_QNAME, Integer.class, TestCaseAddUpdateAutomationScript.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "automationEngineId", scope = TestCaseAddUpdateAutomationScript.class)
    public JAXBElement<Integer> createTestCaseAddUpdateAutomationScriptAutomationEngineId(Integer value) {
        return new JAXBElement<Integer>(_TestCaseAddUpdateAutomationScriptAutomationEngineId_QNAME, Integer.class, TestCaseAddUpdateAutomationScript.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "version", scope = TestCaseAddUpdateAutomationScript.class)
    public JAXBElement<String> createTestCaseAddUpdateAutomationScriptVersion(String value) {
        return new JAXBElement<String>(_TestCaseAddUpdateAutomationScriptVersion_QNAME, String.class, TestCaseAddUpdateAutomationScript.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "binaryData", scope = TestCaseAddUpdateAutomationScript.class)
    public JAXBElement<byte[]> createTestCaseAddUpdateAutomationScriptBinaryData(byte[] value) {
        return new JAXBElement<byte[]>(_DocumentAddFileVersionBinaryData_QNAME, byte[].class, TestCaseAddUpdateAutomationScript.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "projectAttachmentTypeId", scope = TestCaseAddUpdateAutomationScript.class)
    public JAXBElement<Integer> createTestCaseAddUpdateAutomationScriptProjectAttachmentTypeId(Integer value) {
        return new JAXBElement<Integer>(_TestCaseAddUpdateAutomationScriptProjectAttachmentTypeId_QNAME, Integer.class, TestCaseAddUpdateAutomationScript.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Token", scope = RemoteAutomationHost.class)
    public JAXBElement<String> createRemoteAutomationHostToken(String value) {
        return new JAXBElement<String>(_RemoteAutomationHostToken_QNAME, String.class, RemoteAutomationHost.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AutomationHostId", scope = RemoteAutomationHost.class)
    public JAXBElement<Integer> createRemoteAutomationHostAutomationHostId(Integer value) {
        return new JAXBElement<Integer>(_RemoteAutomationHostAutomationHostId_QNAME, Integer.class, RemoteAutomationHost.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteAutomationHost.class)
    public JAXBElement<String> createRemoteAutomationHostDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteAutomationHost.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteAutomationHost.class)
    public JAXBElement<String> createRemoteAutomationHostName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteAutomationHost.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Release_RetrieveCommentsResult", scope = ReleaseRetrieveCommentsResponse.class)
    public JAXBElement<ArrayOfRemoteComment> createReleaseRetrieveCommentsResponseReleaseRetrieveCommentsResult(ArrayOfRemoteComment value) {
        return new JAXBElement<ArrayOfRemoteComment>(_ReleaseRetrieveCommentsResponseReleaseRetrieveCommentsResult_QNAME, ArrayOfRemoteComment.class, ReleaseRetrieveCommentsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCaseParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_RetrieveParametersResult", scope = TestCaseRetrieveParametersResponse.class)
    public JAXBElement<ArrayOfRemoteTestCaseParameter> createTestCaseRetrieveParametersResponseTestCaseRetrieveParametersResult(ArrayOfRemoteTestCaseParameter value) {
        return new JAXBElement<ArrayOfRemoteTestCaseParameter>(_TestCaseRetrieveParametersResponseTestCaseRetrieveParametersResult_QNAME, ArrayOfRemoteTestCaseParameter.class, TestCaseRetrieveParametersResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArtifactLinkId", scope = RemoteAssociation.class)
    public JAXBElement<Integer> createRemoteAssociationArtifactLinkId(Integer value) {
        return new JAXBElement<Integer>(_RemoteAssociationArtifactLinkId_QNAME, Integer.class, RemoteAssociation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "DestArtifactTypeName", scope = RemoteAssociation.class)
    public JAXBElement<String> createRemoteAssociationDestArtifactTypeName(String value) {
        return new JAXBElement<String>(_RemoteAssociationDestArtifactTypeName_QNAME, String.class, RemoteAssociation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreationDate", scope = RemoteAssociation.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteAssociationCreationDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteAssociationCreationDate_QNAME, XMLGregorianCalendar.class, RemoteAssociation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Comment", scope = RemoteAssociation.class)
    public JAXBElement<String> createRemoteAssociationComment(String value) {
        return new JAXBElement<String>(_RemoteAssociationComment_QNAME, String.class, RemoteAssociation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "DestArtifactName", scope = RemoteAssociation.class)
    public JAXBElement<String> createRemoteAssociationDestArtifactName(String value) {
        return new JAXBElement<String>(_RemoteAssociationDestArtifactName_QNAME, String.class, RemoteAssociation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreatorName", scope = RemoteAssociation.class)
    public JAXBElement<String> createRemoteAssociationCreatorName(String value) {
        return new JAXBElement<String>(_RemoteReleaseCreatorName_QNAME, String.class, RemoteAssociation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreatorId", scope = RemoteAssociation.class)
    public JAXBElement<Integer> createRemoteAssociationCreatorId(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseCreatorId_QNAME, Integer.class, RemoteAssociation.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectId", scope = RemoteDataMapping.class)
    public JAXBElement<Integer> createRemoteDataMappingProjectId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDataMappingProjectId_QNAME, Integer.class, RemoteDataMapping.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ExternalKey", scope = RemoteDataMapping.class)
    public JAXBElement<String> createRemoteDataMappingExternalKey(String value) {
        return new JAXBElement<String>(_RemoteDataMappingExternalKey_QNAME, String.class, RemoteDataMapping.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTask", scope = TaskCreate.class)
    public JAXBElement<RemoteTask> createTaskCreateRemoteTask(RemoteTask value) {
        return new JAXBElement<RemoteTask>(_TaskCreateRemoteTask_QNAME, RemoteTask.class, TaskCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "CustomProperty_AddDefinitionResult", scope = CustomPropertyAddDefinitionResponse.class)
    public JAXBElement<RemoteCustomProperty> createCustomPropertyAddDefinitionResponseCustomPropertyAddDefinitionResult(RemoteCustomProperty value) {
        return new JAXBElement<RemoteCustomProperty>(_CustomPropertyAddDefinitionResponseCustomPropertyAddDefinitionResult_QNAME, RemoteCustomProperty.class, CustomPropertyAddDefinitionResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "destinationTestStepId", scope = TestCaseMoveStep.class)
    public JAXBElement<Integer> createTestCaseMoveStepDestinationTestStepId(Integer value) {
        return new JAXBElement<Integer>(_TestCaseMoveStepDestinationTestStepId_QNAME, Integer.class, TestCaseMoveStep.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectId", scope = RemoteArtifact.class)
    public JAXBElement<Integer> createRemoteArtifactProjectId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDataMappingProjectId_QNAME, Integer.class, RemoteArtifact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteArtifactCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CustomProperties", scope = RemoteArtifact.class)
    public JAXBElement<ArrayOfRemoteArtifactCustomProperty> createRemoteArtifactCustomProperties(ArrayOfRemoteArtifactCustomProperty value) {
        return new JAXBElement<ArrayOfRemoteArtifactCustomProperty>(_RemoteArtifactCustomProperties_QNAME, ArrayOfRemoteArtifactCustomProperty.class, RemoteArtifact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteBuild }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Build_CreateResult", scope = BuildCreateResponse.class)
    public JAXBElement<RemoteBuild> createBuildCreateResponseBuildCreateResult(RemoteBuild value) {
        return new JAXBElement<RemoteBuild>(_BuildCreateResponseBuildCreateResult_QNAME, RemoteBuild.class, BuildCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Value", scope = RemoteTestStepParameter.class)
    public JAXBElement<String> createRemoteTestStepParameterValue(String value) {
        return new JAXBElement<String>(_RemoteTestStepParameterValue_QNAME, String.class, RemoteTestStepParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteTestStepParameter.class)
    public JAXBElement<String> createRemoteTestStepParameterName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteTestStepParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectedEffort", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentProjectedEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentProjectedEffort_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreationDate", scope = RemoteIncident.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteIncidentCreationDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteAssociationCreationDate_QNAME, XMLGregorianCalendar.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "PriorityName", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentPriorityName(String value) {
        return new JAXBElement<String>(_RemoteIncidentPriorityName_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ClosedDate", scope = RemoteIncident.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteIncidentClosedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteIncidentClosedDate_QNAME, XMLGregorianCalendar.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemainingEffort", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentRemainingEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentRemainingEffort_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectName", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentProjectName(String value) {
        return new JAXBElement<String>(_RemoteIncidentProjectName_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "DetectedReleaseVersionNumber", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentDetectedReleaseVersionNumber(String value) {
        return new JAXBElement<String>(_RemoteIncidentDetectedReleaseVersionNumber_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ActualEffort", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentActualEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentActualEffort_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "SeverityId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentSeverityId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentSeverityId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "VerifiedReleaseVersionNumber", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentVerifiedReleaseVersionNumber(String value) {
        return new JAXBElement<String>(_RemoteIncidentVerifiedReleaseVersionNumber_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OpenerId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentOpenerId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentOpenerId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentIncidentId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentIncidentId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentTypeId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentIncidentTypeId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentIncidentTypeId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "PriorityId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentPriorityId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentPriorityPriorityId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OpenerName", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentOpenerName(String value) {
        return new JAXBElement<String>(_RemoteIncidentOpenerName_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentStatusId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentIncidentStatusId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentStatusIncidentStatusId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ResolvedReleaseVersionNumber", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentResolvedReleaseVersionNumber(String value) {
        return new JAXBElement<String>(_RemoteIncidentResolvedReleaseVersionNumber_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentOwnerId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetTestCaseMappingOwnerId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentStatusOpenStatus", scope = RemoteIncident.class)
    public JAXBElement<Boolean> createRemoteIncidentIncidentStatusOpenStatus(Boolean value) {
        return new JAXBElement<Boolean>(_RemoteIncidentIncidentStatusOpenStatus_QNAME, Boolean.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ResolvedReleaseId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentResolvedReleaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentResolvedReleaseId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestRunStepId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentTestRunStepId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunStepTestRunStepId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EstimatedEffort", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentEstimatedEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentEstimatedEffort_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerName", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentOwnerName(String value) {
        return new JAXBElement<String>(_RemoteIncidentOwnerName_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FixedBuildName", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentFixedBuildName(String value) {
        return new JAXBElement<String>(_RemoteIncidentFixedBuildName_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FixedBuildId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentFixedBuildId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentFixedBuildId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "SeverityName", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentSeverityName(String value) {
        return new JAXBElement<String>(_RemoteIncidentSeverityName_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "DetectedReleaseId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentDetectedReleaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentDetectedReleaseId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "VerifiedReleaseId", scope = RemoteIncident.class)
    public JAXBElement<Integer> createRemoteIncidentVerifiedReleaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentVerifiedReleaseId_QNAME, Integer.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "StartDate", scope = RemoteIncident.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteIncidentStartDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteIncidentStartDate_QNAME, XMLGregorianCalendar.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentTypeName", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentIncidentTypeName(String value) {
        return new JAXBElement<String>(_RemoteIncidentIncidentTypeName_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentStatusName", scope = RemoteIncident.class)
    public JAXBElement<String> createRemoteIncidentIncidentStatusName(String value) {
        return new JAXBElement<String>(_RemoteIncidentIncidentStatusName_QNAME, String.class, RemoteIncident.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteCustomProperty", scope = CustomPropertyUpdateDefinition.class)
    public JAXBElement<RemoteCustomProperty> createCustomPropertyUpdateDefinitionRemoteCustomProperty(RemoteCustomProperty value) {
        return new JAXBElement<RemoteCustomProperty>(_CustomPropertyUpdateDefinitionRemoteCustomProperty_QNAME, RemoteCustomProperty.class, CustomPropertyUpdateDefinition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteWorkflowIncidentTransition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveWorkflowTransitionsResult", scope = IncidentRetrieveWorkflowTransitionsResponse.class)
    public JAXBElement<ArrayOfRemoteWorkflowIncidentTransition> createIncidentRetrieveWorkflowTransitionsResponseIncidentRetrieveWorkflowTransitionsResult(ArrayOfRemoteWorkflowIncidentTransition value) {
        return new JAXBElement<ArrayOfRemoteWorkflowIncidentTransition>(_IncidentRetrieveWorkflowTransitionsResponseIncidentRetrieveWorkflowTransitionsResult_QNAME, ArrayOfRemoteWorkflowIncidentTransition.class, IncidentRetrieveWorkflowTransitionsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentSeverity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_AddSeverityResult", scope = IncidentAddSeverityResponse.class)
    public JAXBElement<RemoteIncidentSeverity> createIncidentAddSeverityResponseIncidentAddSeverityResult(RemoteIncidentSeverity value) {
        return new JAXBElement<RemoteIncidentSeverity>(_IncidentAddSeverityResponseIncidentAddSeverityResult_QNAME, RemoteIncidentSeverity.class, IncidentAddSeverityResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "password", scope = ConnectionAuthenticate.class)
    public JAXBElement<String> createConnectionAuthenticatePassword(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2Password_QNAME, String.class, ConnectionAuthenticate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "userName", scope = ConnectionAuthenticate.class)
    public JAXBElement<String> createConnectionAuthenticateUserName(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2UserName_QNAME, String.class, ConnectionAuthenticate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateRange }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "dateRange", scope = TestRunCreateForAutomationHost.class)
    public JAXBElement<DateRange> createTestRunCreateForAutomationHostDateRange(DateRange value) {
        return new JAXBElement<DateRange>(_TestRunCreateForAutomationHostDateRange_QNAME, DateRange.class, TestRunCreateForAutomationHost.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "automationHostToken", scope = TestRunCreateForAutomationHost.class)
    public JAXBElement<String> createTestRunCreateForAutomationHostAutomationHostToken(String value) {
        return new JAXBElement<String>(_TestRunCreateForAutomatedTestSetAutomationHostToken_QNAME, String.class, TestRunCreateForAutomationHost.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "parentTestSetFolderId", scope = TestSetCreate.class)
    public JAXBElement<Integer> createTestSetCreateParentTestSetFolderId(Integer value) {
        return new JAXBElement<Integer>(_TestSetCreateParentTestSetFolderId_QNAME, Integer.class, TestSetCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestSet", scope = TestSetCreate.class)
    public JAXBElement<RemoteTestSet> createTestSetCreateRemoteTestSet(RemoteTestSet value) {
        return new JAXBElement<RemoteTestSet>(_TestSetUpdateRemoteTestSet_QNAME, RemoteTestSet.class, TestSetCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteComments", scope = IncidentAddComments.class)
    public JAXBElement<ArrayOfRemoteComment> createIncidentAddCommentsRemoteComments(ArrayOfRemoteComment value) {
        return new JAXBElement<ArrayOfRemoteComment>(_IncidentAddCommentsRemoteComments_QNAME, ArrayOfRemoteComment.class, IncidentAddComments.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "projectRoleId", scope = UserCreate.class)
    public JAXBElement<Integer> createUserCreateProjectRoleId(Integer value) {
        return new JAXBElement<Integer>(_UserCreateProjectRoleId_QNAME, Integer.class, UserCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "passwordQuestion", scope = UserCreate.class)
    public JAXBElement<String> createUserCreatePasswordQuestion(String value) {
        return new JAXBElement<String>(_UserCreatePasswordQuestion_QNAME, String.class, UserCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteUser", scope = UserCreate.class)
    public JAXBElement<RemoteUser> createUserCreateRemoteUser(RemoteUser value) {
        return new JAXBElement<RemoteUser>(_UserCreateRemoteUser_QNAME, RemoteUser.class, UserCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "password", scope = UserCreate.class)
    public JAXBElement<String> createUserCreatePassword(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2Password_QNAME, String.class, UserCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "passwordAnswer", scope = UserCreate.class)
    public JAXBElement<String> createUserCreatePasswordAnswer(String value) {
        return new JAXBElement<String>(_UserCreatePasswordAnswer_QNAME, String.class, UserCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectRoleId", scope = RemoteProjectRole.class)
    public JAXBElement<Integer> createRemoteProjectRoleProjectRoleId(Integer value) {
        return new JAXBElement<Integer>(_RemoteProjectRoleProjectRoleId_QNAME, Integer.class, RemoteProjectRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteProjectRole.class)
    public JAXBElement<String> createRemoteProjectRoleDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteProjectRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteProjectRole.class)
    public JAXBElement<String> createRemoteProjectRoleName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteProjectRole.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "destinationTestSetId", scope = TestSetMove.class)
    public JAXBElement<Integer> createTestSetMoveDestinationTestSetId(Integer value) {
        return new JAXBElement<Integer>(_TestSetMoveDestinationTestSetId_QNAME, Integer.class, TestSetMove.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteManualTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_CreateFromTestCasesResult", scope = TestRunCreateFromTestCasesResponse.class)
    public JAXBElement<ArrayOfRemoteManualTestRun> createTestRunCreateFromTestCasesResponseTestRunCreateFromTestCasesResult(ArrayOfRemoteManualTestRun value) {
        return new JAXBElement<ArrayOfRemoteManualTestRun>(_TestRunCreateFromTestCasesResponseTestRunCreateFromTestCasesResult_QNAME, ArrayOfRemoteManualTestRun.class, TestRunCreateFromTestCasesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_RecordAutomated1Result", scope = TestRunRecordAutomated1Response.class)
    public JAXBElement<RemoteAutomatedTestRun> createTestRunRecordAutomated1ResponseTestRunRecordAutomated1Result(RemoteAutomatedTestRun value) {
        return new JAXBElement<RemoteAutomatedTestRun>(_TestRunRecordAutomated1ResponseTestRunRecordAutomated1Result_QNAME, RemoteAutomatedTestRun.class, TestRunRecordAutomated1Response.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteRequirementTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Requirement_RetrieveTestCoverageResult", scope = RequirementRetrieveTestCoverageResponse.class)
    public JAXBElement<ArrayOfRemoteRequirementTestCaseMapping> createRequirementRetrieveTestCoverageResponseRequirementRetrieveTestCoverageResult(ArrayOfRemoteRequirementTestCaseMapping value) {
        return new JAXBElement<ArrayOfRemoteRequirementTestCaseMapping>(_RequirementRetrieveTestCoverageResponseRequirementRetrieveTestCoverageResult_QNAME, ArrayOfRemoteRequirementTestCaseMapping.class, RequirementRetrieveTestCoverageResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCaseParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_AddParameterResult", scope = TestCaseAddParameterResponse.class)
    public JAXBElement<RemoteTestCaseParameter> createTestCaseAddParameterResponseTestCaseAddParameterResult(RemoteTestCaseParameter value) {
        return new JAXBElement<RemoteTestCaseParameter>(_TestCaseAddParameterResponseTestCaseAddParameterResult_QNAME, RemoteTestCaseParameter.class, TestCaseAddParameterResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteProjectUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Project_RetrieveUserMembershipResult", scope = ProjectRetrieveUserMembershipResponse.class)
    public JAXBElement<ArrayOfRemoteProjectUser> createProjectRetrieveUserMembershipResponseProjectRetrieveUserMembershipResult(ArrayOfRemoteProjectUser value) {
        return new JAXBElement<ArrayOfRemoteProjectUser>(_ProjectRetrieveUserMembershipResponseProjectRetrieveUserMembershipResult_QNAME, ArrayOfRemoteProjectUser.class, ProjectRetrieveUserMembershipResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteIncidentStatus", scope = IncidentAddStatus.class)
    public JAXBElement<RemoteIncidentStatus> createIncidentAddStatusRemoteIncidentStatus(RemoteIncidentStatus value) {
        return new JAXBElement<RemoteIncidentStatus>(_IncidentAddStatusRemoteIncidentStatus_QNAME, RemoteIncidentStatus.class, IncidentAddStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveByTestStepResult", scope = IncidentRetrieveByTestStepResponse.class)
    public JAXBElement<ArrayOfRemoteIncident> createIncidentRetrieveByTestStepResponseIncidentRetrieveByTestStepResult(ArrayOfRemoteIncident value) {
        return new JAXBElement<ArrayOfRemoteIncident>(_IncidentRetrieveByTestStepResponseIncidentRetrieveByTestStepResult_QNAME, ArrayOfRemoteIncident.class, IncidentRetrieveByTestStepResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestCaseCountForFolder.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestCaseCountForFolderRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestCaseCountForFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestCaseRetrieveByReleaseId.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestCaseRetrieveByReleaseIdRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestCaseRetrieveByReleaseId.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectId", scope = RemoteProject.class)
    public JAXBElement<Integer> createRemoteProjectProjectId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDataMappingProjectId_QNAME, Integer.class, RemoteProject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Website", scope = RemoteProject.class)
    public JAXBElement<String> createRemoteProjectWebsite(String value) {
        return new JAXBElement<String>(_RemoteProjectWebsite_QNAME, String.class, RemoteProject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteProject.class)
    public JAXBElement<String> createRemoteProjectDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteProject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteProject.class)
    public JAXBElement<String> createRemoteProjectName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteProject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "StartDate", scope = DateRange.class)
    public JAXBElement<XMLGregorianCalendar> createDateRangeStartDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteIncidentStartDate_QNAME, XMLGregorianCalendar.class, DateRange.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EndDate", scope = DateRange.class)
    public JAXBElement<XMLGregorianCalendar> createDateRangeEndDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateRangeEndDate_QNAME, XMLGregorianCalendar.class, DateRange.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = DocumentRetrieveForArtifact.class)
    public JAXBElement<RemoteSort> createDocumentRetrieveForArtifactRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, DocumentRetrieveForArtifact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = DocumentRetrieveForArtifact.class)
    public JAXBElement<ArrayOfRemoteFilter> createDocumentRetrieveForArtifactRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, DocumentRetrieveForArtifact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_RecordAutomated3Result", scope = TestRunRecordAutomated3Response.class)
    public JAXBElement<ArrayOfRemoteAutomatedTestRun> createTestRunRecordAutomated3ResponseTestRunRecordAutomated3Result(ArrayOfRemoteAutomatedTestRun value) {
        return new JAXBElement<ArrayOfRemoteAutomatedTestRun>(_TestRunRecordAutomated3ResponseTestRunRecordAutomated3Result_QNAME, ArrayOfRemoteAutomatedTestRun.class, TestRunRecordAutomated3Response.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "StringValue", scope = RemoteFilter.class)
    public JAXBElement<String> createRemoteFilterStringValue(String value) {
        return new JAXBElement<String>(_RemoteFilterStringValue_QNAME, String.class, RemoteFilter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "PropertyName", scope = RemoteFilter.class)
    public JAXBElement<String> createRemoteFilterPropertyName(String value) {
        return new JAXBElement<String>(_RemoteFilterPropertyName_QNAME, String.class, RemoteFilter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultiValueFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "MultiValue", scope = RemoteFilter.class)
    public JAXBElement<MultiValueFilter> createRemoteFilterMultiValue(MultiValueFilter value) {
        return new JAXBElement<MultiValueFilter>(_RemoteFilterMultiValue_QNAME, MultiValueFilter.class, RemoteFilter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IntValue", scope = RemoteFilter.class)
    public JAXBElement<Integer> createRemoteFilterIntValue(Integer value) {
        return new JAXBElement<Integer>(_RemoteFilterIntValue_QNAME, Integer.class, RemoteFilter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateRange }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "DateRangeValue", scope = RemoteFilter.class)
    public JAXBElement<DateRange> createRemoteFilterDateRangeValue(DateRange value) {
        return new JAXBElement<DateRange>(_RemoteFilterDateRangeValue_QNAME, DateRange.class, RemoteFilter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Requirement_RetrieveCommentsResult", scope = RequirementRetrieveCommentsResponse.class)
    public JAXBElement<ArrayOfRemoteComment> createRequirementRetrieveCommentsResponseRequirementRetrieveCommentsResult(ArrayOfRemoteComment value) {
        return new JAXBElement<ArrayOfRemoteComment>(_RequirementRetrieveCommentsResponseRequirementRetrieveCommentsResult_QNAME, ArrayOfRemoteComment.class, RequirementRetrieveCommentsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = ReleaseCount.class)
    public JAXBElement<ArrayOfRemoteFilter> createReleaseCountRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, ReleaseCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteDocumentVersion", scope = DocumentAddUrlVersion.class)
    public JAXBElement<RemoteDocumentVersion> createDocumentAddUrlVersionRemoteDocumentVersion(RemoteDocumentVersion value) {
        return new JAXBElement<RemoteDocumentVersion>(_DocumentAddFileVersionRemoteDocumentVersion_QNAME, RemoteDocumentVersion.class, DocumentAddUrlVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestRunStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestRunSteps", scope = RemoteManualTestRun.class)
    public JAXBElement<ArrayOfRemoteTestRunStep> createRemoteManualTestRunTestRunSteps(ArrayOfRemoteTestRunStep value) {
        return new JAXBElement<ArrayOfRemoteTestRunStep>(_RemoteManualTestRunTestRunSteps_QNAME, ArrayOfRemoteTestRunStep.class, RemoteManualTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteManualTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_RetrieveManualByIdResult", scope = TestRunRetrieveManualByIdResponse.class)
    public JAXBElement<RemoteManualTestRun> createTestRunRetrieveManualByIdResponseTestRunRetrieveManualByIdResult(RemoteManualTestRun value) {
        return new JAXBElement<RemoteManualTestRun>(_TestRunRetrieveManualByIdResponseTestRunRetrieveManualByIdResult_QNAME, RemoteManualTestRun.class, TestRunRetrieveManualByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "tabName", scope = SystemGetArtifactUrl.class)
    public JAXBElement<String> createSystemGetArtifactUrlTabName(String value) {
        return new JAXBElement<String>(_SystemGetArtifactUrlTabName_QNAME, String.class, SystemGetArtifactUrl.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = AssociationRetrieveForArtifact.class)
    public JAXBElement<RemoteSort> createAssociationRetrieveForArtifactRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, AssociationRetrieveForArtifact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = AssociationRetrieveForArtifact.class)
    public JAXBElement<ArrayOfRemoteFilter> createAssociationRetrieveForArtifactRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, AssociationRetrieveForArtifact.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteReleaseTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Release_RetrieveTestMappingResult", scope = ReleaseRetrieveTestMappingResponse.class)
    public JAXBElement<ArrayOfRemoteReleaseTestCaseMapping> createReleaseRetrieveTestMappingResponseReleaseRetrieveTestMappingResult(ArrayOfRemoteReleaseTestCaseMapping value) {
        return new JAXBElement<ArrayOfRemoteReleaseTestCaseMapping>(_ReleaseRetrieveTestMappingResponseReleaseRetrieveTestMappingResult_QNAME, ArrayOfRemoteReleaseTestCaseMapping.class, ReleaseRetrieveTestMappingResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "parameterName", scope = TestCaseCreateParameterToken.class)
    public JAXBElement<String> createTestCaseCreateParameterTokenParameterName(String value) {
        return new JAXBElement<String>(_TestCaseCreateParameterTokenParameterName_QNAME, String.class, TestCaseCreateParameterToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "existingTestSetTestCaseId", scope = TestSetAddTestMapping.class)
    public JAXBElement<Integer> createTestSetAddTestMappingExistingTestSetTestCaseId(Integer value) {
        return new JAXBElement<Integer>(_TestSetAddTestMappingExistingTestSetTestCaseId_QNAME, Integer.class, TestSetAddTestMapping.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSetTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestSetTestCaseMapping", scope = TestSetAddTestMapping.class)
    public JAXBElement<RemoteTestSetTestCaseMapping> createTestSetAddTestMappingRemoteTestSetTestCaseMapping(RemoteTestSetTestCaseMapping value) {
        return new JAXBElement<RemoteTestSetTestCaseMapping>(_TestSetAddTestMappingRemoteTestSetTestCaseMapping_QNAME, RemoteTestSetTestCaseMapping.class, TestSetAddTestMapping.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "parameters", scope = TestSetAddTestMapping.class)
    public JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> createTestSetAddTestMappingParameters(ArrayOfRemoteTestSetTestCaseParameter value) {
        return new JAXBElement<ArrayOfRemoteTestSetTestCaseParameter>(_TestSetAddTestMappingParameters_QNAME, ArrayOfRemoteTestSetTestCaseParameter.class, TestSetAddTestMapping.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentFolder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_AddFolderResult", scope = DocumentAddFolderResponse.class)
    public JAXBElement<RemoteDocumentFolder> createDocumentAddFolderResponseDocumentAddFolderResult(RemoteDocumentFolder value) {
        return new JAXBElement<RemoteDocumentFolder>(_DocumentAddFolderResponseDocumentAddFolderResult_QNAME, RemoteDocumentFolder.class, DocumentAddFolderResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestSet_RetrieveCommentsResult", scope = TestSetRetrieveCommentsResponse.class)
    public JAXBElement<ArrayOfRemoteComment> createTestSetRetrieveCommentsResponseTestSetRetrieveCommentsResult(ArrayOfRemoteComment value) {
        return new JAXBElement<ArrayOfRemoteComment>(_TestSetRetrieveCommentsResponseTestSetRetrieveCommentsResult_QNAME, ArrayOfRemoteComment.class, TestSetRetrieveCommentsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentFolder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_RetrieveFolderByIdResult", scope = DocumentRetrieveFolderByIdResponse.class)
    public JAXBElement<RemoteDocumentFolder> createDocumentRetrieveFolderByIdResponseDocumentRetrieveFolderByIdResult(RemoteDocumentFolder value) {
        return new JAXBElement<RemoteDocumentFolder>(_DocumentRetrieveFolderByIdResponseDocumentRetrieveFolderByIdResult_QNAME, RemoteDocumentFolder.class, DocumentRetrieveFolderByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "existingProjectId", scope = ProjectCreate.class)
    public JAXBElement<Integer> createProjectCreateExistingProjectId(Integer value) {
        return new JAXBElement<Integer>(_ProjectCreateExistingProjectId_QNAME, Integer.class, ProjectCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteProject", scope = ProjectCreate.class)
    public JAXBElement<RemoteProject> createProjectCreateRemoteProject(RemoteProject value) {
        return new JAXBElement<RemoteProject>(_ProjectCreateRemoteProject_QNAME, RemoteProject.class, ProjectCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "CustomProperty_RetrieveCustomListsResult", scope = CustomPropertyRetrieveCustomListsResponse.class)
    public JAXBElement<ArrayOfRemoteCustomList> createCustomPropertyRetrieveCustomListsResponseCustomPropertyRetrieveCustomListsResult(ArrayOfRemoteCustomList value) {
        return new JAXBElement<ArrayOfRemoteCustomList>(_CustomPropertyRetrieveCustomListsResponseCustomPropertyRetrieveCustomListsResult_QNAME, ArrayOfRemoteCustomList.class, CustomPropertyRetrieveCustomListsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = TestRunRetrieveManual.class)
    public JAXBElement<RemoteSort> createTestRunRetrieveManualRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, TestRunRetrieveManual.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestRunRetrieveManual.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestRunRetrieveManualRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestRunRetrieveManual.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_RetrieveAutomatedByIdResult", scope = TestRunRetrieveAutomatedByIdResponse.class)
    public JAXBElement<RemoteAutomatedTestRun> createTestRunRetrieveAutomatedByIdResponseTestRunRetrieveAutomatedByIdResult(RemoteAutomatedTestRun value) {
        return new JAXBElement<RemoteAutomatedTestRun>(_TestRunRetrieveAutomatedByIdResponseTestRunRetrieveAutomatedByIdResult_QNAME, RemoteAutomatedTestRun.class, TestRunRetrieveAutomatedByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Values", scope = MultiValueFilter.class)
    public JAXBElement<ArrayOfint> createMultiValueFilterValues(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_MultiValueFilterValues_QNAME, ArrayOfint.class, MultiValueFilter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Value", scope = RemoteSetting.class)
    public JAXBElement<String> createRemoteSettingValue(String value) {
        return new JAXBElement<String>(_RemoteTestStepParameterValue_QNAME, String.class, RemoteSetting.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteSetting.class)
    public JAXBElement<String> createRemoteSettingName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteSetting.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestSet_CreateFolderResult", scope = TestSetCreateFolderResponse.class)
    public JAXBElement<RemoteTestSet> createTestSetCreateFolderResponseTestSetCreateFolderResult(RemoteTestSet value) {
        return new JAXBElement<RemoteTestSet>(_TestSetCreateFolderResponseTestSetCreateFolderResult_QNAME, RemoteTestSet.class, TestSetCreateFolderResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDocumentFolder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_RetrieveFoldersResult", scope = DocumentRetrieveFoldersResponse.class)
    public JAXBElement<ArrayOfRemoteDocumentFolder> createDocumentRetrieveFoldersResponseDocumentRetrieveFoldersResult(ArrayOfRemoteDocumentFolder value) {
        return new JAXBElement<ArrayOfRemoteDocumentFolder>(_DocumentRetrieveFoldersResponseDocumentRetrieveFoldersResult_QNAME, ArrayOfRemoteDocumentFolder.class, DocumentRetrieveFoldersResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AutomationAttachmentId", scope = RemoteTestCase.class)
    public JAXBElement<Integer> createRemoteTestCaseAutomationAttachmentId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestCaseAutomationAttachmentId_QNAME, Integer.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EstimatedDuration", scope = RemoteTestCase.class)
    public JAXBElement<Integer> createRemoteTestCaseEstimatedDuration(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestCaseEstimatedDuration_QNAME, Integer.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestSteps", scope = RemoteTestCase.class)
    public JAXBElement<ArrayOfRemoteTestStep> createRemoteTestCaseTestSteps(ArrayOfRemoteTestStep value) {
        return new JAXBElement<ArrayOfRemoteTestStep>(_RemoteTestCaseTestSteps_QNAME, ArrayOfRemoteTestStep.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestCasePriorityName", scope = RemoteTestCase.class)
    public JAXBElement<String> createRemoteTestCaseTestCasePriorityName(String value) {
        return new JAXBElement<String>(_RemoteTestCaseTestCasePriorityName_QNAME, String.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AuthorId", scope = RemoteTestCase.class)
    public JAXBElement<Integer> createRemoteTestCaseAuthorId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentVersionAuthorId_QNAME, Integer.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestCaseId", scope = RemoteTestCase.class)
    public JAXBElement<Integer> createRemoteTestCaseTestCaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunStepTestCaseId_QNAME, Integer.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerId", scope = RemoteTestCase.class)
    public JAXBElement<Integer> createRemoteTestCaseOwnerId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetTestCaseMappingOwnerId_QNAME, Integer.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ExecutionDate", scope = RemoteTestCase.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteTestCaseExecutionDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteTestCaseExecutionDate_QNAME, XMLGregorianCalendar.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteTestCase.class)
    public JAXBElement<String> createRemoteTestCaseDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectName", scope = RemoteTestCase.class)
    public JAXBElement<String> createRemoteTestCaseProjectName(String value) {
        return new JAXBElement<String>(_RemoteIncidentProjectName_QNAME, String.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerName", scope = RemoteTestCase.class)
    public JAXBElement<String> createRemoteTestCaseOwnerName(String value) {
        return new JAXBElement<String>(_RemoteIncidentOwnerName_QNAME, String.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ExecutionStatusId", scope = RemoteTestCase.class)
    public JAXBElement<Integer> createRemoteTestCaseExecutionStatusId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestStepExecutionStatusId_QNAME, Integer.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestCasePriorityId", scope = RemoteTestCase.class)
    public JAXBElement<Integer> createRemoteTestCaseTestCasePriorityId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestCaseTestCasePriorityId_QNAME, Integer.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IndentLevel", scope = RemoteTestCase.class)
    public JAXBElement<String> createRemoteTestCaseIndentLevel(String value) {
        return new JAXBElement<String>(_RemoteReleaseIndentLevel_QNAME, String.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AuthorName", scope = RemoteTestCase.class)
    public JAXBElement<String> createRemoteTestCaseAuthorName(String value) {
        return new JAXBElement<String>(_RemoteDocumentVersionAuthorName_QNAME, String.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AutomationEngineId", scope = RemoteTestCase.class)
    public JAXBElement<Integer> createRemoteTestCaseAutomationEngineId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestCaseAutomationEngineId_QNAME, Integer.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteTestCase.class)
    public JAXBElement<String> createRemoteTestCaseName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteTestCase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Value", scope = RemoteCustomPropertyOption.class)
    public JAXBElement<String> createRemoteCustomPropertyOptionValue(String value) {
        return new JAXBElement<String>(_RemoteTestStepParameterValue_QNAME, String.class, RemoteCustomPropertyOption.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Association_CreateResult", scope = AssociationCreateResponse.class)
    public JAXBElement<RemoteAssociation> createAssociationCreateResponseAssociationCreateResult(RemoteAssociation value) {
        return new JAXBElement<RemoteAssociation>(_AssociationCreateResponseAssociationCreateResult_QNAME, RemoteAssociation.class, AssociationCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirementTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteReqTestCaseMapping", scope = RequirementRemoveTestCoverage.class)
    public JAXBElement<RemoteRequirementTestCaseMapping> createRequirementRemoveTestCoverageRemoteReqTestCaseMapping(RemoteRequirementTestCaseMapping value) {
        return new JAXBElement<RemoteRequirementTestCaseMapping>(_RequirementAddTestCoverageRemoteReqTestCaseMapping_QNAME, RemoteRequirementTestCaseMapping.class, RequirementRemoveTestCoverage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectedEffort", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskProjectedEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentProjectedEffort_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskPriorityId", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskTaskPriorityId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTaskTaskPriorityId_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskId", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskTaskId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTaskTaskId_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerId", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskOwnerId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetTestCaseMappingOwnerId_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteTask.class)
    public JAXBElement<String> createRemoteTaskDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RemainingEffort", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskRemainingEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentRemainingEffort_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectName", scope = RemoteTask.class)
    public JAXBElement<String> createRemoteTaskProjectName(String value) {
        return new JAXBElement<String>(_RemoteIncidentProjectName_QNAME, String.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ActualEffort", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskActualEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentActualEffort_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EstimatedEffort", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskEstimatedEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentEstimatedEffort_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerName", scope = RemoteTask.class)
    public JAXBElement<String> createRemoteTaskOwnerName(String value) {
        return new JAXBElement<String>(_RemoteIncidentOwnerName_QNAME, String.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RequirementId", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskRequirementId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTaskRequirementId_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ReleaseId", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskReleaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseReleaseId_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RequirementName", scope = RemoteTask.class)
    public JAXBElement<String> createRemoteTaskRequirementName(String value) {
        return new JAXBElement<String>(_RemoteTaskRequirementName_QNAME, String.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ReleaseVersionNumber", scope = RemoteTask.class)
    public JAXBElement<String> createRemoteTaskReleaseVersionNumber(String value) {
        return new JAXBElement<String>(_RemoteTaskReleaseVersionNumber_QNAME, String.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "StartDate", scope = RemoteTask.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteTaskStartDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteIncidentStartDate_QNAME, XMLGregorianCalendar.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreatorId", scope = RemoteTask.class)
    public JAXBElement<Integer> createRemoteTaskCreatorId(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseCreatorId_QNAME, Integer.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskStatusName", scope = RemoteTask.class)
    public JAXBElement<String> createRemoteTaskTaskStatusName(String value) {
        return new JAXBElement<String>(_RemoteTaskTaskStatusName_QNAME, String.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EndDate", scope = RemoteTask.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteTaskEndDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateRangeEndDate_QNAME, XMLGregorianCalendar.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteTask.class)
    public JAXBElement<String> createRemoteTaskName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskPriorityName", scope = RemoteTask.class)
    public JAXBElement<String> createRemoteTaskTaskPriorityName(String value) {
        return new JAXBElement<String>(_RemoteTaskTaskPriorityName_QNAME, String.class, RemoteTask.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteComment", scope = TestSetCreateComment.class)
    public JAXBElement<RemoteComment> createTestSetCreateCommentRemoteComment(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_RequirementCreateCommentRemoteComment_QNAME, RemoteComment.class, TestSetCreateComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentSeverity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteIncidentSeverity", scope = IncidentAddSeverity.class)
    public JAXBElement<RemoteIncidentSeverity> createIncidentAddSeverityRemoteIncidentSeverity(RemoteIncidentSeverity value) {
        return new JAXBElement<RemoteIncidentSeverity>(_IncidentAddSeverityRemoteIncidentSeverity_QNAME, RemoteIncidentSeverity.class, IncidentAddSeverity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteReleaseTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteReleaseTestCaseMapping", scope = ReleaseAddTestMapping.class)
    public JAXBElement<RemoteReleaseTestCaseMapping> createReleaseAddTestMappingRemoteReleaseTestCaseMapping(RemoteReleaseTestCaseMapping value) {
        return new JAXBElement<RemoteReleaseTestCaseMapping>(_ReleaseAddTestMappingRemoteReleaseTestCaseMapping_QNAME, RemoteReleaseTestCaseMapping.class, ReleaseAddTestMapping.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteBuild }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Build_RetrieveByReleaseIdResult", scope = BuildRetrieveByReleaseIdResponse.class)
    public JAXBElement<ArrayOfRemoteBuild> createBuildRetrieveByReleaseIdResponseBuildRetrieveByReleaseIdResult(ArrayOfRemoteBuild value) {
        return new JAXBElement<ArrayOfRemoteBuild>(_BuildRetrieveByReleaseIdResponseBuildRetrieveByReleaseIdResult_QNAME, ArrayOfRemoteBuild.class, BuildRetrieveByReleaseIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "DefaultValue", scope = RemoteTestCaseParameter.class)
    public JAXBElement<String> createRemoteTestCaseParameterDefaultValue(String value) {
        return new JAXBElement<String>(_RemoteTestCaseParameterDefaultValue_QNAME, String.class, RemoteTestCaseParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestCaseParameterId", scope = RemoteTestCaseParameter.class)
    public JAXBElement<Integer> createRemoteTestCaseParameterTestCaseParameterId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestCaseParameterTestCaseParameterId_QNAME, Integer.class, RemoteTestCaseParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteTestCaseParameter.class)
    public JAXBElement<String> createRemoteTestCaseParameterName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteTestCaseParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCaseParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestCaseParameter", scope = TestCaseAddParameter.class)
    public JAXBElement<RemoteTestCaseParameter> createTestCaseAddParameterRemoteTestCaseParameter(RemoteTestCaseParameter value) {
        return new JAXBElement<RemoteTestCaseParameter>(_TestCaseAddParameterRemoteTestCaseParameter_QNAME, RemoteTestCaseParameter.class, TestCaseAddParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestCase", scope = TestCaseUpdate.class)
    public JAXBElement<RemoteTestCase> createTestCaseUpdateRemoteTestCase(RemoteTestCase value) {
        return new JAXBElement<RemoteTestCase>(_TestCaseCreateFolderRemoteTestCase_QNAME, RemoteTestCase.class, TestCaseUpdate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_RetrieveForFolderResult", scope = DocumentRetrieveForFolderResponse.class)
    public JAXBElement<ArrayOfRemoteDocument> createDocumentRetrieveForFolderResponseDocumentRetrieveForFolderResult(ArrayOfRemoteDocument value) {
        return new JAXBElement<ArrayOfRemoteDocument>(_DocumentRetrieveForFolderResponseDocumentRetrieveForFolderResult_QNAME, ArrayOfRemoteDocument.class, DocumentRetrieveForFolderResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveByIdResult", scope = IncidentRetrieveByIdResponse.class)
    public JAXBElement<RemoteIncident> createIncidentRetrieveByIdResponseIncidentRetrieveByIdResult(RemoteIncident value) {
        return new JAXBElement<RemoteIncident>(_IncidentRetrieveByIdResponseIncidentRetrieveByIdResult_QNAME, RemoteIncident.class, IncidentRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FieldCaption", scope = RemoteWorkflowIncidentFields.class)
    public JAXBElement<String> createRemoteWorkflowIncidentFieldsFieldCaption(String value) {
        return new JAXBElement<String>(_RemoteWorkflowIncidentFieldsFieldCaption_QNAME, String.class, RemoteWorkflowIncidentFields.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FieldName", scope = RemoteWorkflowIncidentFields.class)
    public JAXBElement<String> createRemoteWorkflowIncidentFieldsFieldName(String value) {
        return new JAXBElement<String>(_RemoteWorkflowIncidentFieldsFieldName_QNAME, String.class, RemoteWorkflowIncidentFields.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ParentProjectAttachmentFolderId", scope = RemoteDocumentFolder.class)
    public JAXBElement<Integer> createRemoteDocumentFolderParentProjectAttachmentFolderId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentFolderParentProjectAttachmentFolderId_QNAME, Integer.class, RemoteDocumentFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectAttachmentFolderId", scope = RemoteDocumentFolder.class)
    public JAXBElement<Integer> createRemoteDocumentFolderProjectAttachmentFolderId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentFolderProjectAttachmentFolderId_QNAME, Integer.class, RemoteDocumentFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IndentLevel", scope = RemoteDocumentFolder.class)
    public JAXBElement<String> createRemoteDocumentFolderIndentLevel(String value) {
        return new JAXBElement<String>(_RemoteReleaseIndentLevel_QNAME, String.class, RemoteDocumentFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteDocumentFolder.class)
    public JAXBElement<String> createRemoteDocumentFolderName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteDocumentFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = IncidentRetrieve.class)
    public JAXBElement<RemoteSort> createIncidentRetrieveRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, IncidentRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = IncidentRetrieve.class)
    public JAXBElement<ArrayOfRemoteFilter> createIncidentRetrieveRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, IncidentRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_AddFileResult", scope = DocumentAddFileResponse.class)
    public JAXBElement<RemoteDocument> createDocumentAddFileResponseDocumentAddFileResult(RemoteDocument value) {
        return new JAXBElement<RemoteDocument>(_DocumentAddFileResponseDocumentAddFileResult_QNAME, RemoteDocument.class, DocumentAddFileResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "PlannedEffort", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementPlannedEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleasePlannedEffort_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskActualEffort", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementTaskActualEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseTaskActualEffort_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AuthorId", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementAuthorId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentVersionAuthorId_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CoverageCountBlocked", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementCoverageCountBlocked(Integer value) {
        return new JAXBElement<Integer>(_RemoteRequirementCoverageCountBlocked_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerId", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementOwnerId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetTestCaseMappingOwnerId_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteRequirement.class)
    public JAXBElement<String> createRemoteRequirementDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectName", scope = RemoteRequirement.class)
    public JAXBElement<String> createRemoteRequirementProjectName(String value) {
        return new JAXBElement<String>(_RemoteIncidentProjectName_QNAME, String.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerName", scope = RemoteRequirement.class)
    public JAXBElement<String> createRemoteRequirementOwnerName(String value) {
        return new JAXBElement<String>(_RemoteIncidentOwnerName_QNAME, String.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RequirementId", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementRequirementId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTaskRequirementId_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CoverageCountCaution", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementCoverageCountCaution(Integer value) {
        return new JAXBElement<Integer>(_RemoteRequirementCoverageCountCaution_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CoverageCountFailed", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementCoverageCountFailed(Integer value) {
        return new JAXBElement<Integer>(_RemoteRequirementCoverageCountFailed_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskCount", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementTaskCount(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseTaskCount_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CoverageCountPassed", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementCoverageCountPassed(Integer value) {
        return new JAXBElement<Integer>(_RemoteRequirementCoverageCountPassed_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CoverageCountTotal", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementCoverageCountTotal(Integer value) {
        return new JAXBElement<Integer>(_RemoteRequirementCoverageCountTotal_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ReleaseId", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementReleaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseReleaseId_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "StatusName", scope = RemoteRequirement.class)
    public JAXBElement<String> createRemoteRequirementStatusName(String value) {
        return new JAXBElement<String>(_RemoteRequirementStatusName_QNAME, String.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IndentLevel", scope = RemoteRequirement.class)
    public JAXBElement<String> createRemoteRequirementIndentLevel(String value) {
        return new JAXBElement<String>(_RemoteReleaseIndentLevel_QNAME, String.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ReleaseVersionNumber", scope = RemoteRequirement.class)
    public JAXBElement<String> createRemoteRequirementReleaseVersionNumber(String value) {
        return new JAXBElement<String>(_RemoteTaskReleaseVersionNumber_QNAME, String.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AuthorName", scope = RemoteRequirement.class)
    public JAXBElement<String> createRemoteRequirementAuthorName(String value) {
        return new JAXBElement<String>(_RemoteDocumentVersionAuthorName_QNAME, String.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ImportanceId", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementImportanceId(Integer value) {
        return new JAXBElement<Integer>(_RemoteRequirementImportanceId_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "StatusId", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementStatusId(Integer value) {
        return new JAXBElement<Integer>(_RemoteRequirementStatusId_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TaskEstimatedEffort", scope = RemoteRequirement.class)
    public JAXBElement<Integer> createRemoteRequirementTaskEstimatedEffort(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseTaskEstimatedEffort_QNAME, Integer.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteRequirement.class)
    public JAXBElement<String> createRemoteRequirementName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ImportanceName", scope = RemoteRequirement.class)
    public JAXBElement<String> createRemoteRequirementImportanceName(String value) {
        return new JAXBElement<String>(_RemoteRequirementImportanceName_QNAME, String.class, RemoteRequirement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "System_GetProductVersionResult", scope = SystemGetProductVersionResponse.class)
    public JAXBElement<RemoteVersion> createSystemGetProductVersionResponseSystemGetProductVersionResult(RemoteVersion value) {
        return new JAXBElement<RemoteVersion>(_SystemGetProductVersionResponseSystemGetProductVersionResult_QNAME, RemoteVersion.class, SystemGetProductVersionResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteManualTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_CreateFromTestSetResult", scope = TestRunCreateFromTestSetResponse.class)
    public JAXBElement<ArrayOfRemoteManualTestRun> createTestRunCreateFromTestSetResponseTestRunCreateFromTestSetResult(ArrayOfRemoteManualTestRun value) {
        return new JAXBElement<ArrayOfRemoteManualTestRun>(_TestRunCreateFromTestSetResponseTestRunCreateFromTestSetResult_QNAME, ArrayOfRemoteManualTestRun.class, TestRunCreateFromTestSetResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestSet_RetrieveForOwnerResult", scope = TestSetRetrieveForOwnerResponse.class)
    public JAXBElement<ArrayOfRemoteTestSet> createTestSetRetrieveForOwnerResponseTestSetRetrieveForOwnerResult(ArrayOfRemoteTestSet value) {
        return new JAXBElement<ArrayOfRemoteTestSet>(_TestSetRetrieveForOwnerResponseTestSetRetrieveForOwnerResult_QNAME, ArrayOfRemoteTestSet.class, TestSetRetrieveForOwnerResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "token", scope = AutomationEngineRetrieveByToken.class)
    public JAXBElement<String> createAutomationEngineRetrieveByTokenToken(String value) {
        return new JAXBElement<String>(_AutomationHostRetrieveByTokenToken_QNAME, String.class, AutomationEngineRetrieveByToken.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = IncidentCount.class)
    public JAXBElement<ArrayOfRemoteFilter> createIncidentCountRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, IncidentCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteAssociation", scope = AssociationUpdate.class)
    public JAXBElement<RemoteAssociation> createAssociationUpdateRemoteAssociation(RemoteAssociation value) {
        return new JAXBElement<RemoteAssociation>(_AssociationUpdateRemoteAssociation_QNAME, RemoteAssociation.class, AssociationUpdate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteReleaseTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteReleaseTestCaseMapping", scope = ReleaseRemoveTestMapping.class)
    public JAXBElement<RemoteReleaseTestCaseMapping> createReleaseRemoveTestMappingRemoteReleaseTestCaseMapping(RemoteReleaseTestCaseMapping value) {
        return new JAXBElement<RemoteReleaseTestCaseMapping>(_ReleaseAddTestMappingRemoteReleaseTestCaseMapping_QNAME, RemoteReleaseTestCaseMapping.class, ReleaseRemoveTestMapping.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestRuns", scope = TestRunRecordAutomated3 .class)
    public JAXBElement<ArrayOfRemoteAutomatedTestRun> createTestRunRecordAutomated3RemoteTestRuns(ArrayOfRemoteAutomatedTestRun value) {
        return new JAXBElement<ArrayOfRemoteAutomatedTestRun>(_TestRunSaveRemoteTestRuns_QNAME, ArrayOfRemoteAutomatedTestRun.class, TestRunRecordAutomated3 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAssociation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteAssociation", scope = AssociationCreate.class)
    public JAXBElement<RemoteAssociation> createAssociationCreateRemoteAssociation(RemoteAssociation value) {
        return new JAXBElement<RemoteAssociation>(_AssociationUpdateRemoteAssociation_QNAME, RemoteAssociation.class, AssociationCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_RetrieveByReleaseIdResult", scope = TestCaseRetrieveByReleaseIdResponse.class)
    public JAXBElement<ArrayOfRemoteTestCase> createTestCaseRetrieveByReleaseIdResponseTestCaseRetrieveByReleaseIdResult(ArrayOfRemoteTestCase value) {
        return new JAXBElement<ArrayOfRemoteTestCase>(_TestCaseRetrieveByReleaseIdResponseTestCaseRetrieveByReleaseIdResult_QNAME, ArrayOfRemoteTestCase.class, TestCaseRetrieveByReleaseIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectAttachmentTypeId", scope = RemoteDocumentType.class)
    public JAXBElement<Integer> createRemoteDocumentTypeProjectAttachmentTypeId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentTypeProjectAttachmentTypeId_QNAME, Integer.class, RemoteDocumentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteDocumentType.class)
    public JAXBElement<String> createRemoteDocumentTypeDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteDocumentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteDocumentType.class)
    public JAXBElement<String> createRemoteDocumentTypeName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteDocumentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = BuildRetrieveByReleaseId.class)
    public JAXBElement<RemoteSort> createBuildRetrieveByReleaseIdRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, BuildRetrieveByReleaseId.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = BuildRetrieveByReleaseId.class)
    public JAXBElement<ArrayOfRemoteFilter> createBuildRetrieveByReleaseIdRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, BuildRetrieveByReleaseId.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_RetrieveForOwnerResult", scope = TestCaseRetrieveForOwnerResponse.class)
    public JAXBElement<ArrayOfRemoteTestCase> createTestCaseRetrieveForOwnerResponseTestCaseRetrieveForOwnerResult(ArrayOfRemoteTestCase value) {
        return new JAXBElement<ArrayOfRemoteTestCase>(_TestCaseRetrieveForOwnerResponseTestCaseRetrieveForOwnerResult_QNAME, ArrayOfRemoteTestCase.class, TestCaseRetrieveForOwnerResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CommentId", scope = RemoteComment.class)
    public JAXBElement<Integer> createRemoteCommentCommentId(Integer value) {
        return new JAXBElement<Integer>(_RemoteCommentCommentId_QNAME, Integer.class, RemoteComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "UserName", scope = RemoteComment.class)
    public JAXBElement<String> createRemoteCommentUserName(String value) {
        return new JAXBElement<String>(_RemoteUserUserName_QNAME, String.class, RemoteComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreationDate", scope = RemoteComment.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteCommentCreationDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteAssociationCreationDate_QNAME, XMLGregorianCalendar.class, RemoteComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "UserId", scope = RemoteComment.class)
    public JAXBElement<Integer> createRemoteCommentUserId(Integer value) {
        return new JAXBElement<Integer>(_RemoteUserUserId_QNAME, Integer.class, RemoteComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Text", scope = RemoteComment.class)
    public JAXBElement<String> createRemoteCommentText(String value) {
        return new JAXBElement<String>(_RemoteCommentText_QNAME, String.class, RemoteComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "destinationReleaseId", scope = ReleaseMove.class)
    public JAXBElement<Integer> createReleaseMoveDestinationReleaseId(Integer value) {
        return new JAXBElement<Integer>(_ReleaseMoveDestinationReleaseId_QNAME, Integer.class, ReleaseMove.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestSet_RetrieveTestCaseMappingResult", scope = TestSetRetrieveTestCaseMappingResponse.class)
    public JAXBElement<ArrayOfRemoteTestSetTestCaseMapping> createTestSetRetrieveTestCaseMappingResponseTestSetRetrieveTestCaseMappingResult(ArrayOfRemoteTestSetTestCaseMapping value) {
        return new JAXBElement<ArrayOfRemoteTestSetTestCaseMapping>(_TestSetRetrieveTestCaseMappingResponseTestSetRetrieveTestCaseMappingResult_QNAME, ArrayOfRemoteTestSetTestCaseMapping.class, TestSetRetrieveTestCaseMappingResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTask", scope = TaskUpdate.class)
    public JAXBElement<RemoteTask> createTaskUpdateRemoteTask(RemoteTask value) {
        return new JAXBElement<RemoteTask>(_TaskCreateRemoteTask_QNAME, RemoteTask.class, TaskUpdate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "StackTrace", scope = ServiceFaultMessage.class)
    public JAXBElement<String> createServiceFaultMessageStackTrace(String value) {
        return new JAXBElement<String>(_ServiceFaultMessageStackTrace_QNAME, String.class, ServiceFaultMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "Type", scope = ServiceFaultMessage.class)
    public JAXBElement<String> createServiceFaultMessageType(String value) {
        return new JAXBElement<String>(_ServiceFaultMessageType_QNAME, String.class, ServiceFaultMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "Message", scope = ServiceFaultMessage.class)
    public JAXBElement<String> createServiceFaultMessageMessage(String value) {
        return new JAXBElement<String>(_ServiceFaultMessageMessage_QNAME, String.class, ServiceFaultMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveByTestCaseResult", scope = IncidentRetrieveByTestCaseResponse.class)
    public JAXBElement<ArrayOfRemoteIncident> createIncidentRetrieveByTestCaseResponseIncidentRetrieveByTestCaseResult(ArrayOfRemoteIncident value) {
        return new JAXBElement<ArrayOfRemoteIncident>(_IncidentRetrieveByTestCaseResponseIncidentRetrieveByTestCaseResult_QNAME, ArrayOfRemoteIncident.class, IncidentRetrieveByTestCaseResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "User_RetrieveByIdResult", scope = UserRetrieveByIdResponse.class)
    public JAXBElement<RemoteUser> createUserRetrieveByIdResponseUserRetrieveByIdResult(RemoteUser value) {
        return new JAXBElement<RemoteUser>(_UserRetrieveByIdResponseUserRetrieveByIdResult_QNAME, RemoteUser.class, UserRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "User_CreateResult", scope = UserCreateResponse.class)
    public JAXBElement<RemoteUser> createUserCreateResponseUserCreateResult(RemoteUser value) {
        return new JAXBElement<RemoteUser>(_UserCreateResponseUserCreateResult_QNAME, RemoteUser.class, UserCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "binaryData", scope = DocumentAddFile.class)
    public JAXBElement<byte[]> createDocumentAddFileBinaryData(byte[] value) {
        return new JAXBElement<byte[]>(_DocumentAddFileVersionBinaryData_QNAME, byte[].class, DocumentAddFile.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteDocument", scope = DocumentAddFile.class)
    public JAXBElement<RemoteDocument> createDocumentAddFileRemoteDocument(RemoteDocument value) {
        return new JAXBElement<RemoteDocument>(_DocumentAddUrlRemoteDocument_QNAME, RemoteDocument.class, DocumentAddFile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSetTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestSetTestCaseMapping", scope = TestSetRemoveTestMapping.class)
    public JAXBElement<RemoteTestSetTestCaseMapping> createTestSetRemoveTestMappingRemoteTestSetTestCaseMapping(RemoteTestSetTestCaseMapping value) {
        return new JAXBElement<RemoteTestSetTestCaseMapping>(_TestSetAddTestMappingRemoteTestSetTestCaseMapping_QNAME, RemoteTestSetTestCaseMapping.class, TestSetRemoveTestMapping.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_CreateResult", scope = TestCaseCreateResponse.class)
    public JAXBElement<RemoteTestCase> createTestCaseCreateResponseTestCaseCreateResult(RemoteTestCase value) {
        return new JAXBElement<RemoteTestCase>(_TestCaseCreateResponseTestCaseCreateResult_QNAME, RemoteTestCase.class, TestCaseCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = AutomationHostRetrieve.class)
    public JAXBElement<RemoteSort> createAutomationHostRetrieveRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, AutomationHostRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = AutomationHostRetrieve.class)
    public JAXBElement<ArrayOfRemoteFilter> createAutomationHostRetrieveRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, AutomationHostRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Requirement_Create1Result", scope = RequirementCreate1Response.class)
    public JAXBElement<RemoteRequirement> createRequirementCreate1ResponseRequirementCreate1Result(RemoteRequirement value) {
        return new JAXBElement<RemoteRequirement>(_RequirementCreate1ResponseRequirementCreate1Result_QNAME, RemoteRequirement.class, RequirementCreate1Response.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentPriority }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteIncidentPriority", scope = IncidentAddPriority.class)
    public JAXBElement<RemoteIncidentPriority> createIncidentAddPriorityRemoteIncidentPriority(RemoteIncidentPriority value) {
        return new JAXBElement<RemoteIncidentPriority>(_IncidentAddPriorityRemoteIncidentPriority_QNAME, RemoteIncidentPriority.class, IncidentAddPriority.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreationDate", scope = RemoteBuildSourceCode.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteBuildSourceCodeCreationDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteAssociationCreationDate_QNAME, XMLGregorianCalendar.class, RemoteBuildSourceCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RevisionKey", scope = RemoteBuildSourceCode.class)
    public JAXBElement<String> createRemoteBuildSourceCodeRevisionKey(String value) {
        return new JAXBElement<String>(_RemoteBuildSourceCodeRevisionKey_QNAME, String.class, RemoteBuildSourceCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestSetCount.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestSetCountRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestSetCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Project_CreateResult", scope = ProjectCreateResponse.class)
    public JAXBElement<RemoteProject> createProjectCreateResponseProjectCreateResult(RemoteProject value) {
        return new JAXBElement<RemoteProject>(_ProjectCreateResponseProjectCreateResult_QNAME, RemoteProject.class, ProjectCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "destinationRequirementId", scope = RequirementMove.class)
    public JAXBElement<Integer> createRequirementMoveDestinationRequirementId(Integer value) {
        return new JAXBElement<Integer>(_RequirementMoveDestinationRequirementId_QNAME, Integer.class, RequirementMove.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteReleaseTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteReleaseTestCaseMappings", scope = ReleaseAddTestMapping2 .class)
    public JAXBElement<ArrayOfRemoteReleaseTestCaseMapping> createReleaseAddTestMapping2RemoteReleaseTestCaseMappings(ArrayOfRemoteReleaseTestCaseMapping value) {
        return new JAXBElement<ArrayOfRemoteReleaseTestCaseMapping>(_ReleaseAddTestMapping2RemoteReleaseTestCaseMappings_QNAME, ArrayOfRemoteReleaseTestCaseMapping.class, ReleaseAddTestMapping2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "DataMapping_RetrieveArtifactMappingsResult", scope = DataMappingRetrieveArtifactMappingsResponse.class)
    public JAXBElement<ArrayOfRemoteDataMapping> createDataMappingRetrieveArtifactMappingsResponseDataMappingRetrieveArtifactMappingsResult(ArrayOfRemoteDataMapping value) {
        return new JAXBElement<ArrayOfRemoteDataMapping>(_DataMappingRetrieveArtifactMappingsResponseDataMappingRetrieveArtifactMappingsResult_QNAME, ArrayOfRemoteDataMapping.class, DataMappingRetrieveArtifactMappingsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestSet_RetrieveByIdResult", scope = TestSetRetrieveByIdResponse.class)
    public JAXBElement<RemoteTestSet> createTestSetRetrieveByIdResponseTestSetRetrieveByIdResult(RemoteTestSet value) {
        return new JAXBElement<RemoteTestSet>(_TestSetRetrieveByIdResponseTestSetRetrieveByIdResult_QNAME, RemoteTestSet.class, TestSetRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = RequirementCount.class)
    public JAXBElement<ArrayOfRemoteFilter> createRequirementCountRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, RequirementCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "PropertyName", scope = RemoteSort.class)
    public JAXBElement<String> createRemoteSortPropertyName(String value) {
        return new JAXBElement<String>(_RemoteFilterPropertyName_QNAME, String.class, RemoteSort.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Definition", scope = RemoteArtifactCustomProperty.class)
    public JAXBElement<RemoteCustomProperty> createRemoteArtifactCustomPropertyDefinition(RemoteCustomProperty value) {
        return new JAXBElement<RemoteCustomProperty>(_RemoteArtifactCustomPropertyDefinition_QNAME, RemoteCustomProperty.class, RemoteArtifactCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "StringValue", scope = RemoteArtifactCustomProperty.class)
    public JAXBElement<String> createRemoteArtifactCustomPropertyStringValue(String value) {
        return new JAXBElement<String>(_RemoteFilterStringValue_QNAME, String.class, RemoteArtifactCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "DateTimeValue", scope = RemoteArtifactCustomProperty.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteArtifactCustomPropertyDateTimeValue(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteArtifactCustomPropertyDateTimeValue_QNAME, XMLGregorianCalendar.class, RemoteArtifactCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "DecimalValue", scope = RemoteArtifactCustomProperty.class)
    public JAXBElement<BigDecimal> createRemoteArtifactCustomPropertyDecimalValue(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_RemoteArtifactCustomPropertyDecimalValue_QNAME, BigDecimal.class, RemoteArtifactCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "BooleanValue", scope = RemoteArtifactCustomProperty.class)
    public JAXBElement<Boolean> createRemoteArtifactCustomPropertyBooleanValue(Boolean value) {
        return new JAXBElement<Boolean>(_RemoteArtifactCustomPropertyBooleanValue_QNAME, Boolean.class, RemoteArtifactCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IntegerListValue", scope = RemoteArtifactCustomProperty.class)
    public JAXBElement<ArrayOfint> createRemoteArtifactCustomPropertyIntegerListValue(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_RemoteArtifactCustomPropertyIntegerListValue_QNAME, ArrayOfint.class, RemoteArtifactCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IntegerValue", scope = RemoteArtifactCustomProperty.class)
    public JAXBElement<Integer> createRemoteArtifactCustomPropertyIntegerValue(Integer value) {
        return new JAXBElement<Integer>(_RemoteArtifactCustomPropertyIntegerValue_QNAME, Integer.class, RemoteArtifactCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "DataMapping_RetrieveCustomPropertyValueMappingsResult", scope = DataMappingRetrieveCustomPropertyValueMappingsResponse.class)
    public JAXBElement<ArrayOfRemoteDataMapping> createDataMappingRetrieveCustomPropertyValueMappingsResponseDataMappingRetrieveCustomPropertyValueMappingsResult(ArrayOfRemoteDataMapping value) {
        return new JAXBElement<ArrayOfRemoteDataMapping>(_DataMappingRetrieveCustomPropertyValueMappingsResponseDataMappingRetrieveCustomPropertyValueMappingsResult_QNAME, ArrayOfRemoteDataMapping.class, DataMappingRetrieveCustomPropertyValueMappingsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_CreateCommentResult", scope = TestCaseCreateCommentResponse.class)
    public JAXBElement<RemoteComment> createTestCaseCreateCommentResponseTestCaseCreateCommentResult(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_TestCaseCreateCommentResponseTestCaseCreateCommentResult_QNAME, RemoteComment.class, TestCaseCreateCommentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_CreateForAutomatedTestSetResult", scope = TestRunCreateForAutomatedTestSetResponse.class)
    public JAXBElement<ArrayOfRemoteAutomatedTestRun> createTestRunCreateForAutomatedTestSetResponseTestRunCreateForAutomatedTestSetResult(ArrayOfRemoteAutomatedTestRun value) {
        return new JAXBElement<ArrayOfRemoteAutomatedTestRun>(_TestRunCreateForAutomatedTestSetResponseTestRunCreateForAutomatedTestSetResult_QNAME, ArrayOfRemoteAutomatedTestRun.class, TestRunCreateForAutomatedTestSetResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDocumentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_RetrieveTypesResult", scope = DocumentRetrieveTypesResponse.class)
    public JAXBElement<ArrayOfRemoteDocumentType> createDocumentRetrieveTypesResponseDocumentRetrieveTypesResult(ArrayOfRemoteDocumentType value) {
        return new JAXBElement<ArrayOfRemoteDocumentType>(_DocumentRetrieveTypesResponseDocumentRetrieveTypesResult_QNAME, ArrayOfRemoteDocumentType.class, DocumentRetrieveTypesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestSet_CreateResult", scope = TestSetCreateResponse.class)
    public JAXBElement<RemoteTestSet> createTestSetCreateResponseTestSetCreateResult(RemoteTestSet value) {
        return new JAXBElement<RemoteTestSet>(_TestSetCreateResponseTestSetCreateResult_QNAME, RemoteTestSet.class, TestSetCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Token", scope = RemoteAutomationEngine.class)
    public JAXBElement<String> createRemoteAutomationEngineToken(String value) {
        return new JAXBElement<String>(_RemoteAutomationHostToken_QNAME, String.class, RemoteAutomationEngine.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteAutomationEngine.class)
    public JAXBElement<String> createRemoteAutomationEngineDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteAutomationEngine.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteAutomationEngine.class)
    public JAXBElement<String> createRemoteAutomationEngineName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteAutomationEngine.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AutomationEngineId", scope = RemoteAutomationEngine.class)
    public JAXBElement<Integer> createRemoteAutomationEngineAutomationEngineId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestCaseAutomationEngineId_QNAME, Integer.class, RemoteAutomationEngine.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveForOwnerResult", scope = IncidentRetrieveForOwnerResponse.class)
    public JAXBElement<ArrayOfRemoteIncident> createIncidentRetrieveForOwnerResponseIncidentRetrieveForOwnerResult(ArrayOfRemoteIncident value) {
        return new JAXBElement<ArrayOfRemoteIncident>(_IncidentRetrieveForOwnerResponseIncidentRetrieveForOwnerResult_QNAME, ArrayOfRemoteIncident.class, IncidentRetrieveForOwnerResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Requirement_RetrieveResult", scope = RequirementRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteRequirement> createRequirementRetrieveResponseRequirementRetrieveResult(ArrayOfRemoteRequirement value) {
        return new JAXBElement<ArrayOfRemoteRequirement>(_RequirementRetrieveResponseRequirementRetrieveResult_QNAME, ArrayOfRemoteRequirement.class, RequirementRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_CreateFolderResult", scope = TestCaseCreateFolderResponse.class)
    public JAXBElement<RemoteTestCase> createTestCaseCreateFolderResponseTestCaseCreateFolderResult(RemoteTestCase value) {
        return new JAXBElement<RemoteTestCase>(_TestCaseCreateFolderResponseTestCaseCreateFolderResult_QNAME, RemoteTestCase.class, TestCaseCreateFolderResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteIncident", scope = IncidentCreate.class)
    public JAXBElement<RemoteIncident> createIncidentCreateRemoteIncident(RemoteIncident value) {
        return new JAXBElement<RemoteIncident>(_IncidentUpdateRemoteIncident_QNAME, RemoteIncident.class, IncidentCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocumentVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_AddFileVersionResult", scope = DocumentAddFileVersionResponse.class)
    public JAXBElement<RemoteDocumentVersion> createDocumentAddFileVersionResponseDocumentAddFileVersionResult(RemoteDocumentVersion value) {
        return new JAXBElement<RemoteDocumentVersion>(_DocumentAddFileVersionResponseDocumentAddFileVersionResult_QNAME, RemoteDocumentVersion.class, DocumentAddFileVersionResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRelease }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteRelease", scope = ReleaseCreate.class)
    public JAXBElement<RemoteRelease> createReleaseCreateRemoteRelease(RemoteRelease value) {
        return new JAXBElement<RemoteRelease>(_ReleaseUpdateRemoteRelease_QNAME, RemoteRelease.class, ReleaseCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "parentReleaseId", scope = ReleaseCreate.class)
    public JAXBElement<Integer> createReleaseCreateParentReleaseId(Integer value) {
        return new JAXBElement<Integer>(_ReleaseCreateParentReleaseId_QNAME, Integer.class, ReleaseCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestSet_AddTestMappingResult", scope = TestSetAddTestMappingResponse.class)
    public JAXBElement<ArrayOfRemoteTestSetTestCaseMapping> createTestSetAddTestMappingResponseTestSetAddTestMappingResult(ArrayOfRemoteTestSetTestCaseMapping value) {
        return new JAXBElement<ArrayOfRemoteTestSetTestCaseMapping>(_TestSetAddTestMappingResponseTestSetAddTestMappingResult_QNAME, ArrayOfRemoteTestSetTestCaseMapping.class, TestSetAddTestMappingResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteIncidentType", scope = IncidentAddType.class)
    public JAXBElement<RemoteIncidentType> createIncidentAddTypeRemoteIncidentType(RemoteIncidentType value) {
        return new JAXBElement<RemoteIncidentType>(_IncidentAddTypeRemoteIncidentType_QNAME, RemoteIncidentType.class, IncidentAddType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = RequirementRetrieve.class)
    public JAXBElement<ArrayOfRemoteFilter> createRequirementRetrieveRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, RequirementRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "userName", scope = UserRetrieveByUserName.class)
    public JAXBElement<String> createUserRetrieveByUserNameUserName(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2UserName_QNAME, String.class, UserRetrieveByUserName.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "BuildStatusName", scope = RemoteBuild.class)
    public JAXBElement<String> createRemoteBuildBuildStatusName(String value) {
        return new JAXBElement<String>(_RemoteBuildBuildStatusName_QNAME, String.class, RemoteBuild.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteBuildSourceCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Revisions", scope = RemoteBuild.class)
    public JAXBElement<ArrayOfRemoteBuildSourceCode> createRemoteBuildRevisions(ArrayOfRemoteBuildSourceCode value) {
        return new JAXBElement<ArrayOfRemoteBuildSourceCode>(_RemoteBuildRevisions_QNAME, ArrayOfRemoteBuildSourceCode.class, RemoteBuild.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreationDate", scope = RemoteBuild.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteBuildCreationDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteAssociationCreationDate_QNAME, XMLGregorianCalendar.class, RemoteBuild.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "BuildId", scope = RemoteBuild.class)
    public JAXBElement<Integer> createRemoteBuildBuildId(Integer value) {
        return new JAXBElement<Integer>(_RemoteBuildBuildId_QNAME, Integer.class, RemoteBuild.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteBuild.class)
    public JAXBElement<String> createRemoteBuildDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteBuild.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteBuild.class)
    public JAXBElement<String> createRemoteBuildName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteBuild.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_RetrieveByIdResult", scope = TestRunRetrieveByIdResponse.class)
    public JAXBElement<RemoteTestRun> createTestRunRetrieveByIdResponseTestRunRetrieveByIdResult(RemoteTestRun value) {
        return new JAXBElement<RemoteTestRun>(_TestRunRetrieveByIdResponseTestRunRetrieveByIdResult_QNAME, RemoteTestRun.class, TestRunRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteComment", scope = TestCaseCreateComment.class)
    public JAXBElement<RemoteComment> createTestCaseCreateCommentRemoteComment(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_RequirementCreateCommentRemoteComment_QNAME, RemoteComment.class, TestCaseCreateComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteWorkflowIncidentCustomProperties }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveWorkflowCustomPropertiesResult", scope = IncidentRetrieveWorkflowCustomPropertiesResponse.class)
    public JAXBElement<ArrayOfRemoteWorkflowIncidentCustomProperties> createIncidentRetrieveWorkflowCustomPropertiesResponseIncidentRetrieveWorkflowCustomPropertiesResult(ArrayOfRemoteWorkflowIncidentCustomProperties value) {
        return new JAXBElement<ArrayOfRemoteWorkflowIncidentCustomProperties>(_IncidentRetrieveWorkflowCustomPropertiesResponseIncidentRetrieveWorkflowCustomPropertiesResult_QNAME, ArrayOfRemoteWorkflowIncidentCustomProperties.class, IncidentRetrieveWorkflowCustomPropertiesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "parentTestSetFolderId", scope = TestSetCreateFolder.class)
    public JAXBElement<Integer> createTestSetCreateFolderParentTestSetFolderId(Integer value) {
        return new JAXBElement<Integer>(_TestSetCreateParentTestSetFolderId_QNAME, Integer.class, TestSetCreateFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestSet", scope = TestSetCreateFolder.class)
    public JAXBElement<RemoteTestSet> createTestSetCreateFolderRemoteTestSet(RemoteTestSet value) {
        return new JAXBElement<RemoteTestSet>(_TestSetUpdateRemoteTestSet_QNAME, RemoteTestSet.class, TestSetCreateFolder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_RetrieveAutomatedResult", scope = TestRunRetrieveAutomatedResponse.class)
    public JAXBElement<ArrayOfRemoteAutomatedTestRun> createTestRunRetrieveAutomatedResponseTestRunRetrieveAutomatedResult(ArrayOfRemoteAutomatedTestRun value) {
        return new JAXBElement<ArrayOfRemoteAutomatedTestRun>(_TestRunRetrieveAutomatedResponseTestRunRetrieveAutomatedResult_QNAME, ArrayOfRemoteAutomatedTestRun.class, TestRunRetrieveAutomatedResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FieldCaption", scope = RemoteWorkflowIncidentCustomProperties.class)
    public JAXBElement<String> createRemoteWorkflowIncidentCustomPropertiesFieldCaption(String value) {
        return new JAXBElement<String>(_RemoteWorkflowIncidentFieldsFieldCaption_QNAME, String.class, RemoteWorkflowIncidentCustomProperties.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FieldName", scope = RemoteWorkflowIncidentCustomProperties.class)
    public JAXBElement<String> createRemoteWorkflowIncidentCustomPropertiesFieldName(String value) {
        return new JAXBElement<String>(_RemoteWorkflowIncidentFieldsFieldName_QNAME, String.class, RemoteWorkflowIncidentCustomProperties.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteRelease }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Release_RetrieveResult", scope = ReleaseRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteRelease> createReleaseRetrieveResponseReleaseRetrieveResult(ArrayOfRemoteRelease value) {
        return new JAXBElement<ArrayOfRemoteRelease>(_ReleaseRetrieveResponseReleaseRetrieveResult_QNAME, ArrayOfRemoteRelease.class, ReleaseRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomPropertyOption }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Options", scope = RemoteCustomProperty.class)
    public JAXBElement<ArrayOfRemoteCustomPropertyOption> createRemoteCustomPropertyOptions(ArrayOfRemoteCustomPropertyOption value) {
        return new JAXBElement<ArrayOfRemoteCustomPropertyOption>(_RemoteCustomPropertyOptions_QNAME, ArrayOfRemoteCustomPropertyOption.class, RemoteCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CustomPropertyTypeName", scope = RemoteCustomProperty.class)
    public JAXBElement<String> createRemoteCustomPropertyCustomPropertyTypeName(String value) {
        return new JAXBElement<String>(_RemoteCustomPropertyCustomPropertyTypeName_QNAME, String.class, RemoteCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CustomList", scope = RemoteCustomProperty.class)
    public JAXBElement<RemoteCustomList> createRemoteCustomPropertyCustomList(RemoteCustomList value) {
        return new JAXBElement<RemoteCustomList>(_RemoteCustomPropertyCustomList_QNAME, RemoteCustomList.class, RemoteCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "SystemDataType", scope = RemoteCustomProperty.class)
    public JAXBElement<String> createRemoteCustomPropertySystemDataType(String value) {
        return new JAXBElement<String>(_RemoteCustomPropertySystemDataType_QNAME, String.class, RemoteCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CustomPropertyId", scope = RemoteCustomProperty.class)
    public JAXBElement<Integer> createRemoteCustomPropertyCustomPropertyId(Integer value) {
        return new JAXBElement<Integer>(_RemoteCustomPropertyCustomPropertyId_QNAME, Integer.class, RemoteCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteCustomProperty.class)
    public JAXBElement<String> createRemoteCustomPropertyName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CustomPropertyFieldName", scope = RemoteCustomProperty.class)
    public JAXBElement<String> createRemoteCustomPropertyCustomPropertyFieldName(String value) {
        return new JAXBElement<String>(_RemoteCustomPropertyCustomPropertyFieldName_QNAME, String.class, RemoteCustomProperty.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Task_RetrieveCommentsResult", scope = TaskRetrieveCommentsResponse.class)
    public JAXBElement<ArrayOfRemoteComment> createTaskRetrieveCommentsResponseTaskRetrieveCommentsResult(ArrayOfRemoteComment value) {
        return new JAXBElement<ArrayOfRemoteComment>(_TaskRetrieveCommentsResponseTaskRetrieveCommentsResult_QNAME, ArrayOfRemoteComment.class, TaskRetrieveCommentsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "System_GetProductNameResult", scope = SystemGetProductNameResponse.class)
    public JAXBElement<String> createSystemGetProductNameResponseSystemGetProductNameResult(String value) {
        return new JAXBElement<String>(_SystemGetProductNameResponseSystemGetProductNameResult_QNAME, String.class, SystemGetProductNameResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveTypesResult", scope = IncidentRetrieveTypesResponse.class)
    public JAXBElement<ArrayOfRemoteIncidentType> createIncidentRetrieveTypesResponseIncidentRetrieveTypesResult(ArrayOfRemoteIncidentType value) {
        return new JAXBElement<ArrayOfRemoteIncidentType>(_IncidentRetrieveTypesResponseIncidentRetrieveTypesResult_QNAME, ArrayOfRemoteIncidentType.class, IncidentRetrieveTypesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteCustomList", scope = CustomPropertyUpdateCustomList.class)
    public JAXBElement<RemoteCustomList> createCustomPropertyUpdateCustomListRemoteCustomList(RemoteCustomList value) {
        return new JAXBElement<RemoteCustomList>(_CustomPropertyAddCustomListRemoteCustomList_QNAME, RemoteCustomList.class, CustomPropertyUpdateCustomList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveStatusesResult", scope = IncidentRetrieveStatusesResponse.class)
    public JAXBElement<ArrayOfRemoteIncidentStatus> createIncidentRetrieveStatusesResponseIncidentRetrieveStatusesResult(ArrayOfRemoteIncidentStatus value) {
        return new JAXBElement<ArrayOfRemoteIncidentStatus>(_IncidentRetrieveStatusesResponseIncidentRetrieveStatusesResult_QNAME, ArrayOfRemoteIncidentStatus.class, IncidentRetrieveStatusesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "Summary", scope = ValidationFaultMessage.class)
    public JAXBElement<String> createValidationFaultMessageSummary(String value) {
        return new JAXBElement<String>(_ValidationFaultMessageSummary_QNAME, String.class, ValidationFaultMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfValidationFaultMessageItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "Messages", scope = ValidationFaultMessage.class)
    public JAXBElement<ArrayOfValidationFaultMessageItem> createValidationFaultMessageMessages(ArrayOfValidationFaultMessageItem value) {
        return new JAXBElement<ArrayOfValidationFaultMessageItem>(_ValidationFaultMessageMessages_QNAME, ArrayOfValidationFaultMessageItem.class, ValidationFaultMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Project_RetrieveByIdResult", scope = ProjectRetrieveByIdResponse.class)
    public JAXBElement<RemoteProject> createProjectRetrieveByIdResponseProjectRetrieveByIdResult(RemoteProject value) {
        return new JAXBElement<RemoteProject>(_ProjectRetrieveByIdResponseProjectRetrieveByIdResult_QNAME, RemoteProject.class, ProjectRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "plugInName", scope = ConnectionAuthenticate2 .class)
    public JAXBElement<String> createConnectionAuthenticate2PlugInName(String value) {
        return new JAXBElement<String>(_ConnectionAuthenticate2PlugInName_QNAME, String.class, ConnectionAuthenticate2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "password", scope = ConnectionAuthenticate2 .class)
    public JAXBElement<String> createConnectionAuthenticate2Password(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2Password_QNAME, String.class, ConnectionAuthenticate2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "userName", scope = ConnectionAuthenticate2 .class)
    public JAXBElement<String> createConnectionAuthenticate2UserName(String value) {
        return new JAXBElement<String>(_TestRunRecordAutomated2UserName_QNAME, String.class, ConnectionAuthenticate2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_RetrieveByIdResult", scope = TestCaseRetrieveByIdResponse.class)
    public JAXBElement<RemoteTestCase> createTestCaseRetrieveByIdResponseTestCaseRetrieveByIdResult(RemoteTestCase value) {
        return new JAXBElement<RemoteTestCase>(_TestCaseRetrieveByIdResponseTestCaseRetrieveByIdResult_QNAME, RemoteTestCase.class, TestCaseRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "destinationTestCaseId", scope = TestCaseMove.class)
    public JAXBElement<Integer> createTestCaseMoveDestinationTestCaseId(Integer value) {
        return new JAXBElement<Integer>(_TestCaseMoveDestinationTestCaseId_QNAME, Integer.class, TestCaseMove.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "System_GetWebServerUrlResult", scope = SystemGetWebServerUrlResponse.class)
    public JAXBElement<String> createSystemGetWebServerUrlResponseSystemGetWebServerUrlResult(String value) {
        return new JAXBElement<String>(_SystemGetWebServerUrlResponseSystemGetWebServerUrlResult_QNAME, String.class, SystemGetWebServerUrlResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_RetrieveCommentsResult", scope = TestCaseRetrieveCommentsResponse.class)
    public JAXBElement<ArrayOfRemoteComment> createTestCaseRetrieveCommentsResponseTestCaseRetrieveCommentsResult(ArrayOfRemoteComment value) {
        return new JAXBElement<ArrayOfRemoteComment>(_TestCaseRetrieveCommentsResponseTestCaseRetrieveCommentsResult_QNAME, ArrayOfRemoteComment.class, TestCaseRetrieveCommentsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationEngine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "AutomationEngine_RetrieveByTokenResult", scope = AutomationEngineRetrieveByTokenResponse.class)
    public JAXBElement<RemoteAutomationEngine> createAutomationEngineRetrieveByTokenResponseAutomationEngineRetrieveByTokenResult(RemoteAutomationEngine value) {
        return new JAXBElement<RemoteAutomationEngine>(_AutomationEngineRetrieveByTokenResponseAutomationEngineRetrieveByTokenResult_QNAME, RemoteAutomationEngine.class, AutomationEngineRetrieveByTokenResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_AddCommentsResult", scope = IncidentAddCommentsResponse.class)
    public JAXBElement<ArrayOfRemoteComment> createIncidentAddCommentsResponseIncidentAddCommentsResult(ArrayOfRemoteComment value) {
        return new JAXBElement<ArrayOfRemoteComment>(_IncidentAddCommentsResponseIncidentAddCommentsResult_QNAME, ArrayOfRemoteComment.class, IncidentAddCommentsResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_CreateParameterTokenResult", scope = TestCaseCreateParameterTokenResponse.class)
    public JAXBElement<String> createTestCaseCreateParameterTokenResponseTestCaseCreateParameterTokenResult(String value) {
        return new JAXBElement<String>(_TestCaseCreateParameterTokenResponseTestCaseCreateParameterTokenResult_QNAME, String.class, TestCaseCreateParameterTokenResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_RetrieveForArtifactResult", scope = DocumentRetrieveForArtifactResponse.class)
    public JAXBElement<ArrayOfRemoteDocument> createDocumentRetrieveForArtifactResponseDocumentRetrieveForArtifactResult(ArrayOfRemoteDocument value) {
        return new JAXBElement<ArrayOfRemoteDocument>(_DocumentRetrieveForArtifactResponseDocumentRetrieveForArtifactResult_QNAME, ArrayOfRemoteDocument.class, DocumentRetrieveForArtifactResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectAttachmentTypeName", scope = RemoteDocument.class)
    public JAXBElement<String> createRemoteDocumentProjectAttachmentTypeName(String value) {
        return new JAXBElement<String>(_RemoteDocumentProjectAttachmentTypeName_QNAME, String.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EditorName", scope = RemoteDocument.class)
    public JAXBElement<String> createRemoteDocumentEditorName(String value) {
        return new JAXBElement<String>(_RemoteDocumentEditorName_QNAME, String.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArtifactId", scope = RemoteDocument.class)
    public JAXBElement<Integer> createRemoteDocumentArtifactId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentArtifactId_QNAME, Integer.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AuthorId", scope = RemoteDocument.class)
    public JAXBElement<Integer> createRemoteDocumentAuthorId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentVersionAuthorId_QNAME, Integer.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ArtifactTypeId", scope = RemoteDocument.class)
    public JAXBElement<Integer> createRemoteDocumentArtifactTypeId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentArtifactTypeId_QNAME, Integer.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDocumentVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Versions", scope = RemoteDocument.class)
    public JAXBElement<ArrayOfRemoteDocumentVersion> createRemoteDocumentVersions(ArrayOfRemoteDocumentVersion value) {
        return new JAXBElement<ArrayOfRemoteDocumentVersion>(_RemoteDocumentVersions_QNAME, ArrayOfRemoteDocumentVersion.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AttachmentId", scope = RemoteDocument.class)
    public JAXBElement<Integer> createRemoteDocumentAttachmentId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentAttachmentId_QNAME, Integer.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectAttachmentTypeId", scope = RemoteDocument.class)
    public JAXBElement<Integer> createRemoteDocumentProjectAttachmentTypeId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentTypeProjectAttachmentTypeId_QNAME, Integer.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteDocument.class)
    public JAXBElement<String> createRemoteDocumentDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AttachmentTypeName", scope = RemoteDocument.class)
    public JAXBElement<String> createRemoteDocumentAttachmentTypeName(String value) {
        return new JAXBElement<String>(_RemoteDocumentAttachmentTypeName_QNAME, String.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CurrentVersion", scope = RemoteDocument.class)
    public JAXBElement<String> createRemoteDocumentCurrentVersion(String value) {
        return new JAXBElement<String>(_RemoteDocumentCurrentVersion_QNAME, String.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectAttachmentFolderId", scope = RemoteDocument.class)
    public JAXBElement<Integer> createRemoteDocumentProjectAttachmentFolderId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentFolderProjectAttachmentFolderId_QNAME, Integer.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AuthorName", scope = RemoteDocument.class)
    public JAXBElement<String> createRemoteDocumentAuthorName(String value) {
        return new JAXBElement<String>(_RemoteDocumentVersionAuthorName_QNAME, String.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EditorId", scope = RemoteDocument.class)
    public JAXBElement<Integer> createRemoteDocumentEditorId(Integer value) {
        return new JAXBElement<Integer>(_RemoteDocumentEditorId_QNAME, Integer.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "FilenameOrUrl", scope = RemoteDocument.class)
    public JAXBElement<String> createRemoteDocumentFilenameOrUrl(String value) {
        return new JAXBElement<String>(_RemoteDocumentVersionFilenameOrUrl_QNAME, String.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Tags", scope = RemoteDocument.class)
    public JAXBElement<String> createRemoteDocumentTags(String value) {
        return new JAXBElement<String>(_RemoteDocumentTags_QNAME, String.class, RemoteDocument.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_RetrieveResult", scope = TestRunRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteTestRun> createTestRunRetrieveResponseTestRunRetrieveResult(ArrayOfRemoteTestRun value) {
        return new JAXBElement<ArrayOfRemoteTestRun>(_TestRunRetrieveResponseTestRunRetrieveResult_QNAME, ArrayOfRemoteTestRun.class, TestRunRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteCustomProperty", scope = CustomPropertyAddDefinition.class)
    public JAXBElement<RemoteCustomProperty> createCustomPropertyAddDefinitionRemoteCustomProperty(RemoteCustomProperty value) {
        return new JAXBElement<RemoteCustomProperty>(_CustomPropertyUpdateDefinitionRemoteCustomProperty_QNAME, RemoteCustomProperty.class, CustomPropertyAddDefinition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "customListId", scope = CustomPropertyAddDefinition.class)
    public JAXBElement<Integer> createCustomPropertyAddDefinitionCustomListId(Integer value) {
        return new JAXBElement<Integer>(_CustomPropertyAddDefinitionCustomListId_QNAME, Integer.class, CustomPropertyAddDefinition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteRequirement", scope = RequirementCreate1 .class)
    public JAXBElement<RemoteRequirement> createRequirementCreate1RemoteRequirement(RemoteRequirement value) {
        return new JAXBElement<RemoteRequirement>(_RequirementCreate2RemoteRequirement_QNAME, RemoteRequirement.class, RequirementCreate1 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Requirement_RetrieveForOwnerResult", scope = RequirementRetrieveForOwnerResponse.class)
    public JAXBElement<ArrayOfRemoteRequirement> createRequirementRetrieveForOwnerResponseRequirementRetrieveForOwnerResult(ArrayOfRemoteRequirement value) {
        return new JAXBElement<ArrayOfRemoteRequirement>(_RequirementRetrieveForOwnerResponseRequirementRetrieveForOwnerResult_QNAME, ArrayOfRemoteRequirement.class, RequirementRetrieveForOwnerResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteIncidentSeverity.class)
    public JAXBElement<String> createRemoteIncidentSeverityName2(String value) {
        return new JAXBElement<String>(_RemoteIncidentPriorityName2_QNAME, String.class, RemoteIncidentSeverity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Color", scope = RemoteIncidentSeverity.class)
    public JAXBElement<String> createRemoteIncidentSeverityColor(String value) {
        return new JAXBElement<String>(_RemoteIncidentPriorityColor_QNAME, String.class, RemoteIncidentSeverity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "SeverityId", scope = RemoteIncidentSeverity.class)
    public JAXBElement<Integer> createRemoteIncidentSeveritySeverityId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentSeverityId_QNAME, Integer.class, RemoteIncidentSeverity.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentPriority }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_AddPriorityResult", scope = IncidentAddPriorityResponse.class)
    public JAXBElement<RemoteIncidentPriority> createIncidentAddPriorityResponseIncidentAddPriorityResult(RemoteIncidentPriority value) {
        return new JAXBElement<RemoteIncidentPriority>(_IncidentAddPriorityResponseIncidentAddPriorityResult_QNAME, RemoteIncidentPriority.class, IncidentAddPriorityResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteSort", scope = TestRunRetrieveAutomated.class)
    public JAXBElement<RemoteSort> createTestRunRetrieveAutomatedRemoteSort(RemoteSort value) {
        return new JAXBElement<RemoteSort>(_DocumentRetrieveForFolderRemoteSort_QNAME, RemoteSort.class, TestRunRetrieveAutomated.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestRunRetrieveAutomated.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestRunRetrieveAutomatedRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestRunRetrieveAutomated.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteComment", scope = ReleaseCreateComment.class)
    public JAXBElement<RemoteComment> createReleaseCreateCommentRemoteComment(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_RequirementCreateCommentRemoteComment_QNAME, RemoteComment.class, ReleaseCreateComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveByTestRunStepResult", scope = IncidentRetrieveByTestRunStepResponse.class)
    public JAXBElement<ArrayOfRemoteIncident> createIncidentRetrieveByTestRunStepResponseIncidentRetrieveByTestRunStepResult(ArrayOfRemoteIncident value) {
        return new JAXBElement<ArrayOfRemoteIncident>(_IncidentRetrieveByTestRunStepResponseIncidentRetrieveByTestRunStepResult_QNAME, ArrayOfRemoteIncident.class, IncidentRetrieveByTestRunStepResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncidentStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_AddStatusResult", scope = IncidentAddStatusResponse.class)
    public JAXBElement<RemoteIncidentStatus> createIncidentAddStatusResponseIncidentAddStatusResult(RemoteIncidentStatus value) {
        return new JAXBElement<RemoteIncidentStatus>(_IncidentAddStatusResponseIncidentAddStatusResult_QNAME, RemoteIncidentStatus.class, IncidentAddStatusResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TaskCount.class)
    public JAXBElement<ArrayOfRemoteFilter> createTaskCountRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TaskCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteComment", scope = TaskCreateComment.class)
    public JAXBElement<RemoteComment> createTaskCreateCommentRemoteComment(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_RequirementCreateCommentRemoteComment_QNAME, RemoteComment.class, TaskCreateComment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_RetrieveResult", scope = TestCaseRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteTestCase> createTestCaseRetrieveResponseTestCaseRetrieveResult(ArrayOfRemoteTestCase value) {
        return new JAXBElement<ArrayOfRemoteTestCase>(_TestCaseRetrieveResponseTestCaseRetrieveResult_QNAME, ArrayOfRemoteTestCase.class, TestCaseRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestRun", scope = TestRunRecordAutomated1 .class)
    public JAXBElement<RemoteAutomatedTestRun> createTestRunRecordAutomated1RemoteTestRun(RemoteAutomatedTestRun value) {
        return new JAXBElement<RemoteAutomatedTestRun>(_TestRunRecordAutomated1RemoteTestRun_QNAME, RemoteAutomatedTestRun.class, TestRunRecordAutomated1 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_AddStepResult", scope = TestCaseAddStepResponse.class)
    public JAXBElement<RemoteTestStep> createTestCaseAddStepResponseTestCaseAddStepResult(RemoteTestStep value) {
        return new JAXBElement<RemoteTestStep>(_TestCaseAddStepResponseTestCaseAddStepResult_QNAME, RemoteTestStep.class, TestCaseAddStepResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RunnerTestName", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<String> createRemoteAutomatedTestRunRunnerTestName(String value) {
        return new JAXBElement<String>(_RemoteAutomatedTestRunRunnerTestName_QNAME, String.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AutomationAttachmentId", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<Integer> createRemoteAutomatedTestRunAutomationAttachmentId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestCaseAutomationAttachmentId_QNAME, Integer.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RunnerMessage", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<String> createRemoteAutomatedTestRunRunnerMessage(String value) {
        return new JAXBElement<String>(_RemoteAutomatedTestRunRunnerMessage_QNAME, String.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RunnerAssertCount", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<Integer> createRemoteAutomatedTestRunRunnerAssertCount(Integer value) {
        return new JAXBElement<Integer>(_RemoteAutomatedTestRunRunnerAssertCount_QNAME, Integer.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ScheduledDate", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteAutomatedTestRunScheduledDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteAutomatedTestRunScheduledDate_QNAME, XMLGregorianCalendar.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AutomationHostId", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<Integer> createRemoteAutomatedTestRunAutomationHostId(Integer value) {
        return new JAXBElement<Integer>(_RemoteAutomationHostAutomationHostId_QNAME, Integer.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RunnerStackTrace", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<String> createRemoteAutomatedTestRunRunnerStackTrace(String value) {
        return new JAXBElement<String>(_RemoteAutomatedTestRunRunnerStackTrace_QNAME, String.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AutomationEngineToken", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<String> createRemoteAutomatedTestRunAutomationEngineToken(String value) {
        return new JAXBElement<String>(_RemoteAutomatedTestRunAutomationEngineToken_QNAME, String.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RunnerName", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<String> createRemoteAutomatedTestRunRunnerName(String value) {
        return new JAXBElement<String>(_RemoteAutomatedTestRunRunnerName_QNAME, String.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestRunStep }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestRunSteps", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<ArrayOfRemoteTestRunStep> createRemoteAutomatedTestRunTestRunSteps(ArrayOfRemoteTestRunStep value) {
        return new JAXBElement<ArrayOfRemoteTestRunStep>(_RemoteManualTestRunTestRunSteps_QNAME, ArrayOfRemoteTestRunStep.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AutomationEngineId", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<Integer> createRemoteAutomatedTestRunAutomationEngineId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestCaseAutomationEngineId_QNAME, Integer.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Parameters", scope = RemoteAutomatedTestRun.class)
    public JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> createRemoteAutomatedTestRunParameters(ArrayOfRemoteTestSetTestCaseParameter value) {
        return new JAXBElement<ArrayOfRemoteTestSetTestCaseParameter>(_RemoteAutomatedTestRunParameters_QNAME, ArrayOfRemoteTestSetTestCaseParameter.class, RemoteAutomatedTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteAutomationHost", scope = AutomationHostCreate.class)
    public JAXBElement<RemoteAutomationHost> createAutomationHostCreateRemoteAutomationHost(RemoteAutomationHost value) {
        return new JAXBElement<RemoteAutomationHost>(_AutomationHostUpdateRemoteAutomationHost_QNAME, RemoteAutomationHost.class, AutomationHostCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestRunCount.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestRunCountRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestRunCount.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomationHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "AutomationHost_RetrieveResult", scope = AutomationHostRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteAutomationHost> createAutomationHostRetrieveResponseAutomationHostRetrieveResult(ArrayOfRemoteAutomationHost value) {
        return new JAXBElement<ArrayOfRemoteAutomationHost>(_AutomationHostRetrieveResponseAutomationHostRetrieveResult_QNAME, ArrayOfRemoteAutomationHost.class, AutomationHostRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteDocument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_RetrieveByIdResult", scope = DocumentRetrieveByIdResponse.class)
    public JAXBElement<RemoteDocument> createDocumentRetrieveByIdResponseDocumentRetrieveByIdResult(RemoteDocument value) {
        return new JAXBElement<RemoteDocument>(_DocumentRetrieveByIdResponseDocumentRetrieveByIdResult_QNAME, RemoteDocument.class, DocumentRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_CreateResult", scope = IncidentCreateResponse.class)
    public JAXBElement<RemoteIncident> createIncidentCreateResponseIncidentCreateResult(RemoteIncident value) {
        return new JAXBElement<RemoteIncident>(_IncidentCreateResponseIncidentCreateResult_QNAME, RemoteIncident.class, IncidentCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Value", scope = RemoteTestSetTestCaseParameter.class)
    public JAXBElement<String> createRemoteTestSetTestCaseParameterValue(String value) {
        return new JAXBElement<String>(_RemoteTestStepParameterValue_QNAME, String.class, RemoteTestSetTestCaseParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteTestSetTestCaseParameter.class)
    public JAXBElement<String> createRemoteTestSetTestCaseParameterName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteTestSetTestCaseParameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteDataMappings", scope = DataMappingAddArtifactMappings.class)
    public JAXBElement<ArrayOfRemoteDataMapping> createDataMappingAddArtifactMappingsRemoteDataMappings(ArrayOfRemoteDataMapping value) {
        return new JAXBElement<ArrayOfRemoteDataMapping>(_DataMappingAddUserMappingsRemoteDataMappings_QNAME, ArrayOfRemoteDataMapping.class, DataMappingAddArtifactMappings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Document_OpenFileResult", scope = DocumentOpenFileResponse.class)
    public JAXBElement<byte[]> createDocumentOpenFileResponseDocumentOpenFileResult(byte[] value) {
        return new JAXBElement<byte[]>(_DocumentOpenFileResponseDocumentOpenFileResult_QNAME, byte[].class, DocumentOpenFileResponse.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestStepParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_RetrieveStepParametersResult", scope = TestCaseRetrieveStepParametersResponse.class)
    public JAXBElement<ArrayOfRemoteTestStepParameter> createTestCaseRetrieveStepParametersResponseTestCaseRetrieveStepParametersResult(ArrayOfRemoteTestStepParameter value) {
        return new JAXBElement<ArrayOfRemoteTestStepParameter>(_TestCaseRetrieveStepParametersResponseTestCaseRetrieveStepParametersResult_QNAME, ArrayOfRemoteTestStepParameter.class, TestCaseRetrieveStepParametersResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteManualTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_RetrieveManualResult", scope = TestRunRetrieveManualResponse.class)
    public JAXBElement<ArrayOfRemoteManualTestRun> createTestRunRetrieveManualResponseTestRunRetrieveManualResult(ArrayOfRemoteManualTestRun value) {
        return new JAXBElement<ArrayOfRemoteManualTestRun>(_TestRunRetrieveManualResponseTestRunRetrieveManualResult_QNAME, ArrayOfRemoteManualTestRun.class, TestRunRetrieveManualResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteProjectRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "ProjectRole_RetrieveResult", scope = ProjectRoleRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteProjectRole> createProjectRoleRetrieveResponseProjectRoleRetrieveResult(ArrayOfRemoteProjectRole value) {
        return new JAXBElement<ArrayOfRemoteProjectRole>(_ProjectRoleRetrieveResponseProjectRoleRetrieveResult_QNAME, ArrayOfRemoteProjectRole.class, ProjectRoleRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveResult", scope = IncidentRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteIncident> createIncidentRetrieveResponseIncidentRetrieveResult(ArrayOfRemoteIncident value) {
        return new JAXBElement<ArrayOfRemoteIncident>(_IncidentRetrieveResponseIncidentRetrieveResult_QNAME, ArrayOfRemoteIncident.class, IncidentRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteDataMappings", scope = DataMappingRemoveArtifactMappings.class)
    public JAXBElement<ArrayOfRemoteDataMapping> createDataMappingRemoveArtifactMappingsRemoteDataMappings(ArrayOfRemoteDataMapping value) {
        return new JAXBElement<ArrayOfRemoteDataMapping>(_DataMappingAddUserMappingsRemoteDataMappings_QNAME, ArrayOfRemoteDataMapping.class, DataMappingRemoveArtifactMappings.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationEngine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "AutomationEngine_RetrieveByIdResult", scope = AutomationEngineRetrieveByIdResponse.class)
    public JAXBElement<RemoteAutomationEngine> createAutomationEngineRetrieveByIdResponseAutomationEngineRetrieveByIdResult(RemoteAutomationEngine value) {
        return new JAXBElement<RemoteAutomationEngine>(_AutomationEngineRetrieveByIdResponseAutomationEngineRetrieveByIdResult_QNAME, RemoteAutomationEngine.class, AutomationEngineRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "System_GetArtifactUrlResult", scope = SystemGetArtifactUrlResponse.class)
    public JAXBElement<String> createSystemGetArtifactUrlResponseSystemGetArtifactUrlResult(String value) {
        return new JAXBElement<String>(_SystemGetArtifactUrlResponseSystemGetArtifactUrlResult_QNAME, String.class, SystemGetArtifactUrlResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRelease }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Release_RetrieveByIdResult", scope = ReleaseRetrieveByIdResponse.class)
    public JAXBElement<RemoteRelease> createReleaseRetrieveByIdResponseReleaseRetrieveByIdResult(RemoteRelease value) {
        return new JAXBElement<RemoteRelease>(_ReleaseRetrieveByIdResponseReleaseRetrieveByIdResult_QNAME, RemoteRelease.class, ReleaseRetrieveByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteAutomatedTestRun }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestRun_CreateForAutomationHostResult", scope = TestRunCreateForAutomationHostResponse.class)
    public JAXBElement<ArrayOfRemoteAutomatedTestRun> createTestRunCreateForAutomationHostResponseTestRunCreateForAutomationHostResult(ArrayOfRemoteAutomatedTestRun value) {
        return new JAXBElement<ArrayOfRemoteAutomatedTestRun>(_TestRunCreateForAutomationHostResponseTestRunCreateForAutomationHostResult_QNAME, ArrayOfRemoteAutomatedTestRun.class, TestRunCreateForAutomationHostResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Task_RetrieveForOwnerResult", scope = TaskRetrieveForOwnerResponse.class)
    public JAXBElement<ArrayOfRemoteTask> createTaskRetrieveForOwnerResponseTaskRetrieveForOwnerResult(ArrayOfRemoteTask value) {
        return new JAXBElement<ArrayOfRemoteTask>(_TaskRetrieveForOwnerResponseTaskRetrieveForOwnerResult_QNAME, ArrayOfRemoteTask.class, TaskRetrieveForOwnerResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Task_CreateCommentResult", scope = TaskCreateCommentResponse.class)
    public JAXBElement<RemoteComment> createTaskCreateCommentResponseTaskCreateCommentResult(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_TaskCreateCommentResponseTaskCreateCommentResult_QNAME, RemoteComment.class, TaskCreateCommentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_RetrieveByTestSetIdResult", scope = TestCaseRetrieveByTestSetIdResponse.class)
    public JAXBElement<ArrayOfRemoteTestCase> createTestCaseRetrieveByTestSetIdResponseTestCaseRetrieveByTestSetIdResult(ArrayOfRemoteTestCase value) {
        return new JAXBElement<ArrayOfRemoteTestCase>(_TestCaseRetrieveByTestSetIdResponseTestCaseRetrieveByTestSetIdResult_QNAME, ArrayOfRemoteTestCase.class, TestCaseRetrieveByTestSetIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "parentTestFolderId", scope = TestCaseCreate.class)
    public JAXBElement<Integer> createTestCaseCreateParentTestFolderId(Integer value) {
        return new JAXBElement<Integer>(_TestCaseCreateFolderParentTestFolderId_QNAME, Integer.class, TestCaseCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteTestCase", scope = TestCaseCreate.class)
    public JAXBElement<RemoteTestCase> createTestCaseCreateRemoteTestCase(RemoteTestCase value) {
        return new JAXBElement<RemoteTestCase>(_TestCaseCreateFolderRemoteTestCase_QNAME, RemoteTestCase.class, TestCaseCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TesterId", scope = RemoteTestRun.class)
    public JAXBElement<Integer> createRemoteTestRunTesterId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunTesterId_QNAME, Integer.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EstimatedDuration", scope = RemoteTestRun.class)
    public JAXBElement<Integer> createRemoteTestRunEstimatedDuration(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestCaseEstimatedDuration_QNAME, Integer.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ActualDuration", scope = RemoteTestRun.class)
    public JAXBElement<Integer> createRemoteTestRunActualDuration(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunActualDuration_QNAME, Integer.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ReleaseId", scope = RemoteTestRun.class)
    public JAXBElement<Integer> createRemoteTestRunReleaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseReleaseId_QNAME, Integer.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "BuildId", scope = RemoteTestRun.class)
    public JAXBElement<Integer> createRemoteTestRunBuildId(Integer value) {
        return new JAXBElement<Integer>(_RemoteBuildBuildId_QNAME, Integer.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestRunId", scope = RemoteTestRun.class)
    public JAXBElement<Integer> createRemoteTestRunTestRunId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunTestRunId_QNAME, Integer.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "EndDate", scope = RemoteTestRun.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteTestRunEndDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateRangeEndDate_QNAME, XMLGregorianCalendar.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestSetTestCaseId", scope = RemoteTestRun.class)
    public JAXBElement<Integer> createRemoteTestRunTestSetTestCaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunTestSetTestCaseId_QNAME, Integer.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestSetId", scope = RemoteTestRun.class)
    public JAXBElement<Integer> createRemoteTestRunTestSetId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunTestSetId_QNAME, Integer.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteTestRun.class)
    public JAXBElement<String> createRemoteTestRunName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteTestRun.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "User_RetrieveByUserNameResult", scope = UserRetrieveByUserNameResponse.class)
    public JAXBElement<RemoteUser> createUserRetrieveByUserNameResponseUserRetrieveByUserNameResult(RemoteUser value) {
        return new JAXBElement<RemoteUser>(_UserRetrieveByUserNameResponseUserRetrieveByUserNameResult_QNAME, RemoteUser.class, UserRetrieveByUserNameResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteBuild }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteBuild", scope = BuildCreate.class)
    public JAXBElement<RemoteBuild> createBuildCreateRemoteBuild(RemoteBuild value) {
        return new JAXBElement<RemoteBuild>(_BuildCreateRemoteBuild_QNAME, RemoteBuild.class, BuildCreate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "FieldName", scope = ValidationFaultMessageItem.class)
    public JAXBElement<String> createValidationFaultMessageItemFieldName(String value) {
        return new JAXBElement<String>(_ValidationFaultMessageItemFieldName_QNAME, String.class, ValidationFaultMessageItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", name = "Message", scope = ValidationFaultMessageItem.class)
    public JAXBElement<String> createValidationFaultMessageItemMessage(String value) {
        return new JAXBElement<String>(_ServiceFaultMessageMessage_QNAME, String.class, ValidationFaultMessageItem.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteCustomList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "CustomProperty_RetrieveCustomListByIdResult", scope = CustomPropertyRetrieveCustomListByIdResponse.class)
    public JAXBElement<RemoteCustomList> createCustomPropertyRetrieveCustomListByIdResponseCustomPropertyRetrieveCustomListByIdResult(RemoteCustomList value) {
        return new JAXBElement<RemoteCustomList>(_CustomPropertyRetrieveCustomListByIdResponseCustomPropertyRetrieveCustomListByIdResult_QNAME, RemoteCustomList.class, CustomPropertyRetrieveCustomListByIdResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestStepParameter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "parameters", scope = TestCaseAddLink.class)
    public JAXBElement<ArrayOfRemoteTestStepParameter> createTestCaseAddLinkParameters(ArrayOfRemoteTestStepParameter value) {
        return new JAXBElement<ArrayOfRemoteTestStepParameter>(_TestSetAddTestMappingParameters_QNAME, ArrayOfRemoteTestStepParameter.class, TestCaseAddLink.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteFilters", scope = TestCaseRetrieve.class)
    public JAXBElement<ArrayOfRemoteFilter> createTestCaseRetrieveRemoteFilters(ArrayOfRemoteFilter value) {
        return new JAXBElement<ArrayOfRemoteFilter>(_TestCaseCountRemoteFilters_QNAME, ArrayOfRemoteFilter.class, TestCaseRetrieve.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "remoteRequirement", scope = RequirementUpdate.class)
    public JAXBElement<RemoteRequirement> createRequirementUpdateRemoteRequirement(RemoteRequirement value) {
        return new JAXBElement<RemoteRequirement>(_RequirementCreate2RemoteRequirement_QNAME, RemoteRequirement.class, RequirementUpdate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Requirement_CreateCommentResult", scope = RequirementCreateCommentResponse.class)
    public JAXBElement<RemoteComment> createRequirementCreateCommentResponseRequirementCreateCommentResult(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_RequirementCreateCommentResponseRequirementCreateCommentResult_QNAME, RemoteComment.class, RequirementCreateCommentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentPriority }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrievePrioritiesResult", scope = IncidentRetrievePrioritiesResponse.class)
    public JAXBElement<ArrayOfRemoteIncidentPriority> createIncidentRetrievePrioritiesResponseIncidentRetrievePrioritiesResult(ArrayOfRemoteIncidentPriority value) {
        return new JAXBElement<ArrayOfRemoteIncidentPriority>(_IncidentRetrievePrioritiesResponseIncidentRetrievePrioritiesResult_QNAME, ArrayOfRemoteIncidentPriority.class, IncidentRetrievePrioritiesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteAutomationHost }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "AutomationHost_CreateResult", scope = AutomationHostCreateResponse.class)
    public JAXBElement<RemoteAutomationHost> createAutomationHostCreateResponseAutomationHostCreateResult(RemoteAutomationHost value) {
        return new JAXBElement<RemoteAutomationHost>(_AutomationHostCreateResponseAutomationHostCreateResult_QNAME, RemoteAutomationHost.class, AutomationHostCreateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestSet_RetrieveResult", scope = TestSetRetrieveResponse.class)
    public JAXBElement<ArrayOfRemoteTestSet> createTestSetRetrieveResponseTestSetRetrieveResult(ArrayOfRemoteTestSet value) {
        return new JAXBElement<ArrayOfRemoteTestSet>(_TestSetRetrieveResponseTestSetRetrieveResult_QNAME, ArrayOfRemoteTestSet.class, TestSetRetrieveResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RecurrenceName", scope = RemoteTestSet.class)
    public JAXBElement<String> createRemoteTestSetRecurrenceName(String value) {
        return new JAXBElement<String>(_RemoteTestSetRecurrenceName_QNAME, String.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CountCaution", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetCountCaution(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetCountCaution_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CountFailed", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetCountFailed(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetCountFailed_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerId", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetOwnerId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetTestCaseMappingOwnerId_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ExecutionDate", scope = RemoteTestSet.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteTestSetExecutionDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteTestCaseExecutionDate_QNAME, XMLGregorianCalendar.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CountNotRun", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetCountNotRun(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetCountNotRun_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Description", scope = RemoteTestSet.class)
    public JAXBElement<String> createRemoteTestSetDescription(String value) {
        return new JAXBElement<String>(_RemoteReleaseDescription_QNAME, String.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ProjectName", scope = RemoteTestSet.class)
    public JAXBElement<String> createRemoteTestSetProjectName(String value) {
        return new JAXBElement<String>(_RemoteIncidentProjectName_QNAME, String.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestSetId", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetTestSetId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestRunTestSetId_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "PlannedDate", scope = RemoteTestSet.class)
    public JAXBElement<XMLGregorianCalendar> createRemoteTestSetPlannedDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_RemoteTestSetPlannedDate_QNAME, XMLGregorianCalendar.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "OwnerName", scope = RemoteTestSet.class)
    public JAXBElement<String> createRemoteTestSetOwnerName(String value) {
        return new JAXBElement<String>(_RemoteIncidentOwnerName_QNAME, String.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ReleaseId", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetReleaseId(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseReleaseId_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CountBlocked", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetCountBlocked(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetCountBlocked_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "AutomationHostId", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetAutomationHostId(Integer value) {
        return new JAXBElement<Integer>(_RemoteAutomationHostAutomationHostId_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestRunTypeId", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetTestRunTypeId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetTestRunTypeId_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IndentLevel", scope = RemoteTestSet.class)
    public JAXBElement<String> createRemoteTestSetIndentLevel(String value) {
        return new JAXBElement<String>(_RemoteReleaseIndentLevel_QNAME, String.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "ReleaseVersionNumber", scope = RemoteTestSet.class)
    public JAXBElement<String> createRemoteTestSetReleaseVersionNumber(String value) {
        return new JAXBElement<String>(_RemoteTaskReleaseVersionNumber_QNAME, String.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreatorId", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetCreatorId(Integer value) {
        return new JAXBElement<Integer>(_RemoteReleaseCreatorId_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CreatorName", scope = RemoteTestSet.class)
    public JAXBElement<String> createRemoteTestSetCreatorName(String value) {
        return new JAXBElement<String>(_RemoteReleaseCreatorName_QNAME, String.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "TestSetStatusName", scope = RemoteTestSet.class)
    public JAXBElement<String> createRemoteTestSetTestSetStatusName(String value) {
        return new JAXBElement<String>(_RemoteTestSetTestSetStatusName_QNAME, String.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "RecurrenceId", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetRecurrenceId(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetRecurrenceId_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CountNotApplicable", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetCountNotApplicable(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetCountNotApplicable_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CountPassed", scope = RemoteTestSet.class)
    public JAXBElement<Integer> createRemoteTestSetCountPassed(Integer value) {
        return new JAXBElement<Integer>(_RemoteTestSetCountPassed_QNAME, Integer.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteTestSet.class)
    public JAXBElement<String> createRemoteTestSetName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteTestSet.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "TestCase_RetrieveByFolderResult", scope = TestCaseRetrieveByFolderResponse.class)
    public JAXBElement<ArrayOfRemoteTestCase> createTestCaseRetrieveByFolderResponseTestCaseRetrieveByFolderResult(ArrayOfRemoteTestCase value) {
        return new JAXBElement<ArrayOfRemoteTestCase>(_TestCaseRetrieveByFolderResponseTestCaseRetrieveByFolderResult_QNAME, ArrayOfRemoteTestCase.class, TestCaseRetrieveByFolderResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "CustomPropertyListId", scope = RemoteCustomList.class)
    public JAXBElement<Integer> createRemoteCustomListCustomPropertyListId(Integer value) {
        return new JAXBElement<Integer>(_RemoteCustomListCustomPropertyListId_QNAME, Integer.class, RemoteCustomList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomListValue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Values", scope = RemoteCustomList.class)
    public JAXBElement<ArrayOfRemoteCustomListValue> createRemoteCustomListValues(ArrayOfRemoteCustomListValue value) {
        return new JAXBElement<ArrayOfRemoteCustomListValue>(_MultiValueFilterValues_QNAME, ArrayOfRemoteCustomListValue.class, RemoteCustomList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteCustomList.class)
    public JAXBElement<String> createRemoteCustomListName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteCustomList.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncident }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Incident_RetrieveNewResult", scope = IncidentRetrieveNewResponse.class)
    public JAXBElement<ArrayOfRemoteIncident> createIncidentRetrieveNewResponseIncidentRetrieveNewResult(ArrayOfRemoteIncident value) {
        return new JAXBElement<ArrayOfRemoteIncident>(_IncidentRetrieveNewResponseIncidentRetrieveNewResult_QNAME, ArrayOfRemoteIncident.class, IncidentRetrieveNewResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", name = "Release_CreateCommentResult", scope = ReleaseCreateCommentResponse.class)
    public JAXBElement<RemoteComment> createReleaseCreateCommentResponseReleaseCreateCommentResult(RemoteComment value) {
        return new JAXBElement<RemoteComment>(_ReleaseCreateCommentResponseReleaseCreateCommentResult_QNAME, RemoteComment.class, ReleaseCreateCommentResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "Name", scope = RemoteIncidentType.class)
    public JAXBElement<String> createRemoteIncidentTypeName(String value) {
        return new JAXBElement<String>(_RemoteReleaseName_QNAME, String.class, RemoteIncidentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", name = "IncidentTypeId", scope = RemoteIncidentType.class)
    public JAXBElement<Integer> createRemoteIncidentTypeIncidentTypeId(Integer value) {
        return new JAXBElement<Integer>(_RemoteIncidentIncidentTypeId_QNAME, Integer.class, RemoteIncidentType.class, value);
    }

}
