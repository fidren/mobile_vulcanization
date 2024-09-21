package pl.mobilevulcanization.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDateDto {
    private LocalDateTime date;
    private boolean isFree;

    public boolean isFree() {
        return isFree;
    }
}
