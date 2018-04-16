package test.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import taskman.backend.*;
import taskman.backend.importexport.ImportExportException;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.time.Clock;
import taskman.backend.time.TimeParser;
import taskman.backend.user.UserManager;

import java.io.File;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class ControllerTest {

    
    // TODO: zorgen dat de tests compileren!

    private static Controller controller;
    private Clock clock;
    private ProjectOrganizer projectOrganizer;
    private UserManager userManager;
    private ResourceManager resourceManager;
    private LocalTime startBreak;

    @Before
    public void runBeforeMethod() {
        userManager = new UserManager();
        userManager.createUser("test", "test", "projectmanager", startBreak, resourceManager);
        userManager.login("test", "test");
        projectOrganizer = new ProjectOrganizer();
        clock = new Clock();
        resourceManager = new ResourceManager();
        controller = new Controller(clock, userManager, projectOrganizer, resourceManager);
        startBreak = LocalTime.of(12, 0);
    }

    @After
    public void runAfterMethod() {
        controller = null;
    }

    @Test
    public void constructor() {
    	userManager.createUser("test", "myPassword", "developer", startBreak, resourceManager);
    	userManager.login("test", "myPassword");
        assertEquals("Constructor does not initialize system time!", TimeParser.convertLocalDateTimeToString(clock.getTime()), controller.getTime());
        assertEquals("Constructor does not initialize user type!", "test", controller.getCurrentUser().getName());
    }

    @Test
    public void project_and_task() {
        // project and task are only tested to make sure that the controller works correctly.
        // More extended tests are located in separate test classes.
        userManager.createUser("pm", "test", "projectmanager", null, resourceManager);
        userManager.createUser("d", "test", "developer", startBreak, resourceManager);
        userManager.login("pm", "test");

        String projectName = "test name";

        assertEquals("Initial project list should be empty!", 0, projectOrganizer.getProjects().size());
        String due = "06/07/2008 09:10";
        controller.createProject(projectName, "test description", due);
        assertEquals("project list should contain one project!", 1, projectOrganizer.getProjects().size());
        assertEquals("The project name is incorrect!", projectName, controller.getProjects().get(0).getName());

        assertEquals("project already has tasks!", 0, controller.getProjects().get(0).getTasks().size());
        String taskName = "task name";
        controller.createTask(projectName, taskName, "test task description", 709l, 1.345);
        assertEquals("The task is not added!", taskName, controller.getProjects().get(0).getTasks().get(0).getName());
        int taskId = 0;
        assertNotNull("task details cannot be null!", controller.getTaskDetails(projectName, taskId));

        controller.createTask(projectName, "test task description", Long.toString(888l), Double.toString(1.45));
        int dependencyTaskId = 1;
        controller.addDependencyToTask(projectName, taskId, dependencyTaskId);

        userManager.login("d", "test");
        String startTime = "01/01/2003 05:05";
        String endTime = "01/01/2100 00:00";
        controller.updateTaskStatus(projectName, taskId, startTime, endTime, "FAILED");
        controller.createTask(projectName, "test", Long.toString(200L), Double.toString(1.1));
        Integer alternativeTaskId = 2;
        controller.addAlternativeToTask(projectName, taskId, alternativeTaskId);
    }

    @Test
    public void system_time() {
        LocalDateTime time = controller.getTime();
        LocalDateTime newTime = LocalDateTime.of(2018, 3,9, 17, 13);
        assertNotEquals("Initial time is equal to example time!", newTime, time);
        controller.updateTime(newTime);
        assertEquals("time is not updated!", newTime, controller.getTime());
        try {
            controller.updateTime(time);
            fail("time should not be updated to the past!");
        }
        catch (Exception e) {
            assertEquals("Wrong exception when updating time to past! (" + e.getMessage() + ")", IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void user() {
        assertNotNull("user cannot be null!", controller.getCurrentUserName());
        assertNotEquals("user cannot be ''!", "", controller.getCurrentUserName());
        controller.createUser("testUser", "testPassword", "developer", startBreak);
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
