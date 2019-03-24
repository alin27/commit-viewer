package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.Project;
import codacy.com.commitviewer.util.CommitOption;

import java.util.List;

public interface CommitService {

    /**
     * Create a {@link Commit} object based on list of raw commit data.
     *
     * @param rawCommitData List of Strings output by git CLI that represent a git commit
     * @return {@link Project} with unique ID to be created in the database
     */
    Commit buildCommit(List<String> rawCommitData);

    /**
     * Use the git CLI to get a list of commits in a git project directory.
     *
     * @param exeDirectory     String path of to local git project directory
     * @param commitOptionList (Optional) List of {@link CommitOption} to get specific details of commits
     * @return List of Strings output by git CLI that represents list of commits
     */
    List<String> getRawCommitData(String exeDirectory, List<CommitOption> commitOptionList);


    /**
     * Create a list of
     *
     * @return List of {@link Commit} of a git project
     */
    List<Commit> buildCommitList(String exeDirectory, List<CommitOption> commitOptionList);
}
