package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Commit;
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
    public Commit buildCommit(final List<String> rawCommitData) {
        //TODO: expand to handle commit logs that returns different length
        return null;
    }


    @Override
    public List<String> getRawCommitData(final String execDirectory, List<CommitOption> commitOptionList) {
        List<String> rawCommitData = new ArrayList<>();

        try {
            rawCommitData = GitUtil.getRawCommitData(execDirectory, commitOptionList);

        } catch (Exception e) {

        }

        return rawCommitData;
    }

    @Override
    public List<Commit> buildCommitList(final String execDirectory, final List<CommitOption> commitOptionList) {
        List<Commit> commitList = new ArrayList<>();

        try {
            commitList = GitUtil.buildCommitList(getRawCommitData(execDirectory, commitOptionList));
        } catch (Exception e) {

        }
        return commitList;
    }
}
