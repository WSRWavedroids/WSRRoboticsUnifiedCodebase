package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(group =  "Auto", name = "RedOnLaunchZoneANGLED")
public class RedOnLaunchZoneANGLED extends AutonomousPLUS {

    private Robot robot;
    private double spd = 0.46;
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
        moveRobotBackward(1400, 6, 0.5);
        runLauncherAuto(spd - 0.05); //Launch
        sleep(1200);
        runIntake2Auto(0.6);
        runIntakeAuto("in");
        sleep(95);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        runLauncherAuto(spd); //Launch
        sleep(300);

        runIntake2Auto(0);
        sleep(500);
        runIntake2Auto(0.6);
        runIntakeAuto("in");
        sleep(140);
        runIntake2Auto(0.4);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        runLauncherAuto(spd); //Launch
        sleep(300);

        runIntake2Auto(-0.3);
        sleep(500);
        runIntake2Auto(0.6);
        runIntakeAuto("in");
        sleep(450);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        sleep(75);
        runLauncherstop();

        prepareNextAction(6);
        runLauncherAuto(-0.6);
        runIntake2Auto(0.6);
        turnRobotRight(777,6);               //mirror
        moveRobotLeft(675, 6, 0.5);     //mirror
        moveRobotBackward(1250, 6, 0.3);
        turnRobotLeft(100, 6);
        runIntake2Auto(-0.2);
        moveRobotForward(1300, 6, 0.5);
        runIntake2Auto(0);
        turnRobotLeft(750,6);               //mirror

        runLauncherAuto(0.36); //Launch
        sleep(1200);
        runIntake2Auto(0.6);
        runIntakeAuto("in");
        sleep(95);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        runLauncherAuto(spd); //Launch
        sleep(300);

        runIntake2Auto(0);
        sleep(500);
        runIntake2Auto(0.6);
        runIntakeAuto("in");
        sleep(140);
        runIntake2Auto(0.4);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        runLauncherAuto(spd); //Launch
        sleep(300);

        runIntake2Auto(-0.3);
        sleep(500);
        runIntake2Auto(0.6);
        runIntakeAuto("in");
        sleep(450);
        runIntake2Auto(0);
        runIntakeAuto("stop");
        sleep(75);
        runIntake2Auto(-0.3);
        sleep(75);
        runLauncherstop();

        moveRobotRight(800, 6, 0.3);        //mirror

    }
}
//whatever i want
//Jarett Kaedan Butler was here