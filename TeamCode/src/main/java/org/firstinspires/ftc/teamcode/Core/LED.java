package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.VIOLET;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LED {
    private final Servo ledInterface;
    public LED(Servo ledFromHardwareMap) {
        ledInterface = ledFromHardwareMap;
    }

    public enum LEDColors {
        OFF(0),
        RED(0.277),
        ORANGE(0.333),
        YELLOW(0.388),
        SAGE(0.444),
        GREEN(0.5),
        AZURE(0.555),
        BLUE(0.611),
        INDIGO(0.666),
        VIOLET(0.722),
        WHITE(1);
        public final double colorOutputValue;

        LEDColors(double servoOutputValue) {
            this.colorOutputValue = servoOutputValue;
        }


        // Cache the array and size to avoid garbage collection overhead
        //private static final LEDColors[] VALUES = values();
        //private static final int SIZE = VALUES.length;
        private static final Random RANDOM = new Random();
        public static LEDColors randomColor () {
            //return VALUES[RANDOM.nextInt(SIZE)];
            int rand =  RANDOM.nextInt(10);
            if ( rand == 0){
                return RED;
            } else if ( rand == 1){
                return ORANGE;
            } else if ( rand == 2){
                return YELLOW;
            } else if ( rand == 3){
                return SAGE;
            } else if ( rand == 4){
                return GREEN;
            } else if ( rand == 5){
                return AZURE;
            } else if ( rand == 6){
                return BLUE;
            } else if ( rand == 7){
                return INDIGO;
            } else if ( rand == 8){
                return VIOLET;
            } else {
                return WHITE;
            }
        }
    }
    public void setColor(LEDColors color) {
        ledInterface.setPosition(color.colorOutputValue);
    }

    public void setPosition(double colorOutputValue) {
        ledInterface.setPosition(colorOutputValue);
    }
}
