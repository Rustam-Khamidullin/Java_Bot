package edu.java.bot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.dto.api.request.LinkUpdateRequest;
import edu.java.bot.service.UpdateHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;

@ConfigurationProperties(prefix = "kafka-configuration")
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final UpdateHandlerService updateHandlerService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = "${kafka-configuration.updateTopicName}")
    public void listen(String request) {
        LinkUpdateRequest update = objectMapper.readValue(request, LinkUpdateRequest.class);

        updateHandlerService.handleUpdate(update);
    }
}
