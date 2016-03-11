package net.joshuahughes.cookcontrol.function;

public class Linear implements Function{

	@Override
	public float normalize(float min,float max,float value)
	{
		return 1f - (value-min)/(max-min);
	}

}
