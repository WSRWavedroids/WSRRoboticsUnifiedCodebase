package org.firstinspires.ftc.teamcode.Core;

import android.icu.text.Transliterator;

import com.qualcomm.robotcore.hardware.Servo;

public class LED {
    public enum SetColor {OFF, RED, ORANGE, YELLOW, SAGE, GREEN, AZURE, BLUE, INDIGO, VIOLET, WHITE}

    public Servo servo;
    public LED(Servo servo) {
        this.servo = servo;
    }

    public void setColor(SetColor color){
        switch (color){
            case OFF: {
                servo.setPosition(0);
            }
            case RED: {
                servo.setPosition(0.277);
            }
            case ORANGE: {
                servo.setPosition(0.333);
            }
            case YELLOW: {
                servo.setPosition(0.388);
            }
            case SAGE: {
                servo.setPosition(0.444);
            }
            case GREEN: {
                servo.setPosition(0.5);
            }
            case AZURE: {
                servo.setPosition(0.555);
            }
            case BLUE: {
                servo.setPosition(0.611);
            }
            case INDIGO: {
                servo.setPosition(0.666);
            }
            case VIOLET: {
                servo.setPosition(0.722);
            }
            case WHITE: {
                servo.setPosition(1);
            }
        }

    }
    public void setPosition(double position){
        servo.setPosition(position);
    }

}
