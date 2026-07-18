package org.firstinspires.ftc.teamcode.lessons.two;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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
        robot.led.setPosition(.7896974);

        if (robot.distanceSensor.getDistance(DistanceUnit.INCH) < 2 ) {
            robot.motor.setDirection(FORWARD);
            robot.motor.setPower(1);
            sleep(1000);
            robot.motor.setPower(.5);
            sleep(1000);
            robot.motor.setPower(0);
        }

        if (robot.colorSensor.red() > 1000) {
            robot.axonServo.setPosition(.50);
            sleep(1000);
            robot.axonServo.setPosition(1.0);
            sleep(1000);
            robot.axonServo.setPosition(-0.5);
            sleep(1000);
            robot.axonServo.setPosition(-1.0);
            sleep(1000);
            robot.axonServo.setPosition(0);
            sleep(1000);
        }
        if (robot.button.isPressed()){
            robot.led.setPosition(.671);
        }


    }
}

