package taskman.backend.task;

import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;

import java.time.LocalDateTime;
import java.util.List;

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
     * Plan the task with given arguments.
     *
     * @param task the task to plan
     * @param resources the resources that are used in the plan
     * @param startTime the start time of the plan
     * @param resourceManager the resource manager of the system
     * @post the new task will be planned and the task state will be set to planned
     */
    @Override
    public void plan(Task task, List<Resource> resources, LocalDateTime startTime, ResourceManager resourceManager) {
        resourceManager.plan(task, resources, startTime);
        task.setState(new TaskStatePlanned());
    }

}