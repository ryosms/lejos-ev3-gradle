package com.ryosms.ev3.yoshioterada;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Created by ryosms on 2015/12/23.
 */
public class UltrSensorImpl implements Runnable {

    // センサー・モーター
    private final EV3UltrasonicSensor ursensor;
    private final RegulatedMotor leftMotor;
    private final RegulatedMotor rightMotor;
    // 障害物までの距離のしきい値
    private final static int MAX_LENGTH = 250;
    private final static int CRITICAL_LENGTH = 10;
    private final static int WARN_LENGTH = 20;

    // 警告レベル（LEDの点灯色と同一）
    private final static int DEFAULT_LEVEL = 4;     // 緑色の点滅
    private final static int CRITICAL_LEVEL = 5;    // 赤色の点滅
    private final static int WARNING_LEVEL = 6;     // 橙色の点滅

    // スレッドの停止フラグ
    private volatile boolean shutDownFlag;

    // 以前の状態
    private volatile int previousStatus;

    /**
     * コンストラクタ
     */
    public UltrSensorImpl(EV3UltrasonicSensor ursensor,
                          RegulatedMotor leftMotor,
                          RegulatedMotor rightMotor) {
        this.ursensor = ursensor;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;

    }

    @Override
    public void run() {
        SampleProvider distanceMode = ursensor.getDistanceMode();
        float value[] = new float[distanceMode.sampleSize()];

        while (!shutDownFlag) {
            distanceMode.fetchSample(value, 0);
            int centimeter = (int) (value[0] * 100);
            // 1mが1.000(MIN: 3cm, MAX: 250cm)

            if (centimeter <= CRITICAL_LENGTH) {
                executeIndividualOperation(CRITICAL_LEVEL, centimeter);     // 危険(回避)
            } else if (centimeter > CRITICAL_LENGTH && centimeter <= WARN_LENGTH) {
                executeIndividualOperation(WARNING_LEVEL, centimeter);      // ワーニング
            } else if (Integer.MAX_VALUE != centimeter && centimeter <= MAX_LENGTH) {
                executeIndividualOperation(DEFAULT_LEVEL, centimeter);      // 正常
            }
            Delay.msDelay(100);
        }
    }

    /**
     * 各しきい値に対する処理
     *
     * @param pattern    LEDの点灯パターン
     * @param centimeter 距離値(cm)
     */
    private void executeIndividualOperation(int pattern, int centimeter) {
        LCD.clearDisplay();
        LCD.drawString("Distance: " + centimeter, 0, 0);

        // 前回の処理を行った時と同じステータスの場合は終了
        if (previousStatus == pattern) {
            return;
        }
        // 前回のステータスと異なる場合は処理を実施
        Button.LEDPattern(pattern);
        switch (pattern) {
            case DEFAULT_LEVEL:     // 正常
                leftMotor.setSpeed(400);
                rightMotor.setSpeed(400);
                break;
            case CRITICAL_LEVEL:    // 危険でLEGOを回転
                leftMotor.stop();
                rightMotor.stop();

                leftMotor.rotate(360 + 79, false);  // 90度左回転
                leftMotor.waitComplete();

                leftMotor.forward();
                rightMotor.forward();
                break;
            case WARN_LENGTH:       // 警告、スピードを低下
                leftMotor.setSpeed(100);
                rightMotor.setSpeed(100);
                break;
            default:
                break;
        }
        previousStatus = pattern;
    }

    /**
     * スレッド処理の停止
     */
    public void stop() {
        shutDownFlag = true;
    }
}
