package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.Blender.SlotNames.NONE;
import static org.firstinspires.ftc.teamcode.Core.Blender.SlotNames.SLOT_1;
import static org.firstinspires.ftc.teamcode.Core.Blender.SlotNames.SLOT_2;
import static org.firstinspires.ftc.teamcode.Core.Blender.SlotNames.SLOT_3;
import static org.firstinspires.ftc.teamcode.Core.Robot.BallColor.EMPTY;
import static org.firstinspires.ftc.teamcode.Core.Robot.BallColor.GREEN;

public class ArtifactLocator {
    private Robot robot;
    private Robot.BallColor[] colorList = {
            EMPTY, EMPTY, EMPTY
    };

    public ArtifactLocator(Robot robot) {
        this.robot = robot;
    }

    public void storeColor(Robot.BallColor color, int slotNumber) {
        colorList[slotNumber] = color;
    }
    public Blender.SlotNames findColor(Robot.BallColor ballColor) {
        if (colorList[0] == ballColor) {
            return SLOT_1;
        }
        if (colorList[1] == ballColor) {
            return SLOT_2;
        }
        if (colorList[2] == ballColor) {
            return SLOT_3;
        }
        return NONE;
    }
    public Robot.BallColor findSlotContents(Blender.SlotNames slot) {
        if(slot == SLOT_1){
            return colorList[0];
        }
        if(slot == SLOT_2) {
            return colorList[1];
        }
        if(slot == SLOT_3){
            return colorList[2];
        } else {{
            return EMPTY;
        }}
    }
    public int getInventory(Robot.BallColor color){

        }

    }
}
