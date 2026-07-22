package org.firstinspires.ftc.teamcode.lessons.two;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Core.Robot;

@Autonomous(group = "2", name = "BranchingAutos")
public class BranchingAuto extends LinearOpMode {
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
        robot.axonServo.setPosition(0);
        robot.colorServo.setDirection(DcMotorSimple.Direction.FORWARD);
        searchForRed(0.1);
        sleep(500);
        robot.colorServo.setDirection(DcMotorSimple.Direction.REVERSE);
        searchForBlue(0.1);
        robot.motor.setPower(0.75);
        robot.axonServo.setPosition(0.5);
        robot.led.setPosition(0.5);
        sleep(10000);
    }

    private void searchForRed(double power){
        robot.colorServo.setPower(power);
        while (robot.colorSensor.red() < 750 || robot.colorSensor.blue() > 450 || robot.colorSensor.green() > 550) {
            robot.colorServo.setPower(0.1);
            telemetry.addLine("searching for color... RED");
            telemetry.update();
        }
        telemetry.addLine("FOUND RED!");
        telemetry.update();
        robot.colorServo.setPower(0);
    }

    private void searchForBlue(double power){
        while (robot.colorSensor.blue() < 750 || robot.colorSensor.red() > 450 || robot.colorSensor.green() > 550) {
            robot.colorServo.setPower(0.1);
            telemetry.addLine("searching for color... BLUE");
            telemetry.update();
        }
        telemetry.addLine("FOUND BLUE!");
        telemetry.update();
        robot.colorServo.setPower(0);
    }
}

