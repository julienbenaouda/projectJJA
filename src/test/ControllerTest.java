package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import taskman.Controller;

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
    public void getProjectNames() {
    }

    @Test
    public void projectExists() {
    }

    @Test
    public void getProjectCreationForm() {
    }

    @Test
    public void addProject() {
    }

    @Test
    public void getProjectDetails() {
    }

    @Test
    public void getTasksOfProject() {
    }

    @Test
    public void getTaskDetails() {
    }

    @Test
    public void getTaskCreationForm() {
    }

    @Test
    public void addTask() {
    }

    @Test
    public void addAlternativeToTask() {
    }

    @Test
    public void addDependencyToTask() {
    }

    @Test
    public void updateTaskStatus() {
    }

    @Test
    public void getSystemTime() {
    }

    @Test
    public void updateSystemTime() {
    }

    @Test
    public void setUser() {
    }

    @Test
    public void importXML() {
    }

    @Test
    public void exportToXML() {
    }
}