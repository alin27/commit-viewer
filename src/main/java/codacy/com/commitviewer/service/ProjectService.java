package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.exception.ProjectNotFoundException;

import java.util.List;

public interface ProjectService {

    /**
     * @param gitProject Git project data received from the git cli
     * @return {@link Project} with unique ID to be created in the database
     */
    Project createProject(Project gitProject);

    /**
     * Retrieve a git project by ID from the database.
     *
     * @param id String unique ID of the {@link Project}
     * @return {@link Project} with that record ID
     */
    Project getProjectById(String id) throws ProjectNotFoundException;

    /**
     * Delete a git project by ID from the database.
     *
     * @param id String unique ID of the {@link Project}
     */
    void deleteProjectById(String id);

    /**
     * Retrieve a git project by name from the database.
     *
     * @param name String name of the {@link Project}
     * @return {@link Project} with that name
     */
    Project getProjectByName(String name) throws ProjectNotFoundException;

    /**
     * Retrieve a list of git projects by owner from the database.
     *
     * @param owner String owner of the {@link Project}
     * @return List of {@link Project} belonged to that owner
     */
    List<Project> getProjectsByOwner(String owner);


    /** Update an existing git repository with new information. In NoSQL this operation is equivalent to 'createProject'.
     *
     * @param gitProject Project
     * @return {@link Project} with unique ID to be created in the database
     */
    Project updateProject(Project gitProject);

    /**
     * Retrieve a list of {@link Project} existing git repositories from the database.
     *
     * @return List of {@link Project}
     */
    List<Project> getAllProjects();

    /**
     * Delete all {@link Project} stored in the database.
     */
    void deleteAllProjects();
}
