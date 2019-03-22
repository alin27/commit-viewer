package codacy.com.commitviewer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * POJO representing a git commit.
 * <p>
 * Author: Amy Lin
 **/
@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitCommit {

    private String sha;
    private String url;
    private GitUser author;
    private GitUser committer;
    private String message;
    private GitTree tree;
    private List<GitTree> parents;
}
