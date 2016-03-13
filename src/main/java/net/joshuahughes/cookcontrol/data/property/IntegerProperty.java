package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;

@XmlAccessorType(XmlAccessType.FIELD)
public class IntegerProperty extends Property<Integer>{
	public static enum IntegerKey implements Key<Integer>{fantemperatureindex,index}
	@XmlAttribute
	IntegerKey key;
	@XmlAttribute
	Integer value;
	public IntegerKey getKey() {
		return key;
	}
	public void setKey(IntegerKey key) {
		this.key = key;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}
