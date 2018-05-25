package test.backend.task;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.project.Project;
import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.resource.ResourceType;
import taskman.backend.task.*;
import taskman.backend.time.AvailabilityPeriod;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.Developer;
import taskman.backend.user.Manager;
import taskman.backend.user.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a test class for the task class.
 * @author Jeroen Van Der Donckt
*/

public class TaskTest {

	private static Task root;
	private static Task alternative1_3;
	private static Task alternative1_2_1;
	private static Task dependency1_2;
	private static Task dependency1_1_3;
	private static Task alternative1_3d;
	private static ResourceManager resourceManager;
	private static Manager admin;

	@Before
	public void init(){
		resourceManager = new ResourceManager();
	}

	@BeforeClass
	public static void setUp(){
		admin = new Manager("admin", "admin");

		long duration = 22;
		double deviation = 0.15;
		Task task = new Task("task", "Very interesting description.", duration, deviation);

		long estimatedDuration = 5;
		double acceptableDeviation = 0.2356;

		root = new Task("task", "root description", estimatedDuration, acceptableDeviation){
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status){
					case "finished" : state = new TaskStateFinished();
						break;
					case "failed" : state = new TaskStateFailed();
						break;
					case "unavailable" : state = new TaskStateUnavailable();
						break;
				}
			}

