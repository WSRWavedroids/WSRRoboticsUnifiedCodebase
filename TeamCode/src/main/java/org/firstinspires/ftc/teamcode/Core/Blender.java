package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Blender.slotNames.*;

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
    public int tickOffset = 0;
    public boolean forceBlenderLock = false;


//TODO if possible link variable values in enum
    enum slotNames {INTAKE, LAUNCHER, STORAGE}
    public slotNames rotationSlot = INTAKE;
    public void rotateToSlot(slotNames slot) {
        switch (rotationSlot) {
            case INTAKE:
                break;
            case LAUNCHER:
                break;
            case STORAGE:
                break;
        }
    }

    public void initBlender() {
        //rotate until found in place by magnets
        tickOffset = robot.sorterMotor.getCurrentPosition();
    }

    //one slot movement is 2730.666666 ticks, or one third of a rotation, rounded to 2731 to make ints happy
    public void rotateClockwise() {
        int currentPos = robot.sorterMotor.getCurrentPosition() - tickOffset;
        robot.sorterMotor.setTargetPosition(currentPos + 2731 + tickOffset);
    }
    public void rotateCounterClockwise() {
        int currentPos = robot.sorterMotor.getCurrentPosition() - tickOffset;
        robot.sorterMotor.setTargetPosition(currentPos - 2731 + tickOffset);
    }


    public void lockBlender(boolean true_false) {
        forceBlenderLock = true_false;
    }

    public void checkSlotPositions() {
        //do stuff
    }
}
