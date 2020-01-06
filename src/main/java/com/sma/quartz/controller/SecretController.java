package com.sma.quartz.controller;

import com.sma.quartz.config.SecurityRoles;
import com.sma.quartz.security.jtw.AuthorizationCheck;
import com.sma.quartz.security.jtw.model.Client;
import com.sma.quartz.security.jtw.model.ClientRole;
import com.sma.quartz.security.jtw.model.Secret;
import com.sma.quartz.security.jtw.repository.ClientRepository;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/secrets")
public class SecretController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecretController.class);

    @Value("${debug:true}")
    private boolean debug;

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
    @AuthorizationCheck(roles = { SecurityRoles.ROLE_MOBILE_CLIENT })
    public String test() {
        return "TESTING";
    }

    private ClientRole roleUser() {
        final ClientRole role = new ClientRole();
        role.setRole(SecurityRoles.ROLE_MOBILE_CLIENT);
        return role;
    }

    private ClientRole roleAdminUser() {
        final ClientRole role = new ClientRole();
        role.setRole(SecurityRoles.ROLE_BACKOFFICE_ADMIN);
        return role;
    }
}
