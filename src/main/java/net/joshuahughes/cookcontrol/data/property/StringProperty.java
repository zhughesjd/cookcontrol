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
	public StringProperty() {
	}
	public StringProperty(StringKey key, String value,boolean editable) {
		super(editable);
		this.key = key;
		this.value = value;
	}
	public StringKey getKey() {
		return key;
	}
	public StringKey setKey(StringKey newKey) {
		StringKey oldKey = this.key;
		this.key = newKey;
		return oldKey;
	}
	public String getValue() {
		return value;
	}
	public String setValue(String newValue) {
		String oldValue = this.value;
		this.value = newValue;
		return oldValue;
	}
}
