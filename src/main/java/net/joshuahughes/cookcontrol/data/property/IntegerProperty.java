package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAttribute;

public class IntegerProperty extends Property{
	public static enum IntegerKey{fantemperatureindex,index}
	@XmlAttribute
	IntegerKey key;
	@XmlAttribute
	Integer value;
}
