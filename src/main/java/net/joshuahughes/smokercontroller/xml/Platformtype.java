//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.09 at 07:19:47 PM EST 
//


package net.joshuahughes.smokercontroller.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for platformtype complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="platformtype">
 *   &lt;complexContent>
 *     &lt;extension base="{}type">
 *       &lt;sequence>
 *         &lt;element name="smoke" type="{}smoketype" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "platformtype", propOrder = {
    "smoke"
})
public class Platformtype
    extends Type
{

    protected List<Smoketype> smoke;

    /**
     * Gets the value of the smoke property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smoke property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSmoke().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Smoketype }
     * 
     * 
     */
    public List<Smoketype> getSmoke() {
        if (smoke == null) {
            smoke = new ArrayList<Smoketype>();
        }
        return this.smoke;
    }

}