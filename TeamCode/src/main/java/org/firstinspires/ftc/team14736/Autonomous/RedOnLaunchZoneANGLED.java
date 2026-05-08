package org.firstinspires.ftc.team14736.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(group =  "Auto", name = "RedOnLaunchZoneANGLED")
public class RedOnLaunchZoneANGLED extends AutonomousPLUS {
    public static final String ALLIANCE_KEY = "Alliance";
    @Override
    public void runOpMode() {
        super.runOpMode();
        robot.backRightDrive.setTargetPositionTolerance(8);
        robot.frontRightDrive.setTargetPositionTolerance(8);
        robot.backLeftDrive.setTargetPositionTolerance(8);
        robot.frontLeftDrive.setTargetPositionTolerance(8);
        robot.initLimelight();
        blackboard.put(ALLIANCE_KEY, "RED");

        waitForStart();
        //Under This Is Were You Put Stuff
        //900 tick = about 20 in
        //550 ticks = about 90 degrees right
        //6 millisecond pause after everything
        moveRobotBackward(1250, 6, 0.5);
        prepareNextAction(6);

        launchBall(100, 450);
        launchBall(100, 500);
        launchBall(100, 900);
        runLauncherstop();

        prepareNextAction(6);
        runLauncherAuto(-0.6);
        runIntake2Auto(0.75);
        if (blackboard.get(ALLIANCE_KEY) == "RED"){
            turnRobotRight(775,6); }
        else {
            turnRobotLeft(875,6); }
        if (blackboard.get(ALLIANCE_KEY) == "RED"){
            moveRobotLeft(700,6, 0.5);
        }
        else {
            moveRobotRight(670,6, 0.5);
        }
        moveRobotBackward(1150, 6, 0.11);
        runIntake2Auto(-0.2);
        moveRobotForward(1150, 6, 0.4);
        runIntake2Auto(0);
        if (blackboard.get(ALLIANCE_KEY) == "RED"){
            moveRobotRight(700,6, 0.5);
        }
        else {
            moveRobotLeft(625  ,6, 0.5);
        }
        if (blackboard.get(ALLIANCE_KEY) == "RED"){
            turnRobotLeft(880,6);
        }
        else {
            turnRobotRight(840,6);
        }
        moveRobotBackward(175,20,0.5);

        launchBall(100, 450);
        prepareNextAction(50);
        launchBall(100, 500);
        launchBall(100, 900);
        runLauncherstop();

        if (blackboard.get(ALLIANCE_KEY) == "RED"){
            moveRobotRight(800,6, 0.5);
        }
        else {
            moveRobotLeft(800,6, 0.5);
        }
    }
}
//whatever i want
//Jarett Kaedan Butler was here