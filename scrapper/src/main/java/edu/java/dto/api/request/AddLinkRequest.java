package edu.java.dto.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddLinkRequest(@JsonProperty("link") String link) {
}
