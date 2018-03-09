/**
 * 
 */
package taskman;

/**
 * This class represents an XMLParserException. It is thrown when an exception occurs when importing or exporting data to XML.
 * @author Julien Benaouda
 *
 */
public class XMLParserException extends RuntimeException {
	public XMLParserException(String message)
	{
		super(message);
	}
}
