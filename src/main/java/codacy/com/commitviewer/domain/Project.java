package codacy.com.commitviewer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * POJO representing a git project used to store list of {@link GitCommit} in the database.
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
    private List<GitCommit> commitList;
}
