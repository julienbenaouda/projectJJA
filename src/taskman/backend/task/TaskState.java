package taskman.backend.task;

import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface representing the task state.
 * Note: We apply here the State Pattern
 *
 * @author Jeroen Van Der Donckt
 */
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
     * Returns if the state can be planned.
     * @return if the state can be planned.
     */
    public boolean canBePlanned() {
        return false;
    }

    /**
     * Returns if the state can be updated.
     * @return if the state can be update.
     */
    public boolean canBeUpdated() {
        return false;
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
     * Makes the task executing
     * @param task the task to change to executing
     * @param resourceManager the resourceManager to check for availability
     * @throws IllegalStateException when the task is not in the correct state
     */
    public void execute(Task task, ResourceManager resourceManager, LocalDateTime startTime) throws IllegalStateException {
    	throw new IllegalStateException("A task can only be executed when it is in the planned state!");
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
    public void endExecution(Task task, LocalDateTime startTime, LocalDateTime endTime, String taskStatus) throws IllegalStateException, IllegalArgumentException {
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
        throw new IllegalStateException("The dependency must be added to an unavailable task.");
    }

    /**
     * Adds the given requirement to the requirements of the given task.
     *
     * @param resourceManager the resource manager of the system
     * @param task the task to add the requirement to its requirements
     * @param resourceType the resource type of the requirement
     * @param amount the amount of the requirement
     * @throws IllegalStateException
     * @post the requirement is added to the requirements of the task
     */
    public void addRequirement(ResourceManager resourceManager, Task task, ResourceType resourceType, int amount) throws IllegalStateException {
        throw new IllegalStateException("The requirement must be added to an unavailable task.");
    }

    /**
     * Returns if the planned task is available.
     * @param resourceManager a resource manager.
     * @param task a task.
     * @param startTime a start time.
     * @return true if the planned task is available, otherwise false
     */
    public boolean isAvailable(ResourceManager resourceManager, Task task, LocalDateTime startTime){
        return false;
    }

    /**
     * Returns if this task is finished.
     * @return if this task is finished.
     */
    public boolean isFinished() {
        return false;
    }

    /**
     * Initializes a plan for this state.
     * @param task a task.
     * @param resourceManager a resource manager.
     * @param startTime the start time for the plan.
     * @throws IllegalStateException if the state is not unavailable.
     */
    public void initializePlan(Task task, ResourceManager resourceManager, LocalDateTime startTime) throws IllegalStateException {
        throw new IllegalStateException("Task can only be planned in unavailable state!");
    }

    /**
     * Get the resources of the plan of a task.
     * @param task a task.
     * @return a list of resources.
     * @throws IllegalStateException if the state is not planned.
     */
    public List<Resource> getPlannedResources(Task task) throws IllegalStateException {
        throw new IllegalStateException("Task must be in planned state!");
    }

    /**
     * Returns a list of resources as alternatives for the given resource.
     * @param resourceManager a resource manager.
     * @param task a task.
     * @param resource a resource wrapper to search alternatives for.
     * @return a list of resources as alternatives for the given resource.
     * @throws IllegalStateException if the state is not planned.
     */
    public List<Resource> getAlternativeResources(ResourceManager resourceManager, Task task, Resource resource) throws IllegalStateException {
        throw new IllegalStateException("Task must be in planned state!");
    }

    /**
     * Change a resource of a plan of a task.
     * @param task a task.
     * @param oldResource the resource to change.
     * @param newResource the resource to change to.
     * @throws IllegalStateException if the state is not planned.
     */
    public void changeResource(Task task, Resource oldResource, Resource newResource) throws IllegalStateException {
        throw new IllegalStateException("Task must be in planned state!");
    }

    /**
     * Cancel the plan of this task.
     * @param task a task.
     * @throws IllegalStateException if the state is not planned.
     */
    public void cancelPlan(Task task) throws IllegalStateException {
        throw new IllegalStateException("Task must be in planned state!");
    }
    
    /**
     * delegates the task to the given branch office
     * @param branchOffice the branch office to delegate the task to
     * @param task the task to delegate
     * @throws IllegalStateException when the task is not in the correct state to be delegated
     */
    public void delegate(BranchOffice branchOffice, Task task) throws IllegalStateException {
    	throw new IllegalStateException("deleegating a task in this state is not possible!");
    }

}
