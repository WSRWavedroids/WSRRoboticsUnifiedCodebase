package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous(group =  "Auto", name = "BlueOnLaunchZoneANGLED")
public class BlueOnLaunchZoneANGLED extends AutonomousPLUS {

    private Robot robot;

    @Override
    public void runOpMode() {
        super.runOpMode();
        robot = new Robot(hardwareMap, telemetry, this);

        waitForStart();
        //Under This Is Were You Put Stuff
        //900 tick = about 20 in
        //550 ticks = about 90 degrees right
        //6 millisecond pause after everything
        moveRobotBackward(1400, 6, 0.4);
        runLauncherAuto(0.32);
        sleep(1500);
        runIntake2Auto(0.5);
        sleep(1000);
        runIntakeAuto("In");
        sleep(3000);
        prepareNextAction(4);
        runLauncherAuto(0);
        runIntake2Auto(0);
        runIntakeAuto("Stop");
        prepareNextAction(6);
        moveRobotLeft(1200,6,0.2);
        //turnRobotLeft(1650, 6);
        //moveRobotLeft(900,6,0.4);
        //runIntakeAuto(In);          //this is intake
        //moveRobotForward(750,6,40);
        //sleep(2000);
        //runIntakeAuto(Stop);
        //moveRobotBackward(750,6,40);         //this is launch
        //moveRobotLeft(450,6,40);
        //turnRobotRight(1650,6,40);
        //runLauncherAuto(0.7);
        //sleep(1500);
        //runIntake2Auto(1);
        //runIntakeAuto(In);
        //sleep(4000);
        //runLauncherAuto(0);
        //runIntake2Auto(0);
        //runIntakeAuto(Stop);
    }
}
//whatever i want
//Jarett Kaedan Butler was here