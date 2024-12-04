package OSCourse.CPU;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;

public class FCAIScheduler extends Scheduler {

    FCAIScheduler(int contextSwitchTime) {
        super(contextSwitchTime);
    }

    @Override
    public ArrayList<ExecutionRecord> run() {
        ArrayList<ExecutionRecord> records = new ArrayList<>(); 
        Collections.sort(processes, Process.geComparator());

        for (Process p: processes) {
            p.setProperty("remaining", p.getPropety("burstTime"));
        }

        double v1 = (double)processes.get(processes.size() - 1).getPropety("arrivalTime") / 10;
        double v2 = (double)Collections.max(processes, Comparator.comparingInt(p -> p.getPropety("burstTime"))).getPropety("burstTime") / 10;

        Comparator<Process> FCAIComparator = Comparator.comparingDouble(p -> calculateFCAIFactor(p, v1, v2));
        Deque<Process> queue = new ArrayDeque<>();

        int currentTime = processes.get(0).getPropety("arrivalTime");
        int index = addProcessesToQueue(queue, 0, currentTime);

        while (!queue.isEmpty()) {
            Process nextProcess = queue.poll();
            int oldQuantum = nextProcess.getPropety("quantum");
            int firstForty = (int)Math.ceil(0.4 * (double)oldQuantum);
            int runningTime = Math.min(firstForty, nextProcess.getPropety("remaining"));

            currentTime += runningTime;
            nextProcess.setProperty("remaining", nextProcess.getPropety("remaining") - runningTime);
            index = addProcessesToQueue(queue, index, currentTime);


            while (nextProcess.getPropety("remaining") > 0 && runningTime < oldQuantum) {
                
                index = addProcessesToQueue(queue, index, currentTime);

                if (!queue.isEmpty()) {
                    Process bestProcess = Collections.min(queue, FCAIComparator);
                    if (calculateFCAIFactor(nextProcess, v1, v2) > calculateFCAIFactor(bestProcess, v1, v2)) {
                        queue.remove(bestProcess);
                        queue.add(nextProcess);
                        queue.addFirst(bestProcess);
                        nextProcess.setProperty("quantum", 2 * oldQuantum - runningTime);
                        break;
                    }
                }
                currentTime++;
                runningTime++;
                nextProcess.setProperty("remaining", nextProcess.getPropety("remaining") - 1);
            }
            if (runningTime == oldQuantum && nextProcess.getPropety("remaining") > 0) {
                nextProcess.setProperty("quantum", oldQuantum + 2);
                queue.add(nextProcess);
            }

            records.add(new ExecutionRecord(nextProcess.getPropety("id"), currentTime - runningTime, runningTime));
            currentTime += contextSwitchTime;
            if (queue.isEmpty() && index < processes.size()) {
                Process tempProcess = processes.get(index++);
                currentTime = tempProcess.getPropety("burstTime");
                queue.add(tempProcess);
            }
            
        }
        return records;
    }
    int calculateFCAIFactor(Process process, double v1, double v2) {
        return (int)Math.ceil((10 - process.getPropety("priority")) + ((double)process.getPropety("arrivalTime") / v1) + ((double)process.getPropety("remaining") / v2));
    }
}
