package edu.java.client.github;

import edu.java.dto.github.Repository;
import org.springframework.http.ResponseEntity;

public interface GitHubClient {
    ResponseEntity<Repository> getRepository(String owner, String repository);
}
