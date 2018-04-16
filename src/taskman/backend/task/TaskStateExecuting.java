package taskman.backend.task;

import java.time.LocalDateTime;

/**
 * Class representing an executing task state.
 * Note: We apply here the State Pattern
 *
 * @author Jeroen Van Der Donckt
 */
public class TaskStateExecuting extends TaskState{

    /**
     * Creates a new Executing Task State.
     */
    public TaskStateExecuting(){
        setStatus("executing");
    }

    /**
     * Update the state of the given task to the given state
     *
     * @param task the task to update the state from
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @param taskStatus the new state of the task
     * @throws IllegalArgumentException the given state must be failed or finished
     * @post the state of the task is set to the given state and the time span of the task is set to a new time span created with given start and end time
     */
    @Override
    public void updateStatus(Task task, LocalDateTime startTime, LocalDateTime endTime, String taskStatus) throws IllegalArgumentException{
        if (!taskStatus.equals("finished") && !taskStatus.equals("failed")){
            throw new IllegalArgumentException("The new status must be either failed or finished");
        }
        task.setTimeSpan(startTime, endTime);
        switch (taskStatus){
            case "finished" : task.setState(new TaskStateFinished());
                break;
            case "failed" : task.setState(new TaskStateFailed());
                break;
        }
    }

}
