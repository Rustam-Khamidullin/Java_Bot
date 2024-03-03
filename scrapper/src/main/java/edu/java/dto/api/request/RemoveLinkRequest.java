package edu.java.dto.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RemoveLinkRequest(@JsonProperty("link") String link) {
}
