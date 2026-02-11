package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;
import static org.firstinspires.ftc.teamcode.Robot.launchSteps.*;

import android.annotation.SuppressLint;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDPatternCommand;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;
import java.util.Objects;


public class Robot {
//add motors w their names here
    public DcMotorEx frontLeftDrive;
    public DcMotorEx frontRightDrive;
    public DcMotorEx backLeftDrive;
    public DcMotorEx backRightDrive;

    public DcMotorEx intakeMotor;
    public CRServo intake2;
    public DcMotorEx intake3;

    public DcMotorEx launchLeft;
    public DcMotorEx launchRight;

    public Limelight3A limelight;

    public Telemetry telemetry;

    public GoBildaPinpointDriver pinpoint;
    //public BNO055IMU imu;

    //init and declare war
    public OpMode opmode;
    public HardwareMap hardwareMap;
    public String startingPosition;
    public String controlMode = "Robot Centric";// Robot Centric
    public IMU.Parameters imuParameters;

    public double intakeSpeed;
    public double intakeTune;
    public double launchTune;
    public double triggerDeadzone;

    public static double launcherP =0;
    public static double launcherI =0;
    public static double launcherD =0;
    public static double launcherF =0;

    public static TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();


    //Initialize motors and servos
    public Robot(HardwareMap hardwareMap, Telemetry telemetry, OpMode opmode){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.opmode = opmode;

        // This section turns the names of the pieces of hardware into variables that we can program with.
        // Make sure that the device name is the exact same thing you typed in on the configuration on the driver hub.
        frontRightDrive = hardwareMap.get(DcMotorEx.class, "frontRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        backLeftDrive = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotorEx.class, "backRightDrive");

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");
        launchLeft = hardwareMap.get(DcMotorEx.class, "launchLeft");
        launchRight = hardwareMap.get(DcMotorEx.class, "launchRight");
        intake3 = hardwareMap.get(DcMotorEx.class, "intake3");
        intake2 = hardwareMap.get(CRServo.class, "servo1");
        limelight = hardwareMap.get(Limelight3A.class, "Limelight");
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class,"pinpoint");

        launchLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        imuParameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
        );

