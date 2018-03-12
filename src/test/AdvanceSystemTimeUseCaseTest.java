package test;

import static org.junit.Assert.*;

import java.util.ArrayDeque;

import org.junit.Test;

import junit.framework.Assert;

public class AdvanceSystemTimeUseCaseTest {
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

	@Test
	public void testAdvanceSystemTime()
	{
		UIMock ui = new UIMock();
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
		UIMock ui = new UIMock();
		ui.setInput("11/11/2017");
		ui.setInput("7");
		try {
			ui.showMainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("valid"));
	}


}
