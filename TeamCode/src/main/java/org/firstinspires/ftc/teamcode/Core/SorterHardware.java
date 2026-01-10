package org.firstinspires.ftc.teamcode.Core;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.EMPTY;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.GREEN;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.PURPLE;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.BlenderSteps.*;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.FeederState.*;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.*;
import static org.firstinspires.ftc.teamcode.Core.Robot.OpenClosed.*;
import static org.firstinspires.ftc.teamcode.Core.ezPID.movementType.*;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Configurable
public class SorterHardware {

    private final Robot robot;
    public ezPID blenderPID;
    private LauncherHardware launcher;
    public DcMotorEx motor;
    public Servo flicky;
    public CRServo feedServo;
    public AnalogInput flickyFeedback;
    public enum PositionState {
        FIRE(1), LOAD(0), STORE(2), SWITCH(-1);
        public final int offset;
        PositionState(int offset) {
            this.offset = offset;
        }
    }

    public int[] positions;
    public static final int ticksPerRotation = 8192;
    public int currentTickCount;
    public static Double tickTolerance = 100.0;
    public boolean legalToSpin = false;

    public double flickyDownPosition = 0.5;
    public double flickyUpPosition = 1;

    public ElapsedTime cooldownTimer = new ElapsedTime();
    public boolean onCooldown = false;
    private ElapsedTime pidfTime = new ElapsedTime();

    public static Double kneecap = .20;
    public static double kp = 0.0005;
    public static double ki = 0.0009;
    public static double kd = 0.0000065;
    public static double kf = 0.0;
    double lastError = 0;
    double integralSum = 0;

    public final static double feederIntakeSpeed = 1;
    public final static double feederRotateSpeed = 0.75;
    public final static double passiveFeederSpeed = 0.5;

    public double reference;

    public SorterHardware(Robot robot) {
        this.robot = robot;
        this.motor = robot.sorterMotor;
        this.flicky = robot.flicky;
        this.flickyFeedback = robot.flickyFeedback;
        this.launcher = robot.launcher;
        this.feedServo = robot.feedServo;



        positions = new int[3];
        positions[0] = 0;
        positions[1] = ticksPerRotation / 3;
        positions[2] = 2 * ticksPerRotation / 3;

        blenderPID = new ezPID(motor, ticksPerRotation, kp, ki, kd, kf, kneecap, tickTolerance, POSITION);
        //reference = 0;

    }

    private boolean tryToMove = false;
    public boolean doneMoving = false;

    enum BlenderSteps {
        READY_FOR_COMMANDS,
        STALLING_UNTIL_SAFE_OR_NEEDED, CHECK_IF_SAFE, MOVING, RESET,
        CALIBRATE, CALIBRATING
    }
    private BlenderSteps currentBlenderStep = READY_FOR_COMMANDS;

    public enum FeederState {PASSIVE, ROTATE, INTAKE, OUTTAKE}
    private FeederState currentFeederState = PASSIVE;
    private int ensureBlenderPosition = 0;

    public void updateSorterHardware() {
        robot.telemetry.addLine("Untested sorter hardware, I choose you!");
        robot.telemetry.addData("Blender step", currentBlenderStep);
        switch (currentBlenderStep) {
            case READY_FOR_COMMANDS:
                if (tryToMove || !positionedCheck()) {
                    tryToMove = false;
                    doneMoving = false;
                    nextStep(STALLING_UNTIL_SAFE_OR_NEEDED);
                }
                break;
            case STALLING_UNTIL_SAFE_OR_NEEDED:
                if (legalToSpin && !robot.launcher.isInFireSequence()) {
                    nextStep(MOVING);
                }
                if (this.positionedCheck()) {
                    nextStep(RESET);
                }
                break;
            case CHECK_IF_SAFE:
                break;
            case MOVING:
                setFeeders(ROTATE);
                if (this.positionedCheck()) {
                    ensureBlenderPosition += 1;
                }
                if (ensureBlenderPosition >= 5) {
                    nextStep(RESET);
                }
                break;
            case RESET:
                ensureBlenderPosition = 0;
                doneMoving = true;
                setFeeders(PASSIVE);
                nextStep(READY_FOR_COMMANDS);
                break;
            case CALIBRATE:
                tryToMove = false;
                doneMoving = false;
                nextStep(CALIBRATING);
                break;
            case CALIBRATING:
                motor.setPower(0.10);
                if(robot.magsense.isPressed()) {
                    motor.setPower(0);
                    resetSorterEncoder();
                    nextStep(RESET);
                }
                break;
        }
        if (!isCalibrating()) {
            blenderPID.changeBehaviorValues(kp, ki, kd, kf, kneecap);
            blenderPID.runCalledPID(reference);
        }


        switch (currentFeederState) {
            case INTAKE:
                runFeeders(feederIntakeSpeed);
                break;
            case ROTATE:
                runFeeders(feederRotateSpeed);
                break;
            case OUTTAKE:
                runFeeders(-1);
                break;
            case PASSIVE:
                runFeeders(passiveFeederSpeed);
                break;
        }

        robot.panelsTelemetry.addData("Reference", reference);
        robot.panelsTelemetry.addData("Blender Position", motor.getCurrentPosition());
    }
    private void nextStep(BlenderSteps nextStep) {
        currentBlenderStep = nextStep;
    }

