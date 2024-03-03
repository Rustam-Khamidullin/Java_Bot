package edu.java.scrapper.client.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.stackoverflow.StackOverflowRestClient;
import edu.java.dto.stackoverflow.Questions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class StackOverflowRestClientTest {
    static private WireMockServer wireMockServer;
    static private StackOverflowRestClient stackOverflowRestClient;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(35234);
        wireMockServer.start();

        stackOverflowRestClient = new StackOverflowRestClient("http://localhost:" + wireMockServer.port());
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

        ResponseEntity<Questions> responseEntityQuestionResult = stackOverflowRestClient.getQuestions(id);

        Assertions.assertTrue(responseEntityQuestionResult.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(responseEntityQuestionResult.hasBody());

        var questionResult = responseEntityQuestionResult.getBody();

        Assertions.assertEquals(id, questionResult.items().getFirst().id());
        Assertions.assertEquals(lastActivityDate, questionResult.items().getFirst().lastActivityDate());
        Assertions.assertEquals(answerCount, questionResult.items().getFirst().answerCount());
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

        ResponseEntity<Questions> responseEntityQuestionsResult = stackOverflowRestClient.getQuestions(id);

        Assertions.assertTrue(responseEntityQuestionsResult.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(responseEntityQuestionsResult.hasBody());

        var questionsResult = responseEntityQuestionsResult.getBody();

        Assertions.assertNull(questionsResult.items());
    }

    @Test
    public void testGetQuestionsWithoutBody() {
        int id = 78056379;

        String apiUrl = "/questions/%d".formatted(id);

        wireMockServer.stubFor(get(urlEqualTo(apiUrl))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
            ));

        ResponseEntity<Questions> responseEntityQuestionsResult = stackOverflowRestClient.getQuestions(id);

        Assertions.assertTrue(responseEntityQuestionsResult.getStatusCode().is2xxSuccessful());
        Assertions.assertFalse(responseEntityQuestionsResult.hasBody());
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
