/**
 * 
 */
package taskman;

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
	
	public void showUserChoiceDialog()
	{
		StringBuilder message = new StringBuilder();
		message.append("In which user mode would you like to run the program\n");
		message.append("options:");
		message.append("1 - regular user\n2 - developer\n");
		message.append("chose option:");
		print(message.toString());
		int input = inputInt();
		switch (input) {
		case 1: controller.setUser("REGULARUSER");
		break;
		case 2: controller.setUser("DEVELOPER");
		break;
		default: input = inputInt();
		break;
		}
		showMainMenu();
	}
	
	public void showMainMenu()
	{
		StringBuilder message = new StringBuilder("options:\n");
		message.append("1 - list all projects\n");
		message.append("2 - get project details\n");
		message.append("3 - advance system time\n");
		message.append("4 - import data\n");
		message.append("5 - export data\n");
		message.append("6 - change user\n");
		message.append("Chose option:");
		print(message.toString());
	}
	
	/**
	 * Prints the given text to the console.
	 * @param text the text to print
	 */
	public void print(String text)
	{
		System.out.print(text);
	}
	
	private int inputInt()
	{
		Scanner sc = new Scanner(System.in);
		int input = 0;
		while(!sc.hasNextInt()) {
			input = sc.nextInt();
		}
		return input;
	}
}
