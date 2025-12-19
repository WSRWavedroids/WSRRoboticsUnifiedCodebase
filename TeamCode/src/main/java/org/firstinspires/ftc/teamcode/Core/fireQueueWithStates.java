package org.firstinspires.ftc.teamcode.Core;

import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.EMPTY;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.GREEN;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.PURPLE;
import static org.firstinspires.ftc.teamcode.Core.ArtifactLocator.SlotState.UNKNOWN;
import static org.firstinspires.ftc.teamcode.Core.fireQueueWithStates.firingQueue;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.teamcode.Vision.SensorHuskyLens;


@Configurable
public class fireQueueWithStates {
    ///This file will hopefully allow us to queue three shots back to back, eventually allowing
    ///caden to fire back to back to back
    ///also this could clean up autonomous firing sequences a bit

    private Robot robot;
    private BetaSorterHardware sorterHardware;
    private ArtifactLocator sorterLogic;
    private BetaLauncherHardware launcherHardware;
    public enum firingQueue{NONE, SMART, DUMB}
    public fireQueueWithStates.firingQueue wantToFireQueue = firingQueue.NONE;

    public boolean noBallsQueued = true;
    int currentSlot = 0;
    public queueBall[] balls;

    //State Machine innovation here
    private enum QueueState {IDLE, POSITIONING, FIRING, COOLDOWN}
    private QueueState state = QueueState.IDLE;
    //public firingQueue wantToFire = firingQueue.NONE;

    public fireQueueWithStates(Robot robotFile) {
        robot = robotFile;
        sorterHardware = robot.sorterHardware;
        sorterLogic = robot.sorterLogic;
        launcherHardware = robot.launcher;

        balls = new queueBall[3];

        for(int i = 0; i < 3; i++)
        {
           balls[i] = new queueBall();
           balls[i].color = EMPTY;
        }

    }

    public void addToNextSpotColor(ArtifactLocator.SlotState color)
    {
        if(currentSlot < 3)
        {
            balls[currentSlot].color = color;
            currentSlot++;
            noBallsQueued = false;
        }
    }

    public void addToNextSpotSimple()
    {
        if(currentSlot < 3)
        {
            noBallsQueued = false;
            balls[currentSlot].color = UNKNOWN;
            currentSlot++;
        }
    }

    public void addToListDirectly(int positionInList, ArtifactLocator.SlotState color)
    {
        balls[positionInList].color = color;
        noBallsQueued = false;
    }

    public void clearList()
    {
        noBallsQueued = true;
        currentSlot = 0;
        for(int i = 0; i < 3; i++)
        {
            balls[i].color = EMPTY;
        }
    }


    public boolean checkForExistingQueue()
    {
        for(int i = 0; i < 3; i++)
        {
            if(!balls[i].color.equals(EMPTY))
            {
                return  noBallsQueued = false;
            }
        }
        return noBallsQueued = true;

    }


    public void fillSimple()
    {
        robot.queue.wantToFireQueue = firingQueue.DUMB;
        balls[0].color = UNKNOWN;
        balls[1].color = UNKNOWN;
        balls[2].color = UNKNOWN;
        noBallsQueued = false;
    }

    public void addPattern(Object Pattern)
    {
        if(Pattern == "PGP")
        {
            balls[0].color = PURPLE;
            balls[1].color = GREEN;
            balls[2].color = PURPLE;
            noBallsQueued = false;
        }
        else if(Pattern == "PPG")
        {
            balls[0].color = PURPLE;
            balls[1].color = PURPLE;
            balls[2].color = GREEN;
            noBallsQueued = false;
        }
        else if(Pattern == "GPP")
        {
            balls[0].color = GREEN;
            balls[1].color = PURPLE;
            balls[2].color = PURPLE;
            noBallsQueued = false;
        }
        else
        {
            fillSimple();
        }
    }

    public void updateQueueStates(double speedTarget)
    {
            // If the driver isn't requesting a fire sequence, stay IDLE and reset index
            if (wantToFireQueue == firingQueue.NONE) {state = QueueState.IDLE;
                currentSlot = 0; // Reset index to the first ball
                return;
            }

            switch (state) {
                case IDLE:
                    currentSlot = 0;
                    // If we have balls to fire, start the process
                    if (!noBallsQueued) {
                        state = QueueState.POSITIONING;
                    } else {
                        // Fallback: if triggered but empty, reset
                        wantToFireQueue = firingQueue.NONE;
                    }
                    break;

                case POSITIONING:
                    // Check if we've reached the end of our list or hit an empty slot
                    if (currentSlot >= 3 || balls[currentSlot].color == EMPTY) {
                        finishQueue();
                        break;
                    }

                    int targetPosition;
                    if (wantToFireQueue == firingQueue.SMART) {
                        // Look up where this specific color is physically located
                        ArtifactLocator.Slot targetSlot = sorterLogic.findFirstType(balls[currentSlot].color);

                        if (targetSlot == sorterLogic.noSlot) {
                            // If we can't find that color, skip to next ball to avoid "locking up"
                            currentSlot++;
                            break;
                        }
                        targetPosition = targetSlot.getFirePosition();
                    } else {
                        // DUMB fire: assume balls are in slots 1, 3, and 5 (index 1, 3, 5 in positions array)
                        // Logic: 0 -> pos[1], 1 -> pos[3], 2 -> pos[5]
                        targetPosition = sorterHardware.positions[(currentSlot * 2) + 1];
                    }

                    // Move the hardware
                    sorterHardware.prepareNewMovement(sorterHardware.motor.getCurrentPosition(), targetPosition);
                    state = QueueState.FIRING;
                    break;

                case FIRING:
                    // WAIT for hardware to be ready.
                    // We check that the sorter isn't moving AND launcher isn't busy.
                    boolean sorterReady = robot.sorterHardware.doneMoving();
                    boolean launcherReady = !launcherHardware.waitingToFire && !launcherHardware.onCooldown;

                    if (sorterReady && launcherReady) {
                        launcherHardware.readyFire(speedTarget, false);
                        state = QueueState.COOLDOWN;
                    }
                    break;

                case COOLDOWN:
                    // Wait for the launcher to finish its physical movement before moving the sorter again
                    // This prevents the sorter from rotating while the ball is still in the launcher path
                    if (!launcherHardware.waitingToFire && !launcherHardware.onCooldown) {
                        balls[currentSlot].color = EMPTY; // Clear the ball we just fired
                        currentSlot++;

                        if (currentSlot >= 3) {
                            finishQueue();
                        } else {
                            // Go back to position the next ball
                            state = QueueState.POSITIONING;
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
        wantToFireQueue = firingQueue.NONE;
        state = QueueState.IDLE;

        // Return sorter to neutral/home position (usually index 0)
        sorterHardware.prepareNewMovement(sorterHardware.motor.getCurrentPosition(), sorterHardware.positions[0]);
    }

    }







