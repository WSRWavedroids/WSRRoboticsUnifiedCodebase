package org.firstinspires.ftc.team13206.Autonomous.PEDRO;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.EAT_OVERFLOW;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.END;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.FIRE_1;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.FIRE_3;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.FIRE_OVERFLOW;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.GRAB_FIRST;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.GRAB_SECOND;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.LINE_UP_2;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.MOVE_TO_FIRE_2;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.MOVE_TO_FIRE_3;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.MOVE_TO_FIRE_OVERFLOW;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.START;
import static org.firstinspires.ftc.team13206.Autonomous.PEDRO.BlueCornerGrab8Ball.StepsForCornerGrab.UNPARK;
import static org.firstinspires.ftc.team13206.Core.ArtifactLocator.SlotState.GREEN;
import static org.firstinspires.ftc.team13206.Core.ArtifactLocator.SlotState.PURPLE;
import static org.firstinspires.ftc.team13206.Core.Robot.allianceSides.BLUE;
import static org.firstinspires.ftc.team13206.Core.SorterHardware.PositionState.FIRE;
import static org.firstinspires.ftc.team13206.Core.SorterHardware.PositionState.LOAD;
import static org.firstinspires.ftc.team13206.Core.SorterHardware.PositionState.STORE;

import com.bylazar.configurables.annotations.Configurable;
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

import org.firstinspires.ftc.team13206.Autonomous.AutonomousPlusPLUS;
import org.firstinspires.ftc.team13206.Core.ArtifactLocator;
import org.firstinspires.ftc.team13206.Core.Robot;
import org.firstinspires.ftc.team13206.Core.TurretLogic;

