package Process;

import java.util.HashMap;
import java.util.Comparator;

public class Process {
    HashMap<String, Integer> properties;
    public Process(int id, int arrivalTime, int burstTime) {
        properties = new HashMap<>();
        setProperty("id", id);
        setProperty("arrivalTime", arrivalTime);
        setProperty("burstTime", burstTime);
    }
    public void setProperty(String name, Integer value) {
        properties.put(name, value);
    }
    public int getProperty(String name) {
        if (!properties.containsKey(name)) {
            return -1;
        }
        return properties.get(name);
    }
    public static Comparator<Process> getComparator() {
        Comparator<Process> byArrivalTime = Comparator.comparingInt(p -> p.getProperty("arrivalTime"));
        return byArrivalTime;
    }
    public static Comparator<Process> getComparator(String property) {
        Comparator<Process> byProperty = Comparator.comparingInt(p -> p.getProperty(property));
        return byProperty;
    }
}