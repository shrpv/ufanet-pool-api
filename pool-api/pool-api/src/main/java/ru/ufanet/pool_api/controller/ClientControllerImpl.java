package ru.ufanet.pool_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ufanet.pool_api.dto.ClientDTO;
import ru.ufanet.pool_api.dto.ClientNameDTO;
import ru.ufanet.pool_api.dto.ClientUpdateRequest;
import ru.ufanet.pool_api.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/v0/pool/clients")
@RequiredArgsConstructor
public class ClientControllerImpl implements ClientController {
    private final ClientService clientService;

    @Override
    @GetMapping
    public ResponseEntity<List<ClientNameDTO>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable int id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientUpdateRequest request) {
        return new ResponseEntity<>(clientService.createClient(request), HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable int id,
            @Valid @RequestBody ClientUpdateRequest request
    ) {
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }
}
