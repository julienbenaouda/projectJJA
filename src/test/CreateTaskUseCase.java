package test;

import java.util.ArrayDeque;
import java.util.HashMap;

import org.junit.Test;

import junit.framework.Assert;
import taskman.Clock;
import taskman.Controller;
import taskman.Project;
import taskman.UserInterface;

public class CreateTaskUseCase {
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
	

	@Test
	public void testCreateTask() {
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
		Assert.assertTrue(ui.getOutput().contains("Task added successfully"));
	}
	
	@Test
	public void addTaskInvalidData()
	{
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
		ui.setInput("noNumber");
		ui.setInput("testtask");
		try {
			ui.createTask("test");
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("Error"));
	}

}
