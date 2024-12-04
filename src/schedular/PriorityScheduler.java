package schedular;
import process.Process;

import java.util.ArrayList;
import java.util.Iterator;

public class PriorityScheduler implements IScheduler{
    private ArrayList<Process> processes;
    private ArrayList<Process> readyQueue;
    private int currentTime;
    private int contextSwitch;
    private float averageWaitingTime;
    private float averageTurnaroundTime;
    public PriorityScheduler(ArrayList<Process> processes) {
        this.processes = processes;
        this.currentTime = 0;
        this.averageWaitingTime = 0;
        this.averageTurnaroundTime = 0;
        this.readyQueue = new ArrayList<>();
    }
    @Override
    public void addProcess(Process process) {
        processes.add(process);
    }
    @Override
    public void run() {
        System.out.println("Running Priority Scheduler");
        int processCount = processes.size();
        while (!processes.isEmpty()){
            System.out.println("Current Time: " + currentTime);
            Boolean ok = false;
            Iterator<Process> iterator = processes.iterator();
            while (iterator.hasNext()) {
                Process process = iterator.next();
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    iterator.remove();
                }
            }
            readyQueue.sort((p1, p2) -> Integer.compare(p1.getPriority(),p2.getPriority()));
            Iterator<Process> iterator1 = readyQueue.iterator();
            while (iterator1.hasNext()){
                Process process = iterator1.next();
                if (process.getArrivalTime() <= currentTime){
                    process.run();
                    System.out.println("Waiting Time= "+(currentTime));
                    System.out.println("Turnaround Time= "+(currentTime+process.getBurstTime()));
                    averageWaitingTime += currentTime;
                    averageTurnaroundTime += currentTime + process.getBurstTime();
                    currentTime += process.getBurstTime();
                    System.out.println("Priority process with pid " + process.getPid() +" Completed");
                    currentTime += contextSwitch;
                    ok = true;
                }else{
                    processes.add(process);
                }
                iterator1.remove();

            }
            if (!ok){
                ++currentTime;
            }else ok = false;

        }
        averageWaitingTime /= processCount;
        averageTurnaroundTime /= processCount;
        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
    @Override
    public void setContextSwitch(int contextSwitch) {
        this.contextSwitch = contextSwitch;
        System.out.println("Setting context switch to " + contextSwitch);
    }
    @Override
    public void setSchedularType(String schedularType) {
        System.out.println("Setting schedular type to " + schedularType);
    }
    @Override
    public void setSchedularTime(int schedularTime) {
        System.out.println("Setting schedular time to " + schedularTime);
    }
}
