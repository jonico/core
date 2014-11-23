
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
 *         &lt;element name="System_GetProjectIdForArtifactResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "systemGetProjectIdForArtifactResult"
})
@XmlRootElement(name = "System_GetProjectIdForArtifactResponse")
public class SystemGetProjectIdForArtifactResponse {

    @XmlElement(name = "System_GetProjectIdForArtifactResult")
    protected Integer systemGetProjectIdForArtifactResult;

    /**
     * Gets the value of the systemGetProjectIdForArtifactResult property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSystemGetProjectIdForArtifactResult() {
        return systemGetProjectIdForArtifactResult;
    }

    /**
     * Sets the value of the systemGetProjectIdForArtifactResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSystemGetProjectIdForArtifactResult(Integer value) {
        this.systemGetProjectIdForArtifactResult = value;
    }

}
