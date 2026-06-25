package org.firstinspires.ftc.team13206.Core;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;
import static org.firstinspires.ftc.team13206.Core.Robot.DriveMode.*;
import static org.firstinspires.ftc.team13206.Core.SorterHardware.FeederState.INTAKE;
import static org.firstinspires.ftc.team13206.Core.SorterHardware.FeederState.PASSIVE;

import android.annotation.SuppressLint;

import com.bylazar.panels.Panels;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team13206.Vision.Limelight_Target_Scanner;
import org.firstinspires.ftc.team13206.Vision.WaveTag;
import org.firstinspires.ftc.team13206.Vision.Limelight_Randomization_Scanner;
import org.firstinspires.ftc.team13206.pedroPathing.Constants;

import java.util.List;
import java.util.Objects;

public class Robot {

    public DcMotorEx frontLeftDrive;
    public DcMotorEx frontRightDrive;
    public DcMotorEx backLeftDrive;
    public DcMotorEx backRightDrive;

    public DcMotorEx sorterMotor;
    public Servo turretServo;
    public DcMotorEx launcherMotorOne;
    public DcMotorEx launcherMotorTwo;
    public DcMotorEx intakeMotor;

    public Servo flicky;
    public AnalogInput flickyFeedback;
    public AnalogInput analogTurretTracker;
    public CRServo feedServo;

    public TouchSensor magsense;

    public Limelight3A limelight;

    public Servo fireRGB;
    public Servo loadRGB;
    public Servo storeRGB;

    public VoltageSensor voltageSensor;

    public RevColorSensorV3 leftColorScanner;

    public RevColorSensorV3 rightColorScanner;

    public GoBildaPinpointDriver pinpoint;

    public Telemetry telemetry;

    //init and declare war
    public OpMode opmode;
    public HardwareMap hardwareMap;

    public DriveMode controlMode = PEDRO;//ROBOT_CENTRIC;
    public IMU.Parameters imuParameters;
    public WaveTag targetTag = new WaveTag();
    public enum patternColors {PPG, GPP, PGP}
    public patternColors pattern;

    public enum allianceSides {
        BLUE(2), RED(1);
        public final int limelightPipeline;
        allianceSides(int limelightPipeline) {
            this.limelightPipeline = limelightPipeline;
        }
    }
    public allianceSides alliance;

    public Vector2 robotPosition;

    public Vector2 turretPosition;

    public double robotHeading;

    public double turretPositionOffsetXInches = 2.72, turretPositionOffsetYInches =1.57; //Inches from pedro position

    public boolean callPartialPedro = true;


    public SorterHardware sorterHardware;
    public LauncherHardware launcher;
    public ArtifactLocator sorterLogic;
    public TurretLogic turret;
    public Limelight_Randomization_Scanner randomizationScanner;
    public Limelight_Target_Scanner targetScanner;
    public fireQueueWithStates queue;
    public SlotLightManager blinkies;


    public Panels panels;

    public static TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    public enum DriveMode {ROBOT_CENTRIC, PEDRO, LEGACY_FIELD_CENTRIC}
    public enum OpenClosed {OPEN,CLOSED}
    public enum CardinalDirections {
        FORWARD, BACKWARD, LEFT, RIGHT,
        DIAGONAL_LEFT, DIAGONAL_RIGHT,
        TURN_LEFT, TURN_RIGHT}
    public enum UpDown {
        UP, DOWN
    }

    public boolean scanningForTargetTag = false;

    public int limelightSideOffsetAngle = 0;

    //Initialize motors and servos
    public Robot(HardwareMap hardwareMap, Telemetry telemetry, OpMode opmode) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.opmode = opmode;

        // There's pizza here!

