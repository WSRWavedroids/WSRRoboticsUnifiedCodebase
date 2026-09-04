package org.firstinspires.ftc.teamcode.lessons.one;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Core.LED;
import org.firstinspires.ftc.teamcode.Core.Robot;

@Autonomous(group = "1", name = "Hello/wold")
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
       //the well spins
       //robot.motor.setPower(0.26);
       //robot.motor.setDirection(FORWARD);


       //robot.axonServo.setPosition(0.25);
        // robot.colorServo.setDirection(FORWARD);
        //robot.colorServo.setPower(-0.8);
        //robot.led.setColor(RED);
       if (robot.colorSensor.blue() >= 500) {
           robot.led.setColor(RED);
           sleep(1000);
       }
       robot.telemetry.addData("Pos",robot.axonServo.getPosition());
       sleep(1000);


    }
}

