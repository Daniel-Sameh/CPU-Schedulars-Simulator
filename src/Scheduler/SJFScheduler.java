package Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

import Process.Process;

public class SJFScheduler extends AgingScheduler {
    protected int agingTime;
    SJFScheduler(int contextSwitchTime, int agingTime) {
        super(contextSwitchTime, agingTime);
    }
    @Override
    public ArrayList<ExecutionRecord> run() {
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.getComparator());
        for (Process p: processes) {
            p.setProperty("priority", 0);
            p.setProperty("lastExecutionTime", p.getProperty("arrivalTime"));
        }
        Comparator<Process> comparator = 
            Process.getComparator("priority").reversed()
            .thenComparing(Process.getComparator("burstTime"))
            .thenComparing(Process.getComparator());

        PriorityQueue<Process> pq = new PriorityQueue<>(comparator);

        int currentTime = processes.get(0).getProperty("arrivalTime");
        int index = addProcessesToQueue(pq, 0, currentTime);

        while (!pq.isEmpty()) {
            Process nextProcess = pq.poll();
            int burstTime = nextProcess.getProperty("burstTime");
            currentTime += burstTime;

            index = addProcessesToQueue(pq, index, currentTime);
            pq = age(pq, currentTime);

            records.add(new ExecutionRecord(nextProcess.getProperty("id"), currentTime - burstTime, burstTime));
            currentTime += contextSwitchTime;
            if (pq.isEmpty() && index < processes.size()) {
                currentTime = processes.get(index).getProperty("arrivalTime");
                index = addProcessesToQueue(pq, 0, currentTime);
            }
        }
        return records;
    }
}
