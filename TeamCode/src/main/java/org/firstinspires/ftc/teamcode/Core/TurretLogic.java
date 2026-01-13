package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Robot.allianceSides.*;
import static org.firstinspires.ftc.teamcode.Core.TurretLogic.swivelControllers.*;
import static org.firstinspires.ftc.teamcode.Core.TurretLogic.controlMode.*;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Configurable
public class TurretLogic {
    Robot robot;
    DcMotorEx swivelMotor;
    public static double rawP = 0.00016;
    public static double rawI = 0.0;
    public static double rawD = 0.0;
    public static double rawF = 0.0;
    public static double fineP = 0.0009;
    public static double fineI = 0.0002;
    public static double fineD = 0.0;
    public static double fineF = 0.0;
    public static double tolerance;
    ezPID rawSwivelController;
    public ezPID fineSwivelController;
    double turretDegreesFromTarget;
    public int encoderResolution = 8192 * 132 / 16; // Actual encoder resolution * teeth on turret / teeth on motor side
    public static double fineDegreeWindow = 0;
    public static double safeDegreeDistance = 90;
    public static double manualOverridePositionInDegs = 0;
    public boolean goodAngle = false;
    public float inputModifier = 400;
    public float input;

    public enum swivelControllers {RAW, FINE}
    public enum controlMode{FULL, PARTIAL, LOCKED, OVERIDE}
    public static controlMode activeMode = PARTIAL;

    public swivelControllers lastUsedSwivelController;

    public double minimumDistance;
    public boolean canLaunch;

    public Follower follower;


    public TurretLogic(Robot robot, Follower followerIN) {
        this.robot = robot;
        follower = followerIN;

        swivelMotor = robot.swivelMotor;

        fineSwivelController = new ezPID(swivelMotor, 8192, fineP, fineI, fineD,
                fineF, 1.0, tolerance, ezPID.movementType.POSITION);

        rawSwivelController = new ezPID(swivelMotor, 8192, rawP, rawI, rawD,
                rawF, 1.0, tolerance, ezPID.movementType.POSITION);
    }

    public void runTurret() {
        if (follower == null && activeMode.equals(FULL)) {
            activeMode = LOCKED;
        } else if(activeMode.equals(FULL))
        {
            updateTurretPositionXY();
        }

        if (Math.abs(turretDegreesFromTarget) < fineDegreeWindow)
        {
            if (lastUsedSwivelController == RAW)
            {
                fineSwivelController.grabInfoFromPID(rawSwivelController.shareInfo());
            }
            lastUsedSwivelController = swivelControllers.FINE;
            fineSwivelController.changeBehaviorValues(fineP, fineI, fineD, fineF, 1);
            fineSwivelController.runCalledPID(runToSafeAngle(updateAngle()));
        } else if (Math.abs(turretDegreesFromTarget) > fineDegreeWindow)
        {
            if (lastUsedSwivelController == FINE)
            {
                rawSwivelController.grabInfoFromPID(fineSwivelController.shareInfo());
            }
            lastUsedSwivelController = RAW;
            rawSwivelController.changeBehaviorValues(rawP, rawI, rawD, rawF, 1);
            rawSwivelController.runCalledPID(runToSafeAngle(updateAngle()));
        }
        else
        {
            if (lastUsedSwivelController == RAW) {
                fineSwivelController.grabInfoFromPID(rawSwivelController.shareInfo());
            }
            lastUsedSwivelController = swivelControllers.FINE;
            fineSwivelController.changeBehaviorValues(fineP, fineI, fineD, fineF, 1);
            fineSwivelController.runCalledPID(runToSafeAngle(updateAngle()));
        }

        robot.panelsTelemetry.addData("Turret position", robot.swivelMotor.getCurrentPosition());
        robot.panelsTelemetry.addData("Turret target", runToSafeAngle(updateAngle()));
        robot.panelsTelemetry.addData("Turret position (degrees)", ticksToDegrees(robot.swivelMotor.getCurrentPosition()));
        robot.panelsTelemetry.addData("Turret target (degrees)", ticksToDegrees(runToSafeAngle(updateAngle())));

        robot.panelsTelemetry.addData("Limelight cooldown", tagCooldown);
        robot.panelsTelemetry.addData("Last known tag angle", lastKnownTagAngle);

        goodAngle = fineSwivelController.withinTolerance;
    }

