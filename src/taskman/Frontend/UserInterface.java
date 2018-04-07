package taskman.Frontend;

import taskman.Backend.Controller;
import taskman.Backend.ImportExportException;

import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * This class represents the user interface of the taskman program.
 *
 * @author Julien Benaouda, Jeroen Van Der Donckt
 */
public class UserInterface {

    /**
     * Represents the controller of the backend.
     */
    private Controller controller;

    /**
     * Creates a new UserInterface object.
     * @param c the controller that the UserInterface may use.
     * @post a new UserInterface object is created with the given controller. If the controller is null, the system exits.
     */
    public UserInterface(Controller c)
    {
    	if(c != null)
    	{
    		controller = c;
    	} else {
    		print("Error while opening the program, no controller.");
    		System.exit(0);
    	}
    }

    /**
     * Starts the user interface.
     */
    public void start()
    {
        println("Welcome to the taskman project manager!\n");
        startMenu();
        println("\nThank you for using the taskman project manager!");
    }

    /**
     * Shows a dialog where a user can login to the system.
     * @post the user type is set to the specified type.
     */
    private void startMenu()
    {
        String[] options = new String[]{
                "login",
                "create user",
                "import system from file",
                "export system to file",
                "exit"
        };
        while (true) {
            printTitle("start");
            switch (inputOption(options)) {
                case 0: login(); break;
                case 1: userCreationMenu(); break;
                case 2: importFromFile(); break;
                case 3: exportToFile(); break;
                default: return;
            }
        }
    }

    /**
     * Let the user log in to the system and go to the main menu.
     */
    private void login() {
        String[] questions = new String[]{"username", "password"};
        String[] answers = inputAnswers(questions);
        try {
            controller.login(answers[0], answers[1]);
        } catch (IllegalArgumentException e) {
            println(e.getMessage());
            return;
        }
        mainMenu();
        controller.logout();
    }

    /**
     * Let the user create a new account.
     */
    private void userCreationMenu() {
        String[] questions = new String[]{"username", "password", "type"};
        String[] answers = inputAnswers(questions);
        try {
            controller.createUser(answers[0], answers[1], answers[2]);
        } catch (IllegalArgumentException e) {
            println(e.getMessage());
        }
    }

    /**
     * Lets the user import a path to an xml file and converts that xml file into project data.
     * @post all system and project data is restored from the xml file.
     */
    private void importFromFile()
    {
        try {
            controller = Controller.importSystem(inputString("Path to xml file:"));
            println("Data imported successfully.");
        } catch(ImportExportException e) {
            println("Error while parsing the xml file: " +e.getMessage());
        }
    }

    /**
     * Exports all system and project data to a file with the entered path.
     * @post all system and project data is exported to an xml file.
     */
    private void exportToFile()
    {
        try {
            controller.exportSystem(inputString("Path to file:"));
            print("Data exported successfully.\n");
        } catch (ImportExportException e) {
            print("Error while exporting data: " +e.getMessage() +"\n");
        }
    }

    /**
     * Shows the main menu of the program and lets the user chose an option. If the user enters a non valid number, the menu is shown again.
     */
    private void mainMenu()
    {
        String[] options = new String[]{
                "show project details",
                "create project",
                "create task",
                "plan task",
                "update task status",
                "add alternative to task",
                "add dependency to task",
                "show system time",
                "advance system time",
                "logout"
        };
        while (true) {
            printTitle("main menu");
            switch (inputOption(options)) {
                case 0: showProjectDetails(); break;
                case 1: createProject(); break;
                case 2: createTask(); break;
                case 3: planTask(); break;
                case 4: updateTaskStatus(); break;
                case 5: addAlternativeToTask(); break;
                case 6: addDependencyToTask(); break;
                case 7: showSystemTime(); break;
                case 8: advanceSystemTime(); break;
                default: return;
            }
        }
    }

    /**
     * Select a project.
     * @return the name of the project.
     */
    public String selectProject() {
        String[] names = (String[]) controller.getProjectNames().toArray();
        String[] options = new String[names.length];
        for (int i = 0; i < names.length; i++){
            options[i] = names[i] + " (Status: " + controller.getProjectStatus(names[i]) + ")";
        }
        return names[inputOption(options)];
    }

    /**
     * Prints the list of projects.
     */
    private void showProjectDetails() {
        printTitle("projects");
        String project = selectProject();

        printTitle("project details of '" + project + "'");
        println(mapToString(controller.getProjectDetails(project)));

        String[] options = new String[controller.getNumberOfTasks(project)];
        for (int index = 0; index < options.length; index++) {
            options[index] = mapToString(controller.getTaskDetails(project, index));
        }
        Integer taskIndex = inputOption(options);


    }

