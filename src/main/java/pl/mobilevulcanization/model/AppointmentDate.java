package pl.mobilevulcanization.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "appointment_dates")
public class AppointmentDate  {

    @Id
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "is_free", nullable = false)
    private boolean isFree;

}
