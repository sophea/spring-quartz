package com.sma.quartz.security.jtw.client;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponse {
    private String accessToken;
    private int expiresIn;
    private String tokenType;
}
