package probeersel;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class test {

	public static void main(String[] args) {

		TestData testData = new TestData("Hello XML!");

		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(testData);

		System.out.println(xml);
	}

}
