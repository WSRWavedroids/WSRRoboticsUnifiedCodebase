package org.firstinspires.ftc.teamcode.Autonomous.PEDRO;

import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.*;
import static org.firstinspires.ftc.teamcode.Core.Robot.allianceSides.BLUE;
import static org.firstinspires.ftc.teamcode.Core.Robot.patternColors.*;
import static org.firstinspires.ftc.teamcode.Autonomous.PEDRO.BlueBack12Ball.Steps.*;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.FIRE;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.LOAD;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.STORE;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.control.FilteredPIDFCoefficients;
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
import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.Core.TurretLogic;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Blue Back 12 Ball", group = "1. FABIO WITH PEDRO")
@Configurable // Panels
public class BlueBack12Ball extends OpMode {

    public Robot robot = null;
    public AutonomousPlusPLUS auto = null;
    TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    int pathState; // Current autonomous path state (state machine)
    PathsForBack12Blue paths; // Paths defined in the Paths class

    public static final String ALLIANCE_KEY = "Alliance"; //For blackboard
    public static final String PATTERN_KEY = "Pattern";
    public ElapsedTime stallTimer;
    Pose startPose = new Pose(56.5, 9.200, Math.toRadians(180));
    // Make sure this is set HEREqa
    Timer pathTimer, actionTimer, opmodeTimer;

    @Override
    public void init() {

        robot = new Robot(hardwareMap, telemetry, this);
//        TurretLogic.tolerance = TurretLogic.degreesToTicks(8);
        follower = robot.turret.follower;
        follower.setMaxPowerScaling(1);
        follower.setMaxPower(1);
        robot.callPartialPedro = false;
        panelsTelemetry = Robot.panelsTelemetry;
        opmodeTimer = new Timer();
        pathTimer = new Timer();
        actionTimer = new Timer();
        telemetry.addData("tolerance value test pt 1", TurretLogic.tolerance);
        telemetry.addData("tolerance value test pt 2", TurretLogic.tolerance);
        auto = new AutonomousPlusPLUS(robot);
        TurretLogic.activeMode = TurretLogic.controlMode.FULL;

        robot.randomizationScanner.InitLimeLight(0);
        blackboard.put(ALLIANCE_KEY, "BLUE");
        stallTimer = new ElapsedTime();

        robot.turret.follower.setPose(startPose);
        robot.turret.follower.setHeading(startPose.getHeading());

        robot.alliance = BLUE;

        paths = new PathsForBack12Blue(follower); // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    public void init_loop() {
        robot.pattern = robot.randomizationScanner.GetRandomization();
        telemetry.addData(String.valueOf(robot.pattern), " Works!");
        telemetry.update();

        telemetry.addData("Our pattern is: ", String.valueOf(robot.pattern), " ...yay");

        switch (robot.pattern) {
            case PPG:
                telemetry.addData("We doin", " PPG now");
                blackboard.put(PATTERN_KEY, "PPG");
                break;
            case GPP:
                telemetry.addData("We doin", " GPP now");
                blackboard.put(PATTERN_KEY, "GPP");
                break;
            case PGP:
                telemetry.addData("We doin", " PGP now");
                blackboard.put(PATTERN_KEY, "PGP");
                break;
            default:
                telemetry.addData("It failed ", "cry time");
                break;
        }
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    public void start() {
        //runtime.reset();
        opmodeTimer.resetTimer();
        telemetry.addData("HYPE", "Let's do this!!!");
        robot.readyHardware(false);
        robot.sorterHardware.legalToSpin = true;
//        robot.turret.resetEncoder();
        //speed = 1;
    }

    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        robot.updateAllDaThings();
        pathState = autonomousPathUpdate(); // Update autonomous state machine

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", currentStep);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.debug("Max Power Scalar", follower.getMaxPowerScaling());
        panelsTelemetry.debug("Motor power", robot.frontLeftDrive.getVelocity());
        panelsTelemetry.update(telemetry);
    }

    public static class PathsForBack12Blue {

        public PathChain LineUpWithClose;
        public PathChain GrabClose;
        public PathChain MoveToFireSecond;
        public PathChain LineUpWithMiddle;
        public PathChain GrabMiddle;
        public PathChain MoveToFireThird;
        public PathChain GrabFar;
        public PathChain FireLastPattern;
        public PathChain UnparkWithRizz;

        public PathsForBack12Blue(Follower follower) {
            LineUpWithClose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.500, 9.200),

