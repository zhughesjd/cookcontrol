package net.joshuahughes.cookcontrol.fan;

import com.pi4j.wiringpi.SoftPwm;

public class PWMFan implements Fan{
	static
	{
        com.pi4j.wiringpi.Gpio.wiringPiSetup();
	}
	private int min = 0;
	private int max = 100;
	private int pin;
    public PWMFan(int pin)
    {
        SoftPwm.softPwmCreate(this.pin = pin, min, max);
    }
	public float getRPM(float speed) {
        SoftPwm.softPwmWrite(pin, (int)(min + (max-min)*speed));    	
		return Float.NaN;
	}
}