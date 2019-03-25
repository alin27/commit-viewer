package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.GetCommitsRequest;
import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.exception.GitHubException;
import codacy.com.commitviewer.exception.GitHubTimeoutException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ProjectServiceImpTest {

    private ProjectService projectService;

    @Test
    public void testRemote() throws GitHubException, GitHubTimeoutException, IOException {
        GetCommitsRequest request = new GetCommitsRequest();
        request.setProjectName("node-github-url-from-git");
        request.setProjectOwner("tj");

        List<GitCommit> commitList = projectService.getAllCommitsFromGit(request);
       log.warn("Commit List: " + commitList);
    }

    @Test
    public void testLocal() throws GitHubException, GitHubTimeoutException, IOException {
        GetCommitsRequest request = new GetCommitsRequest();
        request.setProjectName("node-github-url-from-git");
        request.setProjectOwner("tj");

        List<GitCommit> commitList = projectService.getAllCommitsFromLocal(request);
        log.warn("Commit List: " + commitList);
    }
}
