package org.firstinspires.ftc.teamcode.Core;

import static com.qualcomm.robotcore.eventloop.opmode.OpMode.blackboard;
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
    public DcMotorEx swivelMotor;
    public static double rawP = 0.00015;
    public static double rawI = 0.00;
    public static double rawD = 0.0;
    public static double rawF = 0.0;

    /*public static double fineP = 0.00008;
    public static double fineI = 0.0002;
    public static double fineD = 0.0;
    public static double fineF = 0.0;*/
    public static double tolerance;
    public ezPID rawSwivelController;
    public ezPID fineSwivelController;
    double turretDegreesFromTarget;
    public static final int encoderResolution = 8192 * 132 / 16; // Actual encoder resolution * teeth on turret / teeth on motor side
    //public static double fineDegreeWindow = 0;
    public static double upperLimit = 150;
    public static double lowerLimit = -90;
    public static double manualOverridePositionInDegs = 0;
    public boolean goodAngle = false;
    public float inputModifier = 400;
    public float input;

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

        swivelMotor = robot.swivelMotor;


        /*fineSwivelController = new ezPID(swivelMotor, 8192, fineP, fineI, fineD,
                fineF, 1.0, tolerance, ezPID.movementType.POSITION);*/

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

        rawSwivelController.tolerance = tolerance;

        /*if (Math.abs(turretDegreesFromTarget) < fineDegreeWindow)
        {
            if (lastUsedSwivelController == RAW)
            {
                fineSwivelController.grabInfoFromPID(rawSwivelController.shareInfo());
            }
            lastUsedSwivelController = swivelControllers.FINE;
            fineSwivelController.changeBehaviorValues(fineP, fineI, fineD, fineF, 1);
            fineSwivelController.runCalledPID(runToSafeAngle(updateAngle()));
            fineSwivelController.tolerance = tolerance;
        } else if (Math.abs(turretDegreesFromTarget) > fineDegreeWindow)
        {
            if (lastUsedSwivelController == FINE)
            {
                rawSwivelController.grabInfoFromPID(fineSwivelController.shareInfo());
            }
            lastUsedSwivelController = RAW;
            rawSwivelController.changeBehaviorValues(rawP, rawI, rawD, rawF, 1);
            rawSwivelController.tolerance = tolerance;
            rawSwivelController.runCalledPID(runToSafeAngle(updateAngle()));
        }
        else
        {
            if (lastUsedSwivelController == RAW) {
                fineSwivelController.grabInfoFromPID(rawSwivelController.shareInfo());
            }
            lastUsedSwivelController = swivelControllers.FINE;
            fineSwivelController.changeBehaviorValues(fineP, fineI, fineD, fineF, 1);
            fineSwivelController.tolerance = tolerance;
            fineSwivelController.runCalledPID(runToSafeAngle(updateAngle()));
        }*/

        lastUsedSwivelController = RAW;
        rawSwivelController.changeBehaviorValues(rawP, rawI, rawD, rawF, 1);
        rawSwivelController.tolerance = tolerance;
        rawSwivelController.runCalledPID(runToSafeAngle(updateAngle()));

        robot.panelsTelemetry.addData("Turret position", robot.swivelMotor.getCurrentPosition());
        robot.panelsTelemetry.addData("Turret target", runToSafeAngle(updateAngle()));
        robot.panelsTelemetry.addData("Turret position (degrees)", ticksToDegrees(robot.swivelMotor.getCurrentPosition()));
        robot.panelsTelemetry.addData("Turret target (degrees)", ticksToDegrees(runToSafeAngle(updateAngle())));

        robot.panelsTelemetry.addData("Limelight cooldown", tagCooldown);
        robot.panelsTelemetry.addData("Last known tag angle", lastKnownTagAngle);

        //goodAngle = rawSwivelController.withinTolerance(runToSafeAngle(updateAngle()));
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

    double ticksToRPM(double ticksPerSecIn)
    {
        return (ticksPerSecIn / encoderResolution) * 60;
    }

    public static int degreesToTicks(double degreesIN)
    {
        return (int) (((double) encoderResolution / 360) * degreesIN);
    }

    public static int ticksToDegrees(double ticksIN)
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


    public int runToSafeAngle(double intINDegs) {

        double finalTargetDeg;
        double currentPosDeg = ticksToDegrees(swivelMotor.getCurrentPosition());

        // 1. If the input angle is unsafe, move to its safe coterminal
        while(intINDegs > upperLimit) {
            intINDegs -= 360;
        }
        while (intINDegs < lowerLimit) {
            intINDegs += 360;
        }
        finalTargetDeg = limitIfNeeded(intINDegs);
        /*// 2. If the input is safe, check if its coterminal is also safe
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

        turretDegreesFromTarget = finalTargetDeg - currentPosDeg;
        return degreesToTicks(finalTargetDeg);
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
            blackboard.put("PedroHeading", robot.robotHeading);

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









