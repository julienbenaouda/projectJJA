package test;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.HashMap;

import org.junit.Test;

import junit.framework.Assert;
import taskman.Clock;
import taskman.Controller;
import taskman.Project;

public class AdvanceSystemTimeUseCaseTest {
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

	@Test
	public void testAdvanceSystemTime()
	{
		Clock clock = new Clock();
		HashMap<String, Project> projects = new HashMap<>();
		Controller controller = new Controller(projects, clock);
		UIMock ui = new UIMock(controller);
		ui.setInput("11/11/2017 16:20");
		ui.setInput("4");
		try {
			ui.showMainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("successfully"));
	}
	
	@Test
	public void testAdvanceTimeInvalidFormat()
	{
		Clock clock = new Clock();
		HashMap<String, Project> projects = new HashMap<>();
		Controller c = new Controller(projects, clock);
		UIMock ui = new UIMock(c);
		ui.setInput("11/11/2017");
		ui.setInput("7");
		try {
			ui.showMainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("valid"));
	}


}
