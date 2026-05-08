package org.firstinspires.ftc.team14736.Autonomous;


import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;



@Autonomous(name="RedFarlaunch", group = "Pedro")
public class PedroRedFarlaunch12Ball extends PedroBlueFarlaunch12Ball {

    @Override
    public void init() {
        super.init();
        follower.setPose(new Pose(85.5, 8.75, Math.PI / 2));
        robot.initLimelight();
        robot.limelightTelemetry();
    }

    class Paths extends PedroBlueFarlaunch12Ball.Paths {
        public Paths(Follower follower) {
            super(follower);
            Launch0 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(85.5, 8.75),

                                    new Pose(85.900, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(67.5))

                    .build();

            IntakeSetup1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(85.900, 16.600),

                                    new Pose(103.000, 35.700)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(67.5), Math.toRadians(180))

                    .build();

            Intake1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(103.000, 35.700),

                                    new Pose(134.000, 35.700)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            Launch1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(134.000, 35.700),

                                    new Pose(85.900, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(67.5))

                    .build();

            IntakeSetup2 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(85.900, 16.600),

                                    new Pose(103.000, 60.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(67.5), Math.toRadians(180))

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

                                    new Pose(85.900, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(67.5))

                    .build();

            IntakeSetup3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(85.900, 16.600),

                                    new Pose(103.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(67.5), Math.toRadians(180))

                    .build();

            Intake3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(103.000, 84.000),

                                    new Pose(126.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            Launch3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(127.000, 84.000),

                                    new Pose(85.900, 16.600)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(67.5))

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

    @Override
    public void start() {
        runPath = new PedroRedFarlaunch12Ball.Paths(follower);
    }

}





