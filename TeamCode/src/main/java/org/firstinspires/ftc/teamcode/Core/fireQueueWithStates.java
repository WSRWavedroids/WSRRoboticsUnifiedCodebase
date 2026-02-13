package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.*;
import static org.firstinspires.ftc.teamcode.Core.Robot.patternColors.*;
import static org.firstinspires.ftc.teamcode.Core.SorterHardware.PositionState.*;
import static org.firstinspires.ftc.teamcode.Core.fireQueueWithStates.QueueState.*;
import static org.firstinspires.ftc.teamcode.Core.fireQueueWithStates.firingQueue.*;

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

    //State Machine innovation here
    public enum QueueState {CHECK, POSITIONING, FIRING}
    private QueueState state = CHECK;

    public QueueState getCurrentState() {
        return state;
    }
    //public firingQueue wantToFire = firingQueue.NONE;

    public fireQueueWithStates(Robot robotFile) {
        robot = robotFile;
        sorterHardware = robot.sorterHardware;
        sorterLogic = robot.sorterLogic;
        launcherHardware = robot.launcher;

        ballQueue = new ArrayList<>();
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
    }


    public boolean checkForExistingQueue()
    {
        return !ballQueue.isEmpty();
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

    public void updateQueueStates()
    {
            // If the driver isn't requesting a fire sequence, stay IDLE and reset index
            if (wantToFireQueue == NONE) {
                state = CHECK;
            }

            switch (state) {
                case CHECK:
                    // If we have balls to fire, start the process
                    if (robot.sorterLogic.inventory.getTotalCount() == 0) {
                        clearList();
                    } else if (!ballQueue.isEmpty()) {
                        state = POSITIONING;
                    }
                    break;
                case POSITIONING:
                    // Check if we've reached the end of our list or hit an empty slot
                    if (ballQueue.isEmpty()) {
                        finishQueue();
                        break;
                    }

                    robot.launcher.setPerfectLauncherVelocity();

                    int targetPosition;
                    ArtifactLocator.SlotState currentColor = ballQueue.get(0);
                    ArtifactLocator.Slot targetSlot;

                    if (currentColor == UNKNOWN) {
                        targetSlot = sorterLogic.findBestPositionedNotType(EMPTY, FIRE);
                    } else {
                        targetSlot = sorterLogic.findBestPositionedType(currentColor, FIRE);
                    }

                    if (!targetSlot.exists()) {
                        ballQueue.remove(0);

                        if (robot.sorterLogic.inventory.getTotalCount() == 0) {
                            finishQueue();
                        }
                        state = CHECK; 
                        break;
                    }
                    targetPosition = targetSlot.getFirePosition();

                    // Move the hardware
                    sorterHardware.prepareNewMovement(targetPosition);
                    launcherHardware.readyFire(0, false, false);
                    state = FIRING;
                    break;
                case FIRING:
                    // Wait for the launcher to finish its physical movement before moving the sorter again
                    // This prevents the sorter from rotating while the ball is still in the launcher path
                    if (launcherHardware.doneFiring()) {
                        ballQueue.remove(0); // Clear the ball we just fired

                        if (ballQueue.isEmpty()) {
                            finishQueue();
                        } else {
                            // Go back to position the next ball
                            state = CHECK;
                        }
                    }
                    break;
            }
        }

        /**
         * Resets hardware and variables after a sequence is done
         */
        private void finishQueue() {
            clearList();
            wantToFireQueue = NONE;
            state = CHECK;
            robot.launcher.setLauncherVelocity(0);

            // Return sorter to neutral/home position (usually index 0)
            //sorterHardware.prepareNewMovement(sorterHardware.motor.getCurrentPosition(), sorterHardware.positions[0]);
        }

    }