    public boolean doneMoving() {
        return doneMoving;
    }

    public void prepareNewMovement(int targetTickPose) {
        this.prepareNewMovement(motor.getCurrentPosition(), targetTickPose);
    }

    public void prepareNewMovement(int currentTickPose, int targetTickPose) {
        //lastSafePosition = currentTickPose;
        reference = (findFastestRotationInTicks(currentTickPose, targetTickPose));
        tryToMove = true;
    }

    public boolean positionedCheck() {
        //int currentMotorPosition = motor.getCurrentPosition();

        return blenderPID.withinTolerance; //currentMotorPosition > reference - tickTolerance && currentMotorPosition < reference + tickTolerance;
    }


    public void setFeeders(FeederState newState) {
        currentFeederState = newState;
    }

    public void calibrate() {
        nextStep(CALIBRATE);
    }

    public boolean isCalibrating() {
        return currentBlenderStep == CALIBRATE || currentBlenderStep == CALIBRATING;
    }

    public void runPIDMotorStuffLol()
    {
        // obtain the encoder position
        double encoderPosition = motor.getCurrentPosition();
        // calculate the error
        double error = reference - encoderPosition;

        double derivative;
        // rate of change of the error
        if(pidfTime.seconds()!= 0)
        {
            derivative = (error - lastError) / pidfTime.seconds();
        }
        else
        {
            derivative = (error - lastError);
        }


        double feedforward = kf * reference;



        if(pidfTime.seconds() != 0)
        {
            // sum of all error over time
            integralSum = integralSum + (error * pidfTime.seconds());
        }

        double out = kneecap * ((kp * error) + (ki * integralSum) + (kd * derivative) + feedforward);

        motor.setMode(RUN_WITHOUT_ENCODER);
        motor.setPower(out);

        lastError = error;

        // reset the timer for next time
        pidfTime.reset();

    }

    public void runFeeders(double speed)
    {
        feedServo.setPower(speed);
    }

    public int findFastestRotationInTicks(int currentPosition, int targetPosition) {
        int howManyCycles = (int) (currentPosition / ticksPerRotation);

        int[] slotSpaces = new int[3];
        slotSpaces[0] =  (targetPosition + (howManyCycles - 1) * ticksPerRotation);
        slotSpaces[1] =  (targetPosition + howManyCycles * ticksPerRotation);
        slotSpaces[2] =  (targetPosition + (howManyCycles + 1) * ticksPerRotation);

        int bestPosition = slotSpaces[0];
        int smallestDistance = Math.abs(slotSpaces[0] - currentPosition);

        for (int potentialPosition : slotSpaces) {
            int distance = Math.abs(potentialPosition - currentPosition);
            if (distance < smallestDistance) {
                smallestDistance = distance;
                bestPosition = potentialPosition;
            }
        }

        return makeSureFastestRotationIsOK(bestPosition);
    }

    private int makeSureFastestRotationIsOK(int position) {
        // Convert the input position to rotations
        double rotation = (double) position / ticksPerRotation;
        // Round the rotations to the nearest third
        rotation = Math.round(rotation * 3) / 3.0;
        // Turn it back into ticks
        return (int) (rotation * ticksPerRotation);
    }

    public void flick() {
        flicky.setPosition(flickyUpPosition);
    }
    public void resetFlicky() {
        flicky.setPosition(flickyDownPosition);
    }

    public boolean fireSafeCheck()
    {
        //if not on servo timeout and there and open, fire
        return positionedCheck();
    }

    public void resetSorterEncoder()
    {
        motor.setMode(STOP_AND_RESET_ENCODER);
        motor.setMode(RUN_WITHOUT_ENCODER);
    }

    /**
     * Checks to see if the flicky servo is in its target position.
     * @return In position or not
     */
    public boolean flickyInPosition() {
        final double maxVoltage = 6.0;
        final double tolerance = 0.05;

        double target = flicky.getPosition();
        double voltage = flickyFeedback.getVoltage();
        double position = voltage / maxVoltage;
        return position > target - tolerance && position < target + tolerance;
    }
}
