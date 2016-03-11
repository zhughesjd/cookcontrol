package net.joshuahughes.cookcontrol.enumproperties;

import java.awt.Color;


public class TemperatureAlert extends EnumProperties
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2908714710835616733L;
	private static int idIncr = 0;
	public TemperatureAlert()
	{
		labelOptions = new String[]{"alarm "+idIncr};
		put(StringKey.label,"alarm "+idIncr++);
		put(StringKey.email,"");
		put(FloatKey.maxtemperature,500);
		put(StringKey.color, EnumProperties.getString(Color.red));
		put(BooleanKey.light, true);
		put(BooleanKey.sound, true);
		put(BooleanKey.vibrate, true);
		init();
	}
	public String toString()
	{
		return get(StringKey.label);
	}
	
}
