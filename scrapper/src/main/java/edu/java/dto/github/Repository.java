package edu.java.dto.github;

import java.time.OffsetDateTime;

public record Repository
(    int id,
    OffsetDateTime updated_at,
    OffsetDateTime pushed_at
     ) {
}
