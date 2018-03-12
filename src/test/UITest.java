package test;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import taskman.Controller;

public class UITest {
	private UIMock ui;
	private Controller c;
	private String mainMenu = "options:\\n1 - list all projects\\n2 - open project\\n3 - add new project\\n4 - advance system time\\n5 - import data\\n6 - export data\\n7 - change user\\nChoose option:";
	
	class UIMock extends taskman.UI {
		ArrayDeque<String> input;
		String output;
		
		public UIMock() {
			super();
			input = new ArrayDeque<>();
		}
				
		public void setInput(String input)
		{
			this.input.push(input);
		}
		
		@Override
		protected String inputString()
		{
			if(input.size() > 0)
			{
				String text = input.pop();
				return text;
			}
			return null;
		}
		
		@Override
		public void print(String text) {
			output = text;
		}
		
		public String getOutput()
		{
			return output;
		}
		
	}

	@Before
	public void setUp() throws Exception {
		ui = new UIMock();
		c = ui.getController();
	}
	
	@Test
	public void testChoseUser()
	{
		ui.setInput("1");
		Assert.assertEquals(mainMenu, ui.getOutput());
	}

	@Test
	public void testListProjects() {
		HashMap<String, String> p = new HashMap<>();
		p.put("name", "test");
		p.put("description", "testdesc");
		p.put("creationTime", "22/02/2011 12:12");
		p.put("dueTime", "23/05/2011 12:00");
		HashMap<String, String> p2 = new HashMap<>(); 
		p2.put("name", "test 2"); 
		p2.put("description", "testdesc"); 
		p2.put("creationTime", "09/04/2015 11:11"); 
		p2.put("dueTime", "11/11/2019 19:00");
		c.addProject(p);
		c.addProject(p2);
		String expected = "test\ntest 2"; 
		ui.listProjects();
		String actual = ui.getOutput();
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testAddProject()
	{
		
	}

}
