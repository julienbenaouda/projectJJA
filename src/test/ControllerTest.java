package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import taskman.Backend.*;

import java.io.File;
import java.nio.file.AccessDeniedException;
import static org.junit.Assert.*;

public class ControllerTest {

    
    // TODO: zorgen dat de tests compileren!

    private static Controller controller;
    private Clock clock;
    private ProjectOrganizer projectOrganizer;
    private UserManager userManager;

    @Before
    public void runBeforeMethod() {
        userManager = new UserManager();
        userManager.createUser("test", "test", "projectmanager");
        userManager.login("test", "test");
        projectOrganizer = new ProjectOrganizer();
        clock = new Clock();
        controller = new Controller(clock, userManager, projectOrganizer);
    }

    @After
    public void runAfterMethod() {
        controller = null;
    }

    @Test
    public void constructor() {
    	userManager.createUser("test", "myPassword", "developer");
    	userManager.login("test", "myPassword");
        assertEquals("Constructor does not initialize system time!", TimeParser.convertLocalDateTimeToString(clock.getTime()), controller.getSystemTime());
        assertEquals("Constructor does not initialize user type!", "test", controller.getCurrentUserName());
    }

    @Test
    public void project_and_task() {
        // Project and task are only tested to make sure that the controller works correctly.
        // More extended tests are located in separate test classes.
        userManager.createUser("pm", "test", "projectmanager");
        userManager.createUser("d", "test", "developer");
        userManager.login("pm", "test");

        String projectName = "test name";

        assertEquals("Initial project list should be empty!", 0, projectOrganizer.getProjectNames().size());
        String due = "06/07/2008 09:10";
        controller.createProject(projectName, "test description", due);
        assertEquals("Project list should contain one project!", 1, projectOrganizer.getProjectNames().size());
        assertTrue("The project name is incorrect!", controller.getProjectNames().contains(projectName));
        assertNotNull("Project details cannot be null!", controller.getProjectDetails(projectName));

        assertEquals("Project already has tasks!", (Integer) 0, controller.getNumberOfTasks(projectName));
        controller.createTask(projectName, "test task description", 709l, 1.345);
        assertEquals("The task is not added!", (Integer) 1, controller.getNumberOfTasks(projectName));
        int taskId = 0;
        assertNotNull("Task details cannot be null!", controller.getTaskDetails(projectName, taskId));

        controller.createTask(projectName, "test task description", 888l, 1.45);
        int dependencyTaskId = 1;
        controller.addDependencyToTask(projectName, taskId, dependencyTaskId);

        userManager.login("d", "test");
        String startTime = "01/01/2003 05:05";
        String endTime = "01/01/2100 00:00";
        controller.updateTaskStatus(projectName, taskId, startTime, endTime, "FAILED");
        controller.createTask(projectName, "test", 200L, 1.1);
        Integer alternativeTaskId = 2;
        controller.addAlternativeToTask(projectName, taskId, alternativeTaskId);
    }

    @Test
    public void system_time() {
        String time = controller.getSystemTime();
        String newTime = "09/03/2018 17:13";
        assertNotEquals("Initial time is equal to example time!", newTime, time);
        controller.updateSystemTime(newTime);
        assertEquals("Time is not updated!", newTime, controller.getSystemTime());
        try {
            controller.updateSystemTime(time);
            fail("Time should not be updated to the past!");
        }
        catch (Exception e) {
            assertEquals("Wrong exception when updating time to past! (" + e.getMessage() + ")", IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void user() {
        assertNotNull("User cannot be null!", controller.getCurrentUserName());
        assertNotEquals("User cannot be ''!", "", controller.getCurrentUserName());
        controller.createUser("testUser", "testPassword", "developer");
        controller.login("testUser", "testPassword");
        assertEquals("The user name isn't correct!", "testUser", controller.getCurrentUserName());
    }

    private void deleteFile(String path) throws AccessDeniedException {
        File file = new File(path);
        if (file.exists()) {
            if (!file.delete()) {
                throw new AccessDeniedException("Cannot write file to '" + path + "'!");
            }
        }
    }

    @Test
    public void import_export() throws ImportExportException, AccessDeniedException {
        String path = System.getProperty("user.dir") + File.separator + "test.xml";
        deleteFile(path);
        controller.exportSystem(path);
        System.out.println("A file is temporally saved to '" + path + "'");
        assertTrue("XML file cannot be saved!", new File(path).exists());
        controller = null;
        controller = Controller.importSystem(path);
        deleteFile(path);
        System.out.println("A file is deleted from '" + path + "'");
        assertNotNull("Controller cannot be restored!", controller);
    }
    

}