package com.os.cpu_scheduler.schedulers;

import com.os.cpu_scheduler.process.*;


/**
 * Scheduler Class is an abstract class responsible for executing scheduler algorithms
 * implemented by subclasses.
 */
public abstract class Scheduler {
    protected ProcessList processes;
    protected boolean idle = true;
    private boolean preemptive = false;
    private int time;

    public int getCurrentExecutingProcessIdx() {
        return currentExecutingProcessIdx;
    }

    protected int currentExecutingProcessIdx = 0;

    // UI concern
    public Scheduler(ProcessList processList) {
        processes = processList;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }

    public boolean isPreemptive() {
        return preemptive;
    }

    public void setPreemptive(boolean Preemptive) {
        this.preemptive = Preemptive;
    }

    public void tick(int i) {
        // Modify the remaining time from the process I have to modify from schedule function
        time++;
        processes.get(i).decRemainingTime();
    }

    public int getTime() {
        return time;
    }

    /**
     * Calculates the waiting time and turnaround time for all processes in the system.
     *
     * @param currentExecutingProcessIdx the index of the currently executing process
     */
    public void calculateWaitingTimeAndTurnaroundTime(int currentExecutingProcessIdx) {
        for (int i = 0; i < processes.length(); i++) {
            // Check if the current process is in the list of processes
            if ((processes.get(i).getArrivalTime() <= processes.getClockCounter())
                    && (processes.get(i).getRemainingTime() > 0) && (i != currentExecutingProcessIdx)) {
                // Increment the waiting time and turnaround time of the process
                processes.get(i).incWaitingTime();
                processes.get(i).incTurnaroundTime();
            }
        }

        // Increment the turnaround time of the current executing process
        processes.get(currentExecutingProcessIdx).incTurnaroundTime();
    }

    /**
     * Abstract method responsible for implementing the scheduler algorithm.
     */
    public abstract void schedule();
}
