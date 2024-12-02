package process;

public class PriorityProcess extends Process {
    private int priority;
    public PriorityProcess(int pid, int arrivalTime, int burstTime, int priority) {
        super(pid, arrivalTime, burstTime);
        this.priority = priority;
    }
    @Override
    public void run() {
        System.out.println("Running priority process with pid " + pid);
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
