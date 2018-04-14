package taskman.backend.task;

import java.time.LocalDateTime;

public class TaskStateFailed extends TaskState {

    /**
     * Creates a new Failed Task State.
     */
    public TaskStateFailed(){
        setStatus("failed");
    }

    /**
     * Sets the alternative of the task to the given task.
     *
     * @param task the task to set the alternative from
     * @param alternative the alternative of the task
     * @throws IllegalArgumentException the alternative may not occur in the dependency graph.
     */
    @Override
    public void setAlternative(Task task, Task alternative) throws IllegalArgumentException{
        if (Task.containsLoop(task, alternative)){
            throw new IllegalArgumentException("The alternative may not be one of the dependecies or the alternative of this or of its dependendecies recursivley.");
        }
        task.setAlternativeTask(alternative);
    }
}
