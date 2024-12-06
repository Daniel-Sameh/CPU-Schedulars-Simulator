package org.cpuscheduling.Process;

import java.util.HashMap;
import java.util.Comparator;

public class Process {
    HashMap<String, Integer> properties;
    public Process(int id, int arrivalTime, int burstTime) {
        properties = new HashMap<>();
        setProperty("id", id);
        setProperty("arrivalTime", arrivalTime);
        setProperty("burstTime", burstTime);
        setProperty("remaining", burstTime);
        setProperty("lastExecutionTime", arrivalTime);
    }
    public void setProperty(String name, Integer value) {
        properties.put(name, value);
    }
    public int getProperty(String name) {
        if (!properties.containsKey(name)) {
            return -1;
        }
        return properties.get(name);
    }
    public void execute(int currentTimeAfterRunning, int runningTime) {
        setProperty("remaining", getProperty("remaining") - runningTime);
        setProperty("lastExecutionTime", currentTimeAfterRunning);
    }
    public int getTurnAroundTime(){
        return getProperty("lastExecutionTime") - getProperty("arrivalTime");
    }
    public int getWaitingTime(){
        return getTurnAroundTime() - getProperty("burstTime");
    }
    public static Comparator<Process> getComparator() {
        Comparator<Process> byArrivalTime = Comparator.comparingInt(p -> p.getProperty("arrivalTime"));
        return byArrivalTime;
    }
    public static Comparator<Process> getComparator(String property) {
        Comparator<Process> byProperty = Comparator.comparingInt(p -> p.getProperty(property));
        return byProperty;
    }
}