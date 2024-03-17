package edu.java.dto.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public record Chat(
    Long chatId,
    Timestamp createdAt) {
    public static Chat rowMap(ResultSet rs, int rowNum) throws SQLException {
        return new Chat(
            rs.getLong("id"),
            rs.getTimestamp("create_at")
        );
    }
}

