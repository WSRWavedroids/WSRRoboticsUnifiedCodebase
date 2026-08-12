package org.firstinspires.ftc.teamcode.lessons.two;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.EasyLED;
import org.firstinspires.ftc.teamcode.Core.Robot;

@Autonomous(group = "2", name = "LEDTest")
public class LEDTest extends LinearOpMode {
    private Robot robot;
    long sleepTime = 1000;

    public void runOpMode() {

        robot = new Robot(hardwareMap, telemetry, this);
        EasyLED Leds = new EasyLED(robot.led);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        while (opModeInInit()) {
            // This is the equivalent of init_loop(). It will repeat until the play button is
            // pressed.
            sleep(1);
        }


        robot.readyHardware();

        //The below turns the light green, then off, then a slightly different shade of green with one second of each
        //TODO find out why the off value doesn't actually turn off the light and how to make it work

        waitForStart();
        while(true) {
            Leds.setColor(EasyLED.Color.SAGE);
            sleep(sleepTime);
            Leds.setColor(EasyLED.Color.OFF);
            sleep(sleepTime);
            Leds.setColor(EasyLED.Color.GREEN);
            sleep(sleepTime);
        }





    }
}

