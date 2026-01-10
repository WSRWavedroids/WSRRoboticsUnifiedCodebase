package org.firstinspires.ftc.teamcode.Core;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.*;
import static org.firstinspires.ftc.teamcode.Core.LauncherHardware.LauncherMode.IF_SAFE_NOW;
import static org.firstinspires.ftc.teamcode.Core.LauncherHardware.LauncherMode.WAIT_FOREVER;
import static org.firstinspires.ftc.teamcode.Core.LauncherHardware.LauncherMode.WAIT_FOR_TIME;
import static org.firstinspires.ftc.teamcode.Core.LauncherHardware.LauncherSteps.*;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.*;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

public class LauncherHardware {

    private Robot robot;
    public DcMotorEx motor;

    private TurretLogic turret;
    private static ezPID launcherPID;


    public LauncherHardware(Robot robotFile) {
        robot = robotFile;
        motor = robot.launcherMotor;
        turret = robot.turret;
        // motor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(P, I, D, F));
        motor.setDirection(REVERSE);
        waitingToFire = false;
    }

    public static double launcherCooldownDuration = 0.3;
    public static double flickTime = 0.3; //TODO Optimize

    public boolean waitingToFire = false;
    boolean lockControls = false;
    boolean stopMotorAfter;
    boolean firing = false;
    boolean activeFiring = false;

    public static final int ticksPerRevolution = 28;
    public static final int revolutionsPerSecond = 100;
    public static final double toleranceRange = 100;

    public double velocityTarget;
    public double percentSpeed;

    public boolean onCooldown = false;

    LauncherMode mode;
    private double waitTime;
    private ElapsedTime waitForSafeTimer = new ElapsedTime();

    enum LauncherSteps {
        READY_FOR_COMMANDS,
        STALLING_UNTIL_SAFE, CHECK_IF_SAFE, WAIT_FOR_TIME_FOR_SAFE,
        REV_MOTOR, STALL_WHILE_MOTOR_REVVING, FLICK, UNFLICK, LAUNCHING, RESET
    }
    public enum LauncherMode {
        WAIT_FOREVER, WAIT_FOR_TIME, IF_SAFE_NOW
    }
    LauncherSteps currentLauncherStep = READY_FOR_COMMANDS;
    private void nextStep(LauncherSteps nextStep) {
        currentLauncherStep = nextStep;
    }
    private ElapsedTime cooldownTimer = new ElapsedTime();

    public void updateLauncherHardware() {
        robot.telemetry.addData("Launcher step", currentLauncherStep);
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
                setLauncherSpeed(percentSpeed);
                cooldownTimer.reset();
                nextStep(STALL_WHILE_MOTOR_REVVING);
                break;
            case STALL_WHILE_MOTOR_REVVING:
                if (motorSpeedCheck(velocityTarget) || cooldownTimer.seconds() >= 5) {
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
                if(robot.sorterHardware.flickyInPosition() || cooldownTimer.seconds() >= flickTime) {
                    robot.sorterHardware.resetFlicky();
                    nextStep(LAUNCHING);
                }
                break;
            case LAUNCHING:
                if (stopMotorAfter && cooldownTimer.seconds() >= launcherCooldownDuration && cooldownTimer.seconds() >= flickTime * 2) {
                    nextStep(RESET);
                }
                else if (robot.sorterHardware.flickyInPosition() || cooldownTimer.seconds() >= flickTime * 2) {
                    nextStep(RESET);
                }
                break;
            case RESET:
                // Stop the motor if requested
                if (stopMotorAfter) setLauncherSpeed(0);

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
        mode = WAIT_FOREVER;
    }

    public void readyFire() {
        this.readyFire(0, false, true);
    }
    public void readyFire(double speedTarget, boolean useSpeedTarget, boolean stopMotorAfter) {
        this.stopMotorAfter = stopMotorAfter;
        readyFire(speedTarget, useSpeedTarget);
    }

    public void setLauncherSpeed(double targetSpeed) {
        velocityTarget = ticksPerRevolution * revolutionsPerSecond * targetSpeed;
        robot.telemetry.addLine("Setting Launcher");
        motor.setVelocity(velocityTarget);
        //turret.launcherController.runCalledPID(targetspeed);
    }

    public boolean motorSpeedCheck(double speedTarget) {
        if (atMaxSpeed()) {
            return true;
        }
        double realVelocity = motor.getVelocity();
        return (realVelocity > (speedTarget - toleranceRange)) && (realVelocity < (speedTarget + toleranceRange));
    }
    public boolean motorSpeedCheck() {
        return motorSpeedCheck(velocityTarget);
    }

    public boolean atMaxSpeed() {
        double voltageMax = -138.3958 * robot.voltageSensor.getVoltage() - 836.5097;
        return motor.getVelocity() > (voltageMax - toleranceRange) &&
                motor.getVelocity() < (voltageMax + toleranceRange);
    }
}
