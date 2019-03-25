package codacy.com.commitviewer.controller;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.GetCommitsRequest;
import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@AllArgsConstructor
public class CommitViewerController {
    /**
     * Autowired {@link Project} service.
     **/
    private final ProjectService projectService;

    /**
     * Retrieve a list of {@link Project} stored in the database via a GET request.
     *
     * @return ResponseEntity with HTTP status code and list of {@link Project} in the response body
     */
    @GetMapping("/projects")
    public ResponseEntity getAllProjects() {
        return new ResponseEntity<>(projectService.getAllProjects(), HttpStatus.OK);
    }

    /**
     * Retrieve a list of {@link GitCommit} of a project via a POST request with a payload
     * {@link GetCommitsRequest}. This endpoint should be used if the user wants to get list of commits using the git
     * CLI (git log), of a project that exists in the database, or of a new project which will be cloned first before
     * generating the {@link GitCommit} list.
     *
     * @return ResponseEntity with HTTP status code and list of {@link Commit} in the response body
     */
    @PostMapping("/commits")
    public ResponseEntity getCommitsFromLocalProject(@Valid @RequestBody final GetCommitsRequest request) {
        try {
            return new ResponseEntity<>(projectService.getAllCommitsFromLocal(request), HttpStatus.OK);
        } catch (final Exception e) {
            log.error("Unable to get list of commits via the endpoint '/commits'.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieve a list of {@link GitCommit} of a project via a POST request with a payload
     * {@link GetCommitsRequest}. This endpoint should be used if the user wants to get the commits using the git API
     * (V3, GET /repos/:owner/:repo/commits). If the request times out or the server responds with error status, it will
     * try
     *
     * @return ResponseEntity with HTTP status code and list of {@link GitCommit} in the response body
     */
    @PostMapping("/commits-remote")
    public ResponseEntity getCommitsFromRemoteProject(@Valid @RequestBody final GetCommitsRequest request) {
        try {
            return new ResponseEntity<>(projectService.getAllCommitsFromGit(request), HttpStatus.OK);
        } catch (final Exception e) {
            log.error("Unable to get list of commits via the endpoint '/commits-remote'.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete all {@link Project} stored in the database, via a DELETE request.
     *
     * @return ResponseEntity with HTTP status code
     */
    @DeleteMapping("/projects")
    public ResponseEntity deleteAllProjects() {
        projectService.deleteAllProjects();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
