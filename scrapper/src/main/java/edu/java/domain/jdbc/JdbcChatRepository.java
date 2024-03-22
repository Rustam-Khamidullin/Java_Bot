package edu.java.domain.jdbc;

import edu.java.dto.repository.Chat;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository {
    private final JdbcTemplate jdbcTemplate;

    public Chat addOrGetExists(Long chatId) {
        Chat existingLink = findByChatId(chatId);

        if (existingLink != null) {
            return existingLink;
        }

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO chat (id) VALUES (?) RETURNING create_at",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setLong(1, chatId);
                return ps;
            },
            keyHolder
        );

        return new Chat(
            chatId,
            (Timestamp) keyHolder.getKeys().get("create_at")
        );
    }

    public boolean remove(Long chatId) {
        return 1 == jdbcTemplate.update(
            "DELETE FROM chat WHERE id = ?",
            chatId
        );
    }

    public List<Chat> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM chat",
            Chat::rowMap
        );
    }

    public Chat findByChatId(Long chatId) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM chat WHERE id = ?",
                Chat::rowMap,
                chatId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
