package codacy.com.commitviewer.domain;

import codacy.com.commitviewer.util.CommitOption;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GetCommitsRequest {
    @NotNull(message="Project owner cannot be missing or empty")
    private String projectOwner;
    @NotNull(message="Project name cannot be missing or empty")
    private String projectName;
    //TODO: validate work directory
    private String execDirectory;
    //TODO: validate commit option
    private List<CommitOption> commitOptions;
}
