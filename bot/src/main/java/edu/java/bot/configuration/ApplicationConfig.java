package edu.java.bot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.configuration.ratelimit.BucketConfiguration;
import edu.java.bot.service.command.Command;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "bot", ignoreUnknownFields = false)
@EnableConfigurationProperties(BucketConfiguration.class)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    String updateTopicName,
    int partitions,
    int replicas
) {
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Lazy
    Map<String, Command> commands() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Command.class.getPackageName());

        return context.getBeansOfType(Command.class)
            .entrySet()
            .stream()
            .collect(Collectors.toMap(entry -> entry.getValue().command(), Map.Entry::getValue));
    }

    @Bean
    public BotCommand[] apiCommands(Map<String, Command> commands) {
        return commands.entrySet().stream()
            .map(e -> new BotCommand(e.getKey(), e.getValue().description()))
            .toArray(BotCommand[]::new);
    }
}
