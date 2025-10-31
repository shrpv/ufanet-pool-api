package ru.ufanet.pool_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ufanet.pool_api.dto.ClientDTO;
import ru.ufanet.pool_api.dto.ClientNameDTO;
import ru.ufanet.pool_api.dto.ClientUpdateRequest;
import ru.ufanet.pool_api.exception.BadRequestException;
import ru.ufanet.pool_api.exception.NotFoundException;
import ru.ufanet.pool_api.mapper.ClientMapper;
import ru.ufanet.pool_api.model.Client;
import ru.ufanet.pool_api.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public List<ClientNameDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientMapper::toNameDTO).toList();
    }

    @Override
    public ClientDTO getClientById(int id) {
        return clientRepository.findById(id).map(ClientMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Not found client with id " + id));
    }

    @Transactional
    @Override
    public ClientDTO createClient(ClientUpdateRequest request) {
        if (clientRepository.emailExists(request.getEmail())) {
            throw new BadRequestException("Email " + request.getEmail() + " already used");
        }

        if (clientRepository.phoneExists(request.getPhone())) {
            throw new BadRequestException("Phone " + request.getPhone() + " already used");
        }

        return ClientMapper.toDTO(clientRepository.save(ClientMapper.toClient(request)));
    }

    @Transactional
    @Override
    public ClientDTO updateClient(int id, ClientUpdateRequest request) {
        if (clientRepository.emailExists(request.getEmail())) {
            throw new BadRequestException("Email " + request.getEmail() + " already used");
        }

        if (clientRepository.phoneExists(request.getPhone())) {
            throw new BadRequestException("Phone " + request.getPhone() + " already used");
        }

        Client client = ClientMapper.toClient(request);
        client.setId(id);
        return ClientMapper.toDTO(clientRepository.update(client));
    }
}
