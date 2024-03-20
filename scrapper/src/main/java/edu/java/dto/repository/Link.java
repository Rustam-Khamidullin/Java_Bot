package edu.java.dto.repository;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public record Link(
    Long linkId,
    URI url,
    Long chatId,
    Timestamp lastUpdate
) {
    public static Link rowMap(ResultSet rs, int rowNum) throws SQLException {
        return new Link(
            rs.getLong("id_link"),
            URI.create(rs.getString("url")),
            rs.getLong("id_chat"),
            rs.getTimestamp("last_update")
        );
    }
}
