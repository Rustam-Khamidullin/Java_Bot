package edu.java.bot.dto.scrapper.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddLinkRequest(@JsonProperty("link") String link) {
}

