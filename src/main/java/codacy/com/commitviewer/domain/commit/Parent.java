package codacy.com.commitviewer.domain.commit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO representing the 'author' and 'committer' attribute of a git commit.
 * <p>
 * Author: Amy Lin
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parent {
    private String sha;
    private String url;

}
