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
            .mass(29)
            .forwardZeroPowerAcceleration(-25.20227881)
            .lateralZeroPowerAcceleration(-68.10070593)
            .useSecondaryTranslationalPIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryDrivePIDF(true)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.15, 0.0000004, 0.02037, 0.1))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.15, 0.0000004, 0.02037, 0.01))
            .headingPIDFCoefficients(new PIDFCoefficients(1, 0, 0.129, 0.1))
            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(1, 0, 0.129, 0.01))
            .centripetalScaling(0.0007)
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.025,0,0.00001,0.6,0.1))
            .secondaryDrivePIDFCoefficients(new FilteredPIDFCoefficients(0.02,0,0.000004,0.6,0.01));
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);
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
            .xVelocity(74.21699439)
            .yVelocity(45.28314737);
    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(-3.75)
            .strafePodX(-1.75)
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