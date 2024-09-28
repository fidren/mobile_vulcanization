package pl.mobilevulcanization.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.model.Client;
import pl.mobilevulcanization.repository.ClientRepository;
import pl.mobilevulcanization.repository.DateRepository;
import pl.mobilevulcanization.request.AddClientRequest;
import pl.mobilevulcanization.request.UpdateClientRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private DateRepository dateRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

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
    void addCompanyClientTest() {
        when(clientRepository.save(Mockito.any(Client.class))).thenReturn(companyClient);

        //Act
        Client savedClient = clientService.addClient(addCompanyClientRequest);

        //Assert
        assertNotNull(savedClient);
    }

    @Test
    void addPersonClientTest() {
        when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);
        when(dateRepository.save(Mockito.any(AppointmentDate.class))).thenReturn(appointmentDate);
        when(dateRepository.findByDate(Mockito.any(LocalDateTime.class))).thenReturn(appointmentDate);

        //Act
        Client savedClient = clientService.addClient(addClientRequest);

        //Assert
        assertNotNull(savedClient);
    }

    @Test
    void addPersonClientFailTest() {
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(false)
                .build();

        when(dateRepository.findByDate(Mockito.any(LocalDateTime.class))).thenReturn(appointmentDate);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            clientService.addClient(addClientRequest);
        });

        //Assert
        assertEquals("Selected appointment date is no longer available.", exception.getMessage());
    }

    @Test
    void deleteCompanyClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.ofNullable(companyClient));

        assertAll(() -> clientService.deleteClient(1L));
    }

    @Test
    void deletePersonClient() {
        when(dateRepository.findByDate(Mockito.any(LocalDateTime.class))).thenReturn(appointmentDate);
        when(clientRepository.findById(1L)).thenReturn(Optional.ofNullable(client));

        assertAll(() -> clientService.deleteClient(1L));
    }

    @Test
    void updateClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.ofNullable(companyClient));
        when(clientRepository.save(Mockito.any(Client.class))).thenReturn(companyClient);

        Client updatedClient = clientService.updateClient(updateClientRequest, 1L);

        assertNotNull(updatedClient);
    }

    @Test
    void getAllClients() {
        List<Client> clients = Arrays.asList(client, companyClient);

        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> allClients = clientService.getAllClients();

        assertNotNull(allClients);
        assertEquals(2, allClients.size());
        assertEquals(clients, allClients);
    }
}