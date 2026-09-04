package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Blender.SlotNames.*;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Blender {

    private Robot robot;
    public Blender(Robot robot) {
        this.robot = robot;
        this.blenderPID = new ezPID(robot.sorterMotor, 8192, 0.000375, 0.0, 0.000005, 0.0, 1, 25, ezPID.movementType.POSITION);
    }
    private ezPID blenderPID;



    public int launcherSlotColor = 0;
    public int intakeSlotColor = 0;
    public int storageSlotColor = 0;
    public int launcherPositionSlotNumber = 2;
    public int intakePositionSlotNumber = 1;
    public int storagePositionSlotNumber = 3;
    public boolean forceBlenderLock = false;


    enum SlotNames {SLOT_1, SLOT_2, SLOT_3, NONE}
    public SlotNames rotationSlot = SLOT_1;
    public void rotateToSlot(SlotNames slot) {
        switch (rotationSlot) {
            case SLOT_1:
                break;
            case SLOT_2:
                break;
            case SLOT_3:
                break;
        }
    }

    public void initBlender() {
        //rotate until found in place by magnets
        robot.sorterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.sorterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //one slot movement is 2730.666666 ticks, or one third of a rotation, rounded to 2731 to make ints happy
    public void rotateClockwise() {
        int currentPos = robot.sorterMotor.getCurrentPosition();
        robot.sorterMotor.setTargetPosition(currentPos + 2731);
        rotateSlotVariables(1);
    }
    public void rotateCounterClockwise() {
        int currentPos = robot.sorterMotor.getCurrentPosition();
        robot.sorterMotor.setTargetPosition(currentPos - 2731);
        rotateSlotVariables(-1);
    }
    public boolean isBlenderPositioned() {
        return robot.blenderMagnetSensor.isPressed();
    }

    /**
     * cycles position variables by +-1
     * @param direction
     */
    public void rotateSlotVariables(int direction) {
        launcherPositionSlotNumber = clampVariable(launcherPositionSlotNumber + direction);
        intakePositionSlotNumber = clampVariable(intakePositionSlotNumber + direction);
        storagePositionSlotNumber = clampVariable(storagePositionSlotNumber + direction);
    }

    /**
     * used inside rotateSlotVariables function, clamps the input between 1 and 3
     * it also loops them (IMPORTANT), meaning when variable is <1, sets it to 3, when >3, sets it to 1
     *
     * @param input
     * @return
     */
    public int clampVariable(int input) {
        if (input < 1) {
            return 3;
        }
        if (input >3) {
            return 1;
        }
        return input;
    }


    public void lockBlender(boolean true_false) {
        forceBlenderLock = true_false;
    }

    public void checkSlotPositions() {
        //do stuff
    }
}
