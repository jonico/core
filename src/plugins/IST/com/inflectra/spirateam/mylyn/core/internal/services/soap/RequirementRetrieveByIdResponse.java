
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
 *         &lt;element name="Requirement_RetrieveByIdResult" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteRequirement" minOccurs="0"/>
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
    "requirementRetrieveByIdResult"
})
@XmlRootElement(name = "Requirement_RetrieveByIdResponse")
public class RequirementRetrieveByIdResponse {

    @XmlElementRef(name = "Requirement_RetrieveByIdResult", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteRequirement> requirementRetrieveByIdResult;

    /**
     * Gets the value of the requirementRetrieveByIdResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteRequirement }{@code >}
     *     
     */
    public JAXBElement<RemoteRequirement> getRequirementRetrieveByIdResult() {
        return requirementRetrieveByIdResult;
    }

    /**
     * Sets the value of the requirementRetrieveByIdResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteRequirement }{@code >}
     *     
     */
    public void setRequirementRetrieveByIdResult(JAXBElement<RemoteRequirement> value) {
        this.requirementRetrieveByIdResult = ((JAXBElement<RemoteRequirement> ) value);
    }

}
