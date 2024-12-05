package OSCourse.CPU.Scheduler;

public class ExecutionRecord {
    public int pid;
    public int startTime;
    public int runningTime;
    public ExecutionRecord(int pid, int startTime, int runningTime) {
        this.pid = pid;
        this.startTime = startTime;
        this.runningTime = runningTime;
    }
}