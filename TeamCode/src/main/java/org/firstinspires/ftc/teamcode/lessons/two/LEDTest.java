package org.firstinspires.ftc.teamcode.lessons.two;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.GREEN;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.OFF;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.SAGE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.EasyLED;
import org.firstinspires.ftc.teamcode.Core.LED;
import org.firstinspires.ftc.teamcode.Core.Robot;

@Autonomous(group = "2", name = "LEDTest")
public class LEDTest extends LinearOpMode {
    private Robot robot;

    public void runOpMode() {

        robot = new Robot(hardwareMap, telemetry, this);
        LED Leds = robot.led;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        while (opModeInInit()) {
            // This is the equivalent of init_loop(). It will repeat until the play button is
            // pressed.
            sleep(1);
        }


        robot.readyHardware();

        waitForStart();
        while(true) {
            Leds.setColor(SAGE);
            sleep(1000);
            Leds.setColor(OFF);
            sleep(1000);
            Leds.setColor(GREEN);
            sleep(1000);
        }





    }
}

