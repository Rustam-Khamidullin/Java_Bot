package edu.java.configuration;

import lombok.Data;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfiguration {
    String updateTopicName;
    int partitions;
    int replicas;

    @Bean
    public NewTopic updateTopic() {
        return TopicBuilder.name(updateTopicName)
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }
}
