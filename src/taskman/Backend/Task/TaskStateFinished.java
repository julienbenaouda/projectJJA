package taskman.Backend.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskStateFinished extends TaskState {

    public TaskStateFinished(){}

    @Override
    public boolean isFinished(){
        return true;
    }


    @Override
    public long getDelay(Task task){
        return Math.round(Duration.between(task.getTimeSpan().getStartTime(), task.getTimeSpan().getEndTime()).toMinutes() - task.getEstimatedDuration()*(1 + task.getAcceptableDeviation()));
    }

}
