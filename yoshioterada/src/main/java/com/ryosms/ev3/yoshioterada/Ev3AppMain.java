package com.ryosms.ev3.yoshioterada;


import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.utility.Delay;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ryosms on 2015/12/19.
 */
@SuppressWarnings("unused")
public class Ev3AppMain {

    // モーター
    private static final RegulatedMotor leftMotor = Motor.B;
    private static final RegulatedMotor rightMotor = Motor.C;

    // 超音波センサー
    private static final EV3UltrasonicSensor ursensor = new EV3UltrasonicSensor(SensorPort.S4);

    // 並列処理用サービス
    private ExecutorService exec;
    private UltrSensorImpl ulSensor;

    /**
     * コンストラクタ
     */
    public Ev3AppMain() {
        leftMotor.resetTachoCount();
        rightMotor.resetTachoCount();
        leftMotor.setSpeed(400);
        rightMotor.setSpeed(400);
    }

    /**
     * メインメソッド
     *
     * @param args 起動パラメータ
     */
    public static void main(String[] args) {
        Ev3AppMain main = new Ev3AppMain();
        main.executeConcurrentTask();
        main.onKeyTouchExitTask();
    }

    /**
     * マルチスレッドでタスクを実行
     */
    private void executeConcurrentTask() {
        exec = Executors.newSingleThreadExecutor();
        ulSensor = new UltrSensorImpl(ursensor, leftMotor, rightMotor);
        exec.submit(ulSensor);
        leftMotor.forward();
        rightMotor.forward();
    }

    /**
     * 停止処理
     */
    private void onKeyTouchExitTask() {
        EV3 ev3 = (EV3) BrickFinder.getLocal();
        Keys keys = ev3.getKeys();
        keys.waitForAnyPress();

        leftMotor.stop();
        rightMotor.stop();
        ursensor.disable();
        ulSensor.stop();
        exec.shutdownNow();
        System.exit(0);
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

    /**
     * モーターの制御
     */
    private void manageMotor() {
        leftMotor.forward();        // 前進
        rightMotor.forward();
        Delay.msDelay(3000);        // 3秒間実施
        leftMotor.stop();           // 停止
        rightMotor.stop();
        leftMotor.setSpeed(100);    // スピード変更
        rightMotor.setSpeed(100);
        leftMotor.backward();       // 後退
        rightMotor.backward();
        Delay.msDelay(3000);        // 3秒間実施
        leftMotor.stop();           // 停止
        rightMotor.stop();
    }

    /**
     * モーターの回転制御
     */
    private void manageMotor1() {
        // leftMotorを使用しrotate()の動作確認
        leftMotor.rotate(360);
        Delay.msDelay(2000);
        leftMotor.rotate(360);
        Delay.msDelay(2000);
        leftMotor.rotate(0);
    }

    /**
     * モーターの回転制御2
     */
    private void manageMotor2() {
        // rightMotorを使用しrotateTo()の動作確認
        rightMotor.rotateTo(360);
        Delay.msDelay(2000);
        rightMotor.rotateTo(360);
        Delay.msDelay(2000);
        rightMotor.rotateTo(0);
    }

    /**
     * モーターの回転制御3
     */
    private void manageMotor3() {
        // 2つの引数を持つメソッドの動作確認
        // 同時に回転
        leftMotor.rotate(360, true);
        rightMotor.rotate(360, true);
        Delay.msDelay(2000);

        // 片車輪ずつ回転
        leftMotor.rotate(360, false);
        rightMotor.rotate(360, false);
        Delay.msDelay(2000);

        // 片車輪ずつ回転
        leftMotor.rotate(360);
        rightMotor.rotate(360);
    }

    private void onKeyTouchExit() {
        LCD.clearDisplay();
        LCD.drawString("Hit any key", 0, 0);
        EV3 ev3 = (EV3) BrickFinder.getLocal();
        Keys keys = ev3.getKeys();
        keys.waitForAnyPress();
        System.exit(0);
    }

    /**
     * 高度なモーターの制御1
     */
    private void managedByPilot1() {
        // 左車輪: 車輪の直径 = 5.6cm, 中心位置からの距離 = 6.4cm（左が正）
        Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, 5.6f).offset(6.4f);
        // 右車輪: 車輪の直径 = 5.6cm, 中心位置からの距離 = 6.4cm（左が正）
        Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, 5.6f).offset(-6.4f);
        Chassis chassis = new WheeledChassis(new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot pilot = new MovePilot(chassis);
        for (int i = 0; i < 4; i++) {
            pilot.travel(20);
            pilot.rotate(90);
        }
        pilot.stop();
    }

    /**
     * 高度なモーターの制御2
     */
    private void managedByPilot2() {
        // 左車輪: 車輪の直径 = 5.6cm, 中心位置からの距離 = 6.4cm（左が正）
        Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, 5.6f).offset(6.4f);
        // 右車輪: 車輪の直径 = 5.6cm, 中心位置からの距離 = 6.4cm（左が正）
        Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, 5.6f).offset(-6.4f);
        Chassis chassis = new WheeledChassis(new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
        MovePilot pilot = new MovePilot(chassis);

        OdometryPoseProvider opProvider =
                new OdometryPoseProvider(pilot);
        Navigator navigator = new Navigator(pilot, opProvider);

        navigator.goTo(20, 0);
        navigator.goTo(20, 20);
        navigator.goTo(0, 20);
        navigator.goTo(0, 0);
        navigator.followPath();
        navigator.waitForStop();
    }

    /**
     * 超音波センサーを利用して障害物までの距離を計測
     */
    private void testSensor() {
        LCD.drawString("Hit Center + Down for exit", 0, 0);
        SampleProvider distanceMode = ursensor.getDistanceMode();
        float value[] = new float[distanceMode.sampleSize()];
        // 超音波センサーの場合distanceMode.sampleSize()は必ず1
        while (true) {
            distanceMode.fetchSample(value, 0);
            int centimeter = (int) (value[0] * 100);
            // 1mが1.000(min:3cm, max:250cm)
            if (centimeter > 3 && centimeter <= 10) {
                executeIndividualOperation(2, centimeter);  // 赤色点灯
            } else if (centimeter > 10 && centimeter <= 20) {
                executeIndividualOperation(3, centimeter);  // 橙色点灯
            } else if (Integer.MAX_VALUE != centimeter && centimeter <= 250) {
                executeIndividualOperation(1, centimeter);  // 緑色点灯
            }
            Delay.msDelay(100);
        }

    }

    private void executeIndividualOperation(int pattern, int centimeter) {
        Button.LEDPattern(pattern);
        LCD.clearDisplay();
        LCD.drawString("Hit Center + Down for exit", 0, 0);
        LCD.drawString("Distance: " + centimeter, 0, 2);
    }

}
