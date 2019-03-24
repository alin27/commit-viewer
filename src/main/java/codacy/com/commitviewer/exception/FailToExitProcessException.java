package codacy.com.commitviewer.exception;

/**
 * Standard exception thrown when no existing git project is found in the database.
 * <p>
 * Author: Amy Lin
 **/
public class FailToExitProcessException extends Exception {

    public FailToExitProcessException(final String errorMessage) {
        super(errorMessage);
    }
}
