package net.joshuahughes.smokercontroller.swing;

import java.util.ArrayList;

import net.joshuahughes.smokercontroller.xml.Thermometertype;
import net.joshuahughes.smokercontroller.xml.Type;

public class Parameters<P extends Type,C extends Type> {
	public interface Key<T>{public T fromString(String s);}
	public static enum LongKey implements Key<Long>{utctime,sleep;public Long fromString(String s){return Long.valueOf(s);}}
	public static enum IntKey implements Key<Integer>{fantemperatureindex,index;public Integer fromString(String s){return Integer.valueOf(s);}}
	public static enum FloatKey implements Key<Float>{sensortemperature,mintemperature,fanrpm,maxtemperature;public Float fromString(String s){return Float.valueOf(s);}}
	public static enum StringKey implements Key<String>{label, email, color,macaddress;public String fromString(String s){return String.valueOf(s);}}
	public static enum BooleanKey implements Key<Boolean>{light,vibrate,sound;public Boolean fromString(String s){return Boolean.valueOf(s);}}
	public static int idIncr = 0;
	public static String parametersFileName = Parameters.class.getSimpleName().toLowerCase()+".txt";
	private ArrayList<C> children;
	public Parameters(P parent)
	{
		children = getList(parent);
	}
	@SuppressWarnings("unchecked")
	private static <P extends Type,C extends Type> ArrayList<C> getList(P parent) {
		ArrayList<C> list = new ArrayList<C>();
		if(parent.getClass().equals(Thermometertype.class))
		{
			list = (ArrayList<C>) ((Thermometertype)parent).getAlert();
		}
		return list;
	}
}
