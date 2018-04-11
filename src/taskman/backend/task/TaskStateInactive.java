package taskman.backend.task;

import java.util.Stack;

public class TaskStateInactive extends TaskState{

    public TaskStateInactive(){
        setStatus("inactive");
    }

    @Override
    public boolean isAvailable(Task task){
        Stack<Task> checkStack = new Stack<>();
        checkStack.push(task);
        while (!checkStack.isEmpty()) {
            Task t = checkStack.pop();
            for (Task d : t.getDependencies()){
                if (d.isFailed()){
                    Task alternative = d;
                    while (alternative.getAlternative() != null && alternative.isFailed()){
                        alternative = alternative.getAlternative();
                    }
                    if (!alternative.isFinished()){
                        return false;
                    }
                    else{
                        checkStack.push(d);
                        checkStack.push(alternative);
                    }
                }
                else if (d.isFinished()){
                    checkStack.push(d);
                }
                else{
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isUnavailable(Task task){
        return !isAvailable(task);
    }

    @Override
    public void addDependency(Task task, Task dependency) throws IllegalArgumentException {
        if (Task.containsLoop(task, dependency)){
            throw new IllegalArgumentException("The alternative may not be one of the dependencies or the alternative of this or of its dependendecies recursivley.");
        }
        task.addDependencyTask(dependency);
    }
}
