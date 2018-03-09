package taskman;

import org.w3c.dom.Element;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


/**
 * This class represents a task.
 * @author Jeroen Van Der Donckt
 *
 */
public class Task implements Comparable<Object> {

    /**
     * Creates a new task with the given values.
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable  deviation of the task
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @post a new task is created with the given attributes
     */
    public Task(String description, Duration estimatedDuration, Double acceptableDeviation, String startTime, String endTime)
    {
        setID();
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStartTime(startTime);
        setEndTime(endTime);
        dependencies = new ArrayList<>();
    }

    public Task(HashMap<String, String> form) {
        throw new NotImplementedException(); // TODO
    }



    /**
     * The latest task ID.
     */
    private static Integer lastTaskID = 0;


    /**
     * Return the ID of the latest task. (This should be the maximum of all ID's)
     * @return the ID of the latest task
     */
    public static Integer getLastTaskID() { return Integer.valueOf(lastTaskID.intValue()); }


    /**
     * Sets the last task ID of task.
     * @param ID the ID of the last task ID
     * @post the last task ID is set to the given ID
     */
    private static void setLastTaskID(Integer ID) { lastTaskID = ID; }



    /**
     * The task ID.
     */
    private Integer ID;


    /**
     * Return the ID of the task.
     * @return the ID of the task
     */
    public Integer getID(){
        return ID;
    }


    /**
     * Sets the ID of the task.
     * @post the last task ID is incremented by 1
     * @post the ID of the task is set to new last task ID
     */
    private void setID(){
        ID = getLastTaskID() + 1;
        setLastTaskID(ID);
    }



    /**
     * The task description.
     */
    private String description;


    /**
     * Returns the task description.
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
     * The estimated duration of the task.
     */
    private Duration estimatedDuration; // TODO: aangezien er enkel minuten doorgegeven wordt, ist dan nie beter om da als int fzo bij te houden?


    /**
     * Returns the estimated duration of the task.
     * @return the estimated duration of the task
     */
    public Duration getEstimatedDuration(){
        return estimatedDuration; // TODO: is het wel een slim idee om dit object terug te geven?? is het niet beter .toString()
    }


    /**
     * Sets the estimated duration of the task to the given duration.
     * @param estimatedDuration the estimated duration of the task
     * @post the estimated duration of the task is set to the given duration
     */
    private void setEstimatedDuration(Duration estimatedDuration){
        this.estimatedDuration = estimatedDuration;
    }



    /**
     * The acceptable deviation of the task.
     */
    private Double acceptableDeviation;


    /**
     * Returns the acceptable deviation of the task
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
    private final void setAcceptableDeviation(Double acceptableDeviation){
        this.acceptableDeviation = acceptableDeviation;
    }



    /**
     * The start time of the task.
     */
    private LocalDateTime startTime;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    /**
     * Returns the start time of the task.
     * @return the start time of the task
     */
    public LocalDateTime getStartTime(){
        return startTime; // TODO: is het wel slim om het object terug te geven?
    }


