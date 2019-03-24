package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.Project;

import java.util.List;

public interface CommitService {

    /**
     * Create a {@link Commit} object based on list of raw commit data.
     * @param rawCommitData List of Strings output by git CLI that represent a git commit
     * @return {@link Project} with unique ID to be created in the database
     */
    Commit createCommit(List<String> rawCommitData);

    /**
     * Use the git CLI to get a list of commits in a git project directory.
     *
     * @param exeDirectory String path of to local git project directory
     * @return List of Strings output by git CLI that represents list of commits
     */
    List<String> getRawCommitData(String exeDirectory);


    /**
     * Create a list of
     * @return List of {@link Commit} of a git project
     */
    List<Commit> createCommitList(List<String> commitOptionList);
}
