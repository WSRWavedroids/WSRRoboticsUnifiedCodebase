package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.*;
import static org.firstinspires.ftc.teamcode.Core.Robot.allianceSides.*;
import static org.firstinspires.ftc.teamcode.Core.Robot.patternColors.*;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.*;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.Core.SorterHardware;
import org.firstinspires.ftc.teamcode.Core.TurretLogic;

import java.util.Objects;

/**
 * This is an iterative autonomous program. It runs in a state machine, which allows us to run the
 * updateAllDaThings() function and properly run the blender without any... questionable code. And
 * it's marginally more efficient. Also, this contains the proper code to run the red alliance auto
 * too, allowing us to keep both autos up to date in a single file. BetaRedFrontAuto is a shell that
 * basically just hijacks this file to work, which is neat.
 */
@Autonomous(group = "0. FABIO NO PEDRO", name = "Blue Back 6 Ball")
public class BetaBlueBackAuto extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();
    private double driveSpeed = 0.25;

    int slot = 0; // temp for testing lol

    int targetOffset = 0;
    int patternCorrectedStrafeDistance;

    //private double storedSpeed;
    public Robot robot = null;
    public AutonomousPlusPLUS auto = null;
    public ElapsedTime stallTimer;
    public IMU imu;

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {
        telemetry.addData("Status", "Robot Stopped");
        doTelemetryStuff();
    }

    private void nextStep(Steps nextStep) {
        currentStep = nextStep;
    }

    private void nextPatternSpecificStep(Steps nextGeneralStep) {

    }
    private void doTelemetryStuff() {
        // This little section updates the driver hub on the runtime and the motor powers.
        // It's mostly used for troubleshooting.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Step", currentStep);
        telemetry.addData("fireInSequenceStallingState", auto.fireInSequenceStallingState);

        if(robot.targetTag.currentlyDetected)
        {
            telemetry.addData("last detected x angle: ", robot.targetTag.angleX);
            telemetry.addData("last detected y angle: ", robot.targetTag.angleY);

            telemetry.addData("last distance x: ", robot.targetTag.distanceX);
            telemetry.addData("last detected distance y: ", robot.targetTag.distanceY);
            telemetry.addData("last detected distance z: ", robot.targetTag.distanceZ);
        }

        telemetry.addData("Last saved pattern: ", blackboard.get(PATTERN_KEY));

        telemetry.addData("Last saved Alliance: ", blackboard.get(ALLIANCE_KEY));

        telemetry.addData("Reference", robot.sorterHardware.reference);

        telemetry.addData("Equalized Target Position", robot.sorterLogic.offsetPositions.get(targetOffset));
        telemetry.addData("Launcher Velocity", robot.launcher.motor.getVelocity());
        telemetry.addData("Launcher Target Velocity", robot.launcher.velocityTarget);
        telemetry.addData("Launcher at Speed", robot.launcher.motorSpeedCheck(robot.launcher.velocityTarget));
        telemetry.addData("Launcher on Cooldown", robot.launcher.onCooldown);

        //robot.tellMotorOutput();
    }

    private float getLargestAbsVal(float[] values){
        // This function does some math!
        float max = 0;
        for (float val : values) {
            if (Math.abs(val) > max) {
                max = Math.abs(val);
            }
        }
        return max;
    }

    private boolean isEven(int x) {
        return x % 2 == 0;
    }

    private enum Steps {
        START, MOVETURRETONE, FIRE3, MOVEFORWARD, TURN, YOINK, BACKUP,
        STRAFE, MOVETURRETTWO, FIRE3AGAIN, RESET, UNPARK, UN_TURN, STOP

    }
    private Steps currentStep = Steps.START;

    public static final String ALLIANCE_KEY = "Alliance"; //For blackboard
    public static final String PATTERN_KEY = "Pattern";


    private final Pose startPose = new Pose(56.5, 9, Math.PI / 2);

    /**
     * Code to run ONCE when the driver hits INIT
     */

    public void init() {

        robot = new Robot(hardwareMap, telemetry, this);
        TurretLogic.tolerance = robot.turret.degreesToTicks(4);
        telemetry.addData("tolerance value test pt 1", TurretLogic.tolerance);
        telemetry.addData("tolerance value test pt 2", robot.turret.tolerance);
        auto = new AutonomousPlusPLUS(robot);
        robot.turret.activeMode = TurretLogic.controlMode.FULL;

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        robot.randomizationScanner.InitLimeLight(0);
        blackboard.put(ALLIANCE_KEY, "BLUE");
        stallTimer = new ElapsedTime();

        robot.turret.follower.setPose(startPose);
        robot.turret.follower.setHeading(startPose.getHeading());

        robot.alliance = BLUE;
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    public void init_loop() {
        telemetry.addData("HYPE", "ARE! YOU! READY?!?!?!?!");

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
        runtime.reset();
        telemetry.addData("HYPE", "Let's do this!!!");
        robot.readyHardware(true);
        robot.sorterHardware.legalToSpin = true;
        driveSpeed = .75;
        robot.sorterLogic.sortCooldownTime = 0.25;
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {
        robot.turret.follower.updatePose();

        telemetry.addData("Distance", robot.targetTag.distanceZ);
        telemetry.addData("Angle", robot.targetTag.angleX);
        telemetry.addData("Within Swivel Tolerance", robot.turret.rawSwivelController.withinTolerance);
        telemetry.addData("Turret Tolerance", robot.turret.rawSwivelController.tolerance);
        telemetry.addData("Swivel Velocity", robot.turret.swivelMotor.getVelocity());

        robot.updateAllDaThings();

        switch (currentStep) {
            case START:
                auto.setTolerances(7);

                auto.setSpeed(driveSpeed);

                robot.sorterLogic.sortOutBlobs(GREEN, LOAD);
                robot.sorterLogic.sortOutBlobs(PURPLE, FIRE);
                robot.sorterLogic.sortOutBlobs(PURPLE, STORE);

                robot.pattern = robot.randomizationScanner.GetRandomization();//One last Check

                robot.targetScanner.InitLimeLightTargeting(robot.alliance.limelightPipeline, robot);

                robot.scanningForTargetTag = true;
                TurretLogic.activeMode = TurretLogic.controlMode.FULL;
                nextStep(Steps.MOVETURRETONE);
                break;
            case MOVETURRETONE:
                //Now start the preturn for max time savings... I dont think this interferes with the next state
                if(robot.pattern.equals(GPP))
                {
                    robot.sorterHardware.prepareNewMovement(robot.sorterLogic.slotA.getFirePosition());
                }
                else
                {
                    robot.sorterHardware.prepareNewMovement(robot.sorterLogic.slotB.getFirePosition());
                }
                //Preset launcher Speed here
                robot.turret.runTurret();

                nextStep(Steps.FIRE3);
                break;
            case FIRE3:
                if(!robot.turret.rawSwivelController.withinTolerance)
                {
                    telemetry.addData("Out of Position, can't fire", ":(");
                }
                else
                {
                    telemetry.addData("In Position, trying to fire", ":(");
                    robot.launcher.setPerfectLauncherVelocity();
                    robot.queue.addPattern(robot.pattern);
                    nextStep(Steps.MOVEFORWARD);
                }
                break;
            case MOVEFORWARD:
                if (robot.queue.noBallsQueued) {
                    auto.moveRobotForward(825);
                    nextStep(Steps.TURN);
                }

                break;
            case TURN:
                if(auto.checkMovement())
                {
                    if(Objects.equals(blackboard.get(ALLIANCE_KEY), "BLUE"))
                    {
                        auto.turnRobotLeft(725);
                    }
                    else
                    {
                        auto.turnRobotRight(725);
                    }

                    nextStep(Steps.YOINK);
                }
                break;
            case YOINK:
                if(auto.checkMovement())
                {
                    auto.setSpeed(0.15);
                    auto.moveRobotForward(1425);
                    auto.yoinkify(1425);
                    nextStep(Steps.BACKUP);
                }
                break;
            case BACKUP:
                auto.yoinkify(1425);
                if(auto.checkYoink() && auto.checkMovement())
                {
                    robot.launcher.setPerfectLauncherVelocity();
                    if(Objects.equals(blackboard.get(ALLIANCE_KEY), "BLUE"))
                    {
                        auto.moveRobotLeft(800);
                    }
                    else
                    {
                        auto.moveRobotRight(800);
                    }

                    nextStep(Steps.FIRE3AGAIN);
                }
                break;
            case FIRE3AGAIN:
                if(auto.checkMovement())
                {
                    robot.queue.addPattern(robot.pattern);
                    nextStep(Steps.RESET);
                }
                break;
            case RESET:
                if (robot.queue.noBallsQueued) {
                    robot.sorterHardware.prepareNewMovement(0);
                    robot.turret.manualOverridePositionInDegs = 0;
                    nextStep(Steps.UNPARK);
                }
                break;
            case UNPARK:
                if(robot.sorterHardware.doneMoving() && robot.turret.rawSwivelController.withinTolerance)
                {
                    if(Objects.equals(blackboard.get(ALLIANCE_KEY), "BLUE"))
                    {
                        auto.moveRobotRight(1000);
                    }
                    else
                    {
                        auto.moveRobotLeft(1000);
                    }
                    nextStep(Steps.STOP);
                }
                break;
            case STOP:
                if(auto.checkMovement())
                {
                    robot.turret.updateTurretPositionXY();
                    /*blackboard.put("PedroX", robot.robotPosition.x = robot.turret.follower.getPose().getX());
                    blackboard.put("PedroY", robot.robotPosition.y = robot.turret.follower.getPose().getY());
                    blackboard.put("PedroHeading", robot.robotHeading = Math.toDegrees(robot.turret.follower.getHeading()));*/
                    super.requestOpModeStop();
                }
        }
        doTelemetryStuff();

    }
    }


