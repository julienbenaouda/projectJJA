package taskman.backend.task;

import java.time.LocalDateTime;

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