        // This section turns the names of the pieces of hardware into variables that we can program with.
        // Make sure that the device name is the exact same thing you typed in on the configuration on the driver hub.
        frontRightDrive = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotorEx.class, "backRightDrive");

        sorterMotor = hardwareMap.get(DcMotorEx.class, "sorterMotor");
        turretServo = hardwareMap.get(Servo.class, "turretServo");
        launcherMotorOne = hardwareMap.get(DcMotorEx.class, "launcherMotor1");
        launcherMotorTwo = hardwareMap.get(DcMotorEx.class, "launcherMotor2");

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        leftColorScanner = hardwareMap.get(RevColorSensorV3.class, "leftColorScanner");
        rightColorScanner = hardwareMap.get(RevColorSensorV3.class, "rightColorScanner");

        feedServo = hardwareMap.get(CRServo.class, "feedServo");

        flicky = hardwareMap.get(Servo.class, "flicky");
        flickyFeedback = hardwareMap.get(AnalogInput.class, "flickyFeedback");

        analogTurretTracker = hardwareMap.get(AnalogInput.class, "analogTurretTracker");


        magsense = hardwareMap.get(TouchSensor.class, "magsense");

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        loadRGB = hardwareMap.get(Servo.class, "loadRGB");
        fireRGB = hardwareMap.get(Servo.class, "fireRGB");
        storeRGB = hardwareMap.get(Servo.class, "storeRGB");

        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        imuParameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
        );

        // This section sets the direction of all of the motors. Depending on the motor, this may change later in the program.
        frontLeftDrive.setDirection(REVERSE);
        frontRightDrive.setDirection(FORWARD);
        backLeftDrive.setDirection(REVERSE);
        backRightDrive.setDirection(FORWARD);

        sorterMotor.setDirection(FORWARD);
        intakeMotor.setDirection(REVERSE);

        // This tells the motors to chill when we're not powering them.
        frontRightDrive.setZeroPowerBehavior(BRAKE);
        backLeftDrive.setZeroPowerBehavior(BRAKE);
        backRightDrive.setZeroPowerBehavior(BRAKE);
        frontLeftDrive.setZeroPowerBehavior(BRAKE);
        sorterMotor.setZeroPowerBehavior(FLOAT);


        //This is new..
        telemetry.addData("Status", "Initialized");

        sorterHardware = new SorterHardware(this);
        sorterLogic = new ArtifactLocator(this);
        launcher = new LauncherHardware(this);
        queue = new fireQueueWithStates(this);
        targetScanner = new Limelight_Target_Scanner(this);
        randomizationScanner = new Limelight_Randomization_Scanner(this);
        turret = new TurretLogic(this, null);
        blinkies = new SlotLightManager(this);

        robotPosition = new Vector2();
        turretPosition = new Vector2();

        turret.follower = Constants.createFollower(hardwareMap);
        turret.follower.setMaxPowerScaling(0);

        if (alliance == null) alliance = allianceSides.BLUE;
    }


    public boolean isWheelsBusy() {
        return backLeftDrive.isBusy() || frontLeftDrive.isBusy() || frontRightDrive.isBusy() || backRightDrive.isBusy();
    }

    public void stopAllMotors() {
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    /**
     * Runs the drive train in a cardinal direction.
     * @param direction The direction, a CardinalDirections enum
     * @param ticks The distance to move in motor ticks
     */
    public void setTargets(CardinalDirections direction, int ticks) {

        // This is all inverted (big sigh)

        switch (direction) {
            case RIGHT:
                frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
                frontRightDrive.setTargetPosition(ticks + frontRightDrive.getCurrentPosition());
                backLeftDrive.setTargetPosition(ticks + backLeftDrive.getCurrentPosition());
                backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
                break;
            case LEFT:
                frontLeftDrive.setTargetPosition(ticks + frontLeftDrive.getCurrentPosition());
                frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
                backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
                backRightDrive.setTargetPosition(ticks + backRightDrive.getCurrentPosition());
                break;
            case FORWARD:
                frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
                frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
                backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
                backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
                break;
            case BACKWARD:
                frontLeftDrive.setTargetPosition(ticks + frontLeftDrive.getCurrentPosition());
                frontRightDrive.setTargetPosition(ticks + frontRightDrive.getCurrentPosition());
                backLeftDrive.setTargetPosition(ticks + backLeftDrive.getCurrentPosition());
                backRightDrive.setTargetPosition(ticks + backRightDrive.getCurrentPosition());
                break;
            case TURN_RIGHT:
                frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
                frontRightDrive.setTargetPosition(ticks + frontRightDrive.getCurrentPosition());
                backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
                backRightDrive.setTargetPosition(ticks + backRightDrive.getCurrentPosition());
                break;
            case TURN_LEFT:
                frontLeftDrive.setTargetPosition(ticks + frontLeftDrive.getCurrentPosition());
                frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
                backLeftDrive.setTargetPosition(ticks + backLeftDrive.getCurrentPosition());
                backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
                break;
            case DIAGONAL_RIGHT:
                frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
                frontRightDrive.setPower(frontRightDrive.getCurrentPosition());
                backLeftDrive.setPower(backLeftDrive.getCurrentPosition());
                backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
                break;
            case DIAGONAL_LEFT:
                frontLeftDrive.setPower(frontLeftDrive.getCurrentPosition());
                frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
                backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
                backRightDrive.setPower(backRightDrive.getCurrentPosition());
                break;
        }
    }

    public void setRunMode(DcMotor.RunMode runMode) {
        frontLeftDrive.setMode(runMode);
        frontRightDrive.setMode(runMode);
        backLeftDrive.setMode(runMode);
        backRightDrive.setMode(runMode);
    }

    /**
     * Sets the drive motors to RUN_TO_POSITION. Enables usage of the DcMotor.setTargetPosition()
     * and Robot.setTargets() functions.
     */
    public void positionRunningMode() {

        frontLeftDrive.setMode(RUN_TO_POSITION);
        frontRightDrive.setMode(RUN_TO_POSITION);
        backLeftDrive.setMode(RUN_TO_POSITION);
        backRightDrive.setMode(RUN_TO_POSITION);
    }

    /**
     * Turns off the motor encoders, to run purely on power.
     */
    public void powerRunningMode()
    {
        frontLeftDrive.setMode(RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(RUN_WITHOUT_ENCODER);
    }
    public void powerSet(double speed) {
        frontLeftDrive.setPower(speed);
        frontRightDrive.setPower(speed);
        backLeftDrive.setPower(speed);
        backRightDrive.setPower(speed);

    }

    /**
     * Sets the motors to run with encoder feedback.
     */
    public void encoderRunningMode(){
        frontLeftDrive.setMode(RUN_USING_ENCODER);
        frontRightDrive.setMode(RUN_USING_ENCODER);
        backLeftDrive.setMode(RUN_USING_ENCODER);
        backRightDrive.setMode(RUN_USING_ENCODER);
    }

    public void encoderReset(){
        frontLeftDrive.setMode(STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(STOP_AND_RESET_ENCODER);
    }

    /**
     * Adds motor data to telemetry and updates it.
     */
    @SuppressLint("DefaultLocale")
    public void tellMotorOutput(){
        telemetry.addData("Control Mode", controlMode);
        telemetry.addData("Motors", String.format("FL Power(%.2f) FL Location (%d) FL Target (%d)", frontLeftDrive.getPower(), frontLeftDrive.getCurrentPosition(), frontLeftDrive.getTargetPosition()));
        telemetry.addData("Motors", String.format("FR Power(%.2f) FR Location (%d) FR Target (%d)", frontRightDrive.getPower(), frontRightDrive.getCurrentPosition(), frontRightDrive.getTargetPosition()));
        telemetry.addData("Motors", String.format("BL Power(%.2f) BL Location (%d) BL Target (%d)", backLeftDrive.getPower(), backLeftDrive.getCurrentPosition(), backLeftDrive.getTargetPosition()));
        telemetry.addData("Motors", String.format("BR Power(%.2f) BR Location (%d) BR Target (%d)", backRightDrive.getPower(), backRightDrive.getCurrentPosition(), backRightDrive.getTargetPosition()));

        telemetry.update();
    }

    public double inchesToTicks(double inches) {
        // returns the inches * ticks per rotation / wheel circ
        return ((inches/12.25) * 537.6 / .5);
        //todo Reference that 1 inch ~= 50 ticks
    }


    public void readyHardware(boolean resetEncoder) {
        sorterHardware.flicky.setPosition(sorterHardware.flickyDownPosition);
        launcher.setLauncherVelocity(0);

        if(resetEncoder)
        {
            sorterHardware.resetSorterEncoder();
            encoderReset();
            sorterHardware.reference = 0;
        }
    }
}
