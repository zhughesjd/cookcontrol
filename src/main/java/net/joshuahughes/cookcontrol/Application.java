package net.joshuahughes.cookcontrol;

import com.pi4j.wiringpi.Spi;

import net.joshuahughes.cookcontrol.controller.Controller;
import net.joshuahughes.cookcontrol.controller.SwingController;
import net.joshuahughes.cookcontrol.enumproperties.Parameters;
import net.joshuahughes.cookcontrol.enumproperties.EnumProperties.FloatKey;
import net.joshuahughes.cookcontrol.enumproperties.EnumProperties.IntKey;
import net.joshuahughes.cookcontrol.enumproperties.EnumProperties.LongKey;
import net.joshuahughes.cookcontrol.fan.Fan;
import net.joshuahughes.cookcontrol.fan.PWMFan;
import net.joshuahughes.cookcontrol.fan.SimulatedFan;
import net.joshuahughes.cookcontrol.function.Function;
import net.joshuahughes.cookcontrol.function.Linear;
import net.joshuahughes.cookcontrol.thermometer.MAX31855x8;
import net.joshuahughes.cookcontrol.thermometer.SimulatedThermometer;
import net.joshuahughes.cookcontrol.thermometer.TemperatureCollection;

public class Application {
	public static Function[] allFunctions = new Function[]{new Linear()};
	public static final boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");
	public static void main(String[] args) throws Exception {
		Parameters parameters = new Parameters();
		if(args!=null && args.length>0)
			parameters.load(Application.class.getResourceAsStream(args[0]));
		Controller controller = new SwingController(parameters);
		Fan fan = isLinux?new PWMFan(4):new SimulatedFan();
		TemperatureCollection thermometerSet = isLinux?new MAX31855x8(Spi.CHANNEL_0):new SimulatedThermometer();
		while (true)
		{	
			parameters.put(LongKey.utctime,System.currentTimeMillis());
			controller.process(parameters);
			parameters.process(thermometerSet.getTemperatures());
			Float fanTemperature =  parameters.getLatestTemperature(parameters.get(IntKey.fantemperatureindex));
			if(fanTemperature!=null)
			{
				float min = parameters.get(FloatKey.mintemperature).floatValue();
				float max = parameters.get(FloatKey.maxtemperature).floatValue();
				float fanSpeed = parameters.function.normalize(min, max, fanTemperature);
				fanSpeed = Math.max(0,Math.min(1, fanSpeed));
				parameters.put(FloatKey.fanrpm, fan.getRPM(fanSpeed));
			}
			Thread.sleep(parameters.get(LongKey.sleep));
		}
	}

}