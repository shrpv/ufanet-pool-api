package ru.ufanet.pool_api.controller;

import org.springframework.http.ResponseEntity;
import ru.ufanet.pool_api.dto.ClientDTO;
import ru.ufanet.pool_api.dto.ClientNameDTO;
import ru.ufanet.pool_api.dto.ClientUpdateRequest;

import java.util.List;

public interface ClientController {
    ResponseEntity<List<ClientNameDTO>> getClients();

    ResponseEntity<ClientDTO> getClientById(int id);

    ResponseEntity<ClientDTO> createClient(ClientUpdateRequest request);

    ResponseEntity<ClientDTO> updateClient(int id, ClientUpdateRequest request);
}
