
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
 *         &lt;element name="remoteTestCase" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteTestCase" minOccurs="0"/>
 *         &lt;element name="parentTestFolderId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "remoteTestCase",
    "parentTestFolderId"
})
@XmlRootElement(name = "TestCase_CreateFolder")
public class TestCaseCreateFolder {

    @XmlElementRef(name = "remoteTestCase", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteTestCase> remoteTestCase;
    @XmlElementRef(name = "parentTestFolderId", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<Integer> parentTestFolderId;

    /**
     * Gets the value of the remoteTestCase property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteTestCase }{@code >}
     *     
     */
    public JAXBElement<RemoteTestCase> getRemoteTestCase() {
        return remoteTestCase;
    }

    /**
     * Sets the value of the remoteTestCase property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteTestCase }{@code >}
     *     
     */
    public void setRemoteTestCase(JAXBElement<RemoteTestCase> value) {
        this.remoteTestCase = ((JAXBElement<RemoteTestCase> ) value);
    }

    /**
     * Gets the value of the parentTestFolderId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getParentTestFolderId() {
        return parentTestFolderId;
    }

    /**
     * Sets the value of the parentTestFolderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setParentTestFolderId(JAXBElement<Integer> value) {
        this.parentTestFolderId = ((JAXBElement<Integer> ) value);
    }

}
