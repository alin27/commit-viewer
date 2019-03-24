package codacy.com.commitviewer.service;

import codacy.com.commitviewer.exception.ProjectNotFoundException;
import lombok.AllArgsConstructor;
import org.junit.Test;

import java.net.MalformedURLException;

@AllArgsConstructor
public class ProjectServiceImpTest {

    private static ProjectService projectService;


    @Test
    public void test() {
        try {
            projectService.getAllCommitsByProjectNameFromGit("tj", "node-github-url-from-git");
        } catch (ProjectNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
