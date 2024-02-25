package edu.java.scrapper.client.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.github.GitHubRestClient;
import edu.java.dto.github.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class GitHubRestClientTest {
    private WireMockServer wireMockServer;

    @Before
    public void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetRepository() {
        // Set up WireMock to mock the GitHub API response
        String owner = "testOwner";
        String repository = "testRepository";
        String apiUrl = String.format("/repos/%s/%s", owner, repository);
        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"name\": \"TestRepo\", \"owner\": {\"login\": \"testOwner\"}}")));

        // Create the GitHubRestClient with the WireMock server base URL
        GitHubRestClient gitHubRestClient = new GitHubRestClient("http://localhost:" + wireMockServer.port());

        // Perform the test
        Repository repositoryResult = gitHubRestClient.getRepository(owner, repository);

        // Verify the result
        assertEquals("TestRepo", repositoryResult.id());
        assertEquals("testOwner", repositoryResult.pushed_at());
        assertEquals("testOwner", repositoryResult.updated_at());
    }
}
