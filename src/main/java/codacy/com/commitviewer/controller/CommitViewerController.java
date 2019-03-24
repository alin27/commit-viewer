package codacy.com.commitviewer.controller;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.exception.ProjectNotFoundException;
import codacy.com.commitviewer.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@AllArgsConstructor
public class CommitViewerController {
    // TODO: Provide Swagger doc for the controller

    /**
     * Autowired {@link Project} service.
     **/
    private final ProjectService projectService;

    /**
     * Retrieve a list of {@link Project} submitted between the last talk and the next talk, via a GET request.
     *
     * @return ResponseEntity with HTTP status code and list of {@link Project} in the response body
     */
    @GetMapping("/projects")
    public ResponseEntity getAllProjects() {
        return new ResponseEntity<>(projectService.getAllProjects(), HttpStatus.OK);
    }

    /**
     * Retrieve a single {@link Project} by its unique ID, via a GET request.
     *
     * @param id String unique ID assigned by the database upon record creation
     * @return ResponseEntity with HTTP status code and the {@link Project} that matches the ID in the response body
     */
    @GetMapping("/project/{id}")
    public ResponseEntity getProjectById(@PathVariable final String id) {
        try {
            return new ResponseEntity<>(projectService.getProjectById(id), HttpStatus.OK);
        } catch (final Exception e) {
            if (e instanceof ProjectNotFoundException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            else {
                // TODO: throw general run time error
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Retrieve a single {@link Project} by its git repository name, via a GET request.
     *
     * @param name String unique ID assigned by the database upon record creation
     * @return ResponseEntity with HTTP status code and the {@link Project} that matches the ID in the response body
     */
    @GetMapping("/project/{name}")
    public ResponseEntity getProjectByName(@PathVariable final String name) {
        try {
            return new ResponseEntity<>(projectService.getProjectByName(name), HttpStatus.OK);
        } catch (final Exception e) {
            if (e instanceof ProjectNotFoundException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            else {
                // TODO: throw general run time error
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Retrieve a list of {@link Commit} of a project by project name via a GET request.
     *
     * @return ResponseEntity with HTTP status code and list of {@link Commit} in the response body
     */
    @GetMapping("/project/{name}/commits")
    public ResponseEntity getCommitsByProjectName(@PathVariable final String name) {
        try {
            return new ResponseEntity<>(projectService.getAllCommitsByProjectName(name), HttpStatus.OK);
        } catch (final Exception e) {
            if (e instanceof ProjectNotFoundException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            /*
                else if ( 404 ) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                else( any other git API error) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }

             */
            else {
                // TODO: throw general run time error
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Retrieve a list of {@link Commit} of a project by project git url via a GET request.
     *
     * @return ResponseEntity with HTTP status code and list of {@link Commit} in the response body
     */
    @GetMapping("/project/{url}/commits")
    public ResponseEntity getCommitsByProjectUrl(@PathVariable final String url) {
        try {
            return new ResponseEntity<>(projectService.getAllCommitsByProjectUrl(url), HttpStatus.OK);
        } catch (final Exception e) {
            if (e instanceof ProjectNotFoundException) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            else {
                // TODO: throw general run time error
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    // TODO: finish code doc
    /**
     * Create a new {@link Project} in the database, via a POST request.
     *
     * @param project   Project payload received
     * @return ResponseEntity with HTTP status code and the {@link Project} with unique ID to be created in the database
     */
    @PostMapping("/project")
    public ResponseEntity createProject(@RequestBody final Project project) {
        return new ResponseEntity<>(projectService.createProject(project), HttpStatus.OK);
    }

    /**
     * Delete a {@link Project} by its unique ID, via a DELETE request.
     *
     * @param id String unique ID assigned by the database upon record creation
     * @return ResponseEntity with HTTP status code
     */
    @DeleteMapping("/project/{id}")
    public ResponseEntity deleteProject(@PathVariable final String id) {
        projectService.deleteProjectById(id);
        return new ResponseEntity<>(HttpStatus.OK);
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
