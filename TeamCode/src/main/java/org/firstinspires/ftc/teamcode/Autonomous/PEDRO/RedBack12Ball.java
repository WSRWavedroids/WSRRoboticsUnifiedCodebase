package org.firstinspires.ftc.teamcode.Autonomous.PEDRO;

import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.*;
import static org.firstinspires.ftc.teamcode.Core.Robot.allianceSides.RED;
import static org.firstinspires.ftc.teamcode.Core.Robot.patternColors.*;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousPlusPLUS;
import org.firstinspires.ftc.teamcode.Core.ArtifactLocator;
import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.Core.TurretLogic;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Red Back 9 Ball", group = "1. FABIO WITH PEDRO")
@Configurable // Panels
public class RedBack12Ball extends BlueBack12Ball {

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


            LineUpWithClose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-56.500, 9.200),

                                    new Pose(144-42.000, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            GrabClose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-42.000, 36.000),

                                    new Pose(144-11.500, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            MoveToFireSecond = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-11.500, 36.000),

                                    new Pose(144-57.500, 12.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            LineUpWithMiddle = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-57.500, 17.000),

                                    new Pose(144-42.000, 60.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            GrabMiddle = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(144-42.000, 60.000),
                                    new Pose(144-36.544, 62.388),
                                    new Pose(144-9.747, 57.576)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            MoveToFireThird = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-9.747, 57.576),
                                    new Pose(144-57.000, 17)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            GrabFar = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(144-52.000, 86.000),
                                    new Pose(144-37.444, 82.924),
                                    new Pose(144-19.700, 84.035)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            FireLastPattern = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-19.700, 84.035),

                                    new Pose(144-52.000, 86.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            UnparkWithRizz = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-57.000, 17),

                                    new Pose(144-30, 12)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))

                    .build();
        }
    }
}
