package org.firstinspires.ftc.teamcode.Core;

import java.util.ArrayList;

public class LaunchSequence {
    Robot robot;
    public LaunchSequence(Robot robot) {
        this.robot = robot;
    }
    ArrayList<Robot.BallColor> queue = new ArrayList<>();
    public void addToQueue(Robot.BallColor ballColor) {
            queue.add(ballColor);

    }
    public void clearQueue() {
        queue.clear();
    }
    private boolean ballCheck = false;
    public void ballCheck() {


    }
    enum LaunchSequenceSteps {
        READY, LAUNCH
    }
    private boolean launchStart = false;
    private boolean doneLaunch = false;
    private LaunchSequenceSteps launchSequenceSteps = LaunchSequenceSteps.READY;
    public void launchSequence() {
        switch (launchSequenceSteps) {
            case READY:
                ballCheck();
                if (launchStart) {
                    if (ballCheck) {
                        launchSequenceSteps = LaunchSequenceSteps.LAUNCH;
                    }
                }
                break;

            case LAUNCH:

                if (doneLaunch == true) {
                    launchSequenceSteps = LaunchSequenceSteps.READY;
                }
                break;
        }
    }
    public void update() {
        //TODO add this function
    }
}
