package pl.mobilevulcanization.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.repository.DateRepository;
import pl.mobilevulcanization.request.AddDateRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DateServiceTest {
    @Mock
    private DateRepository dateRepository;

    @InjectMocks
    private DateService dateService;

    private AppointmentDate appointmentDate;
    private AddDateRequest addDateRequest;

    @BeforeEach
    void setUpAppointmentDate() {
        appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(true)
                .build();
    }

    void setUpAddDateRequest() {
        addDateRequest = AddDateRequest.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree("true")
                .build();
    }

    @Test
    void addAppointmentDateTest() {
        setUpAddDateRequest();

        when(dateRepository.save(Mockito.any(AppointmentDate.class))).thenReturn(appointmentDate);
        //Act
        AppointmentDate savedDate = dateService.addAppointmentDate(addDateRequest);

        //Assert
        assertNotNull(savedDate);
    }

    @Test
    void updateAppointmentDateTest() {
        when(dateRepository.save(Mockito.any(AppointmentDate.class))).thenReturn(appointmentDate);
        when(dateRepository.findByDate(Mockito.any(LocalDateTime.class))).thenReturn(appointmentDate);

        AppointmentDate updatedDate = dateService.updateAppointmentDate("false", LocalDateTime.of(2024, 11, 28, 14, 30));

        assertNotNull(updatedDate);
    }

    @Test
    void deleteAppointmentDateTest() {
        when(dateRepository.findByDate(Mockito.any(LocalDateTime.class))).thenReturn(appointmentDate);

        assertAll(() -> dateService.deleteAppointmentDate(LocalDateTime.of(2024, 11, 28, 14, 30)));
    }
}