			@Override
			public TaskState getState(){
				return state;
			}
		};
		root.endExecution(null, null,"unavailable", null);

		Task dependency1_1 = new Task ("task", "dependency 1_1 description", estimatedDuration, acceptableDeviation){
			private Task alternative;

			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};
		dependency1_2 = new Task ("task", "dependency 1_2 description", estimatedDuration, acceptableDeviation){
			private Task alternative;

			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};
		Task dependency1_3 = new Task ("task", "dependency 1_3 description", estimatedDuration, acceptableDeviation){
			private Task alternative;

			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};

		root.addDependency(dependency1_1);
		root.addDependency(dependency1_2);
		root.addDependency(dependency1_3);

		Task alternative1_1 = new Task ("task", "alternative 1_1 description", estimatedDuration, acceptableDeviation);
		Task alternative1_2 = null;
		alternative1_3 = new Task ("task", "alternative 1_3 description", estimatedDuration, acceptableDeviation){
			private Task alternative;
			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};

		Task alternative1_3a = new Task ("task", "alternative 1_3a description", estimatedDuration, acceptableDeviation) {
			private Task alternative;
			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};
		Task alternative1_3b = new Task ("task", "alternative 1_3b description", estimatedDuration, acceptableDeviation){
			private Task alternative;
			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};
		Task alternative1_3c = new Task ("task", "alternative 1_3c description", estimatedDuration, acceptableDeviation){
			private Task alternative;
			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};
		alternative1_3d = new Task ("task", "alternative 1_3d description", estimatedDuration, acceptableDeviation);
		alternative1_3.setAlternative(alternative1_3a);
		alternative1_3a.setAlternative(alternative1_3b);
		alternative1_3b.setAlternative(alternative1_3c);
		alternative1_3c.setAlternative(alternative1_3d);

		dependency1_1.setAlternative(alternative1_1);
		dependency1_2.setAlternative(alternative1_2);
		dependency1_3.setAlternative(alternative1_3);

		Task dependency1_1_1 = new Task ("task", "dependency 1_1_1 description", estimatedDuration, acceptableDeviation){
			private Task alternative;
			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};
		Task dependency1_1_2 = new Task ("task", "dependency 1_1_2 description", estimatedDuration, acceptableDeviation){
			private Task alternative;
			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};
		dependency1_1_3 = new Task ("task", "dependency 1_1_3 description", estimatedDuration, acceptableDeviation){
			private Task alternative;
			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};

		dependency1_1.addDependency(dependency1_1_1);
		dependency1_1.addDependency(dependency1_1_2);
		dependency1_1.addDependency(dependency1_1_3);

		Task alternative1_1_1 = new Task ("task", "alternative 1_1_1 description", estimatedDuration, acceptableDeviation);
		Task alternative1_1_2 = new Task ("task", "alternative 1_1_1 description", estimatedDuration, acceptableDeviation);
		Task alternative1_1_3 = null;

		dependency1_1_1.setAlternative(alternative1_1_1);
		dependency1_1_2.setAlternative(alternative1_1_2);
		dependency1_1_3.setAlternative(alternative1_1_3);

		Task dependency1_2_1 = new Task ("task", "dependency 1_2_1 description", estimatedDuration, acceptableDeviation){
			private Task alternative;
			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};
		Task dependency1_2_2 = new Task ("task", "dependency 1_2_2 description", estimatedDuration, acceptableDeviation){
			private Task alternative;
			@Override
			public void setAlternative(Task alternative){
				this.alternative = alternative;
			}

			@Override
			public Task getAlternative(){ return alternative; }
		};

		dependency1_2.addDependency(dependency1_2_1);
		dependency1_2.addDependency(dependency1_2_2);

		alternative1_2_1 = new Task ("task", "alternative 1_2_1 description", estimatedDuration, acceptableDeviation);
		Task alternative1_2_2 = new Task ("\"alternative 1_2_2", "alternative 1_2_2 description", estimatedDuration, acceptableDeviation);

		dependency1_2_1.setAlternative(alternative1_2_1);
		dependency1_2_2.setAlternative(alternative1_2_2);
	}


	@Test
	public void testTask(){
		Task task = new Task("task", "Very interesting description.", 22, 0.15);
		Assert.assertEquals("task", task.getName());
		Assert.assertEquals("The descriptions are not equal", "Very interesting description.", task.getDescription());
		Assert.assertEquals("The estimated durations are not equal", 22, task.getEstimatedDuration());
		Assert.assertEquals("The acceptable deviations are not equal", 0.15, task.getAcceptableDeviation(), 0);
		Assert.assertEquals("There is no time span added", null, task.getTimeSpan());
		Assert.assertEquals("The status is not available", true, task.getStatus().equals("unavailable"));
		Assert.assertEquals("The state is not unavailable", TaskStateUnavailable.class, task.getState().getClass());
	}

	@Test
	public void testCanBePlanned(){
		Task canBePlannedTask = new Task("canBePlannedTask", "a descri", 123, 0.003){
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status){
					case "finished" : state = new TaskStateFinished();
						break;
					case "failed" : state = new TaskStateFailed();
						break;
					case "unavailable" : state = new TaskStateUnavailable();
						break;
					case "planned" : state = new TaskStatePlanned();
						break;
					case "executing" : state = new TaskStateExecuting();
						break;
				}
			}

			@Override
			public TaskState getState(){
				return state;
			}
		};
		canBePlannedTask.endExecution(null, null, "unavailable", null);
		Assert.assertEquals(true, canBePlannedTask.canBePlanned());
		canBePlannedTask.endExecution(null, null, "planned", null);
		Assert.assertEquals(false, canBePlannedTask.canBePlanned());
		canBePlannedTask.endExecution(null, null, "executing", null);
		Assert.assertEquals(false, canBePlannedTask.canBePlanned());
		canBePlannedTask.endExecution(null, null, "finished", null);
		Assert.assertEquals(false, canBePlannedTask.canBePlanned());
		canBePlannedTask.endExecution(null, null, "failed", null);
		Assert.assertEquals(false, canBePlannedTask.canBePlanned());
	}

	@Test
	public void testCanBeUpdated(){
		Task canBeUpdated = new Task("canBeUpdated", "a descri", 123, 0.003){
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status){
					case "finished" : state = new TaskStateFinished();
						break;
					case "failed" : state = new TaskStateFailed();
						break;
					case "unavailable" : state = new TaskStateUnavailable();
						break;
					case "planned" : state = new TaskStatePlanned();
						break;
					case "executing" : state = new TaskStateExecuting();
						break;
				}
			}

			@Override
			public TaskState getState(){
				return state;
			}
		};
		canBeUpdated.endExecution(null, null, "unavailable", null);
		Assert.assertEquals(false,canBeUpdated.canBeUpdated());
		canBeUpdated.endExecution(null, null, "planned", null);
		Assert.assertEquals(true, canBeUpdated.canBeUpdated());
		canBeUpdated.endExecution(null, null, "executing", null);
		Assert.assertEquals(false, canBeUpdated.canBeUpdated());
		canBeUpdated.endExecution(null, null, "finished", null);
		Assert.assertEquals(false, canBeUpdated.canBeUpdated());
		canBeUpdated.endExecution(null, null, "failed", null);
		Assert.assertEquals(false, canBeUpdated.canBeUpdated());
	}

	@Test
	public void testIsFinished(){
		Task isFinished = new Task("canBeUpdated", "a descri", 123, 0.003){
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status){
					case "finished" : state = new TaskStateFinished();
						break;
					case "failed" : state = new TaskStateFailed();
						break;
					case "unavailable" : state = new TaskStateUnavailable();
						break;
					case "planned" : state = new TaskStatePlanned();
						break;
					case "executing" : state = new TaskStateExecuting();
						break;
				}
			}

			@Override
			public TaskState getState(){
				return state;
			}
		};
		isFinished.endExecution(null, null, "unavailable", null);
		Assert.assertEquals(false, isFinished.isFinished());
		isFinished.endExecution(null, null, "planned", null);
		Assert.assertEquals(false, isFinished.isFinished());
		isFinished.endExecution(null, null, "executing", null);
		Assert.assertEquals(false, isFinished.isFinished());
		isFinished.endExecution(null, null, "finished", null);
		Assert.assertEquals(true, isFinished.isFinished());
		isFinished.endExecution(null, null, "failed", null);
		Assert.assertEquals(false, isFinished.isFinished());
	}




	@Test
	public void testIsAvailable1() {
		Task availableTask = new Task("available task", "descr", 23, 2);
		LocalDateTime startTime = LocalDateTime.now();
		Developer developer = new Developer("jeroen", "1234");
		resourceManager.createResourceForUser(developer, LocalTime.of(12, 0));
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		availableTask.addRequirement(resourceManager, resourceManager.getResourceType("developer"), 1);
		availableTask.initializePlan(startTime);
		Assert.assertEquals("The task is not available", true, availableTask.isAvailable(startTime));
	}

	@Test
	public void testIsAvailable2() {
		Task availableTask = new Task("available task", "descr", 23, 2);
		LocalDateTime startTime = LocalDateTime.now();
		Developer developer = new Developer("jeroen", "1234");
		resourceManager.createResourceForUser(developer, LocalTime.of(12, 0));
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		availableTask.addRequirement(resourceManager, resourceManager.getResourceType("developer"), 1);

		Task dependencyFinished = new Task("dependency finished", "descript", 23, 1.45) {
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status) {
					case "finished":
						state = new TaskStateFinished();
						break;
					case "failed":
						state = new TaskStateFailed();
						break;
					case "planned" :
						state = new TaskStatePlanned();
						break;
				}
			}

			@Override
			public TaskState getState() {
				return state;
			}
		};
		dependencyFinished.endExecution(null, null,"planned", null);
		availableTask.addDependency(dependencyFinished);
		availableTask.initializePlan(startTime);
		Assert.assertEquals("The task is not available", false, availableTask.isAvailable(startTime));


		dependencyFinished.endExecution(null, null, "finished", null);
		Assert.assertEquals("The task is not available", true, availableTask.isAvailable(startTime));
	}

	@Test
	public void testIsAvailable3() {
		Task availableTask = new Task("available task", "descr", 23, 2);
		LocalDateTime startTime = LocalDateTime.now();
		Developer developer = new Developer("jeroen", "1234");
		resourceManager.createResourceForUser(developer, LocalTime.of(12, 0));
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		availableTask.addRequirement(resourceManager, resourceManager.getResourceType("developer"), 1);

		Task dependencyFinished = new Task("dependency finished", "descript", 23, 1.45) {
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status) {
					case "finished":
						state = new TaskStateFinished();
						break;
					case "failed":
						state = new TaskStateFailed();
						break;
					case "planned":
						state = new TaskStatePlanned();
						break;
					case "unavailable":
						state = new TaskStateUnavailable();
						break;
				}
			}

			@Override
			public TaskState getState() {
				return state;
			}
		};
		dependencyFinished.endExecution(null, null, "unavailable", null);

		Task dependencyFailed = new Task("dependency failed", "description", 23, 1.45) {
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status) {
					case "finished":
						state = new TaskStateFinished();
						break;
					case "failed":
						state = new TaskStateFailed();
						break;
					case "planned":
						state = new TaskStatePlanned();
						break;
					case "unavailable":
						state = new TaskStateUnavailable();
						break;
				}
			}

			@Override
			public TaskState getState() {
				return state;
			}
		};
		dependencyFinished.endExecution(null, null, "unavailable", null);
		dependencyFailed.endExecution(null, null, "unavailable", null);
		dependencyFailed.addDependency(dependencyFinished);
		availableTask.addDependency(dependencyFinished);
		dependencyFinished.endExecution(null, null, "finished", null);
		dependencyFailed.endExecution(null, null, "failed", null);
		Assert.assertEquals("the task is available", false, availableTask.isAvailable(startTime));
	}

	@Test
    public void testIsAvailable4() {
		Task availableTask = new Task("available task", "descr", 23, 2);
		LocalDateTime startTime = LocalDateTime.now();
		Developer developer = new Developer("jeroen", "1234");
		resourceManager.createResourceForUser(developer, LocalTime.of(12, 0));
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		availableTask.addRequirement(resourceManager, resourceManager.getResourceType("developer"), 1);

		Task dependencyFinished = new Task("dependency finished", "descript", 23, 1.45) {
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status) {
					case "finished":
						state = new TaskStateFinished();
						break;
					case "failed":
						state = new TaskStateFailed();
						break;
					case "planned":
						state = new TaskStatePlanned();
						break;
					case "unavailable":
						state = new TaskStateUnavailable();
						break;
				}
			}

			@Override
			public TaskState getState() {
				return state;
			}
		};
		dependencyFinished.endExecution(null, null, "unavailable", null);

		Task dependencyFailed = new Task("dependency failed", "description", 23, 1.45) {
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status) {
					case "finished":
						state = new TaskStateFinished();
						break;
					case "failed":
						state = new TaskStateFailed();
						break;
					case "planned":
						state = new TaskStatePlanned();
						break;
					case "unavailable":
						state = new TaskStateUnavailable();
						break;
				}
			}

			@Override
			public TaskState getState() {
				return state;
			}
		};

		Task alternativeFinished = new Task("alternative finished", "descri", 25, 5.6) {
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status) {
					case "finished":
						state = new TaskStateFinished();
						break;
					case "failed":
						state = new TaskStateFailed();
						break;
				}
			}

			@Override
			public TaskState getState() {
				return state;
			}
		};
		dependencyFinished.endExecution(null, null, "unavailable", null);
		dependencyFailed.endExecution(null, null, "unavailable", null);
		dependencyFailed.addDependency(dependencyFinished);
		availableTask.addDependency(dependencyFinished);
		dependencyFinished.endExecution(null, null, "finished", null);
		dependencyFailed.endExecution(null, null, "failed", null);
		dependencyFailed.setAlternative(alternativeFinished);
		alternativeFinished.endExecution(null, null, "finished", null);
		availableTask.initializePlan(startTime);
		Assert.assertEquals("the task is available", true, availableTask.isAvailable(startTime));
	}

	@Test
    public void testIsAvailable5(){
		Task availableTask = new Task("available task", "descr", 23, 2);
		LocalDateTime startTime = LocalDateTime.now();
		Developer developer = new Developer("jeroen", "1234");
		resourceManager.createResourceForUser(developer, LocalTime.of(12, 0));
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		availableTask.addRequirement(resourceManager, resourceManager.getResourceType("developer"), 1);

		Task dependencyFinished = new Task("dependency finished", "descript", 23, 1.45) {
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status) {
					case "finished":
						state = new TaskStateFinished();
						break;
					case "failed":
						state = new TaskStateFailed();
						break;
					case "planned":
						state = new TaskStatePlanned();
						break;
					case "unavailable":
						state = new TaskStateUnavailable();
						break;
				}
			}

			@Override
			public TaskState getState() {
				return state;
			}
		};
		dependencyFinished.endExecution(null, null, "unavailable", null);

		Task dependencyFailed = new Task("dependency failed", "description", 23, 1.45) {
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status) {
					case "finished":
						state = new TaskStateFinished();
						break;
					case "failed":
						state = new TaskStateFailed();
						break;
					case "planned":
						state = new TaskStatePlanned();
						break;
					case "unavailable":
						state = new TaskStateUnavailable();
						break;
				}
			}

			@Override
			public TaskState getState() {
				return state;
			}
		};

		Task alternativeFinished = new Task("alternative finished", "descri", 25, 5.6) {
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status) {
					case "finished":
						state = new TaskStateFinished();
						break;
					case "failed":
						state = new TaskStateFailed();
						break;
				}
			}

			@Override
			public TaskState getState() {
				return state;
			}
		};


		Task unavailableTask = new Task("unavailable task", "d",789, 1.25);
		Task unavialableTask2 = new Task("unavailable task 2", "d", 4, 0.003);
		unavailableTask.addDependency(unavialableTask2);
		Assert.assertEquals("The task is available", false, unavailableTask.isAvailable(startTime));
		unavailableTask.removeDependency(unavialableTask2);
		dependencyFinished.endExecution(null, null, "unavailable", null);
		unavailableTask.addDependency(dependencyFinished);
		//dependencyFinished.removeDependency(dependencyFailed);
		dependencyFailed.endExecution(null, null, "unavailable", null);
		dependencyFailed.addDependency(unavialableTask2);
		dependencyFinished.endExecution(null, null, "finished", null);
		dependencyFailed.endExecution(null, null, "failed", null);
		Assert.assertEquals("The task is available", false, unavailableTask.isAvailable(startTime));
		dependencyFailed.endExecution(null, null, "inactive", null);
		dependencyFailed.removeDependency(unavialableTask2);
		dependencyFailed.endExecution(null, null, "failed", null);
		dependencyFailed.setAlternative(unavialableTask2);
		Assert.assertEquals("The task is available", false, unavailableTask.isAvailable(startTime));
	}

	@Test
	public void testDelay(){
		Task task = new Task("task", "Description1", 20, 0.5){
			private TaskState state;

			private TimeSpan timeSpan;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status){
					case "finished" : state = new TaskStateFinished();
						break;
					case "failed" : state = new TaskStateFailed();
						break;
				}
				TimeSpan timeSpan = new TimeSpan(startTime, endTime);
				this.timeSpan = timeSpan;
			}

			@Override
			public TaskState getState(){
				return state;
			}

			@Override
			public TimeSpan getTimeSpan(){
				return timeSpan;
			}
		};
		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime endTime = LocalDateTime.now().plus(35, ChronoUnit.MINUTES);
		task.endExecution(startTime, endTime, "finished", null);
		Assert.assertEquals("The delay is not correctly calculated", 5, task.getDelay(), 0);
	}

	@Test (expected = IllegalStateException.class)
	public void testIllegalStateDelay(){
		Task task = new Task("task", "Descr", 14, 0.2315);
		task.getDelay();
	}

	@Test
	public void testEndExecution(){
		Task updateStatusTask = new Task("task", "Very inspiring description.", 33, 1.0589);
		String str = "2018-04-08 09:30";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime startTime = LocalDateTime.parse(str, formatter);
		LocalDateTime endTime = startTime.plus(50, ChronoUnit.MINUTES);

		Developer developer = new Developer("jeroen", "1234");
		resourceManager.createResourceForUser(developer, LocalTime.of(12, 0));
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		updateStatusTask.addRequirement(resourceManager, resourceManager.getResourceType("developer"), 1);
		updateStatusTask.initializePlan(startTime);

		Assert.assertEquals("The status is not planned", true, updateStatusTask.getStatus().equals("planned"));
		updateStatusTask.makeExecuting(resourceManager, startTime, developer);
		updateStatusTask.endExecution(startTime, endTime, "finished", developer);
		Assert.assertEquals("The status is not finished", TaskStateFinished.class, updateStatusTask.getState().getClass());
		Assert.assertEquals("The start time is not correctly set", startTime, updateStatusTask.getTimeSpan().getStartTime());
		Assert.assertEquals("The end time is not correctly set", endTime, updateStatusTask.getTimeSpan().getEndTime());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testInvalidEndTimeUpdateStatus(){
		Task invalidUpdateStatusTask = new Task("task", "description 1234", 15, 0.13);
		LocalDateTime endTime = LocalDateTime.now();
		LocalDateTime startTime = LocalDateTime.now().plus(35, ChronoUnit.MINUTES);

		Developer developer = new Developer("jeroen", "1234");
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		invalidUpdateStatusTask.initializePlan(startTime);

		invalidUpdateStatusTask.makeExecuting(resourceManager, startTime, developer);
		invalidUpdateStatusTask.endExecution(startTime, endTime,  "failed", developer);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testInvalidStatusUpdateStatus(){
		Task invalidUpdateStatusTask = new Task("task", "description 1234", 15, 0.13);
		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime endTime = LocalDateTime.now().plus(456, ChronoUnit.SECONDS);

		Developer developer = new Developer("jeroen", "1234");
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		invalidUpdateStatusTask.initializePlan(startTime);

		invalidUpdateStatusTask.makeExecuting(resourceManager, startTime, developer);
		invalidUpdateStatusTask.endExecution(startTime, endTime, "inactive", developer);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testNoDeveloperAssigned(){
		Task invalidUpdateStatusTask = new Task("task", "description 1234", 15, 0.13);
		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime endTime = LocalDateTime.now().plus(456, ChronoUnit.SECONDS);

		Developer developer = new Developer("jeroen", "1234");
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		invalidUpdateStatusTask.initializePlan(startTime);
		Developer developer2 = new Developer("jeroen2", "1233");

		invalidUpdateStatusTask.makeExecuting(resourceManager, startTime, developer);
		invalidUpdateStatusTask.endExecution(startTime, endTime, "inactive", developer2);
	}

	@Test
	public void testMakeExecuting(){
		Task makeExecutingTask = new Task("task", "Very inspiring description.", 33, 1.0589);
		String str = "2018-04-08 09:30";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime startTime = LocalDateTime.parse(str, formatter);
		LocalDateTime executingStartTime = startTime.plusMinutes(4);

		Developer developer = new Developer("jeroen", "1234");
		resourceManager.createResourceForUser(developer, LocalTime.of(12, 0));
		makeExecutingTask.addRequirement(resourceManager, resourceManager.getResourceType("developer"), 1);
		makeExecutingTask.initializePlan(startTime);

		Assert.assertEquals("The status is not planned", true, makeExecutingTask.getStatus().equals("planned"));
		makeExecutingTask.makeExecuting(resourceManager, executingStartTime, developer);
		Assert.assertEquals("The status is not executing", TaskStateExecuting.class, makeExecutingTask.getState().getClass());
		Assert.assertEquals("The start time is not correctly set", executingStartTime, makeExecutingTask.getTimeSpan().getStartTime());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testMakeExecutingUnassignedDeveloper(){
		Task makeExecutingTask = new Task("task", "Very inspiring description.", 33, 1.0589);
		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime executingStartTime = startTime.plusMinutes(4);

		Developer developer = new Developer("jeroen", "1234");

		makeExecutingTask.makeExecuting(resourceManager, executingStartTime, developer);
	}

	@Test
	public void testAlternative(){
		Task setAlternative = new Task("task", "description of this task", 24, 1){
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status){
					case "finished" : state = new TaskStateFinished();
						break;
					case "failed" : state = new TaskStateFailed();
						break;
				}
			}

			@Override
			public TaskState getState(){
				return state;
			}
		};
		setAlternative.endExecution(null, null, "failed", null);

		Assert.assertEquals("There is already an alternative", null, setAlternative.getAlternative());
		Task alternative = new Task("task", "DescRiPTioNNNNN", 245, 0.015);
		setAlternative.setAlternative(alternative);
		Assert.assertEquals("The alternative is not correctly set", alternative, setAlternative.getAlternative());
	}

	@Test (expected = IllegalStateException.class)
	public void testInvaladSetAlternative(){
		Task task = new Task("task", "DescRiPTioNNNNN", 245, 0.015);
		Task alternative = new Task("task", "alternative task", 2, 0.25);
		task.setAlternative(alternative);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testInvalidSetAlternativeToItself(){
		Task setAlternative = new Task("task", "description of this task", 24, 1){
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status){
					case "finished" : state = new TaskStateFinished();
						break;
					case "failed" : state = new TaskStateFailed();
						break;
				}
			}

			@Override
			public TaskState getState(){
				return state;
			}
		};
		setAlternative.endExecution(null, null, "failed", null);
		setAlternative.setAlternative(setAlternative);
	}

	// TODO: loop nog keer testen

	@Test
	public void testAddDepedency_RemoveDepedency(){
		Task task = new Task("task", "DescRiPTioNNNNN", 245, 0.015);
		Assert.assertEquals("The depedencies list is not empty", 0, task.getDependencies().size());

		Task dependency = new Task("task", "Another interesting description.", 2, 0);
		task.addDependency(dependency);
		Assert.assertEquals("The depedencies list does not contains 1 depedency", 1, task.getDependencies().size());
		Assert.assertEquals("The depedency in the list is not the newly added depedency", dependency, task.getDependencies().get(0));

		task.removeDependency(dependency);
		Assert.assertEquals("The dependencies list is not empty", 0, task.getDependencies().size());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalAddItself(){
		Task task = new Task("task", "DescRiPTioNNNNN", 245, 0.015);
		task.addDependency(task);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalRemoveDependency(){
		Task task = new Task("task", "DescRiPTioNNNNN", 245, 0.015);
		task.removeDependency(task);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalSetAlternativeRecursive1(){
		root.endExecution(null, null, "failed", null);
		root.setAlternative(alternative1_3);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalSetAlternativeRecursive2(){
		root.endExecution(null, null, "failed", null);
		root.setAlternative(alternative1_2_1);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalSetAlternativeRecursive3(){
		root.endExecution(null, null, "failed", null);
		root.setAlternative(alternative1_3d);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalAddDependencyRecursive1(){
		root.endExecution(null, null, "unavailable", null);
		root.addDependency(dependency1_2);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalAddDependencyRecursive2(){
		root.endExecution(null, null, "unavailable", null);
		root.addDependency(dependency1_1_3);
	}

	@Test
	public void testGetAlternativeResources() {
		Task task = new Task("task", "test", 25l, 5.5);
		resourceManager.createResourceType("test1234567");
		ResourceType type = resourceManager.getResourceType("test1234567");
		LocalTime start = LocalTime.of(0, 0);
		LocalTime end = LocalTime.of(23, 59);
		AvailabilityPeriod always = new AvailabilityPeriod(start, end);
		for(int i = 1; i <= 7; i++) {
			type.addAvailability(i, always);
		}
		type.createResource("resource1");
		type.createResource("resource2");
		task.addRequirement(resourceManager, type, 1);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		task.initializePlan(startTime);
		List<Resource> list = task.getAlternativeResources(type.getResource("resource1"));
		assertTrue(list.size() == 1);
		assertEquals(type.getResource("resource2"), list.get(0));
	}

	@Test
	public void testInitializePlan() {
		Task task = new Task("task", "test", 25l, 5.5);
		resourceManager.createResourceType("test");
		ResourceType type = resourceManager.getResourceType("test");
		LocalTime start = LocalTime.of(0, 0);
		LocalTime end = LocalTime.of(23, 59);
		AvailabilityPeriod always = new AvailabilityPeriod(start, end);
		for(int i = 1; i <= 7; i++) {
			type.addAvailability(i, always);
		}
		type.createResource("resource4");
		type.createResource("resource5");
		Resource resource = type.getResource("resource4");
		Resource alternative = type.getResource("resource5");
		task.addRequirement(resourceManager, type, 2);
		LocalDateTime startTime = LocalDateTime.of(2018, Month.JULY, 26, 12, 0);
		task.initializePlan(startTime);
		List<Resource> list = task.getPlannedResources();
		assertEquals(2, list.size());
	}

	@Test (expected = IllegalStateException.class)
	public void testDelegateIllegalState(){
		Task task = new Task("task", "will be delegated soon", 69, 0.69){
			private TaskState state;

			@Override
			public void endExecution(LocalDateTime startTime, LocalDateTime endTime, String status, User user) {
				switch (status){
					case "finished" : state = new TaskStateFinished();
						break;
					case "failed" : state = new TaskStateFailed();
						break;
				}
			}

			@Override
			public TaskState getState(){
				return state;
			}
		};
		task.endExecution(null, null,"failed", null);
		task.delegate(null, null);
	}

	@Test
	public void testDelegate(){
		BranchOffice branchOffice1 = new BranchOffice("branchoffice 1");
		Task task = new Task("task12", "will be delegated soon", 69, 0.69);
		Assert.assertEquals( "There is a project in branch office 1.",0 ,branchOffice1.getProjectManager().getProjects().size());
		LocalDateTime time = LocalDateTime.now();
		task.delegate(branchOffice1, time);
		Assert.assertEquals( "There is not 1 project in branch office 1.",1 ,branchOffice1.getProjectManager().getProjects().size());
		Project delegatedProject = branchOffice1.getProjectManager().getProjects().get(0);
		Assert.assertEquals( "The project its creation time is not correct.", time, delegatedProject.getCreationTime());
		Assert.assertEquals("There is not 1 task in the project", 1, delegatedProject.getTasks().size());
		DelegatedTask delegatedTask = (DelegatedTask) branchOffice1.getProjectManager().getProjects().get(0).getTask("delegated_task12");;
		Assert.assertEquals("The task name is not correct.", "delegated_" + task.getName(), delegatedTask.getName());
		Assert.assertEquals("The description is not correct.", task.getDescription(), delegatedTask.getDescription());
		Assert.assertEquals("The estimated duration is not correct.",task.getEstimatedDuration(), delegatedTask.getEstimatedDuration());
		Assert.assertEquals("The acceptable deviation is not correct.",task.getAcceptableDeviation(), delegatedTask.getAcceptableDeviation(), 0);
		Assert.assertEquals("The status is not delegated unavailable.", "delegated_unavailable" ,task.getStatus());
		Assert.assertEquals("The description is not unavailable.", "unavailable", delegatedTask.getStatus());
		Assert.assertEquals("The delegated task is not referenced.", delegatedTask, ((TaskStateDelegated) task.getState()).getDelegatedTask());
	}

	@Test (expected = IllegalStateException.class)
	public void testDelegatedTaskIllegalAddDependency() {
		BranchOffice branchOffice1 = new BranchOffice("branchoffice 1");
		Task task = new Task("task12", "will be delegated soon", 69, 0.69);
		task.delegate(branchOffice1, LocalDateTime.now());
		DelegatedTask delegatedTask = (DelegatedTask) branchOffice1.getProjectManager().getProjects().get(0).getTask("delegated_task12");
		Task dependency = new Task("dependency", "blabla", 123, 0.1);
		delegatedTask.addDependency(dependency);
	}

	@Test (expected = IllegalStateException.class)
	public void testDelegatedTaskIllegalRemoveDependency() {
		BranchOffice branchOffice1 = new BranchOffice("branchoffice 1");
		Task task = new Task("task12", "will be delegated soon", 69, 0.69);
		task.delegate(branchOffice1, LocalDateTime.now());
		DelegatedTask delegatedTask = (DelegatedTask) branchOffice1.getProjectManager().getProjects().get(0).getTask("delegated_task12");
		Task dependency = new Task("dependency", "blabla", 123, 0.1);
		delegatedTask.removeDependency(dependency);
	}

	@Test (expected = IllegalStateException.class)
	public void testDelegatedTaskIllegalSetAlternative() {
		BranchOffice branchOffice1 = new BranchOffice("branchoffice 1");
		Task task = new Task("task12", "will be delegated soon", 69, 0.69);
		task.delegate(branchOffice1, LocalDateTime.now());
		DelegatedTask delegatedTask = (DelegatedTask) branchOffice1.getProjectManager().getProjects().get(0).getTask("delegated_task12");
		Task alternative = new Task("alternative", "blabla", 123, 0.1);
		delegatedTask.setAlternative(alternative);
	}

}
