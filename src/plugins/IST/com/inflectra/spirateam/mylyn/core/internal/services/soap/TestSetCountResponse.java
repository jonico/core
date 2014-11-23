
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
 *         &lt;element name="TestSet_CountResult" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
    "testSetCountResult"
})
@XmlRootElement(name = "TestSet_CountResponse")
public class TestSetCountResponse {

    @XmlElement(name = "TestSet_CountResult")
    protected Long testSetCountResult;

    /**
     * Gets the value of the testSetCountResult property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTestSetCountResult() {
        return testSetCountResult;
    }

    /**
     * Sets the value of the testSetCountResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTestSetCountResult(Long value) {
        this.testSetCountResult = value;
    }

}
