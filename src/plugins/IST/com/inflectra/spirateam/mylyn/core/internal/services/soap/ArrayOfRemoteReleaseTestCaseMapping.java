
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfRemoteReleaseTestCaseMapping complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRemoteReleaseTestCaseMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RemoteReleaseTestCaseMapping" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteReleaseTestCaseMapping" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRemoteReleaseTestCaseMapping", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", propOrder = {
    "remoteReleaseTestCaseMapping"
})
public class ArrayOfRemoteReleaseTestCaseMapping {

    @XmlElement(name = "RemoteReleaseTestCaseMapping", nillable = true)
    protected List<RemoteReleaseTestCaseMapping> remoteReleaseTestCaseMapping;

    /**
     * Gets the value of the remoteReleaseTestCaseMapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the remoteReleaseTestCaseMapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRemoteReleaseTestCaseMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RemoteReleaseTestCaseMapping }
     * 
     * 
     */
    public List<RemoteReleaseTestCaseMapping> getRemoteReleaseTestCaseMapping() {
        if (remoteReleaseTestCaseMapping == null) {
            remoteReleaseTestCaseMapping = new ArrayList<RemoteReleaseTestCaseMapping>();
        }
        return this.remoteReleaseTestCaseMapping;
    }

}
