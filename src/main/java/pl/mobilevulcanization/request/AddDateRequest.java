package pl.mobilevulcanization.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AddDateRequest {
    private LocalDateTime date;
    private String isFree;
}
