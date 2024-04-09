package com.os.cpu_scheduler.process;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The ProcessList class represents a list of processes and provides various methods to manipulate and analyze them.
 */
public class ProcessList {
    private ArrayList<Process> processes;
    private int totalRemainingTime = 0;
    private int clockCounter = 0;

    /**
     * Constructs an empty ProcessList.
     */
    public ProcessList() {
        this.processes = new ArrayList<Process>();
    }

    /**
     * Retrieves the list of processes.
     * @return The list of processes.
     */
    public ArrayList<Process> getProcesses() {
        return processes;
    }

    /**
     * Retrieves the process at the specified index.
     * @param i The index of the process to retrieve.
     * @return The process at the specified index.
     */
    public Process get(int i){
        return processes.get(i);
    }

    /**
     * Retrieves the number of processes in the list.
     * @return The number of processes in the list.
     */
    public int length(){
        return processes.size();
    }

    /**
     * Retrieves the total remaining time of all processes in the list.
     * @return The total remaining time of all processes.
     */
    public int getTotalRemainingTime() {
        return totalRemainingTime;
    }

    /**
     * Retrieves the clock counter.
     * @return The clock counter.
     */
    public int getClockCounter() {
        return clockCounter;
    }

    /**
     * Increments the clock counter by 1.
     */
    public void incClockCounter() {
        this.clockCounter += 1;
    }

    /**
     * Adds a process to the list, sorting the list based on arrival time.
     * @param process The process to add to the list.
     */
    public void addProcess(Process process){
        processes.add(process);
        sortTheArray();
        totalRemainingTime += process.getRemainingTime();
    }

    /**
     * Checks if all processes in the list have finished execution.
     * @return True if all processes have finished, otherwise false.
     */
    public boolean isProcessesFinished() {
        boolean finish = true;
        for(int i = 0; i < processes.size(); i++) {
            if(processes.get(i).getRemainingTime() > 0) {
                finish = false;
                break;
            }
        }
        return finish;
    }

    /**
     * Calculates the average waiting time of all processes in the list.
     * @return The average waiting time of all processes.
     */
    public double avgWaitingTime(){
        double avg = 0;
        for (Process process : processes) {
            avg += process.getWaitingTime();
        }
        return Math.round((avg/processes.size()) *100)/(100.0);
    }

    /**
     * Calculates the average turnaround time of all processes in the list.
     * @return The average turnaround time of all processes.
     */
    public double avgTurnAroundTime(){
        double avg = 0;
        for (Process process : processes) {
            avg += process.getTurnaroundTime();
        }
        return Math.round((avg/processes.size()) *100)/(100.0);
    }

    /**
     * Prints the arrival times of all processes in the list.
     */
    public void printTheArray(){
        for (int i = 0; i < processes.size() ; i++) {
            System.out.println(processes.get(i).getArrivalTime());
        }
    }

    /**
     * Sorts the list of processes based on their arrival times.
     */
    private void sortTheArray(){
        Comparator<Process> comparator = Comparator.comparingInt(Process::getArrivalTime);
        Collections.sort(processes, comparator);
    }
}
