package net.joshuahughes.smokercontroller;

import com.pi4j.wiringpi.Spi;

import net.joshuahughes.smokercontroller.Parameters.FloatKey;
import net.joshuahughes.smokercontroller.Parameters.IntKey;
import net.joshuahughes.smokercontroller.Parameters.LongKey;
import net.joshuahughes.smokercontroller.controller.Controller;
import net.joshuahughes.smokercontroller.controller.PrintStreamController;
import net.joshuahughes.smokercontroller.function.Function;
import net.joshuahughes.smokercontroller.function.Linear;
import net.joshuahughes.smokercontroller.smoker.PWMFan;
import net.joshuahughes.smokercontroller.smoker.MAX31855x8;

public class Application {
	public static Function[] allFunctions = new Function[]{new Linear()};

	public static void main(String[] args) throws Exception {
		Parameters parameters = new Parameters();
		if(args!=null && args.length>0)
			parameters.load(Application.class.getResourceAsStream(args[0]));
		System.out.println(parameters.get(Parameters.LongKey.sleep));
		System.out.println(parameters.get(Parameters.LongKey.sleep.toString()));
		PWMFan fan = new PWMFan(4);
		Controller controller = new PrintStreamController();
		MAX31855x8 max31855x8 = new MAX31855x8(Spi.CHANNEL_0);
		while (true)
		{	
			controller.process(parameters);
			parameters.put(LongKey.utctime,System.currentTimeMillis());
			parameters.indexTemperatureMap = max31855x8.getMap();
			Float fanTemperature =  parameters.indexTemperatureMap.get(parameters.get(IntKey.fantemperatureindex));
			if(fanTemperature!=null)
			{
				float min = parameters.get(FloatKey.lotemperature);
				float max = min + parameters.get(FloatKey.temperaturerange);
				float fanSpeed = parameters.function.normalize(min, max, fanTemperature);
				fanSpeed = Math.max(0,Math.min(1, fanSpeed));
				fan.setSpeed(fanSpeed);
			}
			parameters.put(FloatKey.fanrpm, fan.getRPM());
			Thread.sleep(parameters.get(LongKey.sleep));
		}
	}

}