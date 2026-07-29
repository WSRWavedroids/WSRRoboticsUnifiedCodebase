package org.firstinspires.ftc.teamcode.lessons.five;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.*;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

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

@TeleOp(name = "Basic TeleOp", group = "Templates")
public class Purple_Banana_Op extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    public Robot robot = null;

    private double servoPosition = 0.5;

    private double servoSpeed = 0.5;

    private double startTime = 0;

    boolean ledIsOn = false;
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
            robot.colorServo.setPower(servoSpeed);
        }
        else if(gamepad1.crossWasReleased()) {
            robot.colorServo.setPower(0);
        }
        if (gamepad1.leftBumperWasPressed()) {
            servoPosition -= 0.1;
        } else if(gamepad1.rightBumperWasPressed()) {
            servoPosition += 0.1;
        }

        if (servoPosition < 0) servoPosition = 0;
        if (servoPosition > 1) servoPosition = 1;

        robot.axonServo.setPosition(servoPosition);
        robot.motor.setPower(gamepad1.left_stick_x);

        if (servoSpeed > 1) servoSpeed = 1;
        if (servoSpeed < -1) servoSpeed = -1;

        if (gamepad1.squareWasPressed()) {
            servoSpeed -= 0.1;
        }
         else if (gamepad1.circleWasPressed()) {
             servoSpeed += 0.1;
        }

         telemetry.addData("Servo Speed", servoSpeed);
         telemetry.update();


        if (gamepad1.triangle){
             robot.led.setColor(RED);
             startTime = runtime.seconds();
             ledIsOn = true;
         }
         else if(ledIsOn && runtime.seconds() - startTime >= 1) {
             robot.led.setColor(OFF);
             ledIsOn = false;
         }


    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {

    }

    public double getServoSpeed() {
        return servoSpeed;
    }

    public void setServoSpeed(double servoSpeed) {
        this.servoSpeed = servoSpeed;
    }
}


