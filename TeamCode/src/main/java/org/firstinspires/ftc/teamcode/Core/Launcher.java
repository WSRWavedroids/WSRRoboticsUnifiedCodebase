package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class Launcher {
    private Robot robot;
    private PIDFCoefficients launcherPIDValues = new PIDFCoefficients(0.0045, 0, 0, 0.0004);

    public Launcher(Robot robot) {
        this.robot = robot;
        robot.launcherMotorOne.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, launcherPIDValues);
        robot.launcherMotorTwo.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, launcherPIDValues);
    }

    private int launcherSpeed = 0;
    public boolean isLaunching = false;
    public boolean launched = false;
    public boolean readyToLaunch = false;
    private boolean flickyIsUp = true;
    private double targetVelocity = 0;
    private double currentTime = 0;

    //flipperIsUp is set to true initially until it is checked for extra safety
    private enum launcherState {
        STALL,
        CHECK_BLENDER,
        REV_MOTOR,
        FLICKY_UP,
        FLICKY_DOWN

    }

    launcherState currentState = launcherState.STALL;
    public void fireBall() {
        goLaunch = true;
    }
    public boolean goLaunch = false;

    public void update() {
       switch (currentState) {
            case STALL:
                //add a way to switch to launching
                if (goLaunch) {
                    currentState = launcherState.CHECK_BLENDER;
                }
                break;
            case CHECK_BLENDER:
                if (robot.blender.isBlenderPositioned()) {
                    robot.blender.lockBlender(true);
                    setPerfectLaunchSpeed(robot.limelight.getTargetTag().distanceZ);
                    currentState = launcherState.REV_MOTOR;
                }
                break;
           case REV_MOTOR:
               if (Math.abs(robot.launcherMotorOne.getVelocity() - targetVelocity) <= 40) {
                   currentState = launcherState.FLICKY_UP;
                   robot.flicky.setPosition(0);
                   flickyIsUp = true;
                   currentTime = robot.runtime.seconds();
               }
               break;
           case FLICKY_UP:
               if (robot.runtime.seconds() - currentTime >= 1) {
                   robot.flicky.setPosition(1);
                   currentState = launcherState.FLICKY_DOWN;
               }
               break;
           case FLICKY_DOWN:
               if (robot.runtime.seconds() - currentTime >= 2) {
                   goLaunch = false;
                   currentState = launcherState.STALL;
               }
               break;
        }

    }
    private void setPerfectLaunchSpeed(double distance) {
        targetVelocity = (21.20299 * Math.pow(distance, 4))
                - (233.8409 * Math.pow(distance, 3))
                + (966.85113 * Math.pow(distance, 2))
                - (1610.42433 * Math.pow(distance, 1))
                + 2186.7502;
        robot.launcherMotorOne.setVelocity(targetVelocity);
        robot.launcherMotorTwo.setVelocity(targetVelocity);
    }
}
