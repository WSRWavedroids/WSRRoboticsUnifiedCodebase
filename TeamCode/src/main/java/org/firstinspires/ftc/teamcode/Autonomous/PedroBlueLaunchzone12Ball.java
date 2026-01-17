package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PedroBlueLaunchzone12Ball", group = "Pedro")
public class PedroBlueLaunchzone12Ball extends AutonomousPLUS {

    @Override
    public void runOpMode() {
        super.runOpMode();
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
                                        new Pose(24.400, 130.000),

                                        new Pose(62.300, 94.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(143.5), Math.toRadians(135))

                        .build();

                IntakeSetup1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(62.300, 94.000),

                                        new Pose(41.000, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(360))

                        .build();

                Intake1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(41.000, 84.000),

                                        new Pose(23.100, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                        .build();

                Launch1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(23.100, 84.000),

                                        new Pose(62.300, 94.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(135))

                        .build();

                IntakeSetup2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(62.300, 94.000),

                                        new Pose(41.000, 60.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(360))

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

                                        new Pose(62.300, 94.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(135))

                        .build();

                IntakeSetup3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(62.300, 94.000),

                                        new Pose(41.000, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(360))

                        .build();

                Intake3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(41.000, 35.700),

                                        new Pose(23.100, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                        .build();

                Launch3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(23.100, 35.700),

                                        new Pose(62.300, 94.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(135))

                        .build();

                Unpark = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(62.300, 94.000),

                                        new Pose(45.700, 79.800)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(135))

                        .build();
            }
        }
    }
}
