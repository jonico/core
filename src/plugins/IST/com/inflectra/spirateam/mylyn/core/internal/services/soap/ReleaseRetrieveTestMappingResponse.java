
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
 *         &lt;element name="Release_RetrieveTestMappingResult" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteReleaseTestCaseMapping" minOccurs="0"/>
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
    "releaseRetrieveTestMappingResult"
})
@XmlRootElement(name = "Release_RetrieveTestMappingResponse")
public class ReleaseRetrieveTestMappingResponse {

    @XmlElementRef(name = "Release_RetrieveTestMappingResult", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteReleaseTestCaseMapping> releaseRetrieveTestMappingResult;

    /**
     * Gets the value of the releaseRetrieveTestMappingResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteReleaseTestCaseMapping }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteReleaseTestCaseMapping> getReleaseRetrieveTestMappingResult() {
        return releaseRetrieveTestMappingResult;
    }

    /**
     * Sets the value of the releaseRetrieveTestMappingResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteReleaseTestCaseMapping }{@code >}
     *     
     */
    public void setReleaseRetrieveTestMappingResult(JAXBElement<ArrayOfRemoteReleaseTestCaseMapping> value) {
        this.releaseRetrieveTestMappingResult = ((JAXBElement<ArrayOfRemoteReleaseTestCaseMapping> ) value);
    }

}
