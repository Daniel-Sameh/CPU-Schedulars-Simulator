package process;

import java.util.ArrayList;

public abstract class Process {
    int pid;
    private int priority;
    private int arrivalTime;
    private int burstTime;
    private ArrayList<ArrayList<Integer>> runTimes;
    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.runTimes = new ArrayList<>();
    }
    public int getPid() {
        return pid;
    }
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }
    public ArrayList<ArrayList<Integer>> getRunTimes() {
        return runTimes;
    }
    protected void addRunTime(int startTime, int endTime){
        ArrayList<Integer> runTime = new ArrayList<>();
        runTime.add(startTime);
        runTime.add(endTime);
        runTimes.add(runTime);
    }

    public abstract void run(int startTime, int endTime);

}
