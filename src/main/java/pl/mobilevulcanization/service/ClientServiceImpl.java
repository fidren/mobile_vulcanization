package pl.mobilevulcanization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.mobilevulcanization.dto.ClientDto;
import pl.mobilevulcanization.model.Client;
import pl.mobilevulcanization.repository.ClientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private  final ClientRepository clientRepository;

    @Override
    public Client addClient(ClientDto clientDto) {
        return clientRepository.save(mapToEntity(clientDto));
    }

    private Client mapToEntity(ClientDto clientDto) {
        return new Client(
                clientDto.getAppointmentDate(),
                clientDto.getName(),
                clientDto.getEmail(),
                clientDto.getPhone(),
                clientDto.getAddress(),
                clientDto.getCategory(),
                clientDto.getDescription()
        );
    }

    @Override
    public Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client with id " + id + " not found"));
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.findById(id)
                .ifPresentOrElse(client -> clientRepository.delete(client), () -> new RuntimeException("Client with id " + id + " not found"));
    }

    @Override
    public void updateClient(ClientDto clientDto, Long id) {
        clientRepository.findById(id)
                .map(existingClient -> updateExistingClient(existingClient, clientDto))
                .map(clientRepository::save)
                .orElseThrow(() -> new RuntimeException("Client with id " + id + " not found"));
    }

    private Client updateExistingClient(Client existingClient, ClientDto clientDto) {
        existingClient.setName(clientDto.getName());
        existingClient.setEmail(clientDto.getEmail());
        existingClient.setPhone(clientDto.getPhone());
        existingClient.setAddress(clientDto.getAddress());
        existingClient.setCategory(clientDto.getCategory());
        existingClient.setDescription(clientDto.getDescription());
        return existingClient;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> getClientsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00 tego dnia
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999999999); // 23:59:59.999999 tego dnia

        return clientRepository.findClientsByDateRange(startOfDay, endOfDay);
    }
}
