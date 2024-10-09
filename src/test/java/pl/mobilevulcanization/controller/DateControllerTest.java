package pl.mobilevulcanization.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.repository.DateRepository;
import pl.mobilevulcanization.request.AddDateRequest;
import pl.mobilevulcanization.service.DateService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DateController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class DateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DateService dateService;
    @MockBean
    private DateRepository dateRepository;

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
    void getAllDatesTest() throws Exception {
        List<AppointmentDate> appointmentDates = Arrays.asList(
                appointmentDate,
                AppointmentDate.builder()
                        .date(LocalDateTime.of(2024, 12, 5, 10, 0))
                        .isFree(false)
                        .build()
        );

        when(dateRepository.findAll()).thenReturn(appointmentDates);

        mockMvc.perform(get("/allDates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].date").value("2024-11-28T14:30:00"))
                .andExpect(jsonPath("$[0].free").value(true))
                .andExpect(jsonPath("$[1].date").value("2024-12-05T10:00:00"))
                .andExpect(jsonPath("$[1].free").value(false));
    }

    @Test
    void addAppointmentDateTest() throws Exception {
        setUpAddDateRequest();

        when(dateService.addAppointmentDate(addDateRequest)).thenReturn(appointmentDate);

        mockMvc.perform(post("/addDate")
                        .param("date", "2024-11-28T14:30:00")
                .param("isFree", "true")
                .flashAttr("dateRequest", addDateRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dates"));
        //.andDo(MockMvcResultHandlers.print())
    }

    @Test
    void updateAppointmentDateTest() throws Exception {
        when(dateService.updateAppointmentDate("true", appointmentDate.getDate())).thenReturn(appointmentDate);

        mockMvc.perform(put("/date/{localDate}/update", "2024-11-28T14:30:00")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value("2024-11-28T14:30:00"))
                .andExpect(jsonPath("$.free").value(true));
    }

    @Test
    void deleteAppointmentDateTest() throws Exception {
        doNothing().when(dateService).deleteAppointmentDate(appointmentDate.getDate());

        mockMvc.perform(delete("/date/{localDate}/delete", "2024-11-28T14:30:00")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}