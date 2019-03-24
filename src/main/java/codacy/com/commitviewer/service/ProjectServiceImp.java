package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.exception.ProjectNotFoundException;
import codacy.com.commitviewer.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

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
        List<Commit> commitList = commitService.createCommitList(projectData.getCommitOptions());
        final Project project = Project.builder()
                .id(projectData.getId())
                .name(projectData.getName())
                .owner(projectData.getOwner())
                .url(projectData.getUrl())
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
    public List<Commit> getAllCommitsByProjectUrl(final String url) throws ProjectNotFoundException {
        try {
            // TODO: try querying the Git URL first
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
        } catch (Exception e) {
            /* If (request takes > 5 mins)
                try {
                    return query DB;
                } catch exception {
                    if (ProjectNotFound) {
                        throw (project not found);
                    }
                }

                else if ( 404 ) {
                    throw (Git endpoint not found exception);
                }

                else( any other git API error) {
                    throw (Git API not available exception);
                }
            }

             */


        }
        final Optional<Project> gitRepository = repository.findByUrl(url);
        if (gitRepository.isPresent()) {
            return gitRepository.get().getCommitList();
        } else {
            throw new ProjectNotFoundException("Git project with url '" + url + "' is not found");
        }
    }
}
