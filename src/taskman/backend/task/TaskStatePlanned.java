package taskman.backend.task;

import java.time.LocalDateTime;

import taskman.backend.resource.Plan;
import taskman.backend.resource.ResourceManager;
import taskman.backend.time.TimeSpan;

/**
 * Class representing a planned task state.
 * Note: We apply here the State Pattern
 *
 * @author Jeroen Van Der Donckt
 */
public class TaskStatePlanned extends TaskState {

    /**
     * Creates a new Planned Task State.
     */
    public TaskStatePlanned(){
        setStatus("planned");
    }

    /**
     * sets the status of a task to executing and tries to update the plan
     * @param task the task to make executing
     * @param resourceManager the resourceManager to pass to the plan
     * @param startTime the time when the task starts executing
     * @post the task is set to executing and if needed, a new plan is created.
     */
    @Override
    public void execute(Task task, ResourceManager resourceManager, LocalDateTime startTime) throws IllegalStateException {
        if (isAvailable()) {
            Plan p = task.getPlan();
            p.makeExecuting(resourceManager, startTime, task.getEstimatedDuration());
            task.setTimeSpan(startTime, startTime.plusMinutes(task.getEstimatedDuration()));
            task.setState(new TaskStateExecuting());
        }
        else {
            throw new IllegalStateException("The task must be available in order to start its execution.");
        }
    }

    @Override
    public boolean isAvailable(Task task){
        // TODO
    }

}
