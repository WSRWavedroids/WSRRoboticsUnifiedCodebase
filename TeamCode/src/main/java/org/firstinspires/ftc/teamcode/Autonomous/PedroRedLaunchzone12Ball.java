package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PedroRedLaunchzone12Ball", group = "Pedro")
public class PedroRedLaunchzone12Ball extends AutonomousPLUS {

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
                                        new Pose(119.600, 130.000),

                                        new Pose(81.700, 94.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(36.5), Math.toRadians(45))

                        .build();

                IntakeSetup1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(81.700, 94.000),

                                        new Pose(103.000, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(180))

                        .build();

                Intake1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(103.000, 84.000),

                                        new Pose(120.900, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                        .build();

                Launch1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(120.900, 84.000),

                                        new Pose(81.700, 94.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(45))

                        .build();

                IntakeSetup2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(81.700, 94.000),

                                        new Pose(103.000, 60.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(180))

                        .build();

                Intake2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(103.000, 60.000),

                                        new Pose(120.900, 60.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                        .build();

                Launch2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(120.900, 60.000),

                                        new Pose(81.700, 94.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(45))

                        .build();

                IntakeSetup3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(81.700, 94.000),

                                        new Pose(103.000, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(180))

                        .build();

                Intake3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(103.000, 35.700),

                                        new Pose(120.900, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                        .build();

                Launch3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(120.900, 35.700),

                                        new Pose(81.700, 94.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(45))

                        .build();

                Unpark = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(81.700, 94.000),

                                        new Pose(98.300, 79.800)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(45), Math.toRadians(45))

                        .build();
            }
        }
    }
}
