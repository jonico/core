
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
 *         &lt;element name="remoteIncidentResolutions" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v3_0.DataObjects}ArrayOfRemoteIncidentResolution" minOccurs="0"/>
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
    "remoteIncidentResolutions"
})
@XmlRootElement(name = "Incident_AddResolutions")
public class IncidentAddResolutions {

    @XmlElementRef(name = "remoteIncidentResolutions", namespace = "http://www.inflectra.com/SpiraTest/Services/v3.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteIncidentResolution> remoteIncidentResolutions;

    /**
     * Gets the value of the remoteIncidentResolutions property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentResolution }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteIncidentResolution> getRemoteIncidentResolutions() {
        return remoteIncidentResolutions;
    }

    /**
     * Sets the value of the remoteIncidentResolutions property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteIncidentResolution }{@code >}
     *     
     */
    public void setRemoteIncidentResolutions(JAXBElement<ArrayOfRemoteIncidentResolution> value) {
        this.remoteIncidentResolutions = ((JAXBElement<ArrayOfRemoteIncidentResolution> ) value);
    }

}
