package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.INTAKE1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.INTAKE2;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.INTAKE3;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.INTAKESETUP1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.INTAKESETUP2;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.INTAKESETUP3;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.LAUNCH0;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.LAUNCH1;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.LAUNCH2;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.LAUNCH3;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.START;
import static org.firstinspires.ftc.teamcode.Autonomous.PedroBlueFarlaunch12Ball.Steps.UNPARK;

import android.widget.Switch;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


@Autonomous(name="PedroBlue" +
        "Farlaunch12Ball")
public class PedroBlueFarlaunch12Ball extends OpMode {
    Steps currentstep = START;
public Follower follower;
private Robot robot;
private AutonomousPLUS auto;
    private void nextStep(Steps nextStep){
        currentstep = nextStep;
    }

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, this);
        auto = new AutonomousPLUS();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(55,9,Math.PI/2));
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
        follower.update();
        switch(currentstep){
            case START:
                nextStep(LAUNCH0);
                break;
            case LAUNCH0:
                    nextStep(INTAKESETUP1);
                    follower.followPath(runPath.Launch0);
                break;

            case INTAKESETUP1:
                if(!follower.isBusy()) {
                    nextStep(INTAKE1);
                    follower.followPath(runPath.Intake1);
                }
                break;

            case INTAKE1:
                if(!follower.isBusy()) {
                    nextStep(LAUNCH1);
                    follower.followPath(runPath.Launch1);
                }
                break;

            case LAUNCH1:
                if(!follower.isBusy()) {
                    nextStep(INTAKESETUP2);
                    follower.followPath(runPath.IntakeSetup2);
                }
                break;

            case INTAKESETUP2:
                if(!follower.isBusy()) {
                    nextStep(INTAKE2);
                }

                break;

            case INTAKE2:
                if(!follower.isBusy()) {
                    nextStep(LAUNCH2);
                    follower.followPath(runPath.Launch2);
                }

                break;

            case LAUNCH2:
                if(!follower.isBusy()) {
                    nextStep(INTAKESETUP3);
                    follower.followPath(runPath.IntakeSetup3);
                }
                break;

            case INTAKESETUP3:
                if(!follower.isBusy()) {
                    nextStep(INTAKE3);
                    follower.followPath(runPath.Intake3);
                }
                break;

            case INTAKE3:
                if(!follower.isBusy()) {
                    nextStep(LAUNCH3);
                }
                break;

            case LAUNCH3:
                if(!follower.isBusy()) {
                    nextStep(UNPARK);
                    follower.followPath(runPath.Unpark);
                }
                break;

            case UNPARK:

                break;

        }

        telemetry.addData("Current Step", currentstep);
    }

    enum Steps {
        START,
        LAUNCH0,
        INTAKESETUP1,
        INTAKE1,
        LAUNCH1,
        INTAKESETUP2,
        INTAKE2,
        LAUNCH2,
        INTAKESETUP3,
        INTAKE3,
        LAUNCH3,
        UNPARK
    }






}