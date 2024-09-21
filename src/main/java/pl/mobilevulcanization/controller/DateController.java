package pl.mobilevulcanization.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mobilevulcanization.dto.AppointmentDateDto;
import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.service.DateService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
public class DateController {
    private final DateService dateService;

    @GetMapping("/AllDates")
    public ResponseEntity<List<AppointmentDate>> getAllDates() {
        List<AppointmentDate> datesList = dateService.getAllAppointmentDates();
        return ResponseEntity.ok(datesList);
    }

    @GetMapping("/dates/{localDate}")
    public ResponseEntity<List<AppointmentDate>> getAllDatesByDate(@PathVariable("localDate") LocalDate date) {
        List<AppointmentDate> datesList = dateService.getAllAppointmentDatesByDate(date);
        return ResponseEntity.ok(datesList);
    }

    @GetMapping("/dates/current")
    public ResponseEntity<List<AppointmentDate>> getAllCurrentDates() {
        List<AppointmentDate> datesList = dateService.getAllCurrentAppointmentDates();
        return ResponseEntity.ok(datesList);
    }

    @GetMapping("/date/{localDate}/date")
    public ResponseEntity<AppointmentDate> getAppointmentDateByDate(@PathVariable("localDate") LocalDateTime date) {
        AppointmentDate appointmentDate = dateService.getAppointmentDate(date);
        return ResponseEntity.ok(appointmentDate);
    }

    @PostMapping("/addDate")
    public ResponseEntity<AppointmentDate> addAppointmentDate(@RequestBody AppointmentDateDto appointmentDateDto) {
        AppointmentDate appointmentDate = dateService.addAppointmentDate(appointmentDateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentDate);
    }

    @PutMapping("/date/{localDate}/update")
    public ResponseEntity<AppointmentDate> updateAppointmentDate(@RequestBody AppointmentDateDto appointmentDateDto, @PathVariable("localDate") LocalDateTime date) {
        AppointmentDate appointmentDate = dateService.updateAppointmentDate(appointmentDateDto, date);
        return ResponseEntity.ok(appointmentDate);
    }

    @DeleteMapping("/date/{localDate}/delete")
    public ResponseEntity<Void> deleteAppointmentDate(@PathVariable("localDate") LocalDateTime date) {
        dateService.deleteAppointmentDate(date);
        return ResponseEntity.noContent().build();
    }
}
