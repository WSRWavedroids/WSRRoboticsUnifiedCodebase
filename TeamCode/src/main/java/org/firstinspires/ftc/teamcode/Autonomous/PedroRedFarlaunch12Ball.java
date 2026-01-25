package org.firstinspires.ftc.teamcode.Autonomous;


import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.INTAKE1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.INTAKE2;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.INTAKE3;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.INTAKESETUP1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.INTAKESETUP2;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.INTAKESETUP3;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.LAUNCH0;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.LAUNCH1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.LAUNCH2;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.LAUNCH3;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.SHOOT0BALL1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.SHOOT1BALL1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.SHOOT2BALL1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.SHOOT3BALL1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.START;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.UNPARK;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroRedFarlaunch12Ball.Steps.*;
import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


@Autonomous(name="PedroRedFarlaunch12Ball", group = "Pedro")
public class PedroRedFarlaunch12Ball extends OpMode {
    PedroRedFarlaunch12Ball.Steps currentstep = START;
    public Follower follower;
    private Robot robot;
    private AutonomousPLUS auto;
    private ElapsedTime cooldown = new ElapsedTime();

    private void nextStep(PedroRedFarlaunch12Ball.Steps nextStep) {
        currentstep = nextStep;
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, this);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(55, 9, Math.PI / 2));
        robot.initLimelight();
    }


    public void loop() {
        robot.setupLaunchers();
        robot.getApriltagDistance();
        telemetry.addData("ODOMETRY X", follower.getPose().getX());
        telemetry.addData("ODOMETRY Y", follower.getPose().getY());
        telemetry.addData("Launch State", robot.doneLaunching);
        telemetry.addData("Cooldown", robot.cooldown);
        telemetry.addData("Follower is Busy", follower.isBusy());
        follower.update();
        switch (currentstep) {
            case START:
                nextStep(LAUNCH0);
                break;

            case LAUNCH0:
                follower.setMaxPower(2);
                follower.followPath(runPath.Launch0);
                nextStep(SHOOT0BALL1);
                break;

            case SHOOT0BALL1:
                if (!follower.isBusy()) {
                    robot.launchLoop(100, 2500);
                    if (robot.doneLaunching) {
                        follower.followPath(runPath.IntakeSetup1);
                        nextStep(INTAKESETUP1);
                    }
                }
                break;

            case INTAKESETUP1:
                if (!follower.isBusy()) {
                    robot.intakeMotor.setPower(1);
                    robot.intake3.setPower(0.55);
                    robot.launchLeft.setPower(0.4);
                    robot.launchRight.setPower(0.4);
                    follower.setMaxPower(0.25);
                    follower.followPath(runPath.Intake1);
                    nextStep(INTAKE1);
                }
                break;

            case INTAKE1:
                if (!follower.isBusy()) {
                    follower.setMaxPower(2);
                    robot.intakeMotor.setPower(1);
                    robot.intake3.setPower(-1);
                    robot.launchLeft.setPower(0);
                    robot.launchRight.setPower(0);
                    follower.followPath(runPath.Launch1);
                    nextStep(LAUNCH1);
                }
                break;

            case LAUNCH1:
                if (!follower.isBusy()) {
                    robot.intakeMotor.setPower(0);
                    robot.intake3.setPower(0);
                    nextStep(SHOOT1BALL1);
                }
                break;

            case SHOOT1BALL1:
                if (!follower.isBusy()) {
                    robot.launchLoop(100, 2500);
                    if (robot.doneLaunching) {
                        follower.followPath(runPath.IntakeSetup2);
                        nextStep(INTAKESETUP2);
                    }
                }
                break;


            case INTAKESETUP2:
                if (!follower.isBusy()) {
                    robot.intakeMotor.setPower(1);
                    robot.intake3.setPower(0.55);
                    robot.launchLeft.setPower(0.4);
                    robot.launchRight.setPower(0.4);
                    follower.setMaxPower(.25);
                    follower.followPath(runPath.Intake2);
                    nextStep(INTAKE2);
                }

                break;

            case INTAKE2:
                if (!follower.isBusy()) {
                    follower.setMaxPower(2);
                    robot.intakeMotor.setPower(1);
                    robot.intake3.setPower(-1);
                    robot.launchLeft.setPower(0);
                    robot.launchRight.setPower(0);
                    follower.followPath(runPath.Launch2);
                    nextStep(LAUNCH2);
                }
                break;

            case LAUNCH2:
                if (!follower.isBusy()) {
                    robot.intakeMotor.setPower(0);
                    robot.intake3.setPower(0);
                    nextStep(SHOOT2BALL1);
                }
                break;

            case SHOOT2BALL1:
                if (!follower.isBusy()) {
                    robot.launchLoop(100, 2500);
                    if (robot.doneLaunching) {
                        follower.followPath(runPath.IntakeSetup3);
                        nextStep(INTAKESETUP3);
                    }
                }

                break;

            case INTAKESETUP3:
                if (!follower.isBusy()) {
                    robot.intakeMotor.setPower(1);
                    robot.intake3.setPower(0.55);
                    robot.launchLeft.setPower(0.4);
                    robot.launchRight.setPower(0.4);
                    follower.setMaxPower(.25);
                    follower.followPath(runPath.Intake3);
                    nextStep(INTAKE3);
                }
                break;

            case INTAKE3:
                if (!follower.isBusy()) {
                    follower.setMaxPower(2);
                    robot.intakeMotor.setPower(1);
                    robot.intake3.setPower(-1);
                    robot.launchLeft.setPower(0);
                    robot.launchRight.setPower(0);
                    follower.followPath(runPath.Launch3);
                    nextStep(LAUNCH3);
                }
                break;

            case LAUNCH3:
                if (!follower.isBusy()) {
                    robot.intakeMotor.setPower(0);
                    robot.intake3.setPower(0);
                    nextStep(SHOOT3BALL1);
                }
                break;

            case SHOOT3BALL1:
                if (!follower.isBusy()) {
                    robot.launchLoop(100, 2500);
                    if (robot.doneLaunching) {
                        follower.followPath(runPath.Unpark);
                        nextStep(UNPARK);
                    }
                }
                break;

            case UNPARK:

                break;
            case END:
                if (!follower.isBusy()) {
                    requestOpModeStop();
                }
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
        UNPARK,
        END;
    }

    class Paths {
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
                                    new Pose(87.800, 7.800),

                                    new Pose(85.900, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(67.5))

                    .build();

            IntakeSetup1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(85.900, 16.600),

                                    new Pose(103.000, 35.700)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(67.5), Math.toRadians(0))

                    .build();

            Intake1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(103.000, 35.700),

                                    new Pose(120.900, 35.700)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                    .build();

            Launch1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(120.900, 35.700),

                                    new Pose(85.900, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(67.5))

                    .build();

            IntakeSetup2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(85.900, 16.600),

                                    new Pose(103.000, 60.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(67.5), Math.toRadians(0))

                    .build();

            Intake2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(103.000, 60.000),

                                    new Pose(120.900, 60.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                    .build();

            Launch2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(120.900, 60.000),

                                    new Pose(85.900, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(67.5))

                    .build();

            IntakeSetup3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(85.900, 16.600),

                                    new Pose(103.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(67.5), Math.toRadians(360))

                    .build();

            Intake3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(103.000, 84.000),

                                    new Pose(120.900, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                    .build();

            Launch3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(120.900, 84.000),

                                    new Pose(85.900, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(67.5))

                    .build();

            Unpark = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(85.900, 16.600),

                                    new Pose(85.900, 30.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(67.5), Math.toRadians(90))

                    .build();
        }
    }

        private Paths runPath;

        public void start() {
            runPath = new Paths(follower);
        }




}





