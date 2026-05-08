package org.firstinspires.ftc.team14736.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="BlueLaunchzone", group = "Pedro")
public class PedroBlueLaunchzone12Ball extends PedroBlueFarlaunch12Ball {

    @Override
    public void init() {
        super.init();
        follower.setPose(new Pose(27.5, 127.5, Math.toRadians(138)));
        robot.initLimelight();
        robot.limelightTelemetry();
    }
        class Paths extends PedroBlueFarlaunch12Ball.Paths{
            public Paths(Follower follower) {
                super(follower);
                Launch0 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(27.50, 127.5),

                                        new Pose(58.000, 96.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(138), Math.toRadians(137.5))

                        .build();

                IntakeSetup1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(58.000, 96.000),

                                        new Pose(41.000, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(137.5), Math.toRadians(360))

                        .build();

                Intake1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(41.000, 84.000),

                                        new Pose(17.000, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                        .build();

                Launch1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(17.000, 84.000),

                                        new Pose(58.000, 96.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(137.5))

                        .build();

                IntakeSetup2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(58.000, 96.000),

                                        new Pose(41.000, 60.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(137.5), Math.toRadians(360))

                        .build();

                Intake2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(41.000, 60.000),

                                        new Pose(9.000, 60.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                        .build();

                Launch2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(9.000, 60.000),

                                        new Pose(58.000, 96.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(137.5))

                        .build();

                IntakeSetup3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(58.000, 96.000),

                                        new Pose(41.000, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(137.5), Math.toRadians(360))

                        .build();

                Intake3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(41.000, 35.700),

                                        new Pose(9.000, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                        .build();

                Launch3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(9.000, 35.700),

                                        new Pose(58.000, 96.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(137.5))

                        .build();

                Unpark = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(58.000, 96.000),

                                        new Pose(45.700, 79.800)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(137.5), Math.toRadians(135))

                        .build();
            }
        }

    @Override
    public void start() {
        runPath = new PedroBlueLaunchzone12Ball.Paths(follower);
    }
}
