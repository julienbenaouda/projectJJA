package test;

import taskman.Project;
import taskman.Task;
import taskman.XmlException;
import taskman.XmlObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.OperationNotSupportedException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class ProjectTest {
	private Project p;
	private String creation;
	private String due;
	private ArrayList<Task> tasks;
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);

	@Before
	public void setUp() {
		tasks = new ArrayList<>();
	}

	@Test
	public void testProjectcorrect() {
		creation = "03/03/2018 12:00";
		due = "09/03/2018 19:00";
		p = new Project("test", "testdesc", creation, due);
		Assert.assertEquals("The specified name is not equal to the given name", "test", p.getName());
		Assert.assertEquals("the descriptions are not equal", "testdesc", p.getDescription());
		Assert.assertEquals("The start dates are not equal", creation, p.getCreationTime().format(dateFormatter));
		Assert.assertEquals("the due dates are not equal", due, p.getDueTime().format(dateFormatter));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testProjectEarlyEndDate() {
        creation = "03/03/2018 12:00";
        due = "01/03/2018 14:00";
		p = new Project("test", "testdesc", creation, due);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testProjectWrongDate() {
		creation = "050/12/2010 13:00";
		due="12/12/2012 12:12";
		p = new Project("test", "testdesc", creation, due);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testProjectIllegalDate()
	{
		creation = "02/02/2002 02:02";
		due = "29/02/2011 11:11";
		p = new Project("test", "testdesc", creation, due);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testProjectNoName()
	{
		creation = "20/11/2007 16:50";
		due = "22/01/2020 19:00";
		p = new Project("", "testdesc", creation, due);
	}

	
	@Test
	public void testAddTask()
	{
		Project p = new Project("test", "testdesc", "22/02/2015 15:00", "22/05/2019 11:00");
		Task t = new Task("taskdesc", "20", "5");
		p.addTask(t);
		Integer id = t.getID();
		Task added = p.getTask(id);
		Assert.assertEquals(t, added);
	}
	
	@Test
	public void testCreateProjectWithForm()
	{
		creation = "22/04/2018 12:50";
		due = "22/05/2018 14:00";
		HashMap<String, String> h = Project.getCreationForm();
		h.put("name", "test");
		h.put("description", "testdesc");
		h.put("creationTime", "22/04/2018 12:50");
		h.put("dueTime", "22/05/2018 14:00");
		Project pnew = new Project(h);
		Assert.assertEquals("The specified name is not equal to the given name", "test", pnew.getName());
		Assert.assertEquals("the descriptions are not equal", "testdesc", pnew.getDescription());
		Assert.assertEquals("The start dates are not equal", creation, pnew.getCreationTime().format(dateFormatter));
		Assert.assertEquals("the due dates are not equal", due, pnew.getDueTime().format(dateFormatter));
	}
	
	@Test
	public void testGetAvailableTaskDetails() {
		Project p = new Project("test", "testdesc", "22/02/2002 22:22", "22/02/2004 22:22");
		Task t = new Task("test",  "20", "5");
		p.addTask(t);
		Assert.assertEquals("There is a different number of available tasks",1, p.getAvailableTaskDetails().size());
		Assert.assertEquals("The task is not the available task", t.getID().toString(), p.getAvailableTaskDetails().get(0).get("id"));
	}
	
	@Test
	public void testGetTask()
	{
		Task t = new Task("testdesc", "10", "10");
		int id = t.getID();
		p = new Project("test", "testdesc", "22/02/2011 10:00", "22/05/2012 11:00");
		p.addTask(t);
		HashMap<String, String> h = p.getTaskDetails(id);
		Assert.assertEquals("testdesc", h.get("description"));
		Assert.assertEquals("10", h.get("estimatedDuration"));
		Assert.assertEquals("10", h.get("acceptableDeviation"));
		// TODO: task krijgt niet onderstaande parametrs van project
		Assert.assertEquals("22/02/2011", h.get("startTime"));
		Assert.assertEquals("22/05/2012 11:00", h.get("endTime"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetTaskIllegalID()
	{
		Project p = new Project("test", "testdesc", "22/02/2018 22:00", "22/05/2018 22:00");
		p.getTaskDetails(25);
	}
	
	@Test
	public void removeTask()
	{
		Project p = new Project("test", "testdesc", "12/12/2012 12:12", "12/12/2013 12:12");
		Task t = new Task("test", "5", "5");
		int id = t.getID();
		p.addTask(t);
		p.removeTask(id);
		Assert.assertEquals(0, p.getTaskIds().size());
	}
	
	@Test
	public void testExportXML()
	{
		try {
			Project project = new Project("test", "testdesc", "22/03/2018 12:00", "24/03/2018 12:00");
			XmlObject object = new XmlObject();
			project.addToXml(object.addXmlObject("project"));
			Project pnew = Project.getFromXml(object.getXmlObjects("project").get(0));
			Assert.assertEquals(project.getName(), pnew.getName());
			Assert.assertEquals(project.getDescription(), pnew.getDescription());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
