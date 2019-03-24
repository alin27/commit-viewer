package codacy.com.commitviewer.util;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.CommitAttribute;
import codacy.com.commitviewer.domain.commit.User;
import codacy.com.commitviewer.exception.FailToExitProcessException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class GitUtil {

    private static final Map<CommitOption, String> COMMIT_OPTION_TAG_MAP = buildCommitOptionTagMap();
    private static final String GIT_LOG_BASE_CMD = "git log";
    private static final String GIT_LOG_PRETTY_FORMAT_BASE_CMD = "--pretty=format:\"%s\"";
    private static final String COMMIT_FIELD_DELIMITER = "|";

    public List<String> getRawCommitData(String execDirectory, List<CommitOption> commitOptionList) {
        List<String> rawCommitDataStringList = new ArrayList<>();
        List<String> rawErrorStringList = new ArrayList<>();

        try {
            ProcessBuilder builder = initialiseProcessBuilder(new File(execDirectory), commitOptionList);
            Process process = builder.start();

            BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader processErrorOutput = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = processOutput.readLine()) != null) {
                rawCommitDataStringList.add(line);
            }

            String errorLine;
            while ((errorLine = processErrorOutput.readLine()) != null) {
                rawErrorStringList.add(errorLine.trim());
            }

            int exitCode = process.waitFor();

            if (!executedSuccessfully(exitCode)) {
                String error = buildErrorStringFromStream(rawErrorStringList);
                throw new FailToExitProcessException("The process started but was unable to exit successfully (exit " +
                        "code: " + exitCode + ". Error occured during the process: " + error);
            }

            process.destroy();

        } catch (Exception e) {
            // Thrown by opening the directory specified
            if (e instanceof IOException) {
                log.error("Unable to access the execution directory specified (" + execDirectory + "), ensure the " +
                        "directory exits and is accessible. Error: " + e.getMessage());
            }

            // Thrown by 'Process.waitFor()'
            else if (e instanceof InterruptedException) {
                log.error("The current thread is interrupted by another thread while the bash command runs. Error: " +
                        e.getMessage());

            } else if (e instanceof FailToExitProcessException) {
                log.error("Unable to exit the process. Error: " + e.getMessage());
            } else {
                log.error("Unable to execute the git CLI command. Error: " + e.getMessage() + ". Skipping...");
            }
        }

        return rawCommitDataStringList;
    }

    public String buildErrorStringFromStream(List<String> rawErrorStringList) {
        StringBuilder errorStringBuilder = new StringBuilder();

        for (String errorString : rawErrorStringList) {
            errorStringBuilder.append(errorString);
            errorStringBuilder.append(" ");
        }

        return errorStringBuilder.toString();
    }

    //TODO: expand this to populate other fields of Commit
    public List<Commit> buildCommitList(List<String> rawCommitData) {
        List<Commit> commitList = new ArrayList<>();
        for (String commitData : rawCommitData) {
            // SHA | author name | author email | author date | message
            String[] commitDataFields = commitData.split(COMMIT_FIELD_DELIMITER);
            assert commitDataFields.length == 4;

            CommitAttribute authorName = CommitAttribute
                    .builder()
                    .value(commitDataFields[1])
                    .build();
            CommitAttribute authorEmail = CommitAttribute
                    .builder()
                    .value(commitDataFields[2])
                    .build();
            CommitAttribute authorDate = CommitAttribute
                    .builder()
                    .value(commitDataFields[3])
                    .build();
            CommitAttribute sha = CommitAttribute
                    .builder()
                    .value(commitDataFields[0])
                    .build();
            CommitAttribute message = CommitAttribute
                    .builder()
                    .value(commitDataFields[5])
                    .build();

            User author = User.builder()
                    .name(authorName)
                    .email(authorEmail)
                    .date(authorDate)
                    .build();

            Commit commit =
                    Commit.builder()
                            .sha(sha)
                            .author(author)
                            .message(message)
                            .build();

            commitList.add(commit);
        }

        return commitList;
    }

    ProcessBuilder initialiseProcessBuilder(File execDirectoryFile, List<CommitOption> commitOptionList) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        builder.directory(execDirectoryFile);
        builder.command(buildGitCommandList(commitOptionList));
        return builder;
    }

    List<String> buildGitCommandList(List<CommitOption> commitOptionList) {
        List<String> commandList = new ArrayList<>(Arrays.asList(GIT_LOG_BASE_CMD.split(" ")));
        String commitOptions = "";

        if (commitOptionList == null || commitOptionList.isEmpty()) {
            log.info("No commit option specified, querying default commit data (sha, author name, author " +
                    "email, author date and message)");
            commitOptions = buildDefaultCommitOptionString();

        } else {
            List<String> commandOptionStringList = new ArrayList<>();
            for (CommitOption commitOption : commitOptionList) {
                if (COMMIT_OPTION_TAG_MAP.containsKey(commitOption)) {
                    commandOptionStringList.add(COMMIT_OPTION_TAG_MAP.get(commitOption));
                } else {
                    log.warn("Commit option specified '" + commitOption + "' does not match any valid tag that can be " +
                            "used by git log --pretty=format. Skipping...");
                }
            }

            commitOptions = String.join(COMMIT_FIELD_DELIMITER, commandOptionStringList);
        }
        String commitOptionString = String.format(GIT_LOG_PRETTY_FORMAT_BASE_CMD, commitOptions);
        commandList.add(commitOptionString);

        return commandList;
    }

    String buildDefaultCommitOptionString() {
        return String.format("%s|%s|%s|%s|%s", COMMIT_OPTION_TAG_MAP.get(CommitOption.SHA),
                COMMIT_OPTION_TAG_MAP.get(CommitOption.AUTHOR_NAME),
                COMMIT_OPTION_TAG_MAP.get(CommitOption.AUTHOR_EMAIL),
                COMMIT_OPTION_TAG_MAP.get(CommitOption.AUTHOR_DATE),
                COMMIT_OPTION_TAG_MAP.get(CommitOption.MESSAGE));
    }

    boolean executedSuccessfully(int exitCode) {
        return exitCode == 0;
    }

    Map<CommitOption, CommitAttribute> buildCommitOptionMapFromCommitOptionList(List<CommitOption> commitOptionList) {
        Map<CommitOption, CommitAttribute> map = new HashMap<>();

        for (CommitOption commitOption : commitOptionList) {
            CommitAttribute attribute = CommitAttribute
                    .builder()
                    .cliTag(COMMIT_OPTION_TAG_MAP.get(commitOption))
                    .build();

            map.put(commitOption, attribute);
        }

        return map;
    }

    Map<CommitOption, String> buildCommitOptionTagMap() {
        Map<CommitOption, String> map = new HashMap<>();
        map.put(CommitOption.SHA, "%H");
        map.put(CommitOption.AUTHOR_DATE, "%at"); // UNIX timestamp
        map.put(CommitOption.AUTHOR_NAME, "%an");
        map.put(CommitOption.AUTHOR_EMAIL, "%ae");
        map.put(CommitOption.COMMITTER_DATE, "%ct"); // UNIX timestamp
        map.put(CommitOption.COMMITTER_NAME, "%cn");
        map.put(CommitOption.COMMITTER_EMAIL, "%ce");
        map.put(CommitOption.MESSAGE, "%N");
        map.put(CommitOption.TREE_SHA, "%T"); // tree.url is remote url + tree.sha
        map.put(CommitOption.PARENT_SHA, "%P"); // parent.url is remote url + parent.sha

        //TODO: might need to parse the validation message to extract out individual field values
        map.put(CommitOption.VALIDATION, "%GG");

        return map;
    }
}
