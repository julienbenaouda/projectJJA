package test;

import junit.framework.Assert;
import taskman.Clock;
import taskman.Controller;
import taskman.Project;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.HashMap;

public class CreateProjectUseCase {

    private CreateProjectUseCase.UIMock ui;


    class UIMock extends taskman.UI {
        ArrayDeque<String> input;
        String output;

        public UIMock(Controller c) {
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
        ui = new CreateProjectUseCase.UIMock(c);
    }

    @Test
    public void testAddProject()
    {
        ui.setInput("22/02/2032 22:22");
        ui.setInput("22/02/2022 10:10");
        ui.setInput("testdesc");
        ui.setInput("test");
        try {
            ui.createProject();
        } catch (UnsupportedOperationException e) {}
        Assert.assertTrue(ui.getOutput().contains("Project created successfully"));
    }

}
