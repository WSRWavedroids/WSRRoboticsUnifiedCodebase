package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.AZURE;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.BLUE;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.GREEN;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.INDIGO;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.ORANGE;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.RED;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.SAGE;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.VIOLET;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.WHITE;
import static org.firstinspires.ftc.teamcode.Core.LED.LEDColors.YELLOW;

import com.qualcomm.robotcore.hardware.Servo;

public class LED {
    private final Servo ledInterface;
    public boolean ledIsOn = false;

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
    }

    public void setColor(LEDColors color) {
        ledInterface.setPosition(color.colorOutputValue);
        if (color == LEDColors.OFF) {
            ledIsOn = false;
        } else {
            ledIsOn = true;
        }
    }

    public void setPosition(double colorOutputValue) {
        ledInterface.setPosition(colorOutputValue);
        if (colorOutputValue == 0) {
            ledIsOn = false;
        } else {
            ledIsOn = true;
        }
    }

    public void setRandomColor() {
        double color = Math.round(Math.random() * 10);
        if (color == 0) {
            setColor(RED);
        }
        if (color == 1) {
            setColor(ORANGE);
        }
        if (color == 2) {
            setColor(YELLOW);
        }
        if (color == 3) {
            setColor(SAGE);
        }
        if (color == 4) {
            setColor(GREEN);
        }
        if (color == 5) {
            setColor(AZURE);
        }
        if (color == 6) {
            setColor(BLUE);
        }
        if (color == 7) {
            setColor(INDIGO);
        }
        if (color == 8) {
            setColor(VIOLET);
        }
        if (color == 9) {
            setColor(WHITE);
        }

    }
}
