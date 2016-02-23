package net.joshuahughes.smokercontroller.controller;

public class FanTemperatureControl 
{
	public static enum Function{linear,gaussianCDF}
	public double loFarenheightBound;
	public double extent;
	public Function function;
}
