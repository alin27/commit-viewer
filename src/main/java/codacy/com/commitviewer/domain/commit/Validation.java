package codacy.com.commitviewer.domain.commit;

import codacy.com.commitviewer.domain.GitCommit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * POJO representing the 'validation' attribute of a {@link GitCommit}
 *
 * Author: Amy Lin
 **/
@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Validation {
    private String verified;
    private String reason;
    private String signature;
    private String payload;
}
