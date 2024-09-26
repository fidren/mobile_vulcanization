package pl.mobilevulcanization.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.request.AddDateRequest;
import pl.mobilevulcanization.service.DateService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
public class DateController {
    private final DateService dateService;

    //used in admin panel /dates
    @GetMapping("/allDates")
    public ResponseEntity<List<AppointmentDate>> getAllDates() {
        List<AppointmentDate> datesList = dateService.getAllAppointmentDates();
        return ResponseEntity.ok(datesList);
    }

    //used in admin panel /dates
    @GetMapping("/filteredDates")
    public ResponseEntity<List<AppointmentDate>> getFilteredDates(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                                  @RequestParam(required = false) Boolean isCurrent,
                                                                  @RequestParam(required = false) Boolean isFree) {

        List<AppointmentDate> datesList = dateService.getFilteredDates(date, isCurrent, isFree);
        return ResponseEntity.ok(datesList);
    }

    //used in making appointment
    @GetMapping("/allDates/free/current/{localDate}")
    public ResponseEntity<List<AppointmentDate>> getAllFreeDatesByDate(@PathVariable("localDate") LocalDate date) {
        List<AppointmentDate> datesList = dateService.getAllFreeCurrentAppointmentDatesByDate(date);
        return ResponseEntity.ok(datesList);
    }

    //used in admin panel /dates
    @PostMapping("/addDate")
    public ModelAndView addAppointmentDate(@ModelAttribute("dateRequest") AddDateRequest addDateRequest) {
        dateService.addAppointmentDate(addDateRequest);
        return new ModelAndView("redirect:/dates");
    }

    //used in admin panel /dates
    @PutMapping("/date/{localDate}/update")
    public ResponseEntity<AppointmentDate> updateAppointmentDate(@RequestBody String isFreeField, @PathVariable("localDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime originalDate) {
        AppointmentDate appointmentDate = dateService.updateAppointmentDate(isFreeField, originalDate);
        return ResponseEntity.ok(appointmentDate);
    }

    //used in admin panel /dates
    @DeleteMapping("/date/{localDate}/delete")
    public ResponseEntity<Void> deleteAppointmentDate(@PathVariable("localDate") LocalDateTime date) {
        dateService.deleteAppointmentDate(date);
        return ResponseEntity.noContent().build();
    }
}
