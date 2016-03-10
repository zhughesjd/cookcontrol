//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.10 at 08:01:30 AM EST 
//


package net.joshuahughes.smokercontroller.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.joshuahughes.smokercontroller.xml package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Cook_QNAME = new QName("", "cook");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.joshuahughes.smokercontroller.xml
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Type }
     * 
     */
    public Type createType() {
        return new Type();
    }

    /**
     * Create an instance of {@link Cooktype }
     * 
     */
    public Cooktype createCooktype() {
        return new Cooktype();
    }

    /**
     * Create an instance of {@link Alerttype }
     * 
     */
    public Alerttype createAlerttype() {
        return new Alerttype();
    }

    /**
     * Create an instance of {@link Thermometertype }
     * 
     */
    public Thermometertype createThermometertype() {
        return new Thermometertype();
    }

    /**
     * Create an instance of {@link Type.Comment }
     * 
     */
    public Type.Comment createTypeComment() {
        return new Type.Comment();
    }

    /**
     * Create an instance of {@link Type.Property }
     * 
     */
    public Type.Property createTypeProperty() {
        return new Type.Property();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Cooktype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "cook")
    public JAXBElement<Cooktype> createCook(Cooktype value) {
        return new JAXBElement<Cooktype>(_Cook_QNAME, Cooktype.class, null, value);
    }

}
