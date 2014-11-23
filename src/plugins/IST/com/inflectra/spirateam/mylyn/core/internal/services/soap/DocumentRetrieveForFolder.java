
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
 *         &lt;element name="folderId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="remoteFilters" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteFilter" minOccurs="0"/>
 *         &lt;element name="remoteSort" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteSort" minOccurs="0"/>
 *         &lt;element name="startingRow" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="numberRows" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "folderId",
    "remoteFilters",
    "remoteSort",
    "startingRow",
    "numberRows"
})
@XmlRootElement(name = "Document_RetrieveForFolder")
public class DocumentRetrieveForFolder {

    protected Integer folderId;
    @XmlElementRef(name = "remoteFilters", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteFilter> remoteFilters;
    @XmlElementRef(name = "remoteSort", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteSort> remoteSort;
    protected Integer startingRow;
    protected Integer numberRows;

    /**
     * Gets the value of the folderId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFolderId() {
        return folderId;
    }

    /**
     * Sets the value of the folderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFolderId(Integer value) {
        this.folderId = value;
    }

    /**
     * Gets the value of the remoteFilters property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteFilter> getRemoteFilters() {
        return remoteFilters;
    }

    /**
     * Sets the value of the remoteFilters property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteFilter }{@code >}
     *     
     */
    public void setRemoteFilters(JAXBElement<ArrayOfRemoteFilter> value) {
        this.remoteFilters = ((JAXBElement<ArrayOfRemoteFilter> ) value);
    }

    /**
     * Gets the value of the remoteSort property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}
     *     
     */
    public JAXBElement<RemoteSort> getRemoteSort() {
        return remoteSort;
    }

    /**
     * Sets the value of the remoteSort property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteSort }{@code >}
     *     
     */
    public void setRemoteSort(JAXBElement<RemoteSort> value) {
        this.remoteSort = ((JAXBElement<RemoteSort> ) value);
    }

    /**
     * Gets the value of the startingRow property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStartingRow() {
        return startingRow;
    }

    /**
     * Sets the value of the startingRow property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStartingRow(Integer value) {
        this.startingRow = value;
    }

    /**
     * Gets the value of the numberRows property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberRows() {
        return numberRows;
    }

    /**
     * Sets the value of the numberRows property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberRows(Integer value) {
        this.numberRows = value;
    }

}
