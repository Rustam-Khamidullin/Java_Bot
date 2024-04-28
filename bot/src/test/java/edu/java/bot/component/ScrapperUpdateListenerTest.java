package edu.java.bot.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.dto.api.request.LinkUpdateRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka
public class ScrapperUpdateListenerTest {
    @Autowired
    private ScrapperUpdateListener scrapperUpdateListener;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${kafka.update-topic-name}")
    private String topic;

    @Test
    @SneakyThrows
    public void testReceiveMessage() {
        String data = objectMapper.writeValueAsString(new LinkUpdateRequest(1, "url", "desc"));

        kafkaTemplate.send(topic, data);

        Thread.sleep(1000);

        Assertions.assertEquals(1, scrapperUpdateListener.getRequestCounter());
    }

    @Test
    @SneakyThrows
    public void testIncorrectMessage() {
        String data = "Incorrect data";

        kafkaTemplate.send(topic, data);

        Thread.sleep(1000);

        Assertions.assertEquals(1, scrapperUpdateListener.getIncorrectRequestCounter());
    }
}
