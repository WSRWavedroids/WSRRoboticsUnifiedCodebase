package org.firstinspires.ftc.teamcode.lessons.eight;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.Robot;

/**
 * This file is our iterative (Non-Linear) "OpMode" for TeleOp.
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is selected on the Robot Controller and executed.
 * This OpMode controls the functions of the robot during the driver-controlled period.
 * <p>
 * If the "@Disabled" line is not commented out, the program will not show up on the driver hub.
 * If you ever have problems with the program not showing up on the driver hub, it's probably because of that.
 * <p>
 */

@TeleOp(name = "Pink_Banana_Op", group = "Templates")
public class Pink_Banana_Op extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    public Robot robot = null;

    private double motorStartTime = 0;

    private boolean motorIsRunning = false;

    private enum Step{
        NOT_RUNNING, CLOCKWISE, COUNTERCLOCKWISE, FAST_COUNTERCLOCKWISE
    }
    private Step step = Step.NOT_RUNNING;
    /*
     * Code to run ONCE when the driver hits INIT
     */
    public void init() {
        // Call the initialization protocol from the Robot class.
        robot = new Robot(hardwareMap, telemetry, this);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    public void init_loop() {
        telemetry.addData("HYPE", "ARE! YOU! READY?!?!?!?!");
        telemetry.update();
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    public void start() {
        runtime.reset();
        telemetry.addData("HYPE", "Let's do this!!!");
        gamepad1.setLedColor(0, 0, 255, 10);
        gamepad2.setLedColor(0, 0, 255, 10);

    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {
        switch (step) {
            case NOT_RUNNING:
                if (gamepad1.crossWasPressed()) {
                    robot.motor.setPower(0.5);
                    motorStartTime = runtime.seconds();
                    step = Step.COUNTERCLOCKWISE;
                }
                break;
            case COUNTERCLOCKWISE:
                if (runtime.seconds() - motorStartTime >= 2){
                    robot.motor.setPower(-0.5);
                    motorStartTime = runtime.seconds();
                    step = Step.CLOCKWISE;
                }
                break;
            case CLOCKWISE:
            if (runtime.seconds() - motorStartTime >= 2){
                robot.motor.setPower(0.5);
                motorStartTime = runtime.seconds()
            }
            break;
            case Step.FAST_COUNTERCLOCKWISE:
                // Step 4 code
                break;
        }
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {

    }
}


