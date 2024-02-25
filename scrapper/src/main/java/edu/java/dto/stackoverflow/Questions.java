package edu.java.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Questions(
    List<Item> items
) {
    public record Item(
        @JsonProperty("question_id")
        long id,
        @JsonProperty("last_activity_date")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        OffsetDateTime lastActivityDate
    ) {
    }
}
