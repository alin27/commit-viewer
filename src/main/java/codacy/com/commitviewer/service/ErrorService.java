package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Service class that map any error captured into a {@link ResponseEntity} with {@link HttpStatus}.
 *
 * Author: Amy Lin
 */
public interface ErrorService {
    ResponseEntity constructErrorResponseView(Error e);
}
