package codacy.com.commitviewer.service;

import codacy.com.commitviewer.util.GitUtil;

/**
 * Service class providing interface to use {@link GitUtil} to run git CLI.
 *
 * Author: Amy Lin
 */
public interface GitService {

    /**
     * Use the git CLI to clone a remote git project directory into the specified work directory.
     *
     * @param execDirectory     String path of to local git project directory
     * @param projectOwner String owner of the project
     * @param projectName String name of the project
     */
    void clone(String execDirectory, String projectOwner, String projectName);


}
