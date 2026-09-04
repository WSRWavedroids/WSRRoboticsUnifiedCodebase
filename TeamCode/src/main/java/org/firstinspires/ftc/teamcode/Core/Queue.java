package org.firstinspires.ftc.teamcode.Core;

import java.util.ArrayList;

public class Queue {
    Robot robot;
    public Queue(Robot robot) {
        this.robot = robot;
    }
            ArrayList<Robot.BallColor> queue = new ArrayList<>(); // Indentation please -- Michael

    // Consider consolidating this into one addToQueue(BallColor color) function
    public void addToQueue(Robot.BallColor ballColor) {
        if (queue.size() < 3) {
            queue.add(queue.size() + 1, ballColor); //TODO check if Indexing works
            // queue.add() will add it to the end of the list if you don't specify an index at all.
            // Do that. -- Michael
        }
        else {
            //TODO add something that happens if the queue is fulla nd you try to add more

            // Why bother? You'll implement logic in the update() function that weeds out balls
            // that aren't in the blender anyway, so who cares if the list is too long? -- Michael
        }
    }


    public void setQueue(Robot.BallColor ball1, Robot.BallColor ball2, Robot.BallColor ball3) {
        queue.add(1, ball1);
        queue.add(2, ball2);
        queue.add(3, ball3);
    }
    public void clearQueue() {
        queue.clear();
    }
    //TODO Fix from here down
    enum Amount { // This can probably be made an integer. It'll be easier -- Michael
        ONE, TWO, THREE
    }
    public Amount amount = Amount.ONE;
    public void getQueue(Amount amount) {
        switch (amount) {
            case ONE:
                queue.get(1);   // This line of code only finds the ball associated with the number
                                // you give it. It's not doing anything with it. It's functionally
                                // the same as saying "GREEN" and nothing else -- Michael
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
        queue.clear();
    }
    public void getNextBall() {
        //TODO add this function
    }
    public void update() {
        //TODO add this function
    }
}
