package net.joshuahughes.smokercontroller.thermometer;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public interface Thermometer {
	public Entry<Float,LinkedHashMap<Integer, Float>> getTemperatures();
}
