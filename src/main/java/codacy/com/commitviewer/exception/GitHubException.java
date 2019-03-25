package codacy.com.commitviewer.exception;

/**
 * Standard exception thrown when no existing git project is found in the database.
 * <p>
 * Author: Amy Lin
 **/
public class GitHubException extends Exception {

    public GitHubException(final String errorMessage) {
        super(errorMessage);
    }
}
