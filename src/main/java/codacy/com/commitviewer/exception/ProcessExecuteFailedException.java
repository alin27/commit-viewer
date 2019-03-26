package codacy.com.commitviewer.exception;

import codacy.com.commitviewer.domain.Error;

/**
 * Standard exception thrown when no existing git project is found in the database.
 * <p>
 * Author: Amy Lin
 **/
public class ProcessExecuteFailedException extends Exception {

    private Error.ErrorReason errorReason;
    private String message;

    public ProcessExecuteFailedException(final String errorMessage) {
        super(errorMessage);
    }
    public ProcessExecuteFailedException(final Error.ErrorReason errorReason, final String message) {
        this.errorReason = errorReason;
        this.message = message;
    }
}
