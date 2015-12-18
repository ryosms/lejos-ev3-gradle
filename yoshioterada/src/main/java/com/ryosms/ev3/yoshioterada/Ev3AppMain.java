package com.ryosms.ev3.yoshioterada;


import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;

/**
 * Created by ryosms on 2015/12/19.
 */
public class Ev3AppMain {
    public static void main(String[] args) {
        Ev3AppMain main = new Ev3AppMain();
        main.printHelloWorld();
        main.onKeyTouchExit();
    }

    private void printHelloWorld() {
        LCD.drawString("Hello World", 0, 0);
        Button.LEDPattern(1);
    }

    private void onKeyTouchExit() {
        EV3 ev3 = (EV3) BrickFinder.getLocal();
        Keys keys = ev3.getKeys();
        keys.waitForAnyPress();
        System.exit(0);
    }
}
