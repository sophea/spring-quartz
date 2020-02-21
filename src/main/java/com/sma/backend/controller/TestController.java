package com.sma.backend.controller;

import com.sma.backend.config.SecurityRoles;
import com.sma.common.tools.security.jwt.AuthorizationCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secrets")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    @AuthorizationCheck(roles = { SecurityRoles.ROLE_MOBILE_CLIENT })
    public String test() {
        return "TESTING";
    }
}
