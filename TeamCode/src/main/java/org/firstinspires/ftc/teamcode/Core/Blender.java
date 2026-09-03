package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Blender.slotNames.*;

public class Blender {

    private Robot robot;
    public Blender(Robot robot) {
        this.robot = robot;
    }



    public int launcherSlotColor = 0;
    public int intakeSlotColor = 0;
    public int storageSlotColor = 0;
    public boolean forceBlenderLock = false;


    enum slotNames {CURRENT, INTAKE, LAUNCHER, STORAGE}
    public slotNames rotationSlot = CURRENT;
    public void rotateToSlot(slotNames slot) {
        switch (rotationSlot) {
            case CURRENT:
                break;
            case INTAKE:
                break;
            case LAUNCHER:
                break;
            case STORAGE:
                break;
        }
    }

    public void lockBlender(boolean trueFalse) {
        forceBlenderLock = trueFalse;
    }

    //possibly unneeded here, may move to other files
    public void checkSlotPositions() {
        //do stuff
    }
}