        // This section sets the direction of all of the motors. Depending on the motor, this may change later in the program.

        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);


        // This tells the motors to chill when we're not powering them.
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launchRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launchLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        launchRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launchRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        //This is new..
        telemetry.addData("Status", "Initialized");

    }


    public boolean isWheelsBusy(){
        return backLeftDrive.isBusy() || frontLeftDrive.isBusy() || frontRightDrive.isBusy() || backRightDrive.isBusy();
    }

    public void stopAllMotors() {
    }


    public void setTargets(String direction, int ticks) {

        //This is all inverted (big sigh)

        if (Objects.equals(direction, "Right")){
            frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
            frontRightDrive.setTargetPosition(ticks + frontRightDrive.getCurrentPosition());
            backLeftDrive.setTargetPosition(ticks + backLeftDrive.getCurrentPosition());
            backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());

        } else if (direction == "Left"){
            frontLeftDrive.setTargetPosition(ticks + frontLeftDrive.getCurrentPosition());
            frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
            backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
            backRightDrive.setTargetPosition(ticks + backRightDrive.getCurrentPosition());

        } else if (direction == "Forward"){
            frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
            frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
            backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
            backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());

        } else if (direction == "Backward") {
            frontLeftDrive.setTargetPosition(ticks + frontLeftDrive.getCurrentPosition());
            frontRightDrive.setTargetPosition(ticks + frontRightDrive.getCurrentPosition());
            backLeftDrive.setTargetPosition(ticks + backLeftDrive.getCurrentPosition());
            backRightDrive.setTargetPosition(ticks + backRightDrive.getCurrentPosition());

        } else if (direction == "Turn Right") {
            frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
            frontRightDrive.setTargetPosition(ticks + frontRightDrive.getCurrentPosition());
            backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
            backRightDrive.setTargetPosition(ticks + backRightDrive.getCurrentPosition());

        } else if (direction == "Turn Left") {
            frontLeftDrive.setTargetPosition(ticks + frontLeftDrive.getCurrentPosition());
            frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
            backLeftDrive.setTargetPosition(ticks + backLeftDrive.getCurrentPosition());
            backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
        }
        else if (direction == "Diagonal Right") {
            frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
            frontRightDrive.setPower(0);
            backLeftDrive.setPower(0);
            backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
        }
        else if (direction == "Diagonal Left") {
            frontLeftDrive.setPower(0);
            frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
            backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
            backRightDrive.setPower(0);
        }




    }

    public void positionRunningMode(){

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void powerRunningMode()
    {
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        launchRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launchLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void powerSet(double speed) {
        frontLeftDrive.setPower(speed);
        frontRightDrive.setPower(speed);
        backLeftDrive.setPower(speed);
        backRightDrive.setPower(speed);

    }


    public DcMotor.RunMode encoderRunningMode(){
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        return null;
    }
    public void encoderReset(){
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    @SuppressLint("DefaultLocale")
    public void tellMotorOutput(){
        telemetry.addData("Control Mode", controlMode);
        telemetry.addData("Motors", String.format("FL Power(%.2f) FL Location (%d) FL Target (%d)", frontLeftDrive.getPower(), frontLeftDrive.getCurrentPosition(), frontLeftDrive.getTargetPosition()));
        telemetry.addData("Motors", String.format("FR Power(%.2f) FR Location (%d) FR Target (%d)", frontRightDrive.getPower(), frontRightDrive.getCurrentPosition(), frontRightDrive.getTargetPosition()));
        telemetry.addData("Motors", String.format("BL Power(%.2f) BL Location (%d) BL Target (%d)", backLeftDrive.getPower(), backLeftDrive.getCurrentPosition(), backLeftDrive.getTargetPosition()));
        telemetry.addData("Motors", String.format("BR Power(%.2f) BR Location (%d) BR Target (%d)", backRightDrive.getPower(), backRightDrive.getCurrentPosition(), backRightDrive.getTargetPosition()));

        telemetry.update();
        panelsTelemetry.update();
    }

    public double inchesToTicks(double inches) {
        // returns the inches * ticks per rotation / wheel circ
        return ((inches/12.25) * 537.6 / .5);
        //todo Reference that 1 inch ~= 50 ticks
    }

    public void limelightTelemetry(){
        if (!limelight.isConnected()){
            telemetry.addLine();
            telemetry.addLine("LIMELIGHT UNPLUGGED");
            telemetry.addLine("LIMELIGHT UNPLUGGED");
            telemetry.addLine("LIMELIGHT UNPLUGGED");
            telemetry.addLine("LIMELIGHT UNPLUGGED");
            telemetry.addLine("LIMELIGHT UNPLUGGED");
            telemetry.addLine("LIMELIGHT UNPLUGGED");
            telemetry.addLine("LIMELIGHT UNPLUGGED");
            telemetry.addLine("LIMELIGHT UNPLUGGED");
            telemetry.addLine("LIMELIGHT UNPLUGGED");
            telemetry.addLine("LIMELIGHT UNPLUGGED");
        }
        telemetry.addLine();
        telemetry.addData("Limelight is initialized?" ,limelight.isConnected());
        telemetry.addData("cam dist to apriltag" ,getApriltagDistance());
        telemetry.addData("launch spd" ,limelightAdjustedSpeed/2000);

    }


    public void setupLaunchers() {

        double dist = getApriltagDistance();
        if (dist == 0){
            dist = 1.6;
        }

        launcherMath(dist,
                103.80245,
                1008.15851,
                3610.79545,
                5511.20962,
                3765);
    }

    public void launcherMath(double x,double m1, double m2, double m3, double m4, double add){
        //PIDFCoefficients  numbers = new PIDFCoefficients(launcherP, launcherI, launcherD, launcherF);
        //launchLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, numbers);
        //launchRight.setPIDFCoefficients((DcMotor.RunMode.RUN_USING_ENCODER), numbers);
        limelightAdjustedSpeed = m1*Math.pow(x,4) - m2*Math.pow(x,3) + m3*Math.pow(x,2) - m4*x + add;
    }

    public boolean upToSpeed(){
        double velocity = launchLeft.getVelocity();
        return (velocity > -limelightAdjustedSpeed - 40) && (velocity < -limelightAdjustedSpeed + 40);
    }

    public boolean upToSpeed(double tolerance){
        double velocity = launchLeft.getVelocity();
        return (velocity > -limelightAdjustedSpeed - tolerance) && (velocity < -limelightAdjustedSpeed + tolerance);
    }

public ElapsedTime cooldown = new ElapsedTime();

    public enum launchSteps {
        START,
        STARTMOTORS,
        STARTINTAKE,
        END
    }

    public launchSteps currentstep = launchSteps.START;

public boolean doneLaunching = false;

        public void launchLoop(double sleep1, double sleep2) {
            switch(currentstep) {
                case START:
                    doneLaunching = false;
                    cooldown.reset();
                    currentstep = STARTMOTORS;
                    break;
                case STARTMOTORS:
                    setupLaunchers();
                    launchLeft.setVelocity(-limelightAdjustedSpeed);
                    launchRight.setVelocity(-limelightAdjustedSpeed);
                    if (!upToSpeed()) {
                        if (cooldown.seconds() >= 0.003) {
                            cooldown.reset();
                        }
                    }
                    else if (cooldown.milliseconds() >= sleep1) {
                        cooldown.reset();
                        currentstep = STARTINTAKE;
                    }
                    break;
                case STARTINTAKE:
                    intake3.setPower(1);
                    intake2.setPower(-1);
                    intakeMotor.setPower(1);
                    if (cooldown.milliseconds() >= sleep2) {
                        cooldown.reset();
                        currentstep = END;
                    }
                    break;
                case END:
                    intake3.setPower(0);
                    intake2.setPower(0);
                    intakeMotor.setPower(0);
                    launchLeft.setVelocity(-0);
                    launchRight.setVelocity(-0);
                    doneLaunching = true;
                    currentstep = START;
                    break;
            }
        }


    ElapsedTime timer = new ElapsedTime();

    public double tuningspd = 0.5;
    public double limelightAdjustedSpeed = 0;

    public void prepareAuto() {

    }

    public void initLimelight() {
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public double getApriltagDistance(){
        List<LLResultTypes.FiducialResult> results = limelight.getLatestResult().getFiducialResults();
        if (!results.isEmpty()) {
            return results.get(0).getTargetPoseCameraSpace().getPosition().z;
        }
        return 0;
    }
}
