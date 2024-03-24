package com.os.cpu_scheduler.schedulers;

public enum SchedulerTypes {
    FCFS("FCFS"),
    PRIORITY_PREEMPTIVE("Priority Preemptive"),
    PRIORITY_NON_PREEMPTIVE("Priority Non Preemptive"),
    ROUND_ROBIN("Round Robin"),
    SJF_PREEMPTIVE("SJF Preemptive"),
    SJF_NON_PREEMPTIVE("SJF Non Preemptive");

    private final String description;

    // Constructor to associate a description with each enum constant
    SchedulerTypes(String description) {
        this.description = description;
    }

    // Getter method to retrieve the description
    public String getDescription() {
        return description;
    }
}
