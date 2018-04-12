package taskman.backend.task;

import java.time.LocalDateTime;

public abstract class  TaskState {

    // TODO: moet niet alles protected staan?

    private String status;

    public String getStatus(){
        return status;
    }

    protected void setStatus(String status){
        this.status = status;
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

    public void planTask(Task task) throws IllegalStateException, IllegalArgumentException{
        throw new IllegalStateException("The task can only be planned in unavailable state.");
    }

}
