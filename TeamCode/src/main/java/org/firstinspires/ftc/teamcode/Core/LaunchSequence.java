package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.LaunchSequence.LaunchSequenceSteps.*;

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

    public boolean ballCheck() {
        return robot.artifactLocator.findColor(queue.get(0)) != Blender.SlotNames.NONE;
    }
    enum LaunchSequenceSteps {
        READY, CHECK, LAUNCH
    }
    private LaunchSequenceSteps launchSequenceSteps = READY;
    public void update() {
        switch (launchSequenceSteps) {
            case READY:
                if (queue.isEmpty()) {
                    launchSequenceSteps = LAUNCH;
                }

                break;
            case CHECK:
                if (ballCheck()) {
                    robot.blender.rotateSlotToPosition(robot.artifactLocator.findColor(queue.get(0)), Blender.SlotPositions.LAUNCH);
                    robot.launcher.fireBall();
                } else {
                    queue.remove(0);
                    launchSequenceSteps = READY;
                }
                break;

            case LAUNCH:
                if (!robot.launcher.isLaunching) {
                    launchSequenceSteps = READY;
                    queue.remove(0);
                }
                break;
        }
    }
}
