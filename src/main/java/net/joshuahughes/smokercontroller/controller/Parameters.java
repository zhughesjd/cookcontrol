package net.joshuahughes.smokercontroller.controller;

import java.util.LinkedHashMap;

import net.joshuahughes.smokercontroller.function.Function;

public class Parameters
{
	public long utcTime;
	public float[] outsideTemp;
	public LinkedHashMap<Integer,Float> indexFahrenheitMap = new LinkedHashMap<>();
	public int fanTemperatureIndex;
	public double loFarenheightBound;
	public double extent;
	public Function function;

}
