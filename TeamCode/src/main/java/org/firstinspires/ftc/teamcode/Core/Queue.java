package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Blender.slotNames.CURRENT;

import java.util.ArrayList;

public class Queue {
    Robot robot;
    public Queue(Robot robot) {
        this.robot = robot;
    }
    public enum ballColor{ // Consider putting this in ArtifactLocator. Also it's BallColor -- Michael
        PURPLE, GREEN
    }
            ArrayList<ballColor> queue = new ArrayList<>(); // Indentation please -- Michael

    // Consider consolidating this into one addToQueue(BallColor color) function
    public void addGreenToQueue(/*ballColor[] args I think this is part of the System thing*/) {
        if (queue.size() < 3) {
            queue.add(queue.size() + 1, ballColor.GREEN); //TODO check if Indexing works
            // queue.add() will add it to the end of the list if you don't specify an index at all.
            // Do that. -- Michael
        }
        else {
            //TODO add something that happens if the queue is fulla nd you try to add more

            // Why bother? You'll implement logic in the update() function that weeds out balls
            // that aren't in the blender anyway, so who cares if the list is too long? -- Michael
        }
    }
    public void addPurpleToQueue(/*ballColor[] args I think this is part of the System thing*/) {
        queue.add(ballColor.GREEN); // Green? -- Michael
        if (queue.size() < 3) {
            queue.add(queue.size() + 1, ballColor.GREEN); //TODO check if Indexing works
        }
        else {
            //TODO add something that happens if the queue is fulla nd you try to add more

            // See above
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
            queue.get(1); // Fix indentation por favor -- Michael
            queue.get(2);
            queue.get(3);

        //system.out.println(queue); This is something google showed me when I was researching array lists
        //It could be better I'm not sure

        // We don't have access to the system console. This is an example of AI not knowing how FTC works -- Michael
        queue.clear();
    }
    public void getNextBall() {
        //TODO add this function
    }
    public void update() {
        //TODO add this function
    }
}
