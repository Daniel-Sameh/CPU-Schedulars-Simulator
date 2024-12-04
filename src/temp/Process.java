package OSCourse.CPU;

import java.util.HashMap;
import java.util.Comparator;

public class Process {
    HashMap<String, Integer> properties;
    Process(int id, int arrivalTime, int burstTime) {
        properties = new HashMap<>();
        setProperty("id", id);
        setProperty("arrivalTime", arrivalTime);
        setProperty("burstTime", burstTime);
    }
    public void setProperty(String name, Integer value) {
        properties.put(name, value);
    }
    public int getPropety(String name) {
        return properties.get(name);
    }
    public static Comparator<Process> geComparator() {
        Comparator<Process> byArrivalTime = Comparator.comparingInt(p -> p.getPropety("arrivalTime"));
        return byArrivalTime;
    }
    public static Comparator<Process> geComparator(String property) {
        Comparator<Process> byProperty = Comparator.comparingInt(p -> p.getPropety(property));
        return byProperty;
    }
}