package net.joshuahughes.smokercontroller;


import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import net.joshuahughes.smokercontroller.function.Function;
import net.joshuahughes.smokercontroller.function.Linear;

@SuppressWarnings("unchecked")
public class Parameters extends Properties
{
	private static final long serialVersionUID = -3548459685215964550L;
	public interface Key<T>{}
	public static enum LongKey implements Key<Long>{utctime,sleep}
	public static enum FloatKey implements Key<Float>{sensortemperature,lotemperature,temperaturerange,fanrpm}
	public static enum IntKey implements Key<Integer>{fantemperatureindex}
	public Function function = new Linear();
	public Thermometer sensorFeature;
	private TreeSet<Thermometer> thermometerSet = new TreeSet<>();
	private LinkedHashMap<Thermometer,Float> temperatureMap = new LinkedHashMap<>();
	public Parameters()
	{
		put(LongKey.sleep,1000l);
		put(IntKey.fantemperatureindex,0);
		put(FloatKey.lotemperature,200f);
		put(FloatKey.temperaturerange,50f);
	}
	public <T> T get(Key<T> key){return (T) super.get(key);}
	public <T> T put(Key<T> key,T value){return (T) super.put(key,value);}
	public Set<Entry<Thermometer, Float>> getTemperatureEntries()
	{
		return this.temperatureMap.entrySet();
	}
	public Float getLatestTemperature(int index)
	{
		return temperatureMap.get(getThermometer(index));
	}
	public Object put(Object key,Object value)
	{
		Entry<Key<Object>, Object> entry = getKeyValue(key.toString(),value.toString());
		Key<Object> enumKey = entry.getKey();
		Object enumValue = entry.getValue();
		return super.put(enumKey==null?key:enumKey, enumValue==null?value:enumValue);
	}
	public Object get( Object key)
	{
		Key<?> enumKey = getKeyValue(key.toString(),null).getKey();
		return super.get(enumKey==null?key:enumKey);
	}
	private static <T> Entry<Key<T>,T> getKeyValue(String stringKey,String stringValue) {
		Key<T> key = null;
		T value = null;
		try{key = (Key<T>) LongKey.valueOf(stringKey);value = (T)new Long(Long.parseLong(stringValue));}catch(Exception e){}
		try{key = (Key<T>) FloatKey.valueOf(stringKey);value = (T)new Float(Float.parseFloat(stringValue));}catch(Exception e){}
		try{key = (Key<T>) IntKey.valueOf(stringKey);value = (T)new Integer(Integer.parseInt(stringValue));}catch(Exception e){}
		return new AbstractMap.SimpleEntry<Key<T>,T>(key,value);
	}
	public void process(Entry<Float, LinkedHashMap<Integer, Float>> temperatures)
	{
		temperatureMap.clear();
		temperatureMap.put(getThermometer(-1), temperatures.getKey());
		for(Entry<Integer, Float> entry : temperatures.getValue().entrySet())
			temperatureMap.put(getThermometer(entry.getKey()),entry.getValue());
	}
	public Thermometer getThermometer(int index)
	{
		Thermometer thermometer = new Thermometer(index);
		if(thermometerSet.contains(thermometer))
			thermometer = thermometerSet.floor(thermometer);
		else
			thermometerSet.add(thermometer);
		return thermometer;
	}
	public class Thermometer implements Comparable<Thermometer>
	{
		public LinkedHashSet<TemperatureAlert> alertList = new LinkedHashSet<TemperatureAlert>();
		private String id;
		private int index;
		public Thermometer(int index)
		{
			id = index<0?"sensor":"probe "+index;
			this.index = -1;
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
			for(Thermometer thermometer : thermometerSet)
				if(thermometer.getId().equals(id))
					return false;
			this.id = id;
			return true;
		}
		@Override
		public int compareTo(Thermometer o) {
			return id.compareTo(o.id);
		}
		public String toString()
		{
			return id;
		}
	}
	public static class TemperatureAlert
	{
		private static int idIncr = 0;
		public static enum Type{light,vibrate,sound}
		public String id;
		public String email;
		public float temperature = 500;
		public LinkedHashSet<Type> typeList = new LinkedHashSet<Type>();
		public TemperatureAlert()
		{
			id = "alarm "+idIncr++;
		}
	}
}
