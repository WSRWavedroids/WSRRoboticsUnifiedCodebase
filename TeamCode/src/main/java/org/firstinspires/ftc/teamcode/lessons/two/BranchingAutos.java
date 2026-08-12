package org.firstinspires.ftc.teamcode.lessons.two;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.Robot;

@Autonomous(group = "2", name = "BranchingAutos")
public class BranchingAutos extends LinearOpMode {
    private Robot robot;

    /**
     * This makes the axon servo rotate to 180 degrees with clearer than a percentage
     */
    private static final double One_Eighty_Degrees = 0.75;

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
            robot.axonServo.setPosition(One_Eighty_Degrees);



        } else{
            robot.axonServo.setPosition(0.25);
        }
        /* Makes it so if it's blue or green, it will set the axon servo to 270 degrees clockwise
        If it's not, then it will set it to 90 degrees
         */

        if (robot.colorSensor.green() > 300){
            robot.motor.setPower(1);
        // If it's green, spin the motor at full speed


        } else{
            robot.colorServo.setPower(0.50);
        }
        //If it isn't green, spin the color servo at half speed
        if (robot.axonServo.getPosition()==0.75){
            robot.led.setPosition(0.20);
        } else{
            robot.led.setPosition(0.9);
        }
        // TODO make this code look better
        sleep(50000);
    }
}
