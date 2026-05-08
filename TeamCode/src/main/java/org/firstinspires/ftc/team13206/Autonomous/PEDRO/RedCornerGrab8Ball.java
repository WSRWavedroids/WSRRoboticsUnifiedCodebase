package org.firstinspires.ftc.team13206.Autonomous.PEDRO;

import static org.firstinspires.ftc.team13206.Core.Robot.allianceSides.RED;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team13206.Autonomous.AutonomousPlusPLUS;
import org.firstinspires.ftc.team13206.Core.Robot;
import org.firstinspires.ftc.team13206.Core.TurretLogic;

@Autonomous(name = "Red Corner Grab 9 Ball", group = "1. FABIO WITH PEDRO")
@Configurable // Panels
public class RedCornerGrab8Ball extends BlueCornerGrab8Ball {

    private Pose startPose = new Pose(144-56.5, 9.200, Math.toRadians(0));

    public void init() {

        super.init();
        // Override the alliance key set in the blue auto to run the red one
        robot = new Robot(hardwareMap, telemetry, this);
//        TurretLogic.tolerance = robot.turret.degreesToTicks(8);
        auto = new AutonomousPlusPLUS(robot);
        robot.turret.activeMode = TurretLogic.controlMode.FULL;

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        robot.randomizationScanner.InitLimeLight(0);

        stallTimer = new ElapsedTime();

        robot.turret.follower.setPose(startPose);
        robot.turret.follower.setHeading(startPose.getHeading());
        blackboard.put(ALLIANCE_KEY, "RED");
        robot.alliance = RED;
        panelsTelemetry.debug("Status", "Initialized");
        paths = new PathsForBack12Red(follower);
        panelsTelemetry.update(telemetry);
        robot.turret.blackboardSafe = true;
    }

    public void init_loop() {
        super.init_loop();
    }

    public void start() {
        super.start();
    }

    public void loop() {
        super.loop();
    }

    public void stop() {
        super.stop();
    }

    public static class PathsForBack12Red extends PathsForBack12Blue {
        public PathsForBack12Red(Follower follower) {
            super(follower);
            Lineupwithclose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-56.500, 9.200),

                                    new Pose(144-43, 36.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                    .build();

            GrabClose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-43, 36.000),

                                    new Pose(144-11.500, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            MovetoFireSecondPattern = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-11.500, 36.000),

                                    new Pose(144-57.500, 17.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            GrabFirstCornerBall = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-57.500, 17.000),

                                    new Pose(144-11.718, 16.953)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-20))

                    .build();

            GrabSecondCornerBall = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(144-11.718, 16.953),
                                    new Pose(144-23.588, 16.147),
                                    new Pose(144-11.718, 11.494)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-20), Math.toRadians(-20))

                    .build();

            GrabLastCornerBall = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(144-11.718, 11.494),
                                    new Pose(144-22.900, 10.556),
                                    new Pose(144-12.176, 9.500)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(-20), Math.toRadians(0))

                    .build();

            MoveToFireCornerBalls = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-12.176, 9.500),

                                    new Pose(144-57.500, 17.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                    .build();

            GrabOverflow = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-57.500, 17.000),

                                    new Pose(144-12.212, 20.541)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(30))

                    .build();

            FireOverflow = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-12.212, 20.541),

                                    new Pose(144-57.500, 17.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(30), Math.toRadians(0))

                    .build();

            Unpark = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-57.500, 17.000),

                                    new Pose(144-36.718, 14.882)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                    .build();
        }
    }
}
