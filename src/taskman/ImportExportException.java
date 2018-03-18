package taskman;

/**
 * This class represents an ImportExportException. It is thrown when an exception occurs when importing or exporting data to a file.
 *
 * @author Julien Benaouda, Alexander Braekevelt
 */
public class ImportExportException extends Exception {

    /**
     * Instantiates a new ImportExportException.
     * @param message the message
     */
    public ImportExportException(String message) {
		super(message);
	}

}