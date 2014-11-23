
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
 *         &lt;element name="remoteProject" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteProject" minOccurs="0"/>
 *         &lt;element name="existingProjectId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "remoteProject",
    "existingProjectId"
})
@XmlRootElement(name = "Project_Create")
public class ProjectCreate {

    @XmlElementRef(name = "remoteProject", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteProject> remoteProject;
    @XmlElementRef(name = "existingProjectId", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<Integer> existingProjectId;

    /**
     * Gets the value of the remoteProject property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteProject }{@code >}
     *     
     */
    public JAXBElement<RemoteProject> getRemoteProject() {
        return remoteProject;
    }

    /**
     * Sets the value of the remoteProject property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteProject }{@code >}
     *     
     */
    public void setRemoteProject(JAXBElement<RemoteProject> value) {
        this.remoteProject = ((JAXBElement<RemoteProject> ) value);
    }

    /**
     * Gets the value of the existingProjectId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getExistingProjectId() {
        return existingProjectId;
    }

    /**
     * Sets the value of the existingProjectId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setExistingProjectId(JAXBElement<Integer> value) {
        this.existingProjectId = ((JAXBElement<Integer> ) value);
    }

}
