package pl.mobilevulcanization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.mobilevulcanization.model.AppointmentDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DateRepository extends JpaRepository<AppointmentDate, Long> {
    AppointmentDate findByDate(LocalDateTime localDateTime);

    @Query("SELECT a FROM AppointmentDate a WHERE FUNCTION('DATE', a.date) = :localDate")
    List<AppointmentDate> findAllByDate(@Param("localDate") LocalDate localDate);

    @Query("SELECT a FROM AppointmentDate a WHERE FUNCTION('DATE', a.date) >= CURRENT_DATE")
    List<AppointmentDate> findAllCurrentDate();
}
