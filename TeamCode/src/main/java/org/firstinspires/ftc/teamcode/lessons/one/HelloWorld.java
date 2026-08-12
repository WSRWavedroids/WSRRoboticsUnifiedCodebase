package org.firstinspires.ftc.teamcode.lessons.one;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Core.Robot;

@Autonomous(group = "1", name = "HelloWorld")
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


        robot.readyHardware(); //Hardware is now ready.

        waitForStart();

        //The code to run once play has been pressed goes here.

        telemetry.addLine("Hello, World");
        telemetry.update(); //Updating current telemetry.
        robot.motor.setPower(0.5);
        robot.motor.setDirection(DcMotorSimple.Direction.FORWARD);
        robot.axonServo.setPosition(0.75);
        robot.colorServo.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.colorServo.setPower(1);
        sleep(1000);
        telemetry.addData("Pos", robot.motor.getCurrentPosition());
        sleep(1000);

    }
}

