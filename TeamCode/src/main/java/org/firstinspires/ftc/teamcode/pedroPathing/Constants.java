package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;



import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(14.9685)
            .forwardZeroPowerAcceleration(-35.41393601)
            .lateralZeroPowerAcceleration(-82.48715380)
            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(true)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.15, 0.0000004, 0.02037, 0.1))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.15, 0.0000004, 0.02037, 0.01))
            .headingPIDFCoefficients(new PIDFCoefficients(1, 0, 0.129, 0.1))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(1, 0, 0.129, 0.01))
            .centripetalScaling(0.0007)
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.012,0,0.0027,0.6,0.1))
            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0.012,0,0.0064,0.6,0.1));
    public static PathConstraints pathConstraints = new PathConstraints(
            0.99,
            100,
            3.3,
            1);
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frontRightDrive")
            .rightRearMotorName("backRightDrive")
            .leftRearMotorName("backLeftDrive")
            .leftFrontMotorName("frontLeftDrive")
            .leftFrontMotorDirection(DcMotorEx.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorEx.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorEx.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorEx.Direction.REVERSE)
            .xVelocity(74.98508771)
            .yVelocity(53.24601301)
            .useBrakeModeInTeleOp(true)
            .useVoltageCompensation(true);
    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(4.25)
            .strafePodX(-1.5)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .pinpointLocalizer(localizerConstants)
                .mecanumDrivetrain(driveConstants)
                /* other builder steps */
                .build();
    }
}

//Clay's official legal name was here
//Deven Was Here