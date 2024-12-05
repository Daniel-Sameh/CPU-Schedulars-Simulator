package OSCourse.CPU;

import java.util.ArrayList;
import java.util.Scanner;

import OSCourse.CPU.Scheduler.ExecutionRecord;
import OSCourse.CPU.Scheduler.FCAIScheduler;
import OSCourse.CPU.Scheduler.Scheduler;

import OSCourse.CPU.Process.Process;

public class Main {
    public static void main(String[] args) {
        Scheduler scheduler = new FCAIScheduler(0);
        Scanner scanner = new Scanner(System.in);
        int id = 1;
        while (true) {
            int arrivalTime = scanner.nextInt();
            if (arrivalTime < 0)
                break;
            int burstTime = scanner.nextInt();
            int priority = scanner.nextInt();
            int quantum = scanner.nextInt();
            Process current = new Process(id++, arrivalTime, burstTime);
            current.setProperty("priority", priority);
            current.setProperty("quantum", quantum);
            scheduler.addProcess(current);
        }
        ArrayList<ExecutionRecord> records = scheduler.run();
        for (ExecutionRecord r: records) {
            System.out.println("PID: " + r.pid);
            System.out.println("StartTime: " + r.startTime);
            System.out.println("Running Time: " + r.runningTime);
            System.out.println("=======================================");
        }
        scanner.close();
    }
}
