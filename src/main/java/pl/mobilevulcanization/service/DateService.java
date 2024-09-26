package pl.mobilevulcanization.service;

import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.request.AddDateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DateService {
    AppointmentDate addAppointmentDate(AddDateRequest addDateRequest);
    AppointmentDate updateAppointmentDate(String isFreeField, LocalDateTime localDateTime);
    void deleteAppointmentDate(LocalDateTime localDateTime);
    List<AppointmentDate> getAllAppointmentDates();
    List<AppointmentDate> getAllFreeCurrentAppointmentDatesByDate(LocalDate date);
    List<AppointmentDate> getFilteredDates(LocalDate date, Boolean isCurrent, Boolean isFree);
}
