package taskman.backend.task;

import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing an unavailable task state.
 * Note: We apply here the State Pattern
 *
 * @author Jeroen Van Der Donckt
 */
public class TaskStateUnavailable extends TaskState {

    /**
     * Creates a new Unavailable Task State.
     */
    public TaskStateUnavailable() {
        setStatus("unavailable");
    }

    /**
     * Adds a dependency to the given task.
     *
     * @param task the task to add the dependency to
     * @param dependency the dependency to add to the task
     * @throws IllegalArgumentException the dependency already occurs in the dependency graph
     * @post the dependency is added to the task
     */
    @Override
    public void addDependency(Task task, Task dependency) throws IllegalArgumentException {
        if (Task.containsLoop(task, dependency)) {
            throw new IllegalArgumentException("The dependency may not be one of the dependencies or the alternative of this or of its dependendecies recursivley.");
        }
        task.addDependencyTask(dependency);
    }

    /**
     * Returns if the state can be planned.
     * @return if the state can be planned.
     */
    @Override
    public boolean canBePlanned() {
        return true;
    }
    // TODO: moet isAvailable niet gechecked worden

    /**
     * Adds the given requirement to the task its requirements.
     *
     * @param resourceManager the resource manager of the system
     * @param task the task to add the requirement to its requirements
     * @param resourceType the resource type of the requirement
     * @param amount the amount of the requirement
     * @post the given requirement is added to the requirements of the task
     */
    @Override
    public void addRequirement(ResourceManager resourceManager, Task task, ResourceType resourceType, int amount){
        resourceManager.addRequirement(task.getPlan(), resourceType, amount);
        // TODO: moet task wel meegegeven worden
    }

    /**
     * Initializes a plan for this task.
     * @param task a task.
     * @param resourceManager a resource manager.
     * @param startTime the start time for the plan.
     */
    public void initializePlan(Task task, ResourceManager resourceManager, LocalDateTime startTime) {
        resourceManager.initializePlan(task.getPlan(), task.getEstimatedDuration(), startTime);
        task.setState(new TaskStatePlanned());
    }
    
    @Override
    public void delegate(BranchOffice branchOffice, Task task, LocalDateTime currentTime) {
    	Map<ResourceType, Integer> requirements = task.getPlan().cloneRequirements();
    	Task delegatedTask = branchOffice.executeDelegation(requirements, task.getName(), task.getDescription(), currentTime, task.getEstimatedDuration(), task.getAcceptableDeviation());
    	TaskStateDelegated delegatedState = new TaskStateDelegated();
    	delegatedState.setDelegatedTask(delegatedTask);
    	task.setState(delegatedState);
    }

}