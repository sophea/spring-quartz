package com.sma.quartz.security.jtw.client;

public class SecurityApiBuilder {
    private String secApiUrl;
    private String clientId;
    private String clientSecret;

    private SecurityApiBuilder() {
    }

    public static SecurityApiBuilder newBuilder() {
        return new SecurityApiBuilder();
    }

    public SecurityApiBuilder withSecApiUrl(String secApiUrl) {
        this.secApiUrl = secApiUrl;
        return this;
    }
    public SecurityApiBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public SecurityApiBuilder withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public SecurityApi build() {
        return new SecurityApiServiceImp(secApiUrl, clientId, clientSecret);
    }
}
