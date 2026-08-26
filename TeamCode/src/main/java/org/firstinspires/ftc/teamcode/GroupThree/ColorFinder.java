package org.firstinspires.ftc.teamcode.GroupThree;

import static org.firstinspires.ftc.teamcode.Core.Robot.Color.*;

import org.firstinspires.ftc.teamcode.Core.Robot;

public class ColorFinder {
    private Robot robot;
    private Robot.Color targetColor = NONE;
    public ColorFinder(Robot robot) {
        this.robot = robot;
    }

    public void move(Robot.Color targetColor) {
        int currentPosition = colorToNumber(robot.getColor());
        int targetPosition = colorToNumber(targetColor);
        this.targetColor = targetColor;
        if (targetPosition - currentPosition > 2.5 | targetPosition - currentPosition < 0) {
            robot.colorServo.setPower(0.25);
        } else if (targetPosition - currentPosition < 2.5) {
            robot.colorServo.setPower(-0.25);
        }
        if (currentPosition == targetPosition) {
            robot.colorServo.setPower(0);
        }
    }

    public void update() {
        if (targetColor == robot.getColor()) {
            robot.colorServo.setPower(0);
        }
    }

    private int colorToNumber(Robot.Color color) {
        if (color == PINK) {
            return 0;
        } else if (color == ORANGE) {
            return  1;
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
}