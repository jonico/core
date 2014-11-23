
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfRemoteWorkflowIncidentCustomProperties complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRemoteWorkflowIncidentCustomProperties">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RemoteWorkflowIncidentCustomProperties" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteWorkflowIncidentCustomProperties" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRemoteWorkflowIncidentCustomProperties", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", propOrder = {
    "remoteWorkflowIncidentCustomProperties"
})
public class ArrayOfRemoteWorkflowIncidentCustomProperties {

    @XmlElement(name = "RemoteWorkflowIncidentCustomProperties", nillable = true)
    protected List<RemoteWorkflowIncidentCustomProperties> remoteWorkflowIncidentCustomProperties;

    /**
     * Gets the value of the remoteWorkflowIncidentCustomProperties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the remoteWorkflowIncidentCustomProperties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRemoteWorkflowIncidentCustomProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RemoteWorkflowIncidentCustomProperties }
     * 
     * 
     */
    public List<RemoteWorkflowIncidentCustomProperties> getRemoteWorkflowIncidentCustomProperties() {
        if (remoteWorkflowIncidentCustomProperties == null) {
            remoteWorkflowIncidentCustomProperties = new ArrayList<RemoteWorkflowIncidentCustomProperties>();
        }
        return this.remoteWorkflowIncidentCustomProperties;
    }

}