@Autonomous(name = "Blue Corner Grab 8 Ball", group = "1. FABIO WITH PEDRO")
@Configurable // Panels
public class BlueCornerGrab8Ball extends OpMode {

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
        robot.turret.blackboardSafe = true;
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
        robot.sorterHardware.resetSorterEncoder();
        robot.sorterHardware.reference = 0;
        robot.sorterHardware.legalToSpin = true;
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
        telemetry.addData("Color Queue", robot.queue.ballQueue);
        telemetry.addData("Slot Queue", ArtifactLocator.getNamesOfSlots(robot.queue.slotQueue));
        panelsTelemetry.update(telemetry);
    }

    public static class PathsForBack12Blue {

        public PathChain Lineupwithclose;
        public PathChain GrabClose;
        public PathChain MovetoFireSecondPattern;
        public PathChain GrabFirstCornerBall;
        public PathChain GrabSecondCornerBall;
        public PathChain GrabLastCornerBall;
        public PathChain MoveToFireCornerBalls;
        public PathChain GrabOverflow;
        public PathChain FireOverflow;
        public PathChain Unpark;

        public Pose unparkPose = new Pose (36.718, 14.882, Math.toRadians(180));

        public PathsForBack12Blue(Follower follower) {
            Lineupwithclose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.500, 9.200),

                                    new Pose(43, 36.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            GrabClose = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(56.000, 36.000),

                                    new Pose(11.500, 36.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            MovetoFireSecondPattern = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(11.500, 36.000),

                                    new Pose(57.500, 17.000)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(180))

                    .build();

            GrabFirstCornerBall = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(57.500, 17.000),

                                    new Pose(11.718, 16.953)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(200))

                    .build();

            GrabSecondCornerBall = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(11.718, 16.953),
                                    new Pose(23.588, 16.147),
                                    new Pose(11.718, 11.494)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(200), Math.toRadians(200))

                    .build();

            GrabLastCornerBall = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(11.718, 11.494),
                                    new Pose(28, 18),
                                    new Pose(11.718, 11)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(200), Math.toRadians(180))

                    .build();

            MoveToFireCornerBalls = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(11.718, 16.953),

                                    new Pose(57.500, 17.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(200), Math.toRadians(180))

                    .build();

            GrabOverflow = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(57.500, 17.000),

                                    new Pose(12.212, 20.541)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(150))

                    .build();

            FireOverflow = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(12.212, 20.541),

                                    new Pose(57.500, 17.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(150), Math.toRadians(180))

                    .build();

            Unpark = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(57.500, 17.000),

                                    new Pose(36.718, 14.882)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(0))

                    .build();
        }
    }

    enum StepsForCornerGrab {
        START,
        FIRE_1,
        LINE_UP_2, YOINK_2,
        MOVE_TO_FIRE_2, FIRE_2,
        GRAB_FIRST, GRAB_SECOND, GRAB_THIRD,
        MOVE_TO_FIRE_3, FIRE_3,
        EAT_OVERFLOW,
        MOVE_TO_FIRE_OVERFLOW, FIRE_OVERFLOW,
        UNPARK,
        END
    }


    private StepsForCornerGrab currentStep = START; // Current autonomous path state (state machine)
    private boolean eat = false;
    public int autonomousPathUpdate() {
        // Add your state machine Here
        // Access paths with paths.pathName
        // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine

        emergencyFinishIfNeeded();

        if(eat)
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
                robot.sorterLogic.sortOutBlobs(GREEN, LOAD);
                robot.sorterLogic.sortOutBlobs(PURPLE, FIRE);
                robot.sorterLogic.sortOutBlobs(PURPLE, STORE);
                robot.targetScanner.InitLimeLightTargeting(robot.alliance.limelightPipeline, robot);
                robot.scanningForTargetTag = true;
                TurretLogic.activeMode = TurretLogic.controlMode.FULL;
                setCurrentStep(FIRE_1);
                break;
            case FIRE_1:
                if(robot.turret.positioned() && robot.sorterHardware.positionedCheck() && !robot.sorterHardware.isCalibrating())
                {
                    robot.queue.addPattern(robot.pattern);
                    setCurrentStep(LINE_UP_2);
                }
                break;
            case LINE_UP_2:
                if (robot.queue.noBallsQueued) {
                    robot.sorterLogic.takeInventory();
                    if (robot.sorterLogic.inventory.getTotalCount() != 0) {
                        robot.queue.fillSimple();
                        break;
                    }
                    follower.followPath(paths.Lineupwithclose);
                    setCurrentStep(StepsForCornerGrab.YOINK_2);
                }
                break;
            case YOINK_2:
                if(!follower.isBusy())
                {
                    eat = true;
                    follower.setMaxPower(0.40);
                    follower.followPath(paths.GrabClose);
                    setCurrentStep(MOVE_TO_FIRE_2);
                }
                break;
            case MOVE_TO_FIRE_2:
                if (!follower.isBusy()) {
                    follower.setMaxPower(1);
                    follower.followPath(paths.MovetoFireSecondPattern);
                    setCurrentStep(StepsForCornerGrab.FIRE_2);
                }
                break;
            case FIRE_2:
                if ((!follower.isBusy()) && robot.turret.positioned()) {
                    eat = false;
                    robot.queue.addPattern(robot.pattern);
                    setCurrentStep(GRAB_FIRST);
                }
                break;
            case GRAB_FIRST:
                if(robot.queue.noBallsQueued)
                {
                    eat = true;
                    follower.setMaxPower(0.65);
                    follower.followPath(paths.GrabFirstCornerBall);
                    setCurrentStep(GRAB_SECOND);

                }
                break;
            case GRAB_SECOND:
                if (!follower.isBusy())
                {
                    eat = true;
                    follower.followPath(paths.GrabSecondCornerBall);
                    setCurrentStep(MOVE_TO_FIRE_3);
                    break;
                }
            case GRAB_THIRD://skipping for now
                if(!follower.isBusy())
                {
                    eat = true;
                    follower.followPath(paths.GrabLastCornerBall);
                    setCurrentStep(MOVE_TO_FIRE_3);
                    break;
                }
            case MOVE_TO_FIRE_3:
                if(!follower.isBusy())
                {
                    follower.setMaxPower(1);
                    eat = true;
                    follower.followPath(paths.MoveToFireCornerBalls);
                    setCurrentStep(FIRE_3);
                    break;
                }
            case FIRE_3:
                if ((!follower.isBusy()) && robot.turret.positioned()) {
                    eat = false;
                    robot.queue.addPattern(robot.pattern);
                    setCurrentStep(EAT_OVERFLOW);
                }
                break;
            case EAT_OVERFLOW:
                if(robot.queue.noBallsQueued)
                {
                    eat = true;
                    follower.followPath(paths.GrabOverflow);
                    setCurrentStep(MOVE_TO_FIRE_OVERFLOW);
                    break;
                }
            case MOVE_TO_FIRE_OVERFLOW:
                if((auto.timeLeft(opmodeTimer) < 5 && robot.sorterLogic.inventory.getTotalCount() > 0) || (robot.sorterLogic.inventory.getTotalCount() >= 3))
                {
                    //If we have things to fire, go shoot!
                    follower.followPath(paths.FireOverflow);
                    setCurrentStep(FIRE_OVERFLOW);
                    break;
                }
                else if(auto.timeLeft(opmodeTimer) < 2.8)
                {
                    //If its a loss, go unpark
                    follower.followPath(auto.makeDynamicPath(follower, paths.unparkPose, follower.getHeading()));
                    setCurrentStep(END);
                    break;
                }


            case FIRE_OVERFLOW:
                if ((!follower.isBusy()) && robot.turret.positioned()) {
                    eat = false;
                    robot.queue.addPattern(robot.pattern);
                    setCurrentStep(UNPARK);
                }
                break;


            case UNPARK:
                if(robot.queue.noBallsQueued)
                {
                    follower.followPath(paths.Unpark);
                    setCurrentStep(END);
                }

            case END:
                if(robot.sorterHardware.doneMoving() && !follower.isBusy())
                {
                    return 0;
                }
        }
        return 0;
    }

    /** These change the states of the paths and actions.
     * It will also reset the timers of the individual switches **/

    void setCurrentStep(StepsForCornerGrab nextStep) {
        currentStep = nextStep;
        pathTimer.resetTimer();
    }

    private void scan()
    {

    }

    private void emergencyFinishIfNeeded()
    {
        if(opmodeTimer.getElapsedTimeSeconds() >= 29.5)
        {
            follower.breakFollowing();
            robot.frontRightDrive.setZeroPowerBehavior(BRAKE);
            robot.backLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.backRightDrive.setZeroPowerBehavior(BRAKE);
            robot.frontLeftDrive.setZeroPowerBehavior(BRAKE);

            robot.frontRightDrive.setPower(0);
            robot.backLeftDrive.setPower(0);
            robot.backRightDrive.setPower(0);
            robot.frontLeftDrive.setPower(0);

            robot.turret.updateTurretPositionXY();
            setCurrentStep(END);
        }
    }


}
