/**
 * 
 */
package taskman;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class represents the user interface of the taskman program.
 * @author Julien Benaouda, Jeroen Van Der Donckt
 *
 */
public class UI {
	private Controller controller;

	/**
	 * Creates a new UI object.
	 */
	private UI()
	{
		controller = new Controller();
	}
	
	/**
	 * Prints the welcome dialog
	 */
	public void welcomeDialog()
	{
		StringBuilder message = new StringBuilder("Welcome to the taskman project manager.\n");
		print(message.toString());
		showUserChoiceDialog();
	}
	
	/**
	 * Shows a dialog where a user type can be chosen.
	 * @post the user type is set to the specified type. 
	 */
	public void showUserChoiceDialog()
	{
		StringBuilder message = new StringBuilder();
		message.append("In which user mode would you like to run the program\n");
		message.append("options:\n");
		message.append("1 - regular user\n2 - developer\n");
		message.append("Choose option:");
		print(message.toString());
		int input = inputInt();
		try {
			switch (input) {
			case 1: controller.setUserType("REGULARUSER");
					break;
			case 2: controller.setUserType("DEVELOPER");
					break;
			default: print("Invalid argument, please try again.\n");
					 showUserChoiceDialog();
					 break;
			}
		} catch(IllegalArgumentException e) {
			print("Invalid option, please try again");
			showUserChoiceDialog();
		}
		showMainMenu();
	}
	
	/**
	 * Shows the main menu of the program and lets the user chose an option. If the user enters a non valid number, the menu is shown again.
	 */
	public void showMainMenu()
	{
		StringBuilder message = new StringBuilder("options:\n");
		message.append("1 - list all projects\n");
		message.append("2 - open project\n");
		message.append("3 - add new project\n");
		message.append("4 - advance system time\n");
		message.append("5 - import data\n");
		message.append("6 - export data\n");
		message.append("7 - change user\n");
		message.append("Choose option:");
		print(message.toString());
		int input = inputInt();
		switch (input) {
		case 1: listProjects();
				break;
		case 2: showProjectMenu();
				break;
		case 3: createProject();
				break;
		case 4: advanceSystemTime();
				break;
		case 5: importFile();
				break;
		case 6: exportFile();
				break;
		case 7: showUserChoiceDialog();
				break;
		default: print("Invalid number, please try again");
		 		 showMainMenu();
				 break;
		}
	}
	
	/**
	 * Prints the list of projects. Only the project names are printed.
	 */
	public void listProjects() {
		StringBuilder message = new StringBuilder();
		for (String projectName : controller.getProjectNames()){
			message.append(projectName + "\n");
		}
		print(message.toString());
		showMainMenu();
	}
	
	/**
	 * Lets the user import a path to an xml file and converts that xml file into project data.
	 * @post all system and project data is restored from the xml file.
	 */
	public void importFile()
	{
		print("Path to xml file: ");
		String path = inputString();
		try {
			controller.importXML(path);
		}
		catch(XMLParserException e)
		{
			print("Error while parsing the xml file: " +e.getMessage());
		}
	}
	
	/**
	 * Exports all system and project data to a file with the entered path
	 * @post all system and project data is exported to an xml file.
	 */
	public void exportFile()
	{
		print("Path to file: ");
		String path = inputString();
		try {
			controller.exportToXML(path);
		} catch (XMLParserException e)
		{
			print("Error while exporting data: " +e.getMessage());
		}
	}
	
	/**
	 * lets the user enter a project.
	 */
	public void showProjectMenu()
	{
		print("Project name: ");
		String name = inputString();
		try {
			controller.projectExists(name);
			showProjectMenu(name);
		} catch (IllegalArgumentException e)
		{
			print("A project with the given name does not exist, please try again.");
			showMainMenu();
		}
	}
	
	/**
	 * Shows the project menu for a given project and let's the user choose an option.
	 * @param name the name of the project to show options for.
	 */
	public void showProjectMenu(String name)
	{
		StringBuilder sb = new StringBuilder("options:\n");
		sb.append("1 - view project details\n");
		sb.append("2 - view task details\n");
		sb.append("3 - add task\n");
		sb.append("4 - update task status\n");
		sb.append("5 - back to main menu\n");
		sb.append("Choose option: ");
		print(sb.toString());
		int option = inputInt();
		switch (option)
		{
		case 1: showProjectDetails(name);
				break;
		case 2: showTaskDetails(name);
				break;
		case 3: try {
					createTask(name);
				} catch (IllegalArgumentException e) {
					print("Invalid arguments while creating task: " + e.getMessage());
					showProjectMenu(name);
				}
				break;
		case 4: try {
					updateTaskStatus(name);
				} catch (IllegalArgumentException e) {
					print("Invalid argument while updating task status: " + e.getMessage());
					showProjectMenu(name);
				}
				break;
		case 5: showMainMenu();
				break;
		default: print("Invalid option, please try again.");
				 showProjectMenu(name);
				 break;
		}
	}
	
