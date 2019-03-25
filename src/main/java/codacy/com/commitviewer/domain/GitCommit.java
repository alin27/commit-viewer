package codacy.com.commitviewer.domain;

import codacy.com.commitviewer.domain.commit.Parent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * POJO representing the a git commit object returned by the git API, as defined in
 * (https://developer.github.com/v3/repos/commits/)
 *
 * Author: Amy Lin
 **/
@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitCommit {
    private String sha;
    private Commit commit;
    private List<Parent> parents;
}