package codacy.com.commitviewer.service;

import codacy.com.commitviewer.util.CommitOption;
import java.util.List;

public interface GitService {

    /**
     * Use the git CLI to get a list of commits in a git project directory.
     *
     * @param execDirectory     String path of to local git project directory
     * @param commitOptionList (Optional) List of {@link CommitOption} to get specific details of commits
     * @return List of Strings output by git CLI that represents list of commits
     */
    List<String> getRawCommitData(String execDirectory, List<CommitOption> commitOptionList);

    /**
     * Use the git CLI to clone a remote git project directory into the specified work directory.
     *
     * @param execDirectory     String path of to local git project directory
     * @param gitUrl String remote git project url
     */
    void clone(String execDirectory, String gitUrl);


}
