package taskman.Backend.Task;

import java.time.LocalDateTime;

public abstract class  TaskState {

    // TODO: moet niet alles protected staan?

    public  boolean isAvailable(Task task){
        return false;
    }

    public boolean isUnavailable(Task task){
        return false;
    }

    public boolean isExecuting(){
        return false;
    }

    public boolean isFinished(){
        return false;
    }

    public boolean isFailed(){
        return false;
    }

    public long getDelay(Task t) throws IllegalStateException {
        throw new IllegalStateException("Cannot calculate delay of task if not finished!");
    }

    public void updateStatus(Task task, LocalDateTime startTime, LocalDateTime endTime, String taskStatus) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("Cannot update status of task if not executing!");
    }

    public void setAlternative(Task task, Task alternative) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("The task must be failed to set an alternative.");
    }

    public void addDependency(Task task, Task dependency) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalStateException("The dependency must be added to an inactive task.");
        // TODO: is dit correct? Of moet het enkel een available task zijn
    }

}
