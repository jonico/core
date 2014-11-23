
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfRemoteWorkflowIncidentTransition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRemoteWorkflowIncidentTransition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RemoteWorkflowIncidentTransition" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects}RemoteWorkflowIncidentTransition" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRemoteWorkflowIncidentTransition", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0.DataObjects", propOrder = {
    "remoteWorkflowIncidentTransition"
})
public class ArrayOfRemoteWorkflowIncidentTransition {

    @XmlElement(name = "RemoteWorkflowIncidentTransition", nillable = true)
    protected List<RemoteWorkflowIncidentTransition> remoteWorkflowIncidentTransition;

    /**
     * Gets the value of the remoteWorkflowIncidentTransition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the remoteWorkflowIncidentTransition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRemoteWorkflowIncidentTransition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RemoteWorkflowIncidentTransition }
     * 
     * 
     */
    public List<RemoteWorkflowIncidentTransition> getRemoteWorkflowIncidentTransition() {
        if (remoteWorkflowIncidentTransition == null) {
            remoteWorkflowIncidentTransition = new ArrayList<RemoteWorkflowIncidentTransition>();
        }
        return this.remoteWorkflowIncidentTransition;
    }

}
