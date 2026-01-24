package org.firstinspires.ftc.teamcode.Autonomous.PEDRO;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomous.AutonomousPlusPLUS;
import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Red Front 12 Ball", group = "Autonomous")
@Configurable // Panels
public class RedFront12Ball extends BlueFront12Ball {

    @Override
    public void init() {

        robot = new Robot(hardwareMap, telemetry, this);
        auto = new AutonomousPlusPLUS(robot);

        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(144-22.465, 123.866, Math.toRadians(125)));

        paths = new PathsForFront12Red(follower, pathTimer); // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    public static class PathsForFront12Red extends PathsForFront12Blue {
        public PathsForFront12Red(Follower follower, Timer pathTimer) {
            super(follower, pathTimer);
            MoveAwayFromGoal = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(144-22.465, 123.866), new Pose(144-53.408, 112.845))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(125), Math.toRadians(35))
                    .build();


            LineUpWithCenterBalls = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(144-53.408, 112.845), new Pose(144-52.984, 58.378))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(35), Math.toRadians(0))
                    .build();

            GrabCenterBalls = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(144-52.984, 58.378), new Pose(144-8.477, 58.378))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

             MoveToLetBallsOut = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(144-8.477, 58.378),
                                    new Pose(144-36.665, 51.596),
                                    new Pose(144-16.107, 68.975)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                    .build();


            MoveToScoreSecondPattern = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(144-16.107, 68.975),
                                    new Pose(144-50.865, 65.584),
                                    new Pose(144-53.408, 112.845)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(35))
                    .build();


            LineUpToGrabCloseBalls = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(144-53.408, 112.845), new Pose(144-53.620, 84.658))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(35), Math.toRadians(0))
                    .build();

            GrabCloseBalls = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(144-53.620, 84.658), new Pose(144-18.650, 84.234))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            MoveToFireThirdPattern = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(144-18.650, 84.234), new Pose(144-53.408, 113.269))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(35))
                    .build();


            LineUpWithFarBalls = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(144-53.408, 113.269), new Pose(144-52.560, 35.065))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(35), Math.toRadians(0))
                    .build();

            GrabFarBalls = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(144-52.560, 35.065), new Pose(144-9.325, 34.641))
                    )
                    .setTangentHeadingInterpolation()
                    .build();

            MoveForFinalPattern = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(144-9.325, 34.641), new Pose(144-53.408, 113.481))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(35))
                    .build();

        }
    }
}
