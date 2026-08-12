package org.firstinspires.ftc.teamcode.lessons.five;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.LED;
import org.firstinspires.ftc.teamcode.Core.Robot;

/**
 * This file is our iterative (Non-Linear) "OpMode" for TeleOp.
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is selected on the Robot Controller and executed.
 * This OpMode controls the functions of the robot during the driver-controlled period.
 * <p>
 * If the "@Disabled" line is not commented out, the program will not show up on the driver hub.
 * If you ever have problems with the program not showing up on the driver hub, it's probably because of that.
 * <p>
 */

@TeleOp(name = "TeleopPractice", group = "5")
public class TeleopPractice extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    public Robot robot = null;
    private double colorServoSpeed = 0;
    private double axonServoSpeed = 0;
    private double colorServoSpeedIncrement = 0.1;
    private double axonServoSpeedIncrement = 0.1;
    private double axonWaveDir = 1;
    private double axonWaving = -1;
    private double ledFlashing = 0;
    private double ledFlashingTime = -1;

    private double rbPressed = 0;
    private double lbPressed = 0;
    private double aPressed = 0;
    private double bPressed = 0;
    private double xPressed = 0;
    private double yPressed = 0;



    /*
     * Code to run ONCE when the driver hits INIT
     */
    public void init() {
        // Call the initialization protocol from the Robot class.
        robot = new Robot(hardwareMap, telemetry, this);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    public void init_loop() {
        telemetry.addData("HYPE", "ARE! YOU! READY?!?!?!?!");
        telemetry.update();
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    public void start() {
        runtime.reset();
        telemetry.addData("HYPE", "Let's do this!!!");
        gamepad1.setLedColor(0, 0, 255, 10);
        gamepad2.setLedColor(0, 0, 255, 10);
        robot.colorServo.setDirection(FORWARD);

    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {
        //CODE
        /*if (gamepad1.a) {
            robot.colorServo.setPower(0.5);
        } else {
            robot.colorServo.setPower(0);
        }
        if (gamepad1.leftBumperWasPressed()) {
            robot.axonServo.setPosition(robot.axonServo.getPosition() - 0.1);
        }
        if (gamepad1.rightBumperWasPressed()) {
            robot.axonServo.setPosition(robot.axonServo.getPosition() + 0.1);
        }
        robot.motor.setPower(gamepad1.left_stick_x);
        if (gamepad1.xWasPressed()) {
            robot.colorServo.setPower(robot.colorServo.getPower() - 0.1);
        }
        if (gamepad1.bWasPressed()) {
            robot.colorServo.setPower(robot.colorServo.getPower() + 0.1);
        }*/
        //OPTIMIZED
        if (gamepad1.yWasPressed()) {
            ledFlashing = 1 - ledFlashing;
        }
        if (ledFlashing == 1 || robot.led.ledIsOn) {
            loopRandomColors();
        }
        checkButtonPress();
        colorServoSpeed = colorServoSpeed + colorServoSpeedIncrement * (xPressed - bPressed);
        robot.motor.setPower(gamepad1.left_stick_x);
        if (gamepad1.a) {
            robot.colorServo.setPower(colorServoSpeed);
        }
        if (gamepad1.dpadUpWasPressed()) {
            axonWaving = getRuntime() + 3;
        }
        if (gamepad1.dpadDownWasPressed()) {
            axonWaving = getRuntime();
        }
        if (getRuntime() > axonWaving) {
            robot.axonServo.setPosition(robot.axonServo.getPosition() + axonServoSpeedIncrement * (rbPressed - lbPressed));
        } else {
            waveAxon(axonServoSpeedIncrement / 5, 0.25);
        }

    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {

    }

    private void checkButtonPress() {
        if (gamepad1.leftBumperWasPressed()) {
            lbPressed = 1;
        } else {
            lbPressed = 0;
        }

        if (gamepad1.rightBumperWasPressed()) {
            rbPressed = 1;
        } else {
            rbPressed = 0;
        }

        if (gamepad1.aWasPressed()) {
            aPressed = 1;
        } else {
            aPressed = 0;
        }

        if (gamepad1.bWasPressed()) {bPressed = 1;
        } else {
            bPressed = 0;
        }

        if (gamepad1.xWasPressed()) {xPressed = 1;
        } else {
            xPressed = 0;
        }

        if (gamepad1.yWasPressed()) {yPressed = 1;
        } else {
            yPressed = 0;
        }
    }
    /*public enum numberColor {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SE
    }*/
    private void waveAxon(double speed, double range) {
        double position = robot.axonServo.getPosition() + (speed * axonWaveDir);
        if (position > range) {
            axonWaveDir = -1;
            position = range;
        }
        else if (position < -range) {
            axonWaveDir = 1;
            position = -range;
        }
        robot.axonServo.setPosition(position);
    }

    public void loopRandomColors() {
        ledFlashingTime = getRuntime() + 0.5;
        if (getRuntime() >= ledFlashingTime) {
            if (robot.led.ledIsOn) {
                robot.led.setColor(OFF);
            } else {
                robot.led.setRandomColor();

            }
        }
    }
}


