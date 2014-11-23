
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TestCase_AddStepResult" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteTestStep" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "testCaseAddStepResult"
})
@XmlRootElement(name = "TestCase_AddStepResponse")
public class TestCaseAddStepResponse {

    @XmlElementRef(name = "TestCase_AddStepResult", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteTestStep> testCaseAddStepResult;

    /**
     * Gets the value of the testCaseAddStepResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteTestStep }{@code >}
     *     
     */
    public JAXBElement<RemoteTestStep> getTestCaseAddStepResult() {
        return testCaseAddStepResult;
    }

    /**
     * Sets the value of the testCaseAddStepResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteTestStep }{@code >}
     *     
     */
    public void setTestCaseAddStepResult(JAXBElement<RemoteTestStep> value) {
        this.testCaseAddStepResult = ((JAXBElement<RemoteTestStep> ) value);
    }

}
