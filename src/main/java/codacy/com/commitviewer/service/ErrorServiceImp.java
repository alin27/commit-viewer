package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ErrorServiceImp implements ErrorService{

    @Override
    public ResponseEntity constructErrorResponseView(Error e) {
        switch (e.getErrorReason()) {
            case GIT_API_MALFORMED_RESPONSE:
            case GIT_API_EXCEPTION:
                return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
            case INVALID_PROCESS_EXECUTION_DIRECTORY:
            case CREATE_DIRECTORY_EXCEPTION:
            case PROCESS_EXECUTION_EXCEPTION:
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
