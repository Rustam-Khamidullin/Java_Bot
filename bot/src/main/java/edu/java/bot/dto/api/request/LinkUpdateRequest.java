package edu.java.bot.dto.api.request;

import jakarta.validation.constraints.NotBlank;

public record LinkUpdateRequest(
    long chatId,
    @NotBlank
    String url,
    String description
) {
}

