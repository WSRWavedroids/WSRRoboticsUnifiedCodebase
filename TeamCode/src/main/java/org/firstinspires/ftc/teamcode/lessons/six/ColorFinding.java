package org.firstinspires.ftc.teamcode.lessons.six;

import static org.firstinspires.ftc.teamcode.lessons.six.ColorFinding.FindColorSteps.*;

import org.firstinspires.ftc.teamcode.Core.LED;
import org.firstinspires.ftc.teamcode.Core.Robot;

import java.util.ArrayList;

public class ColorFinding {

    public ColorFinding(Robot robot) {
        this.robot = robot;
    }

    public Robot robot;
    public enum FindColorSteps {
        EMPTY, RED, YELLOW, GREEN, BLUE, PINK
    }
    public FindColorSteps currentStep = EMPTY;
    public void findColor() {
        switch (currentStep) {
            case EMPTY:
                robot.colorServo.setPower(0);
                break;
            case RED:
                if (robot.getColor() == Robot.Color.YELLOW || robot.getColor() == Robot.Color.GREEN) {
                    robot.colorServo.setPower(-0.1);
                }
                else {
                    robot.colorServo.setPower(0.1);
                }
                if (robot.getColor() == Robot.Color.RED) {
                    robot.colorServo.setPower(0);
                    currentStep = EMPTY;
                }
                break;
            case YELLOW:
                if (robot.getColor() == Robot.Color.BLUE || robot.getColor() == Robot.Color.GREEN) {
                    robot.colorServo.setPower(-0.1);
                }
                else {
                    robot.colorServo.setPower(0.1);
                }
                if (robot.getColor() == Robot.Color.YELLOW) {
                    robot.colorServo.setPower(0);
                    currentStep = EMPTY;
                }
                break;
            case GREEN:
                if (robot.getColor() == Robot.Color.BLUE || robot.getColor() == Robot.Color.PINK) {
                    robot.colorServo.setPower(-0.1);
                }
                else {
                    robot.colorServo.setPower(0.1);
                }
                if (robot.getColor() == Robot.Color.GREEN) {
                    robot.colorServo.setPower(0);
                    currentStep = EMPTY;
                }
                break;
            case BLUE:
                if (robot.getColor() == Robot.Color.PINK || robot.getColor() == Robot.Color.RED) {
                    robot.colorServo.setPower(-0.1);
                }
                else {
                    robot.colorServo.setPower(0.1);
                }
                if (robot.getColor() == Robot.Color.BLUE) {
                    robot.colorServo.setPower(0);
                    currentStep = EMPTY;
                }
                break;
            case PINK:
                if (robot.getColor() == Robot.Color.RED || robot.getColor() == Robot.Color.YELLOW) {
                    robot.colorServo.setPower(-0.1);
                }
                else {
                    robot.colorServo.setPower(0.1);
                }
                if (robot.getColor() == Robot.Color.PINK) {
                    robot.colorServo.setPower(0);
                    currentStep = EMPTY;
                }
                break;
        }
    }
    public void getRotationDirection() {
        Robot.Color currentColor = robot.getColor();

    }
    private int colorNumberer(Robot.Color color) {
        if (color == Robot.Color.RED) {
            return 1;
        }
        return 0;
    }


}
