package com.sma.security.controller;

import com.sma.security.config.SecurityRoles;
import com.sma.security.entity.Client;
import com.sma.security.entity.ClientRole;
import com.sma.security.entity.Secret;
import com.sma.security.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/secrets")
public class SecretController {

    @Autowired
    private ClientRepository clientRepository;


    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public Client createNormalUser(@RequestParam String clientId,
                                 @RequestParam String name ,
                                 @RequestParam String secret) {

        final Client client = new Client();

        client.setId(clientId);
        client.setName(name);
        client.setPasswordHash(new Secret(secret).getHash());
        client.setClientRoles(Arrays.asList(roleUser()));
        clientRepository.save(client);
        return client;
    }

    @PostMapping(value = "/adminuser", produces = MediaType.APPLICATION_JSON_VALUE)
    public Client createAdminUser(@RequestParam String clientId,
                                 @RequestParam String name ,
                                 @RequestParam String secret) {

        final Client client = new Client();

        client.setId(clientId);
        client.setName(name);
        client.setPasswordHash(new Secret(secret).getHash());
        client.setClientRoles(Arrays.asList(roleUser(), roleAdminUser()));
        clientRepository.save(client);
        return client;
    }

    private ClientRole roleUser() {
        final ClientRole role = new ClientRole();
        role.setRole(SecurityRoles.ROLE_USER);
        return role;
    }

    private ClientRole roleAdminUser() {
        final ClientRole role = new ClientRole();
        role.setRole(SecurityRoles.ROLE_BACKOFFICE_ADMIN);
        return role;
    }
}
