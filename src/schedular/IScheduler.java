package schedular;
import process.Process;

import java.util.ArrayList;

public interface IScheduler {
    void addProcess(Process process);

    void run();
    void setContextSwitch(int contextSwitch);
    void setSchedularType(String schedularType);
    void setSchedularTime(int schedularTime);

}
