package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Property<V,K extends Key<V>>{
	@XmlAttribute
	boolean editable = true;
	public Property(){}
	public Property(boolean editable)
	{
		this.editable=editable;
	}
	public abstract K getKey();
	public abstract V getValue();
	public abstract K setKey(K key);
	public abstract V setValue(V value);
	public int hashCode()
	{
		return getKey().hashCode();
	}
	public boolean equals(Object object)
	{
		if(object == null || !object.getClass().equals(getClass())) return false;
		Property<?,?> property = (Property<?,?>) object;
		return getKey().equals(property.getKey());
	}
}
