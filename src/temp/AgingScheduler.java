package OSCourse.CPU;

import java.util.PriorityQueue;

public abstract class AgingScheduler extends Scheduler {
    int agingTime;
    AgingScheduler(int contextSwitchTime, int agingTime) {
        super(contextSwitchTime);
        this.agingTime = agingTime;
    }
    protected PriorityQueue<Process> age(PriorityQueue<Process> queue, int currentTime) {
        PriorityQueue<Process> newQueue = new PriorityQueue<>(queue.comparator());
        for (Process p: processes) {
            if ((currentTime - p.getPropety("arrivalTime")) % agingTime == 0) {
                p.setProperty("priority", p.getPropety("priority") + 1);
            }
            newQueue.add(p);
        }
        return newQueue;
    }
}
