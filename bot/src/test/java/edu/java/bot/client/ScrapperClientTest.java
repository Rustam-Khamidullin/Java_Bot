package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.bot.dto.scrapper.request.AddLinkRequest;
import edu.java.bot.dto.scrapper.request.RemoveLinkRequest;
import edu.java.bot.dto.scrapper.response.LinkResponse;
import edu.java.bot.dto.scrapper.response.ListLinksResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest(
    properties = {"retry.max-attempt=3", "retry.retryableStatusCodes[0]=404"}
)
class ScrapperClientTest {
    private final RetryTemplate retryTemplate;
    static private WireMockServer wireMockServer;
    static private ScrapperClient scrapperClient;
    static private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ScrapperClientTest(RetryTemplate retryTemplate) {
        this.retryTemplate = retryTemplate;
    }

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(23213);
        wireMockServer.start();

        scrapperClient = new ScrapperClient("http://localhost:" + wireMockServer.port(), retryTemplate);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void registerChat() {
        long id = 1234;

        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/%d".formatted(id)))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("Mocked Response")));

        Assertions.assertDoesNotThrow(() -> scrapperClient.registerChat(id));
    }

    @Test
    void deleteChat() {
        long id = 1234;

        wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/%d".formatted(id)))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("Mocked Response")));

        Assertions.assertDoesNotThrow(() -> scrapperClient.deleteChat(id));
    }

    @Test
    void getAllLinks() throws JsonProcessingException {
        List<LinkResponse> linkResponses = new ArrayList<>();
        linkResponses.add(new LinkResponse(1, URI.create("1")));
        linkResponses.add(new LinkResponse(2, URI.create("2")));
        linkResponses.add(new LinkResponse(3, URI.create("3")));

        ListLinksResponse listLinksResponse = new ListLinksResponse(linkResponses, linkResponses.size());

        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("789"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(objectMapper.writeValueAsString(listLinksResponse))));

        ListLinksResponse response = scrapperClient.getAllLinks(789);

        Assertions.assertEquals(response.size(), 3);
        Assertions.assertIterableEquals(response.links(), linkResponses);
    }

    @Test
    void addLink() throws JsonProcessingException {
        AddLinkRequest addLinkRequest = new AddLinkRequest("some link");

        LinkResponse linkResponse = new LinkResponse(123, URI.create("123"));

        wireMockServer.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("987"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(objectMapper.writeValueAsString(linkResponse))));

        LinkResponse response = scrapperClient.addLink(987, addLinkRequest);

        Assertions.assertEquals(response, linkResponse);
    }

    @Test
    void removeLink() throws JsonProcessingException {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest("some link");

        LinkResponse linkResponse = new LinkResponse(123, URI.create("123"));

        wireMockServer.stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("654"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(objectMapper.writeValueAsString(linkResponse))));

        LinkResponse response = scrapperClient.removeLink(654, removeLinkRequest);

        Assertions.assertEquals(response, linkResponse);
    }

    @Test
    public void retryableCode() {
        long id = 1234;

        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(404)
                .withBody("Mocked Response")));

        AtomicInteger counter = new AtomicInteger(0);
        Assertions.assertThrows(
            HttpClientErrorException.class,
            () -> retryTemplate.execute(
                context -> {
                    counter.incrementAndGet();
                    scrapperClient.getAllLinks(id);
                    return null;
                }
            )
        );
        Assertions.assertEquals(counter.get(), 3);
    }

    @Test
    public void notRetryableCode() {
        long id = 1234;

        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .withHeader("Content-Type", equalTo("application/json"))
            .withHeader("Tg-Chat-Id", equalTo(String.valueOf(id)))
            .willReturn(aResponse()
                .withStatus(403)
                .withBody("Mocked Response")));

        AtomicInteger counter = new AtomicInteger(0);
        Assertions.assertThrows(
            HttpClientErrorException.class,
            () -> retryTemplate.execute(
                context -> {
                    counter.incrementAndGet();
                    scrapperClient.getAllLinks(id);
                    return null;
                }
            )
        );
        Assertions.assertEquals(counter.get(), 1);
    }
}
