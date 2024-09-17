package pl.mobilevulcanization.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment_dates")
public class AppointmentDate  {

    @Id
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "is_free", nullable = false)
    private boolean isFree;

    @OneToOne(mappedBy = "appointmentDate")
    private Client client;
}
