package codacy.com.commitviewer.util;

/**
 * List of available attributes that a user can query using git CLI.
 *
 * Author: Amy Lin
 */
public enum CommitOption {
    SHA, AUTHOR_DATE, AUTHOR_NAME, AUTHOR_EMAIL, COMMITTER_DATE, COMMITTER_NAME, COMMITTER_EMAIL, MESSAGE, TREE_SHA,
    PARENT_SHA, VALIDATION
}
