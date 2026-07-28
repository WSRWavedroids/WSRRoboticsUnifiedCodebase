package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;

public class EasyLED {
    public enum Color { OFF, RED, ORANGE, YELLOW, SAGE, GREEN, AZURE, BLUE, INDIGO, VIOLET, WHITE }
    Servo LED;


    public EasyLED(Servo led){
        LED = led;
    }
    public void setColor(Color inputColor)
    {
      if (inputColor == Color.OFF)
      {
          setPosition(0.1);
      }
      else if (inputColor == Color.RED)
      {
          setPosition(0.277);
      }
      else if (inputColor == Color.ORANGE)
      {
          setPosition(0.333);
      }
      else if (inputColor == Color.YELLOW)
      {
          setPosition(0.388);
      }
      else if (inputColor == Color.SAGE)
      {
          setPosition(0.444);
      }
      else if (inputColor == Color.GREEN)
      {
          setPosition(0.500);
      }
      else if (inputColor == Color.AZURE)
      {
          setPosition(0.555);
      }
      else if (inputColor == Color.BLUE)
      {
          setPosition(0.611);
      }
      else if (inputColor == Color.INDIGO)
      {
          setPosition(0.666);
      }
      else if (inputColor == Color.VIOLET)
      {
          setPosition(0.722);
      }
      else if (inputColor == Color.WHITE)
      {
          setPosition(1.0);
      }
    }

    public void setPosition(double value)
    {
        LED.setPosition(value);
    }
}

