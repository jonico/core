
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
 *         &lt;element name="remoteDocumentVersion" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteDocumentVersion" minOccurs="0"/>
 *         &lt;element name="binaryData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="makeCurrent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
    "remoteDocumentVersion",
    "binaryData",
    "makeCurrent"
})
@XmlRootElement(name = "Document_AddFileVersion")
public class DocumentAddFileVersion {

    @XmlElementRef(name = "remoteDocumentVersion", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteDocumentVersion> remoteDocumentVersion;
    @XmlElementRef(name = "binaryData", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<byte[]> binaryData;
    protected Boolean makeCurrent;

    /**
     * Gets the value of the remoteDocumentVersion property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteDocumentVersion }{@code >}
     *     
     */
    public JAXBElement<RemoteDocumentVersion> getRemoteDocumentVersion() {
        return remoteDocumentVersion;
    }

    /**
     * Sets the value of the remoteDocumentVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteDocumentVersion }{@code >}
     *     
     */
    public void setRemoteDocumentVersion(JAXBElement<RemoteDocumentVersion> value) {
        this.remoteDocumentVersion = ((JAXBElement<RemoteDocumentVersion> ) value);
    }

    /**
     * Gets the value of the binaryData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public JAXBElement<byte[]> getBinaryData() {
        return binaryData;
    }

    /**
     * Sets the value of the binaryData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link byte[]}{@code >}
     *     
     */
    public void setBinaryData(JAXBElement<byte[]> value) {
        this.binaryData = ((JAXBElement<byte[]> ) value);
    }

    /**
     * Gets the value of the makeCurrent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMakeCurrent() {
        return makeCurrent;
    }

    /**
     * Sets the value of the makeCurrent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMakeCurrent(Boolean value) {
        this.makeCurrent = value;
    }

}
