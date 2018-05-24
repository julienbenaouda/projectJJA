package test.backend;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.backend.Controller;
import taskman.backend.branchOffice.BranchOffice;
import taskman.backend.branchOffice.BranchOfficeManager;
import taskman.backend.importexport.ImportExportException;
import taskman.backend.project.ProjectManager;
import taskman.backend.resource.ResourceManager;
import taskman.backend.simulation.SimulationManager;
import taskman.backend.time.Clock;
import taskman.backend.user.OperationNotPermittedException;
import taskman.backend.user.User;
import taskman.backend.user.UserManager;
import taskman.backend.wrappers.BranchOfficeWrapper;
import taskman.backend.wrappers.ProjectWrapper;
import taskman.backend.wrappers.ResourceTypeWrapper;
import taskman.backend.wrappers.TaskWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class ControllerTest {

    private static Controller controller;
    private static Clock clock;
    private static BranchOfficeManager branchOfficeManager;
    private static BranchOffice branchOffice;
    private static SimulationManager simulationManager;
    private static UserManager userManager;
    private static ProjectManager projectOrganizer;
    private static ResourceManager resourceManager;
    private static LocalTime startBreak;
    private static LocalDateTime randomTime;

    @Before
    public void runBeforeMethod() {
        clock = new Clock();
        branchOfficeManager = new BranchOfficeManager();
        branchOfficeManager.createBranchOffice("test");
        branchOffice = branchOfficeManager.getBranchOffices().get(0);
        userManager = branchOffice.getUserManager();
        projectOrganizer = branchOffice.getProjectManager();
        resourceManager = branchOffice.getResourceManager();
        controller = new Controller(clock, branchOfficeManager, new SimulationManager());
        startBreak = LocalTime.of(12, 0);
        randomTime = LocalDateTime.of(1298, 12, 30, 9, 2);
    }

    @After
    public void runAfterMethod() {
        clock = null;
        branchOfficeManager = null;
        branchOffice = null;
        userManager = null;
        projectOrganizer = null;
        resourceManager = null;
        controller = null;
        startBreak = null;
        randomTime = null;
    }

    @Test
    public void constructorTest() {
        controller.createUser(branchOffice, "alexander", "blabla", "project manager", null);
        controller.login(branchOffice,"alexander", "blabla");
        assertEquals("Constructor does not initialize clock!", clock.getTime(), controller.getTime());
        assertEquals("Constructor does not initialize user userManager!", userManager.getCurrentUser(), controller.getCurrentUser());
        assertEquals("Constructor does not initialize project organizer!", projectOrganizer.getProjects(), controller.getProjects());
        assertEquals("Constructor does not initialize resource userManager!", resourceManager.getResourceTypes(), controller.getResourceTypes());
    }

    @Test
    public void getTimeTest() {
        assertEquals("Wrong time!", clock.getTime(), controller.getTime());
    }

    @Test
    public void updateTimeTest() {
        LocalDateTime newTime = LocalDateTime.of(1298, 12, 30, 9, 2);
        controller.updateTime(newTime);
        assertEquals("Wrong time!", newTime, controller.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTimeTest_IllegalArgumentException() {
        controller.updateTime(randomTime);
        controller.updateTime(randomTime.minusMinutes(1));
        fail("No IllegalArgumentException!");
    }

    @Test
    public void getBranchOfficesTest() {
        List<BranchOffice> a = branchOfficeManager.getBranchOffices();
        List<BranchOfficeWrapper> b = controller.getBranchOffices();
        Assert.assertEquals("Wrong number of branch offices!", a.size(), b.size());
        for (int i = 0; i < a.size(); i++) {
            Assert.assertEquals("Wrong branch office!", a.get(i), b.get(i));
        }
    }

    @Test
    public void createBranchOfficeTest() {
        controller.createBranchOffice("hallo");
        List<BranchOfficeWrapper> branchOffices = controller.getBranchOffices();
        BranchOfficeWrapper b = branchOffices.get(branchOffices.size() - 1);
        Assert.assertEquals("Branch office was not created!", "hallo", b.getName());
    }

    @Test
    public void getCurrentUserTest_Developer() {
        controller.createUser(branchOffice, "dev", "devpass", "developer", startBreak);
        controller.login(branchOffice, "dev", "devpass");
        assertEquals("Wrong user!", userManager.getUser("dev"), controller.getCurrentUser());
    }

    @Test
    public void getCurrentUserTest_ProjectManager() {
        controller.createUser(branchOffice, "pm", "pmpass", "project manager", null);
        controller.login(branchOffice,"pm", "pmpass");
        assertEquals("Wrong user!", userManager.getUser("pm"), controller.getCurrentUser());

    }

    @Test(expected = IllegalStateException.class)
    public void getCurrentUserTest_IllegalStateException() {
        controller.getCurrentUser();
    }

    @Test
    public void getUsersTest() {
        assertEquals("Incorrect users given!", userManager.getUsers(), controller.getUsers(branchOffice));
        controller.createUser(branchOffice, "dev", "devpass", "developer", startBreak);
        controller.login(branchOffice, "dev", "devpass");
        assertEquals("Incorrect users given!", userManager.getUsers(), controller.getUsers(branchOffice));
        controller.createUser(branchOffice, "pm", "pmpass", "project manager", null);
        controller.login(branchOffice,"pm", "pmpass");
        assertEquals("Incorrect users given!", userManager.getUsers(), controller.getUsers(branchOffice));
    }

    @Test
    public void getUserTypesTest() {
        assertEquals("Incorrect user types given!", userManager.getUserTypes(), controller.getUserTypes(branchOffice));
    }

    @Test
    public void createUserTest_Developer() {
        assertFalse("User already created!", userManager.hasUser("julien"));
        controller.createUser(branchOffice,"julien", "blablabla", "developer", LocalTime.of(12, 0));
        assertTrue("User not created!", userManager.hasUser("julien"));
    }

    @Test
    public void createUserTest_ProjectManager() {
        assertFalse("User already created!", userManager.hasUser("julien"));
        controller.createUser(branchOffice,"julien", "blablabla", "developer", LocalTime.of(12, 0));
        assertTrue("User not created!", userManager.hasUser("julien"));
    }

    @Test
    public void removeUserTest_Developer() {
        controller.createUser(branchOffice, "julien", "blablabla", "developer", LocalTime.of(12, 0));
        User julien = userManager.getUser("julien");
        assertTrue("User already removed!", userManager.hasUser("julien"));
        controller.removeUser(branchOffice, julien, "blablabla");
        assertFalse("User not removed!", userManager.hasUser("julien"));
    }

    @Test
    public void removeUserTest_ProjectManager() {
        controller.createUser(branchOffice, "alexander", "blabla", "project manager", null);
        User alexander = userManager.getUser("alexander");
        assertTrue("User already removed!", userManager.hasUser("alexander"));
        controller.removeUser(branchOffice, alexander, "blabla");
        assertFalse("User not removed!", userManager.hasUser("alexander"));
    }

    @Test
    public void login_logout_Developer() {
        controller.createUser(branchOffice,"julien", "blablabla", "developer", LocalTime.of(12, 0));
        controller.login(branchOffice,"julien", "blablabla");
        assertTrue("Login failed!", userManager.hasCurrentUser());
        controller.logout();
        assertFalse("Logout failed!", userManager.hasCurrentUser());
    }

    @Test
    public void loginLogoutTest_ProjectManager() {
        controller.createUser(branchOffice,"alexander", "blabla", "project manager", null);
        assertFalse("Already logged in!", userManager.hasCurrentUser());
        controller.login(branchOffice,"alexander", "blabla");
        assertTrue("Login failed!", userManager.hasCurrentUser());
        controller.logout();
        assertFalse("Logout failed!", userManager.hasCurrentUser());
    }

    @Test(expected = OperationNotPermittedException.class)
    public void createProjectTest_Developer_OperationNotPermittedException() {
        controller.createUser(branchOffice,"julien", "blablabla", "developer", LocalTime.of(12, 0));
        controller.login(branchOffice,"julien", "blablabla");
        assertTrue("Projects already present!", controller.getProjects().isEmpty());
        controller.createProject("proj", "xXx", randomTime);
        Assert.fail("Developer should not be able to create projects!");
    }

    @Test
    public void getProjectsTest_Developer() {
        controller.createUser(branchOffice, "alexander", "blabla", "project manager", null);
        controller.createUser(branchOffice,"julien", "blablabla", "developer", LocalTime.of(12, 0));
        controller.login(branchOffice,"alexander", "blabla");
        controller.createProject("proj", "xXx", randomTime);
        ProjectWrapper project = controller.getProjects().get(0);
        controller.logout();
        controller.login(branchOffice,"julien", "blablabla");
        Assert.assertTrue("Project shouldn't be visible!", controller.getProjects().isEmpty());
        controller.logout();
        controller.login(branchOffice,"alexander", "blabla");
        ResourceTypeWrapper developer = controller.getResourceTypes().get(0);
        HashMap<ResourceTypeWrapper, Integer> requirements = new HashMap<>();
        requirements.put(developer, 1);
        controller.createTask(project, "tsk", "oOo", 10, 0.5, requirements);
        TaskWrapper task = controller.getTasks(project).get(0);
        controller.initializePlan(task, randomTime);
        controller.makeExecuting(task);
        controller.logout();
        controller.login(branchOffice,"julien", "blablabla");
        Assert.assertFalse("Project should be visible!", controller.getProjects().isEmpty());
        Assert.assertEquals("Only one project should be visible!", 1, controller.getProjects().size());
        Assert.assertEquals("Wrong project is visible!", project, controller.getProjects().get(0));
        assertEquals("Wrong project status!", "executing", controller.getProjectStatus(project));
    }

    @Test
    public void getProjectsTest_ProjectManager() {
        controller.createUser(branchOffice, "alexander", "blabla", "project manager", null);
        controller.login(branchOffice,"alexander", "blabla");
        assertTrue("Projects already present!", controller.getProjects().isEmpty());
        controller.createProject("proj", "xXx", randomTime);
        assertEquals("More or less than one project added!",1, controller.getProjects().size());
        assertEquals("Wrong project added!", "proj", controller.getProjects().get(0).getName());
        assertEquals("Wrong project added!", "xXx", controller.getProjects().get(0).getDescription());
        ProjectWrapper project = controller.getProjects().get(0);
        assertEquals("Wrong project status!", "active", controller.getProjectStatus(project));
    }

    @Test
    public void getTasksTest_Developer() {
        controller.createUser(branchOffice, "alexander", "blabla", "project manager", null);
        controller.createUser(branchOffice,"julien", "blablabla", "developer", LocalTime.of(12, 0));
        controller.login(branchOffice,"alexander", "blabla");
        controller.createProject("proj", "xXx", randomTime);
        ProjectWrapper project = controller.getProjects().get(0);
        ResourceTypeWrapper developer = controller.getResourceTypes().get(0);
        HashMap<ResourceTypeWrapper, Integer> requirements = new HashMap<>();
        requirements.put(developer, 1);
        controller.createTask(project, "tsk", "oOo", 10, 0.5, requirements);
        TaskWrapper task = controller.getTasks(project).get(0);
        controller.logout();
        controller.login(branchOffice,"julien", "blablabla");
        Assert.assertTrue("Tasks should not be visible!", controller.getTasks(project).isEmpty());
        controller.logout();
        controller.login(branchOffice,"alexander", "blabla");
        controller.initializePlan(task, randomTime);
        controller.makeExecuting(task);
        controller.logout();
        controller.login(branchOffice,"julien", "blablabla");
        Assert.assertEquals("Wrong number of tasks!", 1, controller.getTasks(project).size());
        Assert.assertEquals("Wrong task!", task, controller.getTasks(project).get(0));
        Assert.assertEquals("Wrong task!", "tsk", controller.getTasks(project).get(0).getName());
    }

    @Test
    public void getTasksTest_ProjectManager() {
        controller.createUser(branchOffice, "alexander", "blabla", "project manager", null);
        controller.login(branchOffice,"alexander", "blabla");
        controller.createProject("proj", "xXx", randomTime);
        ProjectWrapper project = controller.getProjects().get(0);
        controller.createTask(project, "tsk", "oOo", 10, 0.5, new HashMap<>());
        Assert.assertEquals("Wrong number of tasks!", 1, controller.getTasks(project).size());
        Assert.assertEquals("Wrong task!", "tsk", controller.getTasks(project).get(0).getName());
    }

    @Test
    public void getStartingTimesTest() {
        controller.createUser(branchOffice, "alexander", "blabla", "project manager", null);
        controller.login(branchOffice,"alexander", "blabla");
        controller.createProject("proj", "xXx", randomTime);
        ProjectWrapper project = controller.getProjects().get(0);
        controller.createTask(project, "tsk", "oOo", 10, 0.5, new HashMap<>());
        TaskWrapper task = controller.getTasks(project).get(0);
        Assert.assertTrue("No starting time given.", controller.getStartingTimes(task).hasNext());
    }

    // TODO: rest van de tests overlopen ...

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
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            throw new AccessDeniedException("Has no access to the file");
        }
    }

}
