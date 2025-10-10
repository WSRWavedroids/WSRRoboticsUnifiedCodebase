package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import java.util.Objects;


public class Robot {

    //public DcMotorEx frontLeftDrive; //todo un // these
    //public DcMotorEx frontRightDrive;
    //public DcMotorEx backLeftDrive;
    //public DcMotorEx backRightDrive;

    public DcMotorEx intakeMotor;

    public Telemetry telemetry;
    //public BNO055IMU imu;

    //init and declare war
    public OpMode opmode;
    public HardwareMap hardwareMap;
    public String startingPosition;
    public String controlMode = "Robot Centric";// Robot Centric
    public IMU.Parameters imuParameters;


    //Initialize motors and servos
    public Robot(HardwareMap hardwareMap, Telemetry telemetry, OpMode opmode){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.opmode = opmode;

        // This section turns the names of the pieces of hardware into variables that we can program with.
        // Make sure that the device name is the exact same thing you typed in on the configuration on the driver hub.
        //frontRightDrive = hardwareMap.get(DcMotorEx.class, "frontRightDrive"); //todo un // these
        //frontLeftDrive = hardwareMap.get(DcMotorEx.class, "frontLeftDrive");
        //backLeftDrive = hardwareMap.get(DcMotorEx.class, "backLeftDrive");
        //backRightDrive = hardwareMap.get(DcMotorEx.class, "backRightDrive");

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intake");

        imuParameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
        );

        // This section sets the direction of all of the motors. Depending on the motor, this may change later in the program.
        //frontLeftDrive.setDirection(DcMotor.Direction.FORWARD); //todo un // these
        //frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        //backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        //backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD); //TODO Add direction


        // This tells the motors to chill when we're not powering them.
        //frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //todo un // these
        //backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //This is new..
        telemetry.addData("Status", "Initialized");

    }

    //todo un // these and remove false
    public boolean isWheelsBusy() {
        return false;//backLeftDrive.isBusy() || frontLeftDrive.isBusy() || frontRightDrive.isBusy() || backRightDrive.isBusy();

    }

    public void stopAllMotors() {
        //frontLeftDrive.setPower(0);
        //frontRightDrive.setPower(0);
        //backLeftDrive.setPower(0);
        //backRightDrive.setPower(0);
    }

    public void setTargets(String direction, int ticks) {

        //This is all inverted (big sigh)
//todo un // these
        if (Objects.equals(direction, "Right")){
            //frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
            //frontRightDrive.setTargetPosition(ticks + frontRightDrive.getCurrentPosition());
            //backLeftDrive.setTargetPosition(ticks + backLeftDrive.getCurrentPosition());
            //backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
//todo un // these
        } else if (direction == "Left"){
            //frontLeftDrive.setTargetPosition(ticks + frontLeftDrive.getCurrentPosition());
            //frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
            //backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
            //backRightDrive.setTargetPosition(ticks + backRightDrive.getCurrentPosition());
//todo un // these
        } else if (direction == "Forward"){
            //frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
            //frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
            //backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
            //backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
//todo un // these
        } else if (direction == "Backward") {
            //frontLeftDrive.setTargetPosition(ticks + frontLeftDrive.getCurrentPosition());
            //frontRightDrive.setTargetPosition(ticks + frontRightDrive.getCurrentPosition());
            //backLeftDrive.setTargetPosition(ticks + backLeftDrive.getCurrentPosition());
            //backRightDrive.setTargetPosition(ticks + backRightDrive.getCurrentPosition());
//todo un // these
        } else if (direction == "Turn Right") {
            //frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
            //frontRightDrive.setTargetPosition(ticks + frontRightDrive.getCurrentPosition());
            //backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
            //backRightDrive.setTargetPosition(ticks + backRightDrive.getCurrentPosition());
//todo un // these
        } else if (direction == "Turn Left") {
            //frontLeftDrive.setTargetPosition(ticks + frontLeftDrive.getCurrentPosition());
            //frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
            //backLeftDrive.setTargetPosition(ticks + backLeftDrive.getCurrentPosition());
            //backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
        }//todo un // these
        else if (direction == "Diagonal Right") {
            //frontLeftDrive.setTargetPosition(-ticks + frontLeftDrive.getCurrentPosition());
            //frontRightDrive.setPower(0);
            //backLeftDrive.setPower(0);
            //backRightDrive.setTargetPosition(-ticks + backRightDrive.getCurrentPosition());
        }//todo un // these
        else if (direction == "Diagonal Left") {
            //frontLeftDrive.setPower(0);
            //frontRightDrive.setTargetPosition(-ticks + frontRightDrive.getCurrentPosition());
            //backLeftDrive.setTargetPosition(-ticks + backLeftDrive.getCurrentPosition());
            //backRightDrive.setPower(0);
        }




    }

    public void positionRunningMode(){
//todo un // these
        //frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void powerRunningMode()
    {//todo un // these
        //frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }//todo un // these
    public void powerSet(double speed) {
        //frontLeftDrive.setPower(speed);
        //frontRightDrive.setPower(speed);
        //backLeftDrive.setPower(speed);
        //backRightDrive.setPower(speed);

    }


    //todo un // these
    public DcMotor.RunMode encoderRunningMode(){
        //frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        return null;
    }
    //todo un // these
    public void encoderReset(){
        //frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    //todo un // these
    @SuppressLint("DefaultLocale")
    public void tellMotorOutput(){
        //telemetry.addData("Control Mode", controlMode);
        //telemetry.addData("Motors", String.format("FL Power(%.2f) FL Location (%d) FL Target (%d)", frontLeftDrive.getPower(), frontLeftDrive.getCurrentPosition(), frontLeftDrive.getTargetPosition()));
        //telemetry.addData("Motors", String.format("FR Power(%.2f) FR Location (%d) FR Target (%d)", frontRightDrive.getPower(), frontRightDrive.getCurrentPosition(), frontRightDrive.getTargetPosition()));
        //telemetry.addData("Motors", String.format("BL Power(%.2f) BL Location (%d) BL Target (%d)", backLeftDrive.getPower(), backLeftDrive.getCurrentPosition(), backLeftDrive.getTargetPosition()));
        //telemetry.addData("Motors", String.format("BR Power(%.2f) BR Location (%d) BR Target (%d)", backRightDrive.getPower(), backRightDrive.getCurrentPosition(), backRightDrive.getTargetPosition()));

        telemetry.update();
    }

    public double inchesToTicks(double inches) {
        // returns the inches * ticks per rotation / wheel circ
        return ((inches/12.25) * 537.6 / .5);
        //todo Reference that 1 inch ~= 50 ticks
    }

    ElapsedTime timer = new ElapsedTime();



    public void prepareAuto(){

    }

}
