package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Error;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

@Slf4j
public class ErrorServiceTest {

    private ErrorService errorService = new ErrorServiceImp();

    @Test
    public void shouldReturnBadRequestWhenErrorIsCreateDirectoryException(){
        Error e = new Error(Error.ErrorReason.CREATE_DIRECTORY_EXCEPTION);
        ResponseEntity response = errorService.constructErrorResponseView(e);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldReturnBadRequestWhenErrorIsProcessExecutionException(){
        Error e = new Error(Error.ErrorReason.PROCESS_EXECUTION_EXCEPTION);
        ResponseEntity response = errorService.constructErrorResponseView(e);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldReturnBadRequestWhenErrorIsInvalidProcessExecutionDirectory(){
        Error e = new Error(Error.ErrorReason.INVALID_PROCESS_EXECUTION_DIRECTORY);
        ResponseEntity response = errorService.constructErrorResponseView(e);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldReturnServiceNotAvailableWhenErrorIsGitApiException(){
        Error e = new Error(Error.ErrorReason.GIT_API_EXCEPTION);
        ResponseEntity response = errorService.constructErrorResponseView(e);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    public void shouldReturnServiceNotAvailableWhenErrorIsGitApiMalformedResponse(){
        Error e = new Error(Error.ErrorReason.GIT_API_MALFORMED_RESPONSE);
        ResponseEntity response = errorService.constructErrorResponseView(e);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }
}
