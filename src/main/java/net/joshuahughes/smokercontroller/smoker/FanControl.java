package net.joshuahughes.smokercontroller.smoker;

import com.pi4j.wiringpi.SoftPwm;

public class FanControl {
    
    public static void main(String[] args) throws InterruptedException {
        
        com.pi4j.wiringpi.Gpio.wiringPiSetup();

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
}