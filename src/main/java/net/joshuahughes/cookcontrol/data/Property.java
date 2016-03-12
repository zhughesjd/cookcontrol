package net.joshuahughes.cookcontrol.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property")
public class Property{
	@XmlAttribute
	Key key;
	@XmlAttribute
	String value;
	@XmlAttribute
	boolean editable;
	
}
