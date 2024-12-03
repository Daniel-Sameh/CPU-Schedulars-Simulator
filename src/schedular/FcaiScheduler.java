package schedular;
import process.Process;
import java.util.ArrayList;

public class FcaiScheduler implements IScheduler {
    private ArrayList<Process> processes;
    public FcaiScheduler(ArrayList<Process> processes) {
        this.processes = processes;
    }
    @Override
    public void addProcess(Process process) {
        processes.add(process);
    }

    @Override
    public void run() {

    }

    @Override
    public void setContextSwitch(int contextSwitch) {

    }

    @Override
    public void setSchedularType(String schedularType) {

    }

    @Override
    public void setSchedularTime(int schedularTime) {

    }
}
