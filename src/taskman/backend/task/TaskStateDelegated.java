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
}
