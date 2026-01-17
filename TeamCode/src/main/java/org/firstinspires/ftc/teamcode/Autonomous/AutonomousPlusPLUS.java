package org.firstinspires.ftc.teamcode.Autonomous;
/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import static org.firstinspires.ftc.teamcode.Autonomous.AutonomousPlusPLUS.fireInSequenceStalling.*;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.EMPTY;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.GREEN;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.PURPLE;
import static org.firstinspires.ftc.teamcode.Core.Robot.CardinalDirections.*;
import static android.os.SystemClock.sleep;

import com.bylazar.panels.Panels;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.ArtifactLocator;
import org.firstinspires.ftc.teamcode.Core.Robot;

/**
 * This is the autonomous mode. It moves the robot without us having to touch the controller.
 * Previous programmers really sucked at explaining what any of this meant, so we're trying to do better.
 * This is our third year now of using this file. It's kind of poetic and also adorable.
 */

public class AutonomousPlusPLUS {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    public ElapsedTime stupidTimer = new ElapsedTime();
    public double speed = 0.6;
    public int sleepTime;
    public boolean inMarker;
    public double power;
    public double slidePos;

    //static TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    public Robot robot = null;

    public AutonomousPlusPLUS(Robot robot) {
        this.robot = robot;
        this.robot.panels = Panels.INSTANCE;
    }

    //These are the basic functions for mechnum movement during auto... Don't mess with these unless something is inverted
    // Remember Without ODO pods there will be some inconsistency due to mechnum slippage

    /**
     * Moves the robot in the provided X and Y directions and turns using... some value.
     *
     * Positive moveRight = Right
     * Positive moveForward = Forward
     * Positive turn = Clockwise
     * @param moveRight negative = left, positive = right
     * @param moveForward positive = forwards, negative = backwards
     * @param turn negative = counterclockwise, positive = clockwise
     * @param waitForCompletion Stall until movement complete
     * @param pauseMS Pause in milliseconds
     */
    public void moveXY(int moveRight, int moveForward, int turn, boolean waitForCompletion, long pauseMS) {

        int[] motorTicks = new int[4];

        motorTicks[0] = (-moveForward + moveRight + turn);
        motorTicks[1] = (-moveForward - moveRight - turn);
        motorTicks[2] = (-moveForward - moveRight + turn);
        motorTicks[3] = (-moveForward + moveRight - turn);

        robot.frontLeftDrive.setTargetPosition(-motorTicks[0] + robot.frontLeftDrive.getCurrentPosition());
        robot.frontRightDrive.setTargetPosition(-motorTicks[1] + robot.frontRightDrive.getCurrentPosition());
        robot.backLeftDrive.setTargetPosition(-motorTicks[2] + robot.backLeftDrive.getCurrentPosition());
        robot.backRightDrive.setTargetPosition(-motorTicks[3] + robot.backRightDrive.getCurrentPosition());

        if (waitForCompletion) {
            while (wheelsAreRunning()) {
                robot.tellMotorOutput();
                robot.updateAllDaThings();
            } // And we stall...
        }

        sleep(pauseMS);
    }
    // No turn
    public void moveXY(int moveRight, int moveForward, boolean waitForCompletion, long pauseMS) {
        moveXY(moveRight,moveForward,0, waitForCompletion, pauseMS);
    }
    //No pauseMS
    public void moveXY(int moveRight, int moveForward, int turn, boolean waitForCompletion) {
        moveXY(moveRight,moveForward,turn,waitForCompletion,0);
    }
    // No turn or pauseMS
    public void moveXY(int moveRight, int moveForward, boolean waitForCompletion) {
        moveXY(moveRight,moveForward,0,waitForCompletion,0);
    }

    /**
     * Checks to see if a started movement is completed. If it is, does the cleanup work for it.
     * @return True if completed, otherwise false.
     */
    public boolean checkMovement() {
        if (robot.isWheelsBusy()) {
            return false;
        } else {
            robot.stopAllMotors();
            robot.encoderRunningMode();
            //robot.encoderReset();
            return true;
        }
    }

