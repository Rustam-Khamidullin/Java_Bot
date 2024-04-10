package edu.java.bot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.dto.api.request.LinkUpdateRequest;
import edu.java.bot.service.UpdateHandlerService;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfiguration {
    private final String updateTopicName;
    private final int partitions;
    private final int replicas;

    @Autowired
    private final UpdateHandlerService updateHandlerService;
    @Autowired
    private final ObjectMapper objectMapper;

    @Bean
    public NewTopic updateTopic() {
        return TopicBuilder.name(updateTopicName)
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }

    @SneakyThrows
    @KafkaListener(topics = "${kafka.updateTopicName}")
    public void listen(String request) {
        LinkUpdateRequest update = objectMapper.readValue(request, LinkUpdateRequest.class);

        updateHandlerService.handleUpdate(update);
    }
}
