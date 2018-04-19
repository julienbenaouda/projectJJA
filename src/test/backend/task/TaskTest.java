package test.backend.task;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import taskman.backend.resource.Resource;
import taskman.backend.resource.ResourceManager;
import taskman.backend.task.*;
import taskman.backend.time.TimeSpan;
import taskman.backend.user.Developer;
import taskman.backend.user.ProjectManager;
import taskman.backend.user.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a test class for the task class.
 * @author Jeroen Van Der Donckt
 *
*/

public class TaskTest {

	private static Task root;
	private static Task alternative1_3;
	private static Task alternative1_2_1;
	private static Task dependency1_2;
	private static Task dependency1_1_3;
	private static Task alternative1_3d;
	private static ResourceManager resourceManager;
	private static ProjectManager admin;


	@BeforeClass
	public static void setUp(){
		resourceManager = new ResourceManager();
		admin = new ProjectManager("admin", "admin");

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
	}


	// TODO: hiervoor uitgebreide test schrijven
	/*
	@Test
	public void testIsAvailable(){
		Task availableTask = new Task("available task", 23, 2);
		Assert.assertEquals("The task is not available", true, availableTask.getStatus().equals("available"));
		Task dependencyFinished = new Task("dependency finished", 23, 1.45){
			private TaskState state;

			@Override
			public void endTaskExecution(LocalDateTime startTime, LocalDateTime endTime, String status) {
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
		availableTask.addDependency(dependencyFinished);

		dependencyFinished.endTaskExecution(null, null, "finished");
		Assert.assertEquals("The task is not available", true, availableTask.getStatus().equals("available"));
		Task dependencyFailed =  new Task("dependency failed", 23, 1.45){
			private TaskState state;

			@Override
			public void endTaskExecution(LocalDateTime startTime, LocalDateTime endTime, String status) {
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
		dependencyFinished.endTaskExecution(null, null, "inactive");
		dependencyFinished.addDependency(dependencyFailed);
		dependencyFinished.endTaskExecution(null, null , "finished");
		Task alternativeFinished = new Task("alternative finished", 25, 5.6){
			private TaskState state;

			@Override
			public void endTaskExecution(LocalDateTime startTime, LocalDateTime endTime, String status) {
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
		dependencyFailed.endTaskExecution(null, null, "failed");
		dependencyFailed.setAlternative(alternativeFinished);
		alternativeFinished.endTaskExecution(null, null, "finished");
		Assert.assertEquals("The task is not available", true, availableTask.getStatus().equals("available"));

		Task unavailableTask = new Task("unavailable task", 789, 1.25);
		Task unavialableTask2 = new Task("unavailable task 2", 4, 0.003);
		unavailableTask.addDependency(unavialableTask2);
		Assert.assertEquals("The task is available", false, unavailableTask.getStatus().equals("available"));
		unavailableTask.removeDependency(unavialableTask2);
		dependencyFinished.endTaskExecution(null, null, "inactive");
		unavailableTask.addDependency(dependencyFinished);
		//dependencyFinished.removeDependency(dependencyFailed);
		dependencyFailed.endTaskExecution(null, null, "inactive");
		dependencyFailed.addDependency(unavialableTask2);
		dependencyFinished.endTaskExecution(null, null, "finished");
		dependencyFailed.endTaskExecution(null, null, "failed");
		Assert.assertEquals("The task is available", false, unavailableTask.getStatus().equals("available"));
		dependencyFailed.endTaskExecution(null, null, "inactive");
		dependencyFailed.removeDependency(unavialableTask2);
		dependencyFailed.endTaskExecution(null, null, "failed");
		dependencyFailed.setAlternative(unavialableTask2);
		Assert.assertEquals("The task is available", false, unavailableTask.getStatus().equals("available"));

	}

	@Test (expected = IllegalStateException.class)
	public void testIllegalStateIsAvailable(){
		Task failedTask = new Task("failed task", 1, 0.03){
			private TaskState state;

			@Override
			public void endTaskExecution(LocalDateTime startTime, LocalDateTime endTime, String status) {
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
		failedTask.endTaskExecution(null, null, "failed");
		failedTask.getStatus().equals("available");
	}
	*/

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
	public void testIlegalStateDelay(){
		Task task = new Task("task", "Descr", 14, 0.2315);
		task.getDelay();
	}

	@Test
	public void testUpdateStatus(){
		Task updateStatusTask = new Task("task", "Very inspiring description.", 33, 1.0589);
		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime endTime = LocalDateTime.now().plus(456, ChronoUnit.SECONDS);

		Developer developer = new Developer("jeroen", "1234");
		resourceManager.createResourceForUser(developer, LocalTime.of(12, 0));
		List<Resource> resourceList = new ArrayList<>();
		resourceList.add(resourceManager.getResourceType("developer").getResource(developer.getName()));
		updateStatusTask.plan(resourceManager, admin, resourceList, startTime);
		updateStatusTask.makeAvailable();

		Assert.assertEquals("The status is not available", true, updateStatusTask.getStatus().equals("available"));
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
		invalidUpdateStatusTask.plan(resourceManager, admin, resourceList, startTime);

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
		invalidUpdateStatusTask.plan(resourceManager, admin, resourceList, startTime);

		invalidUpdateStatusTask.endExecution(startTime, endTime, "inactive", developer);
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

}
