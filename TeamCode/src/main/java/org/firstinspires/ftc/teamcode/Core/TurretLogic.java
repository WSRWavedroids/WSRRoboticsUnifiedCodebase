package org.firstinspires.ftc.teamcode.Core;

import static android.os.SystemClock.sleep;
import static com.qualcomm.robotcore.eventloop.opmode.OpMode.blackboard;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static org.firstinspires.ftc.teamcode.Core.Robot.allianceSides.*;
import static org.firstinspires.ftc.teamcode.Core.TurretLogic.swivelControllers.*;
import static org.firstinspires.ftc.teamcode.Core.TurretLogic.controlMode.*;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Configurable
public class TurretLogic {
    Robot robot;
    public static double tolerance;
    double turretDegreesFromTarget;

    public static double upperLimit = 150;
    public static double lowerLimit = -90;
    public static double manualOverridePositionInDegs = 0;
    public boolean goodAngle = false;
    public float inputModifier = 400;
    public float input;


    double offsetDegrees;

    enum swivelControllers {RAW, FINE}
    public enum controlMode{FULL, PARTIAL, LOCKED, OVERIDE}
    public static controlMode activeMode = FULL;

    public swivelControllers lastUsedSwivelController;

    public double minimumDistance;
    public boolean canLaunch;

    public Follower follower;


    public TurretLogic(Robot robot, Follower followerIN) {
        this.robot = robot;
        follower = followerIN;
    }

    public void runTurret() {
        if(activeMode.equals(FULL))
        {
            if (follower == null) {
                activeMode = LOCKED;
            }
            else {
                updateTurretPositionXY();
            }

        }

        robot.turretServo.setPosition(
                runToSafeAngle(
                        updateAngle()
                )
        );
    }

    double checkDistance() {
        if (robot.targetTag.currentlyDetected) {
            return robot.targetTag.distanceZ;
        } else if (activeMode.equals(FULL)) {

            if (robot.alliance.equals(BLUE)) {
                //A^2 + B^2 = C^2
                return Math.sqrt(Math.pow(robot.turretPosition.x - 4, 2) + Math.pow((robot.turretPosition.y - 140), 2));
            } else if (robot.alliance.equals(RED)) {
                return Math.sqrt(Math.pow(robot.turretPosition.x - 140, 2) + Math.pow((robot.turretPosition.y - 140), 2));
            } else {
                return 0;
            }
        }

        return 0;
    }
    public double updateAngle()
    /// Returns angle the launcher needs to move to IN DEGREES
    /// Dont worry about unsafe positions, a different function converts these
    {
        if(activeMode.equals(LOCKED))
        {
            return 0;
        }
        else if(activeMode.equals(OVERIDE))
        {
            return manualOverridePositionInDegs;
        }
        /*else if(activeMode.equals(PARTIAL) && Math.abs(input) >= 0.1)
        {
            return ticksToTurretHeading() + ticksToDegrees(inputToTicks());
        }*/
        else if(activeMode.equals(FULL))
        {
            double rawAngle;
            if(robot.alliance.equals(BLUE))
            {
                //gets the raw angle without accounting for heading
                rawAngle = Math.toDegrees(Math.atan2((robot.turretPosition.x - 12), (robot.turretPosition.y - 132)));
            }
            else
            {
                //gets the raw angle without accounting for heading
                rawAngle = Math.toDegrees(Math.atan2((robot.turretPosition.x - 132), (robot.turretPosition.y - 132)));
                //Corrects for heading then hands off to safeAngle logic
            }

            //Corrects for heading then hands off to safeAngle logic
            // TODO make this update live
            turretDegreesFromTarget = Math.abs(90 + rawAngle);
            return robot.robotHeading + 90 + rawAngle;
        }
        else return 0;
    }

    /*public static double degreesToServo(double degreesIN)
    {
        return (1/240);
    }*/

    public double runToSafeAngle(double intINDegs) {
        while(intINDegs > upperLimit) {
            intINDegs -= 360;
        }
        while (intINDegs < lowerLimit) {
            intINDegs += 360;
        }

        return degreesToServoUnits(limitIfNeeded(intINDegs));
    }

