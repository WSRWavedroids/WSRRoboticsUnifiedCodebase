package org.firstinspires.ftc.teamcode.Core;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_TO_POSITION;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.*;
import static org.firstinspires.ftc.teamcode.Core.LauncherHardware.LauncherMode.IF_SAFE_NOW;
import static org.firstinspires.ftc.teamcode.Core.LauncherHardware.LauncherMode.WAIT_FOREVER;
import static org.firstinspires.ftc.teamcode.Core.LauncherHardware.LauncherMode.WAIT_FOR_TIME;
import static org.firstinspires.ftc.teamcode.Core.LauncherHardware.LauncherSteps.*;
import static org.firstinspires.ftc.teamcode.Core.Robot.allianceSides.BLUE;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.*;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@Configurable
public class LauncherHardware {

    private Robot robot;
    public DcMotorEx motor1, motor2;
    private TurretLogic turret;
    private PIDMotorGroup launcherMotors;

    public ezPID launcherPID;




    public LauncherHardware(Robot robotFile) {
        robot = robotFile;
        motor1 = robot.launcherMotorOne;
        motor2 = robot.launcherMotorTwo;
        launcherMotors = new PIDMotorGroup(2, motor1, motor2);
        launcherMotors.setDirections(-1, -1);
        launcherPID = new ezPID(launcherMotors, 28, p, i, d, f, 1, toleranceRange, ezPID.movementType.SPEED);
        turret = robot.turret;
        // motor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(P, I, D, F));
        motor1.setDirection(REVERSE);
        waitingToFire = false;
    }

    public static double launcherCooldownDuration = 0.3;
    public static double flickTime = 0.20; //TODO Optimize

    public boolean waitingToFire = false;
    boolean lockControls = false;
    boolean stopMotorAfter;
    boolean firing = false;
    boolean activeFiring = false;

    public static final int ticksPerRevolution = 28;
    public static final int revolutionsPerSecond = 100;
    public static double toleranceRange = 50;

    public static double velocityTarget;
    public double percentSpeed;
    public int steadiness = 0;
    public final int steadinessThreshold = 3;

    public boolean onCooldown = false;
    public static boolean manualTuneMode;

    LauncherMode mode;
    private double waitTime;
    private ElapsedTime waitForSafeTimer = new ElapsedTime();

    public static double p = 100;
    public static double i = 7.5;
    public static double d = 0;
    public static double f = 0;

    public enum LauncherSteps {
        READY_FOR_COMMANDS,
        STALLING_UNTIL_SAFE, CHECK_IF_SAFE, WAIT_FOR_TIME_FOR_SAFE,
        REV_MOTOR, STALL_WHILE_MOTOR_REVVING, FLICK, UNFLICK, LAUNCHING, RESET
    }
    public enum LauncherMode {
        WAIT_FOREVER, WAIT_FOR_TIME, IF_SAFE_NOW
    }
    LauncherSteps currentLauncherStep = READY_FOR_COMMANDS;

    public LauncherSteps getCurrentLauncherStep() {
        return currentLauncherStep;
    }

    private void nextStep(LauncherSteps nextStep) {
        currentLauncherStep = nextStep;
    }
    private ElapsedTime cooldownTimer = new ElapsedTime();

    public void updateLauncherHardware() {
        robot.panelsTelemetry.addData("Launcher Motor 1 Velocity", motor1.getVelocity());
        robot.panelsTelemetry.addData("Launcher Motor 2 Velocity", motor2.getVelocity());
        robot.panelsTelemetry.addData("Target Launcher Velocity", velocityTarget);
        robot.panelsTelemetry.addData("LL Distance", robot.targetTag.distanceZ);

        switch (currentLauncherStep) {
            case READY_FOR_COMMANDS:
                firing = false;
                if (waitingToFire) {
                    waitingToFire = false;
                    firing = true;
                    switch (mode) {
                        case IF_SAFE_NOW:
                            nextStep(CHECK_IF_SAFE);
                            break;
                        case WAIT_FOREVER:
                            nextStep(STALLING_UNTIL_SAFE);
                            break;
                        case WAIT_FOR_TIME:
                            nextStep(WAIT_FOR_TIME_FOR_SAFE);
                            break;
                    }
                }
                break;
            case STALLING_UNTIL_SAFE:
                if (robot.sorterHardware.fireSafeCheck()) {
                    nextStep(REV_MOTOR);
                }
                break;
            case WAIT_FOR_TIME_FOR_SAFE:
                if (robot.sorterHardware.fireSafeCheck()) {
                    // All good
                    nextStep(REV_MOTOR);
                } else if (waitForSafeTimer.seconds() >= waitTime) {
                    // Command timed out
                    nextStep(READY_FOR_COMMANDS);
                }
                break;
            case CHECK_IF_SAFE:
                if (robot.sorterHardware.fireSafeCheck()) {
                    nextStep(REV_MOTOR);
                } else {
                    nextStep(READY_FOR_COMMANDS);
                }
                break;
            case REV_MOTOR:
                setPerfectLauncherVelocity();
                cooldownTimer.reset();
                nextStep(STALL_WHILE_MOTOR_REVVING);
                break;
            case STALL_WHILE_MOTOR_REVVING:
                setPerfectLauncherVelocity();
                if (motorSpeedCheck(velocityTarget) && motorSteady() || cooldownTimer.seconds() >= 5) {
                    nextStep(FLICK);
                }
                break;
            case FLICK:
                lockControls = true;
                activeFiring = true;
                onCooldown = true;
                robot.sorterHardware.flick();
                cooldownTimer.reset();

                nextStep(UNFLICK);
                break;
            case UNFLICK:
                if(/*robot.sorterHardware.flickyInPosition() ||*/ cooldownTimer.seconds() >= flickTime) {
                    robot.sorterHardware.resetFlicky();
                    nextStep(LAUNCHING);
                }
                break;
            case LAUNCHING:
                if (stopMotorAfter && cooldownTimer.seconds() >= launcherCooldownDuration && cooldownTimer.seconds() >= flickTime * 2) {
                    nextStep(RESET);
                }
                else if (/*robot.sorterHardware.flickyInPosition() ||*/ cooldownTimer.seconds() >= flickTime * 2) {
                    nextStep(RESET);
                }
                break;
            case RESET:
                // Stop the motor if requested
                if (stopMotorAfter) setLauncherVelocity(0);

                // Set the slot to empty now that we fired its contents
                robot.sorterLogic.findCurrentSlotInPosition(FIRE).setOccupied(EMPTY);

                // Update the booleans
                lockControls = false;
                activeFiring = false;
                onCooldown = false;
                firing = false;

                // All done, ready for the next one
                nextStep(READY_FOR_COMMANDS);
                break;
        }

        if (motorSpeedCheck()) {
            steadiness += 1;
        } else {
            steadiness = 0;
        }
        launcherPID.changeBehaviorValues(p, i, d, f, 1);
    }

