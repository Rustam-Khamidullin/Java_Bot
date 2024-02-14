package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.Botik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication implements CommandLineRunner {
    @Autowired
    private ApplicationConfig applicationConfig;
    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String token = applicationConfig.telegramToken();

        System.out.println(token);

        Botik bot = new Botik(token);

        bot.start();
    }
}
