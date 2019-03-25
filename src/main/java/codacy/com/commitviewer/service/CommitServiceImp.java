package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
import codacy.com.commitviewer.util.CommitOption;
import codacy.com.commitviewer.util.GitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommitServiceImp implements CommitService {

    @Override
    public GitCommit buildCommit(final List<String> rawCommitData) {
        //TODO: expand to handle commit logs that returns different length
        return null;
    }


    @Override
    public List<String> getRawCommitData(final String execDirectory, List<CommitOption> commitOptionList) {
        List<String> rawCommitData = new ArrayList<>();

        try {
            rawCommitData = GitUtil.getRawCommitData(execDirectory, commitOptionList);

        } catch (ProcessExecuteFailedException e) {

        }

        return rawCommitData;
    }

    @Override
    public List<GitCommit> buildCommitList(final String execDirectory, final List<CommitOption> commitOptionList) {
        List<GitCommit> commitList = new ArrayList<>();

        try {
            commitList = GitUtil.buildCommitList(getRawCommitData(execDirectory, commitOptionList));
        } catch (Exception e) {

        }
        return commitList;
    }
}
