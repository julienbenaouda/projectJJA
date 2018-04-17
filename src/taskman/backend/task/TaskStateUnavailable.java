package taskman.backend.task;

import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;

import java.time.LocalDateTime;
import java.util.List;

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
     * Plan the task with given arguments.
     *
     * @param resourceManager the resource manager of the system
     * @param task the task to plan
     * @param resources the resources that are used in the plan
     * @param startTime the start time of the plan
     * @post the new task will be planned and the task state will be set to planned
     */
    @Override
    public void plan(ResourceManager resourceManager, Task task, List<Resource> resources, LocalDateTime startTime) {
        resourceManager.plan(task.getPlan(), resources, startTime);
        task.setState(new TaskStatePlanned());
        // TODO: moet task wel meegegeven worden
    }

}