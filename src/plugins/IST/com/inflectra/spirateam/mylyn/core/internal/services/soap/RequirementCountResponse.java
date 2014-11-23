
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
 *         &lt;element name="Requirement_CountResult" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
    "requirementCountResult"
})
@XmlRootElement(name = "Requirement_CountResponse")
public class RequirementCountResponse {

    @XmlElement(name = "Requirement_CountResult")
    protected Long requirementCountResult;

    /**
     * Gets the value of the requirementCountResult property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRequirementCountResult() {
        return requirementCountResult;
    }

    /**
     * Sets the value of the requirementCountResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRequirementCountResult(Long value) {
        this.requirementCountResult = value;
    }

}
