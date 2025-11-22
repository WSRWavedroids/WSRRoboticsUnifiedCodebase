package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(group =  "Auto", name = "RedOnLaunchZoneANGLED")
public class RedOnLaunchZoneANGLED extends AutonomousPLUS {

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
        moveRobotBackward(1370, 6, 0.4);
        runLauncherAuto(0.39);
        sleep(3200);
        runIntake2Auto(1);
        runIntakeAuto("in");
        sleep(110);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(1100);
        runIntake2Auto(1);
        runIntakeAuto("in");
        sleep(125);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(1100);
        runIntake2Auto(1);
        runIntakeAuto("in");
        sleep(300);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        runLauncherstop();
        prepareNextAction(6);
        moveRobotRight(740,6,0.2);

    }
}
//whatever i want
//Jarett Kaedan Butler was here