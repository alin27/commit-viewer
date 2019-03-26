package codacy.com.commitviewer.service;

import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
import codacy.com.commitviewer.util.GitUtil;

import java.io.File;

/**
 * Service class providing interface to use {@link GitUtil} to run git CLI.
 *
 * Author: Amy Lin
 */
public interface GitService {

    /**
     * Use the git CLI to clone a remote git project directory into the specified work directory.
     *
     * @param projectOwner String owner of the project
     * @param projectName String name of the project
     * @retunr File of the local project directory cloned
     */
    File clone(String projectOwner, String projectName) throws ProcessExecuteFailedException;


}
