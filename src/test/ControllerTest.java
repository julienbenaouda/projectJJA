package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.Controller;
import taskman.Project;

import java.util.List;


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
    public void testGetProjects() {
        //Assert.assertEquals("Empty controller should not contain projects!", 0, controller.getProjects().size());
    }

    @Test
    public void testGetProjectDetails() {
        Assert.fail("Not yet implemented");

    }

    @Test
    public void testAddNewProject() {
        String name = "test";
        String description = "test desc";
        String creation = "24/02/2018 10:00";
        String due = "15/03/2018 12:30";

        // TODO controller.addNewProject(name, description, creation, due);

        // TODO List<Project> projects = controller.getProjects();
        // TODO Assert.assertEquals("More than one project is added to the controller!", 1, projects.size());

        // TODO Project project = projects.get(0);
        // TODO Assert.assertEquals("Name of new project is not correct!", name, project.getName());
        // TODO Assert.assertEquals("Description of new project is not correct!", description, project.getDescription());
        // TODO Assert.assertEquals("Creation time of new project is not correct!", creation, project.getCreationTime());
        // TODO Assert.assertEquals("Due time of new project is not correct!", due, project.getDueTime());
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