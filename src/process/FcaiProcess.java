package process;

public class FcaiProcess extends Process{
    private int priority;
    private int quantum;
    public FcaiProcess(int pid, int arrivalTime, int burstTime, int priority, int quantum) {
        super(pid, arrivalTime, burstTime);
        this.priority = priority;
        this.quantum = quantum;
    }
    @Override
    public void run() {
        System.out.println("Running FCAI Process with pid " + pid);
    }
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getQuantum() {
        return quantum;
    }
    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

}
