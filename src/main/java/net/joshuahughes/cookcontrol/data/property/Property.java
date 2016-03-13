package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Property<T>{
	@XmlAttribute
	boolean editable;
	
	public abstract Key<T> getKey();
	public abstract T getValue();
	public int hashCode()
	{
		return getKey().hashCode();
	}
	public boolean equals(Object object)
	{
		if(object == null || !object.getClass().equals(getClass())) return false;
		Property<?> property = (Property<?>) object;
		return getKey().equals(property.getKey());
	}
}
