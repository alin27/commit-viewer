package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Error;
import codacy.com.commitviewer.domain.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ErrorServiceImp implements ErrorService{

    @Override
    public ResponseEntity constructErrorResponseView(Error e) {
        ErrorResponse response = ErrorResponse.builder()
                .errorReason(e.getErrorReason())
                .message(e.getMessage()).build();
        switch (e.getErrorReason()) {
            case GIT_API_MALFORMED_RESPONSE:
            case GIT_API_EXCEPTION:
                return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
            case INVALID_PROCESS_EXECUTION_DIRECTORY:
            case CREATE_DIRECTORY_EXCEPTION:
            case PROCESS_EXECUTION_EXCEPTION:
            case INVALID_GIT_URL:
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
