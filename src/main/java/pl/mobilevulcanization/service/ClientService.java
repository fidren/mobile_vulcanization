package pl.mobilevulcanization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mobilevulcanization.exception.ResourceNotFoundException;
import pl.mobilevulcanization.model.AppointmentDate;
import pl.mobilevulcanization.model.Client;
import pl.mobilevulcanization.repository.ClientRepository;
import pl.mobilevulcanization.repository.DateRepository;
import pl.mobilevulcanization.request.AddClientRequest;
import pl.mobilevulcanization.request.UpdateClientRequest;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService{
    private final ClientRepository clientRepository;
    private final DateRepository dateRepository;

    @Transactional
    public Client addClient(AddClientRequest addClientRequest) {
        Client client;

        if(addClientRequest.clientType().equals("person")) {
            //Physical client
            LocalDateTime localDateTime = convertToLocalDateTime(addClientRequest.appointmentDate());
            Optional<AppointmentDate> appointmentDateEntity = Optional.ofNullable(dateRepository.findByDate(localDateTime));

            if(appointmentDateEntity.isPresent() && appointmentDateEntity.get().isFree()) {
                client = clientRepository.save(mapPhysicalClientToEntity(addClientRequest, localDateTime));

                //change status date
                AppointmentDate appointmentDate = appointmentDateEntity.get();
                appointmentDate.setFree(false);
                dateRepository.save(appointmentDate);
            } else {
                throw new IllegalStateException("Selected appointment date is no longer available.");
            }
        } else {
            //Company client
            client = clientRepository.save(mapCompanyClientToEntity(addClientRequest));
        }

        return client;
    }

    private Client mapPhysicalClientToEntity(AddClientRequest addClientRequest, LocalDateTime appointmentDate) {
        return new Client(
                appointmentDate,
                addClientRequest.name().substring(0, addClientRequest.name().length() - 1),
                addClientRequest.email().substring(0, addClientRequest.email().length() - 1),
                addClientRequest.phone().substring(0, addClientRequest.phone().length() - 1),
                addClientRequest.address().substring(0, addClientRequest.address().length() - 1),
                addClientRequest.serviceCategory(),
                addClientRequest.problemDescription().substring(0, addClientRequest.problemDescription().length() - 1),
                addClientRequest.clientType(),
                null
        );
    }

    private Client mapCompanyClientToEntity(AddClientRequest addClientRequest) {
        return new Client(
                null,
                addClientRequest.name().substring(1),
                addClientRequest.email().substring(1),
                addClientRequest.phone().substring(1),
                addClientRequest.address().substring(1),
                null,
                addClientRequest.problemDescription().substring(1),
                addClientRequest.clientType(),
                addClientRequest.nip()
        );
    }

    private LocalDateTime convertToLocalDateTime(String appointmentDate) {
        Instant instant = Instant.parse(appointmentDate);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Europe/Warsaw"));
        return zonedDateTime.toLocalDateTime();
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
        if(client.getClientType().equals("person")){
            Optional<AppointmentDate> appointmentDateEntity = Optional.ofNullable(dateRepository.findByDate(client.getDateOfAppointment()));
            clientRepository.delete(client);
            appointmentDateEntity.ifPresent(dateRepository::delete);
        } else {
            clientRepository.delete(client);
        }
    }

    public Client updateClient(UpdateClientRequest updateClientRequest, Long id) {
        Client client = clientRepository.findById(id)
                .map(existingClient -> updateExistingClient(existingClient, updateClientRequest))
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        return clientRepository.save(client);
    }

    private Client updateExistingClient(Client existingClient, UpdateClientRequest updateClientRequest) {
        existingClient.setName(updateClientRequest.name());
        existingClient.setEmail(updateClientRequest.email());
        existingClient.setPhone(updateClientRequest.phone());
        existingClient.setAddress(updateClientRequest.address());
        existingClient.setDescription(updateClientRequest.problemDescription());
        existingClient.setNip(updateClientRequest.nip());
        return existingClient;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> getFilteredClients(String clientType, LocalDate date, Boolean isCurrent) {
        if(clientType == null && date == null && !isCurrent) {
            return clientRepository.findAll();
        }

        if(clientType != null && clientType.equals("company")){
            return clientRepository.findAllCompanyClients();
        }

        if(date == null){
            return clientRepository.findFilteredClients(clientType, isCurrent);
        }

        return clientRepository.findFilteredClients(clientType, date, isCurrent);
    }
}
