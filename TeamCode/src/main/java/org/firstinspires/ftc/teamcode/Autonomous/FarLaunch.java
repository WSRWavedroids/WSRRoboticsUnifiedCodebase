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
        sleep(10000);
        runLauncherAuto(0.54); //Launch
        sleep(4000);
        runIntake2Auto(1);
        runIntakeAuto("in");
        sleep(70);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        sleep(75);
        runIntake2Auto(0);
        runLauncherAuto(0.52);
        sleep(1400);
        runIntake2Auto(1);
        runIntakeAuto("in");
        sleep(125);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        sleep(75);
        runIntake2Auto(0);
        runLauncherAuto(0.52);
        sleep(1500);
        runIntake2Auto(1);
        runIntakeAuto("in");
        sleep(800);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        sleep(75); //Launch
        runLauncherstop();
        prepareNextAction(6);
        moveRobotForward(550,6,0.38);










    }
}
