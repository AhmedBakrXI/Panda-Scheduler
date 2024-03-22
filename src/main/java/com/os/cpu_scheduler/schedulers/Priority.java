package com.os.cpu_scheduler.schedulers;

import com.os.cpu_scheduler.process.Process;
import com.os.cpu_scheduler.process.ProcessList;


public class Priority extends Scheduler{
    
    public Priority(ProcessList processList) {
        super(processList);
    }

    @Override
    void schedule() {

        setIdle(true);

        if(!(processes.get(currentExecutingProcessIdx).getArrivalTime() > processes.getClockCounter()))
        {

            if(!(processes.isPreemptive()))
            {
                if(!(processes.get(currentExecutingProcessIdx).getRemainingTime() > 0))
                {
                    for(int i = 1; i < processes.length(); i++) {
                        if (!(processes.get(i).getRemainingTime() <= 0)) {
                            if (processes.get(i).getArrivalTime() <= processes.getClockCounter()) {
                                if (processes.get(i).getPriority() < processes.get(currentExecutingProcessIdx).getPriority()
                                        || (processes.get(currentExecutingProcessIdx).getRemainingTime() == 0)){
                                    currentExecutingProcessIdx = i;
                                }
                            }
                        }
                    }

                }
            }
            else
            {
                currentExecutingProcessIdx = 0;
                for(int i = 1; i < processes.length(); i++)
                {
                    if(processes.get(i).getRemainingTime() > 0)
                    {
                        if(processes.get(i).getArrivalTime() <= processes.getClockCounter())
                        {
                            if((processes.get(i).getPriority() < processes.get(currentExecutingProcessIdx).getPriority())
                                    || (processes.get(currentExecutingProcessIdx).getRemainingTime() == 0))
                            {
                                currentExecutingProcessIdx = i;
                            }
                        }

                    }

                }
            }


            if(!(processes.get(currentExecutingProcessIdx).getRemainingTime() == 0))
            {
                setIdle(false);
                //calculatin waiting time and turnaround time
                calculateWaitingTimeAndTurnaroundTime(currentExecutingProcessIdx);
                tick(currentExecutingProcessIdx);
            }
        }
        processes.incClockCounter();
    }
}
