
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
 *         &lt;element name="TestRun_RecordAutomated2Result" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "testRunRecordAutomated2Result"
})
@XmlRootElement(name = "TestRun_RecordAutomated2Response")
public class TestRunRecordAutomated2Response {

    @XmlElement(name = "TestRun_RecordAutomated2Result")
    protected Integer testRunRecordAutomated2Result;

    /**
     * Gets the value of the testRunRecordAutomated2Result property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTestRunRecordAutomated2Result() {
        return testRunRecordAutomated2Result;
    }

    /**
     * Sets the value of the testRunRecordAutomated2Result property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTestRunRecordAutomated2Result(Integer value) {
        this.testRunRecordAutomated2Result = value;
    }

}
