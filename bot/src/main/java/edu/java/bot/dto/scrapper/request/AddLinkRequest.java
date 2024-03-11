package edu.java.bot.dto.scrapper.request;

import jakarta.validation.constraints.NotBlank;

public record AddLinkRequest(
    @NotBlank
    String link
) {
}

