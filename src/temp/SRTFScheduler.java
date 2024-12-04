package OSCourse.CPU;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SRTFScheduler extends AgingScheduler {
    SRTFScheduler(int contextSwitchTime, int agingTime) {
        super(contextSwitchTime, agingTime);
    }
    @Override
    public ArrayList<ExecutionRecord> run() {
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.geComparator());
        for (Process p: processes) {
            p.setProperty("remaining", p.getPropety("burstTime"));
        }
        Comparator<Process> comparator = 
            Process.geComparator("priority")
            .thenComparing(Process.geComparator("remaining"))
            .thenComparing(Process.geComparator());

        PriorityQueue<Process> pq = new PriorityQueue<>(comparator);
        int currentTime = processes.get(0).getPropety("arrivalTime");
        int index = addProcessesToQueue(pq, 0, currentTime);

        while (!pq.isEmpty()) {
            Process nextProcess = pq.poll();
            int runningTime = 0;
            while (nextProcess.getPropety("remaining") > 0) {
                currentTime++;
                runningTime++;
                nextProcess.setProperty("remaining", nextProcess.getPropety("remaining") - 1);
                index = addProcessesToQueue(pq, index, currentTime);
                pq = age(pq, currentTime);

                if (!pq.isEmpty() && comparator.compare(pq.peek(), nextProcess) < 0) {
                    pq.add(nextProcess);
                    break;
                }
            }
            records.add(new ExecutionRecord(nextProcess.getPropety("id"), currentTime - runningTime, runningTime));
            currentTime += contextSwitchTime;
            if (pq.isEmpty() && index < processes.size()) {
                Process tempProcess = processes.get(index++);
                currentTime = tempProcess.getPropety("burstTime");
                pq.add(tempProcess);
            }
        }
        return records;
    }
}
