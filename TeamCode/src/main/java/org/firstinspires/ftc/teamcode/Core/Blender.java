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


    /*
    public int launcherSlotColor = 0;
    public int intakeSlotColor = 0;
    public int storageSlotColor = 0;
    */
    public int launcherPositionSlot = 2;
    public int intakePositionSlot = 1;
    public int storagePositionSlot = 3;
    public boolean forceBlenderLock = false;

    public enum SlotPositions {
        INTAKE(1), LAUNCH(2), STORE(3);
        public final int positionNumber;
        SlotPositions(int positionNumber) {
            this.positionNumber = positionNumber;
        }
    }
    public enum SlotNames {SLOT_1, SLOT_2, SLOT_3, NONE}

    public SlotNames rotationSlot = SLOT_1;

    /**
     * rotates a slot to the specified position
     *
     * @param slot
     */
    public void rotateSlotToPosition(SlotNames slot, SlotPositions position) {
        rotationSlot = slot;
        int targetRotation = 0;
        switch (rotationSlot) {
            case NONE:
                break;
            case SLOT_1:
                targetRotation = position.positionNumber - findSlot(1);
                if (targetRotation > 0) {
                    rotateClockwise(targetRotation);
                } else {
                    rotateCounterClockwise(-targetRotation);
                }
                break;
            case SLOT_2:
                targetRotation = position.positionNumber - findSlot(2);
                if (targetRotation > 0) {
                    rotateClockwise(targetRotation);
                } else {
                    rotateCounterClockwise(-targetRotation);
                }
                break;
            case SLOT_3:
                targetRotation = position.positionNumber - findSlot(3);
                if (targetRotation > 0) {
                    rotateClockwise(targetRotation);
                } else {
                    rotateCounterClockwise(-targetRotation);
                }
                break;
        }
    }


    public int findSlot(int slot) {
        if (intakePositionSlot == slot) {
            return 1;
        }
        if (launcherPositionSlot == slot) {
            return 2;
        }
        if (storagePositionSlot == slot) {
            return 3;
        }
        return 0;
    }

    public void initBlender() {
        while (!isBlenderPositioned()) {
            robot.sorterMotor.setPower(0.1);
        }
        robot.sorterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.sorterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    //one slot movement is 2730.666666 ticks, or one third of a rotation, rounded to 2731 to make ints happy

    /**
     * rotates the blender clockwise by the specified amount if it is safe to do so
     * @param amount
     */
    public void rotateClockwise(int amount) {
        if (!forceBlenderLock) {
            int currentPos = robot.sorterMotor.getCurrentPosition();
            robot.sorterMotor.setTargetPosition(currentPos + (2731 * amount));
            for (int i = 0; i < amount; i++) {
                rotateSlotVariables(1);
            }

        }
    }
    /**
     * rotates the blender counterclockwise by the specified amount if it is safe to do so
     * @param amount
     */
    public void rotateCounterClockwise(int amount) {
        if (!forceBlenderLock) {
            int currentPos = robot.sorterMotor.getCurrentPosition();
            robot.sorterMotor.setTargetPosition(currentPos - (2731 * amount));
            for (int i = 0; i < amount; i++) {
                rotateSlotVariables(-1);
            }
        }
    }

    public boolean isBlenderPositioned() {
        return robot.blenderMagnetSensor.isPressed();
    }

    /**
     * cycles position variables by +-1
     *
     * @param direction
     */
    public void rotateSlotVariables(int direction) {
        launcherPositionSlot = clampVariable(launcherPositionSlot + direction);
        intakePositionSlot = clampVariable(intakePositionSlot + direction);
        storagePositionSlot = clampVariable(storagePositionSlot + direction);
    }

    /**
     * used inside rotateSlotVariables function, clamps the input between 1 and 3.
     * It also loops them (IMPORTANT), meaning when variable is <1, sets it to 3, when >3, sets it to 1
     *
     * @param input
     * @return
     */
    public int clampVariable(int input) {
        if (input < 1) {
            return 3;
        }
        if (input > 3) {
            return 1;
        }
        return input;
    }

    /**
     * lock/unlock blender rotation
     *
     * @param true_false
     */
    public void lockBlender(boolean true_false) {
        forceBlenderLock = true_false;
    }
}
