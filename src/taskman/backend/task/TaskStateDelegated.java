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
     *
     */
    private Task originalTask;

    /**
     * Returns the delegated task.
     * @return the delegated task
     */
    public Task getDelegatedTask() {
        return originalTask;
    }

    /**
     * Sets the delegated task to the given task.
     * @param originalTask the delegated task
     * @post the delegated task is set to the given task
     */
    public void setDelegatedTask(Task originalTask) {
        this.originalTask = originalTask;
    }
}
