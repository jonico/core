
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
 *         &lt;element name="remoteFilters" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteFilter" minOccurs="0"/>
 *         &lt;element name="remoteSort" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteSort" minOccurs="0"/>
 *         &lt;element name="startingRow" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="numberOfRows" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "remoteFilters",
    "remoteSort",
    "startingRow",
    "numberOfRows"
})
@XmlRootElement(name = "TestRun_RetrieveAutomated")
public class TestRunRetrieveAutomated {

    @XmlElementRef(name = "remoteFilters", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteFilter> remoteFilters;
    @XmlElementRef(name = "remoteSort", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteSort> remoteSort;
    protected Integer startingRow;
    protected Integer numberOfRows;

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
     * Gets the value of the numberOfRows property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Sets the value of the numberOfRows property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfRows(Integer value) {
        this.numberOfRows = value;
    }

}