    /**
     * Starts moving the robot forward without stalling.
     * @param ticks The number of ticks to move forward
     */
    public void moveRobotForward(int ticks) {
        robot.setTargets(FORWARD, ticks); // Inverted... Lol
        robot.positionRunningMode();

        robot.powerSet(speed);
    }

    /**
     * Moves the robot forward and stalls while doing it.
     * @param ticks The number of ticks to move forward
     * @param pause Milliseconds to wait after the movement is completed, in MS
     */
    public void moveRobotForward(int ticks, long pause) {
        moveRobotForward(ticks);
        while (!checkMovement()) {
            robot.tellMotorOutput();
            robot.panelsTelemetry.addData("FRD Position", robot.frontRightDrive.getCurrentPosition());
            robot.updateAllDaThings();
        }

        sleep(pause);

        //robot.encoderReset();
    }

    /**
     * Starts moving the robot backwards. Check to see if it's done with checkMovement().
     * @param ticks The number of ticks to move backwards
     */
    public void moveRobotBackward(int ticks) {
        robot.setTargets(BACKWARD, ticks);
        robot.positionRunningMode();
        robot.powerSet(speed);
    }

    /**
     * Moves the robot backward and stalls while doing it.
     * @param ticks The number of ticks to move backward
     * @param pause Milliseconds to wait after the movement is completed, in MS
     */
    public void moveRobotBackward(int ticks, long pause) {
        moveRobotForward(ticks);

        while (checkMovement()) {
            robot.tellMotorOutput();
        }

        sleep(pause);
    }

    /**
     * Starts strafing the robot left. Check to see if it's done with checkMovement().
     * @param ticks The number of ticks to strafe left
     */
    public void moveRobotLeft(int ticks) {
        robot.setTargets(LEFT, ticks);
        robot.positionRunningMode();
        robot.powerSet(speed);
    }

    /**
     * Strafes the robot left and stalls while doing it.
     * @param ticks The number of ticks to strafe left
     * @param pause Milliseconds to wait after the movement is completed, in MS
     */
    public void moveRobotLeft(int ticks, long pause) {
        moveRobotLeft(ticks);

        while (checkMovement()) {
            robot.tellMotorOutput();
        }

        robot.stopAllMotors();
        robot.encoderRunningMode();
        sleep(pause);
    }

    /**
     * Starts strafing the robot right. Check to see if it's done with checkMovement().
     * @param ticks The number of ticks to strafe right
     */
    public void moveRobotRight(int ticks) {
        robot.setTargets(RIGHT, ticks);
        robot.positionRunningMode();
        robot.powerSet(speed);
    }

    /**
     * Strafes the robot right and stalls while doing it.
     * @param ticks The number of ticks to strafe right
     * @param pause Milliseconds to wait after the movement is completed, in MS
     */
    public void moveRobotRight(int ticks, long pause) {
        moveRobotRight(ticks);

        while (checkMovement()) {
            robot.tellMotorOutput();
        }

        sleep(pause);
    }

    /**
     * Starts turning the robot right. Check to see if it's done with checkMovement().
     * @param ticks The number of ticks to turn right
     */
    public void turnRobotRight(int ticks) {
        robot.setTargets(TURN_RIGHT, ticks);
        robot.positionRunningMode();
        robot.powerSet(speed);
    }

    /**
     * Turns the robot right and stalls while doing it.
     * @param ticks The number of ticks to turn right
     * @param pause Milliseconds to wait after the movement is completed, in MS
     */
    public void turnRobotRight(int ticks, long pause) {
        turnRobotRight(ticks);

        while (checkMovement()) {
            robot.tellMotorOutput();
        }
        sleep(pause);
    }

    /**
     * Starts turning the robot left. Check to see if it's done with checkMovement().
     * @param ticks The number of ticks to turn left
     */
    public void turnRobotLeft(int ticks) {
        robot.setTargets(TURN_LEFT, ticks);
        robot.positionRunningMode();
        robot.powerSet(speed);
    }

