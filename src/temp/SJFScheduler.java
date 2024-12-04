package OSCourse.CPU;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SJFScheduler extends AgingScheduler {
    protected int agingTime;
    SJFScheduler(int contextSwitchTime, int agingTime) {
        super(contextSwitchTime, agingTime);
    }
    @Override
    public ArrayList<ExecutionRecord> run() {
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.geComparator());
        for (Process p: processes) {
            p.setProperty("priority", 0);
        }
        Comparator<Process> comparator = 
            Process.geComparator("priority").reversed()
            .thenComparing(Process.geComparator("burstTime"))
            .thenComparing(Process.geComparator());

        PriorityQueue<Process> pq = new PriorityQueue<>(comparator);

        int currentTime = processes.get(0).getPropety("arrivalTime");
        int index = addProcessesToQueue(pq, 0, currentTime);

        while (!pq.isEmpty()) {
            Process nextProcess = pq.poll();
            int burstTime = nextProcess.getPropety("burstTime");
            currentTime += burstTime;

            index = addProcessesToQueue(pq, index, currentTime);
            pq = age(pq, currentTime);

            records.add(new ExecutionRecord(nextProcess.getPropety("id"), currentTime - burstTime, burstTime));
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
