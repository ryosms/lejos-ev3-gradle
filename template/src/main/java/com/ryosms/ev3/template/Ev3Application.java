package com.ryosms.ev3.template;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

/**
 * Created by ryosms on 2015/12/18.
 */
public class Ev3Application {

    public static void main(String[] args) {
        LCD.drawString("Hello EV3!", 0, 4);
        Delay.msDelay(5000);
    }
}
