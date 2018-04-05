package taskman;

import java.time.Duration;
import java.util.ArrayDeque;
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
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable  deviation of the task
     * @post a new task is created with the given attributes an available status
     */
    public Task(String description, long estimatedDuration, double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        timeSpans = new ArrayDeque<>();
        initializeTimeSpan(TaskStatus.AVAILABLE);
        dependencies = new ArrayList<>();
    }


    /**
     * Creates a new task with thte given values.
     *
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable deviation of the task
     * @param timeSpan the time span of the task
     * @post a new task is created with the given attributes
     */
    private Task(String description, long estimatedDuration, double acceptableDeviation, TimeSpan timeSpan) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        timeSpans = new ArrayDeque<>();
        addTimeSpan(timeSpan);
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
    private long estimatedDuration;


    /**
     * Returns the estimated duration of the task in minutes.
     *
     * @return the estimated duration of the task in minutes
     */
    public long getEstimatedDuration(){
        return estimatedDuration;
    }


    /**
     * Sets the estimated duration of the task to the given duration in minutes.
     *
     * @param estimatedDuration the estimated duration of the task in minutes
     * @post the estimated duration of the task is set to the given duration
     */
    private void setEstimatedDuration(long estimatedDuration){
        this.estimatedDuration = estimatedDuration;
    }


    /**
     * The acceptable deviation of the task.
     */
    private double acceptableDeviation;


    /**
     * Returns the acceptable deviation of the task.
     *
     * @return the acceptable deviation of the task
     */
    public double getAcceptableDeviation(){
        return acceptableDeviation;
    }

    /**
     * Sets the acceptable deviation of the task to the given deviation.
     *
     * @param acceptableDeviation the acceptable deviation of the task
     * @post the acceptable deviation of the task is set to the given deviation
     */
    private void setAcceptableDeviation(double acceptableDeviation){
        this.acceptableDeviation = acceptableDeviation;
    }


    /**
     * The start time of the task.
     */
    private ArrayDeque<TimeSpan> timeSpans;

    /**
     * Returns the time spans of the task.
     *
     * @return the time spans of the task
     */
    public ArrayDeque<TimeSpan> getTimeSpans(){
        return timeSpans.clone();
    }

    /**
     * Initializes the time spans of the task with a time span with the given status
     *
     * @param status the status of the time span
     * @post a new time span is added to the time spans
     */
    private void initializeTimeSpan(TaskStatus status){
        TimeSpan timeSpan = new TimeSpan(status);
        timeSpans.push(timeSpan);
    }

    /**
     * Adds the given time span to the task.
     *
     * @param timeSpan the time span of the task
     * @post the time span of the task is added to the task.
     */
    private void addTimeSpan(TimeSpan timeSpan){
        timeSpans.push(timeSpan);
    }
    // TODO: in TimeSpan moet er gechecked worden of het wel een valid timespan is

    /**
     * Returns the last or current time span of the task.
     *
     * @return the latest time span of the task
     */
    public TimeSpan getLastTimeSpan(){
        return timeSpans.getFirst();
    }

    /**
     * Returns if the task is finished.
     *
     * @return true of the task is finished, otherwise false
     */
    public boolean isFinished(){
        return getLastTimeSpan().getStatus() == TaskStatus.FINISHED;
    }


    /**
     * Updates the status of the task.
     *
     * @param timeSpan the time span of the task
     * @throws IllegalArgumentException when the status is not FINISHED and not FAILED or when the timeSpan is invalid
     * @post the start time, end time and status of the task will be updated
     */
    public void updateStatus(TimeSpan timeSpan) throws IllegalArgumentException {
        if (timeSpan.getStatus() != TaskStatus.FINISHED && timeSpan.getStatus() != TaskStatus.FAILED){
            throw new IllegalArgumentException("The status may only be finished or failed.");
        }
        for (Task dependency: this.getDependencies()) {
            if (dependency.getLastTimeSpan().getStatus().isFinal() && timeSpan.getStartTime().isBefore(dependency.getLastTimeSpan().getEndTime())) {
                throw new IllegalArgumentException("The task must start after its dependencies!");
            }
        }
        addTimeSpan(timeSpan);
    }


    /**
     * Return the delay between the end time and the estimated end time in minutes.
     *
     * @return the time between the end time and the estimated end time in minutes
     * @throws IllegalStateException if the task is not yet finished.
     */
    public Long getDelay() throws IllegalStateException {
        if (this.getLastTimeSpan().getStatus() != TaskStatus.FINISHED) {
            throw new IllegalStateException("Cannot calculate delay of task if not finished!");
        }
        else {
            return Duration.between(getLastTimeSpan().getStartTime(), getLastTimeSpan().getEndTime()).toMinutes() - getEstimatedDuration();
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
        if (getLastTimeSpan().getStatus() != TaskStatus.FAILED){
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
     *
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
        if (getLastTimeSpan().getStatus() == TaskStatus.FAILED || getLastTimeSpan().getStatus() == TaskStatus.FINISHED){
            throw new IllegalStateException("No dependencies may be added to failed or finished tasks.");
        }
        if (dependency.getLastTimeSpan().getStatus() == TaskStatus.AVAILABLE || dependency.getLastTimeSpan().getStatus() == TaskStatus.UNAVAILABLE){
            TimeSpan timeSpan = new TimeSpan(TaskStatus.UNAVAILABLE);
            addTimeSpan(timeSpan);
        }
        else if (dependency.getLastTimeSpan().getStatus() == TaskStatus.FAILED){
            Task alternative = dependency;
            while (alternative.getAlternative() !=  null && alternative.getLastTimeSpan().getStatus() == TaskStatus.FAILED){
                alternative = alternative.getAlternative();
            }
            if (alternative.getLastTimeSpan().getStatus() != TaskStatus.FINISHED){
                TimeSpan timeSpan = new TimeSpan(TaskStatus.UNAVAILABLE);
                addTimeSpan(timeSpan);
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
        if (dependency.getLastTimeSpan().getStatus() == TaskStatus.AVAILABLE || dependency.getLastTimeSpan().getStatus() == TaskStatus.UNAVAILABLE){
            boolean becomesAvailable = true;
            for (Task d : getDependencies()){
                if (d.getLastTimeSpan().getStatus() == TaskStatus.AVAILABLE || d.getLastTimeSpan().getStatus() == TaskStatus.UNAVAILABLE){
                    becomesAvailable = false;
                }
                else if (d.getLastTimeSpan().getStatus() == TaskStatus.FAILED){
                    Task alternative = d;
                    while (alternative.getAlternative() != null && alternative.getLastTimeSpan().getStatus() == TaskStatus.FAILED){
                        alternative = alternative.getAlternative();
                    }
                    if (alternative.getLastTimeSpan().getStatus() != TaskStatus.FINISHED){
                        becomesAvailable = false;
                    }
                }
            }
            if (becomesAvailable){
                TimeSpan timeSpan = new TimeSpan(TaskStatus.UNAVAILABLE);
                addTimeSpan(timeSpan);
            }
        }
    }



    // LOOP CHECKING CODE

    /**
     * Checks if the searched task is equal to the root or one of its dependencies or alternatives (recursively).
     * 
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
