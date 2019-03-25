package codacy.com.commitviewer.domain;

import codacy.com.commitviewer.util.CommitOption;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * POJO representing a git project. When parsed as request body in post request, the user may optionally include a list
 * of commit options for querying specific information about the commits. If the option is present, it will be queried
 * when the git CLI is run. See {@link CommitOption} for the full list of available options.
 *
 * An example of {@link CommitOption}: [ SHA, URL, AUTHOR_EMAIL, AUTHOR_DATE, COMMITTER_NAME, MESSAGE...].
 *
 * Author: Amy Lin
 **/
@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDTO {
    @Id
    private String id;
    private String owner;
    private String name;
    private String directory;
    private List<CommitDTO> commitList;
    private List<CommitOption> commitOptionList;
}
