package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.BotClient;
import edu.java.dto.bot.request.LinkUpdateRequest;
import java.net.URI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest
public class BotClientTest {
    @Autowired
    private RetryTemplate retryTemplate;
    private static WireMockServer wireMockServer;
    private static BotClient botClient;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(23213);
        wireMockServer.start();

        botClient = new BotClient("http://localhost:" + wireMockServer.port(), retryTemplate);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testSendUpdate() {
        wireMockServer.stubFor(post(urlEqualTo("/updates"))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("Mocked Response")));

        LinkUpdateRequest request = new LinkUpdateRequest(
            666,
            URI.create("link"),
            "description"
        );

        ResponseEntity<?> responseEntity = botClient.sendUpdate(request);

        Assertions.assertEquals(responseEntity.getStatusCode().value(), 200);
    }

    @Test
    public void retryableCode() {
        wireMockServer.stubFor(post(urlEqualTo("/updates"))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(404)
                .withBody("Mocked Response")));

        LinkUpdateRequest request = new LinkUpdateRequest(
            666,
            URI.create("link"),
            "description"
        );

        // 4 times
        Assertions.assertThrows(HttpClientErrorException.class, () -> botClient.sendUpdate(request));
    }

    @Test
    public void notRetryableCode() {
        wireMockServer.stubFor(post(urlEqualTo("/updates"))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(403)
                .withBody("Mocked Response")));

        LinkUpdateRequest request = new LinkUpdateRequest(
            666,
            URI.create("link"),
            "description"
        );

        // Once
        Assertions.assertThrows(HttpClientErrorException.class, () -> botClient.sendUpdate(request));
    }
}
