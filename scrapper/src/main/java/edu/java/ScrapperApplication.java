package edu.java;

import edu.java.configuration.ApplicationConfiguration;
import edu.java.configuration.client.BotClientConfiguration;
import edu.java.configuration.client.GitHubClientConfiguration;
import edu.java.configuration.client.StackOverflowConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
    ApplicationConfiguration.class,
    GitHubClientConfiguration.class,
    StackOverflowConfiguration.class,
    BotClientConfiguration.class})
@EnableScheduling
@EnableJpaRepositories
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
