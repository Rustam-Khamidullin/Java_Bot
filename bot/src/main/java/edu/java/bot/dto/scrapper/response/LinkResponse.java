package edu.java.bot.dto.scrapper.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public record LinkResponse(
    @JsonProperty("id")
     long id,
     @JsonProperty("url")
     URI url
) {
}
