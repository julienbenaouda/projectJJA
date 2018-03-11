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
 * @author Julien Benaouda, Alexander Braekevelt
 */
public class XmlObject {

    private Document doc;
    private Element element;

    public XmlObject() {
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

    private XmlObject(Document doc, Element node) {
        this.doc = doc;
        this.element = node;
    }

    public static XmlObject importFrom(String path) {
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

    public void exportTo(String path) {
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

    public XmlObject addXmlObject(String name) {
        Element element = this.doc.createElement(name);
        this.element.appendChild(element);
        return new XmlObject(this.doc, element);
    }

    public List<XmlObject> getXmlObjects(String name) {
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

    public XmlObject addAttribute(String name, String value) {
        Attr attribute = doc.createAttribute(name);
        attribute.setValue(value);
        element.setAttributeNode(attribute);
        return new XmlObject(this.doc, element);
    }

    public String getAttribute(String name) {
        return this.element.getAttribute(name);
    }


    public XmlObject addText(String name, String value) {
        Attr attribute = doc.createAttribute(name);
        attribute.setValue(value);
        element.setAttributeNode(attribute);
        return new XmlObject(this.doc, element);
    }

    public List<String> getTexts(String name) {
        NodeList nodes = this.element.getElementsByTagName(name);
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            results.add(node.getTextContent());
        }
        return results;
    }

}
