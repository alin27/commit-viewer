package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
import codacy.com.commitviewer.util.CommitOption;

import java.util.List;

public interface CommitService {

    /**
     * Use the git CLI to get a list of commits in a git project directory.
     *
     * @param exeDirectory     String path of to local git project directory
     * @param commitOptionList (Optional) List of {@link CommitOption} to get specific details of commits
     * @return List of Strings output by git CLI that represents list of commits
     * @throws ProcessExecuteFailedException
     */
    List<String> getRawCommitData(String exeDirectory, List<CommitOption> commitOptionList) throws ProcessExecuteFailedException;


    /**
     * Create a list of commits based on list of raw commit data.
     *
     * @param exeDirectory
     * @param commitOptionList
     * @return List of GitCommit mapped from raw commit data strings
     * @throws ProcessExecuteFailedException
     */
    List<GitCommit> buildCommitList(String exeDirectory, List<CommitOption> commitOptionList) throws ProcessExecuteFailedException;
}
