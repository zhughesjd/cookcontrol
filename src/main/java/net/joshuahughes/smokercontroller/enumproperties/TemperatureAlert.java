package net.joshuahughes.smokercontroller.enumproperties;

public class TemperatureAlert extends EnumProperties
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2908714710835616733L;
	private static int idIncr = 0;
	public TemperatureAlert()
	{
		put(StringKey.label,"alarm "+idIncr++);
		put(StringKey.email,null);
		put(FloatKey.maxtemperature,500);
		put(BooleanKey.light, true);
		put(BooleanKey.sound, true);
		put(BooleanKey.vibrate, true);
	}
}
