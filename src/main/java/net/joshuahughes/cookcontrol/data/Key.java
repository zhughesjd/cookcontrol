package net.joshuahughes.cookcontrol.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

@XmlType(name="simpleType.Generic.ProductReferenceType")
public enum Key {

	creation(Date.class),

	sensortemperature(Float.class),
	mintemperature(Float.class),
	fanrpm(Float.class),
	maxtemperature(Float.class),

	index(Integer.class),
	sleep(Long.class),

	label(String.class),
	email(String.class),
	macaddress(String.class),
	color(String.class),

	light(Boolean.class),
	sound(Boolean.class),
	vibrate(Boolean.class);

	Class<?> clazz;
	Key(Class<?> clazz)
	{
		this.clazz = clazz;
	}
	static DatatypeFactory factory = null;
	static{
		try {
			factory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static <V> V fromString(Key key, String valueString)
	{
		if(key.getClass().equals(String.class)) return (V) valueString;
		if(key.getClass().equals(Long.class)) return (V)(Long)Long.parseLong(valueString);
		if(key.getClass().equals(Integer.class)) return (V)(Integer)Integer.parseInt(valueString);
		if(key.getClass().equals(Float.class)) return (V)(Float)Float.parseFloat(valueString);
		if(key.getClass().equals(Boolean.class)) return (V)(Boolean)Boolean.parseBoolean(valueString);
		if(key.getClass().equals(Date.class)) return (V)new Date(Date.parse(valueString));
		return null;
	}
}
