package org.firstinspires.ftc.teamcode.Core;


import static org.firstinspires.ftc.teamcode.Core.Robot.Color;
import static org.firstinspires.ftc.teamcode.Core.Robot.Color.*;

import org.firstinspires.ftc.teamcode.Templates.TheTeleOp;

public class ColorFinding {
    public Robot bob = null;

    public ColorFinding(Robot robot){
        bob = robot;
    }

    


    public Robot.Color[] autoColorOrder = {
            PINK,
            Robot.Color.YELLOW,
            PINK,
            Robot.Color.BLUE,
            Robot.Color.ORANGE,
            Robot.Color.GREEN,
            PINK,
            Robot.Color.YELLOW,
            Robot.Color.BLUE
    };
    //converts color to number
    private int colorToNumber(Robot.Color color){
        if (color == PINK) {
            return 0;
        } else if (color == ORANGE) {
            return 1;
        } else if (color == YELLOW) {
            return 2;
        } else if (color == GREEN) {
            return 3;
        } else if (color == BLUE) {
            return 4;
        } else {
            return -1;
        }
    }
    //compares color to current color
    public Boolean checkColor(Color color){
        return color == bob.getColor();
    }
    //calculates the shortest distance between colors
    private int shortestDirection(Color c_from, Color c_to){
        int from = colorToNumber(c_from);
        int to = colorToNumber(c_to);
        int v = to - from;
        if (v == 0) {
            return 0;
        } else if (Math.abs(v) < 2.5) {
            return 1;
        } else {
            return -1;
        }

    }
    //moves to the correct color
    public void update(Color color) {
        if (this.checkColor(color)) {
            bob.colorServo.setPower(0);
            return;
        }
        int direction = this.shortestDirection(bob.getColor(), color);
        bob.colorServo.setPower(direction*0.1);

    }
}
