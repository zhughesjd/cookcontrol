package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;

@XmlAccessorType(XmlAccessType.FIELD)
public class FloatProperty extends Property<Float>{
	public static enum FloatKey implements Key<Float>{sensortemperature,mintemperature,fanrpm,maxtemperature}
	@XmlAttribute
	FloatKey key;
	@XmlAttribute
	Float value;
	public FloatKey getKey() {
		return key;
	}
	public void setKey(FloatKey key) {
		this.key = key;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
}
