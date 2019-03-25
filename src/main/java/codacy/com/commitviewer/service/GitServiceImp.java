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
public class GitServiceImp implements GitService {

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
    public void clone(final String execDirectory, final String gitUrl) {
        log.info("Cloning from '" + gitUrl + "' into " + execDirectory + "...");
        try {
            GitUtil.cloneRemoteProject(execDirectory, gitUrl);
        } catch (Exception e){

        }
    }

}
