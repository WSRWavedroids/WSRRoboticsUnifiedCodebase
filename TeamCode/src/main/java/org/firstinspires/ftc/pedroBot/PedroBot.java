package org.firstinspires.ftc.pedroBot;

public class PedroBot {
    PathPlan placeholder;
    private PathPlan calculatePath(PathPlan input) {
        // draw line directly to the goal
        if (true) { // We didn't hit anything!
            return input;
        }
        else {
            // calculate one path
            PathPlan optionOne = calculatePath(placeholder);
            // calulate the other path
            PathPlan optionTwo = calculatePath(placeholder);

            if (optionOne.score >= optionTwo.score) {
                return optionOne;
            } else {
                return optionTwo;
            }
        }
    }


}
