
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
 *         &lt;element name="remoteTestSet" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteTestSet" minOccurs="0"/>
 *         &lt;element name="parentTestSetFolderId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "remoteTestSet",
    "parentTestSetFolderId"
})
@XmlRootElement(name = "TestSet_CreateFolder")
public class TestSetCreateFolder {

    @XmlElementRef(name = "remoteTestSet", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteTestSet> remoteTestSet;
    @XmlElementRef(name = "parentTestSetFolderId", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<Integer> parentTestSetFolderId;

    /**
     * Gets the value of the remoteTestSet property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteTestSet }{@code >}
     *     
     */
    public JAXBElement<RemoteTestSet> getRemoteTestSet() {
        return remoteTestSet;
    }

    /**
     * Sets the value of the remoteTestSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteTestSet }{@code >}
     *     
     */
    public void setRemoteTestSet(JAXBElement<RemoteTestSet> value) {
        this.remoteTestSet = ((JAXBElement<RemoteTestSet> ) value);
    }

    /**
     * Gets the value of the parentTestSetFolderId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getParentTestSetFolderId() {
        return parentTestSetFolderId;
    }

    /**
     * Sets the value of the parentTestSetFolderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setParentTestSetFolderId(JAXBElement<Integer> value) {
        this.parentTestSetFolderId = ((JAXBElement<Integer> ) value);
    }

}
