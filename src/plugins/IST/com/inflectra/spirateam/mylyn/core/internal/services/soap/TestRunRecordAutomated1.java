
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
 *         &lt;element name="remoteTestRun" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteAutomatedTestRun" minOccurs="0"/>
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
    "remoteTestRun"
})
@XmlRootElement(name = "TestRun_RecordAutomated1")
public class TestRunRecordAutomated1 {

    @XmlElementRef(name = "remoteTestRun", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteAutomatedTestRun> remoteTestRun;

    /**
     * Gets the value of the remoteTestRun property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteAutomatedTestRun }{@code >}
     *     
     */
    public JAXBElement<RemoteAutomatedTestRun> getRemoteTestRun() {
        return remoteTestRun;
    }

    /**
     * Sets the value of the remoteTestRun property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteAutomatedTestRun }{@code >}
     *     
     */
    public void setRemoteTestRun(JAXBElement<RemoteAutomatedTestRun> value) {
        this.remoteTestRun = ((JAXBElement<RemoteAutomatedTestRun> ) value);
    }

}
