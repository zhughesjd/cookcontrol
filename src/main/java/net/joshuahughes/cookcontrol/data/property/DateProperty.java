package net.joshuahughes.cookcontrol.data.property;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;
import net.joshuahughes.cookcontrol.data.property.DateProperty.DateKey;

@XmlAccessorType(XmlAccessType.FIELD)
public class DateProperty extends Property<Date,DateKey>{
	public static enum DateKey implements Key<Date>{creation}
	@XmlAttribute
	DateKey key;
	@XmlAttribute
	Date value;
	public DateProperty(){}
	public DateProperty(DateKey key,Date value) {
		this.key = key;
		this.value = value;
	}
	public DateKey getKey() {
		return key;
	}
	public DateKey setKey(DateKey newKey) {
		DateKey oldKey = this.key;
		this.key = newKey;
		return oldKey;
	}
	public Date getValue() {
		return value;
	}
	public Date setValue(Date newValue) {
		Date oldValue = this.value;
		this.value = newValue;
		return oldValue;
	}
}
