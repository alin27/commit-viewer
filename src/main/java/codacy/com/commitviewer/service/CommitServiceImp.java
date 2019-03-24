package codacy.com.commitviewer.service;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.util.GitUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommitServiceImp implements CommitService {

    @Override
    public Commit createCommit(final List<String> rawCommitData) {
        //TODO: expand to handle commit logs that returns different length
        return null;
    }

    @Override
    public List<String> getRawCommitData(final String exeDirectory) {
        return null;
    }

    @Override
    public List<Commit> createCommitList(final List<String> commitOptionList) {
        for (String commitOption : commitOptionList) {

        }


        return null;
    }
}
