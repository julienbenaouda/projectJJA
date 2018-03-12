package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import taskman.Controller;

import java.io.File;
import java.nio.file.AccessDeniedException;
import java.util.ArrayDeque;
import java.util.HashMap;

public class UITest {
	private UIMock ui;
	private Controller c;
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
		ui = new UIMock();
		c = ui.getController();
	}
	
	@Test
	public void testChoseUser()
	{
		try {
			ui.setInput("1");
			ui.showUserChoiceDialog();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("project"));
	}

	@Test
	public void testListProjects() {
		HashMap<String, String> p = new HashMap<>();
		p.put("name", "test 1");
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
		try {
			ui.listProjects();
		} catch (UnsupportedOperationException e) {}
		String actual = ui.getOutput();
		Assert.assertTrue(actual.contains("test 2") && actual.contains("test 1"));
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
	
	@Test
	public void addProjectInvalidData()
	{
		ui.setInput("22/02/2032");
		ui.setInput("22/02/2022 10:10");
		ui.setInput("testdesc");
		ui.setInput("test");
		try {
			ui.createProject();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("Error"));
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
		Assert.assertTrue(ui.getOutput().contains("task"));
	}
	
	@Test
	public void testOpenProjectWrongName()
	{
		ui.setInput("abc");
		try {
			ui.showProjectMenu();
		} catch (UnsupportedOperationException e) {}
		System.out.println(ui.getOutput());
		Assert.assertTrue(ui.getOutput().contains("exist"));
	}
	
	@Test
	public void testShowProjectDetails()
	{
		ui.setInput("22/02/2032 22:22");
		ui.setInput("22/02/2022 10:10");
		ui.setInput("testdesc");
		ui.setInput("test");
		try {
			ui.createProject();
		} catch (UnsupportedOperationException e) {}
		ui.setInput("1");
		ui.setInput("test");
		ui.setInput("2");
		try {
			ui.showMainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("name:"));
	}
	
	@Test
	public void testAddTask()
	{
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
	
	@Test
	public void testUpdateTaskStatus()
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
		ui.setInput("FINISHED");
		ui.setInput("12/02/2018 12:00");
		ui.setInput("11/01/2017 15:00");
		ui.setInput("1");
		try {
			ui.updateTaskStatus("test");
		} catch (UnsupportedOperationException e) {}
		System.out.println(ui.getOutput());
		Assert.assertTrue(ui.getOutput().contains("updated successfully"));
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
		Assert.assertTrue(ui.getOutput().contains("Error"));
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
		Assert.assertTrue(ui.getOutput().contains("denied"));
	}
	
	@Test
	public void testShowTaskDetails()
	{
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
			ui.showTaskDetails("test");
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("description:"));
	}

	private String os_path = System.getProperty("user.dir") + File.separator;

	@Test
	public void testExport() {
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
		try {
			ui.setInput(os_path + "file.xml");
			ui.setInput("6");
			ui.showMainMenu();
		} catch(UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("successfully"));
	}
	
	@Test
	public void testExportInvalidPath()
	{
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
		try {
			ui.setInput(os_path + "fil:e.xml");
			ui.setInput("6");
			ui.showMainMenu();
		} catch(UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("Error"));
	}
	
	@Test
	public void testImport()
	{
		ui.setInput(os_path + "file.xml");
		ui.setInput("5");
		try {
			ui.showMainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("successfully"));
	}
	
	@Test
	public void testImportInvalidPath() {
		ui.setInput(os_path + "file2.xml");
		ui.setInput("5");
		try {
			ui.showMainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("Error"));
	}
	
	@Test
	public void testAdvanceSystemTime()
	{
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
		ui.setInput("11/11/2017");
		ui.setInput("7");
		try {
			ui.showMainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("valid"));
	}
	
	@Test
	public void testAddDependencyToTask() {
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
		ui.setInput("5");
		ui.setInput("10");
		ui.setInput("testtask 2");
		try {
			ui.createTask("test");
		} catch (UnsupportedOperationException e) {}
		ui.setInput("1");
		ui.setInput("2");
		try {
			ui.addDependency("test");
		} catch (UnsupportedOperationException e) {} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertTrue(ui.getOutput().contains("Dependency added successfully"));
	}
	
	@Test
	public void addAlternativeToTask() {
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
		ui.setInput("testtask2");
		try {
			ui.createTask("test");
		} catch (UnsupportedOperationException e) {}
		ui.setInput("5");
		ui.setInput("10");
		ui.setInput("testtask2");
		try {
			ui.createTask("test");
		} catch (UnsupportedOperationException e) {}
		ui.setInput("1");
		ui.setInput("2");
		try {
			ui.addAlternative("test");
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("Alternative added successfully"));
	}

}
