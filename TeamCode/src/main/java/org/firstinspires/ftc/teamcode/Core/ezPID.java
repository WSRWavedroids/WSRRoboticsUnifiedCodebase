package org.firstinspires.ftc.teamcode.Core;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;


@Configurable
public class ezPID {
    /// A PID controller maintains the speed or position of a motor, allowing for consistent movements
    /// EzPID simplifies custom PID controllers and cleans up usage
    /// To use an ezPID, just make an instance in the script using it and pass in the PIDF values
    /// kneecap values, encoder resolution, and a speed or position mode into the constructor.
    /// Then, just call run inside of another script's loop, using the instance you made there
    ///
    /// This script was written by Clay Kramer FTC 13206, 2025

    private Robot robot;
    private DcMotorEx motor;

    private PIDMotorGroup motorGroup;
    public double  p;
    public double i;
    public double d;
    public double f;
    public double kneecap;
    public ElapsedTime Timer;
    public double lastError;
    public double integralSum;
    public double ticksPerRotation;
    public double tolerance;
    public boolean withinTolerance;

    public enum movementType{SPEED,POSITION}

    movementType mode;

    int numberOfMotorsInGroup;

    public ezPID(DcMotorEx motorIn, double ticksPerRotationIN, double inP, double inI, double inD, double inF, double kneecapIN, double toleranceIN, movementType modeIN) {
        motor = motorIn;
        p = inP;
        i = inI;
        d = inD;
        f = inF;
        kneecap = kneecapIN;
        Timer = new ElapsedTime();
        ticksPerRotation = ticksPerRotationIN;
        mode = modeIN;
        tolerance = toleranceIN;
        withinTolerance = false;

    }

    public ezPID(PIDMotorGroup motorsIN, double ticksPerRotationIN, double inP, double inI, double inD, double inF, double kneecapIN, double toleranceIN, movementType modeIN) {
        motorGroup = motorsIN;
        p = inP;
        i = inI;
        d = inD;
        f = inF;
        kneecap = kneecapIN;
        Timer = new ElapsedTime();
        ticksPerRotation = ticksPerRotationIN;
        mode = modeIN;
        tolerance = toleranceIN;
        withinTolerance = false;

    }




    public PIDDUMP shareInfo() {
        /// This function dumps it's info for another PID to use
        PIDDUMP temp = new PIDDUMP() ;
        temp.lastErrorFromOld = lastError;
        temp.timerFromOld = Timer;
        temp.integralFromOld = integralSum;
        return temp;
    }

    public void grabInfoFromPID(PIDDUMP otherPIDINFO)
    {
        /// This function grabs dumped info to allow for seamless controller switching
        lastError = otherPIDINFO.lastErrorFromOld;
        Timer = otherPIDINFO.timerFromOld;
        integralSum = otherPIDINFO.lastErrorFromOld;
    }


    
    public void runCalledPID(double reference)
    {
        /// CALL THIS ONCE PER LOOP IN ANOTHER SCRIPT
        /// PIDNAME.runCalledPID(Target position(Ticks) or speed (Ticks/s));

        if(mode == movementType.POSITION)
        {

            // obtain the encoder position
            double encoderPosition = motor.getCurrentPosition();
            // calculate the error
            double error = reference - encoderPosition;

            if(Math.abs(error) > tolerance)
            {
                withinTolerance = false;
            }
            else
            {
                withinTolerance = true;
            }

            double derivative;
            // rate of change of the error
            if(Timer.seconds()!= 0)
            {
                derivative = (error - lastError) / Timer.seconds();
            }
            else
            {
                derivative = (error - lastError);
            }


            double feedforward = f * reference;

            if(Timer.seconds() != 0)
            {
                // sum of all error over time
                integralSum = integralSum + (error * Timer.seconds());
            }

            double out = kneecap * ((p * error) + (i * integralSum) + (d * derivative) + feedforward);

            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setPower(out);

            lastError = error;

            // reset the timer for next time
            Timer.reset();

        }
        else if(mode == movementType.SPEED)
        {
            // obtain the encoder position
            double encoderSpeed = motor.getVelocity();
            // calculate the error
            double error = reference - encoderSpeed;

            if(Math.abs(error) > tolerance)
            {
                withinTolerance = false;
            }
            else
            {
                withinTolerance = true;
            }

            // rate of change of the error
            double derivative = (error - lastError) / Timer.seconds();

            // sum of all error over time
            integralSum = integralSum + (error * Timer.seconds());

            double out = (p * error) + (i * integralSum) + (d * derivative);

            motor.setPower(out);

            lastError = error;

            // reset the timer for next time
            Timer.reset();
        }
    }


public void runCalledPIDGroup(double reference)
{
    /// CALL THIS ONCE PER LOOP IN ANOTHER SCRIPT
    /// PIDNAME.runCalledPID(Target position(Ticks) or speed (Ticks/s));
    for (int i = 0; i < motorGroup.motorGroup.length; i++)
    {
        if(mode == movementType.POSITION)
        {

            // obtain the encoder position
            double encoderPosition = motor.getCurrentPosition();
            // calculate the error
            double error = reference - encoderPosition;

            if(Math.abs(error) > tolerance)
            {
                withinTolerance = false;
            }
            else
            {
                withinTolerance = true;
            }

            double derivative;
            // rate of change of the error
            if(Timer.seconds()!= 0)
            {
                derivative = (error - lastError) / Timer.seconds();
            }
            else
            {
                derivative = (error - lastError);
            }


            double feedforward = f * reference;

            if(Timer.seconds() != 0)
            {
                // sum of all error over time
                integralSum = integralSum + (error * Timer.seconds());
            }

            double out = kneecap * ((p * error) + (i * integralSum) + (d * derivative) + feedforward);

            motorGroup.motorGroup[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motorGroup.motorGroup[i].setPower(out);

            lastError = error;

            // reset the timer for next time
            Timer.reset();

        }
        else if(mode == movementType.SPEED)
        {
            // obtain the encoder position
            double encoderSpeed = motor.getVelocity();
            // calculate the error
            double error = reference - encoderSpeed;

            if(Math.abs(error) > tolerance)
            {
                withinTolerance = false;
            }
            else
            {
                withinTolerance = true;
            }

            // rate of change of the error
            double derivative = (error - lastError) / Timer.seconds();

            // sum of all error over time
            integralSum = integralSum + (error * Timer.seconds());

            double out = (p * error) + (i * integralSum) + (d * derivative);

            motorGroup.motorGroup[i].setPower(out);

            lastError = error;

            // reset the timer for next time
            Timer.reset();
        }
    }
    }

}

class PIDDUMP {
    ElapsedTime timerFromOld;
    double lastErrorFromOld;
    double integralFromOld;
}












