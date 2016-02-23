package net.joshuahughes.smokercontroller.controller;

import java.util.LinkedHashMap;

public class Parameters extends FanTemperatureControl
{
	public long utcTime;
	public float[] outsideTemp;
	public LinkedHashMap<Integer,Float> indexFahrenheitMap = new LinkedHashMap<>();
	public double fanControlTemp;
}
