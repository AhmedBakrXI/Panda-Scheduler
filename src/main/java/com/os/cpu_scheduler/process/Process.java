package com.os.cpu_scheduler.process;

public class Process {
    // process elements
    private int arrivalTime;
    private int burstTime;
    private String name;
    private int remainingTime;

    private int waitingTime = 0;
    private int turnaroundTime = 0;
    private int priority = 1;
    private static int counter;

    public int getId() {
        return id - 2;
    }

    private int id;

    // constructor
    public Process(int arrivalTime, int burstTime, String name) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.name = name;
        remainingTime = burstTime;
        this.id = counter;
        counter++;
    }
    public Process(int arrivalTime, int burstTime, String name , int priority) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.name = name;
        remainingTime = burstTime;
        this.priority = priority;
        this.id = counter;
        counter++;
    }

    public Process(Process p) {
        this.name = new String(p.name);
        this.burstTime = p.burstTime;
        this.priority = p.priority;
        this.arrivalTime = p.arrivalTime;
        this.remainingTime = p.remainingTime;
        this.id = counter;
        counter++;
    }

    // some getters&setters
    public int getRemainingTime() {
        return remainingTime;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getBurstTime() {
        return burstTime;
    }
    public int getPriority() {
        return priority;
    }
    public int getWaitingTime() {
        return waitingTime;
    }
    public int getTurnaroundTime() {
        return turnaroundTime;
    }
    public String getName() {
        return name;
    }
    public void decRemainingTime() {
        this.remainingTime -= 1;
    }
    
    public void incWaitingTime() {
        this.waitingTime += 1;
    }
    
    public void incTurnaroundTime() {
        this.turnaroundTime += 1;
    }

    @Override
    public String toString() {
        return "Process{" +
                " name='" + name + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", remainingTime=" + remainingTime +
                '}';
    }

    public static void resetCounter() {
        counter = 0;
    }

}
