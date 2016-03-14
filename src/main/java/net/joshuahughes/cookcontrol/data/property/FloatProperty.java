package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;
import net.joshuahughes.cookcontrol.data.property.FloatProperty.FloatKey;

@XmlAccessorType(XmlAccessType.FIELD)
public class FloatProperty extends Property<Float,FloatKey>{
	public static enum FloatKey implements Key<Float>{sensortemperature,mintemperature,fanrpm,maxtemperature}
	@XmlAttribute
	FloatKey key;
	@XmlAttribute
	Float value;
	public FloatProperty(){}
	public FloatProperty(FloatKey key, Float value) {
		this.key = key;
		this.value = value;
	}

	public FloatKey getKey() {
		return key;
	}
	public FloatKey setKey(FloatKey newKey) {
		FloatKey oldKey = this.key;
		this.key = newKey;
		return oldKey;
	}
	public Float getValue() {
		return value;
	}
	public Float setValue(Float newValue) {
		Float oldValue = this.value;
		this.value = newValue;
		return oldValue;
	}
}
