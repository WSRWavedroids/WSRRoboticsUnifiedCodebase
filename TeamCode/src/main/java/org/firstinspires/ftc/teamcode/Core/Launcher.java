package org.firstinspires.ftc.teamcode.Core;

public class Launcher {
    private Robot robot;

    public Launcher(Robot robot) {
        this.robot = robot;
    }

    private int launcherSpeed = 0;
    public boolean isLaunching = false;
    public boolean launched = false;
    public boolean readyToLaunch = false;
    private boolean flipperIsUp = true;

    //flipperIsUp is set to true initially until it is checked for extra safety
    private enum launcherState {
        STALL,
        CHECK_BLENDER,
        REV_MOTOR,
        FLICKY_UP,
        FLICKY_DOWN

    }

    launcherState currentState = launcherState.STALL;

    public void fireBall() {

    }

    public void update() {


        switch (currentState) {
            case STALL:
                /*if (gamePadButton) {

        }*/

        }

    }
}
