package pl.mobilevulcanization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mobilevulcanization.exception.ResourceNotFoundException;
import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.repository.DateRepository;
import pl.mobilevulcanization.request.AddDateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateService{
    private final DateRepository dateRepository;

    public AppointmentDate addAppointmentDate(AddDateRequest addDateRequest) {
        return dateRepository.save(mapToEntity(addDateRequest));
    }

    private AppointmentDate mapToEntity(AddDateRequest addDateRequest) {
        return new AppointmentDate(
                addDateRequest.date(),
                addDateRequest.isFree().equals("true")
        );
    }

    @Transactional
    public AppointmentDate updateAppointmentDate(String isFreeField, LocalDateTime localDateTime) {
        AppointmentDate appointmentDate = dateRepository.findByDate(localDateTime);

        if (appointmentDate == null) {
            throw new ResourceNotFoundException("Date not found");
        }

        appointmentDate.setFree(isFreeField.equals("true"));

        return dateRepository.save(appointmentDate);
    }

    public void deleteAppointmentDate(LocalDateTime localDateTime) {
        AppointmentDate appointmentDate = dateRepository.findByDate(localDateTime);

        if (appointmentDate == null) {
            throw new ResourceNotFoundException("Date: " + localDateTime + " not found");
        }

        dateRepository.delete(appointmentDate);
    }

    public List<AppointmentDate> getAllAppointmentDates() {
        return dateRepository.findAll();
    }

    public List<AppointmentDate> getAllFreeCurrentAppointmentDatesByDate(LocalDate date) {
        return dateRepository.findAllFreeCurrentDateByDate(date);
    }

    public List<AppointmentDate> getFilteredDates(LocalDate date, Boolean isCurrent, Boolean isFree) {
        return dateRepository.findFilteredDates(date, isCurrent, isFree);
    }

}
