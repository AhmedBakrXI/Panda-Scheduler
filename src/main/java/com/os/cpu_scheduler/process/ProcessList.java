package com.os.cpu_scheduler.process;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProcessList{
    private ArrayList<Process> processes;
    private int totalRemainingTime = 0;
    private int clockCounter = 0;
    private boolean preemptive = false;



    public ProcessList() {
        this.processes = new ArrayList<Process>();
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }
    public Process get(int i){
        return processes.get(i);
    }
    public int length(){
        return processes.size();
    }

    public int getTotalRemainingTime() {
        return totalRemainingTime;
    }

    public int getClockCounter() {
        return clockCounter;
    }

    public void incClockCounter() {
        this.clockCounter += 1;
    }

    public boolean isPreemptive() {
        return preemptive;
    }

    public void setPreemptive(boolean Preemptive) {
        this.preemptive = Preemptive;
    }

    public void addProcess(Process process){
        processes.add(process);
        sortTheArray();
        totalRemainingTime += process.getRemainingTime();
    }

    public boolean isProcessesFinished() {
        boolean finish = true;
        for(int i = 0; i < processes.size(); i++)
            {
                if(processes.get(i).getRemainingTime() > 0)
                {
                    finish = false;
                    break;
                }

            }
        return finish;
    }



    public void printTheArray(){
        for (int i = 0; i < processes.size() ; i++) {
            System.out.println(processes.get(i).getArrivalTime());
        }
    }
    
    private void sortTheArray(){
        Comparator<Process> comparator = Comparator.comparingInt(Process::getArrivalTime);
        Collections.sort(processes, comparator);
    }

    public double avgWaitingTime(){
        double avg = 0;
        for (Process process : processes) {
            avg += process.getWaitingTime();
        }
        return Math.round((avg/processes.size()) *100)/(100.0);
    }    
    public double avgTurnAroundTime(){
        double avg = 0;
        for (Process process : processes) {
            avg += process.getTurnaroundTime();
        }
        return Math.round((avg/processes.size()) *100)/(100.0);
    }

 
}


