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
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final DateRepository dateRepository;

    @Transactional
    @Override
    public Client addClient(AddClientRequest addClientRequest) {
        Client client;

        if(addClientRequest.getClientType().equals("person")) {
            //Physical client
            LocalDateTime localDateTime = convertToLocalDateTime(addClientRequest.getAppointmentDate());
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
                addClientRequest.getName().substring(0, addClientRequest.getName().length() - 1),
                addClientRequest.getEmail().substring(0, addClientRequest.getEmail().length() - 1),
                addClientRequest.getPhone().substring(0, addClientRequest.getPhone().length() - 1),
                addClientRequest.getAddress().substring(0, addClientRequest.getAddress().length() - 1),
                addClientRequest.getServiceCategory(),
                addClientRequest.getProblemDescription().substring(0, addClientRequest.getProblemDescription().length() - 1),
                addClientRequest.getClientType(),
                null
        );
    }

    private Client mapCompanyClientToEntity(AddClientRequest addClientRequest) {
        return new Client(
                null,
                addClientRequest.getName().substring(1),
                addClientRequest.getEmail().substring(1),
                addClientRequest.getPhone().substring(1),
                addClientRequest.getAddress().substring(1),
                null,
                addClientRequest.getProblemDescription().substring(1),
                addClientRequest.getClientType(),
                addClientRequest.getNip()
        );
    }

    private LocalDateTime convertToLocalDateTime(String appointmentDate) {
        Instant instant = Instant.parse(appointmentDate);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("Europe/Warsaw"));
        return zonedDateTime.toLocalDateTime();
    }

    @Override
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

    @Override
    public Client updateClient(UpdateClientRequest updateClientRequest, Long id) {
        Client client = clientRepository.findById(id)
                .map(existingClient -> updateExistingClient(existingClient, updateClientRequest))
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        return clientRepository.save(client);
    }

    private Client updateExistingClient(Client existingClient, UpdateClientRequest updateClientRequest) {
        existingClient.setName(updateClientRequest.getName());
        existingClient.setEmail(updateClientRequest.getEmail());
        existingClient.setPhone(updateClientRequest.getPhone());
        existingClient.setAddress(updateClientRequest.getAddress());
        existingClient.setDescription(updateClientRequest.getProblemDescription());
        existingClient.setNip(updateClientRequest.getNip());
        return existingClient;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
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
