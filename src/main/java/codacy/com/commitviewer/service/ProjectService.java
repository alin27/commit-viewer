package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.GetCommitsRequest;
import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.exception.GitHubException;
import codacy.com.commitviewer.exception.ProjectNotFoundException;
import java.util.List;

public interface ProjectService {

    /**
     * Create a new {@link Project} with list of {@link GitCommit}.
     *
     * @param project Git project data received from the git cli
     * @return {@link Project} with unique ID to be created in the database
     */
    Project createProject(Project project);

    /**
     * Retrieve a git project by name from the database.
     *
     * @param name String name of the {@link Project}
     * @return {@link Project} with that name
     */
    Project getProjectByName(String name) throws ProjectNotFoundException;

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

    /**
     * Retrieve a list of {@link GitCommit} of a project from the database.
     *
     * @param name String name of the {@link Project}
     * @return List of {@link GitCommit} of the project
     */
    List<GitCommit> getAllCommitsByProjectName(String name) throws ProjectNotFoundException;

    /**
     * Use the git CLI to get list of {@link GitCommit} of a repository locally. It first checks if there is a database
     * record of the project using getAllCommitsByProjectName (i.e. existing project). If no database record is found
     * it will clone the remote project into the work directory (if specified, default to the current project directory
     * otherwise) and get the commit list from the newly cloned project directory. Finally it create a new
     * {@link Project} the project in the database.
     *
     * @param request GetCommitRequest containing the git url, optionally list of commit options and work directory
     * @return List of commits
     */
    List<GitCommit> getAllCommitsFromLocal(GetCommitsRequest request);

    /**
     * Retrieve a list of {@link GitCommit} of a project from git. If the response status is error or the request times
     * out, retry with getAllCommitsFromLocal.
     *
     * @param request GetCommitRequest containing the git url, optionally list of commit options and work directory
     * @return List of {@link GitCommit} of the project
     */
    List<GitCommit> getAllCommitsFromGit(GetCommitsRequest request) throws Error;

}
