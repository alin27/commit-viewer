package codacy.com.commitviewer.util;

import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * These tests cover the {@link GitUtil} implementation.
 * <p>
 * Author: Amy Lin
 **/
public class GitUtilTest {

    @Test
    public void exitSuccessfullyShouldReturnTrueWhenExitCodeIsZeroAndFalseOtherwise() {
        assertTrue(GitUtil.executedSuccessfully(0));
        assertFalse(GitUtil.executedSuccessfully(1));
    }

    @Test
    public void buildDefaultCommitOptionStringShouldReturnCorrectDefaultCommitOptionString(){
        // SHA | author name | author email | author date | message
        String expectedString = "%H,%an,%ae,%at,%N";
        assertEquals(expectedString, GitUtil.buildDefaultCommitOptionString());
    }

    @Test
    public void buildCommitOptionStringShouldBuildCorrectlyWhenOptionsArePresent(){
        // SHA | message
        List<String> expectedCommands = Arrays.asList("git", "log", "--pretty=format:%H,%N");
        List<CommitOption> commitOptionList = Arrays.asList(CommitOption.SHA, CommitOption.MESSAGE);
        assertEquals(3, GitUtil.buildGitLogCommandList(commitOptionList).size());
        assertEquals(expectedCommands, GitUtil.buildGitLogCommandList(commitOptionList));
    }

    @Test
    public void buildCommitOptionStringShouldBuildDefaultWhenNoOptionsArePresent(){
        List<String> expectedCommands = Arrays.asList("git", "log", "--pretty=format:%H,%an,%ae,%at,%s");
        List<CommitOption> commitOptionList = new ArrayList<>();
        assertEquals(3, GitUtil.buildGitLogCommandList(commitOptionList).size());
        assertEquals(expectedCommands, GitUtil.buildGitLogCommandList(commitOptionList));
    }

    @Test
    public void getRawCommitDataOnThisRepoShouldExecute() throws ProcessExecuteFailedException {
        List<String> rawCommitData = GitUtil.getRawCommitData("", null);
        assertFalse(rawCommitData.isEmpty());
    }

    @Test
    public void validRawCommitDataOnThisRepoShouldConvertedIntoGitCommits() throws ProcessExecuteFailedException {
        List<GitCommit> commitList = GitUtil.buildCommitList(GitUtil.getRawCommitData("", null));
        assertFalse(commitList.isEmpty());
    }

    //Build git clone command tests
    @Test
    public void buildGitCloneStringShouldBuildCorrectlyWhenUrlIsPresent(){
        List<String> expectedCommands = Arrays.asList("git", "clone", "https://github.com/owner/project", "/project");
        List<String> testCommands = GitUtil.buildGitCloneCommandList("https://github.com/owner/project", "/project");

        assertEquals(expectedCommands, testCommands);
    }

    @Test
    public void buildGitCloneStringShouldBuildCorrectlyWhenExecDirAndUrlIsPresent(){
        List<String> expectedCommands = Arrays.asList("git", "clone", "https://github.com/owner/project", "some-directory/project");
        List<String> testCommands = GitUtil.buildGitCloneCommandList("https://github.com/owner/project", "some-directory/project");

        assertEquals(expectedCommands, testCommands);
    }

    @Test
    public void buildMkdirStringShouldBuildCorrectly(){
        List<String> expectedCommands = Arrays.asList("mkdir", "/project");
        List<String> testCommands = GitUtil.buildMkdirCommandList("/project");

        assertEquals(expectedCommands, testCommands);
    }

    @Test
    public void test() throws ProcessExecuteFailedException {
        List<CommitOption> commandOptionList = Arrays.asList(CommitOption.SHA, CommitOption.AUTHOR_DATE);
        List<String> output = GitUtil.getRawCommitData("", commandOptionList);
        System.out.println("Output: " + output);
    }
}
