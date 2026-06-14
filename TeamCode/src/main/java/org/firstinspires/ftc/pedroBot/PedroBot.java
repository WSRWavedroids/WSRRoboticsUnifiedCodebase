package org.firstinspires.ftc.pedroBot;

import com.pedropathing.geometry.Pose;

import kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest;

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
    //PathPlan pathPlan;
    public Pose goal;
    double robotAngle;
    double tolerance;
    double length;//TODO What is this
    double magnitudeToOffsetVector;
    double distanceToOffset;
    double X2 = Math.cos(robotAngle + Math.asin((tolerance / length))); //TODO Ask Michael what these represent exactly and magical simplification
    double Y2 = Math.sin(robotAngle + Math.asin((tolerance / length)));

    public PathPlan calculatePath(PathPlan input) {
        // calculate straight line to goal
        /*
        What is this math doing? What's offset? What does "vectored" mean?

        -- Michael
         */
        //double magnitudeToOffsetVector = length - Math.abs(robotAngle);
        //distanceToOffset = Math.sqrt(Math.pow(magnitudeToOffsetVector *Math.cos(robotAngle)-goal.getX(), 2) + Math.pow(magnitudeToOffsetVector *Math.sin(robotAngle)-goal.getY(), 2));

        /*
        These two if-else blocks can be simplified into one

        -- Michael
         */

        //Math fixed I think
        distanceToOffset =  (Math.abs((Y2/X2)*goal.getX()- goal.getY()))/(Math.sqrt(Math.pow((Y2/X2) , 2) + 1));

        boolean hit;
        if (distanceToOffset <= tolerance) {
            hit = true;
        }
        else {
            hit = false;
        }

        if (!hit) { // We didn't hit anything!
            return input;
        }
        else {
            PathPlan optionOne = input;
            PathPlan optionTwo = new PathPlan(input);
            // calculate one path
            findTangentPoint(optionOne, tolerance);
            findTangentPoint(optionTwo, -tolerance);

            optionOne = calculatePath(optionOne);
            // calulate the other path
            optionTwo = calculatePath(optionTwo);

            if (optionOne.score >= optionTwo.score) {
                return optionOne;
            } else {
                return optionTwo;
            }
        }
    }
    private void findTangentPoint(PathPlan option, double tolerance) {
        double lengthOne = Math.sqrt((Math.pow(option.getLatestPose().getX() - goal.getX(), 2) + (Math.pow(option.getLatestPose().getY() - goal.getY(), 2))));
        double magnitudeOne = Math.sqrt(Math.pow(length, 2) - Math.pow(tolerance, 2));
        double directionOne = robotAngle + Math.asin((tolerance / length));
        option.points.add(
                new Pose(
                        option.getLatestPose().getX() + (magnitudeOne * Math.sin(directionOne)),
                        option.getLatestPose().getY() + (magnitudeOne * Math.cos(directionOne))
                )
        );
    }


}
