package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Error;
import codacy.com.commitviewer.domain.GetCommitsRequest;
import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@Slf4j
public class ProjectServiceTest {

    @MockBean
    private ProjectRepository projectRepository;

    private ProjectService projectService = new ProjectServiceImp(projectRepository, new CommitServiceImp(), new GitServiceImp());

    @Test
    public void testRemote() throws Error{
        GetCommitsRequest request = new GetCommitsRequest();
        request.setProjectName("node-github-url-from-git");
        request.setProjectOwner("tj");

        List<GitCommit> commitList = projectService.getAllCommitsFromGit(request);
       log.warn("Commit List: " + commitList);
    }

    @Test
    public void testLocal() throws Error {
        GetCommitsRequest request = new GetCommitsRequest();
        request.setProjectName("node-github-url-from-git");
        request.setProjectOwner("tj");

        List<GitCommit> commitList = projectService.getAllCommitsFromLocal(request);
        log.warn("Commit List: " + commitList);
    }
}
