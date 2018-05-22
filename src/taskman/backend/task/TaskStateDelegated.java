package taskman.backend.task;

/**
 * Class representing a delegated task state.
 * Note: We apply here the State Pattern
 *
 * @author Jeroen Van Der Donckt
 */
public class TaskStateDelegated extends TaskState {

    /**
     * Creates a new Delegated Task State.
     */
    public TaskStateDelegated(){
        setStatus("delegated");
    }

    /**
     * Returns the status of the delegated task state.
     * @return delegated_ + the status of the delegated task
     */
    @Override
    public String getStatus(){
        return super.getStatus() + "_" + getDelegatedTask().getStatus();
    }

    /**
     *
     */
    private Task delegatedTask;

    /**
     * Returns the delegated task.
     * @return the delegated task
     */
    public Task getDelegatedTask() {
        return delegatedTask;
    }

    /**
     * Sets the delegated task to the given task.
     * @param originalTask the delegated task
     * @post the delegated task is set to the given task
     */
    public void setDelegatedTask(Task originalTask) {
        this.delegatedTask = originalTask;
    }

    /**
     * Sets the alternative of the task to the given alternative.
     *
     * @param task the task to set the alternative from
     * @param alternative the alternative of the task
     * @throws IllegalargumentException when the task contains a loop
     * @post the alternative of the task is set to the given alternative
     */
    @Override
    public void setAlternative(Task task, Task alternative) throws IllegalArgumentException {
    	if(delegatedTask.isFailed()) {
            if (Task.containsLoop(task, alternative)){
                throw new IllegalArgumentException("The alternative may not be one of the dependencies or the alternative of this or of its dependendecies recursivley.");
            }
            task.setAlternativeTask(alternative);
    	} else {
    		throw new IllegalStateException("the task is not in the correct state to add an alternative!");
    	}
    }
    
    /**
     * returns if a task is failed
     * @return true if the task is failed, else false
     */
    @Override
    boolean isFailed() {
    	return getDelegatedTask().isFailed();
    }
}
