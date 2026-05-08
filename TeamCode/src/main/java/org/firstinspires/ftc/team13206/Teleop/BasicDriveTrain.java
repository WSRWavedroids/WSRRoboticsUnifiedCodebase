package org.firstinspires.ftc.team13206.Teleop;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;

import static org.firstinspires.ftc.team13206.Core.ArtifactLocator.SlotState.EMPTY;
import static org.firstinspires.ftc.team13206.Core.ArtifactLocator.SlotState.GREEN;
import static org.firstinspires.ftc.team13206.Core.ArtifactLocator.SlotState.PURPLE;

import android.graphics.Color;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.team13206.Core.ArtifactLocator;

@TeleOp(name = "Literally Just the Drive Train", group = "CompBot")
public class BasicDriveTrain extends OpMode {
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;

    RevColorSensorV3 rightColorScanner;
    RevColorSensorV3 leftColorScanner;



    @Override
    public void init() {

        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        rightColorScanner = hardwareMap.get(RevColorSensorV3.class, "rightColorScanner");
        leftColorScanner = hardwareMap.get(RevColorSensorV3.class, "leftColorScanner");


        frontLeftDrive.setDirection(REVERSE);
        frontRightDrive.setDirection(FORWARD);
        backLeftDrive.setDirection(REVERSE);
        backRightDrive.setDirection(FORWARD);

        frontLeftDrive.setZeroPowerBehavior(BRAKE);
        frontRightDrive.setZeroPowerBehavior(BRAKE);
        backLeftDrive.setZeroPowerBehavior(BRAKE);
        backRightDrive.setZeroPowerBehavior(BRAKE);
    }

    @Override
    public void loop() {
        //singleJoystickDrive();
        runSideScannersWithHSV();
        //runSideScannersWithRGB();
        telemetry.addData("Left Stick X", gamepad1.left_stick_x);
        telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Right Stick X", gamepad1.right_stick_x);
        telemetry.update();
    }

    private void singleJoystickDrive() {
        // We don't really know how this function works, but it makes the wheels drive, so we don't question it.
        // Don't mess with this function unless you REALLY know what you're doing.
        float leftY = this.gamepad1.left_stick_y;
        float rightX = this.gamepad1.right_stick_x;
        float leftX = this.gamepad1.left_stick_x;



        float[] motorPowers = new float[4];

        motorPowers[0] = (leftY - leftX - rightX); // frontLeftDrive
        motorPowers[1] = (leftY + leftX + rightX); // frontRightDrive
        motorPowers[2] = (leftY + leftX - rightX); // backLeftDrive
        motorPowers[3] = (leftY - leftX + rightX); // backRightDrive

        float max = getLargestAbsVal(motorPowers);
        if (max < 1) {
            max = 1;
        }

        for (int i = 0; i < motorPowers.length; i++) {
            motorPowers[i] *= (1 / max);

            float abs = Math.abs(motorPowers[i]);
            if (abs < 0.05) {
                motorPowers[i] = 0.0f;
            }
            if (abs > 1.0) {
                motorPowers[i] /= abs;
            }
        }

        setIndividualPowers(motorPowers);
    }

    private float getLargestAbsVal(float[] values){
        // This function does some math!
        float max = 0;
        for (float val : values) {
            if (Math.abs(val) > max) {
                max = Math.abs(val);
            }
        }
        return max;
    }

