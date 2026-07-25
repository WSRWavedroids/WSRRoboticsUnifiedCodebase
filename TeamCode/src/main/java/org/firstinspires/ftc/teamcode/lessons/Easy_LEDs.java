package org.firstinspires.ftc.teamcode.lessons;


import com.qualcomm.robotcore.hardware.Servo;

public class Easy_LEDs {
    public enum Color{OFF, RED, ORANGE, YELLOW, SAGE, GREEN, AZURE, BLUE, INDIGO, VIOLET, WHITE}
    public double colorValue;
    public Easy_LEDs(Servo led) {
        this.led = led;
    }
    public Servo led;
    public void setColor(Color color){
        if (color == Color.OFF){
            led.setPosition(0.0);
        }
        else if(color == Color.RED){
            led.setPosition(0.277);
        }
        else if(color == Color.ORANGE){
            led.setPosition(0.333);
        }
        else if(color == Color.YELLOW){
            led.setPosition(0.388);
        }
        else if(color == Color.SAGE){
            led.setPosition(0.444);
        }
        else if(color == Color.GREEN){
            led.setPosition(0.500);
        }
        else if(color == Color.AZURE){
            led.setPosition(0.555);
        }
        else if(color == Color.BLUE){
            led.setPosition(0.611);
        }
        else if(color == Color.INDIGO){
            led.setPosition(0.666);
        }
        else if(color == Color.VIOLET){
            led.setPosition(0.722);
        }
        else if(color == Color.WHITE){
            led.setPosition(1.0);
        }
    }

    public void setPosition(double colorValue){
        led.setPosition(colorValue);
    }
}
