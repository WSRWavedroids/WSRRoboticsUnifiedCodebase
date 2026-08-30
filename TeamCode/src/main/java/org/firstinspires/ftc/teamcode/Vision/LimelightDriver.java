package org.firstinspires.ftc.teamcode.Vision;

import static org.firstinspires.ftc.teamcode.Core.Robot.PatternColors.*;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Core.Robot;

import java.util.List;

public class LimelightDriver {

    private final Limelight3A limelight;
    private final Robot robot;

    public LimelightDriver(Robot robot) {
        this.robot = robot;
        limelight = robot.hardwareMap.get(Limelight3A.class , "limelight");
    }

    /**
     * Initializes the LimeLight by setting the pipeline and telling it to start reading tags.
     * @param pipeline The internal LimeLight pipeline. 0 is pattern, 1 is red, and 2 is blue
     */
    public void initLimeLightTargeting(int pipeline) {
        limelight.stop();
        limelight.pipelineSwitch(pipeline);
        limelight.start();
    }

    /**
     * Tracks the target tag. Note: if you switch alliances, call initLimeLightTargeting() to update 
     * it.
     * @return A WaveTag of the current target tag
     */
    public WaveTag getTargetTag() {
        WaveTag current = new WaveTag();

        current.currentlyDetected = false;

        LLResult result = limelight.getLatestResult();

        List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fr : fiducialResults) {
            robot.telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f", fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());

            current.currentlyDetected = true;
            Pose3D targetPoseRs = fr.getTargetPoseRobotSpace();
            Pose3D robotPoseFromTag = fr.getRobotPoseTargetSpace();
            Pose3D robotPoseOnField = fr.getRobotPoseFieldSpace();

            current.tagID = fr.getFiducialId();
            current.distanceZ = targetPoseRs.getPosition().z;
            current.distanceY = targetPoseRs.getPosition().y;
            current.distanceX = targetPoseRs.getPosition().x;

            current.angleX = fr.getTargetXDegrees();
            current.angleY = fr.getTargetYDegrees();

            current.tagPosFromRobot = targetPoseRs;
            current.robotFromTagPos = robotPoseFromTag;
            current.robotFieldPoseFromTag = robotPoseOnField;


        }

        return current;
    }

    /**
     * Find the pattern on the Obelisk. 
     * <p>
     *     WARNING: DO NOT CALL REPEATEDLY. This function switches the LimeLight's pipeline (thus 
     *     starting and stopping it) twice.
     * </p>
     * @return The match's pattern, if the robot can see it
     */
    public Robot.PatternColors getRandomization() {
        Robot.PatternColors current;

        initLimeLightTargeting(0);
        
        LLResult result = limelight.getLatestResult();
        if (result.isValid()) {
            // Access fiducial results
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            switch (fiducialResults.get(1).getFiducialId()) {
                case 21:
                    current = GPP;
                    break;
                case 22:
                    current = PGP;
                    break;
                case 23:
                    current = PPG;
                default:
                    current = UNKNOWN;
            }
        } else {
            current = UNKNOWN;
        }

        initLimeLightTargeting(robot.alliance.limelightPipeline);

        return current;
    }

}