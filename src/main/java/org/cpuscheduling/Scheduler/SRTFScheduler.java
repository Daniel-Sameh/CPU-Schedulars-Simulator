package org.cpuscheduling.Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import org.cpuscheduling.Process.Process;

public class SRTFScheduler extends AgingScheduler {
    public SRTFScheduler(int contextSwitchTime, int agingTime) {
        super(contextSwitchTime, agingTime);
    }
    @Override
    public ArrayList<ExecutionRecord> run() {
        System.out.println("SRTF Scheduler running...");
        System.out.println("Results: ");
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.getComparator());

        for (Process p: processes) {
            p.setProperty("priority", 0);
        }

        Comparator<Process> comparator = 
            Process.getComparator("priority")
            .reversed()
            .thenComparing(Process.getComparator("remaining"))
            .thenComparing(Process.getComparator());

        PriorityQueue<Process> pq = new PriorityQueue<>(comparator);
        int currentTime = processes.get(0).getProperty("arrivalTime");
        int index = addProcessesToQueue(pq, 0, currentTime);

        while (!pq.isEmpty()) {
            Process nextProcess = pq.poll();
            int runningTime = 0;
            while (nextProcess.getProperty("remaining") > 0) { //Simulating each second running of the process
                currentTime++;
                runningTime++;

                nextProcess.execute(currentTime, 1);
                index = addProcessesToQueue(pq, index, currentTime);
                pq = age(pq, currentTime);

                if (nextProcess.getProperty("remaining") > 0 && !pq.isEmpty() && comparator.compare(pq.peek(), nextProcess) < 0) {
                    pq.add(nextProcess);
                    break;
                }
            }

            addRecord(records, nextProcess, currentTime, runningTime);
            currentTime += contextSwitchTime;
            if (pq.isEmpty() && index < processes.size()) {
                currentTime = processes.get(index).getProperty("arrivalTime");
                index = addProcessesToQueue(pq, 0, currentTime);
            }
        }
        for (Process p: processes) {
            System.out.println("Processs#"+p.getProperty("id")+" Waiting Time= "+ p.getWaitingTime()+ ", TurnAround Time= "+p.getTurnAroundTime());
        }
        System.out.println("-------------------------------------------");
        return records;
    }
}