    /**
     * Turns the robot left and stalls while doing it.
     * @param ticks The number of ticks to turn left
     * @param pause Milliseconds to wait after the movement is completed, in MS
     */
    public void turnRobotLeft(int ticks, long pause) {
        turnRobotLeft(ticks);

        while (checkMovement()) {
            robot.tellMotorOutput();
        }

        sleep(pause);
    }

    /**
     * Starts moving the robot along the 45/225 degree axis. Check to see if it's done with checkMovement().
     * @param ticks The number of ticks to move. Positive will go forward, negative will go backwards.
     */
    public void moveDiagonalRight(int ticks) {
        robot.setTargets(DIAGONAL_RIGHT, ticks);
        robot.positionRunningMode();
        robot.powerSet(speed);
    }

    /**
     * Turns the robot diagonally along the 45/225 degree axis and stalls while doing it.
     * @param ticks The number of ticks to move. Positive will go forward, negative will go backwards.
     * @param pause Milliseconds to wait after the movement is completed, in MS
     */
    public void moveDiagonalRight(int ticks, long pause) {
        moveDiagonalRight(ticks);

        while (checkMovement()) {
            robot.tellMotorOutput();
            robot.panelsTelemetry.addData("FRD Position", robot.frontRightDrive.getCurrentPosition());
            robot.panelsTelemetry.addData("FRD Position", robot.frontRightDrive.getVelocity());
            robot.updateAllDaThings();
        }

        sleep(pause);
    }

    /**
     * Starts moving the robot along the 135/315 degree axis. Check to see if it's done with checkMovement().
     * @param ticks The number of ticks move. Positive will go forward, negative will go backwards.
     */
    public void moveDiagonalLeft(int ticks) {
        robot.setTargets(DIAGONAL_LEFT, ticks);
        robot.positionRunningMode();
        robot.powerSet(speed);
    }

    /**
     * Turns the robot diagonally along the 135/315 degree axis and stalls while doing it.
     * @param ticks The number of ticks to move. Positive will go forward, negative will go backwards.
     * @param pause Milliseconds to wait after the movement is completed, in MS
     */
    public void moveDiagonalLeft(int ticks, long pause) {
        moveDiagonalLeft(ticks);

        while (checkMovement()) {
            robot.tellMotorOutput();
            robot.panelsTelemetry.addData("FRD Position", robot.frontRightDrive.getCurrentPosition());
            robot.panelsTelemetry.addData("FRD Position", robot.frontRightDrive.getVelocity());
            robot.updateAllDaThings();
        }

        sleep(pause);
    }

    public boolean wheelsAreRunning() {
        return !(checkTolerance(robot.frontLeftDrive) &&
                checkTolerance(robot.frontRightDrive) &&
                checkTolerance(robot.backLeftDrive) &&
                checkTolerance(robot.backRightDrive)
        );
    }

    private boolean checkTolerance(DcMotorEx motor) {
        int pos = motor.getCurrentPosition();
        int target = motor.getTargetPosition();
        int tol = motor.getTargetPositionTolerance();
        return pos > target - tol && pos < target + tol;
    }
    public void calibrateDriveTrain(int tolerance, double pValue) {
        robot.frontLeftDrive.setTargetPositionTolerance(tolerance);
        robot.frontRightDrive.setTargetPositionTolerance(tolerance);
        robot.backLeftDrive.setTargetPositionTolerance(tolerance);
        robot.backRightDrive.setTargetPositionTolerance(tolerance);

        robot.frontLeftDrive.setPositionPIDFCoefficients(pValue);
        robot.frontRightDrive.setPositionPIDFCoefficients(pValue);
        robot.backLeftDrive.setPositionPIDFCoefficients(pValue);
        robot.backRightDrive.setPositionPIDFCoefficients(pValue);

    }

    public void prepareNextAction(long pause) {
        sleep(pause);
        //robot.encoderReset();
    }



    public int convertInchesToTicks(int inches){
        int ticks = (int) ((537.6 * inches) / (3.77953 * 3.1415926535));
        return ticks;
    }

