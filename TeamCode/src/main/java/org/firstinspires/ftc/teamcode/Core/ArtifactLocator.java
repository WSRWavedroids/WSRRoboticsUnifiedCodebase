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
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.*;

import android.graphics.Color;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

public class ArtifactLocator {
    public enum SlotState {
        EMPTY(1),
        PURPLE(0.71),
        GREEN(0.475),
        UNKNOWN(0);
        public final double lightColor;
        SlotState(double lightColor){
            this.lightColor = lightColor;
        }
    }

    public Slot slotA;
    public Slot slotB;
    public Slot slotC;
    public Slot noSlot;
    public ArrayList<Slot> allSlots = new ArrayList<>();

    public ArrayList<Integer> offsetPositions = new ArrayList<>();
    public SlotInventory inventory;

    public Robot robot;
    public SorterHardware sorterHardware;

    private ElapsedTime sortCooldown = new ElapsedTime();
    public double sortCooldownTime = 0;

    private final int slotALoad = 0;
    private final int slotBLoad = 2;
    private final int slotCLoad = 1;

    public ArtifactLocator(Robot robotFile) {
        robot = robotFile;
        this.sorterHardware = robot.sorterHardware;
        initLogic();
        leftResetTimer = new ElapsedTime();
        rightResetTimer = new ElapsedTime();
    }

    /**
     * Initializes numerous logic-related objects, including slots, the inventory, and lists to
     * iterate through them.
     */
    public void initLogic() {
        // Define slots
        slotA = new Slot(sorterHardware.positions[slotALoad], sorterHardware.positions[slotCLoad], "A");
        slotB = new Slot(sorterHardware.positions[slotBLoad], sorterHardware.positions[slotALoad], "B");
        slotC = new Slot(sorterHardware.positions[slotCLoad], sorterHardware.positions[slotBLoad], "C");
        noSlot = new NoSlot();

        //Sort things into lists
        offsetPositions.add(0, slotA.getLoadPosition());
        offsetPositions.add(1, slotC.getLoadPosition());
        offsetPositions.add(2, slotB.getLoadPosition());

        allSlots.add(slotA); allSlots.add(slotB); allSlots.add(slotC);

        // Define the inventory
        inventory = new SlotInventory();

    }

    /**
     * Queries the camera for the current blob lists, sorts them into slots, and updates the
     * inventory class.
     */
    public void update() {
        // Read the current list
        this.takeInventory();
        sortOutBlobs(runSideScannersWithHSV(), LOAD);
    }


    public double leftHue;
    public double rightHue;
    public double leftValue;
    public double rightValue;

    private ElapsedTime leftResetTimer, rightResetTimer;
    /**
     * Checks the color sensors
     * @return The SlotState contents, PURPLE, GREEN, or EMPTY
     */
    public ArtifactLocator.SlotState runSideScannersWithHSV()
    {
        double purpleMinHue = 170;
        double purpleMaxHue = 295;
        double purpleMinValue = 0.3;
        double purpleMaxValue = 1.2;


        double greenMinHue = 130;
        double greenMaxHue = 160;
        double greenMinValue = 0.3;
        double greenMaxValue = 1.2;

        float[] leftHSVValues = new float[3];
        float[] rightHSVValues = new float[3];
        Color.RGBToHSV(robot.leftColorScanner.red(), robot.leftColorScanner.green(), robot.leftColorScanner.blue(), leftHSVValues);
        Color.RGBToHSV(robot.rightColorScanner.red(), robot.rightColorScanner.green(), robot.rightColorScanner.blue(), rightHSVValues);

        if (leftHue != leftHSVValues[0] && leftValue != leftHSVValues[2]) {
            leftResetTimer.reset();
            leftHue = leftHSVValues[0];
            double leftSaturation = leftHSVValues[1];
            leftValue = leftHSVValues[2];
        }
        if (rightHue != rightHSVValues[0] && rightValue != rightHSVValues[2]) {
            rightResetTimer.reset();
            rightHue = rightHSVValues[0];
            double rightSaturation = rightHSVValues[1];
            rightValue = rightHSVValues[2];
        }

        if (leftResetTimer.seconds() < 10) {
            if (leftHue > purpleMinHue && leftHue < purpleMaxHue &&
                    leftValue > purpleMinValue && leftValue < purpleMaxValue) {
                return PURPLE;
            } else if (leftHue > greenMinHue && leftHue < greenMaxHue &&
                    leftValue > greenMinValue && leftValue < greenMaxValue) {
                return GREEN;
            }
        }

        if (rightResetTimer.seconds() < 10) {
            if(rightHue > purpleMinHue && rightHue < purpleMaxHue &&
                    rightValue > purpleMinValue && rightValue < purpleMaxValue) {
                return PURPLE;
            }
            else if(rightHue > greenMinHue && rightHue < greenMaxHue &&
                    rightValue > greenMinValue && rightValue < greenMaxValue) {
                return GREEN;
            }
        }

        return EMPTY;
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

        sortOutBlobs(newState, LOAD);
    }

