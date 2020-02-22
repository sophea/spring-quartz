package com.sma.security.service;

import com.sma.common.tools.exceptions.ItemNotFoundBusinessException;
import com.sma.security.entity.Client;
import com.sma.security.entity.Secret;
import com.sma.security.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public Client getClient(String clientId, String clientSecret) {

        return clientRepository.findById(clientId).filter(client -> new Secret(clientSecret)
                .isMatched(client.getPasswordHash())).orElseThrow(() -> new ItemNotFoundBusinessException("Client id and client secret do not match.", 406));
    }
}
