package net.joshuahughes.smokercontroller.thermometer;

import java.util.LinkedHashMap;
import java.util.Random;

public class SimulatedThermometer implements Thermometer{
	Random random = new Random(767676709l);
	public LinkedHashMap<Integer, Float> getMap()
	{
		LinkedHashMap<Integer, Float> map = new LinkedHashMap<Integer, Float>();
		for(int index=0;index<10;index++)
			map.put(index,80 + random.nextInt(20)+random.nextFloat());
		return map ;
	}
}
