package com.os.cpu_scheduler.schedulers;

public enum SchedulerTypes {
    FCFS("FCFS"),
    PRIORITY("Priority"),
    ROUND_ROBIN("Round Robin"),
    SJF("Shortest Job First");

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
