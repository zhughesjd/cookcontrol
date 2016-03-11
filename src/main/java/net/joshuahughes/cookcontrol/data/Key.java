package net.joshuahughes.cookcontrol.data;

import javax.xml.bind.annotation.XmlType;


@XmlType(name="simpleType.Generic.ProductReferenceType")
public enum Key {
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

}
