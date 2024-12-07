package org.cpuscheduling.Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import org.cpuscheduling.Process.Process;

public class SJFScheduler extends AgingScheduler {
    protected int agingTime;
    public SJFScheduler(int contextSwitchTime, int agingTime) {
        super(contextSwitchTime, agingTime);
    }
    @Override
    public ArrayList<ExecutionRecord> run() {
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.getComparator());
        for (Process p: processes) {
            p.setProperty("priority", 0);
        }
        Comparator<Process> comparator = 
            Process.getComparator("priority")
            .reversed()
            .thenComparing(Process.getComparator("burstTime"))
            .thenComparing(Process.getComparator());

        PriorityQueue<Process> pq = new PriorityQueue<>(comparator);

        int currentTime = processes.get(0).getProperty("arrivalTime");
        int index = addProcessesToQueue(pq, 0, currentTime);

        while (!pq.isEmpty()) {
            Process nextProcess = pq.poll();
            int burstTime = nextProcess.getProperty("burstTime");
            currentTime += burstTime;

            nextProcess.execute(currentTime, burstTime);
            index = addProcessesToQueue(pq, index, currentTime);
            pq = age(pq, currentTime);

            addRecord(records, nextProcess, currentTime, burstTime);
            currentTime += contextSwitchTime;
            if (pq.isEmpty() && index < processes.size()) {
                currentTime = processes.get(index).getProperty("arrivalTime");
                index = addProcessesToQueue(pq, 0, currentTime);
            }
        }
        return records;
    }
}
