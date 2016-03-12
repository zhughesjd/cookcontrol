package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAttribute;

public class FloatProperty extends Property{
	public static enum FloatKey{sensortemperature,mintemperature,fanrpm,maxtemperature}
	@XmlAttribute
	FloatKey key;
	@XmlAttribute
	Float value;
}
