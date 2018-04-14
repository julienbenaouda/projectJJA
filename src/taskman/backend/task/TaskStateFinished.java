package taskman.backend.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskStateFinished extends TaskState {

    /**
     * Creates a new Finished Task State.
     */
    public TaskStateFinished(){
        setStatus("finished");
    }

    /**
     * Returns the delay of the task.
     *
     * @param task the task calculate the delay from
     * @return the delay of the task
     */
    @Override
    public long getDelay(Task task){
        return Math.round(Duration.between(task.getTimeSpan().getStartTime(), task.getTimeSpan().getEndTime()).toMinutes() - task.getEstimatedDuration()*(1 + task.getAcceptableDeviation()));
    }

}
