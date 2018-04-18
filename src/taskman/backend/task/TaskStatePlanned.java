package taskman.backend.task;

import java.time.LocalDateTime;
import java.util.List;

import taskman.backend.resource.Resource;
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
	 * Returns if the state can be updated.
	 * @return if the state can be update.
	 */
	@Override
	public boolean canBeUpdated() {
		return true;
	}

	/**
	 * Get the resources of the plan of a task.
	 * @param task a task.
	 * @return a list of resources.
	 */
	@Override
	public List<Resource> getPlannedResources(Task task) {
		return task.getPlan().getPlannedResources();
	}

	/**
	 * Returns a list of resources as alternatives for the given resource.
	 * @param resourceManager a resource manager.
	 * @param task a task.
	 * @param resource a resource wrapper to search alternatives for.
	 * @return a list of resources as alternatives for the given resource.
	 */
	public List<Resource> getAlternativeResources(ResourceManager resourceManager, Task task, Resource resource) {
		return resourceManager.getAlternativeResources(resource, task.getEstimatedDuration(), task.getTimeSpan().getStartTime());
	}

	/**
	 * Change a resource of a plan of a task.
	 * @param task a task.
	 * @param oldResource the resource to change.
	 * @param newResource the resource to change to.
	 */
	public void changeResource(Task task, Resource oldResource, Resource newResource) {
		task.getPlan().changeResource(oldResource, newResource);
	}

	/**
	 * Cancel the plan of a task.
	 * @param task a task.
	 */
	public void cancelPlan(Task task) {
		task.getPlan().emptyPlan();
		task.setState(new TaskStateUnavailable());
	}

    /**
     * makes a task available
     * @param task the task to make available
     * @param startTime the start time to set the tasks time span to
     * @throws IllegalStateException when one of the tasks dependencies is not yet finished
     * @post the state of the task is set to available
     */
    @Override
    public void makeAvailable(Task task) {
    	for(Task t: task.getDependencies()) {
    		if(!t.getStatus().equals("finished")) {
    			throw new IllegalStateException("The state of the task can't be changed because one of its dependencies is not yet finished");
    		}
    	}
    	task.setState(new TaskStateAvailable());
    }

}
