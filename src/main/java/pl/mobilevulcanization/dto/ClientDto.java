package pl.mobilevulcanization.dto;

import lombok.Data;
import pl.mobilevulcanization.model.AppointmentDate;

import java.time.LocalDateTime;

@Data
public class ClientDto {
    private AppointmentDate appointmentDate;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String category;
    private String description;
}
