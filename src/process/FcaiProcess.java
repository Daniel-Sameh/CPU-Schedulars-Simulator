package process;

public class FcaiProcess extends Process{
    private int quantum;
    public FcaiProcess(int pid, int arrivalTime, int burstTime, int priority, int quantum) {
        super(pid, arrivalTime, burstTime, priority);
        this.quantum = quantum;
    }
    @Override
    public void run(int startTime, int endTime) {
        addRunTime(startTime, endTime);
        System.out.println("Running FCAI Process with pid " + pid);
    }

    public int getQuantum() {
        return quantum;
    }
    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

}