    /**
     * Creates a new project.
     * @post a new project is created in the system with the entered parameters.
     */
    private void createProject() {
        String[] questions = new String[]{"name:", "description:", "dueTime:"};
        String[] answers = inputAnswersWithCancel(questions);
        if (answers == null) return; // Cancelled
        try {
            controller.createProject(answers[0], answers[1], answers[2]);
            println("Project created successfully.");
        } catch (DateTimeParseException | IllegalArgumentException e) {
            println("Error while creating project: " + e.getMessage());
        }
    }

    /**
     * Creates a task.
     * @post a new task is created.
     */
    private void createTask() {
        String[] names = (String[]) controller.getProjectNames().toArray();
        String project = names[inputOption(names)];
        if (inputCancel()) return; // Cancelled
        String[] questions = new String[]{"description:", "estimatedDuration:", "acceptableDeviation:"};
        String[] answers = inputAnswersWithCancel(questions);
        if (answers == null) return; // Cancelled
        try {
            Long estimatedDuration = Long.parseLong(answers[2]);
            Double acceptableDeviation = Double.parseDouble(answers[3]);
            controller.createTask(project, answers[0], estimatedDuration, acceptableDeviation);
            println("Task created successfully.");
        } catch (DateTimeParseException | IllegalArgumentException e) {
            println("Error while creating task: " + e.getMessage());
        }
    }

    /**
     * Show system time.
     */
    private void showSystemTime() {
        println("The system time is: " + controller.getSystemTime());
    }

    /**
     * Advances the system time to the new time.
     * @post the system time is set to the entered time.
     */
    private void advanceSystemTime()
    {
        try {
            controller.updateSystemTime(inputString("New system time:"));
            print("Time updated successfully.\n");
        } catch (DateTimeParseException | IllegalArgumentException e) {
            println(e.getMessage());
        }
    }

    /**
     * Adds an alternative to a task of a project
     *
     * @param name the name of the project
     */
    public void addAlternativeToTask(String name)
    {
        int taskID = inputInteger("Enter the index of the task for which to add an alternative:");
        int alternativeID = inputInteger("Enter the index of the alaternative task:");
        try {
            controller.addAlternativeToTask(name, taskID, alternativeID);
            print("Alternative added successfully.\n");
        } catch (IllegalStateException | IllegalArgumentException e) {
            print("Error when adding the alternative: " + e.getMessage());
        }
    }

    /**
     * adds a dependency to a task of the given project
     *
     * @param name the name of the project
     * @throws AccessDeniedException    the access denied exception
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void addDependencyToTask(String name) throws AccessDeniedException, IllegalArgumentException
    {
        print("enter the ID of the task for which to add a dependency: ");
        int taskID = inputInteger();
        print("Enter the id of the dependency: ");
        int dependencyID = inputInteger();
        controller.addDependencyToTask(name, taskID, dependencyID);
        print("Dependency added successfully.\n");
        showProjectMenu(name);
    }

    /**
     * Updates the status of a task
     * @param name the name of the project from which a task its status will be updated
     * @post the task its status and other properties are updated
     */

    /**
     * Shows the available tasks of the given project.
     *
     * @param name the name of the given project
     * @throws IllegalArgumentException if the given project does not contain any available tasks
     */
    public void showAvailableTasks(String name) throws IllegalArgumentException{
        ArrayList<HashMap<String, String>> availableTasks = controller.getAvailableTaskDetails(name);
        if (availableTasks.size() == 0){
            throw new IllegalArgumentException("The given project does not contain any available tasks.");
        }
        for (HashMap<String, String> availableTask : availableTasks){
            print(availableTask.get("id") + ": " + availableTask.get("description"));
        }
    }

    /**
     * Fills in the update task status form.
     * @return a HashMap containing as key the attribute names and as value their values
     */
    private HashMap<String, String> fillInTaskUpdateForm() {
        HashMap<String, String> form = controller.getUpdateTaskStatusForm();
        for (String key : form.keySet()){
            print(key + " (in case you want to cancel updating the task status just press enter): ");
            String value = inputString();
            if (value.isEmpty()){
                print("Task status update is cancelled");
                return null;
            }
            form.put(key, value);
        }
        return form;
    }

