package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.BotClient;
import edu.java.dto.bot.request.LinkUpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class BotClientTest {
    static private WireMockServer wireMockServer;
    static private BotClient botClient;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(23213);
        wireMockServer.start();

        botClient = new BotClient("http://localhost:" + wireMockServer.port());
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
}
