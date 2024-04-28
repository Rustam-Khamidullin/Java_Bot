package edu.java.bot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.bot.service.UpdateHandlerService;
import lombok.Data;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaConfiguration {
    private final String updateTopicName;
    private final String dlqSuffix;
    private final int partitions;
    private final int replicas;

    @Autowired
    private final UpdateHandlerService updateHandlerService;
    @Autowired
    private final ObjectMapper objectMapper;
    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    public NewTopic updateTopic() {
        return TopicBuilder.name(updateTopicName)
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }

    @Bean
    public NewTopic dlqUpdateTopic() {
        return TopicBuilder.name(updateTopicName + dlqSuffix)
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }
}
