package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAttribute;

public class StringProperty extends Property{
	public static enum StringKey{label, email, color,macaddress}
	@XmlAttribute
	StringKey key;
	@XmlAttribute
	String value;
}
