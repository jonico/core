
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
 *         &lt;element name="TestCase_CreateCommentResult" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteComment" minOccurs="0"/>
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
    "testCaseCreateCommentResult"
})
@XmlRootElement(name = "TestCase_CreateCommentResponse")
public class TestCaseCreateCommentResponse {

    @XmlElementRef(name = "TestCase_CreateCommentResult", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteComment> testCaseCreateCommentResult;

    /**
     * Gets the value of the testCaseCreateCommentResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}
     *     
     */
    public JAXBElement<RemoteComment> getTestCaseCreateCommentResult() {
        return testCaseCreateCommentResult;
    }

    /**
     * Sets the value of the testCaseCreateCommentResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteComment }{@code >}
     *     
     */
    public void setTestCaseCreateCommentResult(JAXBElement<RemoteComment> value) {
        this.testCaseCreateCommentResult = ((JAXBElement<RemoteComment> ) value);
    }

}
