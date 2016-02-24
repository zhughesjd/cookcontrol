package net.joshuahughes.smokercontroller;

import com.pi4j.wiringpi.Spi;

import net.joshuahughes.smokercontroller.controller.Controller;
import net.joshuahughes.smokercontroller.controller.GitHubController;
import net.joshuahughes.smokercontroller.controller.Parameters;
import net.joshuahughes.smokercontroller.function.Function;
import net.joshuahughes.smokercontroller.function.Linear;
import net.joshuahughes.smokercontroller.smoker.Fan;
import net.joshuahughes.smokercontroller.smoker.MAX31855x8;

public class Application {
	public static Function[] allFunctions = new Function[]{new Linear()};

	public static void main(String[] args) throws Exception {
		Fan fan = new Fan(4);
		Controller controller = new GitHubController();
		MAX31855x8 max31855x8 = new MAX31855x8(Spi.CHANNEL_0);
		Parameters parameters = controller.initiate();
		while (true)
		{	
			parameters.utcTime = System.currentTimeMillis();
			float[] temps = max31855x8.getTemperatures();
			parameters.indexFahrenheitMap.clear();

			double min = parameters.loFarenheightBound;
			double max = parameters.loFarenheightBound + parameters.extent;
			double fanSpeed = parameters.function.normalize(min, max, temps[parameters.fanTemperatureIndex]);
			fanSpeed = Math.max(0,Math.min(1, fanSpeed));
			fan.setSpeed(fanSpeed);

			controller.process(parameters);
			Thread.sleep(10000);
		}
	}

}