package pl.mobilevulcanization.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AddDateRequest(
    LocalDateTime date,
    String isFree
) {
}
