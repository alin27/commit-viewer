package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.GetCommitsRequest;
import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.domain.Error;
import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
import codacy.com.commitviewer.exception.ProjectNotFoundException;
import codacy.com.commitviewer.repository.ProjectRepository;
import codacy.com.commitviewer.util.CommitMapper;
import codacy.com.commitviewer.util.CommitOption;
import codacy.com.commitviewer.util.GitUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProjectServiceImp implements ProjectService {

    /**
     * Autowired data repository.
     **/
    private final ProjectRepository repository;

    /**
     * Autowired commit service.
     **/
    private final CommitService commitService;

    /**
     * Autowired commit service.
     **/
    private final GitService gitService;

    /**
     * Git API end point base URL for getting commits of a repository. See https://developer.github.com/v3/repos/commits/.
     */
    private static final String GIT_API_GET_COMMITS_BASE_URL = "https://api.github.com/repos/%s/%s/commits";

    @Override
    public Project createProject(final Project projectData) {
        final Project project = Project.builder()
                .id(projectData.getId())
                .name(projectData.getName())
                .owner(projectData.getOwner())
                .commitList(projectData.getCommitList())
                .build();
        return repository.save(project);
    }

    @Override
    public Project getProjectByName(final String name) throws ProjectNotFoundException {
        final Optional<Project> gitRepository = repository.findByName(name);
        if (gitRepository.isPresent()) {
            return gitRepository.get();
        } else {
            throw new ProjectNotFoundException("Git project with name '" + name + "' is not found in the database.");
        }
    }

    @Override
    public List<Project> getAllProjects() {
        return repository.findAll();
    }

    @Override
    public void deleteAllProjects() {
        repository.deleteAll();
    }

    @Override
    public List<GitCommit> getAllCommitsByProjectName(final String name) throws ProjectNotFoundException {
        final Optional<Project> gitRepository = repository.findByName(name);
        if (gitRepository.isPresent()) {
            return gitRepository.get().getCommitList();
        } else {
            throw new ProjectNotFoundException("Git project with name '" + name + "' is not found in the database.");
        }
    }

    @Override
    public List<GitCommit> getAllCommitsFromLocal(final GetCommitsRequest request) throws Error {
        String projectName = request.getProjectName();
        String projectOwner = request.getProjectOwner();
        String execDirectory = GitUtil.getValidDirectory(request.getExecDirectory());
        List<CommitOption> commitOptionList = request.getCommitOptions();

        //TODO: Write a scheduler to fetch and loop through all records in DB to get the latest commits from git
        try {
            return getAllCommitsByProjectName(request.getProjectName());
        } catch (ProjectNotFoundException e) {
            log.info("Project does not exist in the database, cloning from remote");
            if (execDirectory != null) {
                log.info("Work directory specified, cloning into '" + execDirectory + "'");
            }

            try {
                gitService.clone(execDirectory, projectOwner, projectName);
                String localGitProjectDirectory = execDirectory + File.separator + projectName;

                List<GitCommit> commitList = commitService.buildCommitList(localGitProjectDirectory, commitOptionList);

                Project newProject = Project.builder()
                        .name(projectName)
                        .owner(projectOwner)
                        .commitList(commitList)
                        .build();

                createProject(newProject);

                return commitList;
            } catch (ProcessExecuteFailedException error) {
                throw new Error(Error.ErrorReason.PROCESS_EXECUTION_EXCEPTION);
            }
        }
    }

    @Override
    public List<GitCommit> getAllCommitsFromGit(final GetCommitsRequest request) throws Error {
        String urlString = String.format(GIT_API_GET_COMMITS_BASE_URL, request.getProjectOwner(), request.getProjectName());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(urlString, String.class);

        if (response.getStatusCode().isError() || response.getStatusCode() == HttpStatus.REQUEST_TIMEOUT) {
            String errorMessage = "Unable to fetch from git hub, trying to retrieve commit list locally...";
            log.error(errorMessage);
            return getAllCommitsFromLocal(request);
        }

        try {
            return CommitMapper.map(response.getBody());
        } catch (IOException e){
            log.error("Unable to parse the response from git API.");
            throw new Error(Error.ErrorReason.GIT_API_MALFORMED_RESPONSE);
        }
    }
}