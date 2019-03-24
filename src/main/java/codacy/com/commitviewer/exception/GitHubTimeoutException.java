package codacy.com.commitviewer.exception;

/**
 * Standard exception thrown when no existing git project is found in the database.
 * <p>
 * Author: Amy Lin
 **/
public class GitHubTimeoutException extends Exception {

    public GitHubTimeoutException(final String errorMessage) {
        super(errorMessage);
    }
}
