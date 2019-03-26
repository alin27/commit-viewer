package codacy.com.commitviewer.util;

import codacy.com.commitviewer.domain.GitCommit;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommitMapperTest {

    @Test
    public void shouldMapCorrectlyWithValidResponseBody() throws IOException {
        String responseBody = FileUtil.getResourceAsString("mockGitCommitResponseBody.json");
        List<GitCommit> commitList = CommitMapper.map(responseBody);
        assertEquals(2, commitList.size());

        GitCommit commit1 = commitList.get(0);
        assertEquals("9b857238a71afa32ad27b6dc61a2d22902fb1f72", commit1.getSha());
        assertEquals("Amy Lin", commit1.getCommit().getAuthor().getName());
        assertEquals("amy.lin@nzqa.govt.nz", commit1.getCommit().getAuthor().getEmail());
        assertEquals("Remove parameter 'AvailabilityZones' when deploying into existing template."
                , commit1.getCommit().getMessage());

        GitCommit commit2 = commitList.get(1);
        assertEquals("32eb4b7f1dda64f7bb38567332977629b1a722f2", commit2.getSha());
        assertEquals("Amy Lin", commit2.getCommit().getAuthor().getName());
        assertEquals("amy.lin@nzqa.govt.nz", commit2.getCommit().getAuthor().getEmail());
        assertEquals("Use array list to generate parameter arrays rather than Arrays.AsList, " +
                "which generates a fixed array that can't be modified.", commit2.getCommit().getMessage());
    }

    @Test
    public void shouldReturnEmptyListWhenResponseIsEmpty() throws IOException {
        String responseBody = "[]";
        List<GitCommit> commitList = CommitMapper.map(responseBody);
        assertEquals(0, commitList.size());
    }
}
