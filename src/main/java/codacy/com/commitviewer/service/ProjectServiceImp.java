package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.exception.GitHubTimeoutException;
import codacy.com.commitviewer.exception.ProjectNotFoundException;
import codacy.com.commitviewer.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
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

    @Override
    public Project createProject(final Project projectData) {
        List<Commit> commitList = commitService.buildCommitList(projectData.getDirectory(),
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
    public List<Commit> getAllCommitsByProjectName(final String name) throws ProjectNotFoundException {
        final Optional<Project> gitRepository = repository.findByName(name);
        if (gitRepository.isPresent()) {
            return gitRepository.get().getCommitList();
        } else {
            throw new ProjectNotFoundException("Git project with name '" + name + "' is not found");
        }
    }

    @Override
    public List<Commit> getAllCommitsByProjectNameFromGit(final String owner, final String name) throws ProjectNotFoundException {
        int connectionTimeout = 5000;
//        String urlString = String.format("https://github.com/%s/%s", owner, name);
        String urlString = String.format("https://api.github.com/users/%s/%s", owner, name);

        try {
            StringBuilder response = new StringBuilder();

            HttpURLConnection con = (HttpURLConnection) new URL(urlString).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/vnd.github.v3+json");
            con.setConnectTimeout(connectionTimeout);
            con.setReadTimeout(connectionTimeout);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {

                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                    response.append(System.lineSeparator());
                }
            }

            if (con.getResponseCode() == HttpStatus.REQUEST_TIMEOUT.value()) {
                throw new GitHubTimeoutException("The request has timeout");
            }

            System.out.println("Response: " + response);
            con.disconnect();

        } catch (Exception e) {
            if (e instanceof GitHubTimeoutException) {
                log.warn("Unable to complete the request to git hub in specified time");
            } else if (e instanceof ProtocolException | e instanceof MalformedURLException | e instanceof IOException) {
                log.error("Unable to connect to git hub. Error: " + e.getMessage());
            }
        }

        return new ArrayList<>();
//        return getAllCommitsByProjectName(name);

//    } catch (Exception e) {
//            /* If (request takes > 5 mins)
//                try {
//                    return query DB;
//                } catch exception {
//                    if (ProjectNotFound) {
//                        throw (project not found);
//                    }
//                }
//
//                else if ( 404 ) {
//                    throw (Git endpoint not found exception);
//                }
//
//                else( any other git API error) {
//                    throw (Git API not available exception);
//                }
//            }
//
//             */
//
//
//        }
//    }
    }
}
