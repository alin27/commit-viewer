package codacy.com.commitviewer.domain;

import codacy.com.commitviewer.domain.commit.Parent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitCommit {
    private String sha;
    private Commit commit;
    private List<Parent> parents;
}