    enum fireInSequenceStalling {READY, FIRE, FIRING}
    fireInSequenceStalling fireInSequenceStallingState = READY;
    int fireInSequenceI = 0;
    int fireInSequenceStep = 0;




    boolean firingInSequence;

    int distanceRemaining = 0;
    int Yoinktarget = 0;
    public boolean yoinking;

    public void yoinkify(int ticks) {
        yoinking = true;
        robot.sorterHardware.runAdvancedIntake();
        if(Yoinktarget == 0)
        {
            Yoinktarget = robot.frontRightDrive.getCurrentPosition() + ticks;
        }
        distanceRemaining = Yoinktarget - robot.frontRightDrive.getCurrentPosition();

        if(checkMovement() || robot.sorterLogic.inventory.getTotalCount() == 3)
        {
            //disable intake
            setSpeed(75);
            moveRobotBackward(-(ticks-distanceRemaining) - 150);
            yoinking = false;
        }

    }

    public boolean checkYoink() {return !yoinking;}


    public void fireInSequence(ArtifactLocator.Slot one, ArtifactLocator.Slot two, ArtifactLocator.Slot three) {
        firingInSequence = true;
        switch (fireInSequenceStep) {
            case 0:
                fireInSequenceStep = 1;
                break;
            case 1:
                fireOne(one);
                break;
            case 2:
                fireOne(two);
                break;
            case 3:
                fireOne(three);
                break;
            case 4:
                firingInSequence = false;
                fireInSequenceStep = 0;
                break;
                //Deven Fixed This A Little Bit He Is A Rubber Duck Now!!!!!!!!;)
        }
    }

    public void fireOneArtifact(ArtifactLocator.Slot slot) {
        firingInSequence = true;
        fireOne(slot);
        if (fireInSequenceStep >= 1) {
            firingInSequence = false;
            fireInSequenceStep = 0;
        }
    }

    private void fireOne(ArtifactLocator.Slot slot) {
        switch (fireInSequenceStallingState) {
            case READY:
                robot.sorterHardware.prepareNewMovement(
                        robot.sorterHardware.motor.getCurrentPosition(),
                        slot.getFirePosition());
                fireInSequenceStallingState = FIRE;
                break;
            case FIRE:
                if (robot.sorterHardware.doneMoving()) {
                    robot.launcher.readyFire();
                    slot.contains(EMPTY);
                    fireInSequenceStallingState = FIRING;
                }
                break;
            case FIRING:
                if (robot.launcher.doneFiring()) {
                    fireInSequenceStallingState = READY;
                    fireInSequenceStep += 1;
                }
                break;
        }
    }

    public void fireMatchPattern() {
        switch (robot.pattern) {
            case PPG:
                fireInSequence(
                        robot.sorterLogic.findFirstType(PURPLE),
                        robot.sorterLogic.findFirstType(PURPLE),
                        robot.sorterLogic.findFirstType(GREEN)
                );
                break;
            case PGP:
                fireInSequence(
                        robot.sorterLogic.findFirstType(PURPLE),
                        robot.sorterLogic.findFirstType(GREEN),
                        robot.sorterLogic.findFirstType(PURPLE)
                );
                break;
            case GPP:
                fireInSequence(
                        robot.sorterLogic.findFirstType(GREEN),
                        robot.sorterLogic.findFirstType(PURPLE),
                        robot.sorterLogic.findFirstType(PURPLE)
                );
                break;
        }
    }

    public boolean fireInSequenceComplete() {
        return !firingInSequence; //&& !robot.launcher.onCooldown; I don't think we need this
    }

    public void setSpeed(double newSpeed) {
        speed = newSpeed;
    }
    public void setTolerances(int tolerance) {
        robot.frontLeftDrive.setTargetPositionTolerance(tolerance);
        robot.frontRightDrive.setTargetPositionTolerance(tolerance);
        robot.backLeftDrive.setTargetPositionTolerance(tolerance);
        robot.backRightDrive.setTargetPositionTolerance(tolerance);
    }
}