	/**
	 * Shows the details of the given project.
	 * @param name the name of the project to show details for
	 */
	public void showProjectDetails(String name) {
		StringBuilder sb = new StringBuilder();
		try {
			HashMap<String, String> details = controller.getProjectDetails(name);
			for (String key: details.keySet()) {
				sb.append(key + ": " + details.get(key) + "\n");
			}
			print(sb.toString());
		} catch (IllegalArgumentException e) {
			print("A project with the specified name does not exist. Please try again");
			showMainMenu();
		}
	}

	/**
	 * Shows the task details of the given project.
	 * @param name the name of the project to show the tasks details for
	 */
	public void showTaskDetails(String name) {
		StringBuilder sb = new StringBuilder();
		try {
			for (int id : controller.getTasksOfProject(name)){
				HashMap<String, String> details = controller.getTaskDetails(name, id);
				for (String key : details.keySet()){
					sb.append(key + ": " + details.get(key) + "\n");
				}
			}
			print(sb.toString());
		} catch (IllegalArgumentException e) {
			print("A project with the specified name does not exist. Please try again");
			showMainMenu();
		}
	}

	/**
	 * Creates a task for the given project.
	 * @param name the name of the project to create the task for
	 * @post a new task is created and added to the given project
	 */
	public void createTask(String name) {
		HashMap<String, String> form =  controller.getTaskCreationForm();
		for (String key: form.keySet()){
			print(key + ": ");
			form.put(key, inputString());
		}
		try{
			controller.addTask(name, form);
		} catch (IllegalArgumentException e){
			print("Error while creating task: " + e.getMessage());
			showMainMenu();
		}
	}

	/**
	 * Updates the status of a task
	 * @param name the name of the project from which a task its status will be updated
	 * @post the task its status and other properties are updated
	 */

	/**
	 * Shows the available tasks of the given project.
	 * @param name the name of the given project
	 * @throws IllegalArgumentException if the given project does not contain any available tasks
	 */
	private void showAvailableTasks(String name) throws IllegalArgumentException{
		ArrayList<HashMap<String, String>> availableTasks = controller.getAvailableTaskDetails(name);
		if (availableTasks.size() == 0){
			throw new IllegalArgumentException("The given task does not contain any available tasks.");
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
				return null; // TODO: ook lelijke code :s // MSS exception gooien en die dan catchen idk
			}
			form.put(key, value);
		}
		return form;
	}

	/**
	 * Updates the task status of an available task of the given project.
	 * @param name the name of the project
	 * @post a task status, start time and end time of the selected available task of the given project is updaten
	 */
	public void updateTaskStatus(String name)
	{
		print("Project: " + name + "\n");
		try{
			showAvailableTasks(name);
			print("task ID (in case you want to cancel updating the task status type 0): "); // normaal kan task ID nooit 0 zijn (begint bij 1) TODO is dit ok voor jullie
			int id = inputInt();
			if (id != 0) {
				print("Task status update is cancelled.");
				return; // TODO: LELIJKE CODE xdxp
			}
			HashMap<String, String> form = fillInTaskUpdateForm();
			if (form == null){
				print("Task status update is cancelled.");
				return; // TODO: nog steeds lelijke code :s
			}
			controller.updateTaskStatus(name, id, form);
		} catch (IllegalArgumentException e) {
			print("Error while updating task status. " + e.getMessage());
			showMainMenu();
		} catch (AccessDeniedException e) {
			print("Acces denied: " + e.getMessage());
			showMainMenu();
		}
	}
	
	/**
	 * creates a new project
	 * @post a new project is created in the system with the entered parameters.
	 */
	public void createProject() {
		HashMap<String, String> form = controller.getProjectCreationForm();
		for(String key: form.keySet()) {
			print(key + ": ");
			form.put(key, inputString());
		}
		try {
			controller.addProject(form);
		} catch (IllegalArgumentException e)
		{
			print("Error while creating project: " +e.getMessage());
			showMainMenu();
		}
	}
	
	/**
	 * Advances the system time to the new time.
	 * @post the system time is set to the entered time.
	 */
	public void advanceSystemTime()
	{
		print("New system time: ");
		String time = inputString();
		try {
			controller.updateSystemTime(time);
		} catch (IllegalArgumentException e)
		{
			print("The given time is not valid. Please try again");
			showMainMenu();
		}
	}
	
	/**
	 * Prints the given text to the console.
	 * @param text the text to print
	 */
	public void print(String text)
	{
		System.out.print(text);
	}
	
	/**
	 * Lets the user input an integer value.
	 * @return the integer the user inputted.
	 * @return
	 */
	private int inputInt()
	{
		String input = inputString();
		int inputInt = 0;
		try {
			Integer.parseInt(input);
		} catch (Exception e)
		{
			print("This is not a valid number, please try again.");
			inputInt();
		}
		return inputInt;
	}
	
	/**
	 * Reads a string from the user input.
	 * @return the string the user entered.
	 */
	private String inputString()
	{
		Scanner sc = new Scanner(System.in);
		String input = "";
		try {
			input = sc.nextLine();
		} catch (NoSuchElementException e)
		{
			input = inputString();
		}
		return input;
	}
}
