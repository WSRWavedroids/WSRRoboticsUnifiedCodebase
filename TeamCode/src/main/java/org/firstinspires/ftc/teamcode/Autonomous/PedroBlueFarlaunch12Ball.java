package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.*;

import android.widget.Switch;

import com.pedropathing.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.localization.Localizer;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


@Autonomous(name="PedroBlue" + "Farlaunch12Ball", group = "Pedro")
public class PedroBlueFarlaunch12Ball extends OpMode {
    Steps currentstep = START;
public Follower follower;
private Robot robot;
private AutonomousPLUS auto;
private ElapsedTime cooldown = new ElapsedTime();
    private void nextStep(Steps nextStep){
        currentstep = nextStep;
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, this);
        follower = Constants.createFollower(hardwareMap);
        robot.initLimelight();
    }

    static class Paths {
        public PathChain Launch0;
        public PathChain IntakeSetup1;
        public PathChain Intake1;
        public PathChain Launch1;
        public PathChain IntakeSetup2;
        public PathChain Intake2;
        public PathChain Launch2;
        public PathChain IntakeSetup3;
        public PathChain Intake3;
        public PathChain Launch3;
        public PathChain Unpark;

        public Paths(Follower follower) {
            Launch0 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(55.000, 9.000),

                                    new Pose(58.100, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(112.5))

                    .build();

            IntakeSetup1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(58.100, 16.600),

                                    new Pose(41.000, 35.700)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(112.5), Math.toRadians(360))

                    .build();

            Intake1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(41.000, 35.700),

                                    new Pose(23.100, 35.700)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                    .build();

            Launch1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(23.100, 35.700),

                                    new Pose(58.100, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(112.5))

                    .build();

            IntakeSetup2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(58.100, 16.600),

                                    new Pose(41.000, 60.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(112.5), Math.toRadians(360))

                    .build();

            Intake2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(41.000, 60.000),

                                    new Pose(23.100, 60.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                    .build();

            Launch2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(23.100, 60.000),

                                    new Pose(58.100, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(112.5))

                    .build();

            IntakeSetup3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(58.100, 16.600),

                                    new Pose(41.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(112.5), Math.toRadians(360))

                    .build();

            Intake3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(41.000, 84.000),

                                    new Pose(23.100, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                    .build();

            Launch3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(23.100, 84.000),

                                    new Pose(58.100, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(112.5))

                    .build();

            Unpark = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(58.100, 16.600),

                                    new Pose(58.100, 30.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(112.5), Math.toRadians(90))

                    .build();
        }



    }

    private Paths runPath;

    public void start() {
        runPath = new Paths(follower);
    }
    public void loop() {
        robot.launcherMath(robot.getApriltagDistance(),
                23.41447,
                274.95936,
                1173.87201,
                1973.79332,
                1897);
        telemetry.addData("ODOMETRY X", follower.getPose().getX());
        telemetry.addData("ODOMETRY Y", follower.getPose().getY());
        follower.update();
        switch(currentstep){
            case START:
                nextStep(LAUNCH0);
                break;
            case LAUNCH0:
                follower.followPath(runPath.Launch0);
                nextStep(UNPARK); //TODO change to RUNLAUNCH0 once it works ALSO ADD OTHER 3 BALLS
                break;
            case SHOOT0BALL1:
                if(!follower.isBusy()) {
                    if (!robot.doneLaunching) {
                        robot.launchLoop(400,500);
                    } else {
                        follower.followPath(runPath.IntakeSetup1);
                        nextStep(INTAKESETUP1);
                    }
                }
                break;
            case INTAKESETUP1:
                if(!follower.isBusy()) {
                    robot.intakeMotor.setPower(1);
                    robot.intake3.setPower(0.8);
                    follower.followPath(runPath.Intake1);
                    nextStep(INTAKE1);
                }
                break;

            case INTAKE1:
                if(!follower.isBusy()) {
                    robot.intakeMotor.setPower(0);
                    robot.intake3.setPower(0);
                    follower.followPath(runPath.Launch1);
                    nextStep(LAUNCH1);
                }
                break;


            case LAUNCH1:
                if(!follower.isBusy()) {
                    nextStep(SHOOT1BALL1);
                }
                break;
            case SHOOT1BALL1:
                if(!follower.isBusy()) {
                    if (!robot.doneLaunching) {
                        robot.launchLoop(400,500);
                    } else {
                        follower.followPath(runPath.IntakeSetup2);
                        nextStep(INTAKESETUP2);
                    }
                }
            case INTAKESETUP2:
                if(!follower.isBusy()) {
                    robot.intakeMotor.setPower(1);
                    robot.intake3.setPower(0.8);
                    follower.followPath(runPath.Intake2);
                    nextStep(INTAKE2);
                }

                break;

            case INTAKE2:
                if(!follower.isBusy()) {
                    robot.intakeMotor.setPower(0);
                    robot.intake3.setPower(0);
                    follower.followPath(runPath.Launch2);
                    nextStep(LAUNCH2);
                }

                break;

            case LAUNCH2:
                if(!follower.isBusy()) {
                    nextStep(SHOOT2BALL1);
                }
                break;
            case SHOOT2BALL1:
                if(!follower.isBusy()) {
                    if (!robot.doneLaunching) {
                        robot.launchLoop(400,500);
                    } else {
                        follower.followPath(runPath.IntakeSetup3);
                        nextStep(INTAKESETUP3);
                    }
                }
            case INTAKESETUP3:
                if(!follower.isBusy()) {
                    robot.intakeMotor.setPower(1);
                    robot.intake3.setPower(0.8);
                    follower.followPath(runPath.Intake3);
                    nextStep(INTAKE3);
                }
                break;

            case INTAKE3:
                if(!follower.isBusy()) {
                    robot.intakeMotor.setPower(0);
                    robot.intake3.setPower(0);
                    follower.followPath(runPath.Launch3);
                    nextStep(LAUNCH3);
                }
                break;

            case LAUNCH3:
                if(!follower.isBusy()) {
                    nextStep(SHOOT3BALL1);
                }
                break;
            case SHOOT3BALL1:
                if(!follower.isBusy()) {
                    if (!robot.doneLaunching) {
                        robot.launchLoop(400,500);
                    } else {
                        follower.followPath(runPath.Unpark);
                        nextStep(UNPARK);
                    }
                }
            case UNPARK:

                break;

        }

        telemetry.addData("Current Step", currentstep);
    }

    enum Steps {
        START,
        LAUNCH0,
        SHOOT0BALL1,
        SHOOT0BALL2,
        SHOOT0BALL3,
        INTAKESETUP1,
        INTAKE1,
        LAUNCH1,
        SHOOT1BALL1,
        SHOOT1BALL2,
        SHOOT1BALL3,
        INTAKESETUP2,
        INTAKE2,
        LAUNCH2,
        SHOOT2BALL1,
        SHOOT2BALL2,
        SHOOT2BALL3,
        INTAKESETUP3,
        INTAKE3,
        LAUNCH3,
        SHOOT3BALL1,
        SHOOT3BALL2,
        SHOOT3BALL3,
        UNPARK
    }






}