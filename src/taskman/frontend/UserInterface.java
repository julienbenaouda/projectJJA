package taskman.frontend;

import taskman.Pair;
import taskman.backend.Controller;
import taskman.backend.importExport.ImportExportException;
import taskman.backend.time.TimeParser;
import taskman.backend.wrappers.ProjectWrapper;
import taskman.backend.wrappers.ResourceWrapper;
import taskman.backend.wrappers.TaskWrapper;
import taskman.backend.wrappers.UserWrapper;
import taskman.frontend.sections.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		if (controller == null) throw new NullPointerException("Controller cannot be null!");
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
		TitleSection exit = new TitleSection("Bye!");
		exit.show();
	}

	/**
	 * Shows a start menu.
	 * @throws Cancel when the user cancels the section.
	 */
	private void startMenu() throws Cancel {
		TitleSection title = new TitleSection("start menu");
		MenuSection menu = new MenuSection("quit");
		menu.addOption("login", this::login);
		menu.addOption("show users", this::showUsers);
		menu.addOption("create user", this::createUser);
		menu.addOption("remove user", this::removeUser);
		menu.addOption("import from file", this::importFromFile);
		menu.addOption("export to file", this::exportToFile);
		while (true) {
			title.show();
			menu.show();
			menu.executeChoice();
		}
	}

	/**
	 * Shows login, menu and logout.
	 * @throws Cancel when the user cancels the section.
	 */
	private void login() throws Cancel {
		TitleSection title = new TitleSection("login");
		title.show();
		FormSection form = new FormSection(false, "Username:", "Password:");
		form.show();
		controller.login(form.getAnswer(0), form.getAnswer(1));
		try {
			loggedInMenu();
		} finally {
			Section info = new TextSection("Logging out...", false);
			info.show();
			controller.logout();
		}
	}

	/**
	 * Shows the users in the system.
	 */
	private void showUsers() {
		TitleSection title = new TitleSection("overview of users");
		title.show();
		TextSection info = new TextSection("", true);
		for (UserWrapper user: controller.getUsers()) info.addLine(user.getName() + " (" + user.getUserType() + ")");
		info.show();
	}

	/**
	 * Shows user creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createUser() throws Cancel {
		TitleSection title = new TitleSection("create user");
		title.show();
		FormSection form = new FormSection(false, "Username:", "Password:");
		form.show();
		SelectionSection<String> selection = new SelectionSection<>(true);
		selection.addOptions(this.controller.getUserTypes());
		selection.show();
		LocalTime startBreak = null;
		if (selection.getAnswer().equals("developer")) {
			FormSection breakForm = new FormSection(false, "Start of break time (hh:mm):");
			breakForm.show();
			startBreak = TimeParser.convertStringToLocalTime(breakForm.getAnswer(0));
		}
		controller.createUser(form.getAnswer(0), form.getAnswer(1), selection.getAnswer(), startBreak);
		Section success = new TextSection("user created successfully!", false);
		success.show();
	}

	/**
	 * Shows the option to remove the user.
	 * @throws Cancel when the user cancels the section.
	 * @throws IllegalArgumentException if the removal fails.
	 * @throws IllegalStateException if the removal fails.
	 */
	private void removeUser() throws Cancel {
		TitleSection title = new TitleSection("remove user");
		title.show();
		SelectionSection<UserWrapper> selection = new SelectionSection<>(false);
		for (UserWrapper user: controller.getUsers()) selection.addOption(user.getName(), user);
		selection.show();
		FormSection form = new FormSection(false, "Password:");
		form.show();
		controller.removeUser(selection.getAnswerObject().getName(), form.getAnswer(0));
		TextSection success = new TextSection("User removed successfully!", false);
		success.show();
	}

	/**
	 * Shows import form.
	 * @throws ImportExportException if import fails.
	 * @throws Cancel when the user cancels the section.
	 */
	private void importFromFile() throws ImportExportException, Cancel {
		TitleSection title = new TitleSection("import from file");
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
		TitleSection title = new TitleSection("export to file");
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
		TitleSection title = new TitleSection("main menu");
		MenuSection menu = new MenuSection("logout");
		menu.addOption("show projects and tasks", this::showProjectsAndTasks);
		menu.addOption("create project", this::createProject);
		menu.addOption("create task", this::createTask);
		menu.addOption("plan task", this::planTask);
		menu.addOption("update task status", this::updateTaskStatus);
		menu.addOption("add alternative to task", this::addAlternativeToTask);
		menu.addOption("add dependency to task", this::addDependencyToTask);
		menu.addOption("add resource type", this::addResourceType);
		menu.addOption("add resource type constraint", this::addConstraint);
		menu.addOption("add resource", this::addResource);
		menu.addOption("show system time", this::showTime);
		menu.addOption("advance system time", this::advanceTime);
		while (true) {
			title.show();
			menu.show();
			menu.executeChoice();
		}
	}

	/**
	 * Shows the list of projects.
	 * @throws Cancel when the user cancels the section.
	 */
	private void showProjectsAndTasks() throws Cancel {
		ProjectWrapper project = selectProject(true, "overview of projects");

		TitleSection titleProjectInfo = new TitleSection("details of " + project.getName());
		titleProjectInfo.show();
		TextSection projectInfo = new TextSection("", true);
		projectInfo.addLine("Name: " + project.getName());
		projectInfo.addLine("Description: " + project.getDescription());
		projectInfo.addLine("Creation time: " + project.getCreationTime());
		projectInfo.addLine("Due time: " + project.getDueTime());
		projectInfo.show();

		TaskWrapper task = selectTask(true, "overview of tasks in " + project.getName(), project);

		TitleSection titleTaskInfo = new TitleSection("details of " + task.getName());
		titleTaskInfo.show();
		TextSection taskInfo = new TextSection("", true);
		taskInfo.addLine("Name: " + task.getName());
		taskInfo.addLine("Description: " + task.getDescription());
		taskInfo.addLine("Status: " + task.getStatus());
		taskInfo.addLine("Acceptable deviation: " + task.getAcceptableDeviation());
		taskInfo.addLine("Estimated duration: " + task.getEstimatedDuration());
		taskInfo.show();
	}

	/**
	 * Shows project creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createProject() throws Cancel {
		TitleSection title = new TitleSection("create project");
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
		ProjectWrapper project = selectProject(true, "select project for task");
		TitleSection title = new TitleSection("create task");
		FormSection form = new FormSection(true,
				"Name:",
				"Description:",
				"Estimated duration (in minutes):",
				"Acceptable deviation (as floating point number):"
		);
		title.show();
		form.show();
		controller.createTask(
				project.getName(),
				form.getAnswer(0),
				form.getAnswer(1),
				Long.parseLong(form.getAnswer(2)),
				Double.parseDouble(form.getAnswer(3))
		);
		Section success = new TextSection("task created successfully!", false);
		success.show();
	}

	/**
	 * Shows plan task form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void planTask() throws Cancel {
		TitleSection taskTitle = new TitleSection("select unplanned task to plan");
		taskTitle.show();
		SelectionSection<Pair<ProjectWrapper, TaskWrapper>> selection1 = new SelectionSection<>(true);
		for (ProjectWrapper project: controller.getProjects()) {
			for (TaskWrapper task: project.getTasks()) {
				if (task.getStatus().equals("unavailable")) {
					selection1.addOption(project.getName() + " - " + task.getName(), new Pair<>(project, task));
				}
			}
		}
		selection1.show();
		Pair<ProjectWrapper, TaskWrapper> pair = selection1.getAnswerObject();
		ProjectWrapper project = pair.getFirst();
		TaskWrapper task = pair.getSecond();

		TitleSection timeTitle = new TitleSection("select start time");
		timeTitle.show();
		SelectionSection<LocalDateTime> timeSelection = new SelectionSection<>(true);
		Iterator<LocalDateTime> times = controller.getStartingsTimes(project.getName(), task.getName());
		for (int i = 1; i <= 3 && times.hasNext(); i++) {
			LocalDateTime nextTime = times.next();
			timeSelection.addOption(TimeParser.convertLocalDateTimeToString(nextTime), nextTime);
		}
		timeSelection.show();
		LocalDateTime startTime = timeSelection.getAnswerObject();

		List<ResourceWrapper> suggestion = controller.getAvailableResources(project.getName(), task.getName(), startTime);
		ResourceWrapper resourceToChange;
		while (true) {
			TitleSection resourceTitle = new TitleSection("continue or select resource to change" + task.getName());
			resourceTitle.show();
			SelectionSection<ResourceWrapper> resourceSelection = new SelectionSection<>(true);
			resourceSelection.addOption("continue", null);
			for (ResourceWrapper resourceWrapper: suggestion) {
				resourceSelection.addOption(resourceWrapper.getName() + " ("+ resourceWrapper.getType().getName() + ")", resourceWrapper);
			}
			resourceSelection.show();
			resourceToChange = resourceSelection.getAnswerObject();

			if (resourceToChange == null) {
				break;
			} else {
				TitleSection alternativeResourceTitle = new TitleSection("select alternative resource" + task.getName());
				alternativeResourceTitle.show();
				SelectionSection<ResourceWrapper> alternativeSelection = new SelectionSection<>(true);
				for (ResourceWrapper alternative: controller.getAlternativeResources(project.getName(), task.getName(), resourceToChange, startTime)) {
					alternativeSelection.addOption(alternative.getName() + " ("+ alternative.getType().getName() + ")", alternative);
				}
				alternativeSelection.show();
				ResourceWrapper alternative = alternativeSelection.getAnswerObject();
				suggestion.remove(resourceToChange);
				suggestion.add(alternative);
			}
		}

		List<Pair<String, String>> resourceInfo = new ArrayList<>();
		for (ResourceWrapper resource: suggestion) {
			resourceInfo.add(new Pair<>(resource.getType().getName(), resource.getName()));
		}
		controller.plan(project.getName(), task.getName(), resourceInfo, startTime);

		TextSection success = new TextSection("Task planned successfully!", false);
		success.show();
	}

	/**
	 * Shows task update form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void updateTaskStatus() throws Cancel {
		TitleSection titleSelection1 = new TitleSection("select an available task");
		titleSelection1.show();
		SelectionSection<Pair<ProjectWrapper, TaskWrapper>> selection1 = new SelectionSection<>(true);
		for (ProjectWrapper project: controller.getProjects()) {
			for (TaskWrapper task: project.getTasks()) {
				if (task.getStatus().equals("available")) {
					selection1.addOption(project.getName() + " - " + task.getName(), new Pair<>(project, task));
				}
			}
		}
		selection1.show();
		Pair<ProjectWrapper, TaskWrapper> pair = selection1.getAnswerObject();
		ProjectWrapper project = pair.getFirst();
		TaskWrapper task = pair.getSecond();

		TitleSection titleSelection2 = new TitleSection("select a task status");
		titleSelection2.show();
		SelectionSection selection2 = new SelectionSection(true);
		selection2.addOption("failed");
		selection2.addOption("finished");
		selection2.show();

		TitleSection titleForm = new TitleSection("status details");
		titleForm.show();
		FormSection form = new FormSection(true,
				"Start time (dd/mm/yyyy hh:mm):",
				"End time (dd/mm/yyyy hh:mm):"
		);
		form.show();

		controller.updateTaskStatus(
				project.getName(),
				task.getName(),
				TimeParser.convertStringToLocalDateTime(form.getAnswer(0)),
				TimeParser.convertStringToLocalDateTime(form.getAnswer(1)),
				selection2.getAnswer()
		);
	}

	/**
	 * Shows task update form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addAlternativeToTask() throws Cancel {
		ProjectWrapper project = selectProject(true, "select project of task");
		TaskWrapper task = selectTask(true, "select task", project);
		TaskWrapper alternative = selectTask(true, "select alternative task", project);
		controller.addAlternativeToTask(project.getName(), task.getName(), alternative.getName());
		Section success = new TextSection("Alternative added successfully!", false);
		success.show();
	}

	/**
	 * Shows add task dependency form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addDependencyToTask() throws Cancel {
		ProjectWrapper project = selectProject(true, "select project of task");
		TaskWrapper task = selectTask(true, "select task", project);
		TaskWrapper dependency = selectTask(true, "select dependent task", project);
		controller.addDependencyToTask(project.getName(), task.getName(), dependency.getName());
		Section success = new TextSection("Dependency added successfully!", false);
		success.show();
	}

	/**
	 * Shows the resource type creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addResourceType() throws Cancel {
		TitleSection title = new TitleSection("create resource type");
		title.show();
		FormSection form = new FormSection(false, "");
		form.show();

	}

	/**
	 * Shows the constraint creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addConstraint() throws Cancel {
		TitleSection title = new TitleSection("create constraint");
		title.show();
		FormSection form = new FormSection(false, "Constraint:");
		form.show();
		controller.addConstraint(form.getAnswer(0));
		Section success = new TextSection("Constraint added successfully!", false);
		success.show();
	}

	/**
	 * Shows the resource creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addResource() {
		// TODO
	}

	/**
	 * Shows time.
	 * @throws Cancel when the user cancels the section.
	 */
	private void showTime() throws Cancel {
		TitleSection titleInfo = new TitleSection("system time");
		titleInfo.show();
		Section info = new TextSection("The system time is: " + controller.getTime(), true);
		info.show();
	}

	/**
	 * Shows advance the time form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void advanceTime() throws Cancel {
		TitleSection titleForm = new TitleSection("advance system time");
		titleForm.show();
		FormSection form = new FormSection(true, "New system time:");
		form.show();
		if (!form.hasAnswers()) return; // Cancelled
		controller.updateTime(TimeParser.convertStringToLocalDateTime(form.getAnswer(0)));
		Section success = new TextSection("System time updated successfully!", false);
		success.show();
	}

	/**
	 * Shows project selection form.
	 * @return a projectWrapper.
	 * @throws Cancel when the user cancels the section.
	 */
	private ProjectWrapper selectProject(boolean withCancel, String title) throws Cancel {
		TitleSection titleProjectSelection = new TitleSection(title);
		titleProjectSelection.show();
		SelectionSection<ProjectWrapper> projectSelection = new SelectionSection<>(withCancel);
		for (ProjectWrapper project : controller.getProjects()) {
			String status = controller.getProjectStatus(project.getName());
			projectSelection.addOption(project.getName() + " (status: " + status + ")", project);
		}
		projectSelection.show();
		return projectSelection.getAnswerObject();
	}

	/**
	 * Shows task selection form for a given project.
	 * @param project a ProjectWrapper.
	 * @return a TaskWrapper.
	 * @throws Cancel when the user cancels the section.
	 */
	private TaskWrapper selectTask(boolean withCancel, String title, ProjectWrapper project) throws Cancel {
		TitleSection titleTaskSelection = new TitleSection(title);
		titleTaskSelection.show();
		SelectionSection<TaskWrapper> taskSelection = new SelectionSection<>(withCancel);
		for (TaskWrapper task: project.getTasks()) taskSelection.addOption(task.getName(), task);
		taskSelection.show();
		return taskSelection.getAnswerObject();
	}

}