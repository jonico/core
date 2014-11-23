
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
 *         &lt;element name="Connection_ConnectToProjectResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
    "connectionConnectToProjectResult"
})
@XmlRootElement(name = "Connection_ConnectToProjectResponse")
public class ConnectionConnectToProjectResponse {

    @XmlElement(name = "Connection_ConnectToProjectResult")
    protected Boolean connectionConnectToProjectResult;

    /**
     * Gets the value of the connectionConnectToProjectResult property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConnectionConnectToProjectResult() {
        return connectionConnectToProjectResult;
    }

    /**
     * Sets the value of the connectionConnectToProjectResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConnectionConnectToProjectResult(Boolean value) {
        this.connectionConnectToProjectResult = value;
    }

}
