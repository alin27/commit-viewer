package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
import codacy.com.commitviewer.util.CommitOption;
import codacy.com.commitviewer.util.GitUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class CommitServiceImp implements CommitService {

    @Override
    public List<String> getRawCommitData(final File execDirectoryFile, List<CommitOption> commitOptionList) throws ProcessExecuteFailedException {
        return GitUtil.getRawCommitData(execDirectoryFile, commitOptionList);
    }

    @Override
    public List<GitCommit> buildCommitList(final File execDirectoryFile, final List<CommitOption> commitOptionList) throws ProcessExecuteFailedException {
        return GitUtil.buildCommitList(getRawCommitData(execDirectoryFile, commitOptionList));
    }
}
