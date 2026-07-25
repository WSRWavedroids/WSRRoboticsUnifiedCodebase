package org.firstinspires.ftc.teamcode.lessons.three;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.Robot;

@Autonomous(group = "Templates", name = "Fun_ctions")
public class Fun_ctions extends LinearOpMode {
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
        robot.colorServo.setPower(0.07);
        while (opModeIsActive()) {
            telemetry.addData("color", robot.getColor());
            telemetry.update();

        }


    }
}

