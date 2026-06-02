package org.firstinspires.ftc.pedroBot;

public class PedroBot {

    /*
    Alright...

    This looks pretty good right now except for one major issue. Your pathPlan object exists
    globally, outside the function. That means that every branch of your recursion tree (you get the
    metaphor) is editing the same PathPlan. You could give the PathPlan class a tree-like structure
    itself, but that seems like a lot of work compared to what you could do. I set up the
    calculatePath() function with a pathPlan as an input and output. Rather than having each node in
    the tree edit a global object, give each node its own PathPlan object (input) and have it modify
    that. That way, the tree-like structure is built in. In other words, pass the data through the
    function inputs, not by storing it outside of the tree.

    I've left comments all around this file and PathPlan.java. You're doing really well, just a
    handful of readability issues here and there that make the logic difficult to parse.

    -- Michael
     */
    PathPlan pathPlan;
    public PathPlan calculatePath(PathPlan input) {
        // calculate straight line to goal
        /*
        What is this math doing? What's offset? What does "vectored" mean?

        -- Michael
         */
        pathPlan.magnitudeToOffsetVectored = pathPlan.length - Math.abs(pathPlan.robotangle);
        pathPlan.distanceToOffset = Math.sqrt(Math.pow(pathPlan.magnitudeToOffsetVectored *Math.cos(pathPlan.robotangle)-pathPlan.goal.getX(), 2) + Math.pow(pathPlan.magnitudeToOffsetVectored *Math.sin(pathPlan.robotangle)-pathPlan.goal.getY(), 2));

        /*
        These two if-else blocks can be simplified into one

        -- Michael
         */
        boolean hit;
        if (pathPlan.distanceToOffset <= pathPlan.offset) {
            hit = true;
        }
        else {
            hit = false;
        }

        if (!hit) { // We didn't hit anything!
            return input;
        }
        else {
            // calculate one path
            pathPlan.length = Math.sqrt((Math.pow(pathPlan.robot.getX() - pathPlan.goal.getX(), 2) + (Math.pow(pathPlan.robot.getY() - pathPlan.goal.getY(), 2))));
            pathPlan.w = Math.sqrt(Math.pow(pathPlan.length, 2) - Math.pow(pathPlan.offset, 2));
            pathPlan.b = pathPlan.robotangle + Math.asin((pathPlan.offset/ pathPlan.length));
            PathPlan optionOne = calculatePath(pathPlan);
            // calulate the other path
            PathPlan optionTwo = calculatePath(pathPlan);

            if (optionOne.score >= optionTwo.score) {
                return optionOne;
            } else {
                return optionTwo;
            }
        }
    }


}
