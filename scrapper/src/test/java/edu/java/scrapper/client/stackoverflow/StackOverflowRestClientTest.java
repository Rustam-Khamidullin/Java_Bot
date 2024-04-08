package edu.java.scrapper.client.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.StackOverflowRestClient;
import edu.java.dto.stackoverflow.Questions;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
public class StackOverflowRestClientTest {
    @Autowired
    private RetryTemplate retryTemplate;
    static private WireMockServer wireMockServer;
    static private StackOverflowClient stackOverflowRestClient;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(35234);
        wireMockServer.start();

        stackOverflowRestClient =
            new StackOverflowRestClient("http://localhost:" + wireMockServer.port(), retryTemplate);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetQuestions() {
        int id = 78056379;
        OffsetDateTime lastActivityDate = Instant.ofEpochSecond(1708872296L)
            .atOffset(ZoneOffset.ofHours(0));
        long answerCount = 123;

        String apiUrl = "/questions/%d".formatted(id);

        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("site", "stackoverflow")
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(
                    "{\n" +
                        "  \"items\": [\n" +
                        "    {\n" +
                        "      \"is_answered\": false,\n" +
                        "      \"view_count\": 6,\n" +
                        "      \"answer_count\": 123,\n" +
                        "      \"score\": 0,\n" +
                        "      \"last_activity_date\": 1708872296,\n" +
                        "      \"creation_date\": 1708872296,\n" +
                        "      \"question_id\": 78056379,\n" +
                        "      \"content_license\": \"CC BY-SA 4.0\",\n" +
                        "      \"link\": \"https://stackoverflow.com/questions/78056379/receiving-400-bad-request-when-communicating-between-client-and-controller\",\n" +
                        "      \"title\": \"Receiving &quot;400 bad request&quot; when communicating between client and controller\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"has_more\": false,\n" +
                        "  \"quota_max\": 10000,\n" +
                        "  \"quota_remaining\": 9923\n" +
                        "}"
                )));

        Questions questionsResponse = stackOverflowRestClient.getQuestions(id);

        Assertions.assertEquals(id, questionsResponse.items().getFirst().id());
        Assertions.assertEquals(lastActivityDate, questionsResponse.items().getFirst().lastActivityDate());
        Assertions.assertEquals(answerCount, questionsResponse.items().getFirst().answerCount());
    }

    @Test
    public void testGetQuestionsEmpty() {
        int id = 78056379;

        String apiUrl = "/questions/%d".formatted(id);

        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("{}")));

        Questions questionsResponse = stackOverflowRestClient.getQuestions(id);

        Assertions.assertNull(questionsResponse.items());
    }

    @Test
    public void testGetQuestionsWithoutBody() {
        int id = 78056379;

        String apiUrl = "/questions/%d".formatted(id);

        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
            ));

        Assertions.assertDoesNotThrow(() -> stackOverflowRestClient.getQuestions(id));
    }

    @Test
    public void testGetRepositoryWithoutBadCode() {
        int id = 78056379;

        String apiUrl = "/questions/%d".formatted(id);

        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(404)
                .withBody("{}")
            ));

        Assertions.assertThrows(HttpClientErrorException.class, () ->
            stackOverflowRestClient.getQuestions(id));

    }
}
