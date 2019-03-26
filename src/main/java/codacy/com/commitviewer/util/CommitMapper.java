package codacy.com.commitviewer.util;

import codacy.com.commitviewer.domain.GitCommit;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * Utility class that performs the mapping between the response body returned by git get commits API and list of POJO
 * {@link GitCommit}.
 *
 * Author: Amy Lin
 */
@Slf4j
@UtilityClass
public class CommitMapper {
    public List<GitCommit> map(String responseBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, new TypeReference<List<GitCommit>>(){});
    }
}