    private void setIndividualPowers(float[] motorPowers) {
        // This function creates an array so that the function below works.
        // Don't mess with this function unless you know what you're doing.

        if (motorPowers.length != 4) {
            return;
        }
        frontLeftDrive.setMode(RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(RUN_WITHOUT_ENCODER);

        frontLeftDrive.setPower(motorPowers[0]);
        frontRightDrive.setPower(motorPowers[1]);
        backLeftDrive.setPower(motorPowers[2]);
        backRightDrive.setPower(motorPowers[3]);
    }

    public ArtifactLocator.SlotState runSideScannersWithRGB()
    {
        float greenMinRed = 30;
        float greenMaxRed = 60;
        float greenMinGreen = 90;
        float greenMaxGreen = 170;
        float greenMinBlue = 65;
        float greenMaxBlue = 110;

        float purpleMinRed = 50;
        float purpleMaxRed = 90;
        float purpleMinGreen= 70;
        float purpleMaxGreen= 150;
        float purpleMinBlue= 80;
        float purpleMaxBlue= 125;


        float leftRed = leftColorScanner.red();
        float leftGreen = leftColorScanner.green();
        float leftBlue = leftColorScanner.blue();

        float rightRed = rightColorScanner.red();
        float rightGreen = rightColorScanner.green();
        float rightBlue = rightColorScanner.blue();

        telemetry.addData("Red: ", leftRed + ", " + rightRed);
        telemetry.addData("Green: ", leftGreen + ", " + rightGreen);
        telemetry.addData("Blue: ", leftBlue + ", " + rightBlue);

        //Now take our values and do something with them.

        if(leftBlue > purpleMinBlue && leftBlue < purpleMaxBlue &&
                leftRed > purpleMinRed && leftRed < purpleMaxRed &&
                leftGreen > purpleMinGreen && leftGreen < purpleMaxGreen) {
            telemetry.addData("It thinks its: ", "Purple");
            return PURPLE;
        }
        else if(leftBlue > greenMinBlue && leftBlue < greenMaxBlue &&
                leftRed > greenMinRed && leftRed < greenMaxRed &&
                leftGreen > greenMinGreen && leftGreen < greenMaxGreen) {
            telemetry.addData("It thinks its: ", "Green");
            return GREEN;
        }
        else if(rightBlue > purpleMinBlue && rightBlue < purpleMaxBlue &&
                rightRed > purpleMinRed && rightRed < purpleMaxRed &&
                rightGreen > purpleMinGreen && rightGreen < purpleMaxGreen) {
            telemetry.addData("It thinks its: ", "Purple");
            return PURPLE;
        }
        else if(rightBlue > greenMinBlue && rightBlue < greenMaxBlue &&
                rightRed > greenMinRed && rightRed < greenMaxRed &&
                rightGreen > greenMinGreen && rightGreen < greenMaxGreen) {
            telemetry.addData("It thinks its: ", "Green");
            return GREEN;
        }
        telemetry.addData("It thinks its: ", "Nothing");
        return EMPTY;

    }

    public ArtifactLocator.SlotState runSideScannersWithHSV()
    {
        double purpleMinHue = 190;
        double purpleMaxHue = 295;
        double purpleMinValue = 0.3;
        double purpleMaxValue = 1.2;


        double greenMinHue = 150;
        double greenMaxHue = 160;
        double greenMinValue = 0.3;
        double greenMaxValue = 1.2;

        float[] leftHSVValues = new float[3];
        float[] rightHSVValues = new float[3];
        Color.RGBToHSV(leftColorScanner.red(), leftColorScanner.green(), leftColorScanner.blue(), leftHSVValues);
        Color.RGBToHSV(rightColorScanner.red(), rightColorScanner.green(), rightColorScanner.blue(), rightHSVValues);

        double leftHue = leftHSVValues[0];
        double leftSaturation = leftHSVValues[1];
        double leftValue = leftHSVValues[2];

        double rightHue = rightHSVValues[0];
        double rightSaturation = rightHSVValues[1];
        double rightValue = rightHSVValues[2];

        telemetry.addData("Hue: ", leftHue + ", " + rightHue);
        telemetry.addData("Saturation: ", leftSaturation + ", " + rightSaturation);
        telemetry.addData("Value: ", leftValue + ", " + rightValue);
        if(leftHue > purpleMinHue && leftHue < purpleMaxHue &&
                leftValue > purpleMinValue && leftValue < purpleMaxValue) {
            telemetry.addData("It thinks its: ", "Purple");
            return PURPLE;
        }
        else if(leftHue > greenMinHue && leftHue < greenMaxHue &&
                leftValue > greenMinValue && leftValue < greenMaxValue) {
            telemetry.addData("It thinks its: ", "Green");
            return GREEN;
        }
        else if(rightHue > purpleMinHue && rightHue < purpleMaxHue &&
                rightValue > purpleMinValue && rightValue < purpleMaxValue) {
            telemetry.addData("It thinks its: ", "Purple");
            return PURPLE;
        }
        else if(rightHue > greenMinHue && rightHue < greenMaxHue &&
                rightValue > greenMinValue && rightValue < greenMaxValue) {
            telemetry.addData("It thinks its: ", "Green");
            return GREEN;
        }
        telemetry.addData("It thinks its: ", "Nothing");
        return EMPTY;
    }
}