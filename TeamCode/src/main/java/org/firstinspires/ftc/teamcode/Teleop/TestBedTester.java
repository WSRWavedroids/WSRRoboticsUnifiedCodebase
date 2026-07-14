package org.firstinspires.ftc.teamcode.Teleop;

import static android.os.SystemClock.sleep;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * This file is our iterative (Non-Linear) "OpMode" for TeleOp.
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode
 * class is selected on the Robot Controller and executed.
 * This OpMode controls the functions of the robot during the driver-controlled period.
 * <p>
 * If the "@Disabled" tag is present, the program will not show up on the driver hub.
 * If you ever have problems with the program not showing up on the driver hub, it's probably
 * because of that.
 * <p>
 */

@TeleOp(name = "Test Bed Tester", group = "0.")
public class TestBedTester extends OpMode {

    // This section tells the program all of the different pieces of hardware that are on our robot
    // that we will use in the program.
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor motor;
    private Servo axonServo;
    private CRServo colorServo;
    private TouchSensor button;
    private ColorSensor colorSensor;
    private TouchSensor magnetSensor;
    private DistanceSensor distanceSensor;
    private Servo led;

    private double ledColorValue = 0;
    private final double colorIncrement = 0.01;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "Motor");
        axonServo = hardwareMap.get(Servo.class, "Axon");
        colorServo = hardwareMap.get(CRServo.class, "Color Servo");
        button = hardwareMap.get(TouchSensor.class, "Button");
        colorSensor = hardwareMap.get(ColorSensor.class, "Color Sensor");
        magnetSensor = hardwareMap.get(TouchSensor.class, "Magnet Sensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "Distance Sensor");
        led = hardwareMap.get(Servo.class, "LED");

        motor.setMode(STOP_AND_RESET_ENCODER);
        sleep(500);
        motor.setMode(RUN_WITHOUT_ENCODER);

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
        motor.setPower(gamepad1.left_stick_x);

        colorServo.setPower(gamepad1.right_stick_x);

        if (gamepad1.squareWasPressed()) {
            axonServo.setPosition(0);
        } else if (gamepad1.triangleWasPressed()) {
            axonServo.setPosition(0.5);
        } else if (gamepad1.circleWasPressed()) {
            axonServo.setPosition(1);
        }

        ledColorValue += colorIncrement;
        if (ledColorValue >= 1) ledColorValue -= 1;
        led.setPosition(ledColorValue);

        telemetry.addData("Motor Position", motor.getCurrentPosition());
        telemetry.addData("Button Pressed", button.isPressed());
        telemetry.addLine("Color Sensor Values:");
        telemetry.addData("R", colorSensor.red());
        telemetry.addData("G", colorSensor.green());
        telemetry.addData("B", colorSensor.blue());
        telemetry.addData("Magnet Detected", magnetSensor.isPressed());
        telemetry.addData("Distance Sensor (inches)", distanceSensor.getDistance(DistanceUnit.INCH));
        telemetry.update();
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    public void stop() {
        telemetry.addLine("Robot Stopped");
        telemetry.update();
    }
}


