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
        robot.backRightDrive.setTargetPositionTolerance(8);
        robot.frontRightDrive.setTargetPositionTolerance(8);
        robot.backLeftDrive.setTargetPositionTolerance(8);
        robot.frontLeftDrive.setTargetPositionTolerance(8);

        waitForStart();
        //Under This Is Were You Put Stuff
        //900 tick = about 20 in
        //550 ticks = about 90 degrees right
        //6 millisecond pause after everything
        moveRobotBackward(1370, 6, 0.5);
        runLauncherAuto(0.33); //Launch
        sleep(1200);
        runIntake2Auto(0.8);
        runIntakeAuto("in");
        sleep(70);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        runLauncherAuto(0.38);
        sleep(300);

        runIntake2Auto(0);
        sleep(300);
        runIntake2Auto(0.8);
        runIntakeAuto("in");
        sleep(140);
        runIntake2Auto(0.4);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        runLauncherAuto(0.38);
        sleep(300);

        runIntake2Auto(-0.3);
        sleep(300);
        runIntake2Auto(0.8);
        runIntakeAuto("in");
        sleep(450);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        sleep(75); //Launch
        runLauncherstop();

        prepareNextAction(6);
        runLauncherAuto(-0.6);
        runIntake2Auto(0.6);
        turnRobotRight(777,6);               //mirror
        moveRobotLeft(675, 6, 0.5);     //mirror
        moveRobotBackward(1100, 6, 0.4);
        turnRobotLeft(100, 6);
        runIntake2Auto(-0.2);
        moveRobotForward(1100, 6, 0.5);
        runIntake2Auto(0);
        turnRobotLeft(750,6);               //mirror

        runLauncherAuto(0.33); //Launch
        sleep(1200);
        runIntake2Auto(0.8);
        runIntakeAuto("in");
        sleep(70);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        runLauncherAuto(0.38);
        sleep(300);

        runIntake2Auto(0);
        sleep(300);
        runIntake2Auto(0.8);
        runIntakeAuto("in");
        sleep(140);
        runIntake2Auto(0.4);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        runLauncherAuto(0.38);
        sleep(300);

        runIntake2Auto(-0.3);
        sleep(300);
        runIntake2Auto(0.8);
        runIntakeAuto("in");
        sleep(450);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        sleep(75); //Launch
        runLauncherstop();

        moveRobotRight(800, 6, 0.3);        //mirror

    }
}
//whatever i want
//Jarett Kaedan Butler was here