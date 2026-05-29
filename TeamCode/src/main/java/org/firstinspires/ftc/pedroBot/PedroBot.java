package org.firstinspires.ftc.pedroBot;

public class PedroBot{

    PathPlan pathPlan;
    public PathPlan calculatePath(PathPlan input) {
        // calculate straight line to goal
        pathPlan.magnitudeToOffsetVectored = pathPlan.length - Math.abs(pathPlan.robotangle);
        pathPlan.distanceToOffset = Math.sqrt(Math.pow(pathPlan.magnitudeToOffsetVectored *Math.cos(pathPlan.robotangle)-pathPlan.goal.getX(), 2) + Math.pow(pathPlan.magnitudeToOffsetVectored *Math.sin(pathPlan.robotangle)-pathPlan.goal.getY(), 2));

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
