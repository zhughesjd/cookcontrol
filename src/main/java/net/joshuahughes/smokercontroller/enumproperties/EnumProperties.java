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
	public LinkedHashMap<Long,String> timeCommentMap = new LinkedHashMap<>();
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
