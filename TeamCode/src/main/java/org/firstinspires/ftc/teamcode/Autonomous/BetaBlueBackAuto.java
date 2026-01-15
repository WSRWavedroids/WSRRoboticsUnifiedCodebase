package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.Autonomous.BetaBlueFrontAuto.Step.UN_TURN;
import static org.firstinspires.ftc.teamcode.Autonomous.BetaBlueFrontAuto.Step.YAY;
import static org.firstinspires.ftc.teamcode.Autonomous.ExtraBetaBlueFrontAuto.Step.RESET_BLENDER1;
import static org.firstinspires.ftc.teamcode.Core.Robot.patternColors.GPP;
import static org.firstinspires.ftc.teamcode.Core.Robot.patternColors.PGP;
import static org.firstinspires.ftc.teamcode.Core.Robot.patternColors.PPG;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.ArtifactLocator;
import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.Core.TurretLogic;

/**
 * This is an iterative autonomous program. It runs in a state machine, which allows us to run the
 * updateAllDaThings() function and properly run the blender without any... questionable code. And
 * it's marginally more efficient. Also, this contains the proper code to run the red alliance auto
 * too, allowing us to keep both autos up to date in a single file. BetaRedFrontAuto is a shell that
 * basically just hijacks this file to work, which is neat.
 */
@Autonomous(group = "Basic", name = "Blue Back 6 Ball")
public class BetaBlueBackAuto extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();
    private double speed = 0.75;

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

    /*
     * The holding cell for all of the random functions we call above.
     */
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

    /**
     * Code to run ONCE when the driver hits INIT
     */
    public void init() {

        // Call the initialization protocol from the Robot class.
        // Go find pizza
        robot = new Robot(hardwareMap, telemetry, this);
        auto = new AutonomousPlusPLUS(robot);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        robot.randomizationScanner.InitLimeLight(0);
        blackboard.put(ALLIANCE_KEY, "BLUE");
        stallTimer = new ElapsedTime();

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
        speed = 1;
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {
        switch (currentStep) {
            case START:
                auto.setTolerances(7);
                robot.sorterHardware.legalToSpin = true;
                robot.pattern = robot.randomizationScanner.GetRandomization();//One last Check
                TurretLogic.activeMode = TurretLogic.controlMode.OVERIDE;
                nextStep(Steps.MOVETURRETONE);
                break;
            case MOVETURRETONE:
                robot.turret.manualOverridePositionInDegs = -30;
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
                if(robot.turret.fineSwivelController.withinTolerance)
                {
                    if (robot.pattern.equals(PPG)) {
                        auto.fireInSequence(robot.sorterLogic.slotB, robot.sorterLogic.slotC, robot.sorterLogic.slotA);
                    } else if (robot.pattern.equals(PGP)) {
                        auto.fireInSequence(robot.sorterLogic.slotB, robot.sorterLogic.slotA, robot.sorterLogic.slotC);
                    } else {
                        auto.fireInSequence(robot.sorterLogic.slotA, robot.sorterLogic.slotB, robot.sorterLogic.slotC);
                    }

                    if (auto.fireInSequenceComplete()) {
                        nextStep(Steps.MOVEFORWARD);
                    }

                }
                break;
            case MOVEFORWARD:
                if(auto.fireInSequenceComplete())
                {
                    auto.moveRobotForward(500);
                    nextStep(Steps.TURN);
                }
                break;
            case TURN:
                if(auto.checkMovement())
                {
                    auto.turnRobotRight(700);
                    nextStep(Steps.YOINK);
                }
                break;
            case YOINK:
                if(auto.checkMovement())
                {
                    auto.moveRobotForward(1000);
                    auto.yoinkify(1000);
                    nextStep(Steps.UN_TURN);
                }
                break;
            case UN_TURN:
                if(auto.checkYoink())
                {
                    //double check intake is off lol
                    auto.turnRobotLeft(700);
                    nextStep(Steps.BACKUP);
                }
                break;
            case BACKUP:
                if(auto.checkMovement())
                {
                    auto.moveRobotBackward(500);
                    nextStep(Steps.FIRE3AGAIN);
                }
            case FIRE3AGAIN:
                if(auto.checkMovement())
                {
                    if (robot.pattern.equals(PPG)) {
                        auto.fireInSequence(robot.sorterLogic.findFirstType(ArtifactLocator.SlotState.PURPLE)
                                , robot.sorterLogic.findFirstType(ArtifactLocator.SlotState.PURPLE),
                                robot.sorterLogic.findFirstType(ArtifactLocator.SlotState.GREEN));

                    } else if (robot.pattern.equals(PGP)) {
                        auto.fireInSequence(robot.sorterLogic.findFirstType(ArtifactLocator.SlotState.PURPLE)
                                , robot.sorterLogic.findFirstType(ArtifactLocator.SlotState.GREEN),
                                robot.sorterLogic.findFirstType(ArtifactLocator.SlotState.PURPLE));
                    } else {
                        auto.fireInSequence(robot.sorterLogic.findFirstType(ArtifactLocator.SlotState.GREEN)
                                , robot.sorterLogic.findFirstType(ArtifactLocator.SlotState.PURPLE),
                                robot.sorterLogic.findFirstType(ArtifactLocator.SlotState.PURPLE));
                    }

                    if (auto.fireInSequenceComplete()) {
                        nextStep(Steps.RESET);
                    }
                }
            case RESET:
                if(auto.fireInSequenceComplete())
                {
                    robot.sorterHardware.prepareNewMovement(0);
                    robot.turret.manualOverridePositionInDegs = 0;
                    nextStep(Steps.UNPARK);
                }
                break;
            case UNPARK:
                if(robot.sorterHardware.doneMoving() && robot.turret.fineSwivelController.withinTolerance)
                {
                    auto.moveRobotForward(500);
                    nextStep(Steps.STOP);
                }
                break;
            case STOP:
                if(auto.checkMovement())
                {
                    super.requestOpModeStop();
                }
        }

        robot.updateAllDaThings();
        doTelemetryStuff();

    }
    }


