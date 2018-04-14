package taskman.backend.task;

public class TaskStatePlanned extends TaskState {

    /**
     * Creates a new Planned Task State.
     */
    public TaskStatePlanned(){
        setStatus("planned");
    }

    // TODO: transition to available

}
