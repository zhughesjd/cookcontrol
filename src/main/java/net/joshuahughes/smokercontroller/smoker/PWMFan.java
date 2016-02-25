package net.joshuahughes.smokercontroller.smoker;

import com.pi4j.wiringpi.SoftPwm;

public class PWMFan {
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
    public void setSpeed(double speed)
    {
        SoftPwm.softPwmWrite(pin, (int)(min + (max-min)*speed));    	
    }
    public static void main(String[] args) throws InterruptedException {
        SoftPwm.softPwmCreate(4, 0, 100);

        while (true) {            
            for (int i = 0; i <= 100; i++) {
                SoftPwm.softPwmWrite(4, i);
                Thread.sleep(100);
            }
            for (int i = 100; i >= 0; i--) {
                SoftPwm.softPwmWrite(4, i);
                Thread.sleep(100);
            }
        }
    }
	public float getRPM() {
		return Float.NaN;
	}
}