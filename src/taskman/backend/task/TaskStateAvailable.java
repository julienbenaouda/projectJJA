package taskman.backend.task;

/**
 * Class representing an available task state.
 * Note: We apply here the State Pattern
 *
 * @author Jeroen Van Der Donckt
 */
public class TaskStateAvailable extends TaskState{

    /**
     * Creates a new Available Task State.
     */
    public TaskStateAvailable(){
        setStatus("available");
    }

    // TODO: transition to executing

}
