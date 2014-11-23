
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
 *         &lt;element name="AutomationHost_RetrieveByIdResult" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteAutomationHost" minOccurs="0"/>
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
    "automationHostRetrieveByIdResult"
})
@XmlRootElement(name = "AutomationHost_RetrieveByIdResponse")
public class AutomationHostRetrieveByIdResponse {

    @XmlElementRef(name = "AutomationHost_RetrieveByIdResult", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteAutomationHost> automationHostRetrieveByIdResult;

    /**
     * Gets the value of the automationHostRetrieveByIdResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteAutomationHost }{@code >}
     *     
     */
    public JAXBElement<RemoteAutomationHost> getAutomationHostRetrieveByIdResult() {
        return automationHostRetrieveByIdResult;
    }

    /**
     * Sets the value of the automationHostRetrieveByIdResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteAutomationHost }{@code >}
     *     
     */
    public void setAutomationHostRetrieveByIdResult(JAXBElement<RemoteAutomationHost> value) {
        this.automationHostRetrieveByIdResult = ((JAXBElement<RemoteAutomationHost> ) value);
    }

}
