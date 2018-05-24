package taskman.backend.task;

import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.time.TimeSpan;

import java.time.LocalDateTime;
import java.util.List;

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
	 * @param task a task.
	 * @param resource a resource wrapper to search alternatives for.
	 * @return a list of resources as alternatives for the given resource.
	 */
	@Override
	public List<Resource> getAlternativeResources(Task task, Resource resource) {
		return resource.getAlternativeResources(task.getTimeSpan());
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
     * sets the status of a task to executing and tries to update the plan
     * @param task the task to make executing
     * @param resourceManager the resourceManager to pass to the plan
     * @param startTime the time when the task starts executing
     * @post the task is set to executing and if needed, a new plan is created.
     * @throws IllegalArgumentException if the plan cannot be rescheduled.
     */
    @Override
    public void execute(Task task, ResourceManager resourceManager, LocalDateTime startTime) throws IllegalStateException {
    	if (!isAvailable(task, startTime)) {
		    throw new IllegalStateException("The task must be available in order to start its execution.");
	    }
	    TimeSpan newTimeSpan = new TimeSpan(startTime, startTime.plusMinutes(task.getEstimatedDuration()));
		task.getPlan().reschedulePlan(newTimeSpan);
        task.setTimeSpan(startTime, startTime.plusMinutes(task.getEstimatedDuration()));
        task.setState(new TaskStateExecuting());
    }

	/**
	 * Returns if the planned task is available.
	 * @param task a task.
	 * @param startTime a start time.
	 * @return true if the planned task is available, otherwise false
	 */
	@Override
	public boolean isAvailable(Task task, LocalDateTime startTime){
		if (!task.getDependencies().stream().allMatch(Task::isFinished)) return false;
		return task.getPlan().canBeRescheduled(new TimeSpan(startTime, startTime.plusMinutes(task.getEstimatedDuration())));
	}

}
