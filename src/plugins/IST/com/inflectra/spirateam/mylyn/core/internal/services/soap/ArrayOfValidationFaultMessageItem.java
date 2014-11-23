
package com.inflectra.spirateam.mylyn.core.internal.services.soap;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfValidationFaultMessageItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfValidationFaultMessageItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ValidationFaultMessageItem" type="{http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0}ValidationFaultMessageItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfValidationFaultMessageItem", namespace = "http://schemas.datacontract.org/2004/07/Inflectra.SpiraTest.Web.Services.v4_0", propOrder = {
    "validationFaultMessageItem"
})
public class ArrayOfValidationFaultMessageItem {

    @XmlElement(name = "ValidationFaultMessageItem")
    protected List<ValidationFaultMessageItem> validationFaultMessageItem;

    /**
     * Gets the value of the validationFaultMessageItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the validationFaultMessageItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValidationFaultMessageItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValidationFaultMessageItem }
     * 
     * 
     */
    public List<ValidationFaultMessageItem> getValidationFaultMessageItem() {
        if (validationFaultMessageItem == null) {
            validationFaultMessageItem = new ArrayList<ValidationFaultMessageItem>();
        }
        return this.validationFaultMessageItem;
    }

}