    /**
     * Updates the task status of an available task of the given project.
     *
     * @param name the name of the project
     * @post a task status, start time and end time of the selected available task of the given project is updaten
     */
    public void updateTaskStatus(String name) {
        print("Project: " + name + "\n");
        try{
            showAvailableTasks(name);
            print("task ID (in case you want to cancel updating the task status type 0): "); // task ID will never be 0 (lowest possible value = 1)
            int id = inputInteger();
            if (id != 0) {
                HashMap<String, String> form = fillInTaskUpdateForm();
                if (form != null){
                    controller.updateTaskStatus(name, id, form);
                    print("TaskStatus updated successfully.\n");
                    showProjectMenu(name);
                }
                else{
                    print("Task status update is cancelled.\n");
                    showProjectMenu(name);
                }
            }
            else{
                print("Task status update is cancelled.\n");
                showProjectMenu(name);
            }
        } catch (IllegalArgumentException e) {
            print("Error while updating task status. " + e.getMessage() +"\n");
            showProjectMenu(name);
        } catch (AccessDeniedException e) {
            print("Acces denied: " + e.getMessage() +"\n");
            showProjectMenu(name);
        }
    }

    /**
     * Converts a map with strings to a string.
     * @param map tha map with strings.
     * @return the resulting string.
     */
    private String mapToString(Map<String, String> map) {
        String str = "";
        for (Map.Entry<String, String> entry: map.entrySet()) {
            str += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        return str;
    }

    /**
     * Prints the given text to the console.
     * @param text the text to print
     */
    private void print(String text)
    {
        System.out.print(text);
    }

    /**
     * Prints the given text to the console and starts a new line.
     * @param text the text to print
     */
    private void println(String text)
    {
        print(text + "\n");
    }

    /**
     * Prints the given text to the console as a title.
     * @param text the text to print
     */
    private void printTitle(String text) {
        String decoration = "----------";
        println(decoration + ' ' + text.toUpperCase() + ' ' + decoration);
    }

    /**
     * Reads a string from the user input.
     * @return the string the user entered.
     */
    private String inputString() {
        try (Scanner sc = new Scanner(System.in)) {
            return sc.nextLine();
        }
    }

    /**
     * Reads a string from the user input.
     * @return the string the user entered.
     */
    private String inputString(String question) {
        print(question + ' ');
        return inputString();
    }

    /**
     * Lets the user input an integer value.
     * @return the integer the user inputted.
     */
    private Integer inputInteger(String question) {
        while (true) {
            try {
                return Integer.parseInt(inputString(question));
            } catch (NumberFormatException e) {
                println("This is not a valid number, please try again.");
            }
        }
    }

    /**
     * Lets the user input an integer value between min and max.
     * @param min the minimum valid value.
     * @param max the maximum valid value.
     * @return the integer the user inputted.
     */
    private Integer inputIntegerBetween(String question, Integer min, Integer max) {
        Integer input;
        while (true) {
            input = inputInteger("Choose option:");
            if (min <= input && input <= max) {
                return input;
            } else {
                println("Only inputs between " + min + " and " + max + " are allowed!");
            }
        }
    }

    /**
     * Only lets the user enter a valid answer.
     * @param question the question to answer.
     * @param answers the valid answers.
     * @return a valid answer.
     */
    private String inputValidAnswer(String question, Collection<String> answers) {
        String answer;
        while (true) {
            answer = inputString(question);
            if (answers.contains(answer)) {
                return answer;
            } else {
                println("Invalid answer! Allowed: " + Arrays.toString(answers.toArray()));
            }
        }
    }

    /**
     * Asks a series of questions with the option to cancel.
     * @param questions the questions to ask.
     * @return a series of answers.
     */
    private String[] inputAnswers(String[] questions) {
        String[] answers = new String[questions.length];
        for (int i = 0; i < questions.length; i++) {
            answers[i] = inputString(questions[i]);
        }
        return answers;
    }

    /**
     * Asks a series of questions with the option to cancel.
     * @param questions the questions to ask.
     * @return a series of answers or null if canceled.
     */
    private String[] inputAnswersWithCancel(String[] questions) {
        String[] answers = new String[questions.length];
        for (int i = 0; i < questions.length; i++) {
            answers[i] = inputString(questions[i]);
            if (inputCancel()) {
                return null;
            }
        }
        return answers;
    }

    /**
     * Let the user decide if he want to cancel.
     * @return if the user wants to cancel.
     */
    private Boolean inputCancel() {
        ArrayList<String> yn = new ArrayList<>();
        yn.add("Y");
        yn.add("N");
        return inputValidAnswer("Cancel? [Y/N]", yn).equals("Y");
    }

    /**
     * Lets the user choose between options.
     * @return the index of the selected option.
     */
    private Integer inputOption(String[] items)
    {
        println("Options:");
        for(int i = 0; i < items.length; i++) {
            println((i + 1) + " - " + items[i]);
        }
        return inputIntegerBetween("Choose option:", 1, items.length) - 1;
    }

}