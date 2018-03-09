package taskman;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.naming.OperationNotSupportedException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
    public Task(String description, String estimatedDuration, String acceptableDeviation, String startTime, String endTime)
    {
        setID();
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStartTime(startTime);
        setEndTime(endTime);
        dependencies = new ArrayList<>();
    }

    /**
     * Creates a new task with parameters from the given hashmap.
     * @param form the HashMap from which to extract the parameters.
     * @throws IllegalArgumentException when one of the parameters is abscent or not valid.
     * @post a new task is created with the given parameters
     */
    public Task(HashMap<String, String> form) throws IllegalArgumentException{
        this(form.get("description"), form.get("estimatedDuration"), form.get("acceptableDeviation"), form.get("startTime"), form.get("endTime"));
    }

    /**
     * Creates a new task with thte given values
     * @param lastTaskID the latest task ID
     * @param ID the task ID
     * @param description the task description
     * @param estimatedDuration the estimated duration of the task in minutes
     * @param acceptableDeviation the acceptable deviation of the task
     * @param startTime the start time of the task
     * @param endTime the end time of the task
     * @post a new task is created with the given attributes
     */
    private Task(String lastTaskID, String ID, String description, String estimatedDuration, String acceptableDeviation, String startTime, String endTime, String status){
        setLastTaskID(Integer.parseInt(lastTaskID));
        setID(Integer.parseInt(ID));
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStartTime(startTime);
        setEndTime(endTime);
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
        return Integer.valueOf(ID.intValue());
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
        return Double.valueOf(acceptableDeviation.doubleValue());
    }

    /**
     * Sets the acceptable deviation of the task to the given deviation.
     * @param acceptableDeviation the acceptable deviation of the task
     * @post the acceptable deviation of the task is set to the given deviation
     */
    private final void setAcceptableDeviation(String acceptableDeviation){
        this.acceptableDeviation = Double.parseDouble(acceptableDeviation);
    }



    /**
     * The start time of the task.
     */
    private LocalDateTime startTime;


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
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, dateFormatter);
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
     * @throws IllegalArgumentException when the parameter is not valid or when the end time is before the starttime
     */
    private void setEndTime(String endTimeStr) throws  IllegalArgumentException{ // TODO: moet er @throws gebruikt worden hier??
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, dateFormatter);
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
     * @throws IllegalArgumentException when the given status does not exist
     * @post the task status is set to the given status
     */
    private void setStatus(String status) throws IllegalArgumentException{
        this.status = Status.fromString(status);
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
    public void setAlternative(Task alternative) throws IllegalArgumentException {
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
     * The DateTimeFormatter used to convert LocalDateTimes to Strings and Strings to LocalDateTimes.
     */
    private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


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
        taskDetails.put("StartTime", startTime.format(dateFormatter));
        taskDetails.put("EndTime", endTime.format(dateFormatter));
        taskDetails.put("Status", status.toString());
        int[] dependenciesIDs = new int[dependencies.size()];
        for (int i = 0; i < dependencies.size(); i++){
            dependenciesIDs[i] = dependencies.get(0).getID();
        }
        taskDetails.put("Dependecies", dependenciesIDs.toString());
        taskDetails.put("Alternative", alternative.getID().toString());

        return taskDetails;
    }


    // LOOP CHECKING CODE

    /**
     * Checks if the searched task is equal to the root or one of its dependencies or alternatives (recursively)
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
     * Compares this task its ID with that from another task or another ID
     * @param o the other task or the ID of the other task
     * @return the comparison of both ID's
     */
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

    /**
     * Compares an integer with the task its ID
     * @param id the ID to compare with
     * @return the comparison of both ID's
     */
    private int compareTo(Integer id) {
        return this.getID().compareTo(id);
    }

    /**
     * Compares the ID's of the task with the given task
     * @param task the task to compare ID's with
     * @return the comparison of both tasks their ID's
     */
    private int compareTo(Task task) {
        return this.getID().compareTo(task.getID());
    }


    // XML


    /**
     * This method returns an XML string containing all task details.
     * @returns an XML element containing all task details.
     * @throws OperationNotSupportedException when the xml string can't be created.
     */
    public Element saveToXML() throws OperationNotSupportedException
    {
        try {
            // create the document
            DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = df.newDocumentBuilder();
            Document doc = db.newDocument();
            // add all task attributes
            Element t = doc.createElement("task");

            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode(getID().toString()));
            t.appendChild(id);
            Element description = doc.createElement("description");
            description.appendChild(doc.createTextNode(getDescription()));
            t.appendChild(description);
            Element estimatedDuration = doc.createElement("estimatedDuration");
            estimatedDuration.appendChild(doc.createTextNode(getEstimatedDuration().toString()));
            t.appendChild(estimatedDuration);
            Element acceptableDeviation = doc.createElement("acceptableDeviation");
            description.appendChild(doc.createTextNode(getAcceptableDeviation().toString()));
            t.appendChild(acceptableDeviation);
            Element startTime = doc.createElement("startTime");
            startTime.appendChild(doc.createTextNode(getStartTime().format(dateFormatter)));
            t.appendChild(startTime);
            Element endTime = doc.createElement("endTime");
            endTime.appendChild(doc.createTextNode(getEndTime().format(dateFormatter)));
            t.appendChild(endTime);
            Element status = doc.createElement("status");
            status.appendChild(doc.createTextNode(getStatus().name())); // TODO: of moet het .toString() zijn
            t.appendChild(status);

            Element lastTaskID = doc.createElement("lastTaskID");
            lastTaskID.appendChild(doc.createTextNode(getLastTaskID().toString()));
            t.appendChild(lastTaskID);

            Element alternative = doc.createElement("alternative");
            alternative.appendChild(getAlternative().saveToXML());
            t.appendChild(alternative);

            Element dependencies = doc.createElement("dependencies");
            // add all tasks of the project
            for(Task d: getDependencies())
            {
                dependencies.appendChild(d.saveToXML());
            }
            t.appendChild(dependencies);
            return t;
        } catch (Exception e) {
            throw new XMLParserException(e.getMessage());
        }
    }

    /**
     * This method converts a xml element containing task data to a task.
     * @param task the xml element containing the task data
     * @return a new task with the data from the xml document
     * @throws OperationNotSupportedException when the provided element can't be parsed.
     */
    public static Task restoreFromXML(Element task) throws OperationNotSupportedException
    {
        try {
            if(!(task.getNodeName().equals("task")))
            {
                throw new XMLParserException("the xml file you provided is not in the correct format. Please correct the errors or try another file");
            }
            String id = task.getElementsByTagName("id").item(0).getTextContent();
            String description = task.getElementsByTagName("description").item(0).getTextContent();
            String estimatedDuration = task.getElementsByTagName("estmatedDuration").item(0).getTextContent();
            String acceptableDeviation = task.getElementsByTagName("acceptableDeviation").item(0).getTextContent();
            String startTime = task.getElementsByTagName("startTime").item(0).getTextContent();
            String endTime = task.getElementsByTagName("endTime").item(0).getTextContent();
            String lastTaskID = task.getElementsByTagName("lastTaskID").item(0).getTextContent();
            String status = task.getElementsByTagName("status").item(0).getTextContent();
            Task t = new Task(lastTaskID, lastTaskID, description, estimatedDuration, acceptableDeviation, startTime, endTime, status);

            Node alternative = task.getElementsByTagName("alternative").item(0);
            if(alternative.getNodeType() != Node.ELEMENT_NODE)
            {
                throw new XMLParserException("the xml file has not the correct format. Pleas correct the errors or try another file");
            }
            t.setAlternative(Task.restoreFromXML((Element) alternative));

            Node dependencies = task.getElementsByTagName("dependencies").item(0);
            if(dependencies.getNodeType() != Node.ELEMENT_NODE)
            {
                throw new XMLParserException("the xml file has not the correct format. Pleas correct the errors or try another file");
            }
            Element dependenciesElem = (Element) dependencies;
            NodeList dl = dependenciesElem.getElementsByTagName("task");
            for(int i = 0; i < dl.getLength(); i++)
            {
                t.addDependency(Task.restoreFromXML((Element) dl.item(i)));
            }
            return t;
        } catch (Exception e)
        {
            throw new XMLParserException(e.getMessage());
        }
    }



    /**
     * This method generates a form containing all parameters needed to create a new task. All values are empty and can be filled in, and then passed back to the task.
     * @return A HashMap containing all elements that need to be filled in to create a new task
     */
    public static HashMap<String,String> getCreationForm() {
        HashMap<String, String> form = new HashMap<>();
        form.put("description", "");
        form.put("estimatedDuration", "");
        form.put("acceptableDeviation", "");
        form.put("startTime", "");
        form.put("endTime", "");
        return form;
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
