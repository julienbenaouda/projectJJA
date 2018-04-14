package taskman.frontend;

import taskman.backend.Controller;
import taskman.backend.importExport.ImportExportException;
import taskman.frontend.sections.*;

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
		controller.createUser("admin", "admin", "project manager", null);

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
		Section title = new TitleSection("start menu");
		MenuSection menu = new MenuSection("quit");
		menu.addOption("login", this::login);
		menu.addOption("create user", this::userCreation);
		menu.addOption("import from file", this::importFromFile);
		menu.addOption("export to file", this::exportToFile);
		while (true) {
			title.show();
			menu.show();
		}
	}

	/**
	 * Shows login, menu and logout.
	 * @throws Cancel when the user cancels the section.
	 */
	private void login() throws Cancel {
		Section title = new TitleSection("login");
		title.show();
		FormSection form = new FormSection(false, "Username:", "Password:");
		form.show();
		controller.login(form.getAnswer(0), form.getAnswer(1));
		try {
			loggedInMenu();
		} catch (Exception e) {
			Section error = new TextSection("Action aborted: " + e.getMessage(), false);
			error.show();
		} finally {
			Section info = new TextSection("Logging out...", false);
			info.show();
			controller.logout();
		}
	}

	/**
	 * Shows user creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void userCreation() throws Cancel {
		Section title = new TitleSection("create user");
		title.show();
		FormSection form = new FormSection(false, "Username:", "Password:");
		form.show();
		SelectionSection<String> selection = new SelectionSection<>(true);
		for (String userType: this.controller.getUserTypes()) selection.addOption(userType);
		selection.show();
		controller.createUser(form.getAnswer(0), form.getAnswer(1), selection.getAnswer(), null);
		Section success = new TextSection("user created successfully!", false);
		success.show();
	}

	/**
	 * Shows import form.
	 * @throws ImportExportException if import fails.
	 * @throws Cancel when the user cancels the section.
	 */
	private void importFromFile() throws ImportExportException, Cancel {
		Section title = new TitleSection("import from file");
		title.show();
		FormSection form = new FormSection(false, "Path to file:");
		form.show();
		this.controller = Controller.importSystem(form.getAnswer(0));
		Section success = new TextSection("Imported successfully!", false);
		success.show();
	}

	/**
	 * Shows export form.
	 * @throws ImportExportException if export fails.
	 * @throws Cancel when the user cancels the section.
	 */
	private void exportToFile() throws ImportExportException, Cancel {
		Section title = new TitleSection("export to file");
		title.show();
		FormSection form = new FormSection(false, "Path to file:");
		form.show();
		controller.exportSystem(form.getAnswer(0));
		Section success = new TextSection("Exported successfully!", false);
		success.show();
	}

	/**
	 * Shows logged in menu.
	 * @throws Cancel when the user cancels the section.
	 */
	private void loggedInMenu() throws Cancel {
		Section title = new TitleSection("main menu");
		MenuSection menu = new MenuSection("logout");
		menu.addOption("show projects", this::showProjects);
		menu.addOption("create project", this::createProject);
		menu.addOption("create task", this::createTask);
		menu.addOption("plan task", this::planTask);
		menu.addOption("update task status", this::updateTaskStatus);
		menu.addOption("add alternative to task", this::addAlternativeToTask);
		menu.addOption("add dependency to task", this::addDependencyToTask);
		menu.addOption("show system time", this::showSystemTime);
		menu.addOption("advance system time", this::advanceSystemTime);
		while (true) {
			title.show();
			menu.show();
		}
	}

	/**
	 * Shows the list of projects.
	 * @throws Cancel when the user cancels the section.
	 */
	private void showProjects() throws Cancel {
		String project = selectProject(true, "overview of projects");

		Section titleProjectInfo = new TitleSection("details of " + project);
		titleProjectInfo.show();
		Section projectInfo = new TextSection(mapToString(controller.getProjectDetails(project)), true);
		projectInfo.show();

		Integer taskNr = selectTaskNr(true, "overview of tasks in " + project, project);

		Section titleTaskInfo = new TitleSection("details of task " + taskNr);
		titleTaskInfo.show();
		Section taskInfo = new TextSection(mapToString(controller.getTaskDetails(project, taskNr)), true);
		taskInfo.show();
	}

	/**
	 * Shows project creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createProject() throws Cancel {
		Section title = new TitleSection("create project");
		title.show();
		FormSection form = new FormSection(true, "Name:", "Description:", "Due time (dd/mm/yyyy hh:mm):");
		form.show();
		controller.createProject(form.getAnswer(0), form.getAnswer(1), form.getAnswer(2));
		Section success = new TextSection("project created successfully!", false);
		success.show();
	}

	/**
	 * Shows task creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createTask() throws Cancel {
		String project = selectProject(true, "select project for task");
		Section title = new TitleSection("create task");
		FormSection form = new FormSection(true,
				"Description:",
				"Estimated duration (in minutes):",
				"Acceptable deviation (as floating point number):"
		);
		title.show();
		form.show();
		controller.createTask(project, form.getAnswer(0), form.getAnswer(1), form.getAnswer(2));
		Section success = new TextSection("task created successfully!", false);
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
		Section titleSelection1 = new TitleSection("select an available task");
		titleSelection1.show();
		SelectionSection<Pair<String, Integer>> selection1 = new SelectionSection<>(true);
		for (String project: controller.getProjectNames()) {
			Integer nr = controller.getNumberOfTasks(project);
			for (int taskNr = 0; taskNr < nr; taskNr++) {
				if (controller.getTaskStatus(project, taskNr).equals("available")) {
					selection1.addOption(project + " - task " + taskNr, new Pair<>(project, taskNr));
				}
			}
		}
		selection1.show();
		Pair<String, Integer> pair = selection1.getAnswerObject();
		String project = pair.getFirst();
		Integer taskNr = pair.getSecond();

		Section titleSelection2 = new TitleSection("select a task status");
		titleSelection2.show();
		SelectionSection selection2 = new SelectionSection(true);
		selection2.addOption("failed");
		selection2.addOption("finished");
		selection2.show();

		Section titleForm = new TitleSection("status details");
		titleForm.show();
		FormSection form = new FormSection(true,
				"Start time (dd/mm/yyyy hh:mm):",
				"End time (dd/mm/yyyy hh:mm):"
		);
		form.show();

		controller.updateTaskStatus(project, taskNr, form.getAnswer(0), form.getAnswer(1), selection2.getAnswer());
	}

	/**
	 * Shows task update form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addAlternativeToTask() throws Cancel {
		String project = selectProject(true, "select project of task");
		Integer taskNr = selectTaskNr(true, "select task", project);
		Integer alternativeNr = selectTaskNr(true, "select alternative task", project);
		controller.addAlternativeToTask(project, taskNr, alternativeNr);
		Section success = new TextSection("Alternative added successfully!", false);
		success.show();
	}

	/**
	 * Shows add task dependency form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addDependencyToTask() throws Cancel {
		String project = selectProject(true, "select project of task");
		Integer taskNr = selectTaskNr(true, "select task", project);
		Integer alternativeNr = selectTaskNr(true, "select dependent task", project);
		controller.addDependencyToTask(project, taskNr, alternativeNr);
		Section success = new TextSection("Dependency added successfully!", false);
		success.show();
	}

	/**
	 * Shows system time.
	 * @throws Cancel when the user cancels the section.
	 */
	private void showSystemTime() throws Cancel {
		Section titleInfo = new TitleSection("system time");
		titleInfo.show();
		Section info = new TextSection("The system time is: " + controller.getSystemTime(), true);
		info.show();
	}

	/**
	 * Shows advance the system time form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void advanceSystemTime() throws Cancel {
		Section titleForm = new TitleSection("advance system time");
		titleForm.show();
		FormSection form = new FormSection(true, "New system time:");
		form.show();
		if (!form.hasAnswers()) return; // Cancelled
		controller.updateSystemTime(form.getAnswer(0));
		Section success = new TextSection("System time updated successfully!", false);
		success.show();
	}

	/**
	 * Shows project selection form.
	 * @return project or null.
	 * @throws Cancel when the user cancels the section.
	 */
	private String selectProject(boolean withCancel, String title) throws Cancel {
		Section titleProjectSelection = new TitleSection(title);
		titleProjectSelection.show();
		SelectionSection<String> projectSelection = new SelectionSection<>(withCancel);
		for (String project : controller.getProjectNames()) {
			projectSelection.addOption(project + " (status: " + controller.getProjectStatus(project) + ")", project);
		}
		projectSelection.show();
		return projectSelection.getAnswerObject();
	}

	/**
	 * Shows task selection form for a given project.
	 * @param project the project.
	 * @return the number of the task or null.
	 * @throws Cancel when the user cancels the section.
	 */
	private Integer selectTaskNr(boolean withCancel, String title, String project) throws Cancel {
		Section titleTaskSelection = new TitleSection(title);
		titleTaskSelection.show();
		SelectionSection<Integer> taskSelection = new SelectionSection<>(withCancel);
		int nrs = controller.getNumberOfTasks(project);
		for (int i = 0; i < nrs; i++) {
			taskSelection.addOption("task " + i, i);
		}
		taskSelection.show();
		return taskSelection.getAnswerObject();
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