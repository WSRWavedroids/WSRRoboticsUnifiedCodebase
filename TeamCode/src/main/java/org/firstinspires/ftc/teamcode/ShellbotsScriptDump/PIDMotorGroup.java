package org.firstinspires.ftc.teamcode.ShellbotsScriptDump;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Core.Robot;


@Configurable
public class PIDMotorGroup {
    /// This script was written to work with ezPID.java
    /// To use this script, make an instance of ezPID like normal, but pass in a motor group instead
    /// then call setDirections and make an array of -1 and 1 for R and F
    /// Change your directions array before calling movement to determine motor behavior
    ///
    /// This script was written by Clay Kramer, FTC 13206 🌊🤖, 2025

    private Robot robot;
    private DcMotorEx motor;


    int numberOfMotorsInGroup;
    DcMotorEx[] motorGroup;

    public PIDMotorGroup(int numberOfMotorsIN, DcMotorEx[] motorsIN) {
        numberOfMotorsInGroup = numberOfMotorsIN;
        motorGroup = new DcMotorEx[numberOfMotorsIN];
    }

    void setDirections(int[] listOfDirectionsIN)
    {
        for (int i = 0; i < listOfDirectionsIN.length; i++)
        {
            if(listOfDirectionsIN[i] == -1)
            {
               motorGroup[i].setDirection(DcMotorSimple.Direction.REVERSE);
            }
            else
            {
                motorGroup[i].setDirection(DcMotorSimple.Direction.FORWARD);
            }
        }
    }




    }












