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
        if (robot.colorSensor.blue() > 250){
            robot.axonServo.setPosition(0.75);



        } else{
            robot.axonServo.setPosition(0.25);
        }

        if (robot.colorSensor.green() > 300){
            robot.motor.setPower(1);



        } else{
            robot.colorServo.setPower(0.50);
        }
        if (robot.axonServo.getPosition()==0.75){
            robot.led.setPosition(0.20);
        } else{
            robot.led.setPosition(0.9);
        }
        sleep(50000);
    }
}
