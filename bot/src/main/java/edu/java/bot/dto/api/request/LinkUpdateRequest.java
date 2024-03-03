package edu.java.bot.dto.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record LinkUpdateRequest(
    long id,
    @NotBlank
    String url,
    String description,
    @NotNull
    List<Long> tgChatIds
) {
}
