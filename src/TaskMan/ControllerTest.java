package TaskMan;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;


class ControllerTest {

    //////////////////// Setup ////////////////////

    private static Controller controller;

    @Before
    public void runBeforeMethod() {
        this.controller = new Controller();
    }

    @After
    public void runAfterMethod() {
        this.controller = null;
    }



    //////////////////// Getters ////////////////////

    @Test
    public void testGetProjects() {

    }

    @Test
    public void testGetProjectDetails() {

    }

    @Test
    public void testGetTaskDetails() {

    }

    @Test
    public void testGetSystemTime() {

    }


    //////////////////// Setters ////////////////////

    @Test
    public void testAddNewProject() {

    }

    @Test
    public void testAddNewTask() {

    }

    @Test
    public void testUpdateTaskStatus() {

    }

    @Test
    public void testUpdateSystemTime() {

    }

}