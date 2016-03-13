package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;

@XmlAccessorType(XmlAccessType.FIELD)
public class BooleanProperty extends Property<Boolean>{
	public static enum BooleanKey implements Key<Boolean>{light,vibrate,sound}
	@XmlAttribute
	BooleanKey key;
	@XmlAttribute
	Boolean value;
	public BooleanKey getKey() {
		return key;
	}
	public void setKey(BooleanKey key) {
		this.key = key;
	}
	public Boolean getValue() {
		return value;
	}
	public void setValue(Boolean value) {
		this.value = value;
	}
}
