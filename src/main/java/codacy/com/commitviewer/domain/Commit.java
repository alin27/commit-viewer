package codacy.com.commitviewer.domain;

import codacy.com.commitviewer.domain.commit.Validation;
import codacy.com.commitviewer.domain.commit.User;
import codacy.com.commitviewer.domain.commit.Tree;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * POJO representing the 'commit' attribute of a {@link GitCommit}
 *
 * Author: Amy Lin
 **/
@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {
    private String url;
    private User author;
    private User committer;
    private String message;
    @JsonProperty("comment_count")
    private String commentCount;
    private Tree tree;
    private Validation validation;
}
