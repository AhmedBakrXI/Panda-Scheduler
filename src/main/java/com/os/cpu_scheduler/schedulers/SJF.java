package com.os.cpu_scheduler.schedulers;

import com.os.cpu_scheduler.process.Process;
import com.os.cpu_scheduler.process.ProcessList;


public class SJF extends Scheduler{
    
    public SJF(ProcessList processList) {
        super(processList);
    }

    @Override
    void schedule() {

        setIdle(true);

        if(!(processes.get(currentExecutingProcessIdx).getArrivalTime() > processes.getClockCounter()))
        {
            setIdle(false);

            if(!(processes.isPreemptive()))
            {
                if(!(processes.get(currentExecutingProcessIdx).getRemainingTime() > 0))
                {
                    for(int i = 1; i < processes.length(); i++) {
                        if (!(processes.get(i).getRemainingTime() <= 0)) {
                            if (processes.get(i).getArrivalTime() <= processes.getClockCounter()) {
                                if (processes.get(i).getRemainingTime() < processes.get(currentExecutingProcessIdx).getRemainingTime()
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
                            if((processes.get(i).getRemainingTime() < processes.get(currentExecutingProcessIdx).getRemainingTime())
                                    || (processes.get(currentExecutingProcessIdx).getRemainingTime() == 0))
                            {
                                currentExecutingProcessIdx = i;
                            }
                        }

                    }

                }
            }


            //calculatin waiting time and turnaround time
            calculateWaitingTimeAndTurnaroundTime(currentExecutingProcessIdx);
            tick(currentExecutingProcessIdx);
        }

        processes.incClockCounter();
    }


}


