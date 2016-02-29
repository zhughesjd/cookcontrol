package net.joshuahughes.smokercontroller.enumproperties;

import java.awt.Color;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.TreeSet;


public class Thermometer extends EnumProperties implements Comparable<Thermometer>{
	public static Random random = new Random(34234928374l);
	private static final long serialVersionUID = 2781070028094088016L;
	public LinkedHashSet<TemperatureAlert> alertSet = new LinkedHashSet<TemperatureAlert>();
	private TreeSet<Thermometer> set;
	public Thermometer(int index, TreeSet<Thermometer> thermometerSet)
	{
		set = thermometerSet;
		put(StringKey.color,EnumProperties.getString(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256))));
		put(StringKey.label,index<0?"sensor":"probe "+index);
		put(IntKey.probeindex,Math.max(index, -1));
	}
	public int getIndex()
	{
		return get(IntKey.probeindex);
	}
	public boolean setLabel(String label)
	{
		for(Thermometer thermometer : set)
			if(thermometer.get(StringKey.label).equals(label))
				return false;
		put(StringKey.label,label);
		return true;
	}
	@Override
	public int compareTo(Thermometer o) {
		return Integer.compare(get(IntKey.probeindex), o.get(IntKey.probeindex));
	}
	public String toString()
	{
		return get(StringKey.label);
	}
	public int hashCode()
	{
		return this.get(IntKey.probeindex);
	}
	public boolean equals(Object object)
	{
		if(!(object instanceof Thermometer)) return false;
		return get(StringKey.label).equals(((Thermometer)object).get(StringKey.label));
	}
}
