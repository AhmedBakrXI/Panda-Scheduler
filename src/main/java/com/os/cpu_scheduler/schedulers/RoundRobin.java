package com.os.cpu_scheduler.schedulers;

import com.os.cpu_scheduler.process.Process;
import com.os.cpu_scheduler.process.ProcessList;


public class RoundRobin extends Scheduler {


    public RoundRobin(ProcessList processList) {
        super(processList);
    }


    @Override
    public void schedule() {
        setIdle(true);
        currentExecutingProcessIdx = (currentExecutingProcessIdx + 1) % processes.length();
        for (int i = 0; i <= processes.length(); i++) {
            if ((processes.get(currentExecutingProcessIdx).getArrivalTime() <= processes.getClockCounter())
                    && (processes.get(currentExecutingProcessIdx).getRemainingTime() > 0)) {
                setIdle(false);
                break;
            }
            currentExecutingProcessIdx = (currentExecutingProcessIdx + 1) % processes.length();
        }

        if (!(isIdle())) {
            calculateWaitingTimeAndTurnaroundTime(currentExecutingProcessIdx);
            tick(currentExecutingProcessIdx);
        }
        processes.incClockCounter();
    }


    public static void main(String[] args) {
        Process process1 = new Process(0, 7, "P1", 3);
        Process process2 = new Process(1, 5, "P2", 2);
        Process process3 = new Process(2, 3, "P3", 1);


        ProcessList processList = new ProcessList();
        processList.addProcess(process1);
        processList.addProcess(process2);
        processList.addProcess(process3);

        RoundRobin rr = new RoundRobin(processList);

        while (!processList.isProcessesFinished()) {
            rr.schedule();
        }
        for (int i = 0; i < processList.length(); i++) {
            System.out.println(" WaitingTime = " + processList.get(i).getWaitingTime() + " TurnaroundTime = "
                    + processList.get(i).getTurnaroundTime());
        }
    }
}
