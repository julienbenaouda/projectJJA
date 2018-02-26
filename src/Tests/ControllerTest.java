package Tests;

import TaskMan.Controller;
import TaskMan.Project;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;


public class ControllerTest {

    private static Controller controller;

    @Before
    public void runBeforeMethod() {
        this.controller = new Controller();
    }

    @After
    public void runAfterMethod() {
        this.controller = null;
    }



    @Test
    public void testGetProjects() {
        Assert.assertEquals("Empty controller should not contain projects!", 0, controller.getProjects().size());
    }

    @Test
    public void testGetProjectDetails() {
        Assert.fail("Not yet implemented");

    }

    @Test
    public void testAddNewProject() {
        String name = "test";
        String description = "test desc";
        LocalDateTime creation = LocalDateTime.of(2018, Month.FEBRUARY, 24, 10, 0);
        LocalDateTime due = LocalDateTime.of(2018, Month.MARCH, 15, 12, 30);

        controller.addNewProject(name, description, creation, due);

        ArrayList<Project> projects = controller.getProjects();
        Assert.assertEquals("More than one project is added to the controller!", 1, projects.size());

        Project project = projects.get(0);
        Assert.assertEquals("Name of new project is not correct!", name, project.getName());
        Assert.assertEquals("Description of new project is not correct!", description, project.getDescription());
        Assert.assertEquals("Creation time of new project is not correct!", creation, project.getCreationTime());
        Assert.assertEquals("Due time of new project is not correct!", due, project.getDueTime());
    }


    @Test
    public void testGetTaskDetails() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testAddNewTask() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testUpdateTaskStatus() {
        Assert.fail("Not yet implemented");
    }


    @Test
    public void testGetSystemTime() {
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testUpdateSystemTime() {
        Assert.fail("Not yet implemented");
    }

}