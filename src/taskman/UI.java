/**
 * 
 */
package taskman;

import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Julien Benaouda
 *
 */
public class UI {
	private Controller controller;
	
	private UI()
	{
		controller = new Controller();
	}
	
	public void welcomeDialog()
	{
		StringBuilder message = new StringBuilder("Welcome to the taskman project manager.\n");
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
		message.append("options:");
		message.append("1 - regular user\n2 - developer\n");
		message.append("chose option:");
		print(message.toString());
		int input = inputInt();
		try {
			switch (input) {
			case 1: controller.setUser("REGULARUSER");
			break;
			case 2: controller.setUser("DEVELOPER");
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
	 * Shows the main menu of the program and lets the user chose an option. If the user enters a non valid number, the is user is prompted again.
	 */
	public void showMainMenu()
	{
		StringBuilder message = new StringBuilder("options:\n");
		message.append("1 - list all projects\n");
		message.append("2 - get project details\n");
		message.append("3 - add new project\n");
		message.append("4 - advance system time\n");
		message.append("5 - import data\n");
		message.append("6 - export data\n");
		message.append("7 - change user\n");
		message.append("Chose option:");
		print(message.toString());
		int input = inputInt();
		switch (input) {
		case 1: listProjects();
		break;
		case 2: showProjectDetails();
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
	 * Prints the list of projects. Only the project names are printes.
	 */
	public void listProjects()
	{
		StringBuilder message = new StringBuilder();
		// [todo] add the list of projects
		print(message.toString());
		showMainMenu();
	}
	
	public void showProjectDetails()
	{
		print("project name: ");
		String name = inputString();
		HashMap<String, String> details = controller.getProjectDetails(name);
		
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
		int inputInt;
		try {
			Integer.parseInt(input);
		} catch (Exception e)
		{
			print("This is not a valid number, please try again.");
			inputInt();
		}
	}
	
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
