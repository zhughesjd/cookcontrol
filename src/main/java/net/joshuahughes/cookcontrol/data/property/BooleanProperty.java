package net.joshuahughes.cookcontrol.data.property;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;
import net.joshuahughes.cookcontrol.data.property.BooleanProperty.BooleanKey;;

@XmlAccessorType(XmlAccessType.FIELD)
public class BooleanProperty extends Property<Boolean,BooleanKey>{
	public static enum BooleanKey implements Key<Boolean>{light,vibrate,sound}
	@XmlAttribute
	BooleanKey key;
	@XmlAttribute
	Boolean value;
	public BooleanProperty(){}
	public BooleanProperty(BooleanKey key,Boolean value) {
		this.key = key;
		this.value = value;
	}
	public BooleanKey getKey() {
		return key;
	}
	public BooleanKey setKey(BooleanKey newKey) {
		BooleanKey oldKey = this.key;
		this.key = newKey;
		return oldKey;
	}
	public Boolean getValue() {
		return value;
	}
	public Boolean setValue(Boolean newValue) {
		Boolean oldValue = this.value;
		this.value = newValue;
		return oldValue;
	}
}
