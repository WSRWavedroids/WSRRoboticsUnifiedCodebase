package org.firstinspires.ftc.teamcode.Teleop;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;
import static org.firstinspires.ftc.teamcode.Robot.allianceSides.BLUE;
import static org.firstinspires.ftc.teamcode.Robot.allianceSides.RED;
import static org.firstinspires.ftc.teamcode.Robot.driveMode.AUTOTARGET;
import static org.firstinspires.ftc.teamcode.Robot.driveMode.CLOSELAUNCH;
import static org.firstinspires.ftc.teamcode.Robot.driveMode.FARLAUNCH;
import static org.firstinspires.ftc.teamcode.Robot.driveMode.FIELDCENTRIC;
import static org.firstinspires.ftc.teamcode.Robot.driveMode.HOLDPOINT;
import static org.firstinspires.ftc.teamcode.Robot.driveMode.LEVER;
import static org.firstinspires.ftc.teamcode.Robot.driveMode.PARK;
import static org.firstinspires.ftc.teamcode.Robot.driveMode.ROBOTCENTRIC;
import static org.firstinspires.ftc.teamcode.Teleop.Basic_TeleOp_NewBot.AutoLaunchSteps.*;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.opencv.core.Mat;

/**
 * This file is our iterative (Non-Linear) "OpMode" for TeleOp.
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is selected on the Robot Controller and executed.
 * This particular one is called "Lean Mean TeleOp Machine". I had a little too much fun with naming this.
 * <p>
 * This OpMode controls the functions of the robot during the driver-controlled period.
 * <p>
 * If the "@Disabled" line is not commented out, the program will not show up on the driver hub.
 * If you ever have problems with the program not showing up on the driver hub, it's probably because of that.
 * <p>
 * Throughout this program, there are comments explaining what everything does because previous programmers
 * did a horrible job of doing that.
 */

