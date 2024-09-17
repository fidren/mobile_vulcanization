package pl.mobilevulcanization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mobilevulcanization.dto.ClientDto;
import pl.mobilevulcanization.model.Client;
import pl.mobilevulcanization.service.ClientService;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClient() {
        List<Client> clientList = clientService.getAllClients();
        return ResponseEntity.ok(clientList);
    }
    @GetMapping("/all/{date}")
    public ResponseEntity<List<Client>> getAllClientByDate(@PathVariable("date") LocalDate date) {
        try {
            List<Client> clientList = clientService.getClientsByDate(date);
            return ResponseEntity.ok(clientList);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("client/{clientId}/client")
    public ResponseEntity<Client> getClientById(@PathVariable("clientId") Long id) {
        try {
            Client client = clientService.getClient(id);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Client> addClient(@RequestBody ClientDto clientDto) {
        try {
            Client client = clientService.addClient(clientDto);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/client/{clientId}/update")
    public ResponseEntity<Void> updateClient(@RequestBody ClientDto clientDto, @PathVariable("clientId") Long id) {
        try {
            clientService.updateClient(clientDto, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/client/{clientId}/delete")
    public ResponseEntity<Void> deleteClient(@PathVariable("clientId") Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
