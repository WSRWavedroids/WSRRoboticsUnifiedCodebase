package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Robot.Alliance.*;
import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.geometry.BezierPoint;

public class Turret {
    private Robot robot;
    public double heading;
    public Turret(Robot robot) {

        this.robot = robot;
    }
    public double degreesToServoUnits(double degrees) {
        final double zeroLimitDegrees = 138;
        final double halfPointDegrees = 0;
        final double oneLimitDegrees = -90;

        if (degrees < halfPointDegrees) {
            return (0.5 - 1) / (halfPointDegrees - oneLimitDegrees) * (degrees - oneLimitDegrees) + 1;
        }
        else {
            return (0 - 0.5) / (zeroLimitDegrees - halfPointDegrees) * (degrees - halfPointDegrees) + 0.5;
        }
    }
    public void startLockOn() {
        double x = follower.getPose().getX() +;
        double y = follower.getPose().getY() +;
        if (robot.alliance == RED) {
            heading = Math.toDegrees(Math.atan2(144 - y, 144 - x));
            robot.turretServo.setPosition(degreesToServoUnits(heading));
        }
        if (robot.alliance == BLUE) {
            heading = Math.toDegrees(Math.atan2(144 - y, 0 - x));
            robot.turretServo.setPosition(degreesToServoUnits(heading));
        }
    }
    public boolean isLockedOn() {

        return false;
    }
    public void update() {

    }
}
