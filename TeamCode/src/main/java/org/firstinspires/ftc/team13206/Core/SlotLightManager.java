package org.firstinspires.ftc.team13206.Core;

import static org.firstinspires.ftc.team13206.Core.SorterHardware.PositionState.*;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class SlotLightManager {

    private final Robot robot;
    private final ElapsedTime timeSinceUpdate;
    public Light fireLight;
    public Light loadLight;
    public Light storeLight;

    public SlotLightManager(Robot robot) {
        this.robot = robot;
        fireLight = new Light(FIRE, robot.fireRGB);
        loadLight = new Light(LOAD, robot.loadRGB);
        storeLight = new Light(STORE, robot.storeRGB);

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
        public Light(SorterHardware.PositionState position, Servo light) {
            this.position = position;
            this.light = light;
        }

        public void update() {
            light.setPosition(robot.sorterLogic.findCurrentSlotInPosition(position)
                    .getOccupied()
                    .lightColor);
        }
    }
}
