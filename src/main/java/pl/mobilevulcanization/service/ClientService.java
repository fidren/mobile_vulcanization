package pl.mobilevulcanization.service;

import pl.mobilevulcanization.dto.ClientDto;
import pl.mobilevulcanization.model.Client;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {
    Client addClient(ClientDto clientDto);
    Client getClient(Long id);
    void deleteClient(Long id);
    void updateClient(ClientDto clientDto, Long id);
    List<Client> getAllClients();
    List<Client> getClientsByDate(LocalDate date);
}
