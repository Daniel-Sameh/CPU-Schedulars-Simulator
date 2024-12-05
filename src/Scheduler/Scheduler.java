package OSCourse.CPU.Scheduler;

import java.util.ArrayList;
import java.util.Queue;

import OSCourse.CPU.Process.Process;

public abstract class Scheduler {
    protected ArrayList<Process> processes;
    protected int contextSwitchTime;
    Scheduler(int contextSwitchTime) {
        processes = new ArrayList<>();
        this.contextSwitchTime = contextSwitchTime;
    }
    public abstract ArrayList<ExecutionRecord> run();
    public void addProcess(Process process) {
        processes.add(process);
    }
    public void setContextSwitchTime(int contextSwitchTime){
        this.contextSwitchTime = contextSwitchTime;
    }
    protected int addProcessesToQueue(Queue<Process> queue, int index, int currentTime) {
        while (index < processes.size() && currentTime >= processes.get(index).getProperty("arrivalTime"))
            queue.add(processes.get(index++));
        return index;
    }
}