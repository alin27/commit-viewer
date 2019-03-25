package codacy.com.commitviewer.util;

import codacy.com.commitviewer.domain.GitCommit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class CommitMapper {
    public List<GitCommit> map(String responseBody){
        ObjectMapper objectMapper = new ObjectMapper();
        List<GitCommit> commitList = new ArrayList<>();
        try {
            commitList = objectMapper.readValue(responseBody, new TypeReference<List<GitCommit>>(){});
        } catch (IOException e) {
            //TODO: exception handler
            e.printStackTrace();
        }

        return commitList;
    }
}
