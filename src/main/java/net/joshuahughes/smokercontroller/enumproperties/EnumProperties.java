package net.joshuahughes.smokercontroller.enumproperties;


import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Properties;

@SuppressWarnings("unchecked")
public class EnumProperties extends Properties
{
	private static final long serialVersionUID = -3548459685215964550L;
	public interface Key<T>{}
	public static enum LongKey implements Key<Long>{utctime,sleep}
	public static enum FloatKey implements Key<Float>{sensortemperature,mintemperature,fanrpm,maxtemperature}
	public static enum IntKey implements Key<Integer>{fantemperatureindex,probeindex}
	public static enum StringKey implements Key<String>{label, email, color}
	public static enum BooleanKey implements Key<Boolean>{light,vibrate,sound}
	public <T> T get(Key<T> key){return (T) super.get(key);}
	public <T> T put(Key<T> key,T value){return (T) super.put(key,value);}
	public <T> LinkedHashMap<Key<T>,T> getSubMap(Class<T> clazz)
	{
		LinkedHashMap<Key<T>,T> map = new LinkedHashMap<>();
		for(Entry<Object, Object> entry : this.entrySet())
			if(clazz.equals(entry.getKey().getClass().getGenericInterfaces()[0]))
				map.put((Key<T>)entry.getKey(), (T)entry.getValue());
		return map;
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
}
