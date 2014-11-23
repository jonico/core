
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
 *         &lt;element name="remoteRelease" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteRelease" minOccurs="0"/>
 *         &lt;element name="parentReleaseId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "remoteRelease",
    "parentReleaseId"
})
@XmlRootElement(name = "Release_Create")
public class ReleaseCreate {

    @XmlElementRef(name = "remoteRelease", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteRelease> remoteRelease;
    @XmlElementRef(name = "parentReleaseId", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<Integer> parentReleaseId;

    /**
     * Gets the value of the remoteRelease property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteRelease }{@code >}
     *     
     */
    public JAXBElement<RemoteRelease> getRemoteRelease() {
        return remoteRelease;
    }

    /**
     * Sets the value of the remoteRelease property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteRelease }{@code >}
     *     
     */
    public void setRemoteRelease(JAXBElement<RemoteRelease> value) {
        this.remoteRelease = ((JAXBElement<RemoteRelease> ) value);
    }

    /**
     * Gets the value of the parentReleaseId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getParentReleaseId() {
        return parentReleaseId;
    }

    /**
     * Sets the value of the parentReleaseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setParentReleaseId(JAXBElement<Integer> value) {
        this.parentReleaseId = ((JAXBElement<Integer> ) value);
    }

}
