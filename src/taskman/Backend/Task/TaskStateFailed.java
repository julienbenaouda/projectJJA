package taskman.Backend.Task;

import java.time.LocalDateTime;

public class TaskStateFailed extends TaskState {

    public TaskStateFailed(){}

    @Override
    public boolean isFailed(){
        return true;
    }


    @Override
    public void setAlternative(Task task, Task alternative) throws IllegalArgumentException{
        if (Task.containsLoop(task, alternative)){
            throw new IllegalArgumentException("The alternative may not be one of the dependecies or the alternative of this or of its dependendecies recursivley.");
        }
        task.setAlternativeTask(alternative);
    }
}
