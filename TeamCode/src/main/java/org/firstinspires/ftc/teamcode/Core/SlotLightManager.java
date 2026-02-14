package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.*;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class SlotLightManager {

    private final Robot robot;
    private final ElapsedTime blinkyTimer;
    private final ElapsedTime timeSinceUpdate;
    public Light fireLight;
    public Light loadLight;
    public Light storeLight;

    public SlotLightManager(Robot robot) {
        this.robot = robot;
        fireLight = new Light(FIRE, robot.fireRGB);
        loadLight = new Light(LOAD, robot.loadRGB);
        storeLight = new Light(STORE, robot.storeRGB);

        blinkyTimer = new ElapsedTime();
        timeSinceUpdate = new ElapsedTime();
    }

    public void update() {
        if (timeSinceUpdate.seconds() >= 0.5) {
            fireLight.update();
            loadLight.update();
            storeLight.update();
            timeSinceUpdate.reset();
        }
    }

    public class Light {
        SorterHardware.PositionState position;
        Servo light;
        boolean blinky;
        public Light(SorterHardware.PositionState position, Servo light) {
            this.position = position;
            this.light = light;
        }

        public void update() {
            final int hertz = 2;
            if (blinky && ((int) blinkyTimer.seconds()) * hertz % (hertz * 2) == 0) {
                light.setPosition(0);
            } else {
                light.setPosition(robot.sorterLogic.findCurrentSlotInPosition(position)
                        .getOccupied()
                        .lightColor);
            }
        }

        public void blinkify(boolean blink) {
            blinky = blink;
        }
    }
}
