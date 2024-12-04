package OSCourse.CPU;

import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;


public class PriorityScheduler extends Scheduler {
    PriorityScheduler(int contextSwitchTime) {
        super(contextSwitchTime);
    }
    @Override
    public ArrayList<ExecutionRecord> run() {
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.geComparator());
        Comparator<Process> comparator = 
            Process.geComparator("priority").reversed()
            .thenComparing(Process.geComparator());
            
        PriorityQueue<Process> pq = new PriorityQueue<>(comparator);

        int currentTime = processes.get(0).getPropety("arrivalTime");
        int index = addProcessesToQueue(pq, 0, currentTime);

        while (!pq.isEmpty()) {
            Process nextProcess = pq.poll();
            int burstTime = nextProcess.getPropety("burstTime");
            currentTime += burstTime;

            index = addProcessesToQueue(pq, index, currentTime);
            
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