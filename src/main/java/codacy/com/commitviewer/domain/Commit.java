package codacy.com.commitviewer.domain;

import codacy.com.commitviewer.domain.commit.Validation;
import codacy.com.commitviewer.domain.commit.User;
import codacy.com.commitviewer.domain.commit.Tree;
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
public class Commit {

    private CommitAttribute sha;
    private CommitAttribute url;
    private User author;
    private User committer;
    private CommitAttribute message;
    private Tree tree;
    private List<Tree> parents;
    private Validation validation;
}
