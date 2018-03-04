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
import java.util.ArrayList;

import javax.naming.OperationNotSupportedException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class ProjectTest {
	private Project p;
	private String creation;
	private String due;
	private ArrayList<Task> tasks;
	
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
		Assert.assertEquals("the descriptions are not equal", "test desc", p.getDescription());
		Assert.assertEquals("The start dates are not equal", creation, p.getCreationTime());
		Assert.assertEquals("the due dates are not equal", due, p.getDueTime());
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
	public void testProjectSaveToString()
	{
		creation = "20/11/2007 16:50";
		due = "22/01/2020 19:00";
		p = new Project("test", "testdesc", creation, due);
		String xml = "<project><name>test</name><description>testdesc</description><startTime>20/11/2007 16:50</startTime><dueTime>22/01/2020 19:00</dueTime><tasks></tasks></project>";
		try {
			Element e = p.saveToString();
			// convert to xml
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); // omit xml header
			DOMSource d = new DOMSource(e);
			StringWriter sw = new StringWriter();
			StreamResult r = new StreamResult(sw);
			t.transform(d, r);
			Assert.assertEquals("the xml text is not equal", xml, sw.toString());
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
