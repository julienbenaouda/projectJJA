package taskman.backend.task;

public class TaskStateAvailable extends TaskState{

    /**
     * Creates a new Available Task State.
     */
    public TaskStateAvailable(){
        setStatus("available");
    }

    // TODO: transition to executing

}
