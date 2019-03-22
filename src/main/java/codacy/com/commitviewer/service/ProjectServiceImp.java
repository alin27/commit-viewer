package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.exception.ProjectNotFoundException;
import codacy.com.commitviewer.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectServiceImp implements ProjectService {

    /**
     * Autowired data repository.
     **/
    private final ProjectRepository repository;

    @Override
    public Project createProject(final Project project) {
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
}
