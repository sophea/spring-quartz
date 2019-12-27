package com.sma.quartz.security.jtw.client;

import java.time.ZonedDateTime;

public class TokenWithExpiryTime {
    private String token;
    private ZonedDateTime expiryTime;

    public TokenWithExpiryTime(String token, ZonedDateTime expiryTime) {
        this.token = token;
        this.expiryTime = expiryTime;
    }

    public String getToken() {
        return token;
    }

    public ZonedDateTime getExpiryTime() {
        return expiryTime;
    }
}
