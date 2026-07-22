package org.firstinspires.ftc.teamcode.lessons.three;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.Robot;

/**
 * This is an iterative autonomous program. It runs in a state machine, which allows us to run the
 * updateAllDaThings() function and properly run the blender without any... questionable code. And
 * it's marginally more efficient. Also, this contains the proper code to run the red alliance auto
 * too, allowing us to keep both autos up to date in a single file. BetaRedFrontAuto is a shell that
 * basically just hijacks this file to work, which is neat.
 */

@Autonomous(group = "3", name = "Functions")
public class Functions extends OpMode {

    public Robot robot;

    private ElapsedTime runtime = new ElapsedTime();

    void nextStep(Steps nextStep) {
        currentStep = nextStep;
    }

    Steps currentStep;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    public void init() {
        robot = new Robot(hardwareMap, telemetry, this);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    public void init_loop() {
        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
        telemetry.addData("HYPE", "ARE! YOU! READY?!?!?!?!");
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    public void start() {
        telemetry.addData("HYPE", "Let's do this!!!");
    }

    /**
     * The steps that happen within the main loop.
     */
    enum Steps {
        EXAMPLE, EXAMPLE_2
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    public void loop() {
        telemetry.addData("color", robot.getColor());
        telemetry.update();

    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {
        telemetry.addData("Status", "Robot Stopped");
    }
}


