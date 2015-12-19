package com.ryosms.ev3.yoshioterada;


import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

/**
 * Created by ryosms on 2015/12/19.
 */
public class Ev3AppMain {

    public static void main(String[] args) {
        Ev3AppMain main = new Ev3AppMain();
        main.printHelloWorld();
        main.onKeyTouchExit();
    }

    /**
     * LCDとLEDの制御
     */
    private void printHelloWorld() {
        LCD.drawString("Hello World", 0, 0);
        /* LEDの点灯／点滅パターン
         *
         * 1. 緑: 点灯
         * 2. 赤: 点灯
         * 3. 橙: 点灯
         * 4. 緑: 点滅
         * 5. 赤: 点滅
         * 6. 橙: 点滅
         */
        for (int i = 1; i <= 6; i++) {
            LCD.drawString(String.format("Pattern: %d", i), 0, 2);
            Button.LEDPattern(i);
            Delay.msDelay(3000);
        }
        // LEDを消灯する
        LCD.drawString("Pattern: Stop", 0, 2);
        Button.LEDPattern(0);
    }

    private void onKeyTouchExit() {
        EV3 ev3 = (EV3) BrickFinder.getLocal();
        Keys keys = ev3.getKeys();
        keys.waitForAnyPress();
        System.exit(0);
    }
}
