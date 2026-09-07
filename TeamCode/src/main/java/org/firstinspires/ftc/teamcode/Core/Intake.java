package org.firstinspires.ftc.teamcode.Core;

public class Intake {
    public Robot robot;

    public Intake(Robot robot) {
        this.robot = robot;
    }

    public void eat() {
        robot.intakeMotor.setPower(1);
    }

    public void barf() {
        robot.intakeMotor.setPower(-1);
    }

    public void sit() {
        robot.intakeMotor.setPower(0);
    }

}
