package edu.java.domain.jdbc;

import edu.java.dto.repository.Link;
import java.net.URI;
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
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;

    public Link addOrGetExisting(Long chatId, URI url) {
        Link existingLink = find(chatId, url);

        if (existingLink != null) {
            return existingLink;
        }

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
            connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO link (id_link, url, id_chat) "
                        + "VALUES (DEFAULT, ?, ?) RETURNING id_link, last_update",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, url.toString());
                ps.setLong(2, chatId);
                return ps;
            },
            keyHolder
        );

        var keys = keyHolder.getKeys();

        return new Link(
            (Long) keys.get("id_link"),
            url,
            (Long) keys.get("id_chat"),
            (Timestamp) keys.get("last_update")
        );
    }

    public Link remove(Long chatId, URI url) {
        Link link = find(chatId, url);

        if (link != null) {
            jdbcTemplate.update(
                "DELETE FROM link WHERE id_chat = ? AND url = ?",
                chatId,
                url.toString()
            );
        }

        return link;
    }

    public List<Link> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM link",
            Link::rowMap
        );
    }

    public List<Link> findAll(Timestamp before) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE last_update <= ?",
            Link::rowMap,
            before
        );
    }

    public List<Link> findAll(Long chatId) {
        return jdbcTemplate.query(
            "SELECT * FROM link "
                + "WHERE id_chat = ?",
            Link::rowMap,
            chatId
        );
    }

    public Link find(Long chatId, URI url) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM link WHERE id_chat = ? AND url = ?",
                Link::rowMap,
                chatId,
                url.toString()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateLinks(Long[] ids) {
        if (ids.length == 0) {
            return;
        }

        StringBuilder query = new StringBuilder("UPDATE link SET last_update = CURRENT_TIMESTAMP "
            + "WHERE id_link IN (");
        for (int i = 0; i < ids.length; i++) {
            query.append("?");
            if (i < ids.length - 1) {
                query.append(", ");
            }
        }
        query.append(')');

        jdbcTemplate.update(query.toString(), (Object[]) ids);
    }
}
