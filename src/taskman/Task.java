package taskman;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Stack;


/**
 * This class represents a task.
 *
 * @author Jeroen Van Der Donckt
 */
public class Task {

    /**
     * Creates a new task with the given values.
     *
     * @param description         the task description
     * @param estimatedDuration   the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable  deviation of the task
     * @post a new task is created with the given attributes an available status
     */
    public Task(String description, Long estimatedDuration, Double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStatus(TaskStatus.AVAILABLE);
        dependencies = new ArrayList<>();
    }


    /**
     * Creates a new task with thte given values.
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable deviation of the task
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @post a new task is created with the given attributes
     */
    private Task(String description, Long estimatedDuration, Double acceptableDeviation, LocalDateTime startTime, LocalDateTime endTime, TaskStatus status) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStartTime(startTime);
        setEndTime(endTime);
        setStatus(status);
        dependencies = new ArrayList<>();
    }


    /**
     * The task description.
     */
    private String description;


    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription(){
        return description;
    }


    /**
     * Sets the task description to the given description.
     * @param description the description of the task
     * @post the task description is set to the given description
     */
    private void setDescription(String description){
        this.description = description;
    }



    /**
     * The estimated duration of the task in minutes.
     */
    private Long estimatedDuration;


    /**
     * Returns the estimated duration of the task in minutes.
     *
     * @return the estimated duration of the task in minutes
     */
    public Long getEstimatedDuration(){
        return estimatedDuration;
    }


    /**
     * Sets the estimated duration of the task to the given duration in minutes.
     * @param estimatedDuration the estimated duration of the task in minutes
     * @post the estimated duration of the task is set to the given duration
     */
    private void setEstimatedDuration(Long estimatedDuration){
        this.estimatedDuration = estimatedDuration;
    }



    /**
     * The acceptable deviation of the task.
     */
    private Double acceptableDeviation;


    /**
     * Returns the acceptable deviation of the task.
     *
     * @return the acceptable deviation of the task
     */
    public Double getAcceptableDeviation(){
        return acceptableDeviation;
    }

    /**
     * Sets the acceptable deviation of the task to the given deviation.
     * @param acceptableDeviation the acceptable deviation of the task
     * @post the acceptable deviation of the task is set to the given deviation
     */
    private void setAcceptableDeviation(Double acceptableDeviation){
        this.acceptableDeviation = acceptableDeviation;
    }


    /**
     * The start time of the task.
     */
    private LocalDateTime startTime;


    /**
     * Returns the start time of the task.
     *
     * @return the start time of the task
     */
    public LocalDateTime getStartTime(){
        return startTime;
    }


    /**
     * Sets the start time of the task to the given start time.
     * @param startTime the start time of the task
     * @post the start time of the task is set to the given start time
     */
    private void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }



    /**
     * The end time of the task.
     */
    private LocalDateTime endTime;


    /**
     * Returns the end time of the task.
     *
     * @return the end time of the task
     */
    public LocalDateTime getEndTime(){
        return endTime;
    }

    /**
     * Sets the end time of the task tot the given end time.
     * @param endTime the end time of the task
     * @throws IllegalArgumentException when the end time is not later than the start time
     * @post the end time of the task is set to the given end time
     */
    private void setEndTime(LocalDateTime endTime){
        if (endTime.compareTo(getStartTime()) <= 0) {
            throw new IllegalArgumentException("The end time can't be before or equal to the start time.");
        }
        this.endTime = endTime;
    }



    /**
     * The status of the task.
     */
    private TaskStatus status;


    /**
     * Returns the task status.
     *
     * @return the task status
     */
    public TaskStatus getStatus(){
        return status;
    }


    /**
     * Sets the task status to the given status.
     * @param status the task status
     * @post the task status is set to the given status
     */
    private void setStatus(TaskStatus status){
        this.status = status;
    }


    /**
     * Updates the status of the task.
     *
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @param status the new status of the task
     * @throws IllegalArgumentException when the status is not FINISHED and not FAILED or when the end time is before the start time or
     * @post the start time, end time and status of the task will be updated
     */
    public void updateStatus(LocalDateTime startTime, LocalDateTime endTime, TaskStatus status) throws IllegalArgumentException {
        if (status != TaskStatus.FINISHED && status != TaskStatus.FAILED){
            throw new IllegalArgumentException("The status may only be finished or failed.");
        }
        if (startTime.isAfter(endTime)){
            throw new IllegalArgumentException("The end time can't be before the start time.");
        }
        for (Task dependency: this.getDependencies()) {
            if (dependency.getStatus().isFinal() && startTime.isBefore(dependency.getEndTime())) {
                throw new IllegalArgumentException("The task must start after its dependencies!");
            }
        }
        setStartTime(startTime);
        setEndTime(endTime);
        setStatus(status);
    }


    /**
     * Return the delay between the end time and the estimated end time in minutes.
     *
     * @return the time between the end time and the estimated end time in minutes
     * @throws IllegalStateException if the task is not yet finished.
     */
    public Long getDelay() throws IllegalStateException {
        if (this.getStatus() != TaskStatus.FINISHED) {
            throw new IllegalStateException("Cannot calculate delay of task if not fininshed!");
        }
        else {
            return Duration.between(getStartTime(), getEndTime()).toMinutes() - getEstimatedDuration();
        }
    }


    /**
     * The alternative task of the task.
     */
    private Task alternative;


    /**
     * Returns the alternative task of the task.
     *
     * @return the alternative task
     */
    public Task getAlternative(){
        return alternative;
    }


    /**
     * Sets the alternative task of the task to the given task.
     *
     * @param alternative the alternative task of the task
     * @throws IllegalStateException the task must be failed to set the alternative task
     * @throws IllegalArgumentException the alternative may not be this task or its alternative or one of its dependencies or one of these alternatives recursively
     * @post the alternative task of the task is set to the given task
     */
    public void setAlternative(Task alternative) throws IllegalStateException, IllegalArgumentException {
        if (getStatus() != TaskStatus.FAILED){
            throw new IllegalStateException("The task must be failed to set an alternative.");
        }
        if (containsLoop(this, alternative)){
            throw new IllegalArgumentException("The alternative may not be one of the dependecies or the alternative of this or of its dependendecies recursivley");
        }
        this.alternative = alternative;             // may be null if there is no alternative
    }


    /**
     * The dependencies of the task.
     */
    private ArrayList<Task> dependencies;


    /**
     * Returns a list with all dependencies of the task.
     *
     * @return the dependencies of the task
     */
    public ArrayList<Task> getDependencies(){
        return (ArrayList<Task>) this.dependencies.clone();
    }


    /**
     * Sets the dependencies of the task.
     * @param dependencies list of dependent tasks of the task
     * @post the dependencies is set to the given dependent tasks
     */
    private void setDependencies(ArrayList<Task> dependencies){
        this.dependencies = dependencies;
    }


    /**
     * Adds a dependency to the task.
     *
     * @param dependency task that needs to be added to the task
     * @throws IllegalArgumentException when the dependency is this task or its alternative or one of its dependencies or one of these alternatives recursively
     * @throws IllegalStateException if the task is already finished or failed
     * @post the dependency is added to the task, the status of the task is updated accordingly
     */
    public void addDependency(Task dependency) throws IllegalArgumentException, IllegalStateException {
        if (containsLoop(this, dependency)){
            throw new IllegalArgumentException("The alternative may not be one of the dependencies or the alternative of this or of its dependendecies recursivley");
        }
        if (getStatus() == TaskStatus.FAILED || getStatus() == TaskStatus.FINISHED){
            throw new IllegalStateException("No dependencies may be added to failed or finished tasks.");
        }
        if (dependency.getStatus() == TaskStatus.AVAILABLE || dependency.getStatus() == TaskStatus.UNAVAILABLE){
            setStatus(TaskStatus.UNAVAILABLE);
        }
        else if (dependency.getStatus() == TaskStatus.FAILED){
            Task alternative = dependency;
            while (alternative.getAlternative() !=  null && alternative.getStatus() == TaskStatus.FAILED){
                alternative = alternative.getAlternative();
            }
            if (alternative.getStatus() != TaskStatus.FINISHED){
                setStatus(TaskStatus.UNAVAILABLE);
            }
        }
        dependencies.add(dependency);
    }

    /**
     * Restores the dependencies of the task without checks.
     *
     * @param dependencies an ArrayList of Task on which this task is dependant.
     */
    public void restoreDependencies(ArrayList<Task> dependencies) { // TODO: wordt deze functie wel echt gebruikt of moet die in de andere public constructor gebruikt worden
        this.setDependencies(dependencies);
    }

    /**
     * Removes dependency of the given task.
     *
     * @param dependency task that needs to be removed as dependency of the task
     * @throws IllegalArgumentException the dependency task must be a dependency of the task
     * @post the dependency is deleted from the task and the task status is updated accordingly
     */
    public void removeDependency(Task dependency){
        if (! dependencies.contains(dependency)){
            throw new IllegalArgumentException("The given task is not a dependency of the task.");
        }
        dependencies.remove(dependency);
        if (dependency.getStatus() == TaskStatus.AVAILABLE || dependency.getStatus() == TaskStatus.UNAVAILABLE){
            boolean becomesAvailable = true;
            for (Task d : getDependencies()){
                if (d.getStatus() == TaskStatus.AVAILABLE || d.getStatus() == TaskStatus.UNAVAILABLE){
                    becomesAvailable = false;
                }
                else if (d.getStatus() == TaskStatus.FAILED){
                    Task alternative = d;
                    while (alternative.getAlternative() != null && alternative.getStatus() == TaskStatus.FAILED){
                        alternative = alternative.getAlternative();
                    }
                    if (alternative.getStatus() != TaskStatus.FINISHED){
                        becomesAvailable = false;
                    }
                }
            }
            if (becomesAvailable){
                setStatus(TaskStatus.AVAILABLE);
            }
        }
    }



    // LOOP CHECKING CODE

    /**
     * Checks if the searched task is equal to the root or one of its dependencies or alternatives (recursively).
     * @param root the root task whose dependencies and alternatives will be further searched
     * @param searchedTask the task we want to search for
     * @return true if the task is found, false otherwise
     */
    private static boolean containsLoop(Task root, Task searchedTask){
        Stack<Task> searchStack = new Stack<>();
        searchStack.push(root);
        if (root.getAlternative() != null) {
            searchStack.push(root.getAlternative());
        }

        while (! searchStack.isEmpty()){
            Task task = searchStack.pop();
            if (task == searchedTask) {
                return true;
            }

            if (task.getAlternative() != null){
                searchStack.push(task.getAlternative());
            }
            for (Task dependency : task.getDependencies()){
                searchStack.push(dependency);
            }
        }
        return false;
    }

}
