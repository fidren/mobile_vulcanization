package pl.mobilevulcanization.service;

import pl.mobilevulcanization.dto.AppointmentDateDto;
import pl.mobilevulcanization.model.AppointmentDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DateService {
    AppointmentDate addAppointmentDate(AppointmentDateDto appointmentDateDto);
    AppointmentDate updateAppointmentDate(AppointmentDateDto appointmentDateDto, LocalDateTime localDateTime);
    void deleteAppointmentDate(LocalDateTime localDateTime);
    AppointmentDate getAppointmentDate(LocalDateTime localDateTime);
    List<AppointmentDate> getAllAppointmentDates();
    List<AppointmentDate> getAllAppointmentDatesByDate(LocalDate date);
    List<AppointmentDate> getAllCurrentAppointmentDates();
}
