package taskman;

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
 * This object is used to import and export objects to XML.
 *
 * @author Alexander Braekevelt, Julien Benaouda Source: https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/ Source: https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
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
     *
     * @throws XmlException if the object can't be created.
     */
    public XmlObject() throws XmlException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            this.doc = doc;
            this.element = doc.createElement("XmlObject");
        } catch (ParserConfigurationException e) {
            throw new XmlException("ParserConfigurationException: " + e.getMessage());
        }
    }

    /**
     * Create an xml object with a given element of a given document.
     * @param doc the xml document
     * @param node the xml element within the document
     */
    private XmlObject(Document doc, Element node) {
        this.doc = doc;
        this.element = node;
    }

    /**
     * Write this object to a XML file.
     *
     * @param path the path to write to.
     * @throws XmlException if the object can't be written to the file.
     */
    public void exportTo(String path) throws XmlException {
        try {
            this.doc.appendChild(this.element);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(this.doc);
            StreamResult result = new StreamResult(new File(path));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throw new XmlException("TransformerConfigurationException: " + e.getMessage());
        } catch (TransformerException e) {
            throw new XmlException("TransformerException: " + e.getMessage());
        }
    }

    /**
     * Create a XmlObject from a given XML file.
     *
     * @param path a String with the path of the file
     * @return the created XmlObject
     * @throws XmlException if the object can't be created.
     */
    public static XmlObject importFrom(String path) throws XmlException {
        try {
            File file = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            return new XmlObject(doc, doc.getDocumentElement());
        } catch (ParserConfigurationException e) {
            throw new XmlException("ParserConfigurationException: " + e.getMessage());
        } catch (IOException e) {
            throw new XmlException("IOException: " + e.getMessage());
        } catch (SAXException e) {
            throw new XmlException("SAXException: " + e.getMessage());
        }
    }

    /**
     * Create a XmlObject as child of this XmlObject.
     *
     * @param name a String with the name of the child object.
     * @return a XmlObject with the given name and parent.
     */
    public XmlObject addXmlObject(String name) {
        Element element = this.doc.createElement(name);
        this.element.appendChild(element);
        return new XmlObject(this.doc, element);
    }

    /**
     * Get all XmlObject with the given name that are a child of this object.
     *
     * @param name a String with the name of the objects.
     * @return a list of XmlObjects.
     * @throws XmlException if a child with the given name is not a XmlObject.
     */
    public List<XmlObject> getXmlObjects(String name) throws XmlException {
        NodeList nodes = this.element.getElementsByTagName(name);
        ArrayList<XmlObject> results = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                results.add(new XmlObject(this.doc, (Element) node));
            } else {
                throw new XmlException("A node with name '" + name + "' cannot be parsed to an XmlObject!");
            }
        }
        return results;
    }

    /**
     * Add an attribute to this object with a unique name and a value.
     *
     * @param name  a String with the unique name of the attribute.
     * @param value a String with the value of the attribute.
     * @throws XmlException if the name contains whitespaces.
     */
    public void addAttribute(String name, String value) throws XmlException {
        if (name.contains(" ")) {
            throw new XmlException("The name '" + name + "' of an attribute cannot contain whitespaces!");
        }
        Attr attribute = doc.createAttribute(name);
        attribute.setValue(value);
        element.setAttributeNode(attribute);
    }

    /**
     * Returns the value of an attribute of this object.
     *
     * @param name a String with the unique name of the attribute.
     * @return a String with the value of the attribute.
     * @throws XmlException if the attribute is empty or does not exist, or the name contains whitespaces.
     */
    public String getAttribute(String name) throws XmlException {
        if (name.contains(" ")) {
            throw new XmlException("The name '" + name + "' of an attribute cannot contain whitespaces!");
        }
        String value = this.element.getAttribute(name);
        if (value.isEmpty()) {
            throw new XmlException("The value of '" + name + "' is empty or does not exist!");
        }
        else {
            return value;
        }
    }

    /**
     * Add a text attribute to this object with a name and a value.
     *
     * @param name  a String with the name of the text attribute.
     * @param value a String with the value of the text attribute.
     * @throws XmlException if the name contains whitespaces.
     */
    public void addText(String name, String value) throws XmlException {
        if (name.contains(" ")) {
            throw new XmlException("The name '" + name + "' of a text attribute cannot contain whitespaces!");
        }
        Element element = this.doc.createElement(name);
        this.element.appendChild(element);
        element.appendChild(this.doc.createTextNode(value));
    }

    /**
     * Returns the value of a text attribute of this object.
     *
     * @param name a String with the name of the text attribute.
     * @return a String with the value of the text attribute.
     * @throws XmlException if the text attribute cannot be handled of the name contains whitespaces.
     */
    public List<String> getTexts(String name) throws XmlException {
        if (name.contains(" ")) {
            throw new XmlException("The name '" + name + "' of a text attribute cannot contain whitespaces!");
        }
        NodeList nodes = this.element.getElementsByTagName(name);
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            try {
                results.add(node.getTextContent());
            } catch (DOMException e) {
                throw new XmlException("DOMException: " + e.getMessage());
            }
        }
        return results;
    }

}
