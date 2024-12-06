//package OSCourse.CPU;

import java.util.ArrayList;
import java.util.Scanner;

import Scheduler.ExecutionRecord;
import Scheduler.FCAIScheduler;
import Scheduler.SRTFScheduler;
import Scheduler.Scheduler;
import Scheduler.*;

import Process.Process;

public class Main {
    public static void main(String[] args) {
//        Scheduler scheduler = new FCAIScheduler(0);
//        Scanner scanner = new Scanner(System.in);
//        int id = 1;
//        while (true) {
//            int arrivalTime = scanner.nextInt();
//            if (arrivalTime < 0)
//                break;
//            int burstTime = scanner.nextInt();
//            int priority = scanner.nextInt();
//            int quantum = scanner.nextInt();
//            Process current = new Process(id++, arrivalTime, burstTime);
//            current.setProperty("priority", priority);
//            current.setProperty("quantum", quantum);
//            scheduler.addProcess(current);
//        }
//        ArrayList<ExecutionRecord> records = scheduler.run();
//        for (ExecutionRecord r: records) {
//            System.out.println("PID: " + r.pid);
//            System.out.println("StartTime: " + r.startTime);
//            System.out.println("Running Time: " + r.runningTime);
//            System.out.println("=======================================");
//        }
//        scanner.close();
        Scheduler scheduler = new SRTFScheduler(0,1);
        Process p = new Process(1, 0, 5);
        p.setProperty("priority", 200);
        scheduler.addProcess(p);

        Process p1 = new Process(2,2,6);
        p.setProperty("priority", 3);
        scheduler.addProcess(p1);

        Process p2 = new Process(3,3,10);
        p.setProperty("priority",300);
        scheduler.addProcess(p2);

        ArrayList<ExecutionRecord> records= scheduler.run();
        System.out.println("SRTF Scheduler Results:");
        for (ExecutionRecord record: records){
            System.out.println("PID: " + record.pid);
            System.out.println("StartTime: " + record.startTime);
            System.out.println("Running Time: " + record.runningTime);
            System.out.println("=======================================");
        }
    }
}
