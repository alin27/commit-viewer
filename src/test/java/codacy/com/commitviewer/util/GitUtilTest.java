package codacy.com.commitviewer.util;

import org.junit.Test;

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
        String expectedString = "%H|%an|%ae|%at|%N";
        assertEquals(expectedString, GitUtil.buildDefaultCommitOptionString());
    }

    @Test
    public void buildCommitOptionStringShouldBuildCorrectlyWhenOptionsArePresent(){
        List<String> expectedCommands = Arrays.asList("git", "log", "--pretty=format:\"%H|%N\"");
        List<CommitOption> commitOptionList = Arrays.asList(CommitOption.SHA, CommitOption.MESSAGE);
        assertEquals(3, GitUtil.buildCommandList(commitOptionList).size());
        assertEquals(expectedCommands, GitUtil.buildCommandList(commitOptionList));
    }

    @Test
    public void test(){
        List<CommitOption> commandOptionList = Arrays.asList(CommitOption.SHA, CommitOption.AUTHOR_DATE);
        List<String> output = GitUtil.getRawCommitData("/Users/amylin/Git/commit-viewer/src/test/java/codacy/com/commitviewer/util", commandOptionList);
        System.out.println("Output: " + output);
    }
}
