
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
 *         &lt;element name="TestCase_RetrieveCommentsResult" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteComment" minOccurs="0"/>
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
    "testCaseRetrieveCommentsResult"
})
@XmlRootElement(name = "TestCase_RetrieveCommentsResponse")
public class TestCaseRetrieveCommentsResponse {

    @XmlElementRef(name = "TestCase_RetrieveCommentsResult", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteComment> testCaseRetrieveCommentsResult;

    /**
     * Gets the value of the testCaseRetrieveCommentsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteComment> getTestCaseRetrieveCommentsResult() {
        return testCaseRetrieveCommentsResult;
    }

    /**
     * Sets the value of the testCaseRetrieveCommentsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}
     *     
     */
    public void setTestCaseRetrieveCommentsResult(JAXBElement<ArrayOfRemoteComment> value) {
        this.testCaseRetrieveCommentsResult = ((JAXBElement<ArrayOfRemoteComment> ) value);
    }

}
