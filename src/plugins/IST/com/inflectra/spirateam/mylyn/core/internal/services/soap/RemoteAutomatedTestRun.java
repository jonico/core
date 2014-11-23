
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;comments xmlns:msc="http://schemas.microsoft.com/ws/2005/12/wsdl/contract" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:tns="http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects" xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing" xmlns:wsa10="http://www.w3.org/2005/08/addressing" xmlns:wsam="http://www.w3.org/2007/05/addressing/metadata" xmlns:wsap="http://schemas.xmlsoap.org/ws/2004/08/addressing/policy" xmlns:wsaw="http://www.w3.org/2006/05/addressing/wsdl" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:wsp="http://schemas.xmlsoap.org/ws/2004/09/policy" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" xmlns:wsx="http://schemas.xmlsoap.org/ws/2004/09/mex" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;summary&gt;
 *                 Represents an automated test run in the system
 *               &lt;/summary&gt;&lt;/comments&gt;
 * </pre>
 * 
 * 
 * <p>Java class for RemoteAutomatedTestRun complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RemoteAutomatedTestRun">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteTestRun">
 *       &lt;sequence>
 *         &lt;element name="AutomationAttachmentId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="AutomationEngineId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="AutomationEngineToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AutomationHostId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Parameters" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteTestSetTestCaseParameter" minOccurs="0"/>
 *         &lt;element name="RunnerAssertCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="RunnerMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RunnerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RunnerStackTrace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RunnerTestName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ScheduledDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="TestRunFormatId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TestRunSteps" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteTestRunStep" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoteAutomatedTestRun", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", propOrder = {
    "automationAttachmentId",
    "automationEngineId",
    "automationEngineToken",
    "automationHostId",
    "parameters",
    "runnerAssertCount",
    "runnerMessage",
    "runnerName",
    "runnerStackTrace",
    "runnerTestName",
    "scheduledDate",
    "testRunFormatId",
    "testRunSteps"
})
public class RemoteAutomatedTestRun
    extends RemoteTestRun
{

    @XmlElementRef(name = "AutomationAttachmentId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> automationAttachmentId;
    @XmlElementRef(name = "AutomationEngineId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> automationEngineId;
    @XmlElementRef(name = "AutomationEngineToken", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> automationEngineToken;
    @XmlElementRef(name = "AutomationHostId", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> automationHostId;
    @XmlElementRef(name = "Parameters", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> parameters;
    @XmlElementRef(name = "RunnerAssertCount", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<Integer> runnerAssertCount;
    @XmlElementRef(name = "RunnerMessage", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> runnerMessage;
    @XmlElementRef(name = "RunnerName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> runnerName;
    @XmlElementRef(name = "RunnerStackTrace", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> runnerStackTrace;
    @XmlElementRef(name = "RunnerTestName", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<String> runnerTestName;
    @XmlElementRef(name = "ScheduledDate", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> scheduledDate;
    @XmlElement(name = "TestRunFormatId")
    protected Integer testRunFormatId;
    @XmlElementRef(name = "TestRunSteps", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteTestRunStep> testRunSteps;

    /**
     * Gets the value of the automationAttachmentId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getAutomationAttachmentId() {
        return automationAttachmentId;
    }

    /**
     * Sets the value of the automationAttachmentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setAutomationAttachmentId(JAXBElement<Integer> value) {
        this.automationAttachmentId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the automationEngineId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getAutomationEngineId() {
        return automationEngineId;
    }

    /**
     * Sets the value of the automationEngineId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setAutomationEngineId(JAXBElement<Integer> value) {
        this.automationEngineId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the automationEngineToken property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAutomationEngineToken() {
        return automationEngineToken;
    }

    /**
     * Sets the value of the automationEngineToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAutomationEngineToken(JAXBElement<String> value) {
        this.automationEngineToken = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the automationHostId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getAutomationHostId() {
        return automationHostId;
    }

    /**
     * Sets the value of the automationHostId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setAutomationHostId(JAXBElement<Integer> value) {
        this.automationHostId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the parameters property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseParameter }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseParameter }{@code >}
     *     
     */
    public void setParameters(JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> value) {
        this.parameters = ((JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> ) value);
    }

    /**
     * Gets the value of the runnerAssertCount property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getRunnerAssertCount() {
        return runnerAssertCount;
    }

    /**
     * Sets the value of the runnerAssertCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setRunnerAssertCount(JAXBElement<Integer> value) {
        this.runnerAssertCount = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the runnerMessage property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRunnerMessage() {
        return runnerMessage;
    }

    /**
     * Sets the value of the runnerMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRunnerMessage(JAXBElement<String> value) {
        this.runnerMessage = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the runnerName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRunnerName() {
        return runnerName;
    }

    /**
     * Sets the value of the runnerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRunnerName(JAXBElement<String> value) {
        this.runnerName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the runnerStackTrace property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRunnerStackTrace() {
        return runnerStackTrace;
    }

    /**
     * Sets the value of the runnerStackTrace property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRunnerStackTrace(JAXBElement<String> value) {
        this.runnerStackTrace = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the runnerTestName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRunnerTestName() {
        return runnerTestName;
    }

    /**
     * Sets the value of the runnerTestName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRunnerTestName(JAXBElement<String> value) {
        this.runnerTestName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the scheduledDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getScheduledDate() {
        return scheduledDate;
    }

    /**
     * Sets the value of the scheduledDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setScheduledDate(JAXBElement<XMLGregorianCalendar> value) {
        this.scheduledDate = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

    /**
     * Gets the value of the testRunFormatId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTestRunFormatId() {
        return testRunFormatId;
    }

    /**
     * Sets the value of the testRunFormatId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTestRunFormatId(Integer value) {
        this.testRunFormatId = value;
    }

    /**
     * Gets the value of the testRunSteps property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestRunStep }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteTestRunStep> getTestRunSteps() {
        return testRunSteps;
    }

    /**
     * Sets the value of the testRunSteps property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestRunStep }{@code >}
     *     
     */
    public void setTestRunSteps(JAXBElement<ArrayOfRemoteTestRunStep> value) {
        this.testRunSteps = ((JAXBElement<ArrayOfRemoteTestRunStep> ) value);
    }

}
