package net.joshuahughes.smokercontroller;

import java.util.LinkedHashMap;
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
	public Parameters()
	{
		put(LongKey.sleep,10000);
		put(IntKey.fantemperatureindex,0);
	}
	public <T> T get(Key<T> key){return (T) super.get(key);}
	public <T> T put(Key<T> key,T value){return (T) super.put(key,value);}
	public Object put(Object key,Object value)
	{
		Key<?> enumKey = getKey(key.toString());
		return super.put(enumKey==null?key:enumKey, value);
	}
	public Object get( Object key)
	{
		return super.get(key);
	}
	public LinkedHashMap<Integer,Float> indexTemperatureMap = new LinkedHashMap<>();
	public Function function = new Linear();
	private static Key<?> getKey(String string) {
		try{return LongKey.valueOf(string);}catch(Exception e){}
		try{return FloatArrayKey.valueOf(string);}catch(Exception e){}
		try{return FloatKey.valueOf(string);}catch(Exception e){}
		try{return IntKey.valueOf(string);}catch(Exception e){}
		return null;
	}
}
