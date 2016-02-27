package net.joshuahughes.smokercontroller.enumproperties;

import java.util.LinkedHashSet;

public class TemperatureAlert extends EnumProperties
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2908714710835616733L;
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