    public void sortOutBlobs(SlotState newState, SorterHardware.PositionState position) {
        Slot loadSlot = this.findCurrentSlotInPosition(position);

        if(!loadSlot.contains(newState)) {
            loadSlot.setOccupied(newState);
            sortCooldown.reset();
        }
    }

    public boolean artifactSortCooldown() {
        return sortCooldown.seconds() >= sortCooldownTime;
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
     * searches for the nth ball of a certain type within the inventory.
     * Can be used to find a ball of type past the first of the type
     * @param slotType The target state of the Slot. Can be EMPTY, GREEN, PURPLE, or UNKNOWN
     *                 in the form of a SlotState enum.
     * @param nthBall The Artifact to search for
     * @return The found Slot
     */
    public Slot findXOfType(SlotState slotType, int nthBall)
    {
        int counter = 0;
        if((slotType == GREEN && inventory.greenCount >= nthBall) || (slotType == PURPLE && inventory.purpleCount >= nthBall)) {
            for (Slot currentSlot : allSlots) {
                if (currentSlot.contains(slotType)) {
                    counter++;
                    if (counter == nthBall) {
                        return currentSlot;
                    }
                }
            }
        }
        return noSlot;
    }


    /**
     * Searches the slots in order of ABC to find the first Slot containing an Artifact.
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
     * Finds a Slot in the FIRE or LOAD position, if there is one.
     * @param targetPosition FIRE or LOAD. FIRE is the position ready to launch, and LOAD is the
     *                       load position.
     * @return A Slot, if there's one in position. If there isn't, will return noSlot.
     */
    public Slot findCurrentSlotInPosition(SorterHardware.PositionState targetPosition) {
        int currentOffset = getCurrentOffset();
        if (targetPosition == SWITCH || currentOffset < 0) return noSlot;

        switch (getCurrentOffset() - targetPosition.offset) {
            case slotALoad:
            case slotALoad - 3:
                return slotA;
            case slotBLoad:
            case slotBLoad - 3:
                return slotB;
            case slotCLoad:
            case slotCLoad - 3:
                return slotC;
            default:
                return noSlot;
        }
    }


    public Slot findBestPositionedType(SlotState targetArtifact, SorterHardware.PositionState targetPosition) {
        Slot currentFireSlot = findCurrentSlotInPosition(targetPosition);
        if (currentFireSlot.contains(targetArtifact)) {
            return currentFireSlot;
        }
        else {
            return findFirstType(targetArtifact);
        }
    }

    public Slot findBestPositionedNotType(SlotState targetArtifact, SorterHardware.PositionState targetPosition) {
        Slot currentFireSlot = findCurrentSlotInPosition(targetPosition);
        if (currentFireSlot.doesNotContain(targetArtifact)) {
            return currentFireSlot;
        }
        else {
            return findFirstNotType(targetArtifact);
        }
    }


    /**
     * Uses the blender encoder and offsetPositions to calculate which offset position the blender
     * is currently in.
     * @return The current offset, 0-2. -1 means it's in transit.
     */
    public int getCurrentOffset() {
        if (robot.sorterHardware.positionedCheck()) {
            return findClosestOffset(robot.sorterHardware.motor.getCurrentPosition());
        } else {
            return -1;
        }
    }

    /**
     * Compares the current motor position to offsetPositions to
     * calculate which offset position the blender is closest to.
     * @param ticks The current position of the encoder
     * @return The current nearest blender offset, from 0-2. -2 = error.
     */
    public int findClosestOffset(int ticks) {
        ticks = equalizeMotorPositions(ticks);

        int lowestDistance = 1000000000;
        int offset = -2;
        int currentDistanceCheck;

        for(int i = 0; i < offsetPositions.size(); i++) {
            currentDistanceCheck = Math.abs(offsetPositions.get(i) - ticks);
            if (currentDistanceCheck < lowestDistance) {
                lowestDistance = currentDistanceCheck;
                offset = i;
            }
        }
        // Check the high offset 0
        currentDistanceCheck = (int) Math.abs(SorterHardware.ticksPerRotation - ticks);
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
        while (ticks > SorterHardware.ticksPerRotation) {
            ticks -= SorterHardware.ticksPerRotation;
        }
        while (ticks < 0) {
            ticks += SorterHardware.ticksPerRotation;
        }
        return ticks;
    }


    /**
     * Tries to see if the current reference is OK (does not work)
     * @param reference The input reference
     * @return Whether or not it's legal
     */
    public boolean isCurrentReferenceLogical(int reference) {
        return reference % ((double) SorterHardware.ticksPerRotation / 3) == 0;
    }

    /**
     * A Slot is a representation of a physical Slot in the blender. The Slot class stores the
     * occupancy state (SlotState enum) and correct motor positions to load and fire an Artifact. A
     * constructor must be called to create an instance of the class, representing one slot in the
     * blender.
     */
    public class Slot {
        private SlotState occupied = UNKNOWN;
        private final int firePosition;
        private final int loadPosition;
        private final String name;

        /**
         * This constructor creates a Slot. A Slot is a representation of a physical Slot in the
         * blender.
         * @param motorLoadPosition The motor position to go to when firing, in ticks.
         * @param motorFirePosition The motor position to go to when loading, in ticks.
         */
        public Slot(int motorLoadPosition, int motorFirePosition, String name) {
            this.loadPosition = motorLoadPosition;
            this.firePosition = motorFirePosition;
            this.name = name;
        }

        /**
         * Returns the stored firing position for the Slot. If the Slot is noSlot,
         * will return the current blender reference, effectively avoiding any change.
         * @return The firing position, in ticks
         */
        public int getFirePosition() {
            return firePosition;
        }

        /**
         * Returns the stored loading position for the Slot. If the Slot is noSlot,
         * will return the current blender reference, effectively avoiding any change.
         * @return The loading position, in ticks
         */
        public int getLoadPosition() {
            return loadPosition;
        }

        /**
         * Returns the current occupation of the Slot. if the Slot is noSlot, will return UNKNOWN.
         * @return The current occupation of the Slot, in a SlotState enum.
         */
        public SlotState getOccupied() {
            return occupied;
        }

        /**
         * Stores the new contents in the Slot.
         * @param newOccupation The new SlotState
         */
        public void setOccupied(SlotState newOccupation) {
            occupied = newOccupation;
        }

        /**
         * Checks to see if the Slot contains the specified contents
         * @param checkState The contents to check for, in the form iof a SlotState enum.
         * @return Whether or not the Slot contains the contents checked for.
         */
        public boolean contains(SlotState checkState) {
            return occupied == checkState;
        }

        /**
         * Checks to see if the Slot doesn't contain any of the specified contents.
         * @param checkStates The state(s) to be checked against. Yes, state(s); it can handle
         *                    multiple.
         * @return Whether or not the Slot does not contain any of the contents checked against.
         */
        public boolean doesNotContain(SlotState... checkStates) {
            for (SlotState currentCheckState : checkStates) {
                if (currentCheckState == occupied) {
                    return false;
                }
            }
            return true;
        }

        /**
         * This function is an easy way to identify if a function returned a NoSlot.
         * @return A real Slot returns true, and a NoSlot returns false
         */
        public boolean exists() {
            return true;
        }

        /**
         * Gets a string for the name of the Slot: "A", "B", "C", or "No Slot Found".
         * @return The name of the Slot.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * The NoSlot is a special case. It will create a "fake" Slot that always returns the
     * current blender reference for positions and UNKNOWN for occupancy. This is to ensure the
     * findFirstSlot(), findFirstNoSlot() functions can still return a "no
     * Slot found" option without returning null.
     */
    public class NoSlot extends Slot {
        /**
         * Creates a phantom Slot, a NoSlot, that does as little damage as possible when it's
         * returned. Positions will return the current blender reference and the occupancy is always
         * UNKNOWN.
         */
        NoSlot() { // Feed it junk data. If you ever see these numbers, we have a problem
            super(0, 0, "No Slot Found");
        }

        @Override
        public int getFirePosition() {
            return (int) robot.sorterHardware.reference;
        }

        @Override
        public int getLoadPosition() {
            return (int) robot.sorterHardware.reference;
        }

        @Override
        public void setOccupied(SlotState newOccupation) {}

        @Override
        public boolean contains(SlotState checkState) {
            return checkState == UNKNOWN;
        }

        @Override
        public boolean doesNotContain(SlotState... checkStates) {
            return false;
        }

        @Override
        public boolean exists() {
            return false;
        }
    }

    /**
     * This class is the inventory. It stores the current count of Artifacts,
     * for each color and in total, and whether or not a Pattern can be created.
     */
    public class SlotInventory {
        private int count;
        private int purpleCount;
        private int greenCount;
        public SlotInventory() {}

        public void updateInventory(int currentPurpleCount, int currentGreenCount) {
            setTotalCount(currentPurpleCount + currentGreenCount);
            setPurpleCount(currentPurpleCount);
            setGreenCount(currentGreenCount);
        }
        public int getTotalCount() {
            return count;
        }
        public void setTotalCount(int newCount) {
            count = newCount;
        }

        public int getPurpleCount() {
            return purpleCount;
        }
        public void setPurpleCount(int newPurpleCount) {
            purpleCount = newPurpleCount;
        }

        public int getGreenCount() {
            return greenCount;
        }
        public void setGreenCount(int newGreenCount) {
            greenCount = newGreenCount;
        }

        /**
         * Checks to see if two PURPLE Artifacts and one GREEN Artifact is in the inventory; in
         * other words, if we can fire one Pattern.
         * @return Whether or not we can make the Pattern
         */
        public boolean canMakePattern() {
            return greenCount == 1 && purpleCount == 2;
        }
    }
}