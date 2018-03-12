package test;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;

public class CreateProjectUseCase {

    private CreateProjectUseCase.UIMock ui;


    class UIMock extends taskman.UI {
        ArrayDeque<String> input;
        String output;

        public UIMock() {
            super();
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
        ui = new CreateProjectUseCase.UIMock();
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
