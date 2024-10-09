package pl.mobilevulcanization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.mobilevulcanization.model.Client;
import pl.mobilevulcanization.request.AddClientRequest;
import pl.mobilevulcanization.request.UpdateClientRequest;
import pl.mobilevulcanization.service.ClientService;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/allClients")
    public ResponseEntity<List<Client>> getAllClient() {
        List<Client> clientList = clientService.getAllClients();
        return ResponseEntity.ok(clientList);
    }

    @GetMapping("/filteredClients")
    public ResponseEntity<List<Client>> getFilteredClient(@RequestParam(required = false) String clientType,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                          @RequestParam(required = false) Boolean isCurrent) {
        List<Client> clientList = clientService.getFilteredClients(clientType, date, isCurrent);
        return ResponseEntity.ok(clientList);
    }

    @PostMapping("/addClient")
    public ModelAndView addClient(@ModelAttribute("clientRequest") AddClientRequest clientRequest) {
        clientService.addClient(clientRequest);
        return new ModelAndView("redirect:/appointment-success");
    }

    @PutMapping("/client/{clientId}/update")
    public ResponseEntity<Client> updateClient(@RequestBody UpdateClientRequest updateClientRequest, @PathVariable("clientId") Long id) {
        Client client = clientService.updateClient(updateClientRequest, id);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/client/{clientId}/delete")
    public ResponseEntity<Void> deleteClient(@PathVariable("clientId") Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
