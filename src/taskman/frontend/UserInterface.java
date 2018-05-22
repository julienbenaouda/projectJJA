package taskman.frontend;

import taskman.backend.Controller;
import taskman.backend.importexport.ImportExportException;
import taskman.backend.time.TimeParser;
import taskman.backend.wrappers.*;
import taskman.frontend.sections.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
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
		if (controller == null) throw new NullPointerException("Controller cannot be null!");
		this.controller = controller;
	}

	/**
	 * Starts the user interface.
	 */
	public void start() {

		// Create default user
		controller.createBranchOffice("main office");
		controller.createUser(controller.getBranchOffices().get(0), "admin", "admin", "project manager", null);

		// Show start menu
		try {
			startMenu();
		} catch (Cancel ignored) {}
		TitleSection exit = new TitleSection("Bye!");
		exit.show();
		// System.exit(0);
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
		//noinspection InfiniteLoopStatement
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
		TitleSection title1 = new TitleSection("select branch office");
		title1.show();
		SelectionSection<BranchOfficeWrapper> officeSelection = new SelectionSection<>(true);
		for (BranchOfficeWrapper b: controller.getBranchOffices()){
			officeSelection.addOption(b.getName(), b);
		}
		officeSelection.show();

		TitleSection title2 = new TitleSection("select user");
		title2.show();
		SelectionSection<UserWrapper> userSelection = new SelectionSection<>(true);
		for (UserWrapper u: controller.getUsers(officeSelection.getAnswerObject())) {
			userSelection.addOption(u.getName(), u);
		}
		userSelection.show();

		TitleSection title3 = new TitleSection("login");
		title3.show();
		FormSection form = new FormSection(false, "Password:");
		form.show();
		controller.login(officeSelection.getAnswerObject(), userSelection.getAnswer(), form.getAnswer(0));
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
	 * @throws Cancel when the user cancels the section.
	 */
	private void showUsers() throws Cancel {
		TitleSection title1 = new TitleSection("select branch office");
		title1.show();
		SelectionSection<BranchOfficeWrapper> officeSelection = new SelectionSection<>(true);
		for (BranchOfficeWrapper b: controller.getBranchOffices()){
			officeSelection.addOption(b.getName(), b);
		}
		officeSelection.show();

		TitleSection title = new TitleSection("overview of users");
		title.show();
		TextSection info = new TextSection("", true);
		for (UserWrapper user: controller.getUsers(officeSelection.getAnswerObject())) {
			info.addLine(user.getName() + " (" + user.getUserType() + ")");
		}
		info.show();
	}

	/**
	 * Shows user creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createUser() throws Cancel {
		TitleSection title1 = new TitleSection("select branch office");
		title1.show();
		SelectionSection<BranchOfficeWrapper> officeSelection = new SelectionSection<>(true);
		for (BranchOfficeWrapper b: controller.getBranchOffices()){
			officeSelection.addOption(b.getName(), b);
		}
		officeSelection.show();

		TitleSection title2 = new TitleSection("create user");
		title2.show();
		FormSection form = new FormSection(false, "Username:", "Password:");
		form.show();

		TitleSection title3 = new TitleSection("select type");
		title3.show();
		SelectionSection<String> selection = new SelectionSection<>(true);
		selection.addOptions(this.controller.getUserTypes());
		selection.show();

		LocalTime startBreak = null;
		if (selection.getAnswer().equals("developer")) {
			FormSection breakForm = new FormSection(false, "Start of break time (hh:mm):");
			breakForm.show();
			startBreak = TimeParser.convertStringToLocalTime(breakForm.getAnswer(0));
		}

		controller.createUser(
				officeSelection.getAnswerObject(),
				form.getAnswer(0), form.getAnswer(1),
				selection.getAnswer(), startBreak
		);
		Section success = new TextSection("User created successfully!", false);
		success.show();
	}

	/**
	 * Shows the option to remove the user.
	 * @throws Cancel when the user cancels the section.
	 * @throws IllegalArgumentException if the removal fails.
	 * @throws IllegalStateException if the removal fails.
	 */
	private void removeUser() throws Cancel {
		TitleSection title1 = new TitleSection("select branch office");
		title1.show();
		SelectionSection<BranchOfficeWrapper> officeSelection = new SelectionSection<>(true);
		for (BranchOfficeWrapper b: controller.getBranchOffices()){
			officeSelection.addOption(b.getName(), b);
		}
		officeSelection.show();

		TitleSection title2 = new TitleSection("remove user");
		title2.show();
		SelectionSection<UserWrapper> selection = new SelectionSection<>(false);
		for (UserWrapper user: controller.getUsers(officeSelection.getAnswerObject())) {
			selection.addOption(user.getName(), user);
		}
		selection.show();

		TitleSection title3 = new TitleSection("give password");
		title3.show();
		FormSection form = new FormSection(false, "Password:");
		form.show();

		controller.removeUser(officeSelection.getAnswerObject(), selection.getAnswerObject(), form.getAnswer(0));
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
		this.controller.importSystem(form.getAnswer(0));
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
		TitleSection title = new TitleSection("Welcome " + controller.getCurrentUser().getName() + "!");
		MenuSection menu = new MenuSection("logout");
		menu.addOption("show projects and tasks", this::showProjectsAndTasks);
		menu.addOption("create project", this::createProject);
		menu.addOption("create task", this::createTask);
		menu.addOption("plan task", this::planTask);
		menu.addOption("update task status", this::updateTaskStatus);
		menu.addOption("add alternative to task", this::addAlternativeToTask);
		menu.addOption("add dependency to task", this::addDependencyToTask);
		menu.addOption("create resource type", this::createResourceType);
		menu.addOption("create resource type constraint", this::createConstraint);
		menu.addOption("create resource", this::createResource);
		menu.addOption("show system time", this::showTime);
		menu.addOption("advance system time", this::advanceTime);
		menu.addOption("start simulation", this::simulationMenu);
		//noinspection InfiniteLoopStatement
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
		projectInfo.addLine("Creation time: " + TimeParser.convertLocalDateTimeToString(project.getCreationTime()));
		projectInfo.addLine("Due time: " + TimeParser.convertLocalDateTimeToString(project.getDueTime()));
		projectInfo.show();

		TaskWrapper task = selectTask(true, "overview of tasks in " + project.getName(), project);

		TitleSection titleTaskInfo = new TitleSection("details of " + task.getName());
		titleTaskInfo.show();
		TextSection taskInfo = new TextSection("", true);
		taskInfo.addLine("Name: " + task.getName());
		taskInfo.addLine("Description: " + task.getDescription());
		taskInfo.addLine("Status: " + task.getStatus());
		taskInfo.addLine("Acceptable deviation: " + task.getAcceptableDeviation());
		taskInfo.addLine("Estimated duration: " + task.getEstimatedDuration() + " minutes");
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
		controller.createProject(form.getAnswer(0), form.getAnswer(1), TimeParser.convertStringToLocalDateTime(form.getAnswer(2)));
		Section success = new TextSection("Project created successfully!", false);
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

		Map<ResourceTypeWrapper, Integer> requirements = new HashMap<>();
		while (true) {
			TitleSection requirementsTitle = new TitleSection("continue or add requirements to task");
			requirementsTitle.show();
			SelectionSection<ResourceTypeWrapper> requirementSelection = new SelectionSection<>(true);
			requirementSelection.addOption("continue", null);
			for (ResourceTypeWrapper resourceType : controller.getResourceTypes()) {
				requirementSelection.addOption(resourceType.getName(), resourceType);
			}
			requirementSelection.show();
			if (requirementSelection.getAnswerObject() == null) {
				break;
			} else {
				FormSection numberForm = new FormSection(false, "Number required:");
				numberForm.show();
				requirements.put(requirementSelection.getAnswerObject(), Integer.parseInt(numberForm.getAnswer(0)));
			}
		}

		controller.createTask(
				project,
				form.getAnswer(0),
				form.getAnswer(1),
				Long.parseLong(form.getAnswer(2)),
				Double.parseDouble(form.getAnswer(3)),
				requirements
		);
		Section success = new TextSection("Task created successfully!", false);
		success.show();

	}

	/**
	 * Shows plan task form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void planTask() throws Cancel {
		TitleSection taskTitle = new TitleSection("select task to plan");
		taskTitle.show();
		SelectionSection<TaskWrapper> selection1 = new SelectionSection<>(true);
		for (ProjectWrapper project: controller.getProjects()) {
			for (TaskWrapper task: controller.getTasks(project)) {
				if (task.canBePlanned()) {
					selection1.addOption(project.getName() + " - " + task.getName(), task);
				}
			}
		}
		selection1.show();
		TaskWrapper task = selection1.getAnswerObject();

		TitleSection timeTitle = new TitleSection("select start time");
		timeTitle.show();
		SelectionSection<LocalDateTime> timeSelection = new SelectionSection<>(true);
		Iterator<LocalDateTime> times = controller.getStartingTimes(task);
		for (int i = 1; i <= 3 && times.hasNext(); i++) {
			LocalDateTime nextTime = times.next();
			timeSelection.addOption(TimeParser.convertLocalDateTimeToString(nextTime), nextTime);
		}
		timeSelection.show();
		LocalDateTime startTime = timeSelection.getAnswerObject();

		controller.initializePlan(task, startTime);
		try {
			List<ResourceWrapper> suggestion = controller.getPlannedResources(task);
			while (true) {
				TitleSection resourceTitle = new TitleSection("continue or select resource to change " + task.getName());
				resourceTitle.show();
				SelectionSection<ResourceWrapper> resourceSelection = new SelectionSection<>(true);
				resourceSelection.addOption("continue", null);
				for (ResourceWrapper resourceWrapper : suggestion) {
					resourceSelection.addOption(resourceWrapper.getName() + " (" + resourceWrapper.getType().getName() + ")", resourceWrapper);
				}
				resourceSelection.show();
				ResourceWrapper resourceToChange = resourceSelection.getAnswerObject();

				if (resourceToChange == null) {
					TextSection success = new TextSection("Task planned successfully!", false);
					success.show();
					return;
				} else {
					TitleSection alternativeResourceTitle = new TitleSection("select alternative resource" + task.getName());
					alternativeResourceTitle.show();
					SelectionSection<ResourceWrapper> alternativeSelection = new SelectionSection<>(true);
					for (ResourceWrapper alternative : controller.getAlternativeResources(task, resourceToChange)) {
						alternativeSelection.addOption(alternative.getName() + " (" + alternative.getType().getName() + ")", alternative);
					}
					alternativeSelection.show();
					ResourceWrapper alternative = alternativeSelection.getAnswerObject();
					controller.changeResource(task, resourceToChange, alternative);
					suggestion = controller.getPlannedResources(task);
				}
			}
		} catch (Exception e) {
			controller.cancelPlan(task);
			throw e;
		}
	}

	/**
	 * Shows task update form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void updateTaskStatus() throws Cancel {
		TitleSection titleSelection1 = new TitleSection("select an available task");
		titleSelection1.show();
		SelectionSection<TaskWrapper> selection1 = new SelectionSection<>(true);
		for (ProjectWrapper project: controller.getProjects()) {
			for (TaskWrapper task: controller.getTasks(project)) {
				if (task.canBeUpdated()) {
					selection1.addOption(project.getName() + " - " + task.getName(), task);
				}
			}
		}
		selection1.show();
		TaskWrapper task = selection1.getAnswerObject();

		TitleSection titleSelection2 = new TitleSection("select a task status");
		titleSelection2.show();
		SelectionSection<String> selection2 = new SelectionSection<>(true);
		selection2.addOption("executing");
		selection2.addOption("finished");
		selection2.addOption("failed");
		selection2.show();

		TitleSection titleForm = new TitleSection("status details");
		titleForm.show();
		FormSection form = new FormSection(true,
				"Start time (dd/mm/yyyy hh:mm):",
				"End time (dd/mm/yyyy hh:mm):"
		);
		form.show();
		if (selection2.getAnswer().equals("executing")) {
			controller.makeExecuting(task);
		} else {
			controller.endTaskExecution(
					task,
					TimeParser.convertStringToLocalDateTime(form.getAnswer(0)),
					TimeParser.convertStringToLocalDateTime(form.getAnswer(1)),
					selection2.getAnswer()
			);
		}
	}

	/**
	 * Shows task update form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void addAlternativeToTask() throws Cancel {
		ProjectWrapper project = selectProject(true, "select project of task");
		TaskWrapper task = selectTask(true, "select task", project);
		TaskWrapper alternative = selectTask(true, "select alternative task", project);
		controller.addAlternativeToTask(task, alternative);
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
		controller.addDependencyToTask(task, dependency);
		Section success = new TextSection("Dependency added successfully!", false);
		success.show();
	}

	/**
	 * Shows the resource type creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createResourceType() throws Cancel {
		TitleSection title = new TitleSection("create resource type");
		title.show();
		FormSection form = new FormSection(false, "Name:");
		form.show();
		controller.createResourceType(form.getAnswer(0));
		Section success = new TextSection("Resource type created successfully!", false);
		success.show();
	}

	/**
	 * Shows the constraint creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createConstraint() throws Cancel {
		TitleSection title = new TitleSection("create constraint");
		title.show();
		FormSection form = new FormSection(false, "Constraint:");
		form.show();
		controller.createConstraint(form.getAnswer(0));
		Section success = new TextSection("Constraint created successfully!", false);
		success.show();
	}

	/**
	 * Shows the resource creation form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void createResource() throws Cancel {
		TitleSection title1 = new TitleSection("select resource type");
		title1.show();
		SelectionSection<ResourceTypeWrapper> selection = new SelectionSection<>(true);
		for (ResourceTypeWrapper type: this.controller.getResourceTypes()) {
			selection.addOption(type.getName(), type);
		}
		selection.show();
		ResourceTypeWrapper type = selection.getAnswerObject();

		TitleSection title2 = new TitleSection("create resource");
		title2.show();
		FormSection form = new FormSection(false, "Name:");
		form.show();

		controller.createResource(type, form.getAnswer(0));
		Section success = new TextSection("Resource created successfully!", false);
		success.show();
	}

	/**
	 * Shows time.
	 * @throws Cancel when the user cancels the section.
	 */
	private void showTime() throws Cancel {
		TitleSection titleInfo = new TitleSection("system time");
		titleInfo.show();
		Section info = new TextSection(
				"The system time is: " + TimeParser.convertLocalDateTimeToString(controller.getTime()
				), true);
		info.show();
	}

	/**
	 * Shows advance the time form.
	 * @throws Cancel when the user cancels the section.
	 */
	private void advanceTime() throws Cancel {
		TitleSection titleForm = new TitleSection("advance system time");
		titleForm.show();
		FormSection form = new FormSection(false, "New time (dd/mm/uuuu hh:mm):");
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
			projectSelection.addOption(
					project.getName() + " (status: " + controller.getProjectStatus(project) + ")", project);
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
		for (TaskWrapper task: controller.getTasks(project)) taskSelection.addOption(task.getName(), task);
		taskSelection.show();
		return taskSelection.getAnswerObject();
	}
	
	/**
	 * show the simulation menu
	 * @throws Cancel when the simulation is cancelled
	 * @throws ImportExportException 
	 * @throws IllegalStateException 
	 */
	private void simulationMenu() throws Cancel, IllegalStateException, ImportExportException {
		controller.startSimulation();
		TitleSection title = new TitleSection("Simulation");
		MenuSection menu = new MenuSection("cancel simulation");
		menu.addOption("show projects and tasks", this::showProjectsAndTasks);
		menu.addOption("create task", this::createTask);
		menu.addOption("plan task", this::planTask);
		menu.addOption("execute simulation", this::executeSimulation);
		//noinspection InfiniteLoopStatement
		while (true) {
			title.show();
			try {
				menu.show();
			} catch(Cancel c) {
				cancelSimulation();
				throw c;
			}
			menu.executeChoice();
		}
	}
	
	/**
	 * cancels the simulation
	 * @throws ImportExportException 
	 * @throws IllegalStateException 
	 */
	private void cancelSimulation() throws IllegalStateException, ImportExportException {
		controller.cancelSimulation();
	}

	/**
	 * executes the simulation
	 * @throws Cancel when the action is cancelled 
	 */
	private void executeSimulation() throws Cancel {
		controller.executeSimulation();
		Section success = new TextSection("Simulated actions are executed in system!", false);
		success.show();
	}

}