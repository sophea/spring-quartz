package com.sma.quartz.security.jtw;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

public class JwtSecurityService {

    @Value("${core.authorization.jwks.url}")
    private String jwksUrl;

    private JWKSource jwkSource;

    @PostConstruct
    public void init() throws MalformedURLException {
        jwkSource = new RemoteJWKSet(new URL(jwksUrl), new DefaultResourceRetriever(2000, 2000));
    }

    /**
     * verify jwt token and get JWTClaimsSet object
     * */
    public JWTClaimsSet getClaimsSetFromToken(String accessToken) throws ParseException, JOSEException, BadJOSEException {
        final ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
        final JWSKeySelector keySelector = new JWSVerificationKeySelector(JWSAlgorithm.RS256, jwkSource);
        jwtProcessor.setJWSKeySelector(keySelector);
        return jwtProcessor.process(accessToken, null);
    }
}
