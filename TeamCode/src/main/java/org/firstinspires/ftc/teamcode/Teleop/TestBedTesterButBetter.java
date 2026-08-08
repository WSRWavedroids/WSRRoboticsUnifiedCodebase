package org.firstinspires.ftc.teamcode.Teleop;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.*;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.LED;
import org.firstinspires.ftc.teamcode.Core.Robot;

import java.util.Random;

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
@TeleOp(name = "Test Bed Tester but better", group = "Teleop")
public class TestBedTesterButBetter extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    public Robot robot = null;

    private double axonServoPos = 0;

    private double colorServoSpeed = 0.50;

    private Random random = new Random();


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
        if (gamepad1.cross) {
            robot.colorServo.setPower(colorServoSpeed);
        } else {
            robot.colorServo.setPower(0);
        }

        if(gamepad1.rightBumperWasPressed()){
            axonServoPos += 0.1;
            robot.axonServo.setPosition(axonServoPos);
        } else if(gamepad1.leftBumperWasPressed()){
            axonServoPos -= 0.1;
            robot.axonServo.setPosition(axonServoPos);
        }

        if (axonServoPos > 1){
            axonServoPos = 1;
        } else if (axonServoPos < 0){
            axonServoPos = 0;
        }

        if (gamepad1.left_stick_x > 0.1){
            robot.motor.setPower(gamepad1.left_stick_x);
        } else if (gamepad1.left_stick_x < -0.1) {
            robot.motor.setPower(gamepad1.left_stick_x);
        } else if (gamepad1.left_stick_x == 0) {
            robot.motor.setPower(0);
        }

       if (gamepad1.squareWasPressed()){
           colorServoSpeed += 0.1;
       } else if (gamepad1.circleWasPressed()) {
           colorServoSpeed -= 0.1;
       }

        if (colorServoSpeed > 1){
            colorServoSpeed = 1;
        } else if (colorServoSpeed < 0){
            colorServoSpeed = 0;
        }

        if (gamepad1.triangle){
            robot.led.setColor(WHITE);
        }

    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {

    }
}


