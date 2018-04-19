package test.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.importexport.ImportExportException;
import taskman.backend.project.Project;
import taskman.backend.project.ProjectOrganizer;
import taskman.backend.resource.ResourceManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.task.Task;
import taskman.backend.time.Clock;
import taskman.backend.user.OperationNotPermittedException;
import taskman.backend.user.User;
import taskman.backend.user.UserManager;

import java.io.File;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class ControllerTest {

    private static Controller controller;
    private Clock clock;
    private ProjectOrganizer projectOrganizer;
    private UserManager userManager;
    private ResourceManager resourceManager;
    private LocalTime startBreak;
    private LocalDateTime randomTime = LocalDateTime.of(1298, 12, 30, 9, 2);

    @Before
    public void runBeforeMethod() {
        clock = new Clock();
        userManager = new UserManager();
        projectOrganizer = new ProjectOrganizer();
        resourceManager = new ResourceManager();
        controller = new Controller(clock, userManager, projectOrganizer, resourceManager, new SimulationManager());
        startBreak = LocalTime.of(12, 0);
    }

    @After
    public void runAfterMethod() {
        clock = null;
        userManager = null;
        projectOrganizer = null;
        resourceManager = null;
        controller = null;
        startBreak = null;
    }

    private void addDeveloper() {
        userManager.createUser("dev", "devpass", "developer", startBreak, resourceManager);
        userManager.login("dev", "devpass");
    }

    private void addProjectManager() {
        userManager.createUser("pm", "pmpass", "project manager", null, resourceManager);
        userManager.login("pm", "pmpass");
    }

    @Test
    public void constructor() {
        addProjectManager();
        assertEquals("Constructor does not initialize clock!", clock.getTime(), controller.getTime());
        assertEquals("Constructor does not initialize user manager!", userManager.getCurrentUser(), controller.getCurrentUser());
        assertEquals("Constructor does not initialize project organizer!", projectOrganizer.getProjects(), controller.getProjects());
        assertEquals("Constructor does not initialize resource manager!", resourceManager.getResourceTypes(), controller.getResourceTypes());
    }

    @Test
    public void getTime() {
        assertEquals("Wrong time!", clock.getTime(), controller.getTime());
    }

    @Test
    public void updateTime() {
        LocalDateTime newTime = LocalDateTime.of(1298, 12, 30, 9, 2);
        controller.updateTime(newTime);
        assertEquals("Wrong time!", newTime, controller.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTime_IllegalArgumentException() {
        controller.updateTime(randomTime);
        controller.updateTime(randomTime.minusMinutes(1));
        fail("No IllegalArgumentException!");
    }

    @Test
    public void getCurrentUser_Developer() {
        addDeveloper();
        assertEquals("Wrong user!", userManager.getUser("dev"), controller.getCurrentUser());
    }

    @Test
    public void getCurrentUser_ProjectManager() {
        addProjectManager();
        assertEquals("Wrong user!", userManager.getUser("pm"), controller.getCurrentUser());

    }

    @Test(expected = OperationNotPermittedException.class)
    public void getCurrentUser_OperationNotPermittedException() {
        controller.getCurrentUser();
    }

    @Test
    public void getUsers() {
        assertEquals("Incorrect users given!", userManager.getUsers(), controller.getUsers());
        addDeveloper();
        assertEquals("Incorrect users given!", userManager.getUsers(), controller.getUsers());
        addProjectManager();
        assertEquals("Incorrect users given!", userManager.getUsers(), controller.getUsers());
    }

    @Test
    public void getUserTypes() {
        assertEquals("Incorrect user types given!", userManager.getUserTypes(), controller.getUserTypes());
    }

    @Test
    public void createUser_login_logout_removeUser() {
        assertFalse("User already created!", userManager.hasUser("alexander"));
        controller.createUser("alexander", "blabla", "project manager", null);
        assertTrue("User not created!", userManager.hasUser("alexander"));
        User alexander = userManager.getUser("alexander");
        assertFalse("User already created!", userManager.hasUser("julien"));
        controller.createUser("julien", "blablabla", "developer", LocalTime.of(12, 0));
        assertTrue("User not created!", userManager.hasUser("julien"));
        User julien = userManager.getUser("julien");

        assertFalse("Already logged in!", userManager.hasCurrentUser());
        controller.login("alexander", "blabla");
        assertTrue("Login failed!", userManager.hasCurrentUser());
        controller.logout();
        assertFalse("Logout failed!", userManager.hasCurrentUser());

        controller.login("julien", "blablabla");
        assertTrue("Login failed!", userManager.hasCurrentUser());
        controller.logout();
        assertFalse("Logout failed!", userManager.hasCurrentUser());

        assertTrue("User already removed!", userManager.hasUser("alexander"));
        controller.removeUser(alexander, "blabla");
        assertFalse("User not removed!", userManager.hasUser("alexander"));
        assertTrue("User already removed!", userManager.hasUser("julien"));
        controller.removeUser(julien, "blablabla");
        assertFalse("User not removed!", userManager.hasUser("julien"));
    }

    @Test
    public void project_and_task() {
        addProjectManager();
        assertTrue("Projects already present!", projectOrganizer.getProjects().isEmpty());
        assertTrue("Projects already present!", controller.getProjects().isEmpty());
        controller.createProject("proj", "xXx", randomTime);
        assertEquals("More or less than one project added!",1, projectOrganizer.getProjects().size());
        assertEquals("More or less than one project added!",1, controller.getProjects().size());
        Project project = projectOrganizer.getProject("proj");
        assertEquals("Wrong project added!", project, controller.getProjects().get(0));

        assertEquals("Wrong project status!", "active", controller.getProjectStatus(project));

        assertTrue("Tasks already present!", project.getTasks().isEmpty());
        assertTrue("Tasks already present!", controller.getTasks(project).isEmpty());
        controller.createTask(project, "tsk", "oOo", 10, 0.5);
        assertEquals("More or less than one task added!",1, project.getTasks().size());
        assertEquals("More or less than one task added!",1, controller.getTasks(project).size());
        Task task = project.getTask("tsk");
        assertEquals("Wrong task added!", task, controller.getTasks(project).get(0));

        assertEquals("Wrong project status!", "active", controller.getProjectStatus(project));

        controller.logout();
        userManager.createUser("dev", "devpass", "developer", startBreak, resourceManager);
        controller.login("dev", "devpass");

        assertTrue("Developer can see unassigned projects!",controller.getProjects().isEmpty());
        assertTrue("Developer can see unassigned tasks!", controller.getTasks(project).isEmpty());

        controller.logout();
        userManager.login("pm", "pmpass");

        // TODO
    }

    @Test
    public void import_export() throws ImportExportException, AccessDeniedException {
        String path = System.getProperty("user.dir") + File.separator + "test.xml";
        deleteFile(path);
        controller.exportSystem(path);
        System.out.println("A file is temporally saved to '" + path + "'");
        assertTrue("System cannot be saved!", new File(path).exists());
        runAfterMethod();
        runBeforeMethod();
        controller.importSystem(path);
        deleteFile(path);
        System.out.println("A file is deleted from '" + path + "'");
        assertNotNull("Controller cannot be restored!", controller);
    }

    private void deleteFile(String path) throws AccessDeniedException {
        File file = new File(path);
        if (file.exists()) {
            if (!file.delete()) {
                throw new AccessDeniedException("Cannot write file to '" + path + "'!");
            }
        }
    }

}
