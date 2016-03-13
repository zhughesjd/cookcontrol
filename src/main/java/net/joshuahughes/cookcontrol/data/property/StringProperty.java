package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;
import net.joshuahughes.cookcontrol.data.property.StringProperty.StringKey;

@XmlAccessorType(XmlAccessType.FIELD)
public class StringProperty extends Property<String,StringKey>{
	public static enum StringKey implements Key<String>{label, email, color,macaddress}
	@XmlAttribute
	StringKey key;
	@XmlAttribute
	String value;
	public StringKey getKey() {
		return key;
	}
	public void setKey(StringKey key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
