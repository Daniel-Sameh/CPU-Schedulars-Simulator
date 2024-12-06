package org.cpuscheduling.Scheduler;

import java.util.PriorityQueue;

import org.cpuscheduling.Process.Process;

public abstract class AgingScheduler extends Scheduler {
    int agingTime;
    AgingScheduler(int contextSwitchTime, int agingTime) {
        super(contextSwitchTime);
        this.agingTime = agingTime;
    }
    protected PriorityQueue<Process> age(PriorityQueue<Process> queue, int currentTime) {
        PriorityQueue<Process> newQueue = new PriorityQueue<>(queue.comparator());
        for (Process p: processes) {
            if (currentTime>= p.getProperty("arrivalTime")){
                if ((currentTime - p.getProperty("lastExecutionTime")) % agingTime == 0) {
                    p.setProperty("priority", p.getProperty("priority") + 1);
                }
                newQueue.add(p);
            }
        }
        return newQueue;
    }
}