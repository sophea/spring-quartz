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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Secret createPasswordAndHash() {
        final Secret responseBody = new Secret();
        return responseBody;
    }

    @PostMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Client createClientId(@RequestParam String clientId, @RequestParam String name ,@RequestParam String secret ) {

        final Client client = new Client();

        client.setId(clientId);
        client.setName(name);
        client.setPasswordHash(new Secret(secret).getHash());
        client.setClientRoles(Arrays.asList(roleUser(), roleAdminUser()));
        clientRepository.save(client);
        //clientRepository.save(client);
        return client;
    }

    @GetMapping("/test")
    public String test() {
        return "TESTING";
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
