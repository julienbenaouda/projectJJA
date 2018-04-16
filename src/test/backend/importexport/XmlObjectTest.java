package test.backend.importexport;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import taskman.backend.importexport.ImportExportException;
import taskman.backend.importexport.XmlObject;

import java.io.File;
import java.nio.file.AccessDeniedException;

public class XmlObjectTest {

    private static XmlObject object;

    @Before
    public void setUp() throws ImportExportException {
        object = new XmlObject();
    }

    @After
    public void tearDown() {
        object = null;
    }

    private void deleteFile(String path) throws AccessDeniedException {
        File file = new File(path);
        if (file.exists()) {
            if (!file.delete()) {
                throw new AccessDeniedException("Cannot write file to '" + path + "'!");
            }
        }
    }

    @Test
    public void import_export() throws ImportExportException, AccessDeniedException {
        XmlObject sub1 = object.createChild("object");
        XmlObject sub2 = object.createChild("object");
        XmlObject sub3 = sub2.createChild("object");

        object.addAttribute("attribute", "attribute value");
        object.addText("text", "text value 1");
        object.addText("text", "text value 2");
        sub1.addAttribute("sub1_attribute", "sub1 attribute value");
        sub1.addText("sub1_text", "sub1 text value");
        sub3.addText("sub3_text", "sub3 text value");

        String path = System.getProperty("user.dir") + File.separator + "test.xml";
        deleteFile(path);

        object.exportTo(path);
        System.out.println("A file is temporally saved to '" + path + "'");
        Assert.assertTrue("XML file cannot be saved!", new File(path).exists());

        XmlObject new_object = XmlObject.importFrom(path);
        deleteFile(path);
        System.out.println("A file is deleted from '" + path + "'");

        XmlObject new_sub1 = new_object.getXmlObjects("object").get(0);
        XmlObject new_sub2 = new_object.getXmlObjects("object").get(1);
        XmlObject new_sub3 = new_sub2.getXmlObjects("object").get(0);

        Assert.assertEquals("Wrong attribute value!", new_object.getAttribute("attribute"), "attribute value");
        Assert.assertEquals("Wrong text value!", new_object.getTexts("text").get(0), "text value 1");
        Assert.assertEquals("Wrong text value!", new_object.getTexts("text").get(1), "text value 2");
        Assert.assertEquals("Wrong attribute value!", new_sub1.getAttribute("sub1_attribute"), "sub1 attribute value");
        Assert.assertEquals("Wrong text value!", new_sub1.getTexts("sub1_text").get(0), "sub1 text value");
        Assert.assertEquals("Wrong text value!", new_sub3.getTexts("sub3_text").get(0), "sub3 text value");
    }

    // TODO: tests voor exceptions...

}