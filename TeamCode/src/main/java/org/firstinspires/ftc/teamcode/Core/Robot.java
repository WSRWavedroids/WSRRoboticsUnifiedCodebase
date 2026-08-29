package org.firstinspires.ftc.teamcode.Core;

import com.bylazar.panels.Panels;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {

    public VoltageSensor voltageSensor;

    public Telemetry telemetry;

    public DcMotorEx motor;
    public Servo axonServo;
    public CRServo colorServo;
    public TouchSensor button;
    public ColorSensor colorSensor;
    public TouchSensor magnetSensor;
    public DistanceSensor distanceSensor;
    public LED led; // silly LED thinks it's a servo

    public ColorFinding colorFinding;

    //init and declare war
    public OpMode opmode;
    public HardwareMap hardwareMap;

    public IMU.Parameters imuParameters;

    public enum Alliance {
        BLUE(2), RED(1);
        public final int limelightPipeline;
        Alliance(int limelightPipeline) {
            this.limelightPipeline = limelightPipeline;
        }
    }
    public enum Color { PINK, ORANGE, YELLOW, GREEN, BLUE, NOCOLOR }

    public Panels panels;

    public static TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    //Initialize motors and servos
    public Robot(HardwareMap hardwareMap, Telemetry telemetry, OpMode opmode) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.opmode = opmode;

        // This section turns the names of the pieces of hardware into variables that we can program with.
        // Make sure that the device name is the exact same thing you typed in on the configuration on the driver hub.
        motor = hardwareMap.get(DcMotorEx.class, "Motor");
        axonServo = hardwareMap.get(Servo.class, "Axon");
        colorServo = hardwareMap.get(CRServo.class, "Color Servo");
        button = hardwareMap.get(TouchSensor.class, "Button");
        colorSensor = hardwareMap.get(ColorSensor.class, "Color Sensor");
        magnetSensor = hardwareMap.get(TouchSensor.class, "Magnet Sensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "Distance Sensor");
        led = new LED(hardwareMap.get(Servo.class, "LED"));

        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        imuParameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
        );
    }

    public Robot.Color targetColor = Color.PINK;

    /**
     * Updates the state of every part of the bob. Should be called once per loop.
     */
    public void update() {
        //TODO Make this function update the bob states
        colorFinding.update(targetColor);
    }

    /**
     * Moves hardware to match-ready positions
     */
    public void readyHardware() {
        // TODO Get your hardware ready here
    }

    /**
     * This function returns the closest color on the flower motor due to its RGB value
     */
    public Color getColor() {
        //
        int red = colorSensor.red();
        int green  = colorSensor.green();
        int blue = colorSensor.blue();

        // The numbers used to represent the filament to show green are unique to itself so it will return to the green
        if (
                red > 500 && red < 700
                && blue > 600 && blue < 800
                && green > 1200 && green < 1500
        ){
            return Color.GREEN;
        } else if (
                red > 1600 && red < 1900
                && blue > 900 && blue < 1200
                && green > 700 && green < 1000
        ) {
            return Color.PINK;
        } else if (
                red > 3000 && red < 3300
                && blue > 1100 && blue < 1400
                && green > 3200 && green < 3500
        ) {
            return Color.YELLOW;
        } else if (
                red > 1400 && red < 1700
                && blue > 300 && blue < 600
                && green > 300 && green < 600
        ) {
            return Color.ORANGE;
        } else if (
                red > 250 && red < 300
                && blue > 1000 && blue < 1200
                && green > 630 && green < 700
        ) {
            return Color.BLUE;
        }
        return  Color.NOCOLOR;
    }

    /**
     * Figures out if an integer is even.
     * @param x
     * @return True or false, for even or odd
     */
    public static boolean isEven(int x) {
        return x % 2 == 0;
    }

    /**
     * Finds the largest absolute value of a list of numbers
     * @param values A list of doubles
     * @return The largest absolute values
     */
    public static double getLargestAbsVal(double... values){
        // This function does some math!
        double max = 0;
        for (double val : values) {
            if (Math.abs(val) > max) {
                max = Math.abs(val);
            }
        }
        return max;
    }

    /**
     * Rounds a number to a specified level of precision.
     * @param input The number to be rounded
     * @param precision The degree of precision with which to round. For example, 0.001 will round
     *                  to three decimal places. Make sure this value is a multiple of 10, otherwise
     *                  some weird stuff will happen.
     * @return The rounded number.
     */
    public static double roundToDecimalPoint(double input, double precision) {
        return Math.round(input / precision) * precision;
    }
}
