package net.joshuahughes.smokercontroller;

import net.joshuahughes.smokercontroller.Parameters.FloatKey;
import net.joshuahughes.smokercontroller.Parameters.IntKey;
import net.joshuahughes.smokercontroller.Parameters.LongKey;
import net.joshuahughes.smokercontroller.controller.Controller;
import net.joshuahughes.smokercontroller.controller.SwingController;
import net.joshuahughes.smokercontroller.fan.Fan;
import net.joshuahughes.smokercontroller.fan.PWMFan;
import net.joshuahughes.smokercontroller.fan.SimulatedFan;
import net.joshuahughes.smokercontroller.function.Function;
import net.joshuahughes.smokercontroller.function.Linear;
import net.joshuahughes.smokercontroller.thermometer.MAX31855x8;
import net.joshuahughes.smokercontroller.thermometer.SimulatedThermometer;
import net.joshuahughes.smokercontroller.thermometer.Thermometer;

import com.pi4j.wiringpi.Spi;

public class Application {
	public static Function[] allFunctions = new Function[]{new Linear()};
	public static final boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");
	public static void main(String[] args) throws Exception {
		Parameters parameters = new Parameters();
		if(args!=null && args.length>0)
			parameters.load(Application.class.getResourceAsStream(args[0]));
		Controller controller = new SwingController(parameters);
		Fan fan = isLinux?new PWMFan(4):new SimulatedFan();
		Thermometer thermometer = isLinux?new MAX31855x8(Spi.CHANNEL_0):new SimulatedThermometer();
		while (true)
		{	
			parameters.put(LongKey.utctime,System.currentTimeMillis());
			controller.process(parameters);
			parameters.indexTemperatureMap = thermometer.getMap();
			Float fanTemperature =  parameters.indexTemperatureMap.get(parameters.get(IntKey.fantemperatureindex));
			if(fanTemperature!=null)
			{
				float min = parameters.get(FloatKey.lotemperature).floatValue();
				float max = min + parameters.get(FloatKey.temperaturerange).floatValue();
				float fanSpeed = parameters.function.normalize(min, max, fanTemperature);
				fanSpeed = Math.max(0,Math.min(1, fanSpeed));
				parameters.put(FloatKey.fanrpm, fan.getRPM(fanSpeed));
			}
			Thread.sleep(parameters.get(LongKey.sleep));
		}
	}

}