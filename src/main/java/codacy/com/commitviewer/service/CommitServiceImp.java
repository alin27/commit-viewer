package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
import codacy.com.commitviewer.util.CommitOption;
import codacy.com.commitviewer.util.GitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class CommitServiceImp implements CommitService {

    @Override
    public List<String> getRawCommitData(final String execDirectory, List<CommitOption> commitOptionList) throws ProcessExecuteFailedException {
        return GitUtil.getRawCommitData(execDirectory, commitOptionList);
    }

    @Override
    public List<GitCommit> buildCommitList(final String execDirectory, final List<CommitOption> commitOptionList) throws ProcessExecuteFailedException {
        return GitUtil.buildCommitList(getRawCommitData(execDirectory, commitOptionList));
    }
}
