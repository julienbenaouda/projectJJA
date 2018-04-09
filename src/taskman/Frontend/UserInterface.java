package taskman.Frontend;

import taskman.Backend.Controller;
import taskman.Backend.ImportExportException;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible for the user interface of the taskman application.
 * @author Julien Benaouda, Jeroen Van Der Donckt, Alexander Braekevelt
 */
public class UserInterface {

	/**
	 * Represents the controller of the backend.
	 */
	private Controller controller;

	/**
	 * Creates a new UserInterface object.
	 * @param controller the controller that the UserInterface will use.
	 * @throws NullPointerException if the controller is null.
	 */
	public UserInterface(Controller controller) throws NullPointerException {
		if (controller == null) {
			throw new NullPointerException("Controller cannot be null!");
		}
		this.controller = controller;
	}

	/**
	 * Starts the user interface.
	 */
	public void start() {

		// Create default user
		controller.createUser("admin", "admin", "project manager");

		// Show start menu
		try {
			startMenu();
		} catch (Cancel ignored) {}
	}

	/**
	 * Shows a start menu.
	 * @throws Cancel when the user cancels the section.
	 */
	private void startMenu() throws Cancel {
		MenuSection menu = new MenuSection();
		menu.addOption("login", this::login);
		menu.addOption("create user", this::userCreation);
		menu.addOption("import from file", this::importFromFile);
		menu.addOption("export to file", this::exportToFile);
		Section titledMenu = new TitleDecoratorSection("start menu", menu);
		while (true) {
			titledMenu.show();
		}
	}

	/**
	 * Shows login, menu and logout.
	 * @throws Cancel when the user cancels the section.
	 */
	private void login() throws Cancel {
		FormSection form = new FormSection(false, "username:", "password:");
		Section titledForm = new TitleDecoratorSection("login", form);
		titledForm.show();
		controller.login(form.getAnswer(0), form.getAnswer(1));
		try {
			loggedInMenu();
		} catch (Exception e) {
			controller.logout();
			throw e;
		}
		controller.logout();
	}

	/**
	 * Shows user creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void userCreation() throws Cancel {
		FormSection form = new FormSection(false, "username:", "password:");
		Section titledForm = new TitleDecoratorSection("create user", form);
		titledForm.show();
		SelectionSection selection = new SelectionSection(true);
		selection.addOptions(this.controller.getUserTypes());
		selection.show();
		controller.createUser(form.getAnswer(0), form.getAnswer(1), selection.getAnswer());
		Section success = new InfoSection("User created successfully!", false);
		success.show();
	}

	/**
	 * Shows import form.
	 * @throws ImportExportException if import fails.
	 * @throws Cancel when the user cancels the section.
	 */
	private void importFromFile() throws ImportExportException, Cancel {
		FormSection form = new FormSection(false, "path to file:");
		Section titledForm = new TitleDecoratorSection("import from file", form);
		titledForm.show();
		this.controller = Controller.importSystem(form.getAnswer(0));
		Section success = new InfoSection("Imported successfully!", false);
		success.show();
	}

	/**
	 * Shows export form.
	 * @throws ImportExportException if export fails.
	 * @throws Cancel when the user cancels the section.
	 */
	private void exportToFile() throws ImportExportException, Cancel {
		FormSection form = new FormSection(false, "path to file:");
		Section titledForm = new TitleDecoratorSection("export to file", form);
		titledForm.show();
		controller.exportSystem(form.getAnswer(0));
		Section success = new InfoSection("Exported successfully!", false);
		success.show();
	}

	/**
	 * Shows logged in menu.
	 * @throws Cancel when the user cancels the section.
	 */
	private void loggedInMenu() throws Cancel {
		MenuSection menu = new MenuSection();
		menu.addOption("show projects", this::showProjects);
		menu.addOption("create project", this::createProject);
		menu.addOption("create task", this::createTask);
		menu.addOption("plan task", this::planTask);
		menu.addOption("update task status", this::updateTaskStatus);
		menu.addOption("add alternative to task", this::addAlternativeToTask);
		menu.addOption("add dependency to task", this::addDependencyToTask);
		menu.addOption("show system time", this::showSystemTime);
		menu.addOption("advance system time", this::advanceSystemTime);
		Section titledMenu = new TitleDecoratorSection("main menu", menu);
		while (true) {
			titledMenu.show();
		}
	}

