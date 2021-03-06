package taskman.backend.task;

import java.time.Duration;

/**
 * Class representing a finished task state.
 * A task is in this state when it is finished. 
 * Note: We apply here the State Pattern
 *
 * @author Jeroen Van Der Donckt
 */
public class TaskStateFinished extends TaskState {

    /**
     * Creates a new Finished Task State.
     */
    public TaskStateFinished(){
        setStatus("finished");
    }

    /**
     * Returns if this task is finished.
     * @return if this task is finished.
     */
    @Override
    public boolean isFinished() {
        return true;
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
