package pl.mobilevulcanization.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pl.mobilevulcanization.model.AppointmentDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DateRepositoryTest {
    @Autowired
    private DateRepository dateRepository;

    @Test
    public void saveDateTest(){
        //Arrange
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(true)
                .build();

        //Act
        AppointmentDate savedDate = dateRepository.save(appointmentDate);

        //Assert
        assertNotNull(savedDate);
    }

    @Test
    public void deleteDateTest(){
        //Arrange
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(true)
                .build();

        //Act
        dateRepository.save(appointmentDate);
        dateRepository.delete(appointmentDate);

        boolean dateExists = dateRepository.findByDate(LocalDateTime.of(2024, 11, 28, 14, 30)) != null;

        //Assert
        assertFalse(dateExists);
    }

    @Test
    public void getAllDatesTest() {
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(true)
                .build();
        AppointmentDate appointmentDate2 = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 15, 0))
                .isFree(true)
                .build();

        dateRepository.save(appointmentDate);
        dateRepository.save(appointmentDate2);

        List<AppointmentDate> allDates = dateRepository.findAll();

        assertNotNull(allDates);
        assertThat(allDates.size()).isEqualTo(2);
    }

    @Test
    public void getAppointmentDateByDateTest() {
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(true)
                .build();

        dateRepository.save(appointmentDate);

        AppointmentDate searchDate = dateRepository.findByDate(appointmentDate.getDate());

        Assertions.assertNotNull(searchDate);
    }

    @Test
    void getAllFreeCurrentDateByDateTest() {
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 9, 10, 14, 30))
                .isFree(true)
                .build();
        AppointmentDate appointmentDate2 = AppointmentDate.builder()
                .date(LocalDate.now().atTime(15, 0))
                .isFree(true)
                .build();
        AppointmentDate appointmentDate3 = AppointmentDate.builder()
                .date(LocalDate.now().atTime(17, 0))
                .isFree(false)
                .build();

        dateRepository.save(appointmentDate);
        dateRepository.save(appointmentDate2);
        dateRepository.save(appointmentDate3);

        List<AppointmentDate> allDates = dateRepository.findAllFreeCurrentDateByDate(LocalDate.now());

        assertNotNull(allDates);
        assertThat(allDates.size()).isEqualTo(1);
    }

    @Test
    void getFilteredDatesTest() {
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 9, 10, 14, 30))
                .isFree(true)
                .build();
        AppointmentDate appointmentDate2 = AppointmentDate.builder()
                .date(LocalDate.now().atTime(15, 0))
                .isFree(true)
                .build();
        AppointmentDate appointmentDate3 = AppointmentDate.builder()
                .date(LocalDate.now().atTime(17, 0))
                .isFree(false)
                .build();

        dateRepository.save(appointmentDate);
        dateRepository.save(appointmentDate2);
        dateRepository.save(appointmentDate3);

        List<AppointmentDate> allDates = dateRepository.findFilteredDates(LocalDate.now(), true, null);
        List<AppointmentDate> allDates2 = dateRepository.findFilteredDates(null, false, true);

        assertNotNull(allDates);
        assertNotNull(allDates2);
        assertThat(allDates.size()).isEqualTo(2);
        assertThat(allDates2.size()).isEqualTo(2);
    }
}