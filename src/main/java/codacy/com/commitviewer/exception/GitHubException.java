package codacy.com.commitviewer.exception;

import codacy.com.commitviewer.domain.Error;

/**
 * Standard exception thrown when no existing git project is found in the database.
 * <p>
 * Author: Amy Lin
 **/
public class GitHubException extends Exception {

    public GitHubException(final String errorMessage) {
        super(errorMessage);
    }
    private Error.ErrorReason errorReason;
    private String message;

    public GitHubException(final Error.ErrorReason errorReason, final String message) {
        this.errorReason = errorReason;
        this.message = message;
    }

}
