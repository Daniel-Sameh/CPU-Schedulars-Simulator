package org.cpuscheduling.Scheduler;

public class ExecutionRecord {
    public int processIndex;
    public int startTime;
    public int runningTime;
    public int quantum;
    public ExecutionRecord(int processIndex, int startTime, int runningTime) {
        this.processIndex = processIndex;
        this.startTime = startTime;
        this.runningTime = runningTime;
        this.quantum = -1;
    }
}