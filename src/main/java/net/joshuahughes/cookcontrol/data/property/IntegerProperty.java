package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;
import net.joshuahughes.cookcontrol.data.property.IntegerProperty.IntegerKey;

@XmlAccessorType(XmlAccessType.FIELD)
public class IntegerProperty extends Property<Integer,IntegerKey>{
	public static enum IntegerKey implements Key<Integer>{fantemperatureindex,index}
	@XmlAttribute
	IntegerKey key;
	@XmlAttribute
	Integer value;
	public IntegerProperty(){}
	public IntegerProperty(IntegerKey key, Integer value, boolean editable) {
		super(editable);
		this.key = key;
		this.value = value;
	}
	public IntegerKey getKey() {
		return key;
	}
	public IntegerKey setKey(IntegerKey newKey) {
		IntegerKey oldKey = this.key;
		this.key = newKey;
		return oldKey;
	}
	public Integer getValue() {
		return value;
	}
	public Integer setValue(Integer newValue) {
		Integer oldValue = this.value;
		this.value = newValue;
		return oldValue;
	}
}
