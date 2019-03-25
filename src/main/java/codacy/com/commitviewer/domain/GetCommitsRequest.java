package codacy.com.commitviewer.domain;

import codacy.com.commitviewer.controller.CommitViewerController;
import codacy.com.commitviewer.util.CommitOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * POJO representing the request payload the user sends to the {@link CommitViewerController} to query list of commits.
 * It may optionally include -
 *      1. a list of {@link CommitOption} for querying specific information about the commits. If the
 *      option is present, it will be queried when the git CLI is run. See {@link CommitOption} for the full list of
 *      available options. An example of {@link CommitOption}: [ SHA, URL, AUTHOR_EMAIL, AUTHOR_DATE, MESSAGE...].
 *
 *      2. a work directory. This is where the project will be cloned into, or where the git CLI will be run. If it is
 *      not specified it will be defaulted to the current project directory.
 *
 *  Author: Amy Lin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
