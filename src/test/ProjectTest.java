package test;

import taskman.Project;
import taskman.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

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
	public void testProjectSaveToXML()
	{
		creation = "20/11/2007 16:50";
		due = "22/01/2020 19:00";
		p = new Project("test", "testdesc", creation, due);
		String xml = "<project><name>test</name><description>testdesc</description><creationTime>20/11/2007 16:50</creationTime><dueTime>22/01/2020 19:00</dueTime><tasks/></project>";
		try {
			Element e = p.saveToXML();
			// convert to xml
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); // omit xml header
			DOMSource d = new DOMSource(e);
			StringWriter sw = new StringWriter();
			StreamResult r = new StreamResult(sw);
			t.transform(d, r);
			Assert.assertEquals("the xml text is not equal", xml, sw.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddTask()
	{
		Task t = new Task("taskdesc", "20", "5", "20/02/2018 11:00", "22/02/2018 15:00");
		p.addTask(t);
		int id = t.getID();
		Task added = p.getTask(id);
		Assert.assertEquals(t, added);
	}
	
	@Test
	public void testCreateProjectWithForm()
	{
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
	public void testgetAvailableTaskDetails() {
		Task t = new Task("test",  "20", "5", "20/02/2018 11:00", "22/02/2018 15:00");
		p.addTask(t);
		// todo set status to available to let task succeed
		Assert.assertEquals(1, p.getAvailableTaskDetails().size());
	}
	
	@Test
	public void testGetTask()
	{
		Task t = new Task("testdesc", "10", "10", "22/01/2014 11:00", "22/02/2022 12:00");
		int id = t.getID();
		p = new Project("test", "testdesc", "22/02/2011", "22/05/2012 11:00");
		p.addTask(t);
		HashMap<String, String> h = p.getTaskDetails(id);
		Assert.assertEquals("testdesc", h.get("description"));
		Assert.assertEquals("10", h.get("estimatedDuration"));
		Assert.assertEquals("10", h.get("acceptableDeviation"));
		Assert.assertEquals("22/02/2011", h.get("startTime"));
		Assert.assertEquals("22/05/2012 11:00", h.get("endTime"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetTaskIllegalID()
	{
		Project p = new Project("test", "testdesc", "22/02/2018 22:00", "22/05/2018 22:00");
		p.getTaskDetails(25);
	}

}
