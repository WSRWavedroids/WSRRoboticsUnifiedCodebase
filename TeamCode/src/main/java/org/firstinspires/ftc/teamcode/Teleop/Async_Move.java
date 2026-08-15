package org.firstinspires.ftc.teamcode.Teleop;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.GREEN;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.OFF;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.RED;

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

@TeleOp(name = "Async Move", group = "Teleop")
public class Async_Move extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    public Robot robot = null;

    private double crossStartTime = 0;

    private double circleStartTime = 0;

    private enum MotorState {
        HALF_SPEED_CLOCKWISE,
        STILL,
        HALF_SPEED_COUNTERCLOCKWISE,
        FULL_SPEED_COUNTERCLOCKWISE
    }

    private MotorState currentMotorState = MotorState.STILL;

    private enum LedState {
        RED,
        GREEN,
        OFF,
    }

    private LedState currentLedState = LedState.OFF;






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
        /*
        if (gamepad1.crossWasPressed() && !motorIsRunning) {
            robot.motor.setPower(0.5);
            motorIsRunning = true;
            crossStartTime = runtime.seconds();
        } else if (motorIsRunning && runtime.seconds() - crossStartTime >= 5) {
            robot.motor.setPower(0);
            motorIsRunning = false;
        } else if (motorIsRunning && runtime.seconds() - crossStartTime >= 4) {
            robot.motor.setPower(-1);
        } else if (motorIsRunning && runtime.seconds() - crossStartTime >= 2) {
            robot.motor.setPower(-0.5);
        }

        if (gamepad1.circleWasPressed() && !ledOn) {
            robot.led.setColor(RED);
            ledOn = true;
            circleStartTime = runtime.seconds();
        } else if (ledOn && runtime.seconds() - circleStartTime >= 3) {
            robot.led.setColor(OFF);
            ledOn = false;
        } else if (ledOn && runtime.seconds() - circleStartTime >= 1) {
            robot.led.setColor(GREEN);
        }
         */

        switch (currentMotorState) {
            case STILL:
                robot.motor.setPower(0);
                if (gamepad1.cross) {
                    currentMotorState = MotorState.HALF_SPEED_CLOCKWISE;
                    robot.motor.setPower(0.5);
                    crossStartTime = runtime.seconds();
                }
                break;
            case HALF_SPEED_CLOCKWISE:
                if (runtime.seconds() - crossStartTime >= 2) {
                    currentMotorState = MotorState.HALF_SPEED_COUNTERCLOCKWISE;
                    robot.motor.setPower(-0.5);
                }
                break;
            case HALF_SPEED_COUNTERCLOCKWISE:
                if (runtime.seconds() - crossStartTime >= 4) {
                    currentMotorState = MotorState.FULL_SPEED_COUNTERCLOCKWISE;
                    robot.motor.setPower(1);
                }
                break;
            case FULL_SPEED_COUNTERCLOCKWISE:
                if (runtime.seconds() - crossStartTime >= 5) {
                    currentMotorState = MotorState.STILL;
                }



        }

        switch (currentLedState){
            case OFF:
                robot.led.setColor(OFF);
                if (gamepad1.circle) {
                    currentLedState = LedState.RED;
                    circleStartTime = runtime.seconds();
                    robot.led.setColor(RED);
                }
                break;
            case RED:
                if (runtime.seconds() - circleStartTime >= 1) {
                    currentLedState = LedState.GREEN;
                    robot.led.setColor(GREEN);
                }
                break;
            case GREEN:
                if (runtime.seconds() - circleStartTime >= 3) {
                    currentLedState = LedState.OFF;
                }
        }

    }



    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {

    }
}


