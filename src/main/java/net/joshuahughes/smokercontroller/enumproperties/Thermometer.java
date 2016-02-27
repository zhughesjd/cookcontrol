package net.joshuahughes.smokercontroller.enumproperties;

import java.util.LinkedHashSet;
import java.util.TreeSet;


public class Thermometer extends EnumProperties implements Comparable<Thermometer>{
	private static final long serialVersionUID = 2781070028094088016L;
	public LinkedHashSet<TemperatureAlert> alertList = new LinkedHashSet<TemperatureAlert>();
	private String id;
	private int index;
	private TreeSet<Thermometer> set;
	public Thermometer(int index, TreeSet<Thermometer> thermometerSet)
	{
		this.set = thermometerSet;
		id = index<0?"sensor":"probe "+index;
		this.index = Math.max(index, -1);
	}
	public int getIndex()
	{
		return index;
	}
	public String getId()
	{
		return id;
	}
	public boolean setId(String id)
	{
		for(Thermometer thermometer : set)
			if(thermometer.getId().equals(id))
				return false;
		this.id = id;
		return true;
	}
	@Override
	public int compareTo(Thermometer o) {
		return Integer.compare(index, o.index);
	}
	public String toString()
	{
		return id;
	}
	public int hashCode()
	{
		return index;
	}
	public boolean equals(Object object)
	{
		if(!(object instanceof Thermometer)) return false;
		return id == ((Thermometer)object).id;
	}
}
