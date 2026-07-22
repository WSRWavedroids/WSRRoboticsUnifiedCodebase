package org.firstinspires.ftc.teamcode.lessons.three;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Core.Robot;

@Autonomous(group = "3", name = "Functions")
public class Functions extends LinearOpMode {
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

        while (opModeIsActive()){
            robot.colorServo.setPower(gamepad1.right_stick_x / 10);
            telemetry.addData("Sensor Color", robot.getColor());
            telemetry.update();
        }
    }
}

