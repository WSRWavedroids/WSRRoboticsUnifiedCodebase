package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(group =  "Auto", name = "BlueOnLaunchZoneANGLED")
public class BlueOnLaunchZoneANGLED extends AutonomousPLUS {

    private Robot robot;
    private double spd = 0.43;
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
        sleep(70);
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
        turnRobotLeft(777,6);               //mirror
        moveRobotRight(675, 6, 0.5);     //mirror
        moveRobotBackward(1100, 6, 0.4);
        turnRobotLeft(100, 6);
        runIntake2Auto(-0.2);
        moveRobotForward(1100, 6, 0.5);
        runIntake2Auto(0);
        turnRobotRight(750,6);               //mirror

        runLauncherAuto(0.36); //Launch
        sleep(1200);
        runIntake2Auto(0.6);
        runIntakeAuto("in");
        sleep(70);
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

        moveRobotLeft(800, 6, 0.3);        //mirror

    }
}
//whatever i want
//Jarett Kaedan Butler was here
