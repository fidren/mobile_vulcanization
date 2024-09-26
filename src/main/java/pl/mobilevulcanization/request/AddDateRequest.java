package pl.mobilevulcanization.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddDateRequest {
    private LocalDateTime date;
    private String isFree;
}
