package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.GetCommitsRequest;
import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.exception.GitHubException;
import codacy.com.commitviewer.exception.GitHubTimeoutException;
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

    private static final int TIMEOUT_IN_MILLIESECONDS = 5000;
    private static final String GIT_HUB_URL_BASE = "https://github.com/%s/%s";

    @Override
    public Project createProject(final Project projectData) {
        List<GitCommit> commitList = commitService.buildCommitList(projectData.getDirectory(),
                projectData.getCommitOptionList());
        final Project project = Project.builder()
                .id(projectData.getId())
                .name(projectData.getName())
                .owner(projectData.getOwner())
                .commitList(commitList)
                .build();
        return repository.save(project);
    }

    @Override
    public Project getProjectById(final String id) throws ProjectNotFoundException {
        final Optional<Project> gitRepository = repository.findById(id);
        if (gitRepository.isPresent()) {
            return gitRepository.get();
        } else {
            throw new ProjectNotFoundException("Git project with id '" + id + "' is not found");
        }
    }

    @Override
    public void deleteProjectById(final String id) {
        repository.deleteById(id);
    }

    @Override
    public Project getProjectByName(final String name) throws ProjectNotFoundException {
        final Optional<Project> gitRepository = repository.findByName(name);
        if (gitRepository.isPresent()) {
            return gitRepository.get();
        } else {
            throw new ProjectNotFoundException("Git project with name '" + name + "' is not found");
        }
    }

    @Override
    public List<Project> getProjectsByOwner(final String owner) {
        return repository.findAllByOwner(owner);
    }

    @Override
    public Project updateProject(final Project project) {
        return repository.save(project);
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
            throw new ProjectNotFoundException("Git project with name '" + name + "' is not found");
        }
    }

    /**
     * This is the one used to get local git commits. If no database record is found it will clone the remote project
     * into the specified work directory, get the commit list from that directory and store it in the database.
     *
     * @param request GetCommitRequest containing the git url, optionally list of commit options and work directory
     * @return List of commits
     */
    @Override
    public List<GitCommit> getAllCommitsFromLocal(final GetCommitsRequest request) {
        String projectName = request.getProjectName();
        String projectOwner = request.getProjectOwner();
        String execDirectory = request.getExecDirectory();
        List<CommitOption> commitOptionList = request.getCommitOptions();

        try{
            return getAllCommitsByProjectName(request.getProjectName());
        } catch (ProjectNotFoundException e) {
            log.info("Project does not exist in the database, cloning from remote");

            String gitUrl = String.format(GIT_HUB_URL_BASE, projectOwner, projectName);

            gitService.clone(execDirectory, gitUrl);

            if(execDirectory != null ){
                log.info("Work directory specified, cloning into '" + execDirectory + "'");
            }

            List<GitCommit> commitList = commitService.buildCommitList(execDirectory, commitOptionList);

            Project newProject = Project.builder()
                    .name(projectName)
                    .owner(projectOwner)
                    .commitList(commitList)
                    .build();

            createProject(newProject);

            return commitList;
        }
    }

    /**
     * This is the one used to get remote git commits
     * @param request GetCommitRequest containing the git url, optionally list of commit options and work directory
     * @return List of commits
     */
    @Override
    public List<GitCommit> getAllCommitsFromGit(final GetCommitsRequest request) throws GitHubException, GitHubTimeoutException {
        String urlString = String.format("https://api.github.com/repos/%s/%s/commits", request.getProjectOwner(), request.getProjectName());
//        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(urlString, String.class);

        if(response.getStatusCode().isError()){
            throw new GitHubException("Unable to fetch from git hub");
        }

        // Time out
        else if (response.getStatusCode().value() == HttpStatus.REQUEST_TIMEOUT.value()){
            throw new GitHubTimeoutException("Request timeout");
        }

        return CommitMapper.map(response.getBody());
    }

//    private ClientHttpRequestFactory getClientHttpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        clientHttpRequestFactory.setConnectTimeout(TIMEOUT_IN_MILLIESECONDS);
//        return clientHttpRequestFactory;
//    }
}