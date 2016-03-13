package net.joshuahughes.cookcontrol.data.property;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;

@XmlAccessorType(XmlAccessType.FIELD)
public class DateProperty extends Property<Date>{
	public static enum DateKey implements Key<Date>{creation}
	@XmlAttribute
	DateKey key;
	@XmlAttribute
	Date value;
	
	public DateKey getKey() {
		return key;
	}
	public void setKey(DateKey key) {
		this.key = key;
	}
	public Date getValue() {
		return value;
	}
	public void setValue(Date value) {
		this.value = value;
	}
}
