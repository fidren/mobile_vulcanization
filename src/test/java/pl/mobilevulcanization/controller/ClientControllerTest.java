package pl.mobilevulcanization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import pl.mobilevulcanization.model.Client;
import pl.mobilevulcanization.request.AddClientRequest;
import pl.mobilevulcanization.request.UpdateClientRequest;
import pl.mobilevulcanization.service.ClientService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Client companyClient;
    private Client client;
    private AppointmentDate appointmentDate;
    private AddClientRequest addCompanyClientRequest;
    private AddClientRequest addClientRequest;
    private UpdateClientRequest updateClientRequest;

    @BeforeEach
    void setUp() {
        companyClient = Client.builder()
                .address("Kielce Żytnia 15")
                .description("Wymiana opon we flocie")
                .email("budim@company.pl")
                .name("Budim")
                .phone("234 567 564")
                .clientType("company")
                .nip("1234567891")
                .build();
        appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(true)
                .build();
        client = Client.builder()
                .address("Kielce Długa 44")
                .category("Wyważanie kół")
                .dateOfAppointment(appointmentDate.getDate())
                .description("Lewe oba koła")
                .email("nowak@pl.pl")
                .name("Adam Nowak")
                .phone("234 567 564")
                .clientType("person")
                .build();
        addCompanyClientRequest = AddClientRequest.builder()
                .address("Kielce Żytnia 15")
                .problemDescription("Wymiana opon we flocie")
                .email("budim@company.pl")
                .name("Budim")
                .phone("234 567 564")
                .clientType("company")
                .nip("1234567891")
                .build();
        addClientRequest = AddClientRequest.builder()
                .address("Kielce Długa 44")
                .serviceCategory("Wyważanie kół")
                //2024-09-29T08:29:00.000Z == 2024-09-29 10:29 due to Time Zone
                .appointmentDate("2024-11-28T12:30:00.000Z")
                .problemDescription("Lewe oba koła")
                .email("nowak@pl.pl")
                .name("Adam Nowak")
                .phone("234 567 564")
                .clientType("person")
                .build();
        updateClientRequest = UpdateClientRequest.builder()
                .address("Kielce Żytnia 15")
                .problemDescription("Wymiana opon we flocie")
                .email("budim@company.pl")
                .name("Budim")
                .phone("234 567 564")
                .nip("1234567891")
                .build();
    }

    @Test
    void getAllClientTest() throws Exception {
        List<Client> clients = Arrays.asList(
                client,
                companyClient
        );

        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/allClients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    void addCompanyClientTest() throws Exception {
        when(clientService.addClient(addCompanyClientRequest)).thenReturn(companyClient);

        mockMvc.perform(post("/addClient")
                .param("clientType", "company")
                .param("name", "Budim")
                .param("email", "budim@company.pl")
                .param("phone", "234 567 564")
                .param("address", "Kielce Żytnia 15")
                .param("problemDescription", "Wymiana opon we flocie")
                .param("nip", "1234567891")
                .flashAttr("clientRequest", addCompanyClientRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointment-success"));
    }

    @Test
    void addPersonClientTest() throws Exception {
        when(clientService.addClient(addClientRequest)).thenReturn(client);

        mockMvc.perform(post("/addClient")
                        .param("clientType", "person")
                        .param("appointmentDate", "2024-11-28T12:30:00.000Z")
                        .param("name", "Adam Nowak")
                        .param("email", "nowak@pl.pl")
                        .param("phone", "234 567 564")
                        .param("address", "Kielce Żytnia 15")
                        .param("problemDescription", "Lewe oba koła")
                        .param("serviceCategory", "Wyważanie kół")
                        .flashAttr("clientRequest", addClientRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointment-success"));
    }

    @Test
    void updateClientTest() throws Exception {
        when(clientService.updateClient(updateClientRequest, 1L)).thenReturn(companyClient);

        mockMvc.perform(put("/client/{clientId}/update", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateClientRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.address").value("Kielce Żytnia 15"))
                .andExpect(jsonPath("$.description").value("Wymiana opon we flocie"))
                .andExpect(jsonPath("$.email").value("budim@company.pl"))
                .andExpect(jsonPath("$.name").value("Budim"))
                .andExpect(jsonPath("$.phone").value("234 567 564"))
                .andExpect(jsonPath("$.clientType").value("company"))
                .andExpect(jsonPath("$.nip").value("1234567891"));
    }

    @Test
    void deleteClientTest() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/client/{clientId}/delete", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}