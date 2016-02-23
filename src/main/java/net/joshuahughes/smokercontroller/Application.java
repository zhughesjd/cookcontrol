package net.joshuahughes.smokercontroller;

import net.joshuahughes.smokercontroller.controller.Controller;
import net.joshuahughes.smokercontroller.controller.FanTemperatureControl;
import net.joshuahughes.smokercontroller.controller.GitHubController;
import net.joshuahughes.smokercontroller.controller.Parameters;
import net.joshuahughes.smokercontroller.function.Function;
import net.joshuahughes.smokercontroller.function.Linear;
import net.joshuahughes.smokercontroller.smoker.Fan;
import net.joshuahughes.smokercontroller.smoker.MAX31855x8;

import com.pi4j.wiringpi.Spi;

public class Application {

	public static void main(String[] args) throws Exception {
		Fan fan = new Fan(4);
		Function function = new Linear();
		Controller controller = new GitHubController();
		MAX31855x8 max31855x8 = new MAX31855x8(Spi.CHANNEL_0);
		while (true)
		{	
			float[] temps = max31855x8.getTemperatures();
			Parameters parameters = new Parameters();
			parameters.utcTime = System.currentTimeMillis();
			FanTemperatureControl control = controller.get(parameters);
			double min = control.loFarenheightBound;
			double max = control.loFarenheightBound + control.extent;
			double fanSpeed = function.normalize(min, max, parameters.fanControlTemp);
			fanSpeed = Math.max(0,Math.min(1, fanSpeed));
			fan.setSpeed(fanSpeed);
			Thread.sleep(10000);
		}
	}

}