                                    new Pose(42.000, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            GrabClose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(42.000, 36.000),

                                    new Pose(11.500, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            MoveToFireSecond = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(11.500, 36.000),

                                    new Pose(56.500, 12.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            LineUpWithMiddle = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.500, 12.000),

                                    new Pose(42.000, 60.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            GrabMiddle = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(42.000, 60.000),
                                    new Pose(36.544, 62.388),
                                    new Pose(9.747, 57.576)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            MoveToFireThird = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(9.747, 57.576),
                                    new Pose(45.709, 62.047),
                                    new Pose(52.000, 86.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            GrabFar = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(52.000, 86.000),
                                    new Pose(37.444, 82.924),
                                    new Pose(19.700, 84.035)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            FireLastPattern = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(19.700, 84.035),

                                    new Pose(52.000, 86.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            UnparkWithRizz = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(52.000, 86.000),

                                    new Pose(23.506, 71.788)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))

                    .build();
        }
    }

    public enum Steps {
        START,
        FIRE_1,
        LINE_UP_2, YOINK_2,
        MOVE_TO_FIRE_2, FIRE_2,
        LINE_UP_3, YOINK_3, MOVE_TO_FIRE_3, FIRE_3,
        LINE_UP_4, YOINK_4, MOVE_TO_FIRE_4, FIRE_4,

        UNPARK,
        END
    }


    private Steps currentStep = START; // Current autonomous path state (state machine)
private boolean eat = false;
    public int autonomousPathUpdate() {
        // Add your state machine Here
        // Access paths with paths.pathName
        // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine

        emergencyFinishIfNeeded();

        if(eat && robot.sorterLogic.inventory.getTotalCount()<3)
        {
            robot.sorterHardware.runAdvancedIntake();
        }
        else
        {
            robot.runBasicIntake(0);
        }

        switch (currentStep) {
            case START:
                follower.setMaxPower(1);
                robot.randomizationScanner.InitLimeLight(0);
                robot.pattern = robot.randomizationScanner.GetRandomization();//One last Check
                robot.targetScanner.InitLimeLightTargeting(robot.alliance.limelightPipeline, robot);
                robot.scanningForTargetTag = true;
                TurretLogic.activeMode = TurretLogic.controlMode.FULL;
                robot.sorterLogic.sortOutBlobs(GREEN, LOAD);
                robot.sorterLogic.sortOutBlobs(PURPLE, FIRE);
                robot.sorterLogic.sortOutBlobs(PURPLE, STORE);
                robot.updateAllDaThings();
                setCurrentStep(FIRE_1);
                break;
            case FIRE_1:
                /*if(robot.turret.rawSwivelController.withinTolerance)
                {
                    robot.queue.addPattern(robot.pattern);
                    setCurrentStep(LINE_UP_2);
                }*/
                break;
            case LINE_UP_2:
                if (robot.queue.noBallsQueued) {
                    follower.followPath(paths.LineUpWithClose);
                    setCurrentStep(YOINK_2);
                }
                break;
            case YOINK_2:
                if(!follower.isBusy())
                {
                    eat = true;
                    follower.setMaxPower(0.5);
                    follower.followPath(paths.GrabClose);
                    setCurrentStep(MOVE_TO_FIRE_2);
                }
                break;

            case MOVE_TO_FIRE_2:

                if (!follower.isBusy()) {
                    eat = false;
                    follower.setMaxPower(1);
                    follower.followPath(paths.MoveToFireSecond);
                    robot.runBasicIntake(0);
                    setCurrentStep(FIRE_2);
                }
                break;
            case FIRE_2:
                /*if ((!follower.isBusy()) && robot.turret.rawSwivelController.withinTolerance) {

                    robot.queue.addPattern(robot.pattern);
                    setCurrentStep(LINE_UP_3);
                }*/
                break;
            case LINE_UP_3:
                if (robot.queue.noBallsQueued) {
                    follower.followPath(paths.LineUpWithMiddle);
                    setCurrentStep(YOINK_3);
                }
                break;
            case YOINK_3:
                if (!follower.isBusy()) {
                    eat = true;
                    follower.setMaxPower(0.5);
                    follower.followPath(paths.GrabMiddle);
                    setCurrentStep(MOVE_TO_FIRE_3);
                }
            case MOVE_TO_FIRE_3:
                if (!follower.isBusy()) {
                    eat = false;
                    follower.setMaxPower(1);
                    follower.followPath(paths.MoveToFireThird);
                    setCurrentStep(FIRE_3);
                }
                break;
            case FIRE_3:
                /*if ((!follower.isBusy()) && robot.turret.rawSwivelController.withinTolerance) {
                    robot.queue.addPattern(robot.pattern);
                    setCurrentStep(LINE_UP_4);
                }*/
                break;
            case YOINK_4:
                if(robot.queue.noBallsQueued)
                {
                    eat = true;
                    follower.setMaxPower(0.5);
                    follower.followPath(paths.GrabFar);
                    setCurrentStep(MOVE_TO_FIRE_4);
                }
                break;
            case MOVE_TO_FIRE_4:
                if(!follower.isBusy())
                {
                    eat = false;
                    follower.setMaxPower(1);
                    follower.followPath(paths.FireLastPattern);
                    setCurrentStep(FIRE_4);
                }
                break;
            case FIRE_4:
                /*if(!follower.isBusy() && robot.turret.rawSwivelController.withinTolerance)
                {
                    robot.queue.addPattern(robot.pattern);
                    setCurrentStep(UNPARK);
                }*/
                break;
            case UNPARK:
                if(robot.queue.noBallsQueued)
                {
                    robot.sorterHardware.prepareNewMovement(0);
                    follower.followPath(paths.UnparkWithRizz);
                    setCurrentStep(END);
                }
            case END:
                if(robot.sorterHardware.doneMoving())
                {
                    return 0;
                }

        }
        return 0;
    }

    /** These change the states of the paths and actions.
     * It will also reset the timers of the individual switches **/

    void setCurrentStep(Steps nextStep) {
        currentStep = nextStep;
        pathTimer.resetTimer();
    }

    private void scan()
    {

    }

    private void emergencyFinishIfNeeded()
    {
        if(opmodeTimer.getElapsedTimeSeconds() >= 29)
        {
            robot.turret.updateTurretPositionXY();
            robot.sorterHardware.prepareNewMovement(0);
            setCurrentStep(END);
        }
    }
}
