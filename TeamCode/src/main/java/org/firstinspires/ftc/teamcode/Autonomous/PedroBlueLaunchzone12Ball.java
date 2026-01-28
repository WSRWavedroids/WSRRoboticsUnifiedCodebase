package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="PedroBlueLaunchzone12Ball", group = "Pedro")
public class PedroBlueLaunchzone12Ball extends PedroBlueFarlaunch12Ball {

    @Override
    public void init() {
        super.init();
        follower.setStartingPose(new Pose(23.75, 130.5, Math.toRadians(143.5))); //todo be cool like michael
    }
        class Paths extends PedroBlueFarlaunch12Ball.Paths{
            public Paths(Follower follower) {
                super(follower);
                Launch0 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(23.750, 130.500),

                                        new Pose(62.300, 94.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(143.5), Math.toRadians(135))

                        .build();

                IntakeSetup1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(62.300, 94.000),

                                        new Pose(44.000, 84.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(360))

                        .build();

                Intake1 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(44.000, 84.000),

                                        new Pose(17.5, 84.000)
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

                                        new Pose(44.000, 60.000)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(360))

                        .build();

                Intake2 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(44.000, 60.000),

                                        new Pose(8.5, 60.000)
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

                                        new Pose(44.000, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(135), Math.toRadians(360))

                        .build();

                Intake3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(44.000, 35.700),

                                        new Pose(8.500, 35.700)
                                )
                        ).setLinearHeadingInterpolation(Math.toRadians(360), Math.toRadians(360))

                        .build();

                Launch3 = follower.pathBuilder().addPath(
                                new BezierLine(
                                        new Pose(8.500, 35.700),

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
    @Override
    public void start() {
        runPath = new PedroBlueLaunchzone12Ball.Paths(follower);
    }
}
