package edu.java.dto.api.response;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {
}
