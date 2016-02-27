package net.joshuahughes.smokercontroller.enumproperties;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import net.joshuahughes.smokercontroller.function.Function;
import net.joshuahughes.smokercontroller.function.Linear;

public class Parameters extends EnumProperties
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7735972862461344117L;
	public Function function = new Linear();
	public Thermometer sensorFeature;
	private TreeSet<Thermometer> thermometerSet = new TreeSet<>();
	private LinkedHashMap<Thermometer,Float> temperatureMap = new LinkedHashMap<>();
	public Parameters()
	{
		put(LongKey.sleep,1000l);
		put(IntKey.fantemperatureindex,0);
		put(FloatKey.mintemperature,200f);
		put(FloatKey.maxtemperature,250f);
	}
	public Set<Entry<Thermometer, Float>> getTemperatureEntries()
	{
		return this.temperatureMap.entrySet();
	}
	public Float getLatestTemperature(int index)
	{
		return temperatureMap.get(getThermometer(index));
	}
	public void process(Entry<Float, LinkedHashMap<Integer, Float>> temperatures)
	{
		temperatureMap.clear();
		temperatureMap.put(getThermometer(-1), temperatures.getKey());
		for(Entry<Integer, Float> entry : temperatures.getValue().entrySet())
			temperatureMap.put(getThermometer(entry.getKey()),entry.getValue());
	}
	public Thermometer getThermometer(int index)
	{
		Thermometer thermometer = new Thermometer(index,thermometerSet);
		if(thermometerSet.contains(thermometer))
			thermometer = thermometerSet.ceiling(thermometer);
		else
			thermometerSet.add(thermometer);
		return thermometer;
	}
}
