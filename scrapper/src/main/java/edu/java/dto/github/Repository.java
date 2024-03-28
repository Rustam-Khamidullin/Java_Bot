package edu.java.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record Repository(
    @JsonProperty("chatId")
    int id,
    @JsonProperty("updated_at")
    OffsetDateTime updatedAt,
    @JsonProperty("pushed_at")
    OffsetDateTime pushedAt
) {
}
