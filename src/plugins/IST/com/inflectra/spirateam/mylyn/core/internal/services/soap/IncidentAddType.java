
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
 *         &lt;element name="remoteIncidentType" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteIncidentType" minOccurs="0"/>
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
    "remoteIncidentType"
})
@XmlRootElement(name = "Incident_AddType")
public class IncidentAddType {

    @XmlElementRef(name = "remoteIncidentType", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteIncidentType> remoteIncidentType;

    /**
     * Gets the value of the remoteIncidentType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteIncidentType }{@code >}
     *     
     */
    public JAXBElement<RemoteIncidentType> getRemoteIncidentType() {
        return remoteIncidentType;
    }

    /**
     * Sets the value of the remoteIncidentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteIncidentType }{@code >}
     *     
     */
    public void setRemoteIncidentType(JAXBElement<RemoteIncidentType> value) {
        this.remoteIncidentType = ((JAXBElement<RemoteIncidentType> ) value);
    }

}
