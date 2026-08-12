package org.firstinspires.ftc.teamcode.lessons.one;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.Robot;


@Autonomous(group = "1", name = "Hello, world!")
@Disabled // this disables the code
// TODO make sure to delete @Disabled before starting program
public class HelloWorld extends LinearOpMode {
    /**
    * this is a robot.
     */
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

        telemetry.addLine("Hello, world!");
        telemetry.update();
        // these two lines control the motor
        robot.motor.setDirection(REVERSE);
        robot.motor.setPower(100);
        // this line controls the color servo
        robot.colorServo.setPower(-3.0);

        // these last four control the axon servo and make it rotate 360 degrees
        robot.axonServo.setPosition(0.25);
        robot.axonServo.setPosition(0.50);
        robot.axonServo.setPosition(0.75);
        robot.axonServo.setPosition(1.00);



        sleep(10000);



    }
}