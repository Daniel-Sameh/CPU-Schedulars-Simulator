package process;

public abstract class Process {
    int pid;
    private int arrivalTime;
    private int burstTime;
    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }
    public abstract void run();

}
