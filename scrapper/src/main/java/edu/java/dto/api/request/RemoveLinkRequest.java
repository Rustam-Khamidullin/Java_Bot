package edu.java.dto.api.request;

import jakarta.validation.Valid;

public record RemoveLinkRequest(
    @Valid
    String link
) {
}
