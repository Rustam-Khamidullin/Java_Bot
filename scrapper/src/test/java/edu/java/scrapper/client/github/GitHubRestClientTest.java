package edu.java.scrapper.client.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.github.GitHubRestClient;
import edu.java.dto.github.Repository;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest
public class GitHubRestClientTest {
    private final RetryTemplate retryTemplate;
    static private WireMockServer wireMockServer;
    static private GitHubRestClient gitHubRestClient;

    @Autowired
    public GitHubRestClientTest(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(35234);
        wireMockServer.start();

        gitHubRestClient = new GitHubRestClient("http://localhost:" + wireMockServer.port(), retryTemplate);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetRepository() {
        String owner = "testOwner";
        String repository = "testRepository";
        int id = 754063270;
        OffsetDateTime updated_at = OffsetDateTime.parse("2024-02-07T10:29:17Z");
        OffsetDateTime pushed_at = OffsetDateTime.parse("2024-02-25T10:08:03Z");

        String apiUrl = String.format("/repos/%s/%s", owner, repository);

        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(
                    "{\n" +
                        "    \"chatId\": 754063270,\n" +
                        "    \"node_id\": \"R_kgDOLPIXpg\",\n" +
                        "    \"name\": \"Java_Bot\",\n" +
                        "    \"full_name\": \"Rustam-Khamidullin/Java_Bot\",\n" +
                        "    \"private\": false,\n" +
                        "    \"html_url\": \"https://github.com/Rustam-Khamidullin/Java_Bot\",\n" +
                        "    \"description\": null,\n" +
                        "    \"fork\": false,\n" +
                        "    \"created_at\": \"2024-02-07T10:28:03Z\",\n" +
                        "    \"updated_at\": \"2024-02-07T10:29:17Z\",\n" +
                        "    \"pushed_at\": \"2024-02-25T10:08:03Z\"\n" +
                        "}"
                )));

        Repository repositoryResponse = gitHubRestClient.getRepository(owner, repository);

        Assertions.assertEquals(id, repositoryResponse.id());
        Assertions.assertEquals(pushed_at, repositoryResponse.pushedAt());
        Assertions.assertEquals(updated_at, repositoryResponse.updatedAt());
    }

    @Test
    public void testGetRepositoryEmpty() {
        String owner = "testOwner";
        String repository = "testRepository";

        String apiUrl = String.format("/repos/%s/%s", owner, repository);

        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("{}")));

        Repository repositoryResponse = gitHubRestClient.getRepository(owner, repository);

        Assertions.assertNull(repositoryResponse.pushedAt());
        Assertions.assertNull(repositoryResponse.updatedAt());
    }

    @Test
    public void testGetRepositoryWithoutBody() {
        String owner = "testOwner";
        String repository = "testRepository";

        String apiUrl = String.format("/repos/%s/%s", owner, repository);

        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
            ));

        Assertions.assertDoesNotThrow(() -> gitHubRestClient.getRepository(owner, repository));
    }

    @Test
    public void testGetRepositoryWithoutBadCode() {
        String owner = "testOwner";
        String repository = "testRepository";

        String apiUrl = String.format("/repos/%s/%s", owner, repository);

        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(404)
                .withBody("{}")
            ));

        Assertions.assertThrows(HttpClientErrorException.class, () ->
            gitHubRestClient.getRepository(owner, repository));

    }
}
