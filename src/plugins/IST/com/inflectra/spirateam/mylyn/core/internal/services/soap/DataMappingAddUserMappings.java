
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
 *         &lt;element name="dataSyncSystemId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="remoteDataMappings" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteDataMapping" minOccurs="0"/>
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
    "dataSyncSystemId",
    "remoteDataMappings"
})
@XmlRootElement(name = "DataMapping_AddUserMappings")
public class DataMappingAddUserMappings {

    protected Integer dataSyncSystemId;
    @XmlElementRef(name = "remoteDataMappings", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteDataMapping> remoteDataMappings;

    /**
     * Gets the value of the dataSyncSystemId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDataSyncSystemId() {
        return dataSyncSystemId;
    }

    /**
     * Sets the value of the dataSyncSystemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDataSyncSystemId(Integer value) {
        this.dataSyncSystemId = value;
    }

    /**
     * Gets the value of the remoteDataMappings property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteDataMapping> getRemoteDataMappings() {
        return remoteDataMappings;
    }

    /**
     * Sets the value of the remoteDataMappings property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteDataMapping }{@code >}
     *     
     */
    public void setRemoteDataMappings(JAXBElement<ArrayOfRemoteDataMapping> value) {
        this.remoteDataMappings = ((JAXBElement<ArrayOfRemoteDataMapping> ) value);
    }

}
