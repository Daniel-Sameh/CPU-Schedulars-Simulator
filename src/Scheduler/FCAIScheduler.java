package Scheduler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;

import Process.Process;

public class FCAIScheduler extends Scheduler {

    public FCAIScheduler(int contextSwitchTime) {
        super(contextSwitchTime);
    }

    @Override
    public ArrayList<ExecutionRecord> run() {
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.getComparator());

        for (Process p: processes) {
            p.setProperty("remaining", p.getProperty("burstTime"));
        }

        double v1 = (double)processes.get(processes.size() - 1).getProperty("arrivalTime") / 10;
        double v2 = (double)Collections.max(processes, Comparator.comparingInt(p -> p.getProperty("burstTime"))).getProperty("burstTime") / 10;

        Comparator<Process> FCAIComparator = Comparator.comparingDouble(p -> calculateFCAIFactor(p, v1, v2));
        Deque<Process> queue = new ArrayDeque<>();

        int currentTime = processes.get(0).getProperty("arrivalTime");
        int index = addProcessesToQueue(queue, 0, currentTime);

        while (!queue.isEmpty()) {
            Process nextProcess = queue.poll();
            int oldQuantum = nextProcess.getProperty("quantum");
            int firstForty = (int)Math.ceil(0.4 * (double)oldQuantum);
            int runningTime = Math.min(firstForty, nextProcess.getProperty("remaining"));

            currentTime += runningTime;
            nextProcess.setProperty("remaining", nextProcess.getProperty("remaining") - runningTime);
            index = addProcessesToQueue(queue, index, currentTime);


            while (nextProcess.getProperty("remaining") > 0 && runningTime < oldQuantum) {
                
                index = addProcessesToQueue(queue, index, currentTime);

                if (!queue.isEmpty()) {
                    Process bestProcess = Collections.min(queue, FCAIComparator);
                    if (calculateFCAIFactor(nextProcess, v1, v2) >= calculateFCAIFactor(bestProcess, v1, v2)) {
                        queue.remove(bestProcess);
                        queue.add(nextProcess);
                        queue.addFirst(bestProcess);
                        nextProcess.setProperty("quantum", 2 * oldQuantum - runningTime);
                        break;
                    }
                }
                currentTime++;
                runningTime++;
                nextProcess.setProperty("remaining", nextProcess.getProperty("remaining") - 1);
            }
            if (runningTime == oldQuantum && nextProcess.getProperty("remaining") > 0) {
                nextProcess.setProperty("quantum", oldQuantum + 2);
                queue.add(nextProcess);
            }

            records.add(new ExecutionRecord(nextProcess.getProperty("id"), currentTime - runningTime, runningTime));
            if (queue.isEmpty() && index < processes.size()) {
                currentTime = processes.get(index).getProperty("arrivalTime");
                index = addProcessesToQueue(queue, 0, currentTime);
            }
        }
        return records;
    }
    int calculateFCAIFactor(Process process, double v1, double v2) {
        return (int)Math.ceil((10 - process.getProperty("priority")) + ((double)process.getProperty("arrivalTime") / v1) + ((double)process.getProperty("remaining") / v2));
    }
}