    double limitIfNeeded(double input) {
        if (input > upperLimit) {
            input = upperLimit;
        } else if (input < lowerLimit) {
            input = lowerLimit;
        }
        return input;
    }

    boolean withinSafeZone(double DegsIn)
    {
        return (DegsIn < upperLimit && DegsIn > lowerLimit);
    }

    public void updateTurretPositionXY()
    {
        if(follower != null) {
            robot.robotPosition.x = follower.getPose().getX();
            robot.robotPosition.y = follower.getPose().getY();
            robot.robotHeading = Math.toDegrees(follower.getHeading());

            blackboard.put("PedroX", robot.robotPosition.x);
            blackboard.put("PedroY", robot.robotPosition.y);
            blackboard.put("PedroHeading", Math.toRadians(robot.robotHeading));

            double rotatedX = robot.turretPositionOffsetXInches * Math.cos(robot.robotHeading) - robot.turretPositionOffsetYInches * Math.sin(robot.robotHeading);
            double rotatedY = robot.turretPositionOffsetXInches * Math.sin(robot.robotHeading) + robot.turretPositionOffsetYInches * Math.cos(robot.robotHeading);

            robot.turretPosition.x = robot.robotPosition.x + rotatedX;
            robot.turretPosition.y = robot.robotPosition.y + rotatedY;
        }
    }

    void calibratePositionFromTag()
    {
        /*//Angle from our robot heading to tag
        double testVal = ticksToDegrees(getMotorPosition()) + robot.targetTag.angleX;

        Vector2 goalPosition = new Vector2();
        if(robot.targetTag.currentlyDetected && fineSwivelController.withinTolerance) {
            if (robot.alliance.equals(BLUE))
            {
                goalPosition.x = 17;
                goalPosition.y = 132;
            } else
            {
                goalPosition.x = 123;
                goalPosition.y = 132;
            }
        }*/
    }

    private ElapsedTime tagCooldown = new ElapsedTime();
    private double lastKnownTagAngle;
    private boolean checkTimeSinceKnownTag(double time) {
        if (robot.targetTag.currentlyDetected) {
            lastKnownTagAngle = robot.targetTag.angleX;
            tagCooldown.reset();
            return true;
        }
        else return tagCooldown.seconds() <= time;
    }

    public double findStartingAngle()
    {
        double maxAnalogValue = 0.875; //tempValue
        double minAnalogValue = 0.355;
        double adjustedMax = maxAnalogValue-minAnalogValue;

        double recorded = robot.analogTurretTracker.getVoltage() - minAnalogValue;
        double voltPercentage = recorded / adjustedMax;
        double degrees = ((153 - -87) * voltPercentage) + -87;

        return  - (5.11539 * (Math.pow(10, -8))) * Math.pow(degrees, 4)
                + 0.0000184408 * (Math.pow(degrees, 3))
                - 0.0042155 * (Math.pow(degrees, 2))
                + 1.05153 * (degrees)
                + 48.60426;
    }

    public double degreesToServoUnits(double degrees) {
        final double zeroLimitDegrees = 145;
        final double halfPointDegrees = 0;
        final double oneLimitDegrees = -90;

        if (degrees < halfPointDegrees) {
            return (0.5 - 1) / (halfPointDegrees - oneLimitDegrees) * (degrees - oneLimitDegrees) + 1;
        }
        else {
            return (0 - 0.5) / (zeroLimitDegrees - halfPointDegrees) * (degrees - halfPointDegrees) + 0.5;
        }
    }

    public static double servoUnitsToDegrees(double servoUnits) {
        final double zeroLimitDegrees = 145;
        final double halfPointDegrees = 0;
        final double oneLimitDegrees = -90;

        if (servoUnits < halfPointDegrees) {
            return (halfPointDegrees - oneLimitDegrees) / (0.5 - 1)  * (servoUnits - 1) + oneLimitDegrees;
        }
        else {
            return (zeroLimitDegrees - halfPointDegrees) / (0 - 0.5) * (servoUnits - 0.5) + halfPointDegrees;
        }
    }

    public boolean positioned() {
        return Math.abs(robot.targetTag.angleX) <= 5;
    }
}









