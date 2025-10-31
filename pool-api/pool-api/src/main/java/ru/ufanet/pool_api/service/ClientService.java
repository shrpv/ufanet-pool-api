package ru.ufanet.pool_api.service;

import ru.ufanet.pool_api.dto.ClientDTO;
import ru.ufanet.pool_api.dto.ClientNameDTO;
import ru.ufanet.pool_api.dto.ClientUpdateRequest;

import java.util.List;

public interface ClientService {
    List<ClientNameDTO> getClients();

    ClientDTO getClientById(int id);

    ClientDTO createClient(ClientUpdateRequest request);

    ClientDTO updateClient(int id, ClientUpdateRequest request);
}
