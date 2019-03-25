package codacy.com.commitviewer.domain.commit;

import codacy.com.commitviewer.domain.CommitAttribute;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * POJO representing the 'author' and 'committer' attribute of a git commit.
 * <p>
 * Author: Amy Lin
 **/
@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private String date;
    private String name;
    private String email;
}