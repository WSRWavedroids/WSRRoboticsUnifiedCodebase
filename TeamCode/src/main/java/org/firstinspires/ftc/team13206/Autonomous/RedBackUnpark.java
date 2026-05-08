package org.firstinspires.ftc.team13206.Autonomous;

import static org.firstinspires.ftc.team13206.Autonomous.BetaBlueBackAuto.Steps.STOP;
import static org.firstinspires.ftc.team13206.Autonomous.BetaBlueBackAuto.Steps.STRAFE;
import static org.firstinspires.ftc.team13206.Autonomous.BetaBlueBackAuto.Steps.UNPARK;
import static org.firstinspires.ftc.team13206.Core.ArtifactLocator.SlotState.GREEN;
import static org.firstinspires.ftc.team13206.Core.ArtifactLocator.SlotState.PURPLE;
import static org.firstinspires.ftc.team13206.Core.SorterHardware.PositionState.FIRE;
import static org.firstinspires.ftc.team13206.Core.SorterHardware.PositionState.LOAD;
import static org.firstinspires.ftc.team13206.Core.SorterHardware.PositionState.STORE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.team13206.Core.TurretLogic;

@Autonomous(group = "0. FABIO NO PEDRO", name = "Red Back Unpark")
public class RedBackUnpark extends BetaRedBackAuto {

    @Override
    public void loop() {
        robot.turret.follower.updatePose();
        robot.updateAllDaThings();
        switch (currentStep) {
            case START:
                auto.setTolerances(7);

                auto.setSpeed(driveSpeed);

                robot.sorterLogic.sortOutBlobs(GREEN, LOAD);
                robot.sorterLogic.sortOutBlobs(PURPLE, FIRE);
                robot.sorterLogic.sortOutBlobs(PURPLE, STORE);

                robot.pattern = robot.randomizationScanner.GetRandomization();//One last Check

                robot.targetScanner.InitLimeLightTargeting(robot.alliance.limelightPipeline, robot);

                robot.scanningForTargetTag = true;
                TurretLogic.activeMode = TurretLogic.controlMode.FULL;
                nextStep(STRAFE);
                break;
            case STRAFE:
                auto.moveRobotLeft(100);
                nextStep(UNPARK);
                break;
            case UNPARK:
                if (auto.checkMovement()) {
                    auto.moveRobotForward(750);
                    nextStep(STOP);
                }
                break;
            case STOP:
                if(auto.checkMovement())
                {
                    robot.turret.updateTurretPositionXY();
                    super.requestOpModeStop();
                }
        }
        doTelemetryStuff();
    }
}
