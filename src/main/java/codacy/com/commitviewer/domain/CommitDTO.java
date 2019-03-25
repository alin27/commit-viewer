package codacy.com.commitviewer.domain;

import codacy.com.commitviewer.domain.commit.Tree;
import codacy.com.commitviewer.domain.commit.User;
import codacy.com.commitviewer.domain.commit.Validation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * POJO representing a git commit. The structure is a mirror of a commit object returned by the git API
 * (https://developer.github.com/v3/git/commits/)
 * <p>
 * Author: Amy Lin
 **/
@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitDTO {

    private String sha;
    private String url;
    private User author;
    private User committer;
    private String message;
    private Tree tree;
    private List<Tree> parents;
    private Validation validation;
}
