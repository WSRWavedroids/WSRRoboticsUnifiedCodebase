package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Blender.slotNames.CURRENT;

import java.util.ArrayList;

public class Queue {
    Robot robot;
    public Queue(Robot robot) {
        this.robot = robot;
    }
    public enum ballColor{
        PURPLE, GREEN
    }
            ArrayList<ballColor> queue = new ArrayList<>();
    public void addGreenToQueue(/*ballColor[] args I think this is part of the System thing*/) {
        if (queue.size() < 3) {
            queue.add(queue.size() + 1, ballColor.GREEN); //TODO check if Indexing works
        }
        else {
            //TODO add something that happens if the queue is fulla nd you try to add more
        }
    }
    public void addPurpleToQueue(/*ballColor[] args I think this is part of the System thing*/) {
        queue.add(ballColor.GREEN);
        if (queue.size() < 3) {
            queue.add(queue.size() + 1, ballColor.GREEN); //TODO check if Indexing works
        }
        else {
            //TODO add something that happens if the queue is fulla nd you try to add more
        }
    }

    public void setQueue(ballColor ball1, ballColor ball2, ballColor ball3) {
        queue.add(1, ball1);
        queue.add(2, ball2);
        queue.add(3, ball3);
    }
    public void clearQueue() {
        queue.clear();
    }
    //TODO Fix from here down
    enum Amount {
        ONE, TWO, THREE
    }
    public Amount amount = Amount.ONE;
    public void getQueue(Amount amount) {
        switch (amount) {
            case ONE:
                queue.get(1);
                break;
            case TWO:
                queue.get(1);
                queue.get(2);
                break;
            case THREE:
                queue.get(1);
                queue.get(2);
                queue.get(3);
                break;
        }
        queue.clear();
    }
    public void getWholeQueue() {
            queue.get(1);
            queue.get(2);
            queue.get(3);

        //system.out.println(queue); This is something google showed me when I was researching array lists
        //It could be better I'm not sure
        queue.clear();
    }
    public void getNextBall() {
        //TODO add this function
    }
    public void update() {
        //TODO add this function
    }
}
