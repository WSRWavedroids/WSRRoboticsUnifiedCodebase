package org.firstinspires.ftc.pedroBot;

import com.pedropathing.geometry.Pose;

import java.util.ArrayList;
import java.util.List;

public class PathPlan {

    public PathPlan(Pose goal) {
        double score = 0;
        this.goal = goal;
        points = new ArrayList<>();
        //points.add(robot.currentPose) //TODO connect to existing code
    }

    public PathPlan(PathPlan input) {
        this.score = input.score;
        this.goal = input.goal;
        this.points = new ArrayList<>(input.points);
    }

    double score;
    public Pose goal;

    public ArrayList<Pose> points;


    /**
     * Here's a fun feature. This is called a Javadoc. It's a documentation method that can give a
     * description to a variable, class, or function. Hover over the word "example" below to see it.
     * <p>
     * You can add these to the more confusing bits of your code to explain how something is
     * intended to be used or to provide more details than a name would allow.
     * <p>
     * -- Michael
     */
    Object example;

    public Pose getLatestPose() {
        return this.points.get(this.points.size() - 1);
    }
}
