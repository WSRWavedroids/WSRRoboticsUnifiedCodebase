package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Robot.Color.*;

public class Colorful_Banana_Op {
    private Robot robot;
    private Robot.Color targetColor = OFF;
    public Colorful_Banana_Op(Robot robot) {
        this.robot = robot;
    }
    public void move(Robot.Color moveColor) {
        int currentPosition = colorToNumber(robot.getColor());
        int movePosition = colorToNumber(moveColor);
        targetColor = moveColor;

        if (movePosition - currentPosition <= 2.5 && >= 0 ) {
            robot.colorServo.setPower(0.1);
        } else {
            robot.colorServo.setPower(-0.1);
        }
    }
    public void update(){
        if (robot.getColor() == targetColor){
            robot.colorServo.setPower(0);
        }
    }
    private int colorToNumber(Robot.Color color){
        if (color == PINK){
            return 0;
        }
        if (color == ORANGE){
            return 1;
        }
        if (color == YELLOW){
            return 2;
        }
        if (color == GREEN){
            return 3;
        }if (color == BLUE) {
            return 4;
        }
        else {
            return -1;
        }
    }
}
