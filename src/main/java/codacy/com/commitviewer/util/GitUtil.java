package codacy.com.commitviewer.util;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.domain.CommitAttribute;
import codacy.com.commitviewer.domain.commit.User;
import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
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
    private static final String GIT_CLONE_BASE_CMD = "git clone %s %s";
    private static final String MKDIR_BASE_CMD = "mkdir %s";
    private static final String GIT_HUB_URL_BASE = "https://github.com/%s/%s";
    private static final String GIT_LOG_PRETTY_FORMAT_BASE_CMD = "--pretty=format:%s";
    private static final String COMMIT_FIELD_DELIMITER = ",";
    private static final String GIT_LOG_PRETTY_FORMAT_DEFAULT_PATTERN =
            "%s" + COMMIT_FIELD_DELIMITER +
            "%s" + COMMIT_FIELD_DELIMITER +
            "%s" + COMMIT_FIELD_DELIMITER +
            "%s" + COMMIT_FIELD_DELIMITER +
            "%s";

    public List<String> getRawCommitData(String execDirectory, List<CommitOption> commitOptionList) throws ProcessExecuteFailedException {
        ProcessBuilder builder = initialiseGitLogProcessBuilder(getValidDirectory(execDirectory), commitOptionList);
        return processExecutor(builder);
    }

    public List<String> processExecutor(ProcessBuilder builder) throws ProcessExecuteFailedException {
        List<String> rawDataStringList = new ArrayList<>();
        List<String> rawErrorStringList = new ArrayList<>();

        try {
            Process process = builder.start();

            BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader processErrorOutput = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = processOutput.readLine()) != null) {
                rawDataStringList.add(line);
            }

            String errorLine;
            while ((errorLine = processErrorOutput.readLine()) != null) {
                rawErrorStringList.add(errorLine.trim());
            }

            int exitCode = process.waitFor();

            if (!executedSuccessfully(exitCode)) {
                String error = buildErrorStringFromStream(rawErrorStringList);
                throw new ProcessExecuteFailedException("The process started but was unable to exit successfully (exit " +
                        "code: " + exitCode + ". Error occurred during the process: " + error);
            }

            log.info("Process executed successfully.");
            process.destroy();

        } catch (Exception e) {
            // Thrown by opening the directory specified
            if (e instanceof IOException) {
                log.error("Unable to access the execution directory specified (" + builder.directory().getName() +
                        "), ensure the directory exits and is accessible. Error: " + e.getMessage());

                //TODO: write exception in GlobalException handler?
                throw new ProcessExecuteFailedException("IO_Exception");
            }

            // Thrown by 'Process.waitFor()'
            else if (e instanceof InterruptedException) {
                log.error("The current thread is interrupted by another thread while the bash command runs. Error: " +
                        e.getMessage());
                throw new ProcessExecuteFailedException("InterruptedException");

            } else if (e instanceof ProcessExecuteFailedException) {
                log.error("Unable to exit the process. Error: " + e.getMessage());
                throw new ProcessExecuteFailedException("ProcessExecuteFailedException");
            } else {
                log.error("Unable to execute the git CLI command. Error: " + e.getMessage() + ". Skipping...");
                throw new ProcessExecuteFailedException("Generic");
            }
        }
        return rawDataStringList;
    }

    public String buildErrorStringFromStream(List<String> rawErrorStringList) {
        StringBuilder errorStringBuilder = new StringBuilder();

        for (String errorString : rawErrorStringList) {
            errorStringBuilder.append(errorString);
            errorStringBuilder.append(" ");
        }

        return errorStringBuilder.toString();
    }

    //TODO: expand this to populate other fields of GitCommit
    public List<GitCommit> buildCommitList(List<String> rawCommitData) {
        List<GitCommit> commitList = new ArrayList<>();
        for (String commitData : rawCommitData) {
            // SHA | author name | author email | author date | message
            String[] commitDataFields = commitData.split(COMMIT_FIELD_DELIMITER);
            assert commitDataFields.length == 5;

            String sha = commitDataFields[0];
            String authorName = commitDataFields[1];
            String authorEmail = commitDataFields[2];
            String authorDate = commitDataFields[3];
            String message = commitDataFields[4];

            User author = User.builder()
                    .name(authorName)
                    .email(authorEmail)
                    .date(authorDate)
                    .build();

            Commit commit = Commit.builder()
                    .author(author)
                    .message(message)
                    .build();

            GitCommit gitCommit =
                    GitCommit.builder()
                            .sha(sha)
                            .commit(commit)
                            .build();

            commitList.add(gitCommit);
        }

        return commitList;
    }

    ProcessBuilder initialiseGitLogProcessBuilder(String execDirectory, List<CommitOption> commitOptionList) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        builder.directory(new File(execDirectory));
        builder.command(buildGitLogCommandList(commitOptionList));
        return builder;
    }

    ProcessBuilder initialiseGitCloneProcessBuilder(String execDirectory, String gitUrl, String localProjectDirectory) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        builder.directory(new File(execDirectory));
        builder.command(buildGitCloneCommandList(gitUrl, localProjectDirectory));
        return builder;
    }

    List<String> buildGitLogCommandList(List<CommitOption> commitOptionList) {
        List<String> commandList = new ArrayList<>(Arrays.asList(GIT_LOG_BASE_CMD.split(" ")));
        StringBuilder commitOptions = new StringBuilder();

        if (commitOptionList == null || commitOptionList.isEmpty()) {
            log.info("No commit option specified, querying default commit data (sha, author name, author " +
                    "email, author date and message)");
            commitOptions.append(buildDefaultCommitOptionString());

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

            commitOptions.append(String.join(COMMIT_FIELD_DELIMITER, commandOptionStringList));
        }
        String commitOptionString = String.format(GIT_LOG_PRETTY_FORMAT_BASE_CMD, commitOptions.toString());
        commandList.add(commitOptionString);

        return commandList;
    }

    List<String> buildGitCloneCommandList(String gitUrl, String localProjectDirectory) {
        log.info("Cloning from '" + gitUrl + "' into " + localProjectDirectory + "...");
        return Arrays.asList(String.format(GIT_CLONE_BASE_CMD, gitUrl, localProjectDirectory).split(" "));
    }

    List<String> buildMkdirCommandList(String localDirectory) {
        log.info("Making a new directory '" + localDirectory + "'");
        return Arrays.asList(String.format(MKDIR_BASE_CMD, localDirectory).split(" "));
    }

    String buildDefaultCommitOptionString() {
        return String.format(GIT_LOG_PRETTY_FORMAT_DEFAULT_PATTERN,
                COMMIT_OPTION_TAG_MAP.get(CommitOption.SHA),
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
        map.put(CommitOption.MESSAGE, "%s");
        map.put(CommitOption.TREE_SHA, "%T"); // tree.url is remote url + tree.sha
        map.put(CommitOption.PARENT_SHA, "%P"); // parent.url is remote url + parent.sha

        //TODO: might need to parse the validation message to extract out individual field values
        map.put(CommitOption.VALIDATION, "%GG");

        return map;
    }

    public void cloneRemoteProject(String execDirectory, String projectOwner, String projectName) throws ProcessExecuteFailedException {
        String localGitProjectDirectory = getValidDirectory(execDirectory) + File.separator + projectName;
        String gitUrl = String.format(GIT_HUB_URL_BASE, projectOwner, projectName);

        if(new File(localGitProjectDirectory).mkdir()){
            ProcessBuilder processBuilder = initialiseGitCloneProcessBuilder(getValidDirectory(execDirectory), gitUrl, localGitProjectDirectory);
            processExecutor(processBuilder);
        } else {
            throw new ProcessExecuteFailedException("Unable to create new directory '" +  localGitProjectDirectory +
                    "'. Ensure it does not exist.");
        }

    }

    public String getValidDirectory(String execDirectory){
        if(execDirectory == null || execDirectory.isEmpty()) {
            String currentProjectDir = System.getProperty("user.dir");
            log.info("No work directory specified, using current project directory '" + currentProjectDir + "'");
            return currentProjectDir;
        }
        return execDirectory;
    }
}
