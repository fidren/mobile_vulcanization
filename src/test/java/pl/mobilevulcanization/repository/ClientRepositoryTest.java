package pl.mobilevulcanization.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.model.Client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DateRepository dateRepository;

    @Test
    public void saveClientTest() {
        //Arrange
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(true)
                .build();

        Client client = Client.builder()
                .address("Kielce Długa 44")
                .category("Wyważanie kół")
                .dateOfAppointment(appointmentDate.getDate())
                .description("Lewe oba koła")
                .email("nowak@pl.pl")
                .name("Adam Nowak")
                .phone("234 567 564")
                .clientType("person")
                .build();

        //Act
        dateRepository.save(appointmentDate);
        Client savedClient = clientRepository.save(client);

        //Assert
        assertNotNull(savedClient);
        assertThat(savedClient.getId()).isGreaterThan(0);
    }

    @Test
    public void deleteClientTest() {
        Client companyClient = Client.builder()
                .address("Kielce Żytnia 15")
                .description("Wymiana opon we flocie")
                .email("budim@company.pl")
                .name("Budim")
                .phone("234 567 564")
                .clientType("company")
                .nip("1234567891")
                .build();

        clientRepository.save(companyClient);
        clientRepository.delete(companyClient);

        boolean clientExists = clientRepository.findById(companyClient.getId()).isPresent();

        assertFalse(clientExists);
    }

    @Test
    public void getAllClientsTest() {
        //Arrange
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(true)
                .build();

        Client personalClient = Client.builder()
                .address("Kielce Długa 44")
                .category("Wyważanie kół")
                .dateOfAppointment(appointmentDate.getDate())
                .description("Lewe oba koła")
                .email("nowak@pl.pl")
                .name("Adam Nowak")
                .phone("234 567 564")
                .clientType("person")
                .build();

        Client companyClient = Client.builder()
                .address("Kielce Żytnia 15")
                .description("Wymiana opon we flocie")
                .email("budim@company.pl")
                .name("Budim")
                .phone("234 567 564")
                .clientType("company")
                .nip("1234567891")
                .build();

        //Act
        dateRepository.save(appointmentDate);
        clientRepository.save(personalClient);
        clientRepository.save(companyClient);

        List<Client> clientList = clientRepository.findAll();

        //Assert
        assertNotNull(clientList);
        assertThat(clientList.size()).isEqualTo(2);
    }

    @Test
    public void getClientByIdTest() {
        Client companyClient = Client.builder()
                .address("Kielce Żytnia 15")
                .description("Wymiana opon we flocie")
                .email("budim@company.pl")
                .name("Budim")
                .phone("234 567 564")
                .clientType("company")
                .nip("1234567891")
                .build();

        clientRepository.save(companyClient);

        Client searchClient = clientRepository.findById(companyClient.getId()).get();

        Assertions.assertNotNull(searchClient);
    }

    @Test
    void getAllCompanyClientsTest() {
        //Arrange
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 14, 30))
                .isFree(true)
                .build();

        Client personalClient = Client.builder()
                .address("Kielce Długa 44")
                .category("Wyważanie kół")
                .dateOfAppointment(appointmentDate.getDate())
                .description("Lewe oba koła")
                .email("nowak@pl.pl")
                .name("Adam Nowak")
                .phone("234 567 564")
                .clientType("person")
                .build();

        Client companyClient = Client.builder()
                .address("Kielce Żytnia 15")
                .description("Wymiana opon we flocie")
                .email("budim@company.pl")
                .name("Budim")
                .phone("234 567 564")
                .clientType("company")
                .nip("1234567891")
                .build();

        //Act
        dateRepository.save(appointmentDate);
        clientRepository.save(personalClient);
        clientRepository.save(companyClient);

        List<Client> clientList = clientRepository.findAllCompanyClients();

        //Assert
        assertNotNull(clientList);
        assertThat(clientList.size()).isEqualTo(1);
    }

    @Test
    void getFilteredClientsTest() {
        //Arrange
        AppointmentDate appointmentDate = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 29, 14, 30))
                .isFree(true)
                .build();

        AppointmentDate appointmentDate2 = AppointmentDate.builder()
                .date(LocalDateTime.of(2024, 11, 28, 15, 0))
                .isFree(true)
                .build();

        Client personalClient = Client.builder()
                .address("Kielce Długa 44")
                .category("Wyważanie kół")
                .dateOfAppointment(appointmentDate.getDate())
                .description("Lewe oba koła")
                .email("nowak@pl.pl")
                .name("Adam Nowak")
                .phone("234 567 564")
                .clientType("person")
                .build();

        Client personalClient2 = Client.builder()
                .address("Kielce Parkowa 3")
                .category("Wyważanie kół")
                .dateOfAppointment(appointmentDate2.getDate())
                .description("Lewe oba koła")
                .email("ptak@pl.pl")
                .name("Adam Ptak")
                .phone("234 566 564")
                .clientType("person")
                .build();

        Client companyClient = Client.builder()
                .address("Kielce Żytnia 15")
                .description("Wymiana opon we flocie")
                .email("budim@company.pl")
                .name("Budim")
                .phone("234 567 564")
                .clientType("company")
                .nip("1234567891")
                .build();

        //Act
        dateRepository.save(appointmentDate);
        dateRepository.save(appointmentDate2);

        clientRepository.save(personalClient);
        clientRepository.save(personalClient2);
        clientRepository.save(companyClient);

        List<Client> clientList1 = clientRepository.findFilteredClients(null, LocalDate.of(2024, 11, 29), false);
        List<Client> clinetList2 = clientRepository.findFilteredClients(null, false);

        //Assert
        assertNotNull(clientList1);
        assertNotNull(clinetList2);
        assertThat(clientList1.size()).isEqualTo(2);
        assertThat(clinetList2.size()).isEqualTo(3);
    }
}