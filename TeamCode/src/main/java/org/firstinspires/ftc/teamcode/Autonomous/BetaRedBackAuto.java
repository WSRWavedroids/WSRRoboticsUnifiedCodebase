package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Core.Robot.allianceSides.BLUE;
import static org.firstinspires.ftc.teamcode.Core.Robot.allianceSides.RED;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.Robot;
import org.firstinspires.ftc.teamcode.Core.TurretLogic;

/**
 * This is a shell that sets the alliance key to "RED" and runs BetaBlueFrontAuto with it. It's
 * efficient, at least. But this class is basically just a button on the control hub. The auto is
 * in BetaBlueFrontAuto. Your problem is probably there.
 */
@Autonomous(group = "0. FABIO NO PEDRO", name = "Red Back 6 Ball")
public class BetaRedBackAuto extends BetaBlueBackAuto {

    private Pose startPose = new Pose(144-56.5, 9.200, Math.toRadians(0));

    public void init() {
        super.init();
        // Override the alliance key set in the blue auto to run the red one
        robot = new Robot(hardwareMap, telemetry, this);
//        TurretLogic.tolerance = robot.turret.degreesToTicks(8);
        auto = new AutonomousPlusPLUS(robot);
        robot.turret.activeMode = TurretLogic.controlMode.FULL;

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        robot.randomizationScanner.InitLimeLight(0);

        stallTimer = new ElapsedTime();

        robot.turret.follower.setPose(startPose);
        robot.turret.follower.setHeading(startPose.getHeading());
        blackboard.put(ALLIANCE_KEY, "RED");
        robot.alliance = RED;
    }

    public void init_loop() {
        super.init_loop();
    }

    public void start() {
        super.start();
    }

    public void loop() {
        super.loop();
    }

    public void stop() {
        super.stop();
    }
}