    /**
     * Sets the start time of the task to the given start time.
     * @param startTimeStr the start time of the task
     * @post the start time of the task is set to the given start time
     */
    private void setStartTime(String startTimeStr){
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, dateTimeFormatter);
        this.startTime = startTime;
    }



    /**
     * The end time of the task.
     */
    private LocalDateTime endTime;


    /**
     * Returns the end time of the task.
     * @return the end time of the task
     */
    public LocalDateTime getEndTime(){
        return endTime; // TODO: idem opmerking startTime
    }

    /**
     * Sets the end time of the task tot the given end time
     * @param endTimeStr the end time of the task
     * @pre the end time must be later than the start time
     * @post the end time of the task is set to the given end time
     */
    private void setEndTime(String endTimeStr) throws  IllegalArgumentException{ // TODO: moet er @throws gebruikt worden hier??
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, dateTimeFormatter);
        if(endTime.compareTo(startTime) > 0)
        {
            this.endTime = endTime;
        } else {
            throw new IllegalArgumentException("The end time can't be before the start time.");
        }
    }



    /**
     * The status of the task.
     */
    private Status status;


    /**
     * Returns the task status.
     * @return the task status
     */
    public Status getStatus(){
        return status;
    }


    /**
     * Sets the task status to the given status.
     * @param status the task status
     * @post the task status is set to the given status
     */
    private void setStatus(Status status){
        this.status = status;
    }


    public void updateStatus(String startTime, String endTime, String status){
        // TODO
        // Wachten op implementatie van klok
    }



    /**
     * The alternative task of the task.
     */
    private Task alternative;


    /**
     * Returns the alternative task of the task
     * @return the alternative task
     */
    public Task getAlternative(){
        return alternative;
    }


    /**
     * Sets the alternative task of the task to the given task.
     * @param alternative the alternative task of the task
     * @post the alternative task of the task is set to the given task
     * @throws IllegalArgumentException the alternative may not be this task or its alternative or one of its dependencies or one of these alternatives recursively
     */
    public void setAlternative(Task alternative) throws IllegalArgumentException {  // TODO: kan dit dan ook naar zichzelf verwijzen? + moeten we niet controleren dat dit ook geen depedency is
        if (containsLoop(this, alternative)){
            throw new IllegalArgumentException("The alternative may not be one of the dependecies or the alternative of this or of its dependendecies recursivley");
        }
        this.alternative = alternative;             // may be null
    }



    /**
     * The dependencies of the task.
     */
    private ArrayList<Task> dependencies;


    /**
     * Returns a list with all dependencies of the task.
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
     * @param dependency task that needs to be added to the task
     * @post the dependency is added to the task
     * @throws IllegalArgumentException the dependency may not be this task or its alternative or one of its dependencies or one of these alternatives recursively
     */
    public void addDependency(Task dependency) throws IllegalArgumentException {
        if (containsLoop(this, dependency)){
            throw new IllegalArgumentException("The alternative may not be one of the dependecies or the alternative of this or of its dependendecies recursivley");
        }
        dependencies.add(dependency);
    }


    /**
     * Removes dependency of the given task.
     * @param dependency task that needs to be removed as dependency of the task
     * @post the dependency is deleted from the task
     * @throws IllegalArgumentException the dependency task must be a dependency of the task
     */
    public void removeDependency(Task dependency){
        if (! dependencies.contains(dependency)){
            throw new IllegalArgumentException("The given task is not a dependency of the task.");
        }
        dependencies.remove(dependency);
    }



    /**
     * Returns the task details of the task
     * @return a HashMap containing as keys the detail name and as value the corresponding detail value
     */
    public HashMap<String, String> getTaskDetails(){
        HashMap<String, String> taskDetails = new HashMap<>();

        taskDetails.put("ID", ID.toString());
        taskDetails.put("Description", description);
        taskDetails.put("EstimatedDuration", estimatedDuration.toString());
        taskDetails.put("AcceptableDeviation", acceptableDeviation.toString());
        taskDetails.put("StartTime", startTime.format(dateTimeFormatter));
        taskDetails.put("EndTime", endTime.format(dateTimeFormatter));
        taskDetails.put("Status", status.toString());
        int[] dependenciesIDs = new int[dependencies.size()];
        for (int i = 0; i < dependencies.size(); i++){
            dependenciesIDs[i] = dependencies.get(0).getID();
        }
        taskDetails.put("Dependecies", dependenciesIDs.toString());
        // TODO: afspreken hoe we alternative gaan doen
        taskDetails.put("Alternative", alternative.getID().toString());

        return taskDetails;
    }


    // LOOP CHECKING CODE

    private boolean containsLoop(Task root, Task searchedTask){
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


    // Comparebale

    public int compareTo(Object o) {
        if (o instanceof Integer) {
            return compareTo(((Integer) o).intValue());
        }
	    else if (o instanceof Task) {
		    return compareTo(((Task) o).getID());
        }
        else {
            throw new IllegalArgumentException("Uncomparable!");
        }
    }

    private int compareTo(Integer id) {
        return this.getID().compareTo(id);
    }

    private int compareTo(Task task) {
        return this.getID().compareTo(task.getID());
    }

    public Element saveToXML() {
        throw new NotImplementedException(); // TODO
    }

    public static Task restoreFromXML(Element item) {
        throw new NotImplementedException(); // TODO
    }

    public static HashMap<String,String> getCreationForm() {
        throw new NotImplementedException(); // TODO
    }

    /*
    public int compareTo(Integer ID){
        IntegerComparator comparator = new IntegerComparator();
        return comparator.compareTo(ID);
    }

    public int compareTo(Task task){
        TaskComparator comparator = new TaskComparator();
        return comparator.compareTo(task);
    }


    private class IntegerComparator implements Comparable<Integer> {

        @Override
        public int compareTo(Integer ID) {
            if (getID() > ID) { return 1 ; }
            else if (getID() < ID) { return -1; }
            else return 0;
        }
    }


    private class TaskComparator implements Comparable<Task> {

        @Override
        public int compareTo(Task task) {
            if (getID() > task.getID()) { return 1 ; }
            else if (getID() < task.getID()) { return -1; }
            else return 0;
        }
    }
    */

}
