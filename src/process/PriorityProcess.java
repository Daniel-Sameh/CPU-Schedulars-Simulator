package process;

public class PriorityProcess extends Process {
    public PriorityProcess(int pid, int arrivalTime, int burstTime, int priority) {
        super(pid, arrivalTime, burstTime, priority);
    }
    @Override
    public void run(int startTime, int endTime) {
        addRunTime(startTime, endTime);
        System.out.println("Process with pid " + pid + " is running");
    }
}
