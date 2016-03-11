package net.joshuahughes.cookcontrol.thermometer;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public interface TemperatureCollection {
	public Entry<Float,LinkedHashMap<Integer, Float>> getTemperatures();
}
