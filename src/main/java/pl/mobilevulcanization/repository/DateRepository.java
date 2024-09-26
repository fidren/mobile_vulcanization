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

    @Query("SELECT a FROM AppointmentDate a WHERE FUNCTION('DATE', a.date) = :localDate AND FUNCTION('DATE', a.date) >= CURRENT_DATE AND a.isFree = true")
    List<AppointmentDate> findAllFreeCurrentDateByDate(LocalDate localDate);

    @Query("SELECT a FROM AppointmentDate a WHERE " +
            "(COALESCE(:localDate, FUNCTION('DATE', a.date)) = FUNCTION('DATE', a.date)) AND" +
            "(:isCurrent = false OR FUNCTION('DATE', a.date) >= CURRENT_DATE) AND" +
            "(COALESCE(:isFree, a.isFree) = a.isFree)"
    )
    List<AppointmentDate> findFilteredDates(LocalDate localDate, Boolean isCurrent, Boolean isFree);
}
