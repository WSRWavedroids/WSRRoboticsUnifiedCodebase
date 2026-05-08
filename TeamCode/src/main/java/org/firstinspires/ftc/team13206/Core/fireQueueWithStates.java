package org.firstinspires.ftc.team13206.Core;

import static org.firstinspires.ftc.team13206.Core.ArtifactLocator.SlotState.*;
import static org.firstinspires.ftc.team13206.Core.Robot.patternColors.*;
import static org.firstinspires.ftc.team13206.Core.SorterHardware.PositionState.*;
import static org.firstinspires.ftc.team13206.Core.fireQueueWithStates.HardwareQueueState.*;
import static org.firstinspires.ftc.team13206.Core.fireQueueWithStates.firingQueue.*;

import com.bylazar.configurables.annotations.Configurable;

import java.util.ArrayList;


@Configurable
public class fireQueueWithStates {
    ///This file will hopefully allow us to queue three shots back to back, eventually allowing
    ///caden to fire back to back to back
    ///also this could clean up autonomous firing sequences a bit

    private final Robot robot;
    private final SorterHardware sorterHardware;
    private final ArtifactLocator sorterLogic;
    private final LauncherHardware launcherHardware;
    public enum firingQueue{NONE, SMART, DUMB}
    public fireQueueWithStates.firingQueue wantToFireQueue = NONE;

    public boolean noBallsQueued = true;

    public ArrayList<ArtifactLocator.SlotState> ballQueue;
    public ArrayList<ArtifactLocator.Slot> slotQueue;

    //State Machine innovation here
    public enum HardwareQueueState {READY, CHECK, POSITIONING, FIRING}
    private HardwareQueueState hardwareState = READY;

    public HardwareQueueState getCurrentHardwareState() {
        return hardwareState;
    }
    //public firingQueue wantToFire = firingQueue.NONE;

    public fireQueueWithStates(Robot robotFile) {
        robot = robotFile;
        sorterHardware = robot.sorterHardware;
        sorterLogic = robot.sorterLogic;
        launcherHardware = robot.launcher;

        ballQueue = new ArrayList<>();
        slotQueue = new ArrayList<>();
    }

    public void addToNextSpotColor(ArtifactLocator.SlotState color)
    {
        ballQueue.add(color);
        noBallsQueued = false;
    }

    public void addToNextSpotSimple()
    {
        noBallsQueued = false;
        ballQueue.add(UNKNOWN);
    }

    public void addToListDirectly(int positionInList, ArtifactLocator.SlotState color)
    {
        ballQueue.add(positionInList, color);
        noBallsQueued = false;
    }

    public void clearList()
    {
        noBallsQueued = true;
        ballQueue.clear();
        slotQueue.clear();
    }


    public boolean checkForExistingQueue()
    {
        return !slotQueue.isEmpty() || !ballQueue.isEmpty();
    }


    public void fillSimple()
    {
        ballQueue.add(UNKNOWN);
        ballQueue.add(UNKNOWN);
        ballQueue.add(UNKNOWN);
        robot.queue.wantToFireQueue = DUMB;
        noBallsQueued = false;
    }

    public void addPattern(Robot.patternColors pattern)
    {
        if(pattern == PGP)
        {
            ballQueue.add(PURPLE);
            ballQueue.add(GREEN);
            ballQueue.add(PURPLE);
            noBallsQueued = false;
        }
        else if(pattern == PPG)
        {
            ballQueue.add(PURPLE);
            ballQueue.add(PURPLE);
            ballQueue.add(GREEN);
            noBallsQueued = false;
        }
        else if(pattern == GPP)
        {
            ballQueue.add(GREEN);
            ballQueue.add(PURPLE);
            ballQueue.add(PURPLE);
            noBallsQueued = false;
        }
        else
        {
            fillSimple();
        }

        wantToFireQueue = SMART;
        noBallsQueued = false;
    }

    public void addOffsetPattern(Robot.patternColors pattern, int offset) {
        addPattern(pattern);

        for (int i = 0; i < offset; i++) {
            ballQueue.add(ballQueue.get(0));
            ballQueue.remove(0);
        }
    }

    public void updateQueueStates()
    {
        // If the driver isn't requesting a fire sequence, stay IDLE and reset index
        if (wantToFireQueue == NONE) {
            hardwareState = READY;
        }
        if (!ballQueue.isEmpty() && wantToFireQueue != NONE) {
            ArtifactLocator.SlotState currentColor = ballQueue.get(0);
            ArtifactLocator.Slot targetSlot;

            if (currentColor == UNKNOWN) {
                targetSlot = sorterLogic.findBestPositionedNotType(EMPTY, FIRE);
            } else {
                targetSlot = sorterLogic.findBestPositionedType(currentColor, FIRE);
            }

            if (!targetSlot.exists()) {
                ballQueue.remove(0);
            }
            else {
                targetSlot.claim();
                slotQueue.add(targetSlot);
                ballQueue.remove(0);
            }
        }
        else if (hardwareState == READY && slotQueue.isEmpty()) {
            noBallsQueued = true;
        }

        switch (hardwareState) {
            case READY:
                // If we have balls to fire, start the process
                if (!slotQueue.isEmpty()) {
                    hardwareState = CHECK;
                }
                break;
            case CHECK:
                if(robot.sorterLogic.inventory.getTotalCount() == 0 || slotQueue.isEmpty()) {
                    finishQueue();
                } else {
                    robot.launcher.setPerfectLauncherVelocity();

                    // Move the hardware
                    sorterHardware.prepareNewMovement(slotQueue.get(0).getFirePosition());
                    launcherHardware.readyFire(0, false, false);
                    hardwareState = FIRING;
                }
                break;
            case FIRING:
                // Wait for the launcher to finish its physical movement before moving the sorter again
                // This prevents the sorter from rotating while the ball is still in the launcher path
                if (launcherHardware.doneFiring()) {
                    slotQueue.remove(0); // Clear the Slot we just fired

                    if (slotQueue.isEmpty()) {
                        finishQueue();
                    } else {
                        // Go back to position the next ball
                        hardwareState = CHECK;
                    }
                }
                break;
        }
    }

        /**
         * Resets hardware and variables after a sequence is done
         */
        public void finishQueue() {
            clearList();
            wantToFireQueue = NONE;
            hardwareState = READY;
            robot.launcher.setLauncherVelocity(0);
            for (ArtifactLocator.Slot slot : robot.sorterLogic.allSlots) {
                slot.release();
            }

            // Return sorter to neutral/home position (usually index 0)
            //sorterHardware.prepareNewMovement(sorterHardware.motor.getCurrentPosition(), sorterHardware.positions[0]);
        }

    }







