package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(group =  "Auto", name = "FarLaunch")
public class FarLaunch extends AutonomousPLUS {

    private Robot robot;

    @Override
    public void runOpMode() {
        super.runOpMode();
        robot = new Robot(hardwareMap, telemetry, this);

        waitForStart();
        //Under This Is Were You Put Stuff
        //900 tick = about 20 in
        //550 ticks = about 90 degrees right
        //6 millisecond pause after everything

        sleep(9500); // for wavedroids

        launchBall(100, 450);
        launchBall(100, 500);
        launchBall(100, 800);

        prepareNextAction(6);
        moveRobotForward(550,6,0.38);










    }
}
