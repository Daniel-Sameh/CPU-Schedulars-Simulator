package org.cpuscheduling.Scheduler;

import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import org.cpuscheduling.Process.Process;

public class PriorityScheduler extends Scheduler {
    public PriorityScheduler(int contextSwitchTime) {
        super(contextSwitchTime);
    }
    @Override
    public ArrayList<ExecutionRecord> run() {
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.getComparator());
        Comparator<Process> comparator = 
            Process.getComparator("priority")
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