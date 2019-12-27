package com.sma.quartz.security.jtw.client;

import com.sma.common.tools.exceptions.InternalBusinessException;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @Author : Mak Sophea
 */
public class SecurityApiServiceImp extends AbstractApiImplementation implements SecurityApi {

    private Logger logger = LoggerFactory.getLogger(SecurityApiServiceImp.class);
    private static final int BUFFER_IN_SECONDS = 30;
    private String secApiUrl;
    private String clientId;
    private String clientSecret;
    private TokenWithExpiryTime tokenWithExpiryTime;

    public SecurityApiServiceImp(String secApiUrl, String clientId, String clientSecret) {
        super();
        if(secApiUrl == null || secApiUrl.isEmpty()
                || clientId == null || clientId.isEmpty()
                || clientSecret == null || clientSecret.isEmpty()) {
            throw new InternalBusinessException("configuration error");
        }
        this.secApiUrl = secApiUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String getAccessToken() {
        ZonedDateTime now = getNow();
        return Optional.ofNullable(tokenWithExpiryTime)
                .filter(tokenWithExpiryTime -> {
                    boolean isValid = tokenWithExpiryTime.getExpiryTime()
                            .isAfter(now.plusSeconds(BUFFER_IN_SECONDS));
                    if(isValid && logger.isInfoEnabled()) {
                        logger.info("using cached access token, expired at " + tokenWithExpiryTime.getExpiryTime().toString());
                    }
                    return isValid;
                })
                .orElseGet(() -> {
                    TokenResponse response = requestToken();
                    ZonedDateTime newExpiryDate = now.plusSeconds(response.getExpiresIn());
                    tokenWithExpiryTime =
                            new TokenWithExpiryTime(response.getAccessToken(), newExpiryDate);
                    if(logger.isInfoEnabled()) {
                        logger.info("got new access token, expired at " + newExpiryDate.toString());
                    }
                    return tokenWithExpiryTime;
                }).getToken();
    }

    protected TokenResponse requestToken() {
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .add("client_id", clientId)
                    .add("client_secret", clientSecret)
                    .build();

            Request request = new Request.Builder()
                    .url(secApiUrl)
                    .post(formBody)
                    .build();

            return gson.fromJson(getResponseAsString(request), TokenResponse.class);
        }
        catch(Exception e) {
            throw new InternalBusinessException("something wrong with service", e);
        }
    }

    public static ZonedDateTime getNow() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }

}
