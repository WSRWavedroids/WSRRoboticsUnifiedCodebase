package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Intake.IntakeSteps.*;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake {
    public Robot robot;

    public Intake(Robot robot) {
        this.robot = robot;
    }
    public enum IntakeSteps {
        READY_FOR_COMMANDS, INTAKE, OUTTAKE, FULL
    }
    public IntakeSteps intakeStep = READY_FOR_COMMANDS;
    public boolean fullInventoryCheck = true;
    private double currentTime = 0;
    public void update() {
        switch (intakeStep) {
            case READY_FOR_COMMANDS:
                robot.feedServo.setPower(1);
                robot.intakeMotor.setPower(0);
                if (robot.artifactLocator.getInventory(Robot.BallColor.ANY) == 3) {
                    intakeStep = FULL;
                    currentTime = robot.runtime.seconds();
                }
                break;
            case INTAKE:
                robot.blender.rotateSlotToPosition(
                        robot.artifactLocator.findColor(Robot.BallColor.EMPTY),
                        Blender.SlotPositions.INTAKE
                );
                robot.feedServo.setPower(1);
                robot.intakeMotor.setPower(1);
                fullInventoryCheck = true;
                break;
            case OUTTAKE:
                robot.feedServo.setPower(-1);
                robot.intakeMotor.setPower(-1);
                break;
            case FULL:
                robot.feedServo.setPower(1);
                robot.intakeMotor.setPower(-1);
                if (robot.runtime.seconds() - currentTime >= 1) {
                    intakeStep = READY_FOR_COMMANDS;
                    fullInventoryCheck = false;
                }
                break;

        }
    }

}
