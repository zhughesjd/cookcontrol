package net.joshuahughes.smokercontroller.controller;

import java.util.LinkedHashMap;

public class Parameters extends FanTemperatureControl
{
	public long utcTime;
	public double internalFahrenheit;
	public LinkedHashMap<Integer,Double> indexFahrenheitMap = new LinkedHashMap<>();
}
