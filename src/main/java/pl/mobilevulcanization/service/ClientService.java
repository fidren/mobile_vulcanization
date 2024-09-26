package pl.mobilevulcanization.service;

import pl.mobilevulcanization.model.Client;
import pl.mobilevulcanization.request.AddClientRequest;
import pl.mobilevulcanization.request.UpdateClientRequest;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {
    Client addClient(AddClientRequest addClientRequest);
    void deleteClient(Long id);
    Client updateClient(UpdateClientRequest updateClientRequest, Long id);
    List<Client> getAllClients();
    List<Client> getFilteredClients(String clientType, LocalDate date, Boolean isCurrent);
}
