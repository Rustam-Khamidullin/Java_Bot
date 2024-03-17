package edu.java.dto.bot.request;

import java.net.URI;

public record LinkUpdateRequest(
    long chatId,
    URI url,
    String description) {
}

