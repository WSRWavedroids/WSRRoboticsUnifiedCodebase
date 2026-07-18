package org.firstinspires.ftc.teamcode.lessons.one;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.Robot;


@Autonomous(group = "1", name = "Hello, world!")
public class HelloWorld extends LinearOpMode {
    private Robot robot;

    public void runOpMode() {

        robot = new Robot(hardwareMap, telemetry, this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        while (opModeInInit()) {
            // This is the equivalent of init_loop(). It will repeat until the play button is
            // pressed.
            sleep(1);
        }


        robot.readyHardware();

        waitForStart();

        //The code to run once play has been pressed goes here.
        telemetry.addLine("Hello, world!");
        telemetry.update();

        robot.motor.setDirection(REVERSE);
        robot.motor.setPower(1);

        robot.axonServo.setPosition(1.50);
        robot.axonServo.setPosition(0.25);
        sleep(10000);

    }
}

