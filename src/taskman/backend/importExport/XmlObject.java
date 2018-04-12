package taskman.backend.importExport;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for providing a convenient, simple and safe abstraction of an Element in XML.
 * @author Alexander Braekevelt, Julien Benaouda
 * Source: https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
 * Source: https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 */
public class XmlObject {

    /**
     * Represents the document (root object) of the DOM.
     */
    private Document doc;

    /**
     * Represents an element of the DOM which is edited by this object.
     */
    private Element element;

    /**
     * Create an empty xml object.
     * @throws ImportExportException if the object can't be created.
     */
    public XmlObject() throws ImportExportException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            this.doc = doc;
            this.element = doc.createElement("XmlObject");
        } catch (ParserConfigurationException e) {
            throw new ImportExportException("ParserConfigurationException: " + e.getMessage());
        }
    }

    /**
     * Create an xml object with a given element of a given document.
     * @param doc the xml document
     * @param node the xml element within the document
     * @throws NullPointerException if an argument is null.
     */
    private XmlObject(Document doc, Element node) throws NullPointerException {
        if (doc == null || node == null) {
            throw new NullPointerException("Null objects in constructor of XmlObject!");
        }
        this.doc = doc;
        this.element = node;
    }

    /**
     * Write this object to a XML file.
     * @param path the path to write to.
     * @throws ImportExportException if the object can't be written to the file or the path is null.
     * @throws NullPointerException is path is null.
     */
    public void exportTo(String path) throws ImportExportException, NullPointerException {
        if (path == null) {
            throw new NullPointerException("Path is null!");
        }
        try {
            this.doc.appendChild(this.element);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(this.doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throw new ImportExportException("TransformerConfigurationException: " + e.getMessage());
        } catch (TransformerException e) {
            throw new ImportExportException("TransformerException: " + e.getMessage());
        }
    }

    /**
     * Create an XmlObject from a given XML file.
     * @param path a String with the path of the file
     * @return the created XmlObject
     * @throws ImportExportException if the object can't be created.
     * @throws NullPointerException if the path is null
     */
    public static XmlObject importFrom(String path) throws ImportExportException, NullPointerException {
        if (path == null) {
            throw new NullPointerException("Path is null!");
        }
        try {
            File file = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            return new XmlObject(doc, doc.getDocumentElement());
        } catch (ParserConfigurationException e) {
            throw new ImportExportException("ParserConfigurationException: " + e.getMessage());
        } catch (IOException e) {
            throw new ImportExportException("IOException: " + e.getMessage());
        } catch (SAXException e) {
            throw new ImportExportException("SAXException: " + e.getMessage());
        }
    }

    /**
     * Create an XmlObject as child of this XmlObject.
     * @param name a String with the name of the child object.
     * @return an XmlObject with the given name and parent.
     * @throws NullPointerException if the name is null.
     */
    public XmlObject createChild(String name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException("Name of XmlObject is null!");
        }
        Element element = this.doc.createElement(name);
        this.element.appendChild(element);
        return new XmlObject(this.doc, element);
    }

    /**
     * Get an XmlObject with the given name that is a child of this object.
     * @param name a String with the name of the object.
     * @return an XmlObject.
     * @throws ImportExportException if a child with the given name is not an XmlObject.
     * @throws NullPointerException if the name is null.
     */
    public XmlObject getChild(String name) throws ImportExportException, NullPointerException {
        List<XmlObject> objects = getXmlObjects(name);
        if (objects.isEmpty()) {
            throw new ImportExportException("No XmlObject with name '" + name + "' found!");
        }
        else {
            return objects.get(0);
        }
    }

    /**
     * Get all XmlObject with the given name that are a child of this object.
     * @param name a String with the name of the objects.
     * @return a list of XmlObjects.
     * @throws ImportExportException if a child with the given name is not an XmlObject.
     * @throws NullPointerException if the name is null.
     */
    public List<XmlObject> getXmlObjects(String name) throws ImportExportException, NullPointerException {
        if (name == null) {
            throw new NullPointerException("Name of XmlObject is null!");
        }
        NodeList nodes = this.element.getElementsByTagName(name);
        ArrayList<XmlObject> results = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                results.add(new XmlObject(this.doc, (Element) node));
            } else {
                throw new ImportExportException("A node with name '" + name + "' cannot be parsed to an XmlObject!");
            }
        }
        return results;
    }

    /**
     * Add an attribute to this object with a unique name and a value.
     * Warning: a value "null" will be returned as null in getAttribute(name)!
     * @param name  a String with the unique name of the attribute.
     * @param value a String with the value of the attribute.
     * @throws ImportExportException if the name contains whitespaces.
     * @throws NullPointerException if name or value are null.
     */
    public void addAttribute(String name, String value) throws ImportExportException, NullPointerException {
        if (name == null || value == null) {
            throw new NullPointerException("An attribute is added with a null as name or value!");
        }
        if (name.contains(" ")) {
            throw new ImportExportException("The name '" + name + "' of an attribute cannot contain whitespaces!");
        }
        Attr attribute = doc.createAttribute(name);
        attribute.setValue(value);
        element.setAttributeNode(attribute);
    }

    /**
     * Returns the value of an attribute of this object.
     * @param name a String with the unique name of the attribute.
     * @return a String with the value of the attribute.
     * @throws ImportExportException if the attribute is empty or does not exist, or the name contains whitespaces.
     * @throws NullPointerException if the name is null.
     */
    public String getAttribute(String name) throws ImportExportException, NullPointerException {
        if (name == null) {
            throw new NullPointerException("Name of attribute is null!");
        }
        if (name.contains(" ")) {
            throw new ImportExportException("The name '" + name + "' of an attribute cannot contain whitespaces!");
        }
        String value = this.element.getAttribute(name);
        if (value.isEmpty()) {
            throw new ImportExportException("The value of '" + name + "' is empty or does not exist!");
        } else {
            return value;
        }
    }

    /**
     * Add a text attribute to this object with a name and a value.
     * @param name  a String with the name of the text attribute.
     * @param value a String with the value of the text attribute.
     * @throws ImportExportException if the name contains whitespaces.
     * @throws NullPointerException if name or value are null.
     */
    public void addText(String name, String value) throws ImportExportException, NullPointerException {
        if (name == null || value == null) {
            throw new NullPointerException("Text is added with a null as name or value!");
        }
        if (name.contains(" ")) {
            throw new ImportExportException("The name '" + name + "' of a text attribute cannot contain whitespaces!");
        }
        Element element = this.doc.createElement(name);
        this.element.appendChild(element);
        element.appendChild(this.doc.createTextNode(value));
    }

    /**
     * Returns the value of a text attribute of this object.
     * @param name a String with the name of the text attribute.
     * @return a String with the value of the text attribute.
     * @throws ImportExportException if the text attribute cannot be handled, does not exist or the name contains whitespaces.
     * @throws NullPointerException if the name is null.
     */
    public String getText(String name) throws ImportExportException {
        List<String> texts = getTexts(name);
        if (texts.isEmpty()) {
            throw new ImportExportException("No text with name '" + name + "' found!");
        }
        else {
            return texts.get(0);
        }
    }

    /**
     * Returns the values of a text attribute of this object.
     * @param name a String with the name of the text attribute.
     * @return a list of Strings with the values of the text attribute.
     * @throws ImportExportException if the text attribute cannot be handled or the name contains whitespaces.
     * @throws NullPointerException if the name is null.
     */
    public List<String> getTexts(String name) throws ImportExportException, NullPointerException {
        if (name == null) {
            throw new NullPointerException("Name of text is null!");
        }
        if (name.contains(" ")) {
            throw new ImportExportException("The name '" + name + "' of a text attribute cannot contain whitespaces!");
        }
        NodeList nodes = this.element.getElementsByTagName(name);
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            try {
                results.add(node.getTextContent());
            } catch (DOMException e) {
                throw new ImportExportException("DOMException: " + e.getMessage());
            }
        }
        return results;
    }

}
