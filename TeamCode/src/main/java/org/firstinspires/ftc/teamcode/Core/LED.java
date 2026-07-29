package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.Servo;

public class LED_GIVER_THINGY {
    public LED.GIVER.THIGY(Servo led) {
        this.led = led;
    }
    private Servo led;

    public enum Color {ledoff, ledred, ledorange, ledyellow, ledsage, ledgreen,ledazure, ledblue, ledindigo, ledviolet, ledwhite};
    public double ledoff = 0;
    public double ledred = 0.227;
    public double ledorange = 0.333;
    public double ledyellow = 0.388;
    public double ledsage = 0.444;
    public double ledgreen = 0.500;
    public double ledazure = 0.555;
    public double ledblue = 0.611;
    public double ledindigo = 666;
    public double ledviolet = 0.722;
    public double ledwhite = 1;

    public void setColor(Color color) {
        if( color == Color.ledoff ) {
            led.setPosition(ledoff);
        }else if(color == Color.ledred ) {
            led.setPosition(ledred);
        }else if(color == Color.ledorange) {
            led.setPosition(ledorange);
        }else if(color == Color.ledyellow) {
            led.setPosition(ledyellow);
        }else if(color == Color.ledsage) {
            led.setPosition(ledsage);
        }else if(color == Color.ledgreen) {
            led.setPosition(ledgreen);
        }else if(color == Color.ledazure) {
            led.setPosition(ledazure);
        }else if(color == Color.ledblue) {
            led.setPosition(ledblue);
        } else if (color == Color.ledindigo) {
            led.setPosition(ledindigo);
        } else if (color == Color.ledviolet) {
            led.setPosition(ledviolet);
        } else if (color == Color.ledwhite) {
            led.setPosition(ledwhite);
        }
    }



    //note: robot.led.setPosition(ledoff);

}
