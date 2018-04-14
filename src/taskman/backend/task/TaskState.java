package taskman.backend.task;

import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;

import java.time.LocalDateTime;
import java.util.List;

public abstract class TaskState {

    // TODO: moet niet alles protected staan?

    private String status;

    /**
     * Returns the task state.
     *
     * @return the task state
     */
    public String getStatus(){
        return status;
    }

    /**
     * Sets the state to the given state.
     *
     * @param status the state of the task state
     * @post the state of the task is set to the given state
     */
    protected void setStatus(String status){
        this.status = status;
    }

    /**
     * Returns the delay of the given task.
     *
     * @param t the task to calculate the delay from
     * @return the delay from the given task
     * @throws IllegalStateException the task state must be finished to calculate the delay
     */
    public long getDelay(Task t) throws IllegalStateException {
        throw new IllegalStateException("Cannot calculate delay of task if not finished!");
    }

    /**
     * Update the state of the task to the given state.
     *
     * @param task the task to update the state from
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @param taskStatus the new state of the task
     * @throws IllegalStateException the task state must be finished
     * @post the state of the task is set to the given state and the time span of the task is set to a new time span created with given start and end time
     */
    public void updateStatus(Task task, LocalDateTime startTime, LocalDateTime endTime, String taskStatus) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("Cannot update status of task if not executing!");
    }

    /**
     * Sets the alternative of the task to the given alternative.
     *
     * @param task the task to set the alternative from
     * @param alternative the alternative of the task
     * @throws IllegalStateException the task must be failed
     * @post the alternative of the task is set to the given alternative
     */
    public void setAlternative(Task task, Task alternative) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("The task must be failed to set an alternative.");
    }

    /**
     * Adds a dependency to the task.
     *
     * @param task the task to add the dependency to
     * @param dependency the dependency to add to the task
     * @throws IllegalStateException the task must be unavailable
     * @post the dependency is added to the task
     */
    public void addDependency(Task task, Task dependency) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("The dependency must be added to an inactive task.");
        // TODO: is dit correct? Of moet het enkel een available task zijn
    }

    /**
     * Plans the task with given resources at the given start time.
     *
     * @param task the task to plan
     * @param resources the resources that are used in the plan
     * @param startTime the start time of the plan
     * @param resourceManager the resource manager of the system
     * @throws IllegalStateException the task must be unavailable
     */
    public void plan(Task task, List<Resource> resources, LocalDateTime startTime, ResourceManager resourceManager) throws IllegalStateException {
        throw new IllegalStateException("The task can only planned in unavailable state.");
    }

}
