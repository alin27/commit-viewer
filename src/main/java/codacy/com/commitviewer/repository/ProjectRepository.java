package codacy.com.commitviewer.repository;

import codacy.com.commitviewer.domain.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Standard database repository interface.
 * <p>
 * Author: Amy Lin
 **/
public interface ProjectRepository extends MongoRepository<Project, String> {

    List<Project> findAllByOwner(String owner);
    Optional<Project> findByName(String name);
    Optional<Project> findByUrl(String url);
}
