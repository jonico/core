
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="TestCase_CountForFolderResult" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
    "testCaseCountForFolderResult"
})
@XmlRootElement(name = "TestCase_CountForFolderResponse")
public class TestCaseCountForFolderResponse {

    @XmlElement(name = "TestCase_CountForFolderResult")
    protected Long testCaseCountForFolderResult;

    /**
     * Gets the value of the testCaseCountForFolderResult property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTestCaseCountForFolderResult() {
        return testCaseCountForFolderResult;
    }

    /**
     * Sets the value of the testCaseCountForFolderResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTestCaseCountForFolderResult(Long value) {
        this.testCaseCountForFolderResult = value;
    }

}
