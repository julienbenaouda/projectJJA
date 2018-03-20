package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import taskman.Clock;
import taskman.Controller;
import taskman.Project;
import taskman.UserInterface;

import java.util.ArrayDeque;
import java.util.HashMap;

public class UpdateTaskStatusUseCase {

    private static UserInterfaceMock ui;

    class UserInterfaceMock extends UserInterface {
        ArrayDeque<String> input;
        String output;

        public UserInterfaceMock(Controller c) {
            super(c);
            input = new ArrayDeque<>();
            emptyOutput();
        }

        public void setInput(String input)
        {
            this.input.push(input);
        }


        @Override
        protected String inputString() throws UnsupportedOperationException
        {
            if(input.size() > 0)
            {
                String text = input.pop();
                return text;
            } else {
                throw new UnsupportedOperationException("de program terminates here (only for testing purposes)");
            }
        }

        @Override
        public void print(String text) {
            output = output +text;
        }


        public String getOutput()
        {
            return output;
        }


        public void emptyOutput()
        {
            output = "";
        }
    }

    @Before
    public void setUp() throws Exception {
					Clock clock = new Clock();
					HashMap<String, Project> projects = new HashMap<>();
					Controller c = new Controller(projects, clock);
					UserInterfaceMock ui = new UserInterfaceMock(c);
    }


    @Test
    public void testListProjects() {
								Clock clock = new Clock();
								HashMap<String, Project> projects = new HashMap<>();
								Controller c = new Controller(projects, clock);
								UserInterfaceMock ui = new UserInterfaceMock(c);
        ui.setInput("22/02/2032 22:22");
        ui.setInput("22/02/2022 10:10");
        ui.setInput("testdesc");
        ui.setInput("test");
        try {
            ui.createProject();
        } catch (UnsupportedOperationException e) {}
        ui.setInput("5");
        ui.setInput("10");
        ui.setInput("testtask");
        try {
            ui.createTask("test");
        } catch (UnsupportedOperationException e) {}
        ui.setInput("1");
        try {
            ui.listProjects();
        } catch (UnsupportedOperationException e) {}
        Assert.assertTrue(ui.getOutput().contains("test"));
        try {
            ui.showProjectDetails("test");
        } catch (UnsupportedOperationException e) {}
        Assert.assertTrue(ui.getOutput().contains("name"));
        try {
            ui.showTaskDetails("test");
        } catch (UnsupportedOperationException e) {}
        Assert.assertTrue(ui.getOutput().contains("description:"));
    }

    @Test
    public void testOpenProject()
    {
        ui.setInput("22/02/2032 22:22");
        ui.setInput("22/02/2022 10:10");
        ui.setInput("testdesc");
        ui.setInput("test");
        try {
            ui.createProject();
        } catch (UnsupportedOperationException e) {}
        ui.setInput("test");
        try {
            ui.showProjectMenu();
        } catch(UnsupportedOperationException e) {}
        junit.framework.Assert.assertTrue(ui.getOutput().contains("task"));
    }

    @Test
    public void testUpdateTaskStatusAccessDenied()
    {
        ui.setInput("1");
        try {
            ui.showUserChoiceDialog();
        } catch (UnsupportedOperationException e) {}
        ui.setInput("22/02/2032 22:22");
        ui.setInput("22/02/2022 10:10");
        ui.setInput("testdesc");
        ui.setInput("test");
        try {
            ui.createProject();
        } catch (UnsupportedOperationException e) {}
        ui.setInput("5");
        ui.setInput("10");
        ui.setInput("testtask");
        try {
            ui.createTask("test");
        } catch (UnsupportedOperationException e) {}
        ui.setInput("FINISHED");
        ui.setInput("12/02/2018 12:00");
        ui.setInput("11/01/2017 15:00");
        ui.setInput("1");
        try {
            ui.updateTaskStatus("test");
        } catch (UnsupportedOperationException e) {}
        System.out.println(ui.getOutput());
        junit.framework.Assert.assertTrue(ui.getOutput().contains("denied"));
    }

    @Test
    public void testUpdateTaskStatusInvalidData()
    {
        ui.setInput("2");
        try {
            ui.showUserChoiceDialog();
        } catch (UnsupportedOperationException e) {}
        ui.setInput("22/02/2032 22:22");
        ui.setInput("22/02/2022 10:10");
        ui.setInput("testdesc");
        ui.setInput("test");
        try {
            ui.createProject();
        } catch (UnsupportedOperationException e) {}
        ui.setInput("5");
        ui.setInput("10");
        ui.setInput("testtask");
        try {
            ui.createTask("test");
        } catch (UnsupportedOperationException e) {}
        ui.setInput("finished");
        ui.setInput("12/02/2018 12:00");
        ui.setInput("11/01/2017 15:00");
        ui.setInput("1");
        try {
            ui.updateTaskStatus("test");
        } catch (UnsupportedOperationException e) {}
        System.out.println(ui.getOutput());
        junit.framework.Assert.assertTrue(ui.getOutput().contains("Error"));
    }



}
