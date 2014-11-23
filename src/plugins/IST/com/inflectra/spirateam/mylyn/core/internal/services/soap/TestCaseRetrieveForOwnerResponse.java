
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
 *         &lt;element name="TestCase_RetrieveForOwnerResult" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteTestCase" minOccurs="0"/>
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
    "testCaseRetrieveForOwnerResult"
})
@XmlRootElement(name = "TestCase_RetrieveForOwnerResponse")
public class TestCaseRetrieveForOwnerResponse {

    @XmlElementRef(name = "TestCase_RetrieveForOwnerResult", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteTestCase> testCaseRetrieveForOwnerResult;

    /**
     * Gets the value of the testCaseRetrieveForOwnerResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCase }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteTestCase> getTestCaseRetrieveForOwnerResult() {
        return testCaseRetrieveForOwnerResult;
    }

    /**
     * Sets the value of the testCaseRetrieveForOwnerResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestCase }{@code >}
     *     
     */
    public void setTestCaseRetrieveForOwnerResult(JAXBElement<ArrayOfRemoteTestCase> value) {
        this.testCaseRetrieveForOwnerResult = ((JAXBElement<ArrayOfRemoteTestCase> ) value);
    }

}
