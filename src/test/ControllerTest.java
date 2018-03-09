package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.Controller;
import taskman.User;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;

public class ControllerTest {


    private static Controller controller;

    @Before
    public void runBeforeMethod() {
        controller = new Controller();
    }

    @After
    public void runAfterMethod() {
        controller = null;
    }

    @Test
    public void constructor() {
        controller = new Controller("01/02/0003 04:05");
        Assert.assertEquals("Constructor does not initialize system time!", "01/02/0003 04:05", controller.getSystemTime());
        controller = new Controller("01/02/0003 04:05", "DEVELOPER");
        Assert.assertEquals("Constructor does not initialize system time!", "01/02/0003 04:05", controller.getSystemTime());
        Assert.assertEquals("Constructor does not initialize user type!", "DEVELOPER", controller.getUserType());
    }

    @Test
    public void project_and_task() throws AccessDeniedException {
        // Project and task are only tested to make sure that the controller works correctly.
        // More extended tests are located in separate test classes.

        String projectName = "test name";

        Assert.assertFalse("A project already exists when creating the controller!", controller.projectExists("test name"));
        Assert.assertEquals("Initial project list should be empty!", 0, controller.getProjectNames().size());
        HashMap<String,String> projectForm = controller.getProjectCreationForm();
        projectForm.put("name", projectName);
        projectForm.put("description", "test description");
        projectForm.put("creationTime", "01/02/2003 04:05");
        projectForm.put("dueTime", "06/07/2008 09:10");
        controller.addProject(projectForm);
        Assert.assertTrue("The added project does not exist!", controller.projectExists(projectName));
        Assert.assertEquals("Project list should contain one project!", 1, controller.getProjectNames().size());
        Assert.assertEquals("The project name is incorrect!", projectName, controller.getProjectNames().get(0));
        Assert.assertNotNull("Project details cannot be null!", controller.getProjectDetails(projectName));

        Assert.assertEquals("Project already has tasks!", 0, controller.getTasksOfProject(projectName).size());
        HashMap<String,String> taskForm = controller.getTaskCreationForm();
        taskForm.put("description", "test task description");
        taskForm.put("estimatedDuration", "7d 1h 30m 25s");
        taskForm.put("acceptableDeviation", "1d 0h 0m 0s");
        controller.addTask(projectName, taskForm);
        Assert.assertEquals("The task is not added!", 1, controller.getTasksOfProject(projectName).size());
        Integer taskId = controller.getLastTaskID();
        Assert.assertNotNull("Task details cannot be null!", controller.getTaskDetails(projectName, taskId));

        controller.addTask(projectName, taskForm);
        Integer alternativeTaskId = controller.getLastTaskID();
        Assert.assertNotEquals("The last task id is not updated!", taskId, alternativeTaskId);
        controller.addAlternativeToTask(projectName, taskId, alternativeTaskId);

        controller.addTask(projectName, taskForm);
        Integer dependencyTaskId = controller.getLastTaskID();
        controller.addDependencyToTask(projectName, taskId, dependencyTaskId);

        try {
            User.setUserType("REGULARUSER");
            controller.updateTaskStatus(projectName, taskId, "01/02/2003 05:05", "01/01/2100 00:00", "FINISHED");
            Assert.fail("A regular user should not be able to update a task status!");
        } catch (AccessDeniedException e) {
            assert true;
        }
        User.setUserType("DEVELOPER");
        controller.updateTaskStatus(projectName, taskId, "01/02/2003 05:05", "01/01/2100 00:00", "FINISHED");

    }

    @Test
    public void system_time() {
        String time = controller.getSystemTime();
        String newTime = "09/03/2018 17:13";
        Assert.assertNotEquals("Initial time is equal to example time!", newTime, time);
        controller.updateSystemTime(newTime);
        Assert.assertEquals("Time is not updated!", newTime, controller.getSystemTime());
        try {
            controller.updateSystemTime(time);
            Assert.fail("Time should not be updated to the past!");
        }
        catch (Exception e) {
            Assert.assertEquals("Wrong exception when updating time to past! (" + e.getMessage() + ")", IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void user() {
        Assert.assertNotNull("User cannot be null!", controller.getUserType());
        Assert.assertNotEquals("User cannot be ''!", "", controller.getUserType());
        controller.setUserType("REGULARUSER");
        Assert.assertEquals("The user type isn't correct!", "REGULARUSER", controller.getUserType());
        controller.setUserType("DEVELOPER");
        Assert.assertEquals("The user type isn't correct!", "DEVELOPER", controller.getUserType());
    }

    @Test
    public void importXML() {
        Assert.fail("Not implemented!"); // TODO
    }

    @Test
    public void exportToXML() {
        Assert.fail("Not implemented!"); // TODO
    }
}