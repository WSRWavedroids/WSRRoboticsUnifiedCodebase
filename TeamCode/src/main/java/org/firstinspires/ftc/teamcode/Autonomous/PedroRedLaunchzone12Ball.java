package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="RedLaunchzone", group = "Pedro")
public class PedroRedLaunchzone12Ball extends PedroBlueFarlaunch12Ball {

    @Override
    public void init() {
        super.init();
        follower.setPose(new Pose(120.25, 130.5, Math.toRadians(45)));
        robot.initLimelight();
        robot.limelightTelemetry();
    }
        class Paths extends PedroBlueFarlaunch12Ball.Paths{
            public Paths(Follower follower) {
                super(follower);
                Launch0 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(120.250, 130.500),

                                        new Pose(86.000, 96.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(36.5), Math.toRadians(42))

                        .build();

                IntakeSetup1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(86.000, 96.000),

                                        new Pose(103.000, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(42), Math.toRadians(180))

                        .build();

                Intake1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(103.000, 84.000),

                                        new Pose(126.000, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                        .build();

                Launch1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(126.000, 84.000),

                                        new Pose(86.000, 96.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(42))

                        .build();

                IntakeSetup2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(86.000, 96.000),

                                        new Pose(103.000, 60.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(42), Math.toRadians(180))

                        .build();

                Intake2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(103.000, 60.000),

                                        new Pose(134.000, 60.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                        .build();

                Launch2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(134.000, 60.000),

                                        new Pose(86.000, 96.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(42))

                        .build();

                IntakeSetup3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(86.000, 96.000),

                                        new Pose(103.000, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(42), Math.toRadians(180))

                        .build();

                Intake3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(103.000, 35.700),

                                        new Pose(134.000, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                        .build();

                Launch3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(134.000, 35.700),

                                        new Pose(86.000, 96.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(42))

                        .build();

                Unpark = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(86.000, 96.000),

                                        new Pose(98.300, 79.800)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(42), Math.toRadians(42))

                        .build();
            }
        }


    @Override
    public void start() {
        runPath = new PedroRedLaunchzone12Ball.Paths(follower);
    }
}