package codacy.com.commitviewer.util;

import codacy.com.commitviewer.domain.Commit;
import codacy.com.commitviewer.domain.GitCommit;
import codacy.com.commitviewer.domain.commit.User;
import codacy.com.commitviewer.exception.ProcessExecuteFailedException;
import codacy.com.commitviewer.service.CommitService;
import codacy.com.commitviewer.service.ProjectService;
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

    /**
     * Map for looking up {@link CommitOption} to their corresponding flag. See https://git-scm.com/docs/git-log.
     */
    private static final Map<CommitOption, String> COMMIT_OPTION_TAG_MAP = buildCommitOptionTagMap();

    /**
     * List of base git commands and parameters, tags are replaced with string values when called in functions.
     */
    private static final String GIT_LOG_BASE_CMD = "git log";
    private static final String GIT_CLONE_BASE_CMD = "git clone %s %s";
    private static final String GIT_HUB_URL_BASE = "https://github.com/%s/%s";
    private static final String GIT_LOG_PRETTY_FORMAT_BASE_CMD = "--pretty=format:%s";
    private static final String COMMIT_FIELD_DELIMITER = ",";
    private static final String GIT_LOG_PRETTY_FORMAT_DEFAULT_PATTERN =
            "%s" + COMMIT_FIELD_DELIMITER +
                    "%s" + COMMIT_FIELD_DELIMITER +
                    "%s" + COMMIT_FIELD_DELIMITER +
                    "%s" + COMMIT_FIELD_DELIMITER +
                    "%s";

    /**
     * Used by {@link CommitService} to get a list of raw commit data as string. The function takes a list of
     * {@link CommitOption} and a work directory to use the git CLI to get the list of commits as string.
     *
     * @param execDirectory    String a valid directory that either points to an existing local git project or the
     *                         destination of where to clone the remote git project. Default to current work directory if
     *                         not specified.
     * @param commitOptionList List of {@link CommitOption} used with git CLI to query specific information about the
     *                         commits. Default to query sha, author name, author email, author date and message if not
     *                         specified.
     * @return processExecutor with the process builder as an input and execute the process.
     * @throws ProcessExecuteFailedException
     */
    public List<String> getRawCommitData(String execDirectory, List<CommitOption> commitOptionList) throws ProcessExecuteFailedException {
        ProcessBuilder builder = initialiseGitLogProcessBuilder(getValidDirectory(execDirectory), commitOptionList);
        return processExecutor(builder);
    }

    /**
     * Execute the process defined by the {@link ProcessBuilder}.
     *
     * @param builder Initialised ProcessBuilder for executing a process
     * @return List of strings representing the output of executing the process
     * @throws ProcessExecuteFailedException
     */
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

    /**
     * Builds output error messages from the process if the process encounters any error as a string.
     *
     * @param rawErrorStringList List of error strings captured while the process executes.
     * @return The error message as string.
     */
    public String buildErrorStringFromStream(List<String> rawErrorStringList) {
        StringBuilder errorStringBuilder = new StringBuilder();

        for (String errorString : rawErrorStringList) {
            errorStringBuilder.append(errorString);
            errorStringBuilder.append(" ");
        }

        return errorStringBuilder.toString();
    }

    /**
     * Used by {@link CommitService} to process output from 'getRawCommitData' and map it to a list of {@link GitCommit}.
     *
     * @param rawCommitData List of strings representing raw commit data
     * @return List of GitCommit
     */
    //TODO: expand this to map other fields of GitCommit
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

    /**
     * Initialise the process builder for getting the commits using git CLI.
     *
     * @param execDirectory String where the process should be executed / location of the local git project.
     * @param commitOptionList Optional list of CommandOption for querying specific info of commits
     * @return ProcessBuilder for executing git log command
     */
    ProcessBuilder initialiseGitLogProcessBuilder(String execDirectory, List<CommitOption> commitOptionList) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        builder.directory(new File(execDirectory));
        builder.command(buildGitLogCommandList(commitOptionList));
        return builder;
    }

    /**
     * Initialise the process builder for cloning the remote project using git CLI.
     *
     * @param execDirectory String where the process should be executed
     * @param gitUrl String git URL of the project
     * @param localProjectDirectory String destination directory to clone the project into
     * @return ProcessBuilder for executing git clone command
     */
    ProcessBuilder initialiseGitCloneProcessBuilder(String execDirectory, String gitUrl, String localProjectDirectory) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        builder.directory(new File(execDirectory));
        builder.command(buildGitCloneCommandList(gitUrl, localProjectDirectory));
        return builder;
    }

    /**
     * Performs string substitution on GIT_LOG_PRETTY_FORMAT_BASE_CMD and builds the commands as list to be passed into
     * {@link ProcessBuilder}.
     *
     * @return List of strings representing 'git log --pretty=format:<commit options>'.
     */
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

    /**
     * Performs string substitution on GIT_CLONE_BASE_CMD and builds the commands as list to be passed into
     * {@link ProcessBuilder}.
     *
     * @return List of strings representing 'git clone <git url> <destination directory>'.
     */
    List<String> buildGitCloneCommandList(String gitUrl, String localProjectDirectory) {
        log.info("Cloning from '" + gitUrl + "' into " + localProjectDirectory + "...");
        return Arrays.asList(String.format(GIT_CLONE_BASE_CMD, gitUrl, localProjectDirectory).split(" "));
    }

    /**
     * Performs string subsitution on GIT_LOG_PRETTY_FORMAT_DEFAUL_PATTERN and builds format options to be used by
     * 'git log --pretty=format: < >' as string.
     *
     * @return String of format options to be substituted by 'buildGitLogCommandList'.
     */
    String buildDefaultCommitOptionString() {
        return String.format(GIT_LOG_PRETTY_FORMAT_DEFAULT_PATTERN,
                COMMIT_OPTION_TAG_MAP.get(CommitOption.SHA),
                COMMIT_OPTION_TAG_MAP.get(CommitOption.AUTHOR_NAME),
                COMMIT_OPTION_TAG_MAP.get(CommitOption.AUTHOR_EMAIL),
                COMMIT_OPTION_TAG_MAP.get(CommitOption.AUTHOR_DATE),
                COMMIT_OPTION_TAG_MAP.get(CommitOption.MESSAGE));
    }

    /**
     * Checks a process executes and exits successfully.
     *
     * @param exitCode int exit code of a process
     * @return boolean if the exit code is 0
     */
    boolean executedSuccessfully(int exitCode) {
        return exitCode == 0;
    }

    /**
     * Linear map used for looking up {@link CommitOption} and tags used to query them using git CLI. See
     * https://git-scm.com/docs/git-log.
     *
     * @return Map of CommitOption and their tags
     */
    //TODO: expand this to keep track of order of fields and
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
        map.put(CommitOption.VALIDATION, "%GG");

        return map;
    }

    /**
     * Used by {@link ProjectService} to clone a remote git project. The function takes a valid work directory and
     * creates an empty directory, then construct the git project URL based on the project owner and project name
     * and finally clones the remote project into the newly created directory using git CLI.
     *
     * @param execDirectory String a valid directory that points to the destination of where to clone the remote git
     *                      project
     * @param projectOwner  String name of git project owner
     * @param projectName   String name of git project
     * @throws ProcessExecuteFailedException
     */
    public void cloneRemoteProject(String execDirectory, String projectOwner, String projectName) throws ProcessExecuteFailedException {
        String localGitProjectDirectory = getValidDirectory(execDirectory) + File.separator + projectName;
        String gitUrl = String.format(GIT_HUB_URL_BASE, projectOwner, projectName);

        if (new File(localGitProjectDirectory).mkdir()) {
            ProcessBuilder processBuilder = initialiseGitCloneProcessBuilder(getValidDirectory(execDirectory), gitUrl, localGitProjectDirectory);
            processExecutor(processBuilder);
        } else {
            throw new ProcessExecuteFailedException("Unable to create new directory '" + localGitProjectDirectory +
                    "'. Ensure it does not exist.");
        }

    }

    /**
     * Checks if the work directory passed in is valid. If it is an empty string or null, the function will return the
     * current directory as a string.
     *
     * @param execDirectory String a valid directory that either points to an existing local git project or the
     *                      destination of where to clone the remote git project.
     * @return String either the execDirectory if it is not null or empty, or the current project directory as string
     */
    public String getValidDirectory(String execDirectory) {
        if (execDirectory == null || execDirectory.isEmpty()) {
            String currentProjectDir = System.getProperty("user.dir");
            log.info("No work directory specified, using current project directory '" + currentProjectDir + "'");
            return currentProjectDir;
        }
        return execDirectory;
    }
}
