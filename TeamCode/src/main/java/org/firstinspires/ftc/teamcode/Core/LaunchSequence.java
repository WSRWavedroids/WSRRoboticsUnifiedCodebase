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
    public void getNextBall() {
        //TODO add this function
    }
    public void update() {
        //TODO add this function
    }
}
