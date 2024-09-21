package pl.mobilevulcanization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import pl.mobilevulcanization.dto.AppointmentDateDto;
import pl.mobilevulcanization.exception.ResourceNotFoundException;
import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.repository.ClientRepository;
import pl.mobilevulcanization.repository.DateRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateServiceImpl implements DateService{
    private final DateRepository dateRepository;

    @Override
    public AppointmentDate addAppointmentDate(AppointmentDateDto appointmentDateDto) {
        return dateRepository.save(mapToEntity(appointmentDateDto));
    }

    private AppointmentDate mapToEntity(AppointmentDateDto appointmentDateDto) {
        return new AppointmentDate(
                appointmentDateDto.getDate(),
                appointmentDateDto.isFree()
        );
    }

    @Override
    public AppointmentDate updateAppointmentDate(AppointmentDateDto appointmentDateDto, LocalDateTime localDateTime) {
        AppointmentDate appointmentDate = dateRepository.findByDate(localDateTime);

        if (appointmentDate == null) {
            throw new ResourceNotFoundException("Date not found");
        }

        appointmentDate = updateExistingDate(appointmentDate, appointmentDateDto);

        return dateRepository.save(appointmentDate);
    }

    private AppointmentDate updateExistingDate(AppointmentDate existingDate, AppointmentDateDto appointmentDateDto) {
        existingDate.setDate(appointmentDateDto.getDate());
        existingDate.setFree(appointmentDateDto.isFree());
        return existingDate;
    }

    @Override
    public void deleteAppointmentDate(LocalDateTime localDateTime) {
        AppointmentDate appointmentDate = dateRepository.findByDate(localDateTime);

        if (appointmentDate == null) {
            throw new ResourceNotFoundException("Date: " + localDateTime + " not found");
        }

        dateRepository.delete(appointmentDate);
    }

    @Override
    public AppointmentDate getAppointmentDate(LocalDateTime localDateTime) {
        AppointmentDate appointmentDate = dateRepository.findByDate(localDateTime);

        if (appointmentDate == null) {
            throw new ResourceNotFoundException("Date: " + localDateTime + " not found");
        }
        return appointmentDate;
    }

    @Override
    public List<AppointmentDate> getAllAppointmentDates() {
        return dateRepository.findAll();
    }

    @Override
    public List<AppointmentDate> getAllAppointmentDatesByDate(LocalDate date) {
        return dateRepository.findAllByDate(date);
    }

    @Override
    public List<AppointmentDate> getAllCurrentAppointmentDates() {
        return dateRepository.findAllCurrentDate();
    }
}
