package net.joshuahughes.smokercontroller;


import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Properties;

import net.joshuahughes.smokercontroller.function.Function;
import net.joshuahughes.smokercontroller.function.Linear;

@SuppressWarnings("unchecked")
public class Parameters extends Properties
{
	private static final long serialVersionUID = -3548459685215964550L;
	public interface Key<T>{}
	public static enum LongKey implements Key<Long>{utctime,sleep}
	public static enum FloatArrayKey implements Key<float[]>{temperatures}
	public static enum FloatKey implements Key<Float>{lotemperature,temperaturerange,fanrpm}
	public static enum IntKey implements Key<Integer>{fantemperatureindex}
	public Function function = new Linear();
	public Parameters()
	{
		put(LongKey.sleep,10000l);
		put(IntKey.fantemperatureindex,0);
		put(FloatKey.lotemperature,200f);
		put(FloatKey.temperaturerange,50f);
	}
	public <T> T get(Key<T> key){return (T) super.get(key);}
	public <T> T put(Key<T> key,T value){return (T) super.put(key,value);}
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
	public LinkedHashMap<Integer,Float> indexTemperatureMap = new LinkedHashMap<>();
	private static <T> Entry<Key<T>,T> getKeyValue(String stringKey,String stringValue) {
		Key<T> key = null;
		T value = null;
		try{key = (Key<T>) LongKey.valueOf(stringKey);value = (T)new Long(Long.parseLong(stringValue));}catch(Exception e){}
		try{key = (Key<T>) FloatKey.valueOf(stringKey);value = (T)new Float(Float.parseFloat(stringValue));}catch(Exception e){}
		try{key = (Key<T>) IntKey.valueOf(stringKey);value = (T)new Integer(Integer.parseInt(stringValue));}catch(Exception e){}
		try{key = (Key<T>) FloatArrayKey.valueOf(stringKey);}catch(Exception e){}
		return new AbstractMap.SimpleEntry<Key<T>,T>(key,value);
	}
}
