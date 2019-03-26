package codacy.com.commitviewer.service;

import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
import codacy.com.commitviewer.util.GitUtil;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GitServiceImp implements GitService {
    @Override
    public File clone(final String projectOwner, final String projectName) throws ProcessExecuteFailedException {
        return GitUtil.cloneRemoteProject(projectOwner, projectName);
    }
}
