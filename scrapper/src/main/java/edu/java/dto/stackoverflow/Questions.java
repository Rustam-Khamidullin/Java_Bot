package edu.java.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Questions(
    @NotNull
    List<Item> items
) {
    public record Item(
        @JsonProperty("question_id")
        long id,
        @JsonProperty("answer_count")
        long answerCount,
        @JsonProperty("last_activity_date")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        OffsetDateTime lastActivityDate,
        @JsonProperty("last_edit_date")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        OffsetDateTime lastEditDate
    ) {
    }
}
