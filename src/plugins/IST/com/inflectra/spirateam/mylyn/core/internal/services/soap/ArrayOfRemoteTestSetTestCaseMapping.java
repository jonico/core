
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfRemoteTestSetTestCaseMapping complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRemoteTestSetTestCaseMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RemoteTestSetTestCaseMapping" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteTestSetTestCaseMapping" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRemoteTestSetTestCaseMapping", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", propOrder = {
    "remoteTestSetTestCaseMapping"
})
public class ArrayOfRemoteTestSetTestCaseMapping {

    @XmlElement(name = "RemoteTestSetTestCaseMapping", nillable = true)
    protected List<RemoteTestSetTestCaseMapping> remoteTestSetTestCaseMapping;

    /**
     * Gets the value of the remoteTestSetTestCaseMapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the remoteTestSetTestCaseMapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRemoteTestSetTestCaseMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RemoteTestSetTestCaseMapping }
     * 
     * 
     */
    public List<RemoteTestSetTestCaseMapping> getRemoteTestSetTestCaseMapping() {
        if (remoteTestSetTestCaseMapping == null) {
            remoteTestSetTestCaseMapping = new ArrayList<RemoteTestSetTestCaseMapping>();
        }
        return this.remoteTestSetTestCaseMapping;
    }

}
