package org.firstinspires.ftc.teamcode.Teleop;

import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.*;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;
import static org.firstinspires.ftc.teamcode.Core.Robot.allianceSides.*;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.FeederState.*;
import static org.firstinspires.ftc.teamcode.Core.Robot.DriveMode.*;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.*;

import com.bylazar.panels.Panels;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Core.ArtifactLocator;
import org.firstinspires.ftc.teamcode.Core.FramerateCalculator;
import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.Core.fireQueueWithStates;


import java.util.Objects;

/**
 * This file is our iterative (Non-Linear) "OpMode" for TeleOp.
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is selected on the Robot Controller and executed.
 * This OpMode controls the functions of the robot during the driver-controlled period.
 * <p>
 * If the "@Disabled" line is not commented out, the program will not show up on the driver hub.
 * If you ever have problems with the program not showing up on the driver hub, it's probably because of that.
 * <p>
 */
@TeleOp(name = "DAVE", group = "CompBot")
public class Vortex_Teleop_Decode extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();
    private final FramerateCalculator fps = new FramerateCalculator(runtime);
    private double speed = 0.75;
    private boolean spinTargetAcquired = false;

    private boolean cadenRecording = false;
    private boolean contTwoBumpersPressed = false;
    boolean cadenON = false;
    boolean cadenHoldingReady = false;
    boolean cadenHoldingFire = false;

    int SpinTargetFrontLeft;
    int SpinTargetFrontRight;
    int SpinTargetBackLeft;
    int SpinTargetBackRight;

    int slot = 0; // temp for testing lol

    int targetOffset = 0;

    //private double storedSpeed;
    public Robot robot = null;
    public IMU imu;


    public static final String ALLIANCE_KEY = "Alliance"; //For blackboard
    public static final String PATTERN_KEY = "Pattern";

    public boolean canManuallyControlVerticalSlides = true;

    ElapsedTime outtakeTimer = new ElapsedTime();
    boolean killSwitchActivated = false;

    static TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    private final Pose startingPose = new Pose(72, 72, Math.PI / 2);
    /*
     * Code to run ONCE when the driver hits INIT
     */
    public void init() {

        // Call the initialization protocol from the Robot class.
        robot = new Robot(hardwareMap, telemetry, this);

        robot.targetScanner.InitLimeLightTargeting(1, robot);


        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        outtakeTimer.reset();

        if (robot.controlMode == LEGACY_FIELD_CENTRIC) {

            imu = hardwareMap.get(IMU.class, "imu");
            IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)); //Forward = left fsr
            // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
            imu.initialize(parameters);
        }
        //if using field centric youl need this lolzeez
        if (Objects.equals(blackboard.get(ALLIANCE_KEY), "BLUE")) {
            robot.targetScanner.InitLimeLightTargeting(1, robot);
            robot.scanningForTargetTag = true;
        } else if (Objects.equals(blackboard.get(ALLIANCE_KEY), "RED")) {
            robot.targetScanner.InitLimeLightTargeting(2, robot);
            robot.scanningForTargetTag = true;
        } else {
            robot.targetScanner.InitLimeLightTargeting(2, robot);
            robot.scanningForTargetTag = true;
        }

        robot.panels = Panels.INSTANCE;
        robot.readyHardware(true);

        robot.turret.follower.setPose(startingPose);
        robot.turret.follower.setHeading(startingPose.getHeading());
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    public void init_loop() {
        telemetry.addData("HYPE", "ARE! YOU! READY?!?!?!?!");
        doTelemetryStuff();
        telemetry.update();
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    public void start() {
        runtime.reset();
        telemetry.addData("HYPE", "Let's do this!!!");
        gamepad1.setLedColor(0, 0, 255, 10);
        gamepad2.setLedColor(0, 0, 255, 10);
        robot.sorterHardware.calibrate();
        robot.sorterHardware.legalToSpin = true;

        robot.frontLeftDrive.setZeroPowerBehavior(BRAKE);
        robot.frontRightDrive.setZeroPowerBehavior(BRAKE);
        robot.backLeftDrive.setZeroPowerBehavior(BRAKE);
        robot.backRightDrive.setZeroPowerBehavior(BRAKE);
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {

        robot.updateAllDaThings();

        //So Begins the input chain. At least try a bit to organise by driver

        driveSpeed();

        controllerRumble();

        holdInPlace();

        driveOrAutoLock();

        fireQueue();

        intake();

        //WSeñorMichael

        launcherToggle();

        //manualTuneLauncher();

        fireCurrentFireSlot();

        incrementThroughPositions();

        colorMovement();

        switchAlliance();

        turretAssist();

        //robot.panelsTelemetry.addData("Motor Position", robot.launcher.motor.getCurrentPosition());
        robot.panelsTelemetry.update();

        //fps.update();
        doTelemetryStuff();

        if (gamepad1.touchpad || gamepad2.touchpad) {
            killSwitchActivated = true;
            requestOpModeStop();
        }
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {
        telemetry.addData("Status", "Robot Stopped");
        if (killSwitchActivated) telemetry.addLine("Killswitch Hit!");
    }


    /*
     * The holding cell for all of the random functions we call above.
     */

    private void colorMovement() {
        if(gamepad2.square && !gamepad2.left_bumper)
        {
            robot.sorterHardware.prepareNewMovement(
                    robot.sorterLogic.findFirstType(PURPLE).getFirePosition());
        }
        else if(gamepad2.triangle && !gamepad2.left_bumper)
        {
            robot.sorterHardware.prepareNewMovement(
                    robot.sorterLogic.findFirstType(GREEN).getFirePosition());
        }
    }

    private void fireQueue() {
        /// Clears list each time the button is deliberately pressed, so ready for queueing
        /// Without this we have no way to empty it without firing
        if(gamepad2.leftBumperWasPressed())
        {
            robot.queue.clearList();
        }

        /// Adds color to queue
        if(gamepad2.squareWasPressed() && gamepad2.left_bumper)
        {
            robot.queue.addToNextSpotColor(PURPLE);
            gamepad2.setLedColor(152, 7, 224,100);
        }
        else if(gamepad2.triangleWasPressed() && gamepad2.left_bumper)
        {
            robot.queue.addToNextSpotColor(GREEN);
            gamepad2.setLedColor(0, 255, 0, 100);
        }

        if(gamepad2.rightBumperWasPressed())
        {
            if(robot.queue.checkForExistingQueue())
            {
                robot.queue.wantToFireQueue = fireQueueWithStates.firingQueue.SMART;
            }
            else if(robot.sorterLogic.inventory.canMakePattern())
            {
                robot.queue.clearList();
                robot.queue.addPattern(robot.pattern);
                robot.queue.wantToFireQueue = fireQueueWithStates.firingQueue.SMART;
            }
            else
            {
                robot.queue.clearList();
                robot.queue.fillSimple(); // replace with the if when cam ready
                robot.queue.wantToFireQueue = fireQueueWithStates.firingQueue.DUMB;
            }
        }
    }

    private void intake() {
        if(gamepad2.cross)
        {
            robot.sorterHardware.runAdvancedIntake();
        }
        else if(gamepad2.circle) // dave spits out artifact
        {
            robot.runBasicIntake(-1);
            robot.sorterHardware.setFeeders(OUTTAKE);
        }
        else //don't run intake if we not pulling trigger
        {
            robot.cancelAutoIntake();
            robot.runBasicIntake(0.01); //Always keep a slight power flow to servos to prevent input delay from module
        }
    }

    private void controllerRumble() {
        if(robot.targetTag.currentlyDetected) {
            gamepad1.rumble(0.25, 0.25, 100);
        }

        if(robot.launcher.isInFireSequence())
        {
            gamepad2.rumble(0.5, 0, 50);
        }
    }

    private void launcherToggle() {
        if(gamepad2.left_trigger > 0.50)
        {

            if (!cadenHoldingReady) {
                cadenHoldingReady = true;
                if (cadenON) {
                    cadenON = false;
                } else {
                    cadenON = true;
                }
            }

            if (cadenON) {
                robot.launcher.setPerfectLauncherVelocity();
            }
            else
            {
                robot.launcher.setLauncherVelocity(0);
            }

        }
        else
        {
            cadenHoldingReady = false;
        }
    }

    private void fireCurrentFireSlot() {
        if(gamepad2.right_trigger > 0.50 && !robot.launcher.isInFireSequence() /*&& robot.queue.wantToFireQueue == fireQueueWithStates.firingQueue.NONE*/) {
            if(!cadenHoldingFire)
            {
                cadenON = true;
                cadenHoldingFire = true;
                robot.launcher.fireWithinTimeIfSafe(0.5, false, false, 0.5);
            }
        }
        else
        {
            cadenHoldingFire = false;
        }
    }

    private void holdInPlace() {
        if(gamepad1.squareWasPressed())//Holds in place...
        {
            SpinTargetFrontLeft = robot.frontLeftDrive.getCurrentPosition();
            SpinTargetFrontRight = robot.frontRightDrive.getCurrentPosition();
            SpinTargetBackLeft = robot.backLeftDrive.getCurrentPosition();
            SpinTargetBackRight = robot.backRightDrive.getCurrentPosition();
            spinTargetAcquired = true;
            speed = 1;
        }
    }

    private void driveOrAutoLock() {
        if (gamepad1.left_bumper || gamepad1.right_bumper || gamepad1.square) {
            autoWheel(robot.targetTag.currentlyDetected, robot.targetTag.angleX);
        } else {
            singleJoystickDrive();
            spinTargetAcquired = false;
        }
    }

    public void setIndividualPowers(float[] motorPowers) {
        // This function creates an array so that the function below works.
        // Don't mess with this function unless you know what you're doing.

        if (motorPowers.length != 4) {
            return;
        }
        robot.frontLeftDrive.setMode(RUN_WITHOUT_ENCODER);
        robot.frontRightDrive.setMode(RUN_WITHOUT_ENCODER);
        robot.backLeftDrive.setMode(RUN_WITHOUT_ENCODER);
        robot.backRightDrive.setMode(RUN_WITHOUT_ENCODER);

        robot.frontLeftDrive.setPower(motorPowers[0]);
        robot.frontRightDrive.setPower(motorPowers[1]);
        robot.backLeftDrive.setPower(motorPowers[2]);
        robot.backRightDrive.setPower(motorPowers[3]);
    }

    private void autoWheel(boolean detected, double anglex) {

        if(gamepad1.left_bumper)//Does 180
        {
            SpinTargetFrontLeft = robot.frontLeftDrive.getCurrentPosition() + 830*2;
            SpinTargetFrontRight = robot.frontRightDrive.getCurrentPosition() - 830*2;
            SpinTargetBackLeft = robot.backLeftDrive.getCurrentPosition() + 830*2;
            SpinTargetBackRight = robot.backRightDrive.getCurrentPosition() - 830*2;
            spinTargetAcquired = true;
            speed = 1;
        }

        if ((gamepad1.right_bumper && !detected) || gamepad1.square) {
            robot.frontLeftDrive.setTargetPosition(SpinTargetFrontLeft);
            robot.frontRightDrive.setTargetPosition(SpinTargetFrontRight);
            robot.backLeftDrive.setTargetPosition(SpinTargetBackLeft);
            robot.backRightDrive.setTargetPosition(SpinTargetBackRight);
        } else if (detected && gamepad1.right_bumper) //Turn to face target tag
        {
            double constant = -1660 / 360; //We will know this later
            speed = 1;
            int turnTicks = (int) ((robot.targetTag.angleX + robot.limelightSideOffsetAngle) * constant);

            robot.frontLeftDrive.setTargetPosition(robot.frontLeftDrive.getCurrentPosition() + turnTicks);
            robot.frontRightDrive.setTargetPosition(robot.frontRightDrive.getCurrentPosition() - turnTicks);
            robot.backLeftDrive.setTargetPosition(robot.backLeftDrive.getCurrentPosition() + turnTicks);
            robot.backRightDrive.setTargetPosition(robot.backRightDrive.getCurrentPosition() - turnTicks);
        }

        robot.setRunMode(RUN_TO_POSITION);
        robot.powerSet(speed);
    }

    private void singleJoystickDrive() {
        // We don't really know how this function works, but it makes the wheels drive, so we don't question it.
        // Don't mess with this function unless you REALLY know what you're doing.
        float leftY = this.gamepad1.left_stick_y;
        float rightX = this.gamepad1.right_stick_x;
        float leftX = this.gamepad1.left_stick_x;



        float[] motorPowers = new float[4];

        motorPowers[0] = (leftY - leftX - rightX); // frontLeftDrive
        motorPowers[1] = (leftY + leftX + rightX); // frontRightDrive
        motorPowers[2] = (leftY + leftX - rightX); // backLeftDrive
        motorPowers[3] = (leftY - leftX + rightX); // backRightDrive

        float max = getLargestAbsVal(motorPowers);
        if (max < 1) {
            max = 1;
        }

        for (int i = 0; i < motorPowers.length; i++) {
            motorPowers[i] *= (1 / max);

            float abs = Math.abs(motorPowers[i]);
            if (abs < 0.05) {
                motorPowers[i] = 0.0f;
            }
            if (abs > 1.0) {
                motorPowers[i] /= abs;
            }
        }

        setIndividualPowers(motorPowers);
    }

    private void controlMode() {
        if (gamepad1.back) {
            if (robot.controlMode == ROBOT_CENTRIC){
                robot.controlMode = LEGACY_FIELD_CENTRIC;
                telemetry.addData("Control Mode", "Field Centric Controls");
            } else if (robot.controlMode == LEGACY_FIELD_CENTRIC) {
                robot.controlMode = ROBOT_CENTRIC;
                telemetry.addData("Control Mode", "Robot Centric Controls");
            }
        }

        if (gamepad1.options && robot.controlMode == LEGACY_FIELD_CENTRIC) {
            imu.resetYaw();
        }
    }

    private void manualTuneLauncher() {
        if (gamepad2.dpadUpWasPressed()) {
            robot.launcher.velocityTarget += 20;
        }
        else if (gamepad2.dpadDownWasPressed()) {
            robot.launcher.velocityTarget -= 20;
        }
    }

    private void driveSpeed() {
        if (gamepad1.dpad_up || gamepad1.right_trigger >= 0.5) {
            speed = 1;
        } else if (gamepad1.dpad_down) {
            speed = 0.25;
        } else if (gamepad1.dpad_left || gamepad1.left_trigger >0.5) {
            speed = 0.5;
        } else if (gamepad1.dpad_right) {
            speed = 0.75;
        }

        if (speed == 1) {
            telemetry.addData("Speed", "Fast Boi");
        } else if (speed == 0.5) {
            telemetry.addData("Speed", "Slow Boi");
        } else if (speed == 0.25) {
            telemetry.addData("Speed", "Super Slow Boi");
        } else if (speed == 0.75) {
            telemetry.addData("Speed", "Normal Boi");
        }
    }

    private void incrementThroughPositions() {
        telemetry.addData("Current Offset (by logic)", robot.sorterLogic.getCurrentOffset());

        // Fire positions
        if (gamepad2.dpadLeftWasPressed()) {
            goNextPosition(-1);
        } else if (gamepad2.dpadRightWasPressed()) {
            goNextPosition(1);
        }

        telemetry.addData("Target Offset", targetOffset);
    }

    private void goNextPosition(int go) {
        robot.sorterHardware.prepareNewMovement(
            robot.sorterLogic.offsetPositions.get(
                    makeSureNewOffsetIsOK(
                        robot.sorterLogic.findClosestOffset(
                                robot.sorterHardware.motor.getCurrentPosition()) + go
                )
            )
        );
    }

    private int makeSureNewOffsetIsOK(int offset) {
        while (offset < 0) {
            offset += 3;
        }
        while (offset > 2) {
            offset -= 3;
        }
        return offset;
    }

    private void switchAlliance() {
        if (gamepad2.shareWasPressed()){
            if (robot.alliance == BLUE) {
                robot.alliance = RED;
            } else {
                robot.alliance = BLUE;
            }
        }
    }

    private void doTelemetryStuff() {
        // This little section updates the driver hub on the runtime and the motor powers.
        // It's mostly used for troubleshooting.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Framerate (last 15 seconds)", fps.getFramerate(30) + " fps");

        telemetry.addLine();

        telemetry.addData("Turret X", robot.turretPosition.x);
        telemetry.addData("Turret Y", robot.turretPosition.y);
        telemetry.addData("Pedro Heading", robot.robotHeading);

        telemetry.addData("Turret Position", robot.turret.ticksToDegrees(robot.swivelMotor.getCurrentPosition()));
        telemetry.addData("Turret Target", robot.turret.ticksToDegrees(robot.turret.runToSafeAngle(robot.turret.updateAngle())));
        telemetry.addData("Raw Turret Target", robot.turret.updateAngle());

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

        telemetry.addLine("Artifact Storage:");
        telemetry.addData("Total Inventory", robot.sorterLogic.inventory.getTotalCount());
        telemetry.addLine("Purple: " + robot.sorterLogic.inventory.getPurpleCount() +
                " Green: " + robot.sorterLogic.inventory.getGreenCount());
        telemetry.addData("Slot A", robot.sorterLogic.slotA.getOccupied());
        telemetry.addData("Slot B", robot.sorterLogic.slotB.getOccupied());
        telemetry.addData("Slot C", robot.sorterLogic.slotC.getOccupied());
        telemetry.addLine();
        telemetry.addData("Blender in position", robot.sorterHardware.positionedCheck());
        telemetry.addData("Equalized Target Position", robot.sorterLogic.offsetPositions.get(targetOffset));
        telemetry.addData("Launcher Velocity", robot.launcher.motor.getVelocity());
        telemetry.addData("Launcher Target Velocity", robot.launcher.velocityTarget);
        telemetry.addData("Launcher at Speed", robot.launcher.motorSpeedCheck(robot.launcher.velocityTarget));
        telemetry.addData("Current Load Slot", robot.sorterLogic.findCurrentSlotInPosition(LOAD).getName());
        telemetry.addData("Current Fire Slot", robot.sorterLogic.findCurrentSlotInPosition(FIRE).getName());
        telemetry.addData("LL Distance", robot.targetTag.distanceZ);
        telemetry.addData("Launcher PIDF", robot.launcher.motor.getPIDFCoefficients(RUN_USING_ENCODER));

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

    private void turretAssist()
    {
        if(gamepad2.right_stick_button)
        {
            robot.turret.input = gamepad2.right_stick_x;
        }
        else
        {
            robot.turret.input = 0;
        }
    }

    private boolean isEven(int x) {
        return x % 2 == 0;
    }
}


