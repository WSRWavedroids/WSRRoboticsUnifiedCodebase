/*
 * Copyright (c) 2024 Phil Malone
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.*;
import static org.firstinspires.ftc.teamcode.Core.BetaSorterHardware.positionState.*;

import java.util.ArrayList;

public class ThreePositionArtifactLocator extends ArtifactLocator {

    //private ExposureControl exposureControl;
    //private GainControl gainControl;

    //private ColorBlobLocatorProcessor purpleLocator;
    //private ColorBlobLocatorProcessor greenLocator;
    //private VisionPortal portal;
    //private List<ColorBlobLocatorProcessor.Blob> purpleBlobList;
    //private List<ColorBlobLocatorProcessor.Blob> greenBlobList;
    //public enum SlotState {EMPTY, PURPLE, GREEN, UNKNOWN}

    public Slot slotA;
    public Slot slotB;
    public Slot slotC;
    public Slot noSlot;
    public Zone zone1;
    public Zone zone2;
    public Zone zone3;
    public Zone zone4;
    public Zone zone5;
    public Zone zone6;
    public ArrayList<Slot> allSlots = new ArrayList<>();
    public ArrayList<Zone> allZones = new ArrayList<>();
    public ArrayList<Integer> offsetPositions = new ArrayList<>();
    public SlotInventory inventory;

    public Robot robot;

    public ThreePositionArtifactLocator(Robot robot) {
        super(robot);
        this.robot = robot;
        initLogic();
    }

    /**
     * Initializes numerous logic-related objects, including slots, the inventory, and lists to
     * iterate through them.
     */
    public void initLogic() {
        double ticksPerRotation = BetaSorterHardware.ticksPerRotation;
        // Define slots
        slotA = new Slot(0, (int) (ticksPerRotation / 3), "A");
        slotB = new Slot((int) (2 * ticksPerRotation / 3), 0, "B");
        slotC = new Slot((int) (ticksPerRotation / 3), (int) (2 * ticksPerRotation / 3), "C");
        noSlot = new NoSlot();

        zone1 = new Zone(140, 180, 0, 120);
        zone2 = new Zone(0, 160, 0, 120);
        zone3 = new Zone(0, 160, 120, 240);

        //Sort things into lists
        offsetPositions.add(0, slotA.getLoadPosition());
        offsetPositions.add(1, slotC.getLoadPosition());
        offsetPositions.add(2, slotB.getLoadPosition());

        allSlots.add(slotA); allSlots.add(slotB); allSlots.add(slotC);

        allZones.add(zone1); allZones.add(zone2); allZones.add(zone3);

        // Define the inventory
        inventory = new SlotInventory();
    }

    /**
     * Updates the Inventory
     */
    public void update() {
        // Read the current list
        this.takeInventory();
    }

    /**
     * Sorts the HushyLens output into the appropriate Slot.
     * @param state The HuskyLens ID. 1 = PURPLE, 2 = GREEN, 3 = EMPTY.
     */
    public void sortOutBlobs(int state) {
        SlotState newState;

        switch (state) {
            case 1: newState = PURPLE; break;
            case 2: newState = GREEN;  break;
            case 3: newState = EMPTY;  break;
            default: newState = UNKNOWN;
        }

        this.findCurrentSlotInPosition(LOAD).setOccupied(newState);
    }

    /**
     * Totals the number of Artifacts stored in the blender and updates the inventory class.
     */
    private void takeInventory() {
        int currentPurpleCount = 0;
        int currentGreenCount = 0;
        for (Slot currentSlot : allSlots) {
            switch (currentSlot.getOccupied()) {
                case EMPTY:
                    break;
                case PURPLE:
                    currentPurpleCount ++;
                    break;
                case GREEN:
                    currentGreenCount ++;
                    break;
            }
        }
        inventory.updateInventory(currentPurpleCount, currentGreenCount);
    }

    /**
     * Searches the slots in order of ABC to find the first Slot containing the indicated contents.
     * @param slotType The target state of the Slot. Can be EMPTY, GREEN, PURPLE, or UNKNOWN
     *                 in the form of a SlotState enum.
     * @return The first found Slot filled with the indicated slotType.
     */
    public Slot findFirstType(SlotState slotType) {
        for (Slot currentSlot : allSlots) {
            if (currentSlot.contains(slotType)) {
                return currentSlot;
            }
        }
        return noSlot;
    }


    /**
     * Searches the slots in order of ABC to find the first Slot containing a ball.
     *                 in the form of a SlotState enum.
     * @return The first found Slot containing a ball
     */
    public Slot findFirstOccupied() {
        for (Slot currentSlot : allSlots) {
            if (currentSlot.contains(GREEN) || currentSlot.contains(PURPLE)) {
                return currentSlot;
            }
        }
        return noSlot;
    }


    /**
     * Searches the slots in order of ABC to find the first Slot that is known and does not contain
     * the specified contents. If the input is UNKNOWN, it will find the first known Slot.
     * @param slotType The not-target state of the Slot. Can be EMPTY, GREEN, PURPLE, or UNKNOWN in
     *                 the form of a SlotState enum.
     * @return The first found Slot.
     */
    public Slot findFirstNotType(SlotState slotType) {
        for (Slot currentSlot : allSlots) {
            if (currentSlot.doesNotContain(slotType, UNKNOWN)) {
                return currentSlot;
            }
        }
        return noSlot;
    }

    /**
     * Uses the current offset to find which Slot is in the specified Zone.
     * @param zone The target Zone.
     * @return The found Slot.
     */
    public Slot findSlotByZone(Zone zone) {
        int offset = getCurrentOffset();
        if (offset == -1) {
            return noSlot;
        }
        int zoneIndex = allZones.indexOf(zone) + 1;
        int x = zoneIndex - offset;

        switch (x) {
            case 1:
                return slotA;
            case 2:
                return slotB;
            case 3:
                return slotC;
        }
        return noSlot;
    }

    /**
     * Finds a Slot in the FIRE or LOAD position, if there is one.
     * @param targetPosition FIRE or LOAD. FIRE is the position ready to launch, and LOAD is the
     *                       load position.
     * @return A Slot, if there's one in position. If there isn't, will return noSlot.
     */
    public Slot findCurrentSlotInPosition(BetaSorterHardware.positionState targetPosition) {
        Slot foundLoadSlot;
        Slot foundFireSlot;

        switch (getCurrentOffset()) {
            case 0:  foundLoadSlot = slotA; foundFireSlot = slotB; break;
            case 1:  foundLoadSlot = slotB; foundFireSlot = slotC; break;
            case 2:  foundLoadSlot = slotC; foundFireSlot = slotA; break;
            default: foundLoadSlot = noSlot; foundFireSlot = noSlot;
        }

        if (targetPosition == LOAD) {
            return foundLoadSlot;
        } else if (targetPosition == FIRE) {
            return foundFireSlot;
        } else return noSlot;
    }

    //TODO Move to LauncherHardware???
    /**
     * Uses the blender encoder and offsetPositions to calculate which offset position the blender
     * is currently in.
     * @return The current offset, 0-5. -1 means it's in transit.
     */
    public int getCurrentOffset() {
        if (robot.sorterHardware.positionedCheck()) {
            return findClosestOffset(robot.sorterHardware.motor.getCurrentPosition());
        } else {
            return -1;
        }
    }

    //TODO Move to LauncherHardware??? Maybe replace it with magnets.
    /**
     * Compares the current motor position to offsetPositions to
     * calculate which offset position the blender is closest to.
     * @param ticks The current position of the encoder
     * @return The current nearest blender offset, from 0-2. -2 = error.
     */
    private int findClosestOffset(int ticks) {
        ticks = equalizeMotorPositions(ticks);

        int lowestDistance = 1000000000;
        int offset = -2;
        int currentDistanceCheck;

        for(int i = 0; i < 3; i++) {
            currentDistanceCheck = Math.abs(offsetPositions.get(i) - ticks);
            if (currentDistanceCheck < lowestDistance) {
                lowestDistance = currentDistanceCheck;
                offset = i;
            }
        }
        // Check the high offset 0
        currentDistanceCheck = (int) Math.abs(BetaSorterHardware.ticksPerRotation - ticks);
        if (currentDistanceCheck < lowestDistance) {
            offset = 0;
        }

        return offset;
    }

    /**
     * Does some basic math to equalize a motor position between 0 and
     * SorterHardware.ticksPerRotation (8192).
     * @param ticks The number to be equalized.
     * @return The equalized position, between 0-8192.
     */
    public int equalizeMotorPositions(int ticks) {
        while (ticks > BetaSorterHardware.ticksPerRotation) {
            ticks -= BetaSorterHardware.ticksPerRotation;
        }
        while (ticks < 0) {
            ticks += BetaSorterHardware.ticksPerRotation;
        }
        return ticks;
    }

    public boolean isCurrentReferenceLogical(int reference) {
        return reference % ((double) BetaSorterHardware.ticksPerRotation / 6) == 0;
    }
}