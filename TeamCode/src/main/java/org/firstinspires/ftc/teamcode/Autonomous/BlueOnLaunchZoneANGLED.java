package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(group =  "Auto", name = "BlueOnLaunchZoneANGLED")
public class BlueOnLaunchZoneANGLED extends AutonomousPLUS {
    public static final String ALLIANCE_KEY = "Alliance";
    private Robot robot;
    @Override
    public void runOpMode() {
        super.runOpMode();
        robot = new Robot(hardwareMap, telemetry, this);
        robot.backRightDrive.setTargetPositionTolerance(8);
        robot.frontRightDrive.setTargetPositionTolerance(8);
        robot.backLeftDrive.setTargetPositionTolerance(8);
        robot.frontLeftDrive.setTargetPositionTolerance(8);
        robot.initLimelight();
        blackboard.put(ALLIANCE_KEY, "BLUE");


        waitForStart();
        //Under This Is Were You Put Stuff
        //900 tick = about 20 in
        //550 ticks = about 90 degrees right
        //6 millisecond pause after everything
        moveRobotBackward(1400, 6, 0.5);

        launchBall(100, 450);
        sleep(150);
        launchBall(100, 500);
        sleep(150);
        launchBall(100, 800);
        sleep(150);
        runLauncherstop();

        prepareNextAction(6);
        runLauncherAuto(-0.6);
        runIntake2Auto(1);
        if (blackboard.get(ALLIANCE_KEY) == "RED"){
            turnRobotRight(777,6); }
        else {
            turnRobotLeft(777,6); }
        if (blackboard.get(ALLIANCE_KEY) == "RED"){
            moveRobotLeft(675,6, 0.5); }
        else {
            moveRobotRight(675,6, 0.5); }
        moveRobotBackward(1200, 6, 0.2);
        turnRobotLeft(100, 6);
        runIntake2Auto(-0.2);
        moveRobotForward(1250, 6, 0.4);
        runIntake2Auto(0);
        if (blackboard.get(ALLIANCE_KEY) == "RED"){
            turnRobotLeft(750,6); }
        else {
            turnRobotRight(750,6); }

        launchBall(100, 450);
        sleep(150);
        launchBall(100, 500);
        sleep(150);
        launchBall(100, 800);
        sleep(150);
        runLauncherstop();

        if (blackboard.get(ALLIANCE_KEY) == "RED"){
            moveRobotRight(800,6, 0.3); }
        else {
            moveRobotLeft(800,6, 0.3); }
    }
}
//whatever i want
//Jarett Kaedan Butler was here
