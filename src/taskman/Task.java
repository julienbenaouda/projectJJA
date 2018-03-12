package taskman;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
     * @post a new task is created with the given attributes an available status
     */
    public Task(String description, String estimatedDuration, String acceptableDeviation) {
        setID();
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStatus(Status.AVAILABLE.toString());
        dependencies = new ArrayList<>();
    }

    /**
     * Creates a new task with parameters from the given hashmap.
     * @param form the HashMap from which to extract the parameters.
     * @throws IllegalArgumentException when one of the parameters is abscent or not valid.
     * @post a new task is created with the given parameters
     */
    public Task(HashMap<String, String> form) throws IllegalArgumentException {
        this(form.get("description"), form.get("estimatedDuration"), form.get("acceptableDeviation"));
    }

    /**
     * Creates a new task with thte given values.
     * @param lastTaskID the latest task ID
     * @param ID the task ID
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable deviation of the task
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @post a new task is created with the given attributes
     */
    private Task(String lastTaskID, String ID, String description, String estimatedDuration, String acceptableDeviation, String startTime, String endTime, String status) {
        setLastTaskID(Integer.parseInt(lastTaskID));
        setID(Integer.parseInt(ID));
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        if (startTime != null){
            setStartTime(startTime);
        }
        if(endTime != null){
            setEndTime(endTime);
        }
        setStatus(status);
        dependencies = new ArrayList<>();
    }


    /**
     * The latest task ID.
     */
    private static Integer lastTaskID = 0;


    /**
     * Return the ID of the latest task. (This should be the maximum of all ID's)
     * @return the ID of the latest task
     */
    public static Integer getLastTaskID() { return lastTaskID; }


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
     * Sets the ID of the task to the given ID.
     * @param ID the task ID
     * @post the ID of the tqsk is set to the given ID
     */
    private void setID(Integer ID){
        this.ID = ID;
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
    private Duration estimatedDuration;


    /**
     * Returns the estimated duration of the task in minutes.
     * @return the estimated duration of the task in minutes
     */
    public String getEstimatedDuration(){
        return Long.toString(estimatedDuration.toMinutes());
    }


    /**
     * Sets the estimated duration of the task to the given duration in minutes.
     * @param estimatedDuration the estimated duration of the task in minutes
     * @post the estimated duration of the task is set to the given duration
     */
    private void setEstimatedDuration(String estimatedDuration){
        this.estimatedDuration = Duration.ofMinutes(Long.parseLong(estimatedDuration));
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
    private void setAcceptableDeviation(String acceptableDeviation){
        this.acceptableDeviation = Double.parseDouble(acceptableDeviation);
    }


    /**
     * The start time of the task.
     */
    private LocalDateTime startTime;

    /**
     * The DateTimeFormatter used to convert LocalDateTimes to Strings and Strings to LocalDateTimes.
     */
    private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);


    /**
     * Returns the start time of the task.
     * @return the start time of the task or null if there is no start time
     */
    public String getStartTime(){
        if (startTime == null){
            return null;
        }
        return startTime.format(dateFormatter);
    }


    /**
     * Sets the start time of the task to the given start time.
     * @param startTimeStr the start time of the task
     * @post the start time of the task is set to the given start time
     */
    private void setStartTime(String startTimeStr){
        this.startTime = LocalDateTime.parse(startTimeStr, dateFormatter);
    }



    /**
     * The end time of the task.
     */
    private LocalDateTime endTime;


    /**
     * Returns the end time of the task.
     * @return the end time of the task or null if there is no end time
     */
    public String getEndTime(){
        if (endTime == null){
            return null;
        }
        return endTime.format(dateFormatter);
    }

    /**
     * Sets the end time of the task tot the given end time
     * @param endTimeStr the end time of the task
     * @pre the end time must be later than the start time
     * @post the end time of the task is set to the given end time
     */
    private void setEndTime(String endTimeStr){
        this.endTime = LocalDateTime.parse(endTimeStr, dateFormatter);
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
     * @throws IllegalArgumentException when the given status does not exist
     * @post the task status is set to the given status
     */
    private void setStatus(String status) throws IllegalArgumentException{
        this.status = Status.fromString(status);
    }


    /**
     * Updates the status of the task.
     * @param form the HashMap from which to extract the necessary values
     * @throws IllegalArgumentException when the status is not FINISHED and not FAILED or when the end time is before the start time
     * @throws IllegalArgumentException when the startTime if before the endTime of its dependencies
     * @post the start time, end time and status of the task will be updated
     */
    public void updateStatus(HashMap<String, String> form) throws IllegalArgumentException {
        Status status = Status.fromString(form.get("status"));
        if (status != Status.FINISHED && status != Status.FAILED){
            throw new IllegalArgumentException("The status may only be finished or failed.");
        }
        LocalDateTime endTime = LocalDateTime.parse(form.get("endTime"), dateFormatter);
        LocalDateTime startTime = LocalDateTime.parse(form.get("startTime"), dateFormatter);
        if (startTime.isAfter(endTime)){
            throw new IllegalArgumentException("The end time can't be before the start time.");
        }
        for (Task dependency: this.getDependencies()) {
            if (startTime.isBefore(dependency.endTime)) {
                throw new IllegalArgumentException("The task must start after its dependencies!");
            }
        }
        setStartTime(form.get("startTime"));
        setEndTime(form.get("endTime"));
        setStatus(form.get("status"));
    }

    /**
     * Return the delay between the end time and the estimated end time in minutes.
     * @return a String with the time between the end time and the estimated end time in minutes.
     * @throws IllegalStateException if the task is not yet finished.
     */
    public String getDelay() throws IllegalStateException {
        if (this.getStatus() != Status.FINISHED) {
            throw new IllegalStateException("Cannot calculate delay of task if not fininshed!");
        }
        else {
            return Long.toString(Duration.between(this.startTime, this.endTime).minus(this.estimatedDuration).toMinutes());
        }
    }


    /**
     * The alternative task of the task.
     */
    private Task alternative;


    /**
     * Returns the alternative task of the task.
     * @return the alternative task
     */
    public Task getAlternative(){
        return alternative;
    }


    /**
     * Sets the alternative task of the task to the given task.
     * @param alternative the alternative task of the task
     * @post the alternative task of the task is set to the given task
     * @throws IllegalStateException the task must be failed to set the alternative task
     * @throws IllegalArgumentException the alternative may not be this task or its alternative or one of its dependencies or one of these alternatives recursively
     */
    public void setAlternative(Task alternative) throws IllegalStateException, IllegalArgumentException {
        // TODO: deze illegalstatexception catchen in ui
        if (getStatus() != Status.FAILED){
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
     * @post the dependency is added to the task, the status of the task is updated accordingly
     * @throws IllegalArgumentException when the dependency is this task or its alternative or one of its dependencies or one of these alternatives recursively
     * @throws IllegalStateException if the task is already finished or failed
     */
    public void addDependency(Task dependency) throws IllegalArgumentException, IllegalStateException {
        if (containsLoop(this, dependency)){
            throw new IllegalArgumentException("The alternative may not be one of the dependencies or the alternative of this or of its dependendecies recursivley");
        }
        if (getStatus() == Status.FAILED || getStatus() == Status.FINISHED){
            throw new IllegalStateException("No dependencies may be added to failed or finished tasks.");
        }
        if (dependency.getStatus() == Status.AVAILABLE || dependency.getStatus() == Status.UNAVAILABLE){
            setStatus(Status.UNAVAILABLE.toString());
        }
        else if (dependency.getStatus() == Status.FAILED){
            Task alternative = dependency;
            while (alternative.getAlternative() !=  null && alternative.getStatus() == Status.FAILED){
                alternative = alternative.getAlternative();
            }
            if (alternative.getStatus() != Status.FINISHED){
                setStatus(Status.UNAVAILABLE.toString());
            }
        }
        dependencies.add(dependency);
    }

    /**
     * Restores the dependencies of the task without checks.
     * @param dependencies an ArrayList of Task on which this task is dependant.
     */
    public void restoreDependencies(ArrayList<Task> dependencies) {
        this.setDependencies(dependencies);
    }

    /**
     * Removes dependency of the given task.
     * @param dependency task that needs to be removed as dependency of the task
     * @post the dependency is deleted from the task and the task status is updated accordingly
     * @throws IllegalArgumentException the dependency task must be a dependency of the task
     */
    public void removeDependency(Task dependency){
        if (! dependencies.contains(dependency)){
            throw new IllegalArgumentException("The given task is not a dependency of the task.");
        }
        dependencies.remove(dependency);
        if (dependency.getStatus() == Status.AVAILABLE || dependency.getStatus() == Status.UNAVAILABLE){
            boolean becomesAvailable = true;
            for (Task d : getDependencies()){
                if (d.getStatus() == Status.AVAILABLE || d.getStatus() == Status.UNAVAILABLE){
                    becomesAvailable = false;
                }
                else if (d.getStatus() == Status.FAILED){
                    Task alternative = d;
                    while (alternative.getAlternative() != null && alternative.getStatus() == Status.FAILED){
                        alternative = alternative.getAlternative();
                    }
                    if (alternative.getStatus() != Status.FINISHED){
                        becomesAvailable = false;
                    }
                }
            }
            if (becomesAvailable){
                setStatus(Status.AVAILABLE.toString());
            }
        }
    }



    /**
     * Returns the task details of the task.
     * @return a HashMap containing as keys the detail name and as value the corresponding detail value
     */
    public HashMap<String, String> getTaskDetails() {
        HashMap<String, String> taskDetails = new HashMap<>();

        taskDetails.put("id", getID().toString());
        taskDetails.put("description", getDescription());
        taskDetails.put("estimatedDuration", getEstimatedDuration());
        taskDetails.put("acceptableDeviation", getAcceptableDeviation().toString());
        if (startTime == null) {
            taskDetails.put("startTime", "not yet set");
        } else {
            taskDetails.put("startTime", startTime.format(dateFormatter));
        }
        if (endTime == null) {
            taskDetails.put("endTime", "not yet set");
        } else{
            taskDetails.put("endTime", endTime.format(dateFormatter));
        }
        taskDetails.put("status", getStatus().toString());
        /*int[] dependenciesIDs = new int[dependencies.size()];
        for (int i = 0; i < dependencies.size(); i++){
            dependenciesIDs[i] = dependencies.get(i).getID();
        }*/
        StringBuilder dependenciesIDs = new StringBuilder();
        for(Task t: getDependencies()) {
        	dependenciesIDs.append(t.getID().toString() +", ");
        }
        if(dependenciesIDs.lastIndexOf(",") != -1) {
        	dependenciesIDs.deleteCharAt(dependenciesIDs.lastIndexOf(","));
        }
        taskDetails.put("dependencies", dependenciesIDs.toString());
        if(alternative != null) {
        	taskDetails.put("alternative", alternative.getID().toString());
        } else {
        	taskDetails.put("alternative", "no alternative task");
        }

        return taskDetails;
    }


    // LOOP CHECKING CODE

    /**
     * Checks if the searched task is equal to the root or one of its dependencies or alternatives (recursively).
     * @param root the root task whose dependencies and alternatives will be further searched
     * @param searchedTask the task we want to search for
     * @return true if the task is found, false otherwise
     */
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

    /**
     * Compares this task its ID with that from another task or another ID.
     * @param o the other task or the ID of the other task
     * @return the comparison of both ID's
     */
    public int compareTo(Object o) {
        if (o instanceof Integer){
            return compareTo((Integer) o);
        }
	    else if (o instanceof Task){
		    return compareTo(((Task) o).getID());
        }
        else{
            throw new IllegalArgumentException("Uncomparable!");
        }
    }

    /**
     * Compares an integer with the task its ID.
     * @param id the ID to compare with
     * @return the comparison of both ID's
     */
    private int compareTo(Integer id) {
        return this.getID().compareTo(id);
    }

    /**
     * Compares the ID's of the task with the given task.
     * @param task the task to compare ID's with
     * @return the comparison of both tasks their ID's
     */
    private int compareTo(Task task) {
        return this.getID().compareTo(task.getID());
    }

    // XML

    /**
     * Add a task to an XmlObject.
     * @param taskObject an XmlObject.
     * @post the task will be added to the XmlObject.
     * @throws XmlException if the clock cannot be added to the XmlObject.
     */
    public void addToXml(XmlObject taskObject) throws XmlException {
        taskObject.addAttribute("id", getID().toString());
        taskObject.addText("description", getDescription());
        taskObject.addText("estimatedDuration", getEstimatedDuration());
        taskObject.addText("acceptableDeviation", getAcceptableDeviation().toString());
        taskObject.addText("startTime", getStartTime());
        taskObject.addText("endTime", getEndTime());
        taskObject.addText("status", getStatus().name());
        taskObject.addText("lastTaskID", getLastTaskID().toString());
        if (getAlternative() == null){
            taskObject.addText("alternative", null);
        }
        else {
            taskObject.addText("alternative", getAlternative().getID().toString());
        }
        for (Task dependency: getDependencies()) {
            taskObject.addText("dependency", dependency.getID().toString());
        }
    }

    /**
     * Restore a task from an XmlObject.
     * @param taskObject the XmlObject.
     * @return the restored task.
     * @throws XmlException if the task can't be created.
     */
    public static Task getFromXml(XmlObject taskObject) throws XmlException {
        String id = taskObject.getAttribute("id");
        String description = taskObject.getTexts("description").get(0);
        String estimatedDuration = taskObject.getTexts("estimatedDuration").get(0);
        String acceptableDeviation = taskObject.getTexts("acceptableDeviation").get(0);
        String startTime = taskObject.getTexts("startTime").get(0);
        String endTime = taskObject.getTexts("endTime").get(0);
        String lastTaskID = taskObject.getTexts("lastTaskID").get(0);
        String status = taskObject.getTexts("status").get(0);
        return new Task(lastTaskID, id, description, estimatedDuration, acceptableDeviation, startTime, endTime, status);
    }


    // Form

    /**
     * This method generates a form containing all parameters needed to create a new task. All values are empty and can be filled in, and then passed back to the task.
     * @return a HashMap containing all elements that need to be filled in to create a new task
     */
    public static HashMap<String, String> getCreationForm() {
        HashMap<String, String> form = new LinkedHashMap<>();
        form.put("description", "");
        form.put("estimatedDuration", "");
        form.put("acceptableDeviation", "");
        return form;
    }


    /**
     * This method generates a form containing all parameters needed to update the status of a task. All values are empty and can be filled in, and then passed back to the task.
     * @return a HashMap containing all elements that need to be filled in to update the status of a task
     */
    public static HashMap<String, String> getUpdateStatusForm() {
        HashMap<String, String> form = new LinkedHashMap<>();
        form.put("startTime", "");
        form.put("endTime", "");
        form.put("status", "");
        return form;
    }


}
