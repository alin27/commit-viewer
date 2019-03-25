package codacy.com.commitviewer.util;

import codacy.com.commitviewer.domain.GitCommit;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CommitMapperTest {

    @Test
    public void shouldMapCorrectlyWithValidResponseBody() {
        String responseBody = "[\n" +
                "    {\n" +
                "        \"sha\": \"486ed80c20edb9cc3cc8accd4c020daa8431e1d2\",\n" +
                "        \"node_id\": \"MDY6Q29tbWl0OTU1NjQ1MDo0ODZlZDgwYzIwZWRiOWNjM2NjOGFjY2Q0YzAyMGRhYTg0MzFlMWQy\",\n" +
                "        \"commit\": {\n" +
                "            \"author\": {\n" +
                "                \"name\": \"Benjamin Coe\",\n" +
                "                \"email\": \"ben@npmjs.com\",\n" +
                "                \"date\": \"2016-11-27T01:55:35Z\"\n" +
                "            },\n" +
                "            \"committer\": {\n" +
                "                \"name\": \"Benjamin Coe\",\n" +
                "                \"email\": \"ben@npmjs.com\",\n" +
                "                \"date\": \"2016-11-27T01:55:35Z\"\n" +
                "            },\n" +
                "            \"message\": \"chore(release): 1.5.0\",\n" +
                "            \"tree\": {\n" +
                "                \"sha\": \"418290a7e807dac03688147c3aa6fa0509c5d6a6\",\n" +
                "                \"url\": \"https://api.github.com/repos/tj/node-github-url-from-git/git/trees/418290a7e807dac03688147c3aa6fa0509c5d6a6\"\n" +
                "            },\n" +
                "            \"url\": \"https://api.github.com/repos/tj/node-github-url-from-git/git/commits/486ed80c20edb9cc3cc8accd4c020daa8431e1d2\",\n" +
                "            \"comment_count\": 0,\n" +
                "            \"verification\": {\n" +
                "                \"verified\": true,\n" +
                "                \"reason\": \"valid\",\n" +
                "                \"signature\": \"-----BEGIN PGP SIGNATURE-----\\nVersion: GnuPG v1\\n\\niQIcBAABAgAGBQJYOj0XAAoJEGCrHB1HjhKGwhcQAMdGFsIh8WJNoWZHIETEnY+D\\nToEeppqUBm8gnv29xxXTRcnuGDjeKVc9exfYPJ0wDAlJsOOsc7g9UTde+dlqJr6m\\n51QU6l40h8YsNVFd8PplE9ZoVOLecA9U3t0Ea3B0id4DR8JF40Lu5IsBusTxKCb9\\nTDe3+3gucey/JPUdSKBDMeIbuasYd1ACvL//A9DVl1XHtC7PMbyUhJYe1NS08wPw\\nyrORr8eb8oNwUXhw9AthKc6Cgy9JyTW+bzRCt5lRuZs9LgCfZfS8wX9Io1R/piW/\\n9Vh54MvX+N222o4EWNhk6iDpFeXvk6Iw4ZLedM6vWhzkxjxn7tIaMjueoBmErkIu\\nAmKiCt0+k9gNs2O+eJA912FmGueqsgpy5p2aR5OCt5kLuD9PnhhGthTXZPicUmIb\\nHVwbI6k8N6PnWG0KnhpdFI54A0jSr00L5LQI6vV3XDurV4OiC6A54OSFE40dAPcV\\nNwSjen0Gbc92XNm7znggUSi9+TklUoXYabx34Bk1KfEQsEDrnqR4RpXEOfYwovPv\\npsVnS7Ny7iZF/zj73mWiyV1sKn0K+Y2uLo/YnmasWGkuPFGPfPyM3u0y4L4RJi66\\nh60VWKzSZMX+enejesoMUb6tsuHOhUDa0TX6Wlvz+GDpd6DfpYRKmAlD+42oK1SA\\n2BQr25nUbOsb3aP8qyb3\\n=F94q\\n-----END PGP SIGNATURE-----\",\n" +
                "                \"payload\": \"tree 418290a7e807dac03688147c3aa6fa0509c5d6a6\\nparent 3c25da87dcfb99d06707ed8a3875b399a971620b\\nauthor Benjamin Coe <ben@npmjs.com> 1480211735 -0800\\ncommitter Benjamin Coe <ben@npmjs.com> 1480211735 -0800\\n\\nchore(release): 1.5.0\\n\"\n" +
                "            }\n" +
                "        },\n" +
                "        \"url\": \"https://api.github.com/repos/tj/node-github-url-from-git/commits/486ed80c20edb9cc3cc8accd4c020daa8431e1d2\",\n" +
                "        \"html_url\": \"https://github.com/tj/node-github-url-from-git/commit/486ed80c20edb9cc3cc8accd4c020daa8431e1d2\",\n" +
                "        \"comments_url\": \"https://api.github.com/repos/tj/node-github-url-from-git/commits/486ed80c20edb9cc3cc8accd4c020daa8431e1d2/comments\",\n" +
                "        \"author\": {\n" +
                "            \"login\": \"bcoe\",\n" +
                "            \"id\": 194609,\n" +
                "            \"node_id\": \"MDQ6VXNlcjE5NDYwOQ==\",\n" +
                "            \"avatar_url\": \"https://avatars3.githubusercontent.com/u/194609?v=4\",\n" +
                "            \"gravatar_id\": \"\",\n" +
                "            \"url\": \"https://api.github.com/users/bcoe\",\n" +
                "            \"html_url\": \"https://github.com/bcoe\",\n" +
                "            \"followers_url\": \"https://api.github.com/users/bcoe/followers\",\n" +
                "            \"following_url\": \"https://api.github.com/users/bcoe/following{/other_user}\",\n" +
                "            \"gists_url\": \"https://api.github.com/users/bcoe/gists{/gist_id}\",\n" +
                "            \"starred_url\": \"https://api.github.com/users/bcoe/starred{/owner}{/repo}\",\n" +
                "            \"subscriptions_url\": \"https://api.github.com/users/bcoe/subscriptions\",\n" +
                "            \"organizations_url\": \"https://api.github.com/users/bcoe/orgs\",\n" +
                "            \"repos_url\": \"https://api.github.com/users/bcoe/repos\",\n" +
                "            \"events_url\": \"https://api.github.com/users/bcoe/events{/privacy}\",\n" +
                "            \"received_events_url\": \"https://api.github.com/users/bcoe/received_events\",\n" +
                "            \"type\": \"User\",\n" +
                "            \"site_admin\": false\n" +
                "        },\n" +
                "        \"committer\": {\n" +
                "            \"login\": \"bcoe\",\n" +
                "            \"id\": 194609,\n" +
                "            \"node_id\": \"MDQ6VXNlcjE5NDYwOQ==\",\n" +
                "            \"avatar_url\": \"https://avatars3.githubusercontent.com/u/194609?v=4\",\n" +
                "            \"gravatar_id\": \"\",\n" +
                "            \"url\": \"https://api.github.com/users/bcoe\",\n" +
                "            \"html_url\": \"https://github.com/bcoe\",\n" +
                "            \"followers_url\": \"https://api.github.com/users/bcoe/followers\",\n" +
                "            \"following_url\": \"https://api.github.com/users/bcoe/following{/other_user}\",\n" +
                "            \"gists_url\": \"https://api.github.com/users/bcoe/gists{/gist_id}\",\n" +
                "            \"starred_url\": \"https://api.github.com/users/bcoe/starred{/owner}{/repo}\",\n" +
                "            \"subscriptions_url\": \"https://api.github.com/users/bcoe/subscriptions\",\n" +
                "            \"organizations_url\": \"https://api.github.com/users/bcoe/orgs\",\n" +
                "            \"repos_url\": \"https://api.github.com/users/bcoe/repos\",\n" +
                "            \"events_url\": \"https://api.github.com/users/bcoe/events{/privacy}\",\n" +
                "            \"received_events_url\": \"https://api.github.com/users/bcoe/received_events\",\n" +
                "            \"type\": \"User\",\n" +
                "            \"site_admin\": false\n" +
                "        },\n" +
                "        \"parents\": [\n" +
                "            {\n" +
                "                \"sha\": \"3c25da87dcfb99d06707ed8a3875b399a971620b\",\n" +
                "                \"url\": \"https://api.github.com/repos/tj/node-github-url-from-git/commits/3c25da87dcfb99d06707ed8a3875b399a971620b\",\n" +
                "                \"html_url\": \"https://github.com/tj/node-github-url-from-git/commit/3c25da87dcfb99d06707ed8a3875b399a971620b\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }]";
        List<GitCommit> commitList = CommitMapper.map(responseBody);
        assertEquals(1, commitList.size());

        GitCommit commit = commitList.get(0);
        assertEquals("486ed80c20edb9cc3cc8accd4c020daa8431e1d2", commit.getSha());
        assertEquals("Benjamin Coe", commit.getCommit().getAuthor().getName());
        assertEquals("ben@npmjs.com", commit.getCommit().getAuthor().getEmail());
        assertEquals("chore(release): 1.5.0", commit.getCommit().getMessage());
    }

    @Test
    public void shouldReturnEmptyListWhenResponseIsEmpty() {
        String responseBody = "[]";
        List<GitCommit> commitList = CommitMapper.map(responseBody);
        assertEquals(0, commitList.size());
    }
}
