
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
 *         &lt;element name="CustomProperty_RetrieveForArtifactTypeResult" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteCustomProperty" minOccurs="0"/>
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
    "customPropertyRetrieveForArtifactTypeResult"
})
@XmlRootElement(name = "CustomProperty_RetrieveForArtifactTypeResponse")
public class CustomPropertyRetrieveForArtifactTypeResponse {

    @XmlElementRef(name = "CustomProperty_RetrieveForArtifactTypeResult", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteCustomProperty> customPropertyRetrieveForArtifactTypeResult;

    /**
     * Gets the value of the customPropertyRetrieveForArtifactTypeResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomProperty }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteCustomProperty> getCustomPropertyRetrieveForArtifactTypeResult() {
        return customPropertyRetrieveForArtifactTypeResult;
    }

    /**
     * Sets the value of the customPropertyRetrieveForArtifactTypeResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteCustomProperty }{@code >}
     *     
     */
    public void setCustomPropertyRetrieveForArtifactTypeResult(JAXBElement<ArrayOfRemoteCustomProperty> value) {
        this.customPropertyRetrieveForArtifactTypeResult = ((JAXBElement<ArrayOfRemoteCustomProperty> ) value);
    }

}