//HI this is BotShell the 3rd. I am a legendary turtle. This is my food please make good tasty and not wrong.
@TeleOp(name = "Shellbot", group = "CompBot")
public class Basic_TeleOp_NewBot extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();
    private double speed = 0.75;
    private boolean spinTargetAquired = false;
    int SpinTargetFrontLeft;
    int SpinTargetFrontRight;
    int SpinTargetBackLeft;
    int SpinTargetBackRight;
    private double launcherLimit = 0.42;
    private double launcherLimit2 = 0.50;

    //private double storedSpeed;
    public Robot robot = null;
    public IMU imu;
    public static final String ALLIANCE_KEY = "Alliance"; //For blackboard
    public static final String PATTERN_KEY = "Pattern";
    public boolean canManuallyControlVerticalSlides = true;
    ElapsedTime outtakeTimer = new ElapsedTime();
    public Follower follower;
    double heading;
    Pose startingPose;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    public void init() {

        // Call the initialization protocol from the Robot class.
        robot = new Robot(hardwareMap, telemetry, this);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        outtakeTimer.reset();

        if (robot.controlMode == "Field Centric") {

            imu = hardwareMap.get(IMU.class, "imu");
            IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.RIGHT)); //Forward = left fsr
            // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
            imu.initialize(parameters);
        }
        //if using field centric youl need this lolzeez

        Object poseX = blackboard.getOrDefault("poseX", 72.0);
        Object poseY = blackboard.getOrDefault("poseY", 72.0);
        Object poseHeading = blackboard.getOrDefault("poseHeading", Math.PI / 2);
        double goodX = 72;
        double goodY = 72;
        double goodHeading = Math.PI / 2;
        if (poseX instanceof Number) {
            goodX = (double) poseX;
        }
        if (poseY instanceof Number) {
            goodY = (double) poseY;
        }
        if (poseHeading instanceof Number) {
            goodHeading = (double) poseHeading;
        }
        startingPose = new Pose(goodX, goodY, goodHeading);
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose);
        follower.update();
        robot.alliance = RED;
    }


    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    public void init_loop() {
        telemetry.addData("HYPE", "ARE! YOU! READY?!?!?!?!");
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    public void start() {
        runtime.reset();
        telemetry.addData("HYPE", "Let's do this!!!");
        gamepad1.setLedColor(0, 0, 255, 100000000);
        gamepad2.setLedColor(255, 0, 240, 100000000);
        robot.initLimelight();
        robot.tuningspd = 0.43;
        follower.update();
        switchdrivemode(ROBOTCENTRIC);
        robot.alliance = RED;
    }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {
        follower.update();
        follower.setMaxPower(1);
        driveMode();
        switchAlliance();
        bandaid();
        //So Begins the input chain. At least try a bit to organise by driver

        //Driver 2

        //controlMode();
        driveSpeed();

        //intakes and launcher
        robot.setupLaunchers();
        intakeservoforward();
        robot.triggerDeadzone = 0.05;
        launch(robot.tuningspd);


        //todo THIS is what NON-PROGRAMMERS can edit (intake speeds)
        //note that the speeds are a decimal between -1 and 1
        //note negative speeds(-1) are reverse, positive(1) are forward, 0 is neutral/off

        intake1(1, 1, 1);
        intake3(1, 1, 1);

        //todo ONLY the stuff BETWEEN the green "TODO" lines are open to everyone


        if (gamepad1.leftBumperWasPressed()) {
            robot.tuningspd = robot.tuningspd + 0.005;
        }
        if (gamepad1.rightBumperWasPressed()) {
            robot.tuningspd = robot.tuningspd - 0.005;
        }
        if (robot.upToSpeed()) {
            gamepad2.rumble(500);
        }


        //Matthew Was Here
        telemetry.addData("tuningspd", robot.tuningspd);
        telemetry.addData("LR Power", robot.launchRight.getPower());
        telemetry.addData("LL Power", robot.launchLeft.getPower());
        telemetry.addData("LR Velocity", robot.launchRight.getVelocity());
        telemetry.addData("LL Velocity", robot.launchLeft.getVelocity());
        telemetry.addData("Launch Tune", robot.launchTune);

        telemetry.addData("cam dist to apriltag", robot.getApriltagDistance());

        telemetry.addData("Launch P", robot.launcherP);
        telemetry.addData("Launch I", robot.launcherI);
        telemetry.addData("Launch D", robot.launcherD);
        telemetry.addData("Launch F", robot.launcherF);
        telemetry.addData("Drive Mode", robot.driveMode);
        telemetry.addData("alliance", robot.alliance);
        telemetry.addData("heading", heading);
        telemetry.addData("starting pose", startingPose);
        telemetry.addData("current X", follower.getPose().getX());
        telemetry.addData("current Y", follower.getPose().getY());
        telemetry.addData("current heading", follower.getPose().getHeading());
        telemetry.addData("FL", robot.frontLeftDrive.getZeroPowerBehavior());
        telemetry.addData("FR", robot.frontRightDrive.getZeroPowerBehavior());
        telemetry.addData("BL", robot.backLeftDrive.getZeroPowerBehavior());
        telemetry.addData("BR", robot.backRightDrive.getZeroPowerBehavior());

        Robot.panelsTelemetry.addData("velocityRight", -robot.launchRight.getVelocity());
        Robot.panelsTelemetry.addData("velocityLeft", -robot.launchLeft.getVelocity());
        Robot.panelsTelemetry.addData("goalVelocityLimelight", robot.limelightAdjustedSpeed);
        Robot.panelsTelemetry.addData("goalVelocity", robot.tuningspd * 2000);
        Robot.panelsTelemetry.update();
        //doTelemetryStuff();
        //driver 1
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {
        telemetry.addData("Status", "Robot Stopped");
    }
//aple sinc please
//    i praise tmmothy cooker
//field centric
        /*if (robot.alliance == RED) {
            follower.setTeleOpDrive(
                    gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x,
                    false,
                    Math.toRadians(0)
                    );
        }
        if (robot.alliance == BLUE) {
            follower.setTeleOpDrive(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    false,
                    Math.toRadians(180)
                    );
        }*/

    //auto locking and field centric
    private void bandaid() {
        if (robot.driveMode == ROBOTCENTRIC && robot.frontLeftDrive.getZeroPowerBehavior() == FLOAT) {
            robot.frontLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.frontRightDrive.setZeroPowerBehavior(BRAKE);
            robot.backLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.backRightDrive.setZeroPowerBehavior(BRAKE);
        }
        if (robot.driveMode == ROBOTCENTRIC && robot.frontRightDrive.getZeroPowerBehavior() == FLOAT) {
            robot.frontLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.frontRightDrive.setZeroPowerBehavior(BRAKE);
            robot.backLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.backRightDrive.setZeroPowerBehavior(BRAKE);
        }
        if (robot.driveMode == ROBOTCENTRIC && robot.backLeftDrive.getZeroPowerBehavior() == FLOAT) {
            robot.frontLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.frontRightDrive.setZeroPowerBehavior(BRAKE);
            robot.backLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.backRightDrive.setZeroPowerBehavior(BRAKE);
        }
        if (robot.driveMode == ROBOTCENTRIC && robot.backRightDrive.getZeroPowerBehavior() == FLOAT) {
            robot.frontLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.frontRightDrive.setZeroPowerBehavior(BRAKE);
            robot.backLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.backRightDrive.setZeroPowerBehavior(BRAKE);
        }
    }
    private void switchOPcancel(double xblue, double yblue, double xred, double yred) {
        if (robot.alliance == RED) {
            if (Math.abs(follower.getPose().getX() - xred) <= 5 && Math.abs(follower.getPose().getY() - yred) <= 5) {
                follower.breakFollowing();
            }
        }
        if (robot.alliance == BLUE) {
            if (Math.abs(follower.getPose().getX() - xblue) <= 5 && Math.abs(follower.getPose().getY() - yblue) <= 5) {
                follower.breakFollowing();
            }
        }

    }

    private void resetOP() {
        if (cancelSpecialOPs()) {
            follower.breakFollowing();
            switchdrivemode(drivemodeSave);
            telemetry.addLine("OP was reset" );
        }
    }

    private boolean cancelSpecialOPs(){
        if (robot.driveMode != FIELDCENTRIC && robot.driveMode != ROBOTCENTRIC) {
            if (anyButtonPressed1()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void switchdrivemode(Robot.driveMode mode) {
        switch (mode) {
            case ROBOTCENTRIC:
                robot.driveMode = ROBOTCENTRIC;
                follower.breakFollowing();
                robot.frontLeftDrive.setZeroPowerBehavior(BRAKE);
                robot.frontRightDrive.setZeroPowerBehavior(BRAKE);
                robot.backLeftDrive.setZeroPowerBehavior(BRAKE);
                robot.backRightDrive.setZeroPowerBehavior(BRAKE);
                break;

            case FIELDCENTRIC:
                robot.driveMode = FIELDCENTRIC;
                follower.breakFollowing();
                follower.startTeleOpDrive();
                break;

            case AUTOTARGET:
                robot.driveMode = AUTOTARGET;
                follower.breakFollowing();
                follower.startTeleOpDrive();
                break;

            case HOLDPOINT:
                robot.driveMode = HOLDPOINT;
                follower.breakFollowing();
                follower.startTeleOpDrive();
                follower.holdPoint(new BezierPoint(follower.getPose()), follower.getHeading());
                break;

            case FARLAUNCH:
                robot.driveMode = FARLAUNCH;
                follower.breakFollowing();
                follower.startTeleOpDrive();
                if (robot.alliance == RED) {
                    follower.followPath(goTo(85.9, 16.6, 67.5));
                }
                if (robot.alliance == BLUE) {
                    follower.followPath(goTo(58.1, 16.6, 112.5));
                }
                break;

            case CLOSELAUNCH:
                robot.driveMode = CLOSELAUNCH;
                follower.breakFollowing();
                follower.startTeleOpDrive();
                if (robot.alliance == RED) {
                    follower.followPath(goTo(39, 135, 0));
                }
                if (robot.alliance == BLUE) {
                    follower.followPath(goTo(100, 135, 180));
                }
                break;

            case LEVER:
                robot.driveMode = LEVER;
                follower.breakFollowing();
                follower.startTeleOpDrive();
                if (robot.alliance == RED) {
                    follower.followPath(goTo(12, 59, 320));
                }
                if (robot.alliance == BLUE) {
                    follower.followPath(goTo(132, 59, 220));
                }
                break;

            case PARK:
                robot.driveMode = PARK;
                follower.breakFollowing();
                follower.startTeleOpDrive();
                if (robot.alliance == RED) {
                    follower.followPath(goTo(38, 30.5, 180));
                }
                if (robot.alliance == BLUE) {
                    follower.followPath(goTo(102, 30.5, 0));
                }
                break;
        }
    }


    //todo this is robot movement modes - update accordingly
    Robot.driveMode drivemodeSave = ROBOTCENTRIC;

    private void driveMode() {
        double multiplier = 1;
        double x = follower.getPose().getX();
        double y = follower.getPose().getX();
        double opWasSwitched = 0;


        //extra button functions
        /*if (gamepad1.yWasPressed()) {
            switchdrivemode(AUTOTARGET);
            opWasSwitched = 1;
            if (robot.alliance == RED) {
                heading = Math.atan2(144 - y, 144 - x);
                follower.holdPoint(new BezierPoint( follower.getPose()), heading);
            }
            if (robot.alliance == BLUE) {
                heading = Math.atan2(144 - y, 0 - x);
                follower.holdPoint(new BezierPoint( follower.getPose()), heading);
            }
        } else if (gamepad1.yWasReleased()) {
            switchdrivemode(drivemodeSave);
            robot.frontLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.frontRightDrive.setZeroPowerBehavior(BRAKE);
            robot.backLeftDrive.setZeroPowerBehavior(BRAKE);
            robot.backRightDrive.setZeroPowerBehavior(BRAKE);
        }*/

        /*if (gamepad1.aWasPressed()) {
            switchdrivemode(HOLDPOINT);
            opWasSwitched = 1;
        } /*else if (gamepad1.aWasReleased()) {
            follower.breakFollowing();
            switchdrivemode(drivemodeSave);
        } */

        /*if (gamepad1.leftTriggerWasPressed()) {
            switchdrivemode(FARLAUNCH);
            opWasSwitched = 1;
            switchOPcancel(58.1, 16.6, 85.9, 16.6);

        } /*else if (gamepad1.leftTriggerWasReleased()) {
            follower.breakFollowing();
            switchdrivemode(drivemodeSave);
        }*/

        /*if (gamepad1.rightTriggerWasPressed()) {
            switchdrivemode(CLOSELAUNCH);
            opWasSwitched = 1;
            switchOPcancel(100, 135, 39, 135);

        } /*else if (gamepad1.rightTriggerWasReleased()) {
            follower.breakFollowing();
            switchdrivemode(drivemodeSave);
        }*/

        /*if (gamepad1.leftBumperWasPressed()) {
            switchdrivemode(LEVER);
            opWasSwitched = 1;
            switchOPcancel(132, 59, 12, 59);
        } /*else if (gamepad1.leftBumperWasReleased()) {
            follower.breakFollowing();
            switchdrivemode(drivemodeSave);
        }*/

        /*if (gamepad1.rightBumperWasPressed()) {
            switchdrivemode(PARK);
            opWasSwitched = 1;
            switchOPcancel(102, 30.5, 38, 30.5);

        } /*else if (gamepad1.rightBumperWasReleased()) {
            follower.breakFollowing();
            switchdrivemode(drivemodeSave);
        }*/


        if (opWasSwitched == 0) {
            resetOP();
        }

        //toggle between centric modes
        if (gamepad1.xWasPressed() & !gamepad1.y) {
            switch (drivemodeSave) {
                case ROBOTCENTRIC:
                    drivemodeSave = FIELDCENTRIC;
                    break;
                case FIELDCENTRIC:
                    drivemodeSave = ROBOTCENTRIC;
                    break;
            }
            switchdrivemode(drivemodeSave);
        }


        switch (robot.driveMode) {
            case FIELDCENTRIC:
                follower.setTeleOpDrive(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -gamepad1.right_stick_x,
                        false,
                        Math.toRadians(90));
                break;
            case ROBOTCENTRIC:
                singleJoystickDrive();
                break;
            case AUTOTARGET:
                if (robot.alliance == RED) {
                    heading = Math.atan2(144 - y, 144 - x);
                }
                if (robot.alliance == BLUE) {
                    heading = Math.atan2(144 - y, 0 - x);
                }
                break;
            case HOLDPOINT:
                break;
            case FARLAUNCH:
                break;
            case CLOSELAUNCH:
                break;
            case LEVER:
                break;
            case PARK:
                break;
        }
    }

    private void switchAlliance() {
        if(gamepad1.touchpadWasPressed())
            if (robot.alliance == RED) {
                robot.alliance = BLUE;
            } else if (robot.alliance == BLUE) {
                robot.alliance = RED;
            }
        }

    private PathChain goTo(double locationX, double locationY , double heading) {
        Pose pose = new Pose(locationX, locationY, Math.toRadians(heading));
        return follower.pathBuilder()
                .addPath(new BezierLine(follower.getPose(), pose))
                .setLinearHeadingInterpolation(follower.getHeading(), Math.toRadians(heading))
                .build();
    }






    /*
     * The holding cell for all of the random functions we call above.
     */

    public void setIndividualPowers(float[] motorPowers) {
        // This function creates an array so that the function below works.
        // Don't mess with this function unless you know what you're doing.

        if (motorPowers.length != 4) {
            return;
        }
        robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.frontLeftDrive.setPower(motorPowers[0]);
        robot.frontRightDrive.setPower(motorPowers[1]);
        robot.backLeftDrive.setPower(motorPowers[2]);
        robot.backRightDrive.setPower(motorPowers[3]);
    }

    private void singleJoystickDrive() {
        // We don't really know how this function works, but it makes the wheels drive, so we don't question it.
        // Don't mess with this function unless you REALLY know what you're doing.
        float leftY = -this.gamepad1.left_stick_y;
        float rightX = this.gamepad1.right_stick_x;
        float leftX = this.gamepad1.left_stick_x;

        double leftStickAngle = Math.atan2(leftY, leftX);
        double leftStickMagnitude = Math.sqrt(leftX * 2.0 + leftY * 2.0);
        //double robotAngle = robot.imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;

        if (leftStickMagnitude > 1){
            leftStickMagnitude = 1;
        }

        float[] motorPowers = new float[4];

        if (robot.controlMode == "Robot Centric") {

            motorPowers[0] = (leftY + leftX + rightX);//might need inverted back
            motorPowers[1] = (leftY - leftX - rightX);
            motorPowers[2] = (leftY - leftX + rightX);
            motorPowers[3] = (leftY + leftX - rightX);

        } else if (robot.controlMode == "Field Centric") {
            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);;// sparky.getPosition().h

            // Rotate the movement direction counter to the bot's rotation
            double rotX = leftX * Math.cos(-botHeading) - leftY * Math.sin(-botHeading);
            double rotY = leftX * Math.sin(-botHeading) + leftY * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rightX), 1);
            double frontLeftPower = (rotY + rotX + rightX) / denominator; //all of the right xs got inverted
            double backLeftPower = (rotY - rotX - rightX) / denominator;
            double frontRightPower = (rotY - rotX + rightX) / denominator;
            double backRightPower = (rotY + rotX - rightX) / denominator;



            motorPowers[0] = (float)frontLeftPower;
            motorPowers[1] = (float) backLeftPower;
            motorPowers[2] = (float)frontRightPower;
            motorPowers[3] = (float) backRightPower;
        }


        float max = getLargestAbsVal(motorPowers);
        if (max < 1) {
            max = 1;
        }

        for (int i = 0; i < motorPowers.length; i++) {
            motorPowers[i] *= (speed / max);

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
            if (robot.controlMode == "Robot Centric"){
                robot.controlMode = "Field Centric";
                telemetry.addData("Control Mode", "Field Centric Controls");
            } else if (robot.controlMode == "Field Centric") {
                robot.controlMode = "Robot Centric";
                telemetry.addData("Control Mode", "Robot Centric Controls");
            }
        }

        if (gamepad1.options && robot.controlMode == "Field Centric") {
            imu.resetYaw();
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


    private void doTelemetryStuff() {
        // This little section updates the driver hub on the runtime and the motor powers.
        // It's mostly used for troubleshooting.
        telemetry.addData("Status", "Run Time: " + runtime.toString());

        robot.tellMotorOutput(); // Updates telemetry too
    }

    //noahguy was here also
    private void intake1(double fwdSPEED, double revSPEED, double master){
        robot.intakeTune = master;
        if (gamepad2.a || gamepad2.dpad_up) {
            robot.intakeSpeed = fwdSPEED * robot.intakeTune;
            currentStep = INPUT;
        } else if (gamepad2.b) {
            robot.intakeSpeed = -revSPEED * robot.intakeTune;
            currentStep = INPUT;
        } if (gamepad2.aWasReleased() || gamepad2.bWasReleased() || gamepad2.dpadUpWasReleased()) {
            if (currentStep == INPUT) {
                robot.intakeSpeed = 0;
            }
        }
        robot.intakeMotor.setPower(robot.intakeSpeed);
    }

    private void intakeservoforward() {
        robot.intake2.setPower(0);
        if (gamepad2.dpad_up) {
            robot.intake2.setPower(-1);

        } else if (gamepad2.dpad_down) {
            robot.intake2.setPower(1);
        }
    }

    private void autolaunch(){
        if (gamepad2.right_bumper) {
            robot.setupLaunchers();
            if (robot.upToSpeed(60)){
                robot.intake2.setPower(-1);
                robot.intake3.setPower(1);
                robot.intakeMotor.setPower(1);
            }
        }
    }

    private void intake3(double fwdSPEED, double revSPEED, double master){
        if (currentStep == INPUT) {
            robot.intake3.setPower(0);
        }
        if (gamepad2.dpad_down) {
            robot.intake3.setPower(-revSPEED);
            currentStep = INPUT;
        }  else if (gamepad2.dpad_up) {
            robot.intake3.setPower(fwdSPEED);
            currentStep = INPUT;
        }
        robot.intake3.setPower(robot.intake3.getPower() * master);
    }


    private void setLaunchPower(double input, double velocity) {;
        robot.launchLeft.setVelocity(-velocity);
        robot.launchRight.setVelocity(-velocity);
        if (gamepad2.right_stick_button) {
            telemetry.addLine("noahguywashere");
            telemetry.addLine("DevenWasHere");
            telemetry.addLine("MatthewWasHere");
        }
    }



        boolean leftTrigger2Down() {
            return (gamepad2.left_trigger > robot.triggerDeadzone);
        }
        boolean leftTrigger1Down() {
            return (gamepad1.left_trigger > robot.triggerDeadzone);
        }
        boolean rightTrigger2Down() {
            return (gamepad2.right_trigger > robot.triggerDeadzone);
        }
        boolean rightTrigger1Down() {
            return (gamepad1.right_trigger > robot.triggerDeadzone);
        }

    private void launch(double pwrtuning){
        if (rightTrigger2Down() || gamepad2.right_bumper) {
            robot.launchTune = robot.limelightAdjustedSpeed;
            setLaunchPower(gamepad2.right_trigger, robot.limelightAdjustedSpeed);
            currentStep = INPUT;
        } else if (leftTrigger2Down()) {
            robot.launchTune = pwrtuning * 2000;
            setLaunchPower(gamepad2.left_trigger, pwrtuning * 2000);
            currentStep = INPUT;
        } else if (currentStep == INPUT){
            robot.launchTune = 0;
            setLaunchPower(0, 0);
        }
        //autoLaunch(robot.limelightAdjustedSpeed, pwrtuning);
        autolaunch();
        override();
    }

    protected enum AutoLaunchSteps{
        INPUT, ON, GET_BALL_1, CHARGE_LAUNCH_2, GET_BALL_2, CHARGE_LAUNCH_3, GET_BALL_3, END
    }
    private AutoLaunchSteps currentStep = INPUT;

    private double time;
//TODO make it not broken
    private void autoLaunch(double pwrLow, double pwrHigh){
        telemetry.addLine(String.valueOf(currentStep));
        switch (currentStep){
            case INPUT:
                if (gamepad2.dpadLeftWasPressed()) {
                    robot.launchTune = pwrLow;
                    currentStep = ON;
                } //else if (gamepad2.dpadRightWasPressed()) {
                    //robot.launchTune = pwrHigh * 2000;
                    //currentStep = ON;
                //}
                break;
            case ON:
                setLaunchPower(1, robot.launchTune);
                time = runtime.milliseconds();
                currentStep = GET_BALL_1;
                break;
            case GET_BALL_1:
                if (runtime.milliseconds() >= time + 1200){
                    robot.intake3.setPower(1);
                    robot.intakeMotor.setPower(-1);
                    time = runtime.milliseconds();
                    currentStep = CHARGE_LAUNCH_2;
                }
                break;
            case CHARGE_LAUNCH_2:
                if (runtime.milliseconds() >= time + 500) {
                    robot.intake3.setPower(-0.4);
                    time = runtime.milliseconds();
                    currentStep = GET_BALL_2;
                }
                break;
            case GET_BALL_2:
                if (runtime.milliseconds() >= time + 300){
                    robot.intake3.setPower(1);
                    time = runtime.milliseconds();
                    currentStep = CHARGE_LAUNCH_3;
                }
                break;
            case CHARGE_LAUNCH_3:
                if (runtime.milliseconds() >= time + 1000){
                    robot.intake3.setPower(-0.4);
                    time = runtime.milliseconds();
                    currentStep = GET_BALL_3;
                }
                break;
            case GET_BALL_3:
                if (runtime.milliseconds() >= time + 300){
                    robot.intake3.setPower(1);
                    time = runtime.milliseconds();
                    currentStep = END;
                }
                break;
            case END:
                if (runtime.milliseconds() >= time + 100){
                    robot.intake3.setPower(0);
                    robot.intakeMotor.setPower(0);
                    setLaunchPower(0, 0);
                    time = runtime.milliseconds();
                    currentStep = INPUT;
                }
                break;
        }
    }


    private void override() {
        if (gamepad2.right_trigger > 0.5 & gamepad2.left_trigger > 0.5 & gamepad2.right_stick_button) {
            robot.launchRight.setVelocity(-6000);
            robot.launchLeft.setVelocity(-6000);
            telemetry.addLine("! CAUTION, LAUNCH OVERRIDE ACTIVE !");
            telemetry.addLine("! CAUTION, LAUNCH OVERRIDE ACTIVE !");
            telemetry.addLine("! CAUTION, LAUNCH OVERRIDE ACTIVE !");
        }
    }

    private boolean anyButtonPressed1(){
        if (gamepad1.aWasPressed() || gamepad1.bWasPressed() || gamepad1.xWasPressed() || gamepad1.yWasPressed()){
            return true;
        } else if (gamepad1.rightBumperWasPressed() || gamepad1.leftBumperWasPressed() || gamepad1.rightTriggerWasPressed() || gamepad1.leftTriggerWasPressed()) {
            return true;
        } else if (gamepad1.dpadUpWasPressed() || gamepad1.dpadDownWasPressed() || gamepad1.dpadLeftWasPressed() || gamepad1.dpadRightWasPressed()){
            return true;
        } else if (gamepad1.startWasPressed() || gamepad1.touchpadWasPressed() || gamepad1.leftStickButtonWasPressed() || gamepad1.rightStickButtonWasPressed()){
            return true;
        } else if (gamepad1.left_stick_x > 0.2 || gamepad1.left_stick_x < -0.2 || gamepad1.left_stick_y > 0.2 || gamepad1.left_stick_y < -0.2){
            return true;
        } else if (gamepad1.right_stick_x > 0.2 || gamepad1.right_stick_x < -0.2 || gamepad1.right_stick_y > 0.2 || gamepad1.right_stick_y < -0.2){
            return true;
        } else {
            return false;
        }
    }


    private float getLargestAbsVal( float[] values){
        // This function does some math!
        float max = 0;
        for (float val : values) {
            if (Math.abs(val) > max) {
                max = Math.abs(val);
            }
        }
        return max;
    }

}


