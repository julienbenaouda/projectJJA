/**
 * 
 */
package taskman;

/**
 * This class represents an XmlException. It is thrown when an exception occurs when importing or exporting data to XML.
 * @author Julien Benaouda
 *
 */
public class XmlException extends RuntimeException {
	public XmlException(String message)
	{
		super(message);
	}
}
