package com.sma.quartz.controller;

import com.sma.common.tools.exceptions.AuthenticationBusinessException;
import com.sma.common.tools.exceptions.InvalidParametersBusinessException;
import com.sma.quartz.security.jtw.ClientService;
import com.sma.quartz.security.jtw.TokenService;
import com.sma.quartz.security.jtw.model.AccessToken;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.sma.quartz.utils.Constants.GRANT_TYPE_CLIENT_CREDENTIAL;

@RestController
public class TokenController {

    private ClientService clientService;
    private TokenService tokenService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

   // @SwaggerPublicApi
    @ApiOperation(value = "request a JWT access token")
    @PostMapping(value = "/oauth2/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AccessToken getAccessToken(@ApiParam(value = "must be set to client_credentials", required = true) @RequestParam("grant_type") String grantType, @ApiParam(value = "the client identifier issued to the client", required = true) @RequestParam("client_id") String clientId, @ApiParam(value = "the client secret", required = true) @RequestParam("client_secret") String clientSecret) {
        if (!GRANT_TYPE_CLIENT_CREDENTIAL.equals(grantType)) {
            //throw new TokenException(T003, String.format(GRANT_TYPE_NOT_SUPPORTED, grantType));
            throw new InvalidParametersBusinessException(String.format("Grant Type %s not supported.", grantType, 400));
        }
        try {
            return tokenService.generateToken(clientId, clientService.getClient(clientId, clientSecret));
        } catch (Exception e) {
            throw new AuthenticationBusinessException("The username or password is not correct");
        }
    }
}
