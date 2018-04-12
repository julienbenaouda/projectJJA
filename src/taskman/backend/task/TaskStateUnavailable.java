package taskman.backend.task;

public class TaskStateUnavailable extends TaskState {

    public TaskStateUnavailable(){
        setStatus("unavailable");
    }

    @Override
    public void addDependency(Task task, Task dependency) throws IllegalArgumentException {
        if (Task.containsLoop(task, dependency)){
            throw new IllegalArgumentException("The alternative may not be one of the dependencies or the alternative of this or of its dependendecies recursivley.");
        }
        task.addDependencyTask(dependency);
    }
}