    double checkDistance() {
        if (robot.targetTag.currentlyDetected) {
            return robot.targetTag.distanceZ;
        } else if (activeMode.equals(FULL)) {

            if (robot.alliance.equals(BLUE)) {
                //A^2 + B^2 = C^2
                return Math.sqrt(Math.pow(robot.turretPosition.x - 12.5, 2) + Math.pow((robot.turretPosition.y - 135.73), 2));
            } else if (robot.alliance.equals(Robot.allianceSides.RED)) {
                return Math.sqrt(Math.pow(robot.turretPosition.x - 131.5, 2) + Math.pow((robot.turretPosition.y - 135.73), 2));
            } else {
                return 0;
            }
        }

        return 0;
    }
    double updateAngle()
    /// Returns angle the launcher needs to move to IN DEGREES
    /// Dont worry about unsafe positions, a different function converts these
    {
        if(activeMode.equals(LOCKED) /*|| ( activeMode.equals(PARTIAL) && !robot.targetTag.currentlyDetected && Math.abs(input) < 0.1)*/)
        {
            return 0;
        }
        else if(activeMode.equals(OVERIDE))
        {
            return manualOverridePositionInDegs;
        }
        else if(checkTimeSinceKnownTag(5))
        {
            turretDegreesFromTarget = Math.abs(lastKnownTagAngle - ticksToTurretHeading());
            return ticksToTurretHeading() - lastKnownTagAngle;
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
                double rawAngle = Math.toDegrees(Math.atan2((robot.turretPosition.x -12.51), (robot.turretPosition.y -136.15)));
                //Corrects for heading then hands off to safeAngle logic
                turretDegreesFromTarget = Math.abs(-90 + (rawAngle));
                return robot.robotHeading - 90 + (rawAngle);
            }
            else
            {
                //gets the raw angle without accounting for heading
                double rawAngle = Math.toDegrees(Math.atan2((robot.turretPosition.x -131.699), (robot.turretPosition.y -135.73)));
                //Corrects for heading then hands off to safeAngle logic
                //turretDegreesFromTarget =  Math.abs(-90 + (rawAngle));
                return robot.robotHeading - 90 + (rawAngle);
            }
        }
        else return 0;
    }

    double ticksToRPM(double ticksPerSecIn)
    {
        return (ticksPerSecIn / encoderResolution) * 60;
    }

    int degreesToTicks(double degreesIN)
    {
        return (int) (((double) encoderResolution / 360) * degreesIN);
    }

    int ticksToDegrees(double ticksIN)
    {
        return (int) ((ticksIN / encoderResolution) * 360);
    }

    /// Will find the turret's direction relative to its zero point
    double ticksToTurretHeading()
    {
        return ticksToDegrees(swivelMotor.getCurrentPosition());
    }

    int inputToTicks()
    {
        return (int) (input * inputModifier);
    }


    int runToSafeAngle(double intINDegs) {

        double finalTargetDeg;
        double currentPosDeg = ticksToDegrees(swivelMotor.getCurrentPosition());
        finalTargetDeg = intINDegs;

        /*// 1. If the input angle is unsafe, move to its safe coterminal
        if (intINDegs > safeDegreeDistance) {
            finalTargetDeg = intINDegs - 360;
        } else if (intINDegs < -safeDegreeDistance) {
            finalTargetDeg = intINDegs + 360;
        }
        // 2. If the input is safe, check if its coterminal is also safe
        else {
            double coterminal = (intINDegs > 0) ? intINDegs - 360 : intINDegs + 360;

            if (withinSafeZone(coterminal)) {
                // Take the shorter movement
                double distNormal = Math.abs(intINDegs - currentPosDeg);
                double distCoterminal = Math.abs(coterminal - currentPosDeg);

                finalTargetDeg = (distCoterminal < distNormal) ? coterminal : intINDegs;
            } else {
                // Only the input is safe
                finalTargetDeg = intINDegs;
            }
        }*/

        if (finalTargetDeg > safeDegreeDistance) finalTargetDeg = safeDegreeDistance;
        else if (finalTargetDeg < -safeDegreeDistance) finalTargetDeg = -safeDegreeDistance;

        turretDegreesFromTarget = finalTargetDeg - currentPosDeg;
        return degreesToTicks(finalTargetDeg);
    }

    boolean withinSafeZone(double DegsIn)
    {
        return (DegsIn < safeDegreeDistance && DegsIn > -safeDegreeDistance);
    }

    void updateTurretPositionXY()
    {
        if(follower != null) {
            robot.robotPosition.x = follower.getPose().getX();
            robot.robotPosition.y = follower.getPose().getY();
            robot.robotHeading = Math.toDegrees(follower.getHeading());


            double rotatedX = robot.turretPositionOffsetXInches * Math.cos(robot.robotHeading) - robot.turretPositionOffsetYInches * Math.sin(robot.robotHeading);
            double rotatedY = robot.turretPositionOffsetXInches * Math.sin(robot.robotHeading) + robot.turretPositionOffsetYInches * Math.cos(robot.robotHeading);

            robot.turretPosition.x = robot.robotPosition.x + rotatedX;
            robot.turretPosition.y = robot.robotPosition.y + rotatedY;
        }
    }

    void calibratePositionFromTag()
    {
        //Angle from our robot heading to tag
        double testVal = ticksToDegrees(swivelMotor.getCurrentPosition()) + robot.targetTag.angleX;



        Vector2 goalPosition = new Vector2();
        if(robot.targetTag.currentlyDetected && fineSwivelController.withinTolerance) {
            if (robot.alliance.equals(BLUE))
            {
                goalPosition.x = 17;
                goalPosition.y = 132;
                //goalPosition.heading = 55;
            } else
            {
                goalPosition.x = 123;
                goalPosition.y = 132;
                //goalPosition.heading = 125;
            }


            //Do some trig
            //robot.robotPosition.x = ;
            //robot.robotPosition.y = ;
            //robot.robotHeading = ;
        }
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


}









