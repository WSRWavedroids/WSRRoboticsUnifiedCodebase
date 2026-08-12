package org.firstinspires.ftc.teamcode.Teleop;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    //this is the vabale for the axon servo pos
    private double axonServoPos = 0;


    //this is starting speed for the color servo
    private double colorServoSpeed = 0.50;

    //TODO make this do something use full
    private Random random = new Random();

    int randomcolor = random.nextInt(10);

    /**
     * this is a longer way to write 0
     */
    private static final double OFF = 0;

    private static final double SERVOLIMET = 1;


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
        gamepad1.setLedColor(255, 0, 0, 10);
        gamepad2.setLedColor(255, 0, 0, 10);

    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {
        //this tells the color servo to start moving when the X is presed
        if (gamepad1.cross) {
            robot.colorServo.setPower(colorServoSpeed);
        } else {
            robot.colorServo.setPower(0);
        }

        //this controls the drection and position of the axon servo
        if(gamepad1.rightBumperWasPressed()){
            axonServoPos += 0.1;
            robot.axonServo.setPosition(axonServoPos);
        } else if(gamepad1.leftBumperWasPressed()){
            axonServoPos -= 0.1;
            robot.axonServo.setPosition(axonServoPos);
        }

        //this limits the axon servo position
        if (axonServoPos > SERVOLIMET){
            axonServoPos = SERVOLIMET;
        } else if (axonServoPos < 0){
            axonServoPos = 0;
        }

        //this contorls the speed and drection of the motor
        if (gamepad1.left_stick_x > 0.1){
            robot.motor.setPower(gamepad1.left_stick_x);
        } else if (gamepad1.left_stick_x < -0.1) {
            robot.motor.setPower(gamepad1.left_stick_x);
        } else if (gamepad1.left_stick_x == 0) {
            robot.motor.setPower(OFF);
        }

        //this controls the speed of color servo
       if (gamepad1.squareWasPressed()){
           colorServoSpeed += 0.1;
       } else if (gamepad1.circleWasPressed()) {
           colorServoSpeed -= 0.1;
       }

       //this limtits the color servo speed
        if (colorServoSpeed > SERVOLIMET){
            colorServoSpeed = SERVOLIMET;
        } else if (colorServoSpeed < 0){
            colorServoSpeed = 0;
        }

        //TODO MAKE THIS DO SOMETHING USEFUL
        if (gamepad1.triangleWasPressed()){
            robot.led.setRandomColor();
        }

    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {

    }
}


