package net.joshuahughes.smokercontroller.thermometer;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

public class SimulatedThermometer implements Thermometer{
	Random random = new Random(767676709l);
	private float sensorTemp = 77;
	public Entry<Float,LinkedHashMap<Integer, Float>> getTemperatures()
	{
		LinkedHashMap<Integer, Float> map = new LinkedHashMap<Integer, Float>();
		for(int index=0;index<10;index++)
			map.put(index,2*sensorTemp + random.nextInt(20)+random.nextFloat());
		return new AbstractMap.SimpleEntry<Float,LinkedHashMap<Integer, Float>>(sensorTemp,map);
	}
}
