package codacy.com.commitviewer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error extends RuntimeException {
    private ErrorReason errorReason;

    public enum ErrorReason {
        GIT_API_EXCEPTION,
        GIT_API_MALFORMED_RESPONSE,
        INVALID_PROCESS_EXECUTION_DIRECTORY,
        INVALID_GIT_URL,
        CREATE_DIRECTORY_EXCEPTION,
        PROCESS_EXECUTION_EXCEPTION,
    }
}
