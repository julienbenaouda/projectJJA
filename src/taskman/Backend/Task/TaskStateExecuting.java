package taskman.Backend.Task;

import java.time.LocalDateTime;

public class TaskStateExecuting extends TaskState{

    public TaskStateExecuting(){}

    @Override
    public boolean isExecuting(){
        return true;
    }

    @Override
    public void updateStatus(Task task, LocalDateTime startTime, LocalDateTime endTime, String taskStatus) throws IllegalArgumentException{
        if (!taskStatus.equals("finished") && !taskStatus.equals("failed")){
            throw new IllegalArgumentException("The new status must be either failed or finished");
        }
        task.setTimeSpan(startTime, endTime);
        switch (taskStatus){
            case "finished" : task.setState(new TaskStateFinished());
                break;
            case "failed" : task.setState(new TaskStateFailed());
                break;
        }
    }

}
