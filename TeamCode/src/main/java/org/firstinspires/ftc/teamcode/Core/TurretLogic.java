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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Configurable
public class TurretLogic {
    Robot robot;
    public static double tolerance;
    double turretDegreesFromTarget;
    public static final int encoderResolution = 8192 * 132 / 16; // Actual encoder resolution * teeth on turret / teeth on motor side
    //public static double fineDegreeWindow = 0;
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
        if (follower == null && activeMode.equals(FULL)) {
            activeMode = LOCKED;
        } else if(activeMode.equals(FULL))
        {
            updateTurretPositionXY();
        }

        

    }

    double checkDistance() {
        if (robot.targetTag.currentlyDetected) {
            return robot.targetTag.distanceZ;
        } else if (activeMode.equals(FULL)) {

            if (robot.alliance.equals(BLUE)) {
                //A^2 + B^2 = C^2
                return Math.sqrt(Math.pow(robot.turretPosition.x - 12.5, 2) + Math.pow((robot.turretPosition.y - 135.73), 2));
            } else if (robot.alliance.equals(RED)) {
                return Math.sqrt(Math.pow(robot.turretPosition.x - 131.5, 2) + Math.pow((robot.turretPosition.y - 135.73), 2));
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
        else if(activeMode.equals(PARTIAL) && checkTimeSinceKnownTag(5))
        {
            turretDegreesFromTarget = Math.abs(lastKnownTagAngle - ticksToTurretHeading());
            return lastKnownTagAngle + ticksToTurretHeading();
        }
        else if(activeMode.equals(PARTIAL) && Math.abs(input) >= 0.1)
        {
            return ticksToTurretHeading() + ticksToDegrees(inputToTicks());
        }
        else if(activeMode.equals(FULL))
        {
            if(robot.alliance.equals(BLUE))
            {
                //gets the raw angle without accounting for heading
                double rawAngle = Math.toDegrees(Math.atan2((robot.turretPosition.x - 12), (robot.turretPosition.y - 132)));
                //Corrects for heading then hands off to safeAngle logic
                // TODO make this update live
                turretDegreesFromTarget = Math.abs(90 + rawAngle);
                return robot.robotHeading + 90 + rawAngle;
            }
            else
            {
                //gets the raw angle without accounting for heading
                double rawAngle = Math.toDegrees(Math.atan2((robot.turretPosition.x - 132), (robot.turretPosition.y - 132)));
                //Corrects for heading then hands off to safeAngle logic
                turretDegreesFromTarget =  Math.abs(90 + (rawAngle));
                return robot.robotHeading + 90 + (rawAngle);
            }
        }
        else return 0;
    }

    public static double degreesToServo(double degreesIN)
    {
        return (1/240);
    }

    public double runToSafeAngle(double intINDegs) {


        return degreesToServoPWM(intINDegs);
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

    public double degreesToServoPWM(double degreesIn)
    {
        double leftLimitDegs = -90;
        double rightLimitDegs = 150;
        double goToValue;
      if(degreesIn < 0)
      {
          goToValue = 0.5 - (0.5*(Math.abs(degreesIn) / Math.abs(leftLimitDegs)));
      }
      else
      {
          goToValue = 0.5 + (0.5*(Math.abs(degreesIn) / Math.abs(rightLimitDegs)));
      }
      return goToValue;
    }
}









