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

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousPlusPLUS;
import org.firstinspires.ftc.teamcode.Core.ArtifactLocator;
import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Red Back 12 Ball", group = "1. FABIO WITH PEDRO")
@Configurable // Panels
public class RedBack12Ball extends BlueBack12Ball {


    @Override
    public void init() {
        startPose = new Pose(144-50.188, 9.200, Math.toRadians(90));

        super.init();

        follower.setStartingPose(new Pose(144-50.188, 9.200, Math.toRadians(90)));

        robot.alliance = RED;

        paths = new PathsForBack12Red(follower); // Build paths

        blackboard.put(ALLIANCE_KEY, "RED");

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    public static class PathsForBack12Red extends PathsForBack12Blue {
        public PathsForBack12Red(Follower follower) {
            super(follower);


            LineUpWithClose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-56.500, 9.200),

                                    new Pose(144-42.000, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180-180))

                    .build();

            GrabClose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-42.000, 36.000),

                                    new Pose(144-11.500, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180-180))

                    .build();

            MoveToFireSecond = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-11.500, 36.000),

                                    new Pose(144-56.500, 12.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180-180))

                    .build();

            LineUpWithMiddle = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-56.500, 12.000),

                                    new Pose(144-42.000, 60.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180-180))

                    .build();

            GrabMiddle = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(144-42.000, 60.000),
                                    new Pose(144-36.544, 62.388),
                                    new Pose(144-9.747, 57.576)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180-180))

                    .build();

            MoveToFireThird = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(144-9.747, 57.576),
                                    new Pose(144-45.709, 62.047),
                                    new Pose(144-52.000, 86.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180-180))

                    .build();

            GrabFar = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(144-52.000, 86.000),
                                    new Pose(144-37.444, 82.924),
                                    new Pose(144-19.700, 84.035)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180-180))

                    .build();

            FireLastPattern = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-19.700, 84.035),

                                    new Pose(144-52.000, 86.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180-180))

                    .build();

            UnparkWithRizz = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(144-52.000, 86.000),

                                    new Pose(144-23.506, 71.788)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180-180), Math.toRadians(270-180))

                    .build();
        }
    }
}
