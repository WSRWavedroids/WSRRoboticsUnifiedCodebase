package org.firstinspires.ftc.teamcode.lessons.two;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.Robot;

@Autonomous(group = "2", name = "BranchingAutos")
public class BranchingAutos extends LinearOpMode {
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
        int blue = robot.colorSensor.blue();
        if (blue > 900 & blue < 1200) {
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(-0.50);
            sleep(1000);
            robot.colorServo.setPower(1);
            robot.axonServo.setPosition(0.50);
            sleep(1000);
        }

        sleep(10000);
    }
}