	/**
	 * Shows the list of projects.
	 * @throws Cancel when the user cancels the section.
	 */
	private void showProjects() throws Cancel {
		String project = selectProject(true, "overview of projects");

		Section projectInfo = new InfoSection(mapToString(controller.getProjectDetails(project)), true);
		Section titledProjectInfo = new TitleDecoratorSection("details of " + project, projectInfo);
		titledProjectInfo.show();

		Integer taskNr = selectTaskNr(true, "overview of tasks in " + project, project);

		Section taskInfo = new InfoSection(mapToString(controller.getTaskDetails(project, taskNr)), true);
		Section titledTaskInfo = new TitleDecoratorSection("details of task " + taskNr, taskInfo);
		titledTaskInfo.show();
	}

	/**
	 * Shows project creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createProject() throws Cancel {
		FormSection form = new FormSection(true, "name:", "description:", "dueTime (dd/mm/yyyy hh:mm):");
		Section titledForm = new TitleDecoratorSection("create project", form);
		titledForm.show();
		controller.createProject(form.getAnswer(0), form.getAnswer(1), form.getAnswer(2));
		Section success = new InfoSection("Project created successfully!", false);
		success.show();
	}

	/**
	 * Shows task creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createTask() throws Cancel {
		String project = selectProject(true, "select project for task");
		FormSection form = new FormSection(true,
				"description:",
				"estimatedDuration (in minutes):",
				"acceptableDeviation (as floating point number):"
		);
		Section titledForm = new TitleDecoratorSection("create task", form);
		titledForm.show();
		controller.createTask(project, form.getAnswer(0), form.getAnswer(1), form.getAnswer(2));
		Section success = new InfoSection("Task created successfully!", false);
		success.show();
	}

	/**
	 * Shows plan task form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void planTask() throws Cancel {
		// TODO
	}

	/**
	 * Shows task update form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void updateTaskStatus() throws Cancel {
		// TODO
	}

	/**
	 * Shows task update form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addAlternativeToTask() throws Cancel {
		String project = selectProject(true, "select project for task");
		Integer taskNr = selectTaskNr(true, "select task", project);
		Integer alternativeNr = selectTaskNr(true, "select new alternative task", project);
		controller.addAlternativeToTask(project, taskNr, alternativeNr);
		Section success = new InfoSection("Alternative added successfully!", false);
		success.show();
	}

	/**
	 * Shows add task dependency form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addDependencyToTask() throws Cancel {
		String project = selectProject(true, "select project for task");
		Integer taskNr = selectTaskNr(true, "select task", project);
		Integer alternativeNr = selectTaskNr(true, "select new dependent task", project);
		controller.addDependencyToTask(project, taskNr, alternativeNr);
		Section success = new InfoSection("Dependency added successfully!", false);
		success.show();
	}

	/**
	 * Shows system time.
	 * @throws Cancel when the user cancels the section.
	 */
	private void showSystemTime() throws Cancel {
		Section info = new InfoSection("The system time is: " + controller.getSystemTime(), true);
		Section titledInfo = new TitleDecoratorSection("system time", info);
		titledInfo.show();
	}

	/**
	 * Shows advance the system time form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void advanceSystemTime() throws Cancel {
		FormSection form = new FormSection(true, "new system time:");
		Section titledForm = new TitleDecoratorSection("advance system time", form);
		titledForm.show();
		if (!form.hasAnswers()) return; // Cancelled
		controller.updateSystemTime(form.getAnswer(0));
		Section success = new InfoSection("System time updated successfully!", false);
		success.show();
	}

	/**
	 * Shows project selection form.
	 * @return project or null.
	 * @throws Cancel when the user cancels the section.
	 */
	private String selectProject(boolean withCancel, String title) throws Cancel {
		SelectionSection projectSelection = new SelectionSection(withCancel);
		List<String> projects = controller.getProjectNames();
		for (String project : projects) {
			projectSelection.addOption(project + " (Status: " + controller.getProjectStatus(project) + ")");
		}
		Section titledProjectSelection = new TitleDecoratorSection(title, projectSelection);
		titledProjectSelection.show();
		return projects.get(projectSelection.getAnswerNumber());
	}

	/**
	 * Shows task selection form for a given project.
	 * @param project the project.
	 * @return the number of the task or null.
	 * @throws Cancel when the user cancels the section.
	 */
	private Integer selectTaskNr(boolean withCancel, String title, String project) throws Cancel {
		SelectionSection taskSelection = new SelectionSection(withCancel);
		int nrs = controller.getNumberOfTasks(project);
		for (int i = 0; i < nrs; i++) {
			taskSelection.addOption("task " + i);
		}
		Section titledTaskSelection = new TitleDecoratorSection(title, taskSelection);
		titledTaskSelection.show();
		return taskSelection.getAnswerNumber();
	}

	/**
	 * Converts a map with strings to a string.
	 * @param map the map with strings.
	 * @return the resulting string.
	 */
	private String mapToString(Map<String, String> map) {
		StringBuilder str = new StringBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			str.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
		return str.toString();
	}
}