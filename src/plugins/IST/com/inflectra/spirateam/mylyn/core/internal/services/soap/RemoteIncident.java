
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;comments xmlns:msc="http://schemas.microsoft.com/ws/2005/12/wsdl/contract" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:tns="http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects" xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing" xmlns:wsa10="http://www.w3.org/2005/08/addressing" xmlns:wsam="http://www.w3.org/2007/05/addressing/metadata" xmlns:wsap="http://schemas.xmlsoap.org/ws/2004/08/addressing/policy" xmlns:wsaw="http://www.w3.org/2006/05/addressing/wsdl" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:wsp="http://schemas.xmlsoap.org/ws/2004/09/policy" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" xmlns:wsx="http://schemas.xmlsoap.org/ws/2004/09/mex" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;summary&gt;
 *                 Represents a single Incident artifact in the system
 *               &lt;/summary&gt;&lt;/comments&gt;
 * </pre>
 * 
 * 
 * <p>Java class for RemoteIncident complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RemoteIncident">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteArtifact">
 *       &lt;sequence>
 *         &lt;element name="ActualEffort" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ClosedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="CompletionPercent" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="CreationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DetectedReleaseId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="DetectedReleaseVersionNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EstimatedEffort" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="FixedBuildId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="FixedBuildName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IncidentId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IncidentStatusId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IncidentStatusName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IncidentStatusOpenStatus" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IncidentTypeId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IncidentTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LastUpdateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OpenerId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="OpenerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OwnerId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="OwnerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PriorityId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="PriorityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProjectedEffort" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="RemainingEffort" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ResolvedReleaseId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ResolvedReleaseVersionNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SeverityId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="SeverityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="TestRunStepId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="VerifiedReleaseId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="VerifiedReleaseVersionNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoteIncident", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", propOrder = {
    "actualEffort",
    "closedDate",
    "completionPercent",
    "creationDate",
    "description",
    "detectedReleaseId",
    "detectedReleaseVersionNumber",
    "estimatedEffort",
    "fixedBuildId",
    "fixedBuildName",
    "incidentId",
    "incidentStatusId",
    "incidentStatusName",
    "incidentStatusOpenStatus",
    "incidentTypeId",
    "incidentTypeName",
    "lastUpdateDate",
    "name",
    "openerId",
    "openerName",
    "ownerId",
    "ownerName",
    "priorityId",
    "priorityName",
    "projectName",
    "projectedEffort",
    "remainingEffort",
    "resolvedReleaseId",
    "resolvedReleaseVersionNumber",
    "severityId",
    "severityName",
    "startDate",
    "testRunStepId",
    "verifiedReleaseId",
    "verifiedReleaseVersionNumber"
})
public class RemoteIncident
    extends RemoteArtifact
{

    @XmlElementRef(name = "ActualEffort", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> actualEffort;
    @XmlElementRef(name = "ClosedDate", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> closedDate;
    @XmlElement(name = "CompletionPercent")
    protected Integer completionPercent;
    @XmlElementRef(name = "CreationDate", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> creationDate;
    @XmlElementRef(name = "Description", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> description;
    @XmlElementRef(name = "DetectedReleaseId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> detectedReleaseId;
    @XmlElementRef(name = "DetectedReleaseVersionNumber", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> detectedReleaseVersionNumber;
    @XmlElementRef(name = "EstimatedEffort", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> estimatedEffort;
    @XmlElementRef(name = "FixedBuildId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> fixedBuildId;
    @XmlElementRef(name = "FixedBuildName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> fixedBuildName;
    @XmlElementRef(name = "IncidentId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> incidentId;
    @XmlElementRef(name = "IncidentStatusId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> incidentStatusId;
    @XmlElementRef(name = "IncidentStatusName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> incidentStatusName;
    @XmlElementRef(name = "IncidentStatusOpenStatus", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Boolean> incidentStatusOpenStatus;
    @XmlElementRef(name = "IncidentTypeId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> incidentTypeId;
    @XmlElementRef(name = "IncidentTypeName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> incidentTypeName;
    @XmlElement(name = "LastUpdateDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpdateDate;
    @XmlElementRef(name = "Name", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> name;
    @XmlElementRef(name = "OpenerId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> openerId;
    @XmlElementRef(name = "OpenerName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> openerName;
    @XmlElementRef(name = "OwnerId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> ownerId;
    @XmlElementRef(name = "OwnerName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> ownerName;
    @XmlElementRef(name = "PriorityId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> priorityId;
    @XmlElementRef(name = "PriorityName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> priorityName;
    @XmlElementRef(name = "ProjectName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> projectName;
    @XmlElementRef(name = "ProjectedEffort", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> projectedEffort;
    @XmlElementRef(name = "RemainingEffort", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> remainingEffort;
    @XmlElementRef(name = "ResolvedReleaseId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> resolvedReleaseId;
    @XmlElementRef(name = "ResolvedReleaseVersionNumber", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> resolvedReleaseVersionNumber;
    @XmlElementRef(name = "SeverityId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> severityId;
    @XmlElementRef(name = "SeverityName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> severityName;
    @XmlElementRef(name = "StartDate", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> startDate;
    @XmlElementRef(name = "TestRunStepId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> testRunStepId;
    @XmlElementRef(name = "VerifiedReleaseId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> verifiedReleaseId;
    @XmlElementRef(name = "VerifiedReleaseVersionNumber", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> verifiedReleaseVersionNumber;

    /**
     * Gets the value of the actualEffort property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getActualEffort() {
        return actualEffort;
    }

    /**
     * Sets the value of the actualEffort property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setActualEffort(JAXBElement<Integer> value) {
        this.actualEffort = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the closedDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getClosedDate() {
        return closedDate;
    }

    /**
     * Sets the value of the closedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setClosedDate(JAXBElement<XMLGregorianCalendar> value) {
        this.closedDate = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

    /**
     * Gets the value of the completionPercent property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCompletionPercent() {
        return completionPercent;
    }

    /**
     * Sets the value of the completionPercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCompletionPercent(Integer value) {
        this.completionPercent = value;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setCreationDate(JAXBElement<XMLGregorianCalendar> value) {
        this.creationDate = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescription(JAXBElement<String> value) {
        this.description = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the detectedReleaseId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getDetectedReleaseId() {
        return detectedReleaseId;
    }

    /**
     * Sets the value of the detectedReleaseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setDetectedReleaseId(JAXBElement<Integer> value) {
        this.detectedReleaseId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the detectedReleaseVersionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDetectedReleaseVersionNumber() {
        return detectedReleaseVersionNumber;
    }

    /**
     * Sets the value of the detectedReleaseVersionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDetectedReleaseVersionNumber(JAXBElement<String> value) {
        this.detectedReleaseVersionNumber = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the estimatedEffort property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getEstimatedEffort() {
        return estimatedEffort;
    }

    /**
     * Sets the value of the estimatedEffort property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setEstimatedEffort(JAXBElement<Integer> value) {
        this.estimatedEffort = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the fixedBuildId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getFixedBuildId() {
        return fixedBuildId;
    }

    /**
     * Sets the value of the fixedBuildId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setFixedBuildId(JAXBElement<Integer> value) {
        this.fixedBuildId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the fixedBuildName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFixedBuildName() {
        return fixedBuildName;
    }

    /**
     * Sets the value of the fixedBuildName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFixedBuildName(JAXBElement<String> value) {
        this.fixedBuildName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the incidentId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getIncidentId() {
        return incidentId;
    }

    /**
     * Sets the value of the incidentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setIncidentId(JAXBElement<Integer> value) {
        this.incidentId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the incidentStatusId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getIncidentStatusId() {
        return incidentStatusId;
    }

    /**
     * Sets the value of the incidentStatusId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setIncidentStatusId(JAXBElement<Integer> value) {
        this.incidentStatusId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the incidentStatusName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIncidentStatusName() {
        return incidentStatusName;
    }

    /**
     * Sets the value of the incidentStatusName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIncidentStatusName(JAXBElement<String> value) {
        this.incidentStatusName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the incidentStatusOpenStatus property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getIncidentStatusOpenStatus() {
        return incidentStatusOpenStatus;
    }

    /**
     * Sets the value of the incidentStatusOpenStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setIncidentStatusOpenStatus(JAXBElement<Boolean> value) {
        this.incidentStatusOpenStatus = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the incidentTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getIncidentTypeId() {
        return incidentTypeId;
    }

    /**
     * Sets the value of the incidentTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setIncidentTypeId(JAXBElement<Integer> value) {
        this.incidentTypeId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the incidentTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIncidentTypeName() {
        return incidentTypeName;
    }

    /**
     * Sets the value of the incidentTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIncidentTypeName(JAXBElement<String> value) {
        this.incidentTypeName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the lastUpdateDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Sets the value of the lastUpdateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpdateDate(XMLGregorianCalendar value) {
        this.lastUpdateDate = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setName(JAXBElement<String> value) {
        this.name = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the openerId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getOpenerId() {
        return openerId;
    }

    /**
     * Sets the value of the openerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setOpenerId(JAXBElement<Integer> value) {
        this.openerId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the openerName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOpenerName() {
        return openerName;
    }

    /**
     * Sets the value of the openerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOpenerName(JAXBElement<String> value) {
        this.openerName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the ownerId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the value of the ownerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setOwnerId(JAXBElement<Integer> value) {
        this.ownerId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the ownerName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOwnerName() {
        return ownerName;
    }

    /**
     * Sets the value of the ownerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOwnerName(JAXBElement<String> value) {
        this.ownerName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the priorityId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getPriorityId() {
        return priorityId;
    }

    /**
     * Sets the value of the priorityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setPriorityId(JAXBElement<Integer> value) {
        this.priorityId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the priorityName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPriorityName() {
        return priorityName;
    }

    /**
     * Sets the value of the priorityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPriorityName(JAXBElement<String> value) {
        this.priorityName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the projectName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getProjectName() {
        return projectName;
    }

    /**
     * Sets the value of the projectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setProjectName(JAXBElement<String> value) {
        this.projectName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the projectedEffort property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getProjectedEffort() {
        return projectedEffort;
    }

    /**
     * Sets the value of the projectedEffort property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setProjectedEffort(JAXBElement<Integer> value) {
        this.projectedEffort = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the remainingEffort property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getRemainingEffort() {
        return remainingEffort;
    }

    /**
     * Sets the value of the remainingEffort property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setRemainingEffort(JAXBElement<Integer> value) {
        this.remainingEffort = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the resolvedReleaseId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getResolvedReleaseId() {
        return resolvedReleaseId;
    }

    /**
     * Sets the value of the resolvedReleaseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setResolvedReleaseId(JAXBElement<Integer> value) {
        this.resolvedReleaseId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the resolvedReleaseVersionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getResolvedReleaseVersionNumber() {
        return resolvedReleaseVersionNumber;
    }

    /**
     * Sets the value of the resolvedReleaseVersionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setResolvedReleaseVersionNumber(JAXBElement<String> value) {
        this.resolvedReleaseVersionNumber = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the severityId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getSeverityId() {
        return severityId;
    }

    /**
     * Sets the value of the severityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setSeverityId(JAXBElement<Integer> value) {
        this.severityId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the severityName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSeverityName() {
        return severityName;
    }

    /**
     * Sets the value of the severityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSeverityName(JAXBElement<String> value) {
        this.severityName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setStartDate(JAXBElement<XMLGregorianCalendar> value) {
        this.startDate = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

    /**
     * Gets the value of the testRunStepId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getTestRunStepId() {
        return testRunStepId;
    }

    /**
     * Sets the value of the testRunStepId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setTestRunStepId(JAXBElement<Integer> value) {
        this.testRunStepId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the verifiedReleaseId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getVerifiedReleaseId() {
        return verifiedReleaseId;
    }

    /**
     * Sets the value of the verifiedReleaseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setVerifiedReleaseId(JAXBElement<Integer> value) {
        this.verifiedReleaseId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the verifiedReleaseVersionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getVerifiedReleaseVersionNumber() {
        return verifiedReleaseVersionNumber;
    }

    /**
     * Sets the value of the verifiedReleaseVersionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setVerifiedReleaseVersionNumber(JAXBElement<String> value) {
        this.verifiedReleaseVersionNumber = ((JAXBElement<String> ) value);
    }

}
