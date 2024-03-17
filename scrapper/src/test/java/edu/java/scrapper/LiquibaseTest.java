package edu.java.scrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.time.OffsetDateTime;
import static edu.java.scrapper.IntegrationTest.POSTGRES;

public class LiquibaseTest {
    private final DataSource dataSource =
        DataSourceBuilder.create()
            .url(POSTGRES.getJdbcUrl())
            .username(POSTGRES.getUsername())
            .password(POSTGRES.getPassword())
            .build();

    @Test
    void simpleTest() {
        var jdbcTemplate = new JdbcTemplate(dataSource);

        long chatId = 111;
        long linkId = 222;
        String url = "https://some_link";
        OffsetDateTime time = OffsetDateTime.now();

        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO link (id, url, last_update) VALUES (?, ?, ?)", linkId, url, time);
        jdbcTemplate.update("INSERT INTO chat_link (id_chat, id_link) VALUES (?, ?)", chatId, linkId);

        Long actualChatId = jdbcTemplate.queryForObject(
            "SELECT id FROM chat WHERE id = ?",
            Long.class, chatId
        );
        String actualUrl = jdbcTemplate.queryForObject(
            "SELECT url FROM link WHERE id = ?",
            String.class, linkId
        );
        OffsetDateTime actualTime = jdbcTemplate.queryForObject(
            "SELECT last_update FROM link WHERE id = ?",
            OffsetDateTime.class, linkId
        );
        Long actualLinkId = jdbcTemplate.queryForObject(
            "SELECT id_link FROM chat_link WHERE id_chat = ?",
            Long.class,
            chatId
        );

        Assertions.assertEquals(actualChatId, chatId);
        Assertions.assertEquals(actualUrl, url);
        Assertions.assertEquals(time.toEpochSecond(), actualTime.toEpochSecond());
        Assertions.assertEquals(actualLinkId, linkId);
    }
}
