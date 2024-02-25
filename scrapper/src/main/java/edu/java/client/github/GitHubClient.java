package edu.java.client.github;

import edu.java.dto.github.Repository;

public interface GitHubClient {
    Repository getRepository(String owner, String repository);
}
