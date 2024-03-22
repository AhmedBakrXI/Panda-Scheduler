package com.os.cpu_scheduler.schedulers;
import  com.os.cpu_scheduler.process.ProcessList;


public class FCFS extends Scheduler {
    // interface with UI
    public FCFS(ProcessList processList) {
        super(processList);
    }

    @Override
    public void schedule() {
        int minArrivalIdx = 0;

        setIdle(true);
        if(!(processes.get(minArrivalIdx).getArrivalTime() > processes.getClockCounter()))
        {
            setIdle(false);
            for (int i = 1; i < processes.length(); i++) {
                if(processes.get(minArrivalIdx).getRemainingTime() == 0){
                    minArrivalIdx++;
                }
                if(processes.get(i).getRemainingTime() > 0){
                    if(processes.get(i).getArrivalTime() < processes.get(minArrivalIdx).getArrivalTime()){
                        minArrivalIdx = i;
                    }
                }
            }
            calculateWaitingTimeAndTurnaroundTime(minArrivalIdx);
            tick(minArrivalIdx);

        }
        processes.incClockCounter();
    }

}
