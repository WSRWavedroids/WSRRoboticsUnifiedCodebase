package org.firstinspires.ftc.teamcode.lessons.six;
import org.firstinspires.ftc.teamcode.Core.Robot;
public class ColorFinding {
    public ColorFinding(Robot robot) {
        this.robot = robot;
        currentColor = colorNumberer(robot.getColor());
        findColor = currentColor;
    }

    public Robot robot;
    int currentColor;
    int findColor;
    public Robot.Color color = Robot.Color.EMPTY;

    public void updateFindColor() {
        if (robot.getColor() == color) {
            robot.colorServo.setPower(0);
        }
    }
    public void findColor(Robot.Color color) {
        this.color = color;
        if (findColor - currentColor > 2.5 || (findColor - currentColor < 0 && findColor - currentColor < -2.5)) {
            robot.colorServo.setPower(0.1);
        } else {
            robot.colorServo.setPower(-0.1);
        }
    }
    public int colorNumberer(Robot.Color color) {
        switch (color) {
            case PINK: return 1;
            case BLUE: return 2;
            case GREEN: return 3;
            case YELLOW: return 4;
            case RED: return 5;
            default: return 0;
        }
    }

}