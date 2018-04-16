package test.frontend;

public class UserInterfaceTest {

	TODO: zorgen dat de tests compileren!

	private UserInterfaceMock ui;
	private Controller c;
	private HashMap<String, project> projects;
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
				throw new UnsupportedOperationException("The program terminates here (only for testing purposes)");
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
		projects = new HashMap<>();
		c = new Controller(projects, clock);
		ui = new UserInterfaceMock(c);
	}
	
	@Test
	public void testChoseUser()
	{
		try {
			ui.setInput("1");
			ui.loginDialog();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("project"));
	}

	@Test
	public void testListProjects() {
		project p1 = new project("test 1", "testdesc", "22/02/2016 22:00", "22/03/2017 12:00");
		projects.put("test 1", p1);
		project p2 = new project("test 2", "testdesc", "22/02/2016 22:00", "22/03/2017 12:00");
		projects.put("test 2", p2);
		try {
			ui.showProjectsAndTasks();
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
		Assert.assertTrue(ui.getOutput().contains("project created successfully"));
	}
	
	@Test
	public void testAddProjectInvalidData()
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
		project p = new project("test", "testdesc", "09/03/2018 20:00", "04/12/2019 09:00");
		projects.put("test", p);
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
		Assert.assertTrue(ui.getOutput().contains("exist"));
	}
	
	@Test
	public void testShowProjectDetails()
	{
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
		ui.setInput("1");
		ui.setInput("test");
		ui.setInput("1");
		try {
			ui.showProjectMenu("test");
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("description:"));
	}
	
	@Test
	public void testAddTask()
	{
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
		ui.setInput("5");
		ui.setInput("10");
		ui.setInput("testtask");
		try {
			ui.createTask("test");
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("task added successfully"));
	}
	
	@Test
	public void addTaskInvalidData()
	{
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
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
			ui.loginDialog();
		} catch (UnsupportedOperationException e) {}
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
		task t = new task("testtask", "5", "5");
		p.createTask(t);
		ui.setInput("FINISHED");
		ui.setInput("12/02/2018 12:00");
		ui.setInput("11/01/2017 15:00");
		ui.setInput("1");
		try {
			ui.updateTaskStatus("test");
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("updated successfully"));
	}

	@Test
	public void testUpdateTaskStatusInvalidData()
	{
		ui.setInput("2");
		try {
			ui.loginDialog();
		} catch (UnsupportedOperationException e) {}
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
		task t = new task("testtask", "5", "5");
		p.createTask(t);
		ui.setInput("finished");
		ui.setInput("12/02/2018 12:00");
		ui.setInput("11/01/2017 15:00");
		ui.setInput("1");
		try {
			ui.updateTaskStatus("test");
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("Error"));
	}

	@Test
	public void testUpdateTaskStatusAccessDenied()
	{
		ui.setInput("1");
		try {
			ui.loginDialog();
		} catch (UnsupportedOperationException e) {}
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
		task t = new task("testtask", "5", "5");
		p.createTask(t);
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
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
		task t = new task("testtask", "5", "5");
		p.createTask(t);
		ui.setInput("1");
		try {
			ui.showTaskDetails("test");
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("description:"));
	}

	private String os_path = System.getProperty("user.dir") + File.separator;

	@Test
	public void testExport() {
		try {
			ui.setInput(os_path + "file.xml");
			ui.setInput("6");
			ui.mainMenu();
		} catch(UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("successfully"));
	}
	
	@Test
	public void testExportInvalidPath()
	{
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
		task t = new task("testtask", "5", "5");
		p.createTask(t);
		try {
			ui.setInput(os_path + "fil:e.xml");
			ui.setInput("6");
			ui.mainMenu();
		} catch(UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("Error"));
	}
	
	@Test
	public void testImport()
	{
		ui.setInput(os_path + "file.xml");
		ui.setInput("5");
		try {
			ui.mainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("successfully"));
	}
	
	@Test
	public void testImportInvalidPath() {
		ui.setInput(os_path + "file2.xml");
		ui.setInput("5");
		try {
			ui.mainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("Error"));
	}
	
	@Test
	public void testAdvanceSystemTime()
	{
		ui.setInput("11/11/2017 16:20");
		ui.setInput("4");
		try {
			ui.mainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("successfully"));
	}
	
	@Test
	public void testAdvanceTimeInvalidFormat()
	{
		ui.setInput("11/11/2017");
		ui.setInput("7");
		try {
			ui.mainMenu();
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("valid"));
	}
	
	@Test
	public void testAddDependencyToTask() {
		ui.setInput("2");
		try {
			ui.loginDialog();
		} catch (UnsupportedOperationException e) {}
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
		task t = new task("testtask", "5", "5");
		p.createTask(t);
		task t2 = new task("testtask 2", "5", "5");
		p.createTask(t2);
		ui.setInput("1");
		ui.setInput("2");
		try {
			ui.addDependencyToTask("test");
		} catch (UnsupportedOperationException e) {} catch (AccessDeniedException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(ui.getOutput().contains("Dependency added successfully"));
	}
	
	@Test
	public void addAlternativeToTask() {
		ui.setInput("2");
		try {
			ui.loginDialog();
		} catch (UnsupportedOperationException e) {}
		project p = new project("test", "testdesc", "17/03/2018 11:00", "17/04/2018 11:00");
		projects.put("test", p);
		task t = new task("testtask", "5", "5");
		p.createTask(t);
		task t2 = new task("testtask 2", "5", "10");
		p.createTask(t2);
		ui.setInput("1");
		ui.setInput("2");
		try {
			ui.addAlternativeToTask("test");
		} catch (UnsupportedOperationException e) {}
		Assert.assertTrue(ui.getOutput().contains("Alternative added successfully"));
	}



}
