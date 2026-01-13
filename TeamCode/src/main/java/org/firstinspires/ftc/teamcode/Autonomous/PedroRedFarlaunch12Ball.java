package org.firstinspires.ftc.teamcode.Autonomous;

import android.media.audiofx.Visualizer;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class PedroRedFarlaunch12Ball {

public static class Paths {
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

}