    public boolean doneFiring() {
        return !firing;
    }
    public boolean isInFireSequence() {
        return firing;
    }
    public boolean isActivelyFiring() {
        return activeFiring;
    }

    public void fireNowIfSafe(double speedTarget, boolean useSpeedTarget, boolean stopMotorAfter){
        readyFire(speedTarget, useSpeedTarget, stopMotorAfter);
        mode = IF_SAFE_NOW;
    }
    public void fireWithinTimeIfSafe(double speedTarget, boolean useSpeedTarget, boolean stopMotorAfter, double waitTime){
        readyFire(speedTarget, useSpeedTarget, stopMotorAfter);
        this.waitTime = waitTime;
        mode = WAIT_FOR_TIME;
        waitForSafeTimer.reset();
    }

    public void readyFire(double speedTarget, boolean useSpeedTarget) {
        if (lockControls) return;

        if (useSpeedTarget) percentSpeed = speedTarget;
        else percentSpeed = 0.5;

        waitingToFire = true;
        stopMotorAfter = false;
        mode = WAIT_FOREVER;
    }

    public void readyFire() {
        this.readyFire(0, false, false);
    }
    public void readyFire(double speedTarget, boolean useSpeedTarget, boolean stopMotorAfter) {
        readyFire(speedTarget, useSpeedTarget);
        this.stopMotorAfter = stopMotorAfter;
    }

    @Deprecated
    public void setLauncherSpeed(double targetSpeed) {
        velocityTarget = ticksPerRevolution * revolutionsPerSecond * targetSpeed;
        launcherPID.runCalledPID(velocityTarget);
        //turret.launcherController.runCalledPID(targetspeed);
    }

    public void setLauncherVelocity(double targetVelocity) {
        if (!manualTuneMode) {
            velocityTarget = targetVelocity;
        }
        launcherPID.runCalledPID(velocityTarget);
    }
    public void setPerfectLauncherVelocity() {
        setLauncherVelocity(findBestMotorVelocity(robot.targetTag.distanceZ));
    }

    public boolean motorSpeedCheck(double speedTarget) {
        /*if (atMaxSpeed()) {
            return true;
        }*/
        double realVelocity = motor1.getVelocity();
        return (realVelocity >= (speedTarget - toleranceRange)) && (realVelocity <= (speedTarget + toleranceRange));
    }
    public boolean motorSpeedCheck() {
        return motorSpeedCheck(velocityTarget);
    }

    //TODO retune this
    public boolean atMaxSpeed() {
        double voltageMax = -138.3958 * robot.voltageSensor.getVoltage() - 836.5097;
        return motor1.getVelocity() >= (voltageMax - toleranceRange);
    }

    public boolean motorSteady() {
        return steadiness >= steadinessThreshold;
    }

    public double findBestMotorVelocity(double input) {

        if(!robot.targetTag.currentlyDetected)
        {
            double targetX;
            double targetY;

            if(robot.alliance.equals(BLUE)) {
                targetX = 12;
                targetY = 132;
            }
            else
            {
                targetX = 132;
                targetY = 132;
            }

            input = Math.sqrt(Math.pow(robot.turretPosition.x - targetX, 2) + Math.pow(robot.turretPosition.y - targetY, 2));
            input /= 39.37; //convert to meters

        }
        return (43.75095 * Math.pow(input, 2)) + (-73.54794 * input) + 1297.48932;

    }
}
