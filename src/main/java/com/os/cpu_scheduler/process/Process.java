package com.os.cpu_scheduler.process;

/**
 * The Process class represents a process entity with various attributes
 * such as arrival time, burst time, name, remaining time, etc.
 */
public class Process {
    // process datafields
    private int arrivalTime;
    private int burstTime;
    private String name;
    private int remainingTime;

    private int waitingTime = 0;
    private int turnaroundTime = 0;
    private int priority = 1;
    private static int counter;
    private int id;

    /**
     * Constructs a process with the specified arrival time, burst time, and name.
     * It also gives id to the process.
     * 
     * @param arrivalTime The arrival time of the process.
     * @param burstTime   The burst time of the process.
     * @param name        The name of the process.
     */
    public Process(int arrivalTime, int burstTime, String name) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.name = name;
        remainingTime = burstTime;
        this.id = counter;
        counter++;
    }

    /**
     * Constructs a process with the specified arrival time, burst time, name, and
     * priority.
     * 
     * @param arrivalTime The arrival time of the process.
     * @param burstTime   The burst time of the process.
     * @param name        The name of the process.
     * @param priority    The priority of the process.
     */
    public Process(int arrivalTime, int burstTime, String name, int priority) {
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.name = name;
        remainingTime = burstTime;
        this.priority = priority;
        this.id = counter;
        counter++;
    }

    /**
     * Constructs a process with the same attributes as the specified process.
     * 
     * @param p The process to deep copy.
     */
    public Process(Process p) {
        this.name = new String(p.name);
        this.burstTime = p.burstTime;
        this.priority = p.priority;
        this.arrivalTime = p.arrivalTime;
        this.remainingTime = p.remainingTime;
        this.id = counter;
        counter++;
    }

    /**
     * Gets the remaining time of the process.
     * 
     * @return The remaining time of the process.
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * Gets the arrival time of the process.
     * 
     * @return The arrival time of the process.
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Gets the burst time of the process.
     * 
     * @return The burst time of the process.
     */
    public int getBurstTime() {
        return burstTime;
    }

    /**
     * Gets the priority of the process.
     * 
     * @return The priority of the process.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Gets the waiting time of the process.
     * 
     * @return The waiting time of the process.
     */
    public int getWaitingTime() {
        return waitingTime;
    }

    /**
     * Gets the turnaround time of the process.
     * 
     * @return The turnaround time of the process.
     */
    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    /**
     * Gets the name of the process.
     * 
     * @return The name of the process.
     */
    public String getName() {
        return name;
    }

    /**
     * Decrements the remaining time of the process by 1.
     */
    public void decRemainingTime() {
        this.remainingTime -= 1;
    }

    /**
     * Increments the waiting time of the process by 1.
     */
    public void incWaitingTime() {
        this.waitingTime += 1;
    }

    /**
     * Increments the turnaround time of the process by 1.
     */
    public void incTurnaroundTime() {
        this.turnaroundTime += 1;
    }

    /**
     * Gets the ID of the process.
     * @return the id of the current process
     */
    public int getId() {
        return id - 2;
    }

    /**
     * Resets the counter used for assigning IDs to processes.
     */
    public static void resetCounter() {
        counter = 0;
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

}
