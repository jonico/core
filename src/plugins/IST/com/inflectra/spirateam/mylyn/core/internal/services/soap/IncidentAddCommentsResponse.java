
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
 *         &lt;element name="Incident_AddCommentsResult" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteComment" minOccurs="0"/>
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
    "incidentAddCommentsResult"
})
@XmlRootElement(name = "Incident_AddCommentsResponse")
public class IncidentAddCommentsResponse {

    @XmlElementRef(name = "Incident_AddCommentsResult", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteComment> incidentAddCommentsResult;

    /**
     * Gets the value of the incidentAddCommentsResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteComment> getIncidentAddCommentsResult() {
        return incidentAddCommentsResult;
    }

    /**
     * Sets the value of the incidentAddCommentsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteComment }{@code >}
     *     
     */
    public void setIncidentAddCommentsResult(JAXBElement<ArrayOfRemoteComment> value) {
        this.incidentAddCommentsResult = ((JAXBElement<ArrayOfRemoteComment> ) value);
    }

}
