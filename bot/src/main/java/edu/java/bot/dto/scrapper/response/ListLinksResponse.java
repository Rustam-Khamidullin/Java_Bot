package edu.java.bot.dto.scrapper.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ListLinksResponse(
    @JsonProperty("links")
    List<LinkResponse> links,
    @JsonProperty("size")
    int size) {
}
