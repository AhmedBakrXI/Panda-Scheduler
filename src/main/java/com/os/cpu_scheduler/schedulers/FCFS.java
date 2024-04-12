package com.os.cpu_scheduler.schedulers;
import  com.os.cpu_scheduler.process.ProcessList;


public class FCFS extends Scheduler {
    // interface with UI
    public FCFS(ProcessList processList) {
        super(processList);
    }

    @Override
    public void schedule() {

        setIdle(true);
        if(!(processes.get(currentExecutingProcessIdx).getArrivalTime() > processes.getClockCounter()))
        {
            for (int i = currentExecutingProcessIdx; i < processes.length(); i++) {
                if(processes.get(currentExecutingProcessIdx).getRemainingTime() == 0){
                    currentExecutingProcessIdx++;
                }
                if(processes.get(i).getRemainingTime() > 0){
                    if(processes.get(i).getArrivalTime() < processes.get(currentExecutingProcessIdx).getArrivalTime()){
                        currentExecutingProcessIdx = i;
                    }
                }
            }

            if(!(processes.get(currentExecutingProcessIdx).getRemainingTime() == 0) &&
                 (!(processes.get(currentExecutingProcessIdx).getArrivalTime() > processes.getClockCounter())))
            {
                setIdle(false);
                // calculating waiting time and turnaround time
                calculateWaitingTimeAndTurnaroundTime(currentExecutingProcessIdx);
                tick(currentExecutingProcessIdx);
            }
        }
        processes.incClockCounter();
    }

}
