package codacy.com.commitviewer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

/**
 * POJO representing a git project. When parsed as request body in post request, the user may optionally include a list
 * of commit options for querying specific information about the commits. If the option is present, it will be queried
 * when the git CLI is run.
 *
 * An example of 'commitOptions': [ sha, url, author.email, author.date, committer.name, title, message...].
 *
 * The available options are:
 *
 * sha
 * author.date
 * author.name
 * author.email
 * committer.date
 * committer.name
 * committer.email
 * message
 * tree.sha
 * parents.sha
 * validation.verified
 * validation.reason
 * validation.signature
 * validation.payload
 *
 * Author: Amy Lin
 **/
@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    @Id
    private String id;
    private String owner;
    private String name;
    private String url;
    private List<Commit> commitList;
    private List<String> commitOptions;
}
