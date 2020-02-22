package com.sma.security.entity;

import com.sma.security.utils.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Secret {
    private String clientSecret;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public Secret() {
        this.clientSecret = PasswordGenerator.generate();
    }

    public Secret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getHash() {
        return bCryptPasswordEncoder.encode(clientSecret);
    }

    public boolean isMatched(String passwordHash) {
        return bCryptPasswordEncoder.matches(clientSecret, passwordHash);
    }
}
