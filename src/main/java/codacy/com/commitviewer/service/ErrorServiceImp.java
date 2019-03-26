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
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorReason(e.getErrorReason())
                .build();
        switch (e.getErrorReason()) {
            case GIT_API_MALFORMED_RESPONSE:
            case GIT_API_EXCEPTION:
                return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
            case INVALID_PROCESS_EXECUTION_DIRECTORY:
            case CREATE_DIRECTORY_EXCEPTION:
            case PROCESS_EXECUTION_EXCEPTION:
            case INVALID_GIT_URL:
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
