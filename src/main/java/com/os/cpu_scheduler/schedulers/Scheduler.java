package com.os.cpu_scheduler.schedulers;
import com.os.cpu_scheduler.process.*;
public abstract class Scheduler {
    protected ProcessList processes;
    protected boolean idle = true;
    private boolean preemptive = false;
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

    public void tick(int i){
        // Modify the remaining time from the process I have to modify from schedule function
        processes.get(i).decRemainingTime();
    }

    public void calculateWaitingTimeAndTurnaroundTime(int currentExecutingProcessIdx) {
            for(int i = 0; i < processes.length(); i++)
            {
                if((processes.get(i).getArrivalTime() <= processes.getClockCounter())
                     && (processes.get(i).getRemainingTime() > 0) && (i != currentExecutingProcessIdx))
                {
                    processes.get(i).incWaitingTime();
                    processes.get(i).incTurnaroundTime();
                }

            }
            processes.get(currentExecutingProcessIdx).incTurnaroundTime();
    }
    
    public abstract void schedule();


}
