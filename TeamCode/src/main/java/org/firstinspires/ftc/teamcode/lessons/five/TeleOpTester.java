package org.firstinspires.ftc.teamcode.lessons.five;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.EasyLED;
import org.firstinspires.ftc.teamcode.Core.LED;
import org.firstinspires.ftc.teamcode.Core.Robot;

import java.util.Random;
import java.util.Timer;

/**
 * This file is our iterative (Non-Linear) "OpMode" for TeleOp.
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is selected on the Robot Controller and executed.
 * This OpMode controls the functions of the bob during the driver-controlled period.
 * <p>
 * If the "@Disabled" line is not commented out, the program will not show up on the driver hub.
 * If you ever have problems with the program not showing up on the driver hub, it's probably because of that.
 * <p>
 */

@TeleOp(name = "Basic TeleOp", group = "Templates")
public class TeleOpTester extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our bob that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    private double goTime;

    public Robot robot = null;

    Random rand = new Random();

    public double axonServoPosition;
    public double colorServoSpeed;
    public int ledStatus = 0;

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

    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {
        axonServoPosition = robot.axonServo.getPosition();
        if (gamepad1.cross) {
            robot.colorServo.setPower(colorServoSpeed);

        }
        if (gamepad1.crossWasReleased()) {
            robot.colorServo.setPower(0.0);

        }
        if (gamepad1.leftBumperWasPressed()) {
            robot.axonServo.setPosition(axonServoPosition - 0.1);
        }
        if (gamepad1.rightBumperWasPressed()) {
            robot.axonServo.setPosition(axonServoPosition + 0.1);
        }
        robot.motor.setPower(gamepad1.left_stick_x);

        if (gamepad1.squareWasPressed()) {
            colorServoSpeed = colorServoSpeed - 0.1;

        }
        if (gamepad1.circleWasPressed()) {
            colorServoSpeed = colorServoSpeed + 0.1;
        }
    }

}
