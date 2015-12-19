package com.ryosms.ev3.yoshioterada;


import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * Created by ryosms on 2015/12/19.
 */
public class Ev3AppMain {

    // モーター
    private static final RegulatedMotor leftMotor = Motor.C;
    private static final RegulatedMotor rightMotor = Motor.B;

    public static void main(String[] args) {
        Ev3AppMain main = new Ev3AppMain();
        main.manageMotor();
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

    private void manageMotor() {
        leftMotor.forward();        // 前進
        Delay.msDelay(3000);        // 3秒間実施
        leftMotor.stop();           // 停止
        leftMotor.setSpeed(100);    // スピード変更
        leftMotor.backward();       // 後退
        Delay.msDelay(3000);        // 3秒間実施
        leftMotor.stop();           // 停止
    }

    private void onKeyTouchExit() {
        LCD.clearDisplay();
        LCD.drawString("Hit any key", 0, 0);
        EV3 ev3 = (EV3) BrickFinder.getLocal();
        Keys keys = ev3.getKeys();
        keys.waitForAnyPress();
        System.exit(0);
    }
}
