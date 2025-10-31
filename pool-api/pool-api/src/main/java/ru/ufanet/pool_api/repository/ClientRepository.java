package ru.ufanet.pool_api.repository;

import ru.ufanet.pool_api.model.Client;
import ru.ufanet.pool_api.model.ClientName;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);

    Client update(Client client);

    List<ClientName> findAll();

    Optional<Client> findById(int id);

    boolean emailExists(String email);

    boolean phoneExists(String phone);
}
