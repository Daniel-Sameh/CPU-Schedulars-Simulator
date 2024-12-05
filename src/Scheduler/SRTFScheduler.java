package OSCourse.CPU.Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

import OSCourse.CPU.Process.Process;

public class SRTFScheduler extends AgingScheduler {
    SRTFScheduler(int contextSwitchTime, int agingTime) {
        super(contextSwitchTime, agingTime);
    }
    @Override
    public ArrayList<ExecutionRecord> run() {
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.getComparator());
        for (Process p: processes) {
            p.setProperty("remaining", p.getProperty("burstTime"));
        }
        Comparator<Process> comparator = 
            Process.getComparator("priority")
            .thenComparing(Process.getComparator("remaining"))
            .thenComparing(Process.getComparator());

        PriorityQueue<Process> pq = new PriorityQueue<>(comparator);
        int currentTime = processes.get(0).getProperty("arrivalTime");
        int index = addProcessesToQueue(pq, 0, currentTime);

        while (!pq.isEmpty()) {
            Process nextProcess = pq.poll();
            int runningTime = 0;
            while (nextProcess.getProperty("remaining") > 0) {
                currentTime++;
                runningTime++;
                nextProcess.setProperty("remaining", nextProcess.getProperty("remaining") - 1);
                index = addProcessesToQueue(pq, index, currentTime);
                pq = age(pq, currentTime);

                if (!pq.isEmpty() && comparator.compare(pq.peek(), nextProcess) < 0) {
                    pq.add(nextProcess);
                    break;
                }
            }
            records.add(new ExecutionRecord(nextProcess.getProperty("id"), currentTime - runningTime, runningTime));
            currentTime += contextSwitchTime;
            if (pq.isEmpty() && index < processes.size()) {
                currentTime = processes.get(index).getProperty("arrivalTime");
                index = addProcessesToQueue(pq, 0, currentTime);
            }
        }
        return records;
    }
}
