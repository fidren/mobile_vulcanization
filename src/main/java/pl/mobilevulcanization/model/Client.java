package pl.mobilevulcanization.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    private String category;

    @OneToOne
    @JoinColumn(name = "date_of_appointment", referencedColumnName = "date", unique = true)  // Relacja One-to-One, połączona z tabelą appointment_dates
    private AppointmentDate appointmentDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(name = "client_type", nullable = false)
    private String clientType;

    private String nip;

    public Client(AppointmentDate appointmentDate, String name, String email, String phone, String address, String category, String description) {
        this.appointmentDate = appointmentDate;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.category = category;
        this.description = description;
    }
}
