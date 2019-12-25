package com.sma.quartz.security.jtw;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyOperation;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sma.common.tools.exceptions.InternalBusinessException;
import com.sma.quartz.security.jtw.model.AccessToken;
import com.sma.quartz.security.jtw.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.ZoneOffset;
import java.util.*;

import static com.sma.quartz.utils.Constants.*;

@Service
@Transactional
public class TokenService {

    private static final String RSA = "RSA";

    @Value("${jwt.private.key}")
    private String privateKeyBase64String;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiry-time-in-seconds:3600}")
    private int expiryTimeInSeconds;

    private JWSSigner signer;
    private String jwks;

    @PostConstruct
    public void init() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PrivateKey privateKey = getPrivateKeyFromBase64String(privateKeyBase64String);
        signer = new RSASSASigner(privateKey);
        jwks = generateJwks(privateKey);
    }
    public AccessToken generateToken(String subject, Client client) {
        try {
            final Calendar now = Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC));
            final Date issueTime = now.getTime();

            final Calendar expirationTime = now;
            expirationTime.add(Calendar.SECOND, expiryTimeInSeconds);

            final JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(subject).issuer(issuer).
                    expirationTime(expirationTime.getTime()).
                    issueTime(issueTime).
                    notBeforeTime(issueTime).
                    jwtID(UUID.randomUUID().toString())
                    .claim(CLAIM_NAME, client.getName())
                    .claim(CLAIM_ROLES, client.getRoles())
                    .claim(CLAIM_GROUPS, client.getGroups()).build();

            final JWSHeader.Builder builder = new JWSHeader.Builder(JWSAlgorithm.RS256);
            final SignedJWT signedJWT = new SignedJWT(builder.type(JOSEObjectType.JWT).build(), claimsSet);

            signedJWT.sign(signer);

            return new AccessToken(signedJWT.serialize(), expiryTimeInSeconds);
        } catch (JOSEException e) {
            //throw new TokenException(T002, e.getMessage());
            throw new InternalBusinessException(e.getMessage(), 1000);
        }
    }

    public String getJwks() {
        return jwks;
    }

    private static String generateJwks(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey) privateKey;
        final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent());
        final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        final PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        final Set<KeyOperation> keyOperationSet = new HashSet<>();
        keyOperationSet.add(KeyOperation.SIGN);

        final JWK jwk = new RSAKey.Builder((RSAPublicKey) publicKey).keyID(UUID.randomUUID().toString()).algorithm(JWSAlgorithm.RS256).keyUse(KeyUse.SIGNATURE).keyOperations(keyOperationSet).build();

        return "{\"keys\": [  " + jwk + "  ]  }";
    }

    private static PrivateKey getPrivateKeyFromBase64String(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
        final KeyFactory kf;
        kf = KeyFactory.getInstance(RSA);
        return kf.generatePrivate(spec);
    }
}
