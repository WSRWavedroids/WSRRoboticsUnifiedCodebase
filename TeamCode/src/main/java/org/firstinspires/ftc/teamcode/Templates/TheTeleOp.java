package org.firstinspires.ftc.teamcode.Templates;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
 * This OpMode controls the functions of the bob during the driver-controlled period.
 * <p>
 * If the "@Disabled" line is not commented out, the program will not show up on the driver hub.
 * If you ever have problems with the program not showing up on the driver hub, it's probably because of that.
 * <p>
 */
@TeleOp(name = "Basic TeleOp", group = "Templates")
public class TheTeleOp extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our bob that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    public Robot robot = null;
    public double crossStartTime;
    public double circleStartTime;
    public double squareStartTime;
    public boolean circlePressed = false;
    public enum LEDSteps{
        RED, GREEN, OFF
    }
    public LEDSteps currentLEDStep = LEDSteps.OFF;

    public enum ServoSteps{
        OFF, COLORSERVO, AXONSERVO, AXONSERVO1, AXONCENTER
    }

    public ServoSteps currentServoStep = ServoSteps.OFF;
    public enum MotorSteps{
        OFF, CWHALF, CCWHALF, CCWFULL
    }
    public MotorSteps currentMotorSteps = MotorSteps.OFF;
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
        /*if (gamepad1.crossWasPressed()) {
            crossStartTime = runtime.seconds();
            bob.motor.setPower(0.5);
        }
        if (runtime.seconds() - crossStartTime >= 2) {
            bob.motor.setPower(-0.5);
        }
        if(runtime.seconds() - crossStartTime >= 4) {
            bob.motor.setPower(-1.0);
        }
        if(runtime.seconds() - crossStartTime >= 5){
            bob.motor.setPower(0.0);
        }*/

        switch (currentLEDStep) {
            case OFF:
                robot.led.setColor(LED.LEDColors.OFF);
                if (gamepad1.circle) {
                    circleStartTime = runtime.seconds();
                    currentLEDStep = LEDSteps.RED;
                    robot.led.setColor(LED.LEDColors.RED);
                }
                break;
            case RED:
                if (runtime.seconds() - circleStartTime >= 1) {
                    currentLEDStep = LEDSteps.GREEN;
                    robot.led.setColor(LED.LEDColors.GREEN);
                }
                break;
            case GREEN:
                robot.led.setColor(LED.LEDColors.GREEN);
                if (runtime.seconds() - circleStartTime >= 3) {
                    currentLEDStep = LEDSteps.OFF;
                    robot.led.setColor(LED.LEDColors.OFF);
                }
                break;

        }

        switch (currentServoStep) {
            case OFF:
                if (gamepad1.square) {
                    currentServoStep = ServoSteps.COLORSERVO;
                }
                break;
            case COLORSERVO:
                robot.colorServo.setPower(0.1);
                if (robot.getColor() == Robot.Color.PINK) {
                    robot.colorServo.setPower(0);
                    currentServoStep = ServoSteps.AXONSERVO;
                    squareStartTime = runtime.seconds();
                }
                break;

            case AXONSERVO:
                robot.axonServo.setPosition(0);
                if (runtime.seconds()-squareStartTime>1) {
                    currentServoStep = ServoSteps.AXONSERVO1;
                    squareStartTime = runtime.seconds();
                }
                break;
            case AXONSERVO1:
                robot.axonServo.setPosition(1);
                if (runtime.seconds()-squareStartTime>1) {
                    currentServoStep = ServoSteps.AXONCENTER;
                    squareStartTime = runtime.seconds();
                }
                break;
            case AXONCENTER:
                if (robot.button.isPressed()) {
                    robot.axonServo.setPosition(0.5);
                }
                currentServoStep = ServoSteps.OFF;
                break;


        }

        switch(currentMotorSteps) {
            case OFF:
                if(gamepad1.cross()){
                    crossStartTime = runtime.seconds();
                    robot.motor.setPower(0.5);
                    currentMotorSteps = MotorSteps.CWHALF;
                }
                break;
            case CWHALF:
                crossStartTime <= runtime.seconds()
        }



    }


    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {

    }
}


