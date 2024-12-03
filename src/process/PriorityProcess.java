package process;

public class PriorityProcess extends Process {
    public PriorityProcess(int pid, int arrivalTime, int burstTime, int priority) {
        super(pid, arrivalTime, burstTime, priority);
    }
    @Override
    public void run() {
        System.out.println("Priority process with pid " + pid+" Completed");
    }
}
