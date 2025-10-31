package ru.ufanet.pool_api.mapper;

import ru.ufanet.pool_api.dto.ClientDTO;
import ru.ufanet.pool_api.dto.ClientNameDTO;
import ru.ufanet.pool_api.dto.ClientUpdateRequest;
import ru.ufanet.pool_api.model.Client;
import ru.ufanet.pool_api.model.ClientName;

public class ClientMapper {
    public static ClientNameDTO toNameDTO(ClientName clientName) {
        return new ClientNameDTO(
                clientName.getId(),
                clientName.getName()
        );
    }

    public static ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getPhone()
        );
    }

    public static Client toClient(ClientUpdateRequest request) {
        Client client = new Client();
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        return client;
    }
}
