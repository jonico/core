
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
 *         &lt;element name="remoteTestSetTestCaseMapping" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteTestSetTestCaseMapping" minOccurs="0"/>
 *         &lt;element name="existingTestSetTestCaseId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="parameters" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}ArrayOfRemoteTestSetTestCaseParameter" minOccurs="0"/>
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
    "remoteTestSetTestCaseMapping",
    "existingTestSetTestCaseId",
    "parameters"
})
@XmlRootElement(name = "TestSet_AddTestMapping")
public class TestSetAddTestMapping {

    @XmlElementRef(name = "remoteTestSetTestCaseMapping", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<RemoteTestSetTestCaseMapping> remoteTestSetTestCaseMapping;
    @XmlElementRef(name = "existingTestSetTestCaseId", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<Integer> existingTestSetTestCaseId;
    @XmlElementRef(name = "parameters", namespace = "http://www.inflectra.com/SpiraTest/Services/v4.0/", type = JAXBElement.class)
    protected JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> parameters;

    /**
     * Gets the value of the remoteTestSetTestCaseMapping property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemoteTestSetTestCaseMapping }{@code >}
     *     
     */
    public JAXBElement<RemoteTestSetTestCaseMapping> getRemoteTestSetTestCaseMapping() {
        return remoteTestSetTestCaseMapping;
    }

    /**
     * Sets the value of the remoteTestSetTestCaseMapping property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemoteTestSetTestCaseMapping }{@code >}
     *     
     */
    public void setRemoteTestSetTestCaseMapping(JAXBElement<RemoteTestSetTestCaseMapping> value) {
        this.remoteTestSetTestCaseMapping = ((JAXBElement<RemoteTestSetTestCaseMapping> ) value);
    }

    /**
     * Gets the value of the existingTestSetTestCaseId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getExistingTestSetTestCaseId() {
        return existingTestSetTestCaseId;
    }

    /**
     * Sets the value of the existingTestSetTestCaseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setExistingTestSetTestCaseId(JAXBElement<Integer> value) {
        this.existingTestSetTestCaseId = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the parameters property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseParameter }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRemoteTestSetTestCaseParameter }{@code >}
     *     
     */
    public void setParameters(JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> value) {
        this.parameters = ((JAXBElement<ArrayOfRemoteTestSetTestCaseParameter> ) value);
    }